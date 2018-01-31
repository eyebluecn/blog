package cn.eyeblue.blog;

import cn.eyeblue.blog.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

//启用异步操作。
@EnableAsync
@SpringBootApplication
public class BlogApplication {

    public static void main(String[] args) {


        //从环境变量中加载配置项。
        Config.loadConfigFromEnvironment();


        SpringApplication.run(BlogApplication.class, args);
    }
}
