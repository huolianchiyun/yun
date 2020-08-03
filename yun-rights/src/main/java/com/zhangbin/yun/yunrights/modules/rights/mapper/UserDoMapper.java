package com.zhangbin.yun.yunrights.modules.rights.mapper;

import com.zhangbin.yun.yunrights.modules.common.page.PageMapper;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.UserDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Set;

@Mapper
public interface UserDoMapper extends PageMapper<UserDo> {

    UserDo selectByPrimaryKey(Long id);

    UserDo selectByUserName(String userName);

    int insert(UserDo record);

    int insertSelective(UserDo record);

    int deleteByPrimaryKey(Long id);

    int batchDelete(@Param("userIds") Set<Long> userIds);

    int updateByPrimaryKeySelective(UserDo record);

}
