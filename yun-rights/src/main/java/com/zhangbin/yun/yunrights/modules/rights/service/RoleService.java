package com.zhangbin.yun.yunrights.modules.rights.service;

import com.zhangbin.yun.yunrights.modules.rights.model.RoleQueryCriteria;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.RoleDo;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.UserDo;
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
    List<RoleDo> queryByConditions();

    /**
     * 根据ID查询
     * @param id /
     * @return /
     */
    RoleDo findById(long id);

    /**
     * 创建
     * @param resources /
     */
    void create(RoleDo resources);

    /**
     * 编辑
     * @param resources /
     */
    void update(RoleDo resources);

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
    List<RoleDo> findByUsersId(Long id);

    /**
     * 根据角色查询角色级别
     * @param roles /
     * @return /
     */
    Integer findByRoles(Set<RoleDo> roles);

    /**
     * 修改绑定的菜单
     * @param resources /
     * @param roleDTO /
     */
    void updateMenu(RoleDo resources, RoleDo roleDTO);

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
    Object queryByConditions(RoleQueryCriteria queryConditions, Pageable pageable);

    /**
     * 查询全部
     * @param queryConditions 条件
     * @return /
     */
    List<RoleDo> queryByConditions(RoleQueryCriteria queryConditions);

    /**
     * 导出数据
     * @param queryAll 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<RoleDo> queryAll, HttpServletResponse response) throws IOException;

    /**
     * 获取用户权限信息
     * @param user 用户信息
     * @return 权限信息
     */
    List<GrantedAuthority> mapToGrantedAuthorities(UserDo user);

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
    List<RoleDo> findInMenuId(List<Long> menuIds);
}
