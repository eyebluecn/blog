![蓝眼博客logo](https://github.com/eyebluecn/blog/blob/master/src/main/resources/static/img/logo.png?raw=true)

# 蓝眼云盘

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
MysqlUserName=blog
#Mysql密码
MysqlPassword=blog123
#通过以上信息组合出的访问链接，这项请不要修改。
MysqlUrl="jdbc:mysql://$MysqlHost:$MysqlPort/$MysqlSchema?useUnicode=true&characterEncoding=UTF-8&useSSL=false"


#蓝眼云盘配置项
#蓝眼云盘访问地址，要求是一个通过公网就能访问到的地址
TankUrl="http://tank.eyeblue.cn"
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

浏览器中打开 http://127.0.0.1:6020 (127.0.0.1请使用服务器所在ip，6020请使用配置的`ServerPort`) 可以看到以下登录页面：

![蓝眼云盘登录页面](https://raw.githubusercontent.com/eyebluecn/tank/master/build/doc/img/login.png)

使用上方配置文件中的邮箱和密码登录后可以看到如下界面：
![蓝眼云盘登录页面](https://raw.githubusercontent.com/eyebluecn/tank/master/build/doc/img/matters.png)

### 使用源代码自行打包

#### a) 准备工作

1. 一台windows/linux服务器，当然你可以使用自己的电脑充当这台服务器

2. Mysql数据库

3. clone本项目

4. 安装Golang，环境变量 `GOPATH`配置到工程目录，建议工程目录结构如下：

```
golang                                          #环境变量GOPATH所在路径
├── bin                                         #编译生成的可执行文件目录
├── pkg                                         #编译生成第三方库
├── src                                         #golang工程源代码
│   ├── github.com                              #来自github的第三方库
│   ├── golang.org                              #来自golang.org的第三方库
│   ├── tank                                    #clone下来的tank根目录
│   │   ├── build                               #用来辅助打包的文件夹
│   │   │   ├── conf                            #默认的配置文件
│   │   │   ├── doc                             #文档
│   │   │   ├── html                            #前端静态资源，从项目tank-front编译获得
│   │   │   ├── pack                            #打包的脚本
│   │   │   ├── service                         #将tank当作服务启动的脚本
│   │   ├── dist                                #运行打包脚本后获得的安装包目录
│   │   ├── rest                                #golang源代码
      
```

5. 准备项目依赖的第三方库

- golang.org/x
- github.com/disintegration/imaging
- github.com/json-iterator/go
- github.com/go-sql-driver/mysql
- github.com/jinzhu/gorm
- github.com/nu7hatch/gouuid

其中`golang.org/x`国内无法下载，请从[这里](https://github.com/eyebluecn/golang.org)下载，并按上文推荐的目录结构放置。其余依赖项均可通过安装脚本自动下载。

#### b) 打包

- windows平台双击运行 `tank/build/pack/build.bat`，成功之后可在`tank/dist`下看到`tank-x.x.x`文件夹，该文件夹即为最终安装包。

- linux平台运行如下命令：
```
cd tank/build/pack/
./build.sh
```
成功之后可在`tank/dist`下看到`tank-x.x.x.linux-amd64.tar.gz`

利用得到的安装包即可参考上文的`使用安装包安装`。


### 文档

[蓝眼云盘后端api](https://github.com/eyebluecn/tank/blob/master/build/doc/api_zh.md)

[蓝眼云盘编程接口](https://github.com/eyebluecn/tank/blob/master/build/doc/alien_zh.md)

### Contribution

感谢所有蓝眼云盘的贡献者 [@zicla](https://github.com/zicla)，[@seaheart](https://github.com/seaheart)，[@yemuhe](https://github.com/yemuhe)，[@hxsherry](https://github.com/hxsherry)

如果您也想参与进来，请尽情的fork, star, post issue, pull requests

### License

[MIT](http://opensource.org/licenses/MIT)

Copyright (c) 2017-present, eyeblue.cn