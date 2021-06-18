用户：root用户创建其他用户，其他用户可以设置权限（看是否可以给其他用户赋予权限的 with grant option）
增加用户 ：create user '用户名'@'%' identified by '密码';
设置权限：grant all privileges on foxitDB.* to '用户名'@'%'   （%是匹配所有的ip地址，如果只想本地连接这里把%换为localhost，本句话grant all 意思为设置全部权限，on foxitDB.*意思为设置为foxitDB数据库的权限，这里如果使用*.*则意味着所有的数据库）
登录： mysql -u 用户名 -p 回车输入密码
查看权限：show grants
选择数据库 ：use 库名
查看所有的数据库：show databases
查看选中的数据库的所有表： show tables
创建数据库：create database 数据库
删除数据库：drop database 数据库
查看支持的存储引擎类型:show engines

InnoDB是事务型数据库的首选引擎，支持事务安全表（ACID）,支持行锁定和外键，mysql5.5.5之后，InnoDB作为默认存储引擎
特性：1.支持事务安全 2。处理巨大数据量的最大性能设计 3完全与mysql服务器整合 4支持外键完整性约束（没有指定主键时InnoDB会为每一行生成一个6b的rowid，并以此为主键）

MyISAM存储引擎 是基于ISAM的存储引擎，拥有较高的插入，查询速度，但不支持事务，在Mysql5.5.5之前是默认存储引擎

memory存储引擎，archive存储引擎

如果要提供提交回滚和崩溃恢复能力的事务安全，并要求实现并发控制，InnoDB是个很好的选择，如果主要用来插入和查询记录，MyISAM能提供
较高的处理效率，如果只是临时存放数据，数据量不打，并且不需要较高的数据安全性，可以使用Memory，
Archive适合存储归档数据，如记录日志。
一个数据库中多个表可以使用不同引擎以满足各种性能和实际需求

主表（父表）：对于两个具有关联联系的表而言，相关联字段中主键所在的那个表即为主表
从表（字表）：对于两个具有关联关系的表而言，相关联字段中外键所在的那个表即是从表

外键规则 
[CONSTRAINT  <外键约束名称> FOREIGN KEY (外键列) REFERENCES <主表名>（主键列）]

查看表基本结构
DESCRIBE 表名；（简写 DESC 表名）