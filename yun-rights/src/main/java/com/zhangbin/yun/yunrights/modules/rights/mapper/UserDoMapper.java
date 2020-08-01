package com.zhangbin.yun.yunrights.modules.rights.mapper;

import com.zhangbin.yun.yunrights.modules.common.page.PageMapper;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.UserDo;
import com.zhangbin.yun.yunrights.modules.rights.model.UserQueryCriteria;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;

@Mapper
public interface UserDoMapper extends PageMapper<UserDo> {
    int insert(UserDo record);

    int insertSelective(UserDo record);

    UserDo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserDo record);

    int updateByPrimaryKey(UserDo record);

    int deleteByPrimaryKey(Long id);

    int batchDelete(Set<Long> userIds);

    List<UserDo> selectByUserName(String userName);

    UserDo selectByLoginName(String loginName);

}
