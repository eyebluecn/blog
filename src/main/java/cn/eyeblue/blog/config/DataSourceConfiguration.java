package cn.eyeblue.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * 动态配置数据源：http://www.bswen.com/2018/05/springboot-How-to-setup-datasources-dynamically-in-springboot.html
 */
@Configuration
public class DataSourceConfiguration {

    @Autowired
    Config config;

    /**
     * spring.datasource.url=jdbc:mysql://eyeblue.cn:3306/blog_dev?useUnicode=true&characterEncoding=UTF-8&useSSL=false
     * spring.datasource.username=blog_dev
     * spring.datasource.password=Blog_dev_123
     * spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
     */


//    @Bean
//    public DataSource dataSource() {
//
//        System.out.println("开始配置数据源了。");
////        return DataSourceBuilder
////                .create()
////                .username("blog_dev")
////                .password("Blog_dev_123")
////                .url("jdbc:mysql://eyeblue.cn:3306/blog_dev?useUnicode=true&characterEncoding=UTF-8&useSSL=false")
////                .driverClassName("com.mysql.cj.jdbc.Driver")
////                .build();
//
//        return DataSourceBuilder
//                .create()
//                .username("")
//                .password("")
//                .url("")
//                .driverClassName("")
//                .build();
//    }


}
