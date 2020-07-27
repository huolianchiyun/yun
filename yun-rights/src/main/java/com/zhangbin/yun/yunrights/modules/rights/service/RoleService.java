package com.zhangbin.yun.yunrights.modules.rights.service;

import com.zhangbin.yun.yunrights.modules.rights.model.RoleQueryConditions;
import com.zhangbin.yun.yunrights.modules.rights.model.entity.Role;
import com.zhangbin.yun.yunrights.modules.rights.model.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @author Zheng Jie
 * @date 2018-12-03
 */
public interface RoleService {

    /**
     * 查询全部数据
     * @return /
     */
    List<Role> queryByConditions();

    /**
     * 根据ID查询
     * @param id /
     * @return /
     */
    Role findById(long id);

    /**
     * 创建
     * @param resources /
     */
    void create(Role resources);

    /**
     * 编辑
     * @param resources /
     */
    void update(Role resources);

    /**
     * 删除
     * @param ids /
     */
    void delete(Set<Long> ids);

    /**
     * 根据用户ID查询
     * @param id 用户ID
     * @return /
     */
    List<Role> findByUsersId(Long id);

    /**
     * 根据角色查询角色级别
     * @param roles /
     * @return /
     */
    Integer findByRoles(Set<Role> roles);

    /**
     * 修改绑定的菜单
     * @param resources /
     * @param roleDTO /
     */
    void updateMenu(Role resources, Role roleDTO);

    /**
     * 解绑菜单
     * @param id /
     */
    void untiedMenu(Long id);

    /**
     * 条件分页查询
     * @param queryConditions 条件
     * @param pageable 分页参数
     * @return /
     */
    Object queryByConditions(RoleQueryConditions queryConditions, Pageable pageable);

    /**
     * 查询全部
     * @param queryConditions 条件
     * @return /
     */
    List<Role> queryByConditions(RoleQueryConditions queryConditions);

    /**
     * 导出数据
     * @param queryAll 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<Role> queryAll, HttpServletResponse response) throws IOException;

    /**
     * 获取用户权限信息
     * @param user 用户信息
     * @return 权限信息
     */
    List<GrantedAuthority> mapToGrantedAuthorities(User user);

    /**
     * 验证是否被用户关联
     * @param ids /
     */
    void verification(Set<Long> ids);

    /**
     * 根据菜单Id查询
     * @param menuIds /
     * @return /
     */
    List<Role> findInMenuId(List<Long> menuIds);
}
