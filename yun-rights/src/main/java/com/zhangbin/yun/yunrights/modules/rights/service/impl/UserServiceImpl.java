package com.zhangbin.yun.yunrights.modules.rights.service.impl;

import com.github.pagehelper.Page;
import com.zhangbin.yun.yunrights.modules.common.config.RsaProperties;
import com.zhangbin.yun.yunrights.modules.common.exception.BadRequestException;
import com.zhangbin.yun.yunrights.modules.common.model.vo.PageInfo;
import com.zhangbin.yun.yunrights.modules.common.page.PageQueryHelper;
import com.zhangbin.yun.yunrights.modules.common.utils.RsaUtils;
import com.zhangbin.yun.yunrights.modules.common.utils.SecurityUtils;
import com.zhangbin.yun.yunrights.modules.rights.mapper.UserDoMapper;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.UserDo;
import com.zhangbin.yun.yunrights.modules.rights.model.UserQueryCriteria;
import com.zhangbin.yun.yunrights.modules.rights.model.dto.UserDto;
import com.zhangbin.yun.yunrights.modules.rights.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserDoMapper userDoMapper;

    @Override
    public UserDo findById(Long id) {
        return null;
    }

    @Override
    public void createUser(UserDo user) {
        // 默认密码 123456
        user.setPwd(passwordEncoder.encode("123456"));
        userDoMapper.insert(user);
    }

    @Override
    public void updateUser(UserDo user) {
        userDoMapper.updateByPrimaryKey(user);
    }

    @Override
    public void deleteUsers(Set<Long> ids) {
        userDoMapper.batchDelete(ids);
    }

    @Override
    public UserDo findByLoginName(String loginName) {
        return userDoMapper.selectByLoginName(loginName);
    }

    @Override
    public PageInfo<List<UserDo>> findByCriteria(UserQueryCriteria criteria) {
        Page<UserDo> page = PageQueryHelper.queryAllByCriteriaWithPage(criteria, userDoMapper);
        PageInfo<List<UserDo>> pageInfo = new PageInfo<>(criteria.getPageNum(), criteria.getPageSize());
        pageInfo.setTotal(page.getTotal());
        List<UserDo> result = page.getResult();
        pageInfo.setData(result);
        return pageInfo;
    }

    @Override
    public void updatePwd(String username, String encryptPwd) {
        String oldPass = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, passVo.getOldPass());
        String newPass = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, passVo.getNewPass());
        UserDto user = userService.findByName(SecurityUtils.getCurrentUsername());
        if (!passwordEncoder.matches(oldPass, user.getPassword())) {
            throw new BadRequestException("修改失败，旧密码错误");
        }
        if (passwordEncoder.matches(newPass, user.getPassword())) {
            throw new BadRequestException("新密码不能与旧密码相同");
        }
        userService.updatePwd(user.getUsername(), passwordEncoder.encode(newPass));
    }

    @Override
    public void updateEmail(String userName, String email) {
        String password = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, user.getPassword());
        UserDto userDto = userService.findByName(SecurityUtils.getCurrentUsername());
        if (!passwordEncoder.matches(password, userDto.getPassword())) {
            throw new BadRequestException("密码错误");
        }
        verificationCodeService.validated(CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey() + user.getEmail(), code);
        userService.updateEmail(userDto.getUsername(), user.getEmail());
    }

    @Override
    public Object queryAllByCriteria(UserQueryCriteria criteria) {
        return null;
    }

    @Override
    public List<UserDo> queryAll4NoPage(UserQueryCriteria criteria) {
        return null;
    }

    @Override
    public void download(List<UserDo> userDoList, HttpServletResponse response) throws IOException {

    }

    @Override
    public void updateCenter(UserDo user) {

    }
}
