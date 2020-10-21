package com.zhangbin.yun.yunrights.modules.rights.service;

import com.zhangbin.yun.common.utils.download.DownLoadSupport;
import com.zhangbin.yun.yunrights.modules.rights.model.criteria.UserQueryCriteria;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.UserDO;
import com.zhangbin.yun.yunrights.modules.rights.model.vo.UserEmailVO;
import com.zhangbin.yun.yunrights.modules.rights.model.vo.UserPwdVO;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface UserService extends PageService<UserQueryCriteria, UserDO>, DownLoadSupport<UserDO> {

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return /
     */
    UserDO queryById(Long id);

    /**
     * 根据ID查询用户名
     *
     * @param id ID
     * @return /
     */
    String queryUsernameById(Long id);

    /**
     * 根据登录名查询
     *
     * @param username /
     * @return /
     */
    UserDO queryByUsername(String username);

    /**
     * 不分页查询满足条件的数据
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
    void create(UserDO user);

    /**
     * 编辑用户
     *
     * @param user /
     */
    void update(UserDO user);

    /**
     * 批量删除用户
     *
     * @param ids /
     */
    void deleteByIds(Set<Long> ids);


    /**
     * 修改密码
     *
     * @param userPwdVo
     */
    void updatePwd(UserPwdVO userPwdVo) throws Exception;

    /**
     * 修改邮箱
     *
     * @param userEmail
     */
    void updateEmail(UserEmailVO userEmail) throws Exception;

}
