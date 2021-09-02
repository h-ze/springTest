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




