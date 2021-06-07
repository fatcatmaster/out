# fatcat

#### 介绍

当前微服务工程提供一套用于业务开发的基础架构，已经集成了常用中间件包括但不仅限于 redis、mybatis-plus、elasticsearch、rabbitMq，并配置了这些中间件的常用规则和属性等；另外对架构做了一些功能增强型的二次开发和封装并提供一整套开发标准，详细内容请看代码；

#### 软件架构

- 主体: SpringCloud(Hoxton.RELEASE); SpringBoot(2.2.1.RELEASE)
- 注册中心:Eureka(版本跟随)
- 网关: Gateway(版本跟随)
- 远程配置: SpringCloudConfig(版本跟随)
- 监控中心: SpringBootAdmin(2.2.1)

#### 额外环境：

- nexus: 3.30.0-01
- elasticsearch: 7.11.2
- rabbit: 3.8-management
- mysql: 5.7.33
- redis: 6.2.1

#### 模块介绍

1.  fc-centre: 整个架构的核心功能模块，包括组件集成和封装、逆向工程、全局常量、jwt、数据格式转换等功能；
2.  fc-common: 全局常量，异常，返回值的封装，统一数据返回格式；
3.  fc-core: 数据转换配置、feign 和 hystrix 的配置、全局日志配置、redis 配置
4.  fc-elastic: 对 es 的二次封装，包括一些常用 api 的封装
5.  fc-generator: 逆向工程，用于生成 controller、service、mapper 标准业务逻辑
6.  fc-jwt: java web token 的工具封装，用于网关做 token 验证
7.  fc-mybatis: mybatis-plus 的配置优化
8.  fc-rabbit: rabbit 的二次封装
9.  fc-web: 标准 mvc 工程的依赖合集
10. fc-config: 远程配置
11. fc-eureka: 注册中心
12. fc-gateway: 网关中心
13. fc-monitor: spring-boot-admin 服务环境监控
14. fc-static: 静态配置文件，与服务名保持一致
15. fc-search: 业务搜索服务中心，主要与 es 交互
16. fc-user: 业务用户管理中心，与 mysql 交互数据

#### 配置参数

熔断器相关配置
1. "spring.hystrix.max-request"："请求处理的队列最大值，线程和信号量处理最大请求量的数"
2. "spring.hystrix.strategy"："熔断策略，自定义默认使用信号量"
3. "spring.hystrix.thread-core-size"："核心线程池的线程大小，策略为线程时生效"
4. "spring.hystrix.time-out"："hystrix 熔断的超时时间"

elastic相关配置
1. "spring.elastic.connect-enabled"："是否开启es连接"
2. "spring.elastic.connect-timeout"："连接超时时间"
3. "spring.elastic.max-connect": "最大连接数"
4. "spring.elastic.max-connect-route": "最大路由数连接数"
5. "spring.elastic.name": "用户名"
6. "spring.elastic.password": "密码"
7. "spring.elastic.request-timeout": "获取连接超时时间"
8. "spring.elastic.socket-timeout": "会话超时时间"
9. "spring.elastic.uris": "es节点，资源路径"

rabbit相关配置
1. "spring.rabbit.uri": "rabbit 资源地址"
2. "spring.rabbit.host": "主机地址"
3. "spring.rabbit.port": "服务端口"
4. "spring.rabbit.name": "用户名"
5. "spring.rabbit.password": "密码"
6. "spring.rabbit.virtual-host": "分区名"

mybatis相关配置
1. "spring.mybatis.character-encoding": "配置上个属性使用，指定编码集"
2. "spring.mybatis.database": "需要使用的数据库名称"
3. "spring.mybatis.server-timezone": "指定时区，也可配置成'GMT+8'"
4. "spring.mybatis.ssl": "高版本mysql需要指定是否使用ssl连接"
5. "spring.mybatis.url": "数据库服务器地址"
6. "spring.mybatis.use-unicode": "使用自定义编码为 utf-8;默认gbk"
