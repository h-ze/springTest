整合spring代码
整合jwt登录
实现后台swagger界面
整合shiro进行认证授权
问题:如果一直在使用时token应该不能过期,如果超过几个小时未使用token应该会有个默认过期时间
默认返回一个token以及一个refreshToken 
问题:如果并发下几个接口同时调用又遇到需要刷新token时


springboot与springcloud的版本一定要对应 不然会出现启动失败的问题

jenkins


Maven中使用 scope 来指定当前包的依赖范围和依赖的传递性
scope范围
compile ：为默认的依赖有效范围。如果在定义依赖关系的时候，没有明确指定依赖有效范围的话，则默认采用该依赖有效范围。此种依赖，在编译、运行、测试时均有效。
provided ：在编译、测试时有效，但是在运行时无效。例如：servlet-api，运行项目时，容器已经提供，就不需要Maven重复地引入一遍了。
runtime ：在运行、测试时有效，但是在编译代码时无效。例如：JDBC驱动实现，项目代码编译只需要JDK提供的JDBC接口，只有在测试或运行项目时才需要实现上述接口的具体JDBC驱动。
test ：只在测试时有效，例如：JUnit。
system ：在编译、测试时有效，但是在运行时无效。和provided的区别是，使用system范围的依赖时必须通过systemPath元素显式地指定依赖文件的路径。由于此类依赖不是通过Maven仓库解析的，而且往往与本机系统绑定，可能造成构建的不可移植，因此应该谨慎使用。systemPath元素可以引用环境变量

使用Spring时,在bean中不要使用this来调用被@Async、@Transactional、@Cacheable等注解标注的方法，this下注解是不生效的

@configuration和@component之间的区别
@configuration和@component之间的区别是：@Component注解的范围最广，所有类都可以注解，但是@Configuration注解一般注解在这样的类上：这个类里面有@Value注解的成员变量和@Bean注解的方法，就是一个配置类。

一句话概括就是 @Configuration 中所有带 @Bean 注解的方法都会被动态代理，因此调用该方法返回的都是同一个实例。使用Component时是不同的对象



Exclusions：是依赖排除（Dependency Exclusions） 
maven的依赖调解有两大原则：路径最近者优先；第一声明者优先