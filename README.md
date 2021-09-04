整合spring代码
整合jwt登录
实现后台swagger界面
整合shiro进行认证授权
问题:如果一直在使用时token应该不能过期,如果超过几个小时未使用token应该会有个默认过期时间
默认返回一个token以及一个refreshToken 
问题:如果并发下几个接口同时调用又遇到需要刷新token时


springboot与springcloud的版本一定要对应 不然会出现启动失败的问题

如果遇到yml文件中无法识别@符号的问题 尝试在双@符号外添加双引号解决问题

本地zookeeper启动失败 可能原因为端口被占用 可以在config包下的zoo.cfg文件中添加新的端口号（zk3.5之后的端口号默认为8080，所以被占用的可能性非常大）

jenkins

Nagios监控系统



针对此电脑 mac 项目启动的要求
首先需要启动 eureka项目
该项目是为了启动eureka server
然后分别启动不同的项目
springserver项目中主要是使用了springboot以及shiro以及jwt整合了一套鉴权的系统
其他的模块大致为springdubbocomsumer相关的三个模块是为了调试dubbo相关的内容，本系统中
dubbo主要是使用了zookeeper来进行协调服务 
其他模块例如mail book等模块是eureka的client模块 中间使用了restTemplate
其他的模块包含springcloud内容

redis相关操作
cd /usr/local/redis-5.0.10/bin
进入到bin目录后执行./redis-server启动redis
然后新开一个黑窗口 同样进入bin目录后执行./redis-cli

zookeeper相关操作
zkServer start 启动zookeeper
zkServer status 查看zookeeper状态
zkCli 执行zookeeper具体操作
zookeeper是个cp系统 讲究的是一致性 具体可以参考cap理论 （cap理论是分布式领域中存在的理论，该理论已被证实只能同时满足两个条件无法同时满足三个条件 即ap和cp）


查看 RabbitMQ 状态
systemctl status rabbitmq-server #Active: active (running) 说明处于运行状态
# service rabbitmq-server status 用service指令也可以查看，同systemctl指令
启动、停止、重启
service rabbitmq-server start # 启动
service rabbitmq-server stop # 停止
service rabbitmq-server restart # 重启

