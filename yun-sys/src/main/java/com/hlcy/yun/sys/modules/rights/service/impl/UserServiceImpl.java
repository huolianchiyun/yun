package com.hlcy.yun.sys.modules.rights.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.Page;
import com.hlcy.yun.common.web.response.Meta;
import com.hlcy.yun.sys.modules.common.config.RsaProperties;
import com.hlcy.yun.sys.modules.common.enums.CodeEnum;
import com.hlcy.yun.sys.modules.rights.common.constant.RightsConstants;
import com.hlcy.yun.sys.modules.rights.mapper.UserGroupMapper;
import com.hlcy.yun.sys.modules.rights.mapper.UserMapper;
import com.hlcy.yun.sys.modules.rights.model.$do.GroupDO;
import com.hlcy.yun.sys.modules.rights.model.$do.UserDO;
import com.hlcy.yun.sys.modules.rights.model.$do.UserGroupDO;
import com.hlcy.yun.sys.modules.rights.model.criteria.UserQueryCriteria;
import com.hlcy.yun.sys.modules.rights.model.vo.UserEmailVO;
import com.hlcy.yun.sys.modules.rights.model.vo.UserPwdVO;
import com.hlcy.yun.sys.modules.rights.service.CaptchaService;
import com.hlcy.yun.sys.modules.rights.service.UserService;
import com.hlcy.yun.sys.modules.security.cache.UserInfoCache;
import com.hlcy.yun.sys.modules.security.service.OnlineUserService;
import com.hlcy.yun.common.spring.redis.RedisUtils;
import com.hlcy.yun.common.spring.security.SecurityUtils;
import com.hlcy.yun.common.utils.encodec.RsaUtils;
import com.hlcy.yun.common.page.PageInfo;
import com.hlcy.yun.common.mybatis.page.PageQueryHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static com.hlcy.yun.sys.modules.common.xcache.CacheKey.*;


@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = USER)
class UserServiceImpl implements UserService {
    @Value("${spring.redis.my.expiration-time:7200000}")
    private long expirationTime;
    @Value("${yun.user.pwd-expiration-period:90}")
    private int pwdExpirationPeriod;

    private final UserMapper userMapper;
    private final UserGroupMapper userGroupMapper;
    private final PasswordEncoder passwordEncoder;
    private final CaptchaService verificationCodeService;
    private final OnlineUserService onlineUserService;
    private final RedisUtils redisUtils;

    @Override
    @Cacheable(value = BIND_USER + USER, key = "'" + UserIdKey + "'+ #p0")
    public UserDO queryById(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    @Cacheable(value = HSet + "H:ID->USERNAME", key = "'" + UserIdKey + "'+ #p0")
    public String queryUsernameById(Long id) {
        return userMapper.selectUsernameById(id);
    }

    @Override
    @Cacheable(value = BIND_USER + USER, key = "'" + UsernameKey + "'+ #p0")
    public UserDO queryByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public PageInfo<List<UserDO>> queryByCriteria(UserQueryCriteria criteria) {
        Page<UserDO> page = PageQueryHelper.queryByCriteriaWithPage(criteria, userMapper);
        PageInfo<List<UserDO>> pageInfo = new PageInfo<>(criteria.getPageNum(), criteria.getPageSize());
        pageInfo.setTotal(page.getTotal());
        List<UserDO> result = page.getResult();
        pageInfo.setData(result);
        return pageInfo;
    }

    @Override
    public List<UserDO> queryAllByCriteriaWithNoPage(UserQueryCriteria criteria) {
        return CollectionUtil.list(false, userMapper.selectByCriteria(criteria));
    }

    @Override
    @CacheEvict(value = BIND_USER_HASH_KEY_PREFIX0, key = "#user.username")
    @Transactional(rollbackFor = Exception.class)
    public void create(UserDO user) {
        user.setAdmin(false);
        checkOperationalRights(user);
        Assert.isNull(userMapper.selectByUsername(user.getUsername()), "用户名已存在，请重新命名！");
        // 默认密码 123456
        user.setPwd(passwordEncoder.encode("123456"));
        userMapper.insert(user);
        associateDeptAndGroup(user, true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = BIND_USER_HASH_KEY_PREFIX0, key = "#user.username")
    public void update(UserDO user) {
        checkOperationalRights(user);
        userMapper.updateByPrimaryKeySelective(user);
        associateDeptAndGroup(user, false);
        UserInfoCache.cleanCacheFor(user.getUsername());
    }

    @Override
    @CacheEvict(value = BIND_USER_HASH_KEY_PREFIX0, key = "#userPwd.username")
    public void updatePwd(UserPwdVO userPwd) throws Exception {
        final String currentUsername = SecurityUtils.getCurrentUsername();
        Assert.isTrue(RightsConstants.ANONYMOUS_USER.equals(currentUsername) || userPwd.getUsername().equals(currentUsername), "只允许修改自己密码！");
        String oldPass = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, userPwd.getOldPwd());
        String newPass = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, userPwd.getNewPwd());
        UserDO userDB = queryByUsername(userPwd.getUsername());
        Assert.isTrue(passwordEncoder.matches(oldPass, userDB.getPwd()), "修改失败，旧密码错误");
        userMapper.updateByPrimaryKeySelective(new UserDO(userDB.getId(), passwordEncoder.encode(newPass), LocalDateTime.now()));
        // 清除用户token，使其重新登录
        onlineUserService.checkLoginOnUser(userDB.getUsername(), null);
        UserInfoCache.cleanCacheFor(userDB.getUsername());
    }

