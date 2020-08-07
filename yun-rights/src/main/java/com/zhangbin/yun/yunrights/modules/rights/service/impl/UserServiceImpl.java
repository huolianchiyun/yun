package com.zhangbin.yun.yunrights.modules.rights.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.Page;
import com.zhangbin.yun.yunrights.modules.common.config.RsaProperties;
import com.zhangbin.yun.yunrights.modules.common.enums.CodeEnum;
import com.zhangbin.yun.yunrights.modules.common.exception.BadRequestException;
import com.zhangbin.yun.yunrights.modules.common.model.vo.PageInfo;
import com.zhangbin.yun.yunrights.modules.common.page.PageQueryHelper;
import com.zhangbin.yun.yunrights.modules.common.utils.*;
import com.zhangbin.yun.yunrights.modules.rights.mapper.UserMapper;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.RoleDO;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.UserDO;
import com.zhangbin.yun.yunrights.modules.rights.model.criteria.UserQueryCriteria;
import com.zhangbin.yun.yunrights.modules.rights.model.vo.UserPwdVO;
import com.zhangbin.yun.yunrights.modules.rights.service.RoleService;
import com.zhangbin.yun.yunrights.modules.rights.service.UserService;
import com.zhangbin.yun.yunrights.modules.rights.service.VerifyService;
import com.zhangbin.yun.yunrights.modules.security.service.UserCacheClean;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleService roleService;
    private final RedisUtils redisUtils;
    private final UserCacheClean userCacheClean;
    private final VerifyService verificationCodeService;

    @Override
    public UserDO queryById(Long id) {
        return null;
    }

    @Override
    public UserDO queryByUserName(String loginName) {
        return userMapper.selectByUserName(loginName);
    }

    @Override
    public PageInfo<List<UserDO>> queryAllByCriteria(UserQueryCriteria criteria) {
        Page<UserDO> page = PageQueryHelper.queryAllByCriteriaWithPage(criteria, userMapper);
        PageInfo<List<UserDO>> pageInfo = new PageInfo<>(criteria.getPageNum(), criteria.getPageSize());
        pageInfo.setTotal(page.getTotal());
        List<UserDO> result = page.getResult();
        pageInfo.setData(result);
        return pageInfo;
        // TODO 数据权限
//        if (!ObjectUtils.isEmpty(criteria.getDeptId())) {
//            criteria.getDeptIds().add(criteria.getDeptId());
//            criteria.getDeptIds().addAll(groupService.getDeptChildren(criteria.getDeptId(),
//                    groupService.findByPid(criteria.getDeptId())));
//        }
//
//        // 数据权限
//        List<Long> dataScopes = groupService.getDeptIds(userService.findByName(SecurityUtils.getCurrentUsername()));
//        // criteria.getDeptIds() 不为空并且数据权限不为空则取交集
//        if (!CollectionUtils.isEmpty(criteria.getDeptIds()) && !CollectionUtils.isEmpty(dataScopes)) {
//            // 取交集
//            criteria.getDeptIds().retainAll(dataScopes);
//            if (!CollectionUtil.isEmpty(criteria.getDeptIds())) {
//                return success(userService.queryAll(criteria, pageable));
//            }
//        } else {
//            // 否则取并集
//            criteria.getDeptIds().addAll(dataScopes);
//            return success(userService.queryAllByCriteria(criteria));
//        }
//        return success(PageUtil.toPage(null, 0));
    }

    @Override
    public List<UserDO> queryAllByCriteriaWithNoPage(UserQueryCriteria criteria) {
        return CollectionUtil.list(false, userMapper.selectAllByCriteria(criteria));
    }

    @Override
    public void createUser(UserDO user) {
        checkCurrentUserRoleLevel(user);
        Assert.notNull(userMapper.selectByUserName(user.getUserName()), "用户名已存在，请重新命名！");
        // 默认密码 123456
        user.setPwd(passwordEncoder.encode("123456"));
        userMapper.insert(user);
    }

    @Override
    public void updateUser(UserDO user) {
        checkCurrentUserRoleLevel(user);
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public void updatePwd(UserPwdVO userPwdVo) throws Exception {
        String oldPass = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, userPwdVo.getOldPass());
        String newPass = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, userPwdVo.getNewPass());
        UserDO user = queryByUserName(SecurityUtils.getCurrentUsername());
        if (!passwordEncoder.matches(oldPass, user.getPwd())) {
            throw new BadRequestException("修改失败，旧密码错误");
        }
        if (passwordEncoder.matches(newPass, user.getPwd())) {
            throw new BadRequestException("新密码不能与旧密码相同");
        }
        String username = user.getUserName();
        userMapper.updateByPrimaryKeySelective(new UserDO(username, passwordEncoder.encode(newPass), LocalDateTime.now()));
        redisUtils.del("user::username:" + username);
        flushCache(username);
    }

    @Override
    public void updateEmail(String code, UserDO user) throws Exception {
        String password = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, user.getPwd());
        UserDO userDb = queryByUserName(SecurityUtils.getCurrentUsername());
        if (!passwordEncoder.matches(password, userDb.getPwd())) {
            throw new BadRequestException("密码错误");
        }
        verificationCodeService.validate(CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey() + user.getEmail(), code);
        userMapper.updateByPrimaryKeySelective(new UserDO(userDb.getUserName(), user.getEmail()));
        redisUtils.del(CacheKey.USER_NAME + userDb.getUserName());
        flushCache(userDb.getUserName());
    }

    @Override
    public void updateCenter(UserDO user) {
        UserDO userDb = Optional.of(userMapper.selectByPrimaryKey(user.getId())).orElseGet(UserDO::new);
        user.setNickName(user.getNickName());
        user.setPhone(user.getPhone());
        user.setGender(user.getGender());
        userMapper.updateByPrimaryKeySelective(userDb);
        // 清理缓存
        delCaches(userDb.getId(), userDb.getUserName());
    }

    @Override
    public void deleteByUserIds(Set<Long> userIds) {
        Assert.isTrue(roleService.hasSupperLevelInUsers(roleService.getLevelOfCurrentUserMaxRole(), userIds),
                "删除用户集合中存在高于或等于你角色的用户，权限不够，删除失败！");
        userMapper.batchDeleteByIds(userIds);
    }

    @Override
    public void download(List<UserDO> userDOList, HttpServletResponse response) throws IOException {
        FileUtil.downloadExcel(Optional.of(userDOList).orElseGet(ArrayList::new).stream().map(UserDO::toLinkedMap).collect(Collectors.toList()), response);
    }

    /**
     * 检测当前用户等级
     * 如果当前用户的角色级别低于要创建或修改用户的角色级别，则抛出权限不足的提示
     *
     * @param user
     */
    private void checkCurrentUserRoleLevel(UserDO user) {
        UserDO currentUser = queryById(SecurityUtils.getCurrentUserId());
        if (!currentUser.isAdmin()) {  // 管理员具有超级权限
            Integer levelOfCurrentUserMaxRole = roleService.getLevelOfCurrentUserMaxRole();
            Set<Long> set = user.getRoles().stream().map(RoleDO::getId).collect(Collectors.toSet());
            Integer levelOfOperatedUserMaxRole = Collections.min(Optional.of(roleService.batchQueryByIds(set)).orElseGet(ArrayList::new)
                    .stream()
                    .map(RoleDO::getLevel)
                    .collect(Collectors.toList()));
            Assert.isTrue(levelOfCurrentUserMaxRole < levelOfOperatedUserMaxRole, "您的角色权限不够， 不能操作高于或等于你权限的用户！");
        }
    }

    /**
     * 清理缓存
     *
     * @param id
     */
    private void delCaches(Long id, String userName) {
        redisUtils.del(CacheKey.USER_ID + id);
        redisUtils.del(CacheKey.USER_NAME + userName);
        flushCache(userName);
    }

    /**
     * 清理 登陆时 用户缓存信息
     *
     * @param username
     */
    private void flushCache(String username) {
        userCacheClean.cleanUserCache(username);
    }
}
