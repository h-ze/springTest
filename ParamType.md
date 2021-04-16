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
    