    @Override
    @CacheEvict(value = BIND_USER_HASH_KEY_PREFIX0, key = "#username")
    public void resetPwd(String username) {
        userMapper.resetPwd( new UserDO(username, passwordEncoder.encode("123456"), LocalDateTime.now()));
    }

    @Override
    @CacheEvict(value = BIND_USER_HASH_KEY_PREFIX0, key = "#userEmail.username")
    public void updateEmail(UserEmailVO userEmail) throws Exception {
        Assert.isTrue(userEmail.getUsername().equals(SecurityUtils.getCurrentUsername()), "只允许修改自己邮箱！");
        UserDO userDb = queryByUsername(userEmail.getUsername());
        String password = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, userEmail.getPassword());
        Assert.isTrue(passwordEncoder.matches(password, userDb.getPwd()), "密码错误");
        verificationCodeService.validate(CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey() + userEmail.getEmail(), userEmail.getCaptcha());
        userMapper.updateByPrimaryKeySelective(new UserDO(userDb.getId(), userEmail.getEmail()));
        UserInfoCache.cleanCacheFor(userDb.getUsername());
    }

    @Override
    public Meta verifyPasswordExpired(String username) {
        final UserDO userDO = userMapper.selectByUsername(username);
        final LocalDateTime pwdResetTime = userDO.getPwdResetTime();
        final LocalDate pwdResetDate = pwdResetTime != null ? pwdResetTime.toLocalDate() : userDO.getCreateTime().toLocalDate();
        final LocalDate expiredDate = pwdResetDate.plus(pwdExpirationPeriod, ChronoUnit.DAYS);
        final long difference = LocalDate.now().until(expiredDate, ChronoUnit.DAYS);
        if (difference >= 0 && difference <= 10) {
            return Meta.error(String.format("你的密码将于 %s 过期，请尽快修改, 以免影响你后续登录！", expiredDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
        }else if(difference < 0){
            return Meta.noApiRights(String.format("你的密码已经于 %s 过期， 将无法登录，请重置密码或联系管理员处理！", expiredDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
        }
        return Meta.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(Set<Long> ids) {
        Set<UserDO> users = userMapper.selectByIds(ids);
        UserDO currentUser = userMapper.selectByUsername(SecurityUtils.getCurrentUsername());
        users.forEach(e -> doCheckOperationalRights(e, currentUser));
        userMapper.deleteByIds(ids);
        // TODO　考虑是否删除该用户创建的组及该用户为组长的组？？
        userGroupMapper.deleteByUserIds(ids);
        cleanUserCache(userMapper.selectByIds(ids));
    }

    /**
     * 关联部门和组
     *
     * @param user    创建或修改的用户
     * @param isCreat 是否是创建用户
     */
    private void associateDeptAndGroup(UserDO user, boolean isCreat) {
        Set<UserGroupDO> userGroups = new HashSet<>(1);
        if (CollectionUtil.isNotEmpty(user.getGroups())) {
            userGroups = user.getGroups().stream()
                    .map(e -> new UserGroupDO(user.getId(), e.getId()))
                    .collect(Collectors.toSet());
        }
        if (user.getDeptId() != null) {
            userGroups.add(new UserGroupDO(user.getId(), user.getDeptId()));
        }
        if (CollectionUtil.isNotEmpty(userGroups)) {
            if (!isCreat) {
                userGroupMapper.deleteByUserIds(CollectionUtil.newHashSet(user.getId()));
            }
            userGroupMapper.batchInsert(userGroups);
        }
    }

    /**
     * 检查当前用户操作权限，权限不够，抛异常提示
     *
     * @param editingUser 将要被操作的用户
     */
    private void checkOperationalRights(UserDO editingUser) {
        UserDO currentUser = userMapper.selectByUsername(SecurityUtils.getCurrentUsername());
        Assert.notNull(currentUser, "修改的用户不存在！");
        if (currentUser.getAdmin() && currentUser.getId().equals(editingUser.getId())
                && !Optional.ofNullable(editingUser.getAdmin()).orElse(Boolean.TRUE)) {
            editingUser.setAdmin(true);
        }
        doCheckOperationalRights(editingUser, currentUser);
    }

    private void doCheckOperationalRights(UserDO operatingUser, UserDO currentUser) {
        if (currentUser.getAdmin()) {
            return;
        }
        // 部门权限校验：检查操作人所在部门是否高于或等于被操作用户所属部门，满足yes，不满足no
        GroupDO dept = operatingUser.getDept();
        if (Objects.nonNull(dept) && StringUtils.hasText(dept.getGroupCode())) {
            Assert.isNull(currentUser.getDept(), "你不属于一个部门，没有操作权限！");
            Assert.isTrue(currentUser.getUsername().equals(currentUser.getDept().getGroupMaster()), "你不是部门管者，没有操作权限！");
            Assert.isTrue(dept.getGroupCode().startsWith(currentUser.getDept().getGroupCode()), "你只能操作你所属部门下的用户！");
        }
    }

    private void cleanUserCache(Set<UserDO> userSet) {
        if (CollectionUtil.isNotEmpty(userSet)) {
            Set<String> usernameSet = userSet.stream().map(UserDO::getUsername).collect(Collectors.toSet());
            redisUtils.delByKeys(BIND_USER_HASH_KEY_PREFIX, usernameSet);
            usernameSet.forEach(UserInfoCache::cleanCacheFor);
        }
    }
}
