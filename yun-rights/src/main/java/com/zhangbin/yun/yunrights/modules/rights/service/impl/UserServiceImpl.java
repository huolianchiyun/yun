package com.zhangbin.yun.yunrights.modules.rights.service.impl;

import com.github.pagehelper.Page;
import com.zhangbin.yun.yunrights.modules.common.config.RsaProperties;
import com.zhangbin.yun.yunrights.modules.common.enums.CodeEnum;
import com.zhangbin.yun.yunrights.modules.common.exception.BadRequestException;
import com.zhangbin.yun.yunrights.modules.common.model.vo.PageInfo;
import com.zhangbin.yun.yunrights.modules.common.page.PageQueryHelper;
import com.zhangbin.yun.yunrights.modules.common.utils.*;
import com.zhangbin.yun.yunrights.modules.rights.mapper.UserDoMapper;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.RoleDo;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.UserDo;
import com.zhangbin.yun.yunrights.modules.rights.model.UserQueryCriteria;
import com.zhangbin.yun.yunrights.modules.rights.model.dto.RoleSmallDto;
import com.zhangbin.yun.yunrights.modules.rights.model.vo.UserPwdVo;
import com.zhangbin.yun.yunrights.modules.rights.service.RoleService;
import com.zhangbin.yun.yunrights.modules.rights.service.UserService;
import com.zhangbin.yun.yunrights.modules.rights.service.VerifyService;
import com.zhangbin.yun.yunrights.modules.security.service.UserCacheClean;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserDoMapper userDoMapper;
    private final RoleService roleService;
    private final RedisUtils redisUtils;
    private final UserCacheClean userCacheClean;
    private final VerifyService verificationCodeService;

    @Override
    public UserDo queryById(Long id) {
        return null;
    }

    @Override
    public UserDo queryByUserName(String loginName) {
        return userDoMapper.selectByUserName(loginName);
    }

    @Override
    public Object queryAllByCriteria(UserQueryCriteria criteria) {
        Page<UserDo> page = PageQueryHelper.queryAllByCriteriaWithPage(criteria, userDoMapper);
        PageInfo<List<UserDo>> pageInfo = new PageInfo<>(criteria.getPageNum(), criteria.getPageSize());
        pageInfo.setTotal(page.getTotal());
        List<UserDo> result = page.getResult();
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
    public List<UserDo> queryAllByCriteriaWithNoPage(UserQueryCriteria criteria) {
        return null;
    }

    @Override
    public void createUser(UserDo user) {
        checkLevel(user);
        // 默认密码 123456
        user.setPwd(passwordEncoder.encode("123456"));
        userDoMapper.insert(user);
    }

    @Override
    public void updateUser(UserDo user) {
        checkLevel(user);
        userDoMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public void updatePwd(UserPwdVo userPwdVo) throws Exception {
        String oldPass = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, userPwdVo.getOldPass());
        String newPass = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, userPwdVo.getNewPass());
        UserDo user = queryByUserName(SecurityUtils.getCurrentUsername());
        if (!passwordEncoder.matches(oldPass, user.getPwd())) {
            throw new BadRequestException("修改失败，旧密码错误");
        }
        if (passwordEncoder.matches(newPass, user.getPwd())) {
            throw new BadRequestException("新密码不能与旧密码相同");
        }
        String username = user.getUserName();
        userDoMapper.updateByPrimaryKeySelective(new UserDo(username, passwordEncoder.encode(newPass), LocalDateTime.now()));
        redisUtils.del("user::username:" + username);
        flushCache(username);
    }

    @Override
    public void updateEmail(String code, UserDo user) throws Exception {
        String password = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, user.getPwd());
        UserDo userDb = queryByUserName(SecurityUtils.getCurrentUsername());
        if (!passwordEncoder.matches(password, userDb.getPwd())) {
            throw new BadRequestException("密码错误");
        }
        verificationCodeService.validate(CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey() + user.getEmail(), code);
        userDoMapper.updateByPrimaryKeySelective(new UserDo(userDb.getUserName(), user.getEmail()));
        redisUtils.del(CacheKey.USER_NAME + userDb.getUserName());
        flushCache(userDb.getUserName());
    }

    @Override
    public void updateCenter(UserDo user) {
        UserDo userDb = Optional.of(userDoMapper.selectByPrimaryKey(user.getId())).orElseGet(UserDo::new);
        user.setNickName(user.getNickName());
        user.setPhone(user.getPhone());
        user.setGender(user.getGender());
        userDoMapper.updateByPrimaryKeySelective(userDb);
        // 清理缓存
        delCaches(userDb.getId(), userDb.getUserName());
    }

    @Override
    public void deleteUsers(Set<Long> ids) {
        for (Long id : ids) {
            Integer currentLevel = Collections.min(roleService.findByUserId(SecurityUtils.getCurrentUserId())
                    .stream().map(RoleSmallDto::getLevel)
                    .collect(Collectors.toList()));
            Integer optLevel = Collections.min(roleService.findByUserId(id).stream().map(RoleSmallDto::getLevel).collect(Collectors.toList()));
            if (currentLevel > optLevel) {
                throw new BadRequestException("角色权限不足，不能删除：" + queryById(id).getUserName());
            }
        }
        userDoMapper.batchDelete(ids);
    }


    @Override
    public void download(List<UserDo> userDoList, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UserDo user : userDoList) {
            List<String> roles = user.getRoles().stream().map(RoleDo::getRoleName).collect(Collectors.toList());
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("用户名", user.getUserName());
            map.put("角色", roles);
            map.put("部门", user.getDept().getGroupName());
            map.put("邮箱", user.getEmail());
            map.put("状态", user.getEnabled() ? "启用" : "禁用");
            map.put("手机号码", user.getPhone());
            map.put("修改密码时间", user.getPwdResetTime());
            map.put("创建日期", user.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * 清理缓存
     *
     * @param id
     */
    public void delCaches(Long id, String userName) {
        redisUtils.del(CacheKey.USER_ID + id);
        redisUtils.del(CacheKey.USER_NAME + userName);
        flushCache(userName);
    }

    /**
     * 创建或更新用户权限校验
     * 如果当前用户的角色级别低于要创建用户的角色级别，则抛出权限不足的错误
     *
     * @param user
     */
    private void checkLevel(UserDo user) {
        Integer currentLevel = Collections.min(
                roleService.findByUserId(SecurityUtils.getCurrentUserId())
                        .stream()
                        .map(RoleSmallDto::getLevel)
                        .collect(Collectors.toList()));
        Integer optLevel = roleService.findByRoles(user.getRoles());
        if (currentLevel > optLevel) {
            throw new BadRequestException("角色权限不足， 操作失败！");
        }
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
