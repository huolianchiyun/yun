package com.zhangbin.yun.yunrights.modules.rights.mapper;

import com.zhangbin.yun.yunrights.modules.rights.model.$do.MenuDo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MenuDoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MenuDo record);

    int insertSelective(MenuDo record);

    MenuDo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MenuDo record);

    int updateByPrimaryKey(MenuDo record);
}