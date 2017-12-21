package cn.eyeblue.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Configuration for email.
 */
@EnableConfigurationProperties(MailProperties.class)
@Configuration
public class MailConfiguration {

    /*****************************Email************************************/

    @Autowired
    private MailProperties mailProperties;

    /*

    //qq邮箱
spring.mail.protocol=smtps
spring.mail.host=smtp.exmail.qq.com
spring.mail.port=465
spring.mail.username=admin@neobay.cn
spring.mail.password=ne0Bay@123
spring.mail.default-encoding=UTF-8
spring.mail.properties.mail.smtps.auth=true
spring.mail.properties.mail.smtps.starttls.enable=true
spring.mail.properties.mail.smtps.debug=true

    //163邮箱
spring.mail.protocol=smtps
spring.mail.host=smtp.163.com
spring.mail.port=465
spring.mail.username=aitripedia@163.com
spring.mail.password=AitriPedia123
spring.mail.default-encoding=UTF-8
spring.mail.properties.mail.smtps.auth=true
spring.mail.properties.mail.smtps.starttls.enable=true
spring.mail.properties.mail.smtps.debug=true

    //126邮箱
spring.mail.protocol=smtps
spring.mail.host=smtp.126.com
spring.mail.port=465
spring.mail.username=ziclax@126.com
spring.mail.password=Ziclax123
spring.mail.default-encoding=UTF-8
spring.mail.properties.mail.smtps.auth=true
spring.mail.properties.mail.smtps.starttls.enable=true
spring.mail.properties.mail.smtps.debug=true

     */


    @Bean
    public JavaMailSenderImpl javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setDefaultEncoding(mailProperties.getDefaultEncoding().toString());
        mailSender.setProtocol(mailProperties.getProtocol());
        mailSender.setHost(mailProperties.getHost());
        mailSender.setPort(mailProperties.getPort());
        mailSender.setUsername(mailProperties.getUsername());
        mailSender.setPassword(mailProperties.getPassword());
        Properties properties = new Properties();

        properties.putAll(mailProperties.getProperties());

        mailSender.setJavaMailProperties(properties);

        return mailSender;
    }


}
