# 电动滑板车租赁后端系统

## 项目简介
电动滑板车租赁系统的后端服务，提供用户管理、车辆管理、订单管理等功能。

## 数据库配置
项目已配置连接到集群内部数据库：

- 主机名：escoot-mysql.ns-x3h6hlzn.svc
- 端口：3306
- 用户名：root
- 密码：tkbbpwrp
- 数据库名：escoot

## 启动说明
直接启动项目即可，默认使用生产环境配置：
```bash
./mvnw spring-boot:run
```

## 开发模式

当前项目处于开发模式，已临时禁用了JWT Token验证功能。所有API接口都可以不带Token直接访问。

- 在`JwtInterceptor.java`中，`DEV_MODE`变量设置为`true`
- 在`WebConfig.java`中，JWT拦截器已被注释掉
- 在前端的`request.uts`文件中，同样设置了`DEV_MODE = true`

开发完成后，需要进行以下修改：

1. 将`JwtInterceptor.java`中的`DEV_MODE`变量设置为`false`
2. 取消`WebConfig.java`中JWT拦截器的注释
3. 将前端`request.uts`文件中的`DEV_MODE`变量设置为`false`

## 跨域配置

项目已配置支持跨域请求，使得前端可以从不同源（域名、端口、协议）访问后端API。主要配置包括：

1. 在`WebConfig.java`中配置了CORS映射和CORS过滤器
2. 添加了`SimpleCorsFilter.java`提供更强大的CORS支持
3. 在`application.properties`中设置了Spring Web CORS配置

支持的前端源包括：
- http://localhost:8080
- http://localhost:3000
- http://localhost:8081
- https://aadujkrrrwxp.sealoshzh.site
- https://escoot-web.sealoshzh.site

如需添加新的前端源，请修改以下文件：
- `WebConfig.java`中的`corsFilter()`方法
- `application.properties`中的`spring.web.cors.allowed-origins`配置

## 项目特性

- 用户管理：注册、登录、查看个人信息
- 滑板车管理：查看滑板车列表、查看滑板车详情
- 订单管理：创建订单、支付订单、查看订单详情

## 技术栈

- Spring Boot
- MyBatis
- MySQL
- HikariCP连接池
- JWT认证 (开发阶段临时禁用) 