package com.zhangbin.yun.yunrights.modules.email.mapper;

import com.zhangbin.yun.yunrights.modules.email.model.$do.EmailConfigDo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmailConfigDoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(EmailConfigDo record);

    int insertSelective(EmailConfigDo record);

    EmailConfigDo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(EmailConfigDo record);

    int updateByPrimaryKey(EmailConfigDo record);
}