package com.yun.sys.modules.email.mapper;

import com.yun.sys.modules.email.model.$do.EmailConfigDO;
import com.yun.sys.modules.rights.datarights.NotPermission;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@NotPermission
public interface EmailConfigMapper {
    EmailConfigDO selectByPrimaryKey(Long id);

    int insert(EmailConfigDO record);

    int updateByPrimaryKeySelective(EmailConfigDO record);

    int deleteByPrimaryKey(Long id);
}
