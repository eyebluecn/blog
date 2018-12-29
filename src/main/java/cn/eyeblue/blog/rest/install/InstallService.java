package cn.eyeblue.blog.rest.install;

import cn.eyeblue.blog.config.NamingStrategyConfiguration;
import cn.eyeblue.blog.config.exception.BadRequestException;
import cn.eyeblue.blog.rest.article.Article;
import cn.eyeblue.blog.rest.install.hibernate.SchemaHelper;
import cn.eyeblue.blog.rest.install.hibernate.SchemaValidatorHelper;
import cn.eyeblue.blog.util.StringUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.hibernate.tool.schema.TargetType;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//整理成文章：1.Hibernate中如何根据Entity来生成建表语句。 2.Hibernate中如何获取表的缺失字段。
@Slf4j
@Service
public class InstallService {


    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {

                //采用代码方式配置Hibernate
                StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();

                Map<String, String> settings = new HashMap<>();

                //配置数据源相关信息
                settings.put(Environment.DRIVER, com.mysql.cj.jdbc.Driver.class.getName());
                settings.put(Environment.URL, "jdbc:mysql://localhost:3306/blog?useUnicode=true&characterEncoding=UTF-8&useSSL=false");
                settings.put(Environment.USER, "root");
                settings.put(Environment.PASS, "lishmoshou511");

                //指定连接方言
                settings.put(Environment.DIALECT, org.hibernate.dialect.MySQL5Dialect.class.getName());
                //指定数据库引擎
                settings.put(Environment.STORAGE_ENGINE, "innodb");

                //自动建表
                settings.put(Environment.HBM2DDL_AUTO, "none");

                //打印SQL语句
                settings.put(Environment.SHOW_SQL, "true");
                settings.put(Environment.FORMAT_SQL, "true");

                //自动命名策略
                settings.put(Environment.PHYSICAL_NAMING_STRATEGY, NamingStrategyConfiguration.class.getName());


                registryBuilder.applySettings(settings);

                registry = registryBuilder.build();

                MetadataSources sources = new MetadataSources(registry);


                //添加上注解的实体
                sources.addAnnotatedClass(Article.class);


                // Create Metadata
                Metadata metadata = sources.getMetadataBuilder().build();


                // Create SessionFactory
                sessionFactory = metadata.getSessionFactoryBuilder().build();


                //在这里我们来搞点儿花样出来(这里可以删掉)
                doSomething(metadata);


            } catch (Exception e) {
                e.printStackTrace();
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    //输出实体的更新语句
    public static void doSomething(Metadata metadata) {

        //获取建表或者修改表的语句。
        String sql = SchemaHelper.getDdlSql(EnumSet.of(TargetType.STDOUT), metadata, registry);
        System.out.println("DDL语句为：" + sql);

        //验证表结构的完整性
        SchemaValidatorHelper.validate(EnumSet.of(TargetType.STDOUT), metadata, registry);
    }


    public static void main(String[] args) {

        SessionFactory sessionFactory = getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.getTransaction().commit();
        session.close();
        shutdown();


    }


    //ping一下jdbc连接，测试是否通
    @SneakyThrows
    public void ping(Install install) {

        int flag = 1;
        Class.forName(com.mysql.cj.jdbc.Driver.class.getName());

        try (
                Connection connection = DriverManager.getConnection(
                        StringUtil.format("jdbc:mysql://{}:{}/{}?useUnicode=true&characterEncoding=UTF-8&useSSL=false", install.getMysqlHost(), install.getMysqlPort(), install.getMysqlSchema()),
                        install.getMysqlUsername(),
                        install.getMysqlPassword()
                );
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT " + flag);
        ) {

            while (resultSet.next()) {

                if (flag != resultSet.getInt(1)) {
                    throw new BadRequestException("数据库连接失败");
                }
            }

        } catch (Throwable throwable) {

            log.error("mysql连接不通", throwable);
            throw new BadRequestException("mysql连接不通：{}", ExceptionUtils.getRootCauseMessage(throwable));
        }


    }


    public List<InstallTableInfo> getTableInfo(Install install) {

        //ping一下数据库
        this.ping(install);

        return null;
    }

}
