**Spring Cache + Redis 缓存使用**

```
@Cacheable(key = "'id:' + #p0")
MenuServiceImpl#queryById(long id)
与 CacheKey.MENU_ID = "menu::id:" 对应
```


**去掉role、role_menu表原因：**
```
1、之前group关联role，role关联menu，但group具有层级结构，要求子组权限不能父组权限，
若group->role->menu，将导致group层级设权限很难控制，因为权限被role封装了，用户不好设置，
故让group直接关联->menu，这样role就多余了，不能让多个入口和menu关联，这样容易造成权限控制混乱。
故让group代替role且使其具备层级概念。

https://www.cnblogs.com/pluto4596/p/11005417.html
数据权限控制效果：要求在同一个数据，同一个请求方法中，根据不同的权限返回不同的数据集，而且无需并且不能由研发编码控制。
方案：利用Mybatis的plugin机制，在底层SQL解析时替换增加过滤条件。
缺点：（用Mybatis，而且数据库使用的是Mysql，这方面就没有太大影响了）
    1、适用性有限，基于底层的Mybatis。
    2、方言有限，针对了某种数据库（我们使用Mysql），而且由于需要在底层解析处理条件所以有可能造成不同的数据库不能兼容。当然Redis和NoSQL也无法限制。
优点：
    1、减少了接口数量及接口复杂度。原本针对不同的角色，可能会区分不同的接口或者在接口实现时利用流程控制逻辑来区分不同的条件。有了数据权限控制，代码中只用写基本逻辑，权限过滤由底层机制自动处理。
    2、提高了数据权限控制的灵活性。例如原本只有主管能查本部门下组织架构/订单数据，现在新增助理角色，能够查询本部门下组织架构，不能查询订单。这样的话普通的写法就需要调整逻辑控制，使用数据权限控制的话，直接修改配置就好。
```

  
works：

1. alarm push system
2. monitor  11
3. code generator
4. auto-deploy


