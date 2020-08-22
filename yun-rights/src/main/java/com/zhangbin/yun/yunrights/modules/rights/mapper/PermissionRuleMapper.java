package com.zhangbin.yun.yunrights.modules.rights.mapper;

import com.zhangbin.yun.yunrights.modules.common.page.PageMapper;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.PermissionRuleDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PermissionRuleMapper extends PageMapper<PermissionRuleDO> {

    PermissionRuleDO selectByPrimaryKey(Long id);

    int insert(PermissionRuleDO permissionRuleDO);

    int updateByPrimaryKeySelective(PermissionRuleDO record);

    int deleteByPrimaryKey(Long id);

}
