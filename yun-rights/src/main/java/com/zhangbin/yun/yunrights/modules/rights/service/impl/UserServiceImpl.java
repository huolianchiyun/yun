package com.zhangbin.yun.yunrights.modules.rights.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.Page;
import com.zhangbin.yun.yunrights.modules.common.config.RsaProperties;
import com.zhangbin.yun.yunrights.modules.common.enums.CodeEnum;
import com.zhangbin.yun.yunrights.modules.common.model.vo.PageInfo;
import com.zhangbin.yun.yunrights.modules.common.page.PageQueryHelper;
import com.zhangbin.yun.yunrights.modules.common.utils.*;
import com.zhangbin.yun.yunrights.modules.rights.mapper.UserGroupMapper;
import com.zhangbin.yun.yunrights.modules.rights.mapper.UserMapper;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.GroupDO;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.UserDO;
import com.zhangbin.yun.yunrights.modules.rights.model.criteria.UserQueryCriteria;
import com.zhangbin.yun.yunrights.modules.rights.model.vo.UserPwdVO;
import com.zhangbin.yun.yunrights.modules.rights.service.UserService;
import com.zhangbin.yun.yunrights.modules.rights.service.CaptchaService;
import com.zhangbin.yun.yunrights.modules.security.service.UserCacheClean;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
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


@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "user")
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserGroupMapper userGroupMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserCacheClean userCacheClean;
    private final CaptchaService verificationCodeService;
    private RedisUtils redisUtils;

    @Autowired(required = false)
    public void setRedisUtils(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    @Override
    @Cacheable(key = "'id:' + #p0")
    public UserDO queryById(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    @Cacheable(key = "'username:' + #p0")
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
    public void createUser(UserDO user) {
        checkOperationalRights(user);
        Assert.isNull(userMapper.selectByUsername(user.getUsername()), "用户名已存在，请重新命名！");
        // 默认密码 123456
        user.setPwd(passwordEncoder.encode("123456"));
        userMapper.insert(user);
    }

    @Override
    public void updateUser(UserDO user) {
        checkOperationalRights(user);
        userMapper.updateByPrimaryKeySelective(user);
        // 清理缓存
        clearCaches(user.getId(), user.getUsername());
    }

    @Override
    public void updatePwd(UserPwdVO userPwdVo) throws Exception {
        String oldPass = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, userPwdVo.getOldPass());
        String newPass = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, userPwdVo.getNewPass());
        UserDO user = queryByUsername(SecurityUtils.getCurrentUsername());
        Assert.isTrue(passwordEncoder.matches(oldPass, user.getPwd()), "修改失败，旧密码错误");
        Assert.isTrue(!passwordEncoder.matches(newPass, user.getPwd()), "新密码不能与旧密码相同");
        String username = user.getUsername();
        userMapper.updateByPrimaryKeySelective(new UserDO(username, passwordEncoder.encode(newPass), LocalDateTime.now()));
        redisUtils.del("user::username:" + username);
        flushCache(username);
    }

    @Override
    public void updateEmail(String code, UserDO user) throws Exception {
        String password = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, user.getPwd());
        UserDO userDb = queryByUsername(SecurityUtils.getCurrentUsername());
        Assert.isTrue(passwordEncoder.matches(password, userDb.getPwd()), "密码错误");
        verificationCodeService.validate(CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey() + user.getEmail(), code);
        userMapper.updateByPrimaryKeySelective(new UserDO(userDb.getUsername(), user.getEmail()));
        redisUtils.del(CacheKey.USER_NAME + userDb.getUsername());
        flushCache(userDb.getUsername());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(Set<Long> ids) {
        Set<UserDO> users = userMapper.selectByPrimaryKeys(ids);
        UserDO currentUser = userMapper.selectByUsername(SecurityUtils.getCurrentUsername());
        users.forEach(e -> checkOperationalRights(e, currentUser));
        userMapper.deleteByIds(ids);
        // TODO　考虑是否删除该用户创建的组及该用户为组长的组？？

        userGroupMapper.deleteByUserIds(ids);
        // 清理缓存
        Optional.of(userMapper.selectByIds(ids)).orElseGet(HashSet::new).forEach(e -> clearCaches(e.getId(), e.getUsername()));
    }

    @Override
    public void download(List<UserDO> userDOList, HttpServletResponse response) throws IOException {
        FileUtil.downloadExcel(Optional.of(userDOList).orElseGet(ArrayList::new).stream().map(UserDO::toLinkedMap).collect(Collectors.toList()), response);
    }

    /**
     * 检查当前用户操作权限，权限不够，抛异常提示
     *
     * @param operatingUser 将要被操作的用户
     */
    private void checkOperationalRights(UserDO operatingUser) {
        UserDO currentUser = userMapper.selectByUsername(SecurityUtils.getCurrentUsername());
        checkOperationalRights(operatingUser, currentUser);
    }

    private void checkOperationalRights(UserDO operatingUser, UserDO currentUser) {
        if (currentUser.isAdmin()) {
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

    /**
     * 清理缓存
     *
     * @param userId 用户 ID
     */
    private void clearCaches(Long userId, String userName) {
        redisUtils.del(CacheKey.USER_ID + userId);
        redisUtils.del(CacheKey.USER_NAME + userName);
        flushCache(userName);
    }

    /**
     * 清理 登陆时 用户缓存信息
     *
     * @param userName 用户名
     */
    private void flushCache(String userName) {
        userCacheClean.cleanUserCache(userName);
    }
}
