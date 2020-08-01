package com.zhangbin.yun.yunrights.modules.rights.service;

import com.zhangbin.yun.yunrights.modules.common.model.vo.PageInfo;
import com.zhangbin.yun.yunrights.modules.rights.model.UserQueryCriteria;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.UserDo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface UserService {

    /**
     * 根据ID查询
     * @param id ID
     * @return /
     */
    UserDo findById(Long id);

    /**
     * 新增用户
     * @param user /
     */
    void createUser(UserDo user);

    /**
     * 编辑用户
     * @param user /
     */
    void updateUser(UserDo user);

    /**
     * 删除用户
     * @param ids /
     */
    void deleteUsers(Set<Long> ids);

    /**
     * 根据登录名查询
     * @param loginName /
     * @return /
     */
    UserDo findByLoginName(String loginName);

    /**
     * 根据登录名查询
     * @param criteria /
     * @return /
     */
    PageInfo<List<UserDo>> findByCriteria(UserQueryCriteria criteria);

    /**
     * 修改密码
     * @param username 用户名
     * @param encryptPwd 密码
     */
    void updatePwd(String username, String encryptPwd);

    /**
     * 修改头像
     * @param file 文件
     * @return /
     */
//    Map<String, String> updateAvatar(MultipartFile file);

    /**
     * 修改邮箱
     * @param userName 用户名
     * @param email 邮箱
     */
    void updateEmail(String userName, String email);

    /**
     * 分页查询全部
     * @param criteria 条件
     * @return /
     */
    Object queryAllByCriteria(UserQueryCriteria criteria);

    /**
     * 查询全部不分页
     * @param criteria 条件
     * @return /
     */
    List<UserDo> queryAll4NoPage(UserQueryCriteria criteria);

    /**
     * 导出数据
     * @param userDoList 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<UserDo> userDoList, HttpServletResponse response) throws IOException;

    /**
     * 用户自助修改资料
     * @param user /
     */
    void updateCenter(UserDo user);
}
