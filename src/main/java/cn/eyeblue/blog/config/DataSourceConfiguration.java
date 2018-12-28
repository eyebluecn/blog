package cn.eyeblue.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 动态配置数据源：http://www.bswen.com/2018/05/springboot-How-to-setup-datasources-dynamically-in-springboot.html
 */
@Configuration
public class DataSourceConfiguration extends AbstractRoutingDataSource {

    @Autowired
    Config config;


    private String currentKey = "h2";
    private Map<Object, Object> dataSourceMap = new HashMap<>();

    public DataSourceConfiguration() {

        System.out.println("初始化数据源路由");

        DataSource dataSource1 = DataSourceBuilder
                .create()
                .username("root")
                .password("123456")
                .url("jdbc:h2:mem:h2test")
                .driverClassName("org.h2.Driver")
                .build();


        dataSourceMap.put("h2", dataSource1);

        this.setTargetDataSources(dataSourceMap);

    }

    public void addDataSource() {

        DataSource mysqlDataSource = DataSourceBuilder
                .create()
                .username("blog_dev")
                .password("Blog_dev_123")
                .url("jdbc:mysql://eyeblue.cn:3306/blog_dev?useUnicode=true&characterEncoding=UTF-8&useSSL=false")
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();

        dataSourceMap.put("mysql", mysqlDataSource);

        this.currentKey = "mysql";

        this.afterPropertiesSet();
    }


    @Override
    protected Object determineCurrentLookupKey() {

        System.out.println("determineCurrentLookupKey h2");
        return currentKey;
    }

}
