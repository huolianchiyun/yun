package com.hlcy.yun.sys.modules.rights.mapper;

import com.hlcy.yun.common.mybatis.page.PageMapper;
import com.hlcy.yun.sys.modules.rights.model.$do.PermissionRuleDO;
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
