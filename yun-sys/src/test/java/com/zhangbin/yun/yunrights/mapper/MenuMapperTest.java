package com.zhangbin.yun.yunrights.mapper;


import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.yun.sys.modules.rights.mapper.MenuMapper;
import com.yun.sys.modules.rights.model.$do.MenuDO;
import com.yun.sys.modules.rights.service.MenuService;
import com.yun.sys.modules.rights.service.impl.MenuServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

@SpringBootTest
public class MenuMapperTest {

    @Autowired
    MenuMapper menuMapper;
    @Autowired
    MenuService menuService;

    @Test
    public void testDownload() {
        MenuServiceImpl menuService = new MenuServiceImpl(null, null, null, null);
        MenuDO menu1 = new MenuDO();
        menu1.setId(0L);
        menu1.setMenuTitle("aa");
        MenuDO menu2 = new MenuDO();
        menu2.setId(0L);
        menu2.setMenuTitle("bb");
        MenuDO menu3 = new MenuDO();
        menu3.setId(0L);
        menu3.setMenuTitle("cc");
        MenuDO menu4 = new MenuDO();
        menu4.setId(0L);
        menu4.setMenuTitle("dd");
        MenuDO menu5 = new MenuDO();
        menu5.setId(0L);
        menu5.setMenuTitle("ee");
        menu1.setChildren(Arrays.asList(menu2));
        menu2.setChildren(Arrays.asList(menu3));
        menu3.setChildren(Arrays.asList(menu4));
        menu4.setChildren(Arrays.asList(menu5));

        MenuDO menu11 = new MenuDO();
        menu11.setId(0L);
        menu11.setMenuTitle("11");
        MenuDO menu22 = new MenuDO();
        menu22.setId(0L);
        menu22.setMenuTitle("22");
        MenuDO menu33 = new MenuDO();
        menu33.setId(0L);
        menu33.setMenuTitle("33");
        MenuDO menu44 = new MenuDO();
        menu44.setId(0L);
        menu44.setMenuTitle("44");
        MenuDO menu55 = new MenuDO();
        menu55.setId(0L);
        menu55.setMenuTitle("55");
        menu11.setChildren(Arrays.asList(menu22));
        menu22.setChildren(Arrays.asList(menu33));
        menu33.setChildren(Arrays.asList(menu44));
        menu44.setChildren(Arrays.asList(menu55));
        List<MenuDO> menuSorted = new ArrayList<>();
        Arrays.asList(menu1, menu11).forEach(new Consumer<MenuDO>() {
            @Override
            public void accept(MenuDO menu) {
                menuSorted.add(menu);
                List<MenuDO> children = menu.getChildren();
                menu.setChildren(null);
                if (CollectionUtil.isNotEmpty(children)) {
                    children.forEach(this);
                }
                return;
            }
        });

        System.out.println(JSON.toJSONString(menuSorted, SerializerFeature.DisableCircularReferenceDetect));

    }

    @Test
    public void testSelectAllByCriteria(){
        Set<MenuDO> set = menuMapper.selectByCriteria(null);
        System.out.println(set);
    }

    @Test
    public void testMenuTree(){
        Set<MenuDO> set = menuMapper.selectByCriteria(null);
        System.out.println(JSON.toJSONString(menuService.buildMenuTree(set), SerializerFeature.WriteNullListAsEmpty));

    }

}
