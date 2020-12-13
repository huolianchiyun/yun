package com.hlcy.yun.admincenter.mapper;

import com.hlcy.yun.admincenter.model.$do.TreeNode;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TreeNodeMapper {

    int insert(TreeNode record);
}
