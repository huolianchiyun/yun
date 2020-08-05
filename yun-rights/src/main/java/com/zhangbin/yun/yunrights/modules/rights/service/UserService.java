package com.zhangbin.yun.yunrights.modules.rights.service;

import com.zhangbin.yun.yunrights.modules.common.model.vo.PageInfo;
import com.zhangbin.yun.yunrights.modules.rights.model.criteria.UserQueryCriteria;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.UserDO;
import com.zhangbin.yun.yunrights.modules.rights.model.vo.UserPwdVO;
import io.swagger.models.auth.In;

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
    UserDO queryById(Long id);

    /**
     * 根据登录名查询
     *
     * @param loginName /
     * @return /
     */
    UserDO queryByUserName(String loginName);

    /**
     * 分页查询全部
     *
     * @param criteria 条件
     * @return /
     */
    PageInfo<List<UserDO>> queryAllByCriteria(UserQueryCriteria criteria);

    /**
     * 查询全部
     *
     * @param criteria 条件
     * @return /
     */
    List<UserDO> queryAllByCriteriaWithNoPage(UserQueryCriteria criteria);

    /**
     * 新增用户
     *
     * @param user /
     */
    void createUser(UserDO user);

    /**
     * 编辑用户
     *
     * @param user /
     */
    void updateUser(UserDO user);

    /**
     * 删除用户
     *
     * @param ids /
     */
    void deleteByUserIds(Set<Long> ids);


    /**
     * 修改密码
     *
     * @param userPwdVo
     */
    void updatePwd(UserPwdVO userPwdVo) throws Exception;

    /**
     * 修改邮箱
     *
     * @param code  验证码
     * @param user
     */
    void updateEmail(String code, UserDO user) throws Exception;

    /**
     * 用户自助修改资料
     *
     * @param user /
     */
    void updateCenter(UserDO user);

    /**
     * 导出数据
     *
     * @param userDOList 待导出的数据
     * @param response   /
     * @throws IOException /
     */
    void download(List<UserDO> userDOList, HttpServletResponse response) throws IOException;

}
