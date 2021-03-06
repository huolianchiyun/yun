package com.zhangbin.yun.yunrights.modules.rights.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.Page;
import com.sun.el.parser.BooleanNode;
import com.zhangbin.yun.yunrights.modules.common.config.RsaProperties;
import com.zhangbin.yun.yunrights.modules.common.enums.CodeEnum;
import com.zhangbin.yun.yunrights.modules.common.model.vo.PageInfo;
import com.zhangbin.yun.yunrights.modules.common.page.PageQueryHelper;
import com.zhangbin.yun.yunrights.modules.common.utils.*;
import com.zhangbin.yun.yunrights.modules.rights.mapper.UserGroupMapper;
import com.zhangbin.yun.yunrights.modules.rights.mapper.UserMapper;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.GroupDO;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.UserDO;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.UserGroupDO;
import com.zhangbin.yun.yunrights.modules.rights.model.criteria.UserQueryCriteria;
import com.zhangbin.yun.yunrights.modules.rights.model.vo.UserEmailVO;
import com.zhangbin.yun.yunrights.modules.rights.model.vo.UserPwdVO;
import com.zhangbin.yun.yunrights.modules.rights.service.UserService;
import com.zhangbin.yun.yunrights.modules.rights.service.CaptchaService;
import com.zhangbin.yun.yunrights.modules.security.cache.UserInfoCache;
import com.zhangbin.yun.yunrights.modules.security.service.OnlineUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.zhangbin.yun.yunrights.modules.common.xcache.CacheKey.*;


@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = USER)
public class UserServiceImpl implements UserService {
    @Value("${spring.redis.my.expiration-time:7200000}")
    private long expirationTime;

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
    public PageInfo<List<UserDO>> queryAllByCriteria(UserQueryCriteria criteria) {
        Page<UserDO> page = PageQueryHelper.queryAllByCriteriaWithPage(criteria, userMapper);
        PageInfo<List<UserDO>> pageInfo = new PageInfo<>(criteria.getPageNum(), criteria.getPageSize());
        pageInfo.setTotal(page.getTotal());
        List<UserDO> result = page.getResult();
        pageInfo.setData(result);
        return pageInfo;
    }

    @Override
    public List<UserDO> queryAllByCriteriaWithNoPage(UserQueryCriteria criteria) {
        return CollectionUtil.list(false, userMapper.selectAllByCriteria(criteria));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(UserDO user) {
        user.setAdmin(false);
        checkOperationalRights(user);
        Assert.isNull(userMapper.selectByUsername(user.getUsername()), "用户名已存在，请重新命名！");
        // 默认密码 123456
        user.setPwd(passwordEncoder.encode("123456"));
        userMapper.insert(user);
        associateDeptAndGroup(user, true);
        throw new RuntimeException();
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
        Assert.isTrue(userPwd.getUsername().equals(SecurityUtils.getCurrentUsername()), "只允许修改自己密码！");
        String oldPass = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, userPwd.getOldPass());
        String newPass = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, userPwd.getNewPass());
        UserDO userDB = queryByUsername(userPwd.getUsername());
        Assert.isTrue(passwordEncoder.matches(oldPass, userDB.getPwd()), "修改失败，旧密码错误");
        Assert.isTrue(!passwordEncoder.matches(newPass, userDB.getPwd()), "新密码不能与旧密码相同");
        userMapper.updateByPrimaryKeySelective(new UserDO(userDB.getId(), passwordEncoder.encode(newPass), LocalDateTime.now()));
        // 清除用户token，使其重新登录
        onlineUserService.checkLoginOnUser(userDB.getUsername(), null);
        UserInfoCache.cleanCacheFor(userDB.getUsername());
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

    @Override
    public void download(List<UserDO> userDOList, HttpServletResponse response) throws IOException {
        FileUtil.downloadExcel(Optional.ofNullable(userDOList).orElseGet(ArrayList::new)
                .stream().map(UserDO::toLinkedMap)
                .collect(Collectors.toList()), response);
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
