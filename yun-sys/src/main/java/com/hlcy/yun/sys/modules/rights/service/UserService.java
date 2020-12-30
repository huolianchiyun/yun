package com.hlcy.yun.sys.modules.rights.service;

import com.hlcy.yun.common.utils.download.DownLoadSupport;
import com.hlcy.yun.common.web.response.Meta;
import com.hlcy.yun.sys.modules.rights.model.criteria.UserQueryCriteria;
import com.hlcy.yun.sys.modules.rights.model.$do.UserDO;
import com.hlcy.yun.sys.modules.rights.model.vo.UserEmailVO;
import com.hlcy.yun.sys.modules.rights.model.vo.UserPwdVO;

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
     * @param userPwd
     */
    void updatePwd(UserPwdVO userPwd) throws Exception;

    /**
     * 重置密码
     *
     * @param username
     */
    void resetPwd(String username);

    /**
     * 修改邮箱
     *
     * @param userEmail
     */
    void updateEmail(UserEmailVO userEmail) throws Exception;

    /**
     * 根据登录名验证密码是否过期
     *
     * @param username /
     * @return /
     */
    Meta verifyPasswordExpired(String username);

}