package com.zhangbin.yun.yunrights.mapper;


import com.zhangbin.yun.yunrights.modules.rights.mapper.GroupMapper;
import com.zhangbin.yun.yunrights.modules.rights.model.$do.GroupDO;
import com.zhangbin.yun.yunrights.modules.rights.service.GroupService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


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
        groupDO.setGroupMaster("admin");
        groupDO.setCreator("admin");
        groupDO.setGroupType("group::dept");
        service.createGroup(groupDO);
    }
}
