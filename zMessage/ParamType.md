============================================================

参数注解说明
条件 	                                                                对应parmType
有@PathVariable注解 	                                                path
有@RequestBody注解 	                                                    body
有@RequestPart注解 	                                                    formData
有@RequestHeader注解 	                                                header
有@RequestParam注解 	                                                解析方式和无注解时一致
参数类型为MultipartFile或被Collection\Array等包装的MultipartFile 	        form
无任何注解consumes包含application/x-www-form-urlencoded且接口类型为post 	form
无任何注解consumes包含multipart/form-data且接口类型为post 	            formData
无任何注解且不满足上述2个条件 	                                        query
不符合上述任何条件 	                                                    body

@RequestParam：将请求参数绑定到你控制器的方法参数上（是springmvc中接收普通参数的注解）
@RequestParam注解用来处理Content-Type: 为 application/x-www-form-urlencoded编码的内容。提交方式为get或post。
@RequestPart这个注解用在multipart/form-data表单提交请求的方法上。(一般用于文件上传)

1.application/x-www-form-urlencoded
GET方式，会将表单中的数据（键值对）经过urlencode编码后追加到url中。
POST方式，会将表单中的数据经过urlencode编码后放在request body 中。
2.multipart/form-data
当需要在表单内上传文件时（二进制流数据）时，就需要使用multipart/form-data 编码方式。    

=========================================================   
 
websocket注解说明    
@ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端。注解的值将被用于监听用户连接的终端访问URL地址

=========================================================

Maven里的内容说明
Maven中使用 scope 来指定当前包的依赖范围和依赖的传递性
scope范围
compile ：为默认的依赖有效范围。如果在定义依赖关系的时候，没有明确指定依赖有效范围的话，则默认采用该依赖有效范围。此种依赖，在编译、运行、测试时均有效。
provided ：在编译、测试时有效，但是在运行时无效。例如：servlet-api，运行项目时，容器已经提供，就不需要Maven重复地引入一遍了。
runtime ：在运行、测试时有效，但是在编译代码时无效。例如：JDBC驱动实现，项目代码编译只需要JDK提供的JDBC接口，只有在测试或运行项目时才需要实现上述接口的具体JDBC驱动。
test ：只在测试时有效，例如：JUnit。
system ：在编译、测试时有效，但是在运行时无效。和provided的区别是，使用system范围的依赖时必须通过systemPath元素显式地指定依赖文件的路径。由于此类依赖不是通过Maven仓库解析的，而且往往与本机系统绑定，可能造成构建的不可移植，因此应该谨慎使用。systemPath元素可以引用环境变量

Exclusions：是依赖排除（Dependency Exclusions） 
maven的依赖调解有两大原则：路径最近者优先；第一声明者优先

========================================================

使用Spring时,在bean中不要使用this来调用被@Async、@Transactional、@Cacheable等注解标注的方法，this下注解是不生效的
@configuration和@component之间的区别
@configuration和@component之间的区别是：@Component注解的范围最广，所有类都可以注解，但是@Configuration注解一般注解在这样的类上：这个类里面有@Value注解的成员变量和@Bean注解的方法，就是一个配置类。
一句话概括就是 @Configuration 中所有带 @Bean 注解的方法都会被动态代理，因此调用该方法返回的都是同一个实例。使用Component时是不同的对象

========================================================

springboot 常用参数校验的注解
注解 	        作用类型 	            解释
@NotNull 	    任何类型 	            属性不能为null
@NotEmpty 	    集合 	                集合不能为null，且size大于0
@NotBlank 	    字符串、字符 	        字符类不能为null，且去掉空格之后长度大于0
@AssertTrue 	Boolean、boolean 	    布尔属性必须是true
@Min 	        数字类型（原子和包装） 	限定数字的最小值（整型）
@Max 	        同@Min 	                限定数字的最大值（整型）
@DecimalMin 	同@Min 	                限定数字的最小值（字符串，可以是小数）
@DecimalMax 	同@Min 	                限定数字的最大值（字符串，可以是小数）
@Range 	        数字类型（原子和包装） 	限定数字范围（长整型）
@Length 	    字符串 	                限定字符串长度
@Size 	        集合 	                限定集合大小
@Past 	        时间、日期 	            必须是一个过去的时间或日期
@Future 	    时期、时间 	            必须是一个未来的时间或日期
@Email 	        字符串 	                必须是一个邮箱格式
@Pattern 	    字符串、字符 	        正则匹配字符串

==========================================================

将一个微服务注册到Eureka Server
使用@EnableDiscoveryClient 或@EnableEurekaClient

从Spring Cloud Edgware开始，@EnableDiscoveryClient 或@EnableEurekaClient 可省略。只需加上相关依赖，并进行相应配置，即可将微服务注册到服务发现组件上。