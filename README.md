![蓝眼博客logo](https://github.com/eyebluecn/blog/blob/master/src/main/resources/static/img/logo.png?raw=true)

# 蓝眼博客

##### [在线Demo](http://blog.eyeblue.cn)

##### [配套前端blog-front](https://github.com/eyebluecn/blog-front)

### 简介
蓝眼博客是 [蓝眼系列开源软件](https://github.com/eyebluecn) 中的第二个

- 可用于快速搭建个人博客。
- 可以作为私人笔记使用。
- 支持多用户写作，评论，邮件通知等核心功能。

蓝眼博客力求小而精，可以作为团队内部或个人私有的博客使用，同时也可作为学习资源使用。

如果您觉得蓝眼博客对您有帮助，请不要吝惜您的star :smile:

### 使用安装包安装

#### a) 准备工作

1. 一台windows/linux服务器，当然你可以使用自己的电脑充当这台服务器

2. [安装好Mysql数据库](https://www.mysql.com/downloads/)

3. 安装好java8

4. [在这里](https://github.com/eyebluecn/blog/releases)下载服务器对应的安装包

5. 在服务器上解压缩，修改脚本文件(windows对应startup.bat，linux对应startup.sh)中的各配置项，各项说明如下：
```

#服务器运行的端口，默认6020。如果配置为80，则可直接用http打开
ServerPort=6020


#Mysql相关的配置
#Mysql端口，默认3306
MysqlPort=3306
#Mysql主机
MysqlHost=127.0.0.1
#Mysql数据库名称
MysqlSchema=blog
#Mysql用户名，建议为蓝眼博客创建一个用户，不建议使用root
MysqlUsername=blog
#Mysql密码
MysqlPassword=blog123
#通过以上信息组合出的访问链接，这项请不要修改。
MysqlUrl="jdbc:mysql://$MysqlHost:$MysqlPort/$MysqlSchema?useUnicode=true&characterEncoding=UTF-8&useSSL=false"


#蓝眼云盘配置项
#蓝眼云盘访问地址，要求是一个通过公网就能访问到的地址
TankUrl="https://tank.eyeblue.cn"
#蓝眼云盘登录用的邮箱
TankEmail=blog_dev@tank.eyeblue.cn
#蓝眼云盘登录用的密码
TankPassword=123456

#超级管理员配置项
#超级管理员昵称
AdminUsername=admin
#超级管理员邮箱
AdminEmail=admin@blog.eyeblue.cn
#超级管理员密码
AdminPassword=123456


#发送邮件配置项
MailProtocol=smtps
MailHost=smtp.126.com
MailPort=465
MailUsername=eyeblue@126.com
MailPassword=eyeblue_password
MailDefaultEncoding=UTF-8

```

#### b) 运行

- windows平台解压后直接双击应用目录下的`startup.bat`。

    如果要关闭，直接关闭命令行窗口即可

- linux平台执行 

```

# 启动应用
cd 应用目录路径
./startup.sh

# 停止应用
cd 应用目录路径
./shutdown.sh

```

#### c) 验证

浏览器中打开 http://127.0.0.1:6020 (127.0.0.1请使用服务器所在ip，6020请使用配置的`ServerPort`) 可以看到以下首页页面：

![蓝眼博客首页](https://github.com/eyebluecn/blog/blob/master/doc/screenshot/home.png)

访问`http://127.0.0.1:6020/by`可看到后台管理页面，使用配置项中的超级管理员邮箱和密码即可登录
![蓝眼博客后台](https://github.com/eyebluecn/blog/blob/master/doc/screenshot/backyard.png)

### 使用源代码自行打包

#### a) 准备工作

1. 一台windows/linux服务器，当然你可以使用自己的电脑充当这台服务器

2. Mysql数据库

3. 安装好java8，配置妥当`JAVA_HOME`

4. clone本项目

#### b) 打包

- windows平台双击运行 `mvnw.cmd install`，成功之后可在`target`下看到`blog-x.x.x.jar`文件，该文件和`doc/script/startup.bat`即组成安装包

- linux平台运行如下命令：
```
./doc/script/pack.sh
```
成功之后可在`target`下看到`blog-x.x.x.linux.tar.gz`

利用得到的安装包即可参考上文的`使用安装包安装`。


### 文档

正在完善中...

### Contribution

感谢所有蓝眼博客的贡献者 [@zicla](https://github.com/zicla)，[@seaheart](https://github.com/seaheart)，[@yemuhe](https://github.com/yemuhe)，[@hxsherry](https://github.com/hxsherry)

如果您也想参与进来，请尽情的fork, star, post issue, pull requests

### License

[MIT](http://opensource.org/licenses/MIT)

Copyright (c) 2017-present, eyeblue.cn