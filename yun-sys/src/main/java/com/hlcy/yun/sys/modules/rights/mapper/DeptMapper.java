package com.hlcy.yun.sys.modules.rights.mapper;

import com.hlcy.yun.common.mybatis.page.PageMapper;
import com.hlcy.yun.sys.modules.rights.model.$do.DeptDO;
import com.hlcy.yun.sys.modules.rights.model.$do.GroupDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

@Mapper
public interface DeptMapper extends PageMapper<DeptDO> {

    DeptDO selectByPrimaryKey(Long id);

    /**
     * 最多查询 1000 条记录
     */
    Set<DeptDO> selectAll();

    Set<DeptDO> selectByPid(Long Pid);

    int insert(DeptDO record);

    int updateByPrimaryKeySelective(DeptDO record);

    int deleteByIds(@Param("ids") Set<Long> ids);

    int countAssociatedUser(@Param("ids") Set<Long> ids);
}
