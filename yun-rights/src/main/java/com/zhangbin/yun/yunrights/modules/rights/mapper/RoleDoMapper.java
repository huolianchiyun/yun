package com.zhangbin.yun.yunrights.modules.rights.mapper;

import com.zhangbin.yun.yunrights.modules.rights.model.$do.RoleDo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoleDoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RoleDo record);

    int insertSelective(RoleDo record);

    RoleDo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RoleDo record);

    int updateByPrimaryKey(RoleDo record);
}