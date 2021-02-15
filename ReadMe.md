**idea 问题排错**

```
1、question：idea中maven配置jar包存在，依旧报找不到错误？
   answer：找到对应项目目录下的项目名.iml文件删除
```

works：
4、ssrc重复问题 -- 原因重启sip后 可能会造成ssrc重复，因为拿数组第一个？？ ****
1、yun-sys 考虑将dept从group中抽离出来 --done 待自测
2、设备注册后 管理中心主动查询device info、catalog、recordinfo --联调


3、cruise time error？？
6、对参数值范围进行校验

抓拍、录像流媒体 负载均衡 --初步方案： 共享ssrc，ssrc附带流媒体IP
flow context 环境清理  --初步方案：设置有效期，有效期到时向流媒体询问ssrc 是否使用，若没，则释放，反之，延长有效期
服务可靠性、分布式部署

自研项：
1、code generator
2、auto-deploy
3、自动化测试： Selenium 是目前用的最广泛的Web UI 自动化测试框架
4、分布式事务： alibaba Seate


