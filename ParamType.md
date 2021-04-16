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


@RequestParam注解用来处理Content-Type: 为 application/x-www-form-urlencoded编码的内容。提交方式为get或post。
@RequestPart这个注解用在multipart/form-data表单提交请求的方法上。(一般用于文件上传)