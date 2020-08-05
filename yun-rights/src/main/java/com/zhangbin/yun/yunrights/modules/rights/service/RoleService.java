package com.zhangbin.yun.yunrights.modules.rights.service;

import com.zhangbin.yun.yunrights.modules.common.exception.BadRequestException;
import com.zhangbin.yun.yunrights.modules.common.model.vo.PageInfo;
import com.zhangbin.yun.yunrights.modules.rights.model.criteria.RoleQueryCriteria;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.RoleDO;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.UserDO;
import org.springframework.security.core.GrantedAuthority;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface RoleService {

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    RoleDO queryById(Long id);

    List<RoleDO> batchQueryByIds(Set<Long> ids);
    /**
     * 根据用户ID查询
     *
     * @param userId 用户ID
     * @return
     */
    List<RoleDO> queryByUserId(Long userId);


    /**
     * 不分页查询满足条件的角色
     *
     * @return
     */
    List<RoleDO> queryAllByCriteriaWithNoPage(RoleQueryCriteria criteria);

    /**
     * 分页查询满足条件的角色
     *
     * @return
     */
    PageInfo<List<RoleDO>> queryAllByCriteria(RoleQueryCriteria criteria);

    /**
     * 创建角色
     *
     * @param role
     */
    void createRole(RoleDO role);

    /**
     * 编辑角色
     *
     * @param role
     */
    void updateRole(RoleDO role);

    /**
     * 批量删除角色
     *
     * @param roleIds
     */
    void batchDeleteRoles(Set<Long> roleIds);

    /**
     * 导出角色数据
     *
     * @param roleList 待导出角色列表
     * @param response
     * @throws IOException
     */
    void download(List<RoleDO> roleList, HttpServletResponse response) throws IOException;

    /**
     * 检查角色集合是否关联用户
     *
     * @param roleIds 将要删除的角色id集合
     * @return 关联返回 true，反之，返回 false
     */
    Boolean isAssociatedUsers(Set<Long> roleIds);


    /**
     * 修改角色关联的菜单
     *
     * @param role
     */
    void updateAssociatedMenuForRole(RoleDO role);


    /**
     * 获取用户权限信息
     *
     * @param user 用户信息
     * @return 权限信息
     */
    List<GrantedAuthority> getGrantedAuthorities(UserDO user);

    /**
     * 获取当前用户最大角色的等级
     *
     * @return level
     */
    Integer getLevelOfCurrentUserMaxRole();


    /**
     * 检测当前用户角色等级是否高于将要操作的角色等级，若低于将要操作的角色等级，则抛异常提示权限不够
     *
     * @param level 将要操作的角色等级
     */
    default void checkRoleLevel(Integer level) {
        int maxLevel = getLevelOfCurrentUserMaxRole();
        if (level != null) {
            if (level < maxLevel) {
                throw new BadRequestException("权限不足，你的角色级别：" + maxLevel + "，低于要操作的角色级别：" + level);
            }
        }
    }

    /**
     * 判断 userIds 用户集合中是否存在其角色 level >= LevelOfCurrentUserMaxRole 的用户
     * @param levelOfCurrentUserMaxRole 当前用户最大角色的等级
     * @param userIds  将要检测的用户 id 集合
     * @return 存在 true | 不存在 false
     */
    boolean hasSupperLevelInUsers(Integer levelOfCurrentUserMaxRole, Set<Long> userIds);
}
