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

        //开始读取环境变量
        Map<String, String> map = System.getenv();

        //服务端口。
        String tmpServerPort = map.get("BLOG_SERVER_PORT");
        if (tmpServerPort != null) {
            int serverPort = Integer.parseInt(tmpServerPort);
            System.setProperty("server.port", tmpServerPort);
        }

        //日志存放路径。控制台中显示所有日志，文件中记录INFO及以上级别的日志。
        String logPath = System.getProperty("user.home") + "/data/eyeblue/blog/log";
        String tmpLogPath = map.get("BLOG_LOG_PATH");
        if (tmpLogPath != null) {
            //将带 ~ 的相对路径转换成绝对路径
            if (tmpLogPath.startsWith("~")) {
                tmpLogPath = tmpLogPath.replace("~", System.getProperty("user.home"));
            }
            logPath = tmpLogPath;
        }
        System.setProperty("log.path", logPath);

        //Mysql主机，端口，数据库
        String mysqlPort = map.get("BLOG_MYSQL_PORT");
        String mysqlHost = map.get("BLOG_MYSQL_HOST");
        String mysqlSchema = map.get("BLOG_MYSQL_SCHEMA");
        if (mysqlPort != null && mysqlHost != null && mysqlSchema != null) {
            String mysqlUrl = "jdbc:mysql://" + mysqlHost + ":" + mysqlPort + "/" + mysqlSchema + "?useUnicode=true&characterEncoding=UTF-8&useSSL=false";
            System.setProperty("spring.datasource.url", mysqlUrl);
        }

        //数据库用户名
        String mysqlUsername = map.get("BLOG_MYSQL_USERNAME");
        if (mysqlUsername != null) {
            System.setProperty("spring.datasource.username", mysqlUsername);
        }
        //数据库密码
        String mysqlPassword = map.get("BLOG_MYSQL_PASSWORD");
        if (mysqlPassword != null) {
            System.setProperty("spring.datasource.password", mysqlPassword);
        }


        //蓝眼云盘的路径
        String tankUrl = map.get("BLOG_TANK_URL");
        if (tankUrl != null) {
            System.setProperty("tank.url", tankUrl);
        }
        //蓝眼云盘的登录邮箱
        String tankEmail = map.get("BLOG_TANK_EMAIL");
        if (tankEmail != null) {
            System.setProperty("tank.email", tankEmail);
        }
        //蓝眼云盘的密码
        String tankPassword = map.get("BLOG_TANK_PASSWORD");
        if (tankPassword != null) {
            System.setProperty("tank.password", tankPassword);
        }

        //蓝眼博客管理员昵称
        String adminUsername = map.get("BLOG_ADMIN_USERNAME");
        if (adminUsername != null) {
            System.setProperty("admin.username", adminUsername);
        }
        //蓝眼博客管理员邮箱
        String adminEmail = map.get("BLOG_ADMIN_EMAIL");
        if (adminEmail != null) {
            System.setProperty("admin.email", adminEmail);
        }
        //蓝眼博客管理员密码
        String adminPassword = map.get("BLOG_ADMIN_PASSWORD");
        if (adminPassword != null) {
            System.setProperty("admin.password", adminPassword);
        }

        //邮件通知协议
        String mailProtocol = map.get("BLOG_MAIL_PROTOCOL");
        if (mailProtocol != null) {
            System.setProperty("spring.mail.protocol", mailProtocol);
        }
        //邮件通知主机
        String mailHost = map.get("BLOG_MAIL_HOST");
        if (mailHost != null) {
            System.setProperty("spring.mail.host", mailHost);
        }
        //邮件通知端口
        String mailPort = map.get("BLOG_MAIL_PORT");
        if (mailPort != null) {
            System.setProperty("spring.mail.port", mailPort);
        }
        //邮件通知用户名，你的邮箱
        String mailUsername = map.get("BLOG_MAIL_USERNAME");
        if (mailUsername != null) {
            System.setProperty("spring.mail.username", mailUsername);
        }

        //邮件通知密码，你的密码
        String mailPassword = map.get("BLOG_MAIL_PASSWORD");
        if (mailPassword != null) {
            System.setProperty("spring.mail.password", mailPassword);
        }

        //邮件编码
        String mailDefaultEncoding = map.get("BLOG_MAIL_DEFAULT_ENCODING");
        if (mailDefaultEncoding != null) {
            System.setProperty("spring.mail.default-encoding", mailDefaultEncoding);
        }

    }

}
