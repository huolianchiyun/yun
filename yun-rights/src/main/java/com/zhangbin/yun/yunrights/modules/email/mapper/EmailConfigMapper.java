package com.zhangbin.yun.yunrights.modules.email.mapper;

import com.zhangbin.yun.yunrights.modules.email.model.entity.EmailConfig;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmailConfigMapper {
    int deleteByPrimaryKey(Long id);

    int insert(EmailConfig record);

    int insertSelective(EmailConfig record);

    EmailConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(EmailConfig record);

    int updateByPrimaryKey(EmailConfig record);
}