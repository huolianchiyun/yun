package com.zhangbin.yun.yunrights.modules.rights.mapper;

import com.zhangbin.yun.common.mybatis.page.PageMapper;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.PermissionRuleDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

@Mapper
public interface PermissionRuleMapper extends PageMapper<PermissionRuleDO> {

    PermissionRuleDO selectByPrimaryKey(Long id);

    Set<PermissionRuleDO> selectAllUsableForSystem();

    int insert(PermissionRuleDO permissionRuleDO);

    int updateByPrimaryKeySelective(PermissionRuleDO record);

    int deleteByPrimaryKey(Long id);

}
