# mall

# 项目介绍
``mall``项目是一个前后端分离的电商平台，包括前台商城和后台商城管理系统。
后台基于SpringBoot实现，使用Docker部署应用。前端基于Vue实现，
前台商城包括商品搜索，商品浏览，商品推荐，购物车，订单管理，会员中心等功能；
后台商城管理系统包括权限管理，商品管理，订单管理等功能。

## 项目体验
**后台商城管理系统**

[前端项目mall-admin-web项目地址](https://github.com/AhaYujie/mall-admin-web)

[项目体验地址](https://app.ahayujie.online/mall-aha)

体验账号: 用户名admin, 密码123456

![后台预览](https://aha-yujie.oss-cn-shenzhen.aliyuncs.com/doc-image/mall/%E5%90%8E%E5%8F%B0%E9%A2%84%E8%A7%88.jpg)

# 项目结构
- ``mall-admin``: 后台商城管理系统接口
- ``mall-code-generator``: 自动生成数据库操作代码
- ``mall-common``: 工具类和通用代码
- ``mall-portal``: 前台商城接口
- ``mall-search``: 商城搜索接口
- ``mall-security``: 实现JWT权限的公共代码

# 技术栈
- ``SpringBoot``: 后台应用框架
- ``SpringSecurity``: 实现JWT的权限框架
- ``MyBatis``: ORM框架
- ``mybatis-plus-generator``: 数据库操作代码自动生成
- ``Elasticsearch``: 商城搜索引擎
- ``RabbitMQ``: 模块间解耦, 订单延迟消息
- ``Redis``: 订单分布式锁
- ``Quartz``: 订单任务分布式调度
- ``Docker``: 容器化部署应用

# 开发进度
**后台商城管理系统**
- 商品管理: 完成
- 订单管理: 开发中
- 会员管理: 开发中
- 权限管理: 完成

**前台商城**
- 商品模块功能: 开发中
- 购物车: 开发中
- 订单管理: 开发中
- 会员中心: 开发中

# 感谢
本项目借鉴了[macrozheng/mall](https://github.com/macrozheng/mall)项目, 
在这里感谢原作者提供的代码思路和想法。
