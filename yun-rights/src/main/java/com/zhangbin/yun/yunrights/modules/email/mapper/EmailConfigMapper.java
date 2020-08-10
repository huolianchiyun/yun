package com.zhangbin.yun.yunrights.modules.email.mapper;

import com.zhangbin.yun.yunrights.modules.email.model.$do.EmailConfigDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmailConfigMapper {
    EmailConfigDO selectByPrimaryKey(Long id);

    int insert(EmailConfigDO record);

    int updateByPrimaryKeySelective(EmailConfigDO record);

    int deleteByPrimaryKey(Long id);
}
