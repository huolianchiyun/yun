package com.hlcy.yun.sys.mapper;


import com.hlcy.yun.sys.modules.rights.model.$do.GroupDO;
import com.hlcy.yun.sys.modules.rights.service.GroupService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import static com.hlcy.yun.sys.modules.rights.common.constant.RightsConstants.ADMINISTRATOR;


@SpringBootTest
public class GroupMapperTest {

    @Autowired
    GroupService service;

    @Test
    @WithMockUser("test")
    void testCreateGroup(){
        GroupDO groupDO = new GroupDO();
        groupDO.setGroupName("6677");
        groupDO.setGroupSort(1);
        groupDO.setPid(1L);
        groupDO.setGroupMaster(ADMINISTRATOR);
        groupDO.setGroupType("group::dept");
        service.create(groupDO);
    }
}
