package com.hlcy.yun.sys.api;

import com.hlcy.yun.sys.modules.rights.controller.MenuController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Collections;

@SpringBootTest
public class MenuControllerTest {
    @Autowired
    MenuController menuController;

    @Test
    @WithMockUser("test")
    public void testDelNotExistMenu(){
        Assertions.assertThrows(IllegalArgumentException.class, ()->menuController.deleteByIds(Collections.singleton(-1L)),"将要删除的菜单不存在！");
    }
}
