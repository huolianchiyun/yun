# yun-common-starter 使用说明

###代码目录结构功能说明
```
--autoconfigure     自动配置相关，只要使用方pom文件中引入了该模块的GAV，则自动识别开启跨域过滤器、pagehelper分页缓存过滤器功能及RedisUtils的IOC管控（可以注入RedisUtils操作redis）
--constant          常量         
--exception         异常，定义了部分异常类
--filter            过滤器
--model             公共 model，BaseDO类为DB实体类基类，定义了ID、创建者、创建时间、修改人、修改时间，并且这些注解添加了审计注解和validation校验，可以参考：yun-sys模块用法
--mybatis           mybatis相关`
  --audit           审计相关，如通过相关注解标识，自动向将要入库实体类添加 创建人、创建时间、更新人、更新时间， 可以参考：yun-sys模块用法
  --page            分页查询，抽象并封装的了pagenhelper分页查询，可以参考：yun-sys模块用法
--page              分页相关，对分页数据响应前端形式的封装
--spring            spring相关
--utils             公共工具，开发工程中可以优先考虑使用该包中的工具类，不要重复造轮子
--web               web相关
  --response        对前端响应数据格式的统一封装，可以参考：yun-sys模块用法
  --websocket       websocket 支持tomcat 和 netty方式， 前端请求url：ws://localhost:8081/ws/sid，sid必填，tcp handshake时需带token进行校验
                    使用方式：springboot 启动类上加 @EnableWebSocket 注解后，将启动websocket功能，并可以设置该注解的属性值切换 tomcat 或 netty 模式
```

