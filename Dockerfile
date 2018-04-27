# 使用java8作为母镜像
FROM openjdk:8

# 维护者信息，也就是作者的姓名和邮箱
MAINTAINER eyeblue "eyebluecn@126.com"

# 指定工作目录就是 tank。工作目录指的就是以后 每层构建的当前目录 。
WORKDIR /root/eyeblue/blog

# 将tank项目下的所有文件移动到golang镜像中去
COPY . /root/eyeblue/blog

# 这里为了维持docker无状态性，准备数据卷作为日志目录
VOLUME /data/log
# 通过环境变量的方式，为应用指定日志目录
ENV BLOG_LOG_PATH=/data/log

# 开始使用Maven打包
RUN ./mvnw install

# 声明运行时容器提供服务端口，这只是一个声明。默认是6020端口
EXPOSE 6020

# 作为执行文件 启动这个容器就会去执行 `jar包`
ENTRYPOINT ["java","-Xmx512m","-jar","/root/eyeblue/blog/target/blog-1.1.0.jar"]
