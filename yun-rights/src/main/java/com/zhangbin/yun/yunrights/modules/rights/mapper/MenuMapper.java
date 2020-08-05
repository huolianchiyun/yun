package com.zhangbin.yun.yunrights.modules.rights.mapper;

import com.zhangbin.yun.yunrights.modules.rights.model.$do.MenuDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MenuMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MenuDO record);

    int insertSelective(MenuDO record);

    MenuDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MenuDO record);

    int updateByPrimaryKey(MenuDO record);
}
