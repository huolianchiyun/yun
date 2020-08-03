package com.zhangbin.yun.yunrights.modules.rights.service;

import com.zhangbin.yun.yunrights.modules.rights.model.UserQueryCriteria;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.UserDo;
import com.zhangbin.yun.yunrights.modules.rights.model.vo.UserPwdVo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface UserService {

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return /
     */
    UserDo queryById(Long id);

    /**
     * 根据登录名查询
     *
     * @param loginName /
     * @return /
     */
    UserDo queryByUserName(String loginName);

    /**
     * 分页查询全部
     *
     * @param criteria 条件
     * @return /
     */
    Object queryAllByCriteria(UserQueryCriteria criteria);

    /**
     * 查询全部不分页
     *
     * @param criteria 条件
     * @return /
     */
    List<UserDo> queryAllByCriteriaWithNoPage(UserQueryCriteria criteria);

    /**
     * 新增用户
     *
     * @param user /
     */
    void createUser(UserDo user);

    /**
     * 编辑用户
     *
     * @param user /
     */
    void updateUser(UserDo user);

    /**
     * 删除用户
     *
     * @param ids /
     */
    void deleteUsers(Set<Long> ids);


    /**
     * 修改密码
     *
     * @param userPwdVo
     */
    void updatePwd(UserPwdVo userPwdVo) throws Exception;

    /**
     * 修改邮箱
     *
     * @param code  验证码
     * @param user
     */
    void updateEmail(String code, UserDo user) throws Exception;

    /**
     * 用户自助修改资料
     *
     * @param user /
     */
    void updateCenter(UserDo user);

    /**
     * 导出数据
     *
     * @param userDoList 待导出的数据
     * @param response   /
     * @throws IOException /
     */
    void download(List<UserDo> userDoList, HttpServletResponse response) throws IOException;

    /**
     * 修改头像
     * @param file 文件
     * @return /
     */
//    Map<String, String> updateAvatar(MultipartFile file);
}
