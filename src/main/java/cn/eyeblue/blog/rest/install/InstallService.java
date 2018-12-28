package cn.eyeblue.blog.rest.install;

import cn.eyeblue.blog.config.exception.BadRequestException;
import cn.eyeblue.blog.util.StringUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@Slf4j
@Service
public class InstallService {


    //ping一下jdbc连接，测试是否通
    @SneakyThrows
    public void ping(Install install) {

        int flag = 1;
        Class.forName(com.mysql.cj.jdbc.Driver.class.getName());

        try (
                Connection connection = DriverManager.getConnection(
                        StringUtil.format("jdbc:mysql://{}:{}/{}", install.getMysqlHost(), install.getMysqlPort(), install.getMysqlSchema()),
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


    public void getTableInfo() {

        //获取加载配置管理类
        Configuration configuration = new Configuration();

        //不给参数就默认加载hibernate.cfg.xml文件，

        configuration.configure();

        //创建Session工厂对象
        SessionFactory factory = configuration.buildSessionFactory();


        //得到Session对象
        Session session = factory.openSession();

        //使用Hibernate操作数据库，都要开启事务,得到事务对象
        Transaction transaction = session.getTransaction();

        //开启事务
        transaction.begin();

        //把对象添加到数据库中
        //session.save(user);

        //提交事务
        transaction.commit();

        //关闭Session
        session.close();


    }

}
