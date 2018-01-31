package cn.eyeblue.blog.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * 顺便配置了web初始化的时候的工作。
 */
@Configuration
@Getter
public class Config {

    @Value("${server.debug}")
    private Boolean serverDebug;

    @Value("${blog.version}")
    private String blogVersion;

    public boolean isDebug() {
        return serverDebug;
    }


    public static void loadConfigFromEnvironment() {


        /*
#configs
ServerPort=6020
LoggingConfig="classpath:logback-pro.xml"
#mysql configs
MysqlPort=3306
MysqlHost=127.0.0.1
MysqlSchema=blog
MysqlUserName=blog
MysqlPassword=blog123
MysqlUrl="jdbc:mysql://$MysqlHost:$MysqlPort/$MysqlSchema?useUnicode=true&characterEncoding=UTF-8&useSSL=false"
#tank configs
TankUrl="https://tank.eyeblue.cn"
TankEmail=blog_dev@tank.eyeblue.cn
TankPassword=123456
#admin configs
AdminUsername=admin
AdminEmail=admin@blog.eyeblue.cn
AdminPassword=123456
#email configs
MailProtocol=smtps
MailHost=smtp.126.com
MailPort=465
MailUsername=eyeblue@126.com
MailPassword=eyeblue_password
MailDefaultEncoding=UTF-8
        */

        //开始读取环境变量
        Map<String, String> map = System.getenv();

        //服务端口。
        String tmpServerPort = map.get("BLOG_SERVER_PORT");
        if (tmpServerPort != null) {
            int serverPort = Integer.parseInt(tmpServerPort);
            System.setProperty("server.port", tmpServerPort);
        }

        //日志存放路径，只有在部署环境才显示日志。

        System.setProperty("log.path", System.getProperty("user.dir") + "/data/blog/log");
//
//        System.setProperty("spring.datasource.url", "jdbc:mysql://127.0.0.1:3306/blog?useUnicode=true&characterEncoding=UTF-8&useSSL=false");
//        System.setProperty("spring.datasource.username", "blog");
//        System.setProperty("spring.datasource.password", "blog123");


    }

}
