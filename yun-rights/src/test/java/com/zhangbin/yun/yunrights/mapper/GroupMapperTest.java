package com.zhangbin.yun.yunrights.mapper;


import com.zhangbin.yun.yunrights.modules.rights.model.$do.GroupDO;
import com.zhangbin.yun.yunrights.modules.rights.service.GroupService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.zhangbin.yun.yunrights.modules.rights.common.constant.RightsConstants.ADMINISTRATOR;


@SpringBootTest
public class GroupMapperTest {

    @Autowired
    GroupService service;

    @Test
    void testCreateGroup(){
        GroupDO groupDO = new GroupDO();
        groupDO.setGroupName("HW新型创新综合部门");
        groupDO.setGroupSort(1);
        groupDO.setPid(1L);
        groupDO.setGroupMaster(ADMINISTRATOR);
        groupDO.setCreator(ADMINISTRATOR);
        groupDO.setGroupType("group::dept");
        service.create(groupDO);
    }
}
