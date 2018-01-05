package cn.eyeblue.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

//启用异步操作。
@EnableAsync
@SpringBootApplication
public class BlogApplication {

	public static void main(String[] args) {

		//在本地迅速创建数据库和账号。
		/*
			create database blog;
			grant all privileges on blog.* to blog identified by 'blog123';
			flush privileges;
		 */

		SpringApplication.run(BlogApplication.class, args);
	}
}
