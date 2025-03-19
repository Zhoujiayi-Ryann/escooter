# 电动滑板车租赁后端系统

## 项目简介
电动滑板车租赁系统的后端服务，提供用户管理、车辆管理、订单管理等功能。

## 环境配置
本项目支持多环境配置，主要有以下环境：

1. **生产环境 (prod)** - 默认环境，连接集群内部数据库
2. **开发环境 (dev)** - 用于本地开发，连接外部数据库

## 数据库配置

### 内网数据库（集群内部使用）
- 主机名：escoot-mysql.ns-x3h6hlzn.svc
- 端口：3306
- 用户名：root
- 密码：tkbbpwrp
- 数据库名：escoot

### 外网数据库（集群外部使用）
- 主机名：dbconn.sealoshzh.site
- 端口：47581
- 用户名：root
- 密码：tkbbpwrp
- 数据库名：escoot

## 启动说明

### 在集群环境内启动（默认）
直接启动项目即可，默认使用生产环境配置：
```bash
./mvnw spring-boot:run
```

### 在外部环境启动
如果您在集群外部的电脑上启动项目，请使用开发环境配置：
```bash
./mvnw spring-boot:run -Dspring.profiles.active=dev
```

## 数据库连接问题解决

如果您在外部电脑上遇到数据库连接问题，请尝试以下解决方案：

### 1. 使用外网数据库地址（推荐）
已经在`application-dev.properties`中配置了外网数据库地址：
```
jdbc:mysql://dbconn.sealoshzh.site:47581/escoot
```

使用以下命令启动应用：
```bash
./mvnw spring-boot:run -Dspring.profiles.active=dev
```

### 2. 使用SSH隧道（备选方案）
如果外网数据库连接不稳定，可以尝试使用SSH隧道连接集群内部数据库：

```bash
# 设置SSH隧道脚本的执行权限
chmod +x setup-tunnel.sh

# 运行SSH隧道脚本
./setup-tunnel.sh -s [跳板机地址] -u [用户名]
```

然后修改`application-dev.properties`中的数据库地址为：
```
spring.datasource.url=jdbc:mysql://localhost:3306/escoot?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
```

### 3. 诊断连接问题
如果仍然遇到连接问题，可以查看应用启动时的日志信息。`DatabaseConfig`类中添加了详细的网络诊断信息，可以帮助您确定问题所在。 