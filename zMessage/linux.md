service iptables status 防火墙的状态
service iptables restart 重启防火墙
service iptables stop/start 关闭/打开防火墙（其他服务类似）

reboot 重启linux服务器

rpm -qa | grep tomcat 查看是否安装tomcat

ps -ef | grep 端口号 查看端口号使用情况
netstat -an | grep 端口号 查看端口是否被打开，通过查看LISTEN列的地址查看是否开放远程连接 本地的会显示 127.0.0.1:3306 可远程连接的显示的是0.0.0.0:3306（3306是举的例子 3306是mysql端口）
cd /地址 切换到哪个地址 如果不写地址 跳转的是根目录 即root目录
var文件系统存放的是数据
etc文件系统存放的是各种系统配置文件


echo $PATH 显示当前PATH环境变量
echo ￥JAVA_HOME 显示当前jdk安装路径，前提已经配置