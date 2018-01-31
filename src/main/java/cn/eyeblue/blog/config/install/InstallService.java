package cn.eyeblue.blog.config.install;

import cn.eyeblue.blog.config.AppContextManager;
import cn.eyeblue.blog.rest.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.sql.*;

@Service
public class InstallService {

    @Autowired
    UserService userService;

    //开始安装
    public void start() throws Exception {

        this.checkTableExists();

    }



    //在本地迅速创建数据库和账号。
    /*
        create database blog;
        grant all privileges on blog.* to blog identified by 'blog123';
        flush privileges;
     */

    //检查数据库表是否完整。
    @Transactional
    public void checkTableExists() throws Exception {

        DataSource dataSource = AppContextManager.getBean(DataSource.class);
        Connection connection = DataSourceUtils.getConnection(dataSource);

        DatabaseMetaData meta = connection.getMetaData();
        ResultSet resultSet = null;
        //检查表：blog10_article
        resultSet = meta.getTables(null, null, "blog10_article", null);
        if (!resultSet.next()) {
            String createSql = "CREATE TABLE `blog10_article` (\n" +
                    "  `uuid` char(36) NOT NULL,\n" +
                    "  `user_uuid` char(36) DEFAULT NULL,\n" +
                    "  `title` varchar(255) DEFAULT NULL COMMENT '标题',\n" +
                    "  `tags` varchar(1024) DEFAULT NULL COMMENT '标签',\n" +
                    "  `poster_tank_uuid` char(36) DEFAULT NULL COMMENT '海报',\n" +
                    "  `poster_url` varchar(512) DEFAULT NULL COMMENT '海报Url',\n" +
                    "  `author` varchar(45) DEFAULT NULL,\n" +
                    "  `digest` varchar(512) DEFAULT NULL,\n" +
                    "  `is_markdown` tinyint(1) DEFAULT '1',\n" +
                    "  `markdown` mediumtext,\n" +
                    "  `html` mediumtext,\n" +
                    "  `privacy` tinyint(1) DEFAULT '0',\n" +
                    "  `top` tinyint(1) DEFAULT '0',\n" +
                    "  `agree` int(11) DEFAULT '0',\n" +
                    "  `words` int(11) DEFAULT '0',\n" +
                    "  `hit` int(11) DEFAULT '1',\n" +
                    "  `comment_num` int(11) DEFAULT '0',\n" +
                    "  `need_notify` tinyint(1) DEFAULT '1' COMMENT '是否接受通知',\n" +
                    "  `sort` bigint(20) DEFAULT '0',\n" +
                    "  `update_time` timestamp NULL DEFAULT NULL,\n" +
                    "  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
                    "  `deleted` tinyint(1) DEFAULT '0',\n" +
                    "  PRIMARY KEY (`uuid`),\n" +
                    "  UNIQUE KEY `id_UNIQUE` (`uuid`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文章表';";
            PreparedStatement preparedStatement = connection.prepareStatement(createSql);
            preparedStatement.execute();
        }

        //检查表：blog10_comment
        resultSet = meta.getTables(null, null, "blog10_comment", null);
        if (!resultSet.next()) {
            String createSql = "CREATE TABLE `blog10_comment` (\n" +
                    "  `uuid` char(36) NOT NULL,\n" +
                    "  `article_uuid` char(36) DEFAULT NULL,\n" +
                    "  `user_uuid` char(36) DEFAULT NULL COMMENT '站内用户的评论，一般都是管理员。匿名评论者为null',\n" +
                    "  `is_floor` tinyint(1) DEFAULT '1' COMMENT '是否是楼层',\n" +
                    "  `floor_uuid` char(36) DEFAULT NULL COMMENT '该评论的楼层uuid，如果自己本身是楼层的话，那么这个值为null',\n" +
                    "  `puuid` char(36) DEFAULT NULL COMMENT '自己回复的那条评论uuid，如果自己是楼层，那么这个值为null',\n" +
                    "  `name` varchar(45) DEFAULT NULL COMMENT '评论者姓名',\n" +
                    "  `avatar_url` varchar(255) DEFAULT NULL,\n" +
                    "  `email` varchar(45) DEFAULT NULL COMMENT '评论者电子邮件',\n" +
                    "  `content` text COMMENT '内容',\n" +
                    "  `agree` int(11) DEFAULT '0' COMMENT '赞同数',\n" +
                    "  `ip` varchar(45) DEFAULT NULL COMMENT '发出评论的ip地址',\n" +
                    "  `sort` bigint(20) DEFAULT '0',\n" +
                    "  `update_time` timestamp NULL DEFAULT NULL,\n" +
                    "  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
                    "  `deleted` tinyint(1) DEFAULT '0',\n" +
                    "  PRIMARY KEY (`uuid`),\n" +
                    "  UNIQUE KEY `id_UNIQUE` (`uuid`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='评论表';";
            PreparedStatement preparedStatement = connection.prepareStatement(createSql);
            preparedStatement.execute();
        }

        //检查表：blog10_history
        resultSet = meta.getTables(null, null, "blog10_history", null);
        if (!resultSet.next()) {
            String createSql = "CREATE TABLE `blog10_history` (\n" +
                    "  `uuid` char(36) NOT NULL,\n" +
                    "  `entity_uuid` char(36) DEFAULT NULL,\n" +
                    "  `entity_name` varchar(200) DEFAULT NULL,\n" +
                    "  `ip` varchar(45) DEFAULT NULL COMMENT '发出点赞的ip地址',\n" +
                    "  `type` varchar(45) DEFAULT NULL,\n" +
                    "  `sort` bigint(20) DEFAULT '0',\n" +
                    "  `update_time` timestamp NULL DEFAULT NULL,\n" +
                    "  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
                    "  `deleted` tinyint(1) DEFAULT '0',\n" +
                    "  PRIMARY KEY (`uuid`),\n" +
                    "  UNIQUE KEY `id_UNIQUE` (`uuid`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='点赞，访问历史表';";
            PreparedStatement preparedStatement = connection.prepareStatement(createSql);
            preparedStatement.execute();
        }

        //检查表：blog10_preference
        resultSet = meta.getTables(null, null, "blog10_preference", null);
        if (!resultSet.next()) {
            String createSql = "CREATE TABLE `blog10_preference` (\n" +
                    "  `uuid` char(36) NOT NULL,\n" +
                    "  `name` varchar(45) DEFAULT NULL COMMENT '网站名称',\n" +
                    "  `logo_url` varchar(255) DEFAULT NULL COMMENT '发出点赞的ip地址',\n" +
                    "  `logo_tank_uuid` char(36) DEFAULT NULL,\n" +
                    "  `favicon_url` varchar(255) DEFAULT NULL,\n" +
                    "  `favicon_tank_uuid` char(36) DEFAULT NULL,\n" +
                    "  `menu_name1` varchar(45) DEFAULT NULL,\n" +
                    "  `menu_url1` varchar(255) DEFAULT NULL,\n" +
                    "  `menu_name2` varchar(45) DEFAULT NULL,\n" +
                    "  `menu_url2` varchar(255) DEFAULT NULL,\n" +
                    "  `menu_name3` varchar(45) DEFAULT NULL,\n" +
                    "  `menu_url3` varchar(255) DEFAULT NULL,\n" +
                    "  `menu_name4` varchar(45) DEFAULT NULL,\n" +
                    "  `menu_url4` varchar(255) DEFAULT NULL,\n" +
                    "  `menu_name5` varchar(45) DEFAULT NULL,\n" +
                    "  `menu_url5` varchar(255) DEFAULT NULL,\n" +
                    "  `footer_line1` varchar(1024) DEFAULT NULL COMMENT '底部第一行',\n" +
                    "  `footer_line2` varchar(1024) DEFAULT NULL COMMENT '底部第二行',\n" +
                    "  `version` varchar(45) DEFAULT NULL,\n" +
                    "  `sort` bigint(20) DEFAULT '0',\n" +
                    "  `update_time` timestamp NULL DEFAULT NULL,\n" +
                    "  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
                    "  `deleted` tinyint(1) DEFAULT '0',\n" +
                    "  PRIMARY KEY (`uuid`),\n" +
                    "  UNIQUE KEY `id_UNIQUE` (`uuid`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='网站中所有的设置项。';";
            PreparedStatement preparedStatement = connection.prepareStatement(createSql);
            preparedStatement.execute();
        }

        //检查表：blog10_report
        resultSet = meta.getTables(null, null, "blog10_report", null);
        if (!resultSet.next()) {
            String createSql = "CREATE TABLE `blog10_report` (\n" +
                    "  `uuid` char(36) NOT NULL,\n" +
                    "  `entity_uuid` char(36) DEFAULT NULL,\n" +
                    "  `entity_name` varchar(200) DEFAULT NULL,\n" +
                    "  `ip` varchar(45) DEFAULT NULL COMMENT '举报者的ip地址',\n" +
                    "  `type` varchar(45) DEFAULT NULL,\n" +
                    "  `content` varchar(200) DEFAULT NULL,\n" +
                    "  `sort` bigint(20) DEFAULT '0',\n" +
                    "  `update_time` timestamp NULL DEFAULT NULL,\n" +
                    "  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
                    "  `deleted` tinyint(1) DEFAULT '0',\n" +
                    "  PRIMARY KEY (`uuid`),\n" +
                    "  UNIQUE KEY `id_UNIQUE` (`uuid`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='举报表';";
            PreparedStatement preparedStatement = connection.prepareStatement(createSql);
            preparedStatement.execute();
        }

        //检查表：blog10_support_captcha
        resultSet = meta.getTables(null, null, "blog10_support_captcha", null);
        if (!resultSet.next()) {
            String createSql = "CREATE TABLE `blog10_support_captcha` (\n" +
                    "  `uuid` char(36) NOT NULL,\n" +
                    "  `session_id` varchar(45) DEFAULT NULL COMMENT 'session_id',\n" +
                    "  `value` varchar(45) DEFAULT NULL COMMENT '验证码值',\n" +
                    "  `expire_time` timestamp NULL DEFAULT NULL,\n" +
                    "  `sort` bigint(20) DEFAULT '0',\n" +
                    "  `update_time` timestamp NULL DEFAULT NULL,\n" +
                    "  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
                    "  `deleted` tinyint(1) DEFAULT '0',\n" +
                    "  PRIMARY KEY (`uuid`),\n" +
                    "  UNIQUE KEY `id_UNIQUE` (`uuid`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='captcha表';";
            PreparedStatement preparedStatement = connection.prepareStatement(createSql);
            preparedStatement.execute();
        }

        //检查表：blog10_support_session
        resultSet = meta.getTables(null, null, "blog10_support_session", null);
        if (!resultSet.next()) {
            String createSql = "CREATE TABLE `blog10_support_session` (\n" +
                    "  `uuid` char(36) NOT NULL,\n" +
                    "  `user_uuid` char(36) DEFAULT NULL COMMENT '用户uuid',\n" +
                    "  `ip` varchar(45) DEFAULT NULL COMMENT '用户的ip地址',\n" +
                    "  `expire_time` timestamp NULL DEFAULT NULL,\n" +
                    "  `sort` bigint(20) DEFAULT '0',\n" +
                    "  `update_time` timestamp NULL DEFAULT NULL,\n" +
                    "  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
                    "  `deleted` tinyint(1) DEFAULT '0',\n" +
                    "  PRIMARY KEY (`uuid`),\n" +
                    "  UNIQUE KEY `id_UNIQUE` (`uuid`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='session表';";
            PreparedStatement preparedStatement = connection.prepareStatement(createSql);
            preparedStatement.execute();
        }

        //检查表：blog10_support_validation
        resultSet = meta.getTables(null, null, "blog10_support_validation", null);
        if (!resultSet.next()) {
            String createSql = "CREATE TABLE `blog10_support_validation` (\n" +
                    "  `uuid` char(36) NOT NULL,\n" +
                    "  `user_uuid` char(36) DEFAULT NULL COMMENT '用户uuid',\n" +
                    "  `email` varchar(45) DEFAULT NULL COMMENT '用户的ip地址',\n" +
                    "  `code` char(32) DEFAULT NULL,\n" +
                    "  `type` varchar(45) DEFAULT 'VALIDATION',\n" +
                    "  `sort` bigint(20) DEFAULT '0',\n" +
                    "  `update_time` timestamp NULL DEFAULT NULL,\n" +
                    "  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
                    "  `deleted` tinyint(1) DEFAULT '0',\n" +
                    "  PRIMARY KEY (`uuid`),\n" +
                    "  UNIQUE KEY `id_UNIQUE` (`uuid`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='验证邮箱表';";
            PreparedStatement preparedStatement = connection.prepareStatement(createSql);
            preparedStatement.execute();
        }

        //检查表：blog10_tag
        resultSet = meta.getTables(null, null, "blog10_tag", null);
        if (!resultSet.next()) {
            String createSql = "CREATE TABLE `blog10_tag` (\n" +
                    "  `uuid` char(36) NOT NULL,\n" +
                    "  `user_uuid` char(36) DEFAULT NULL,\n" +
                    "  `name` varchar(255) DEFAULT NULL COMMENT '名称',\n" +
                    "  `logo_tank_uuid` char(36) DEFAULT NULL COMMENT 'logo',\n" +
                    "  `logo_url` varchar(512) DEFAULT NULL COMMENT 'logo Url',\n" +
                    "  `sort` bigint(20) DEFAULT '0',\n" +
                    "  `update_time` timestamp NULL DEFAULT NULL,\n" +
                    "  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
                    "  `deleted` tinyint(1) DEFAULT '0',\n" +
                    "  PRIMARY KEY (`uuid`),\n" +
                    "  UNIQUE KEY `id_UNIQUE` (`uuid`),\n" +
                    "  UNIQUE KEY `name_UNIQUE` (`name`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文章标签表';";
            PreparedStatement preparedStatement = connection.prepareStatement(createSql);
            preparedStatement.execute();
        }

        //检查表：blog10_tank
        resultSet = meta.getTables(null, null, "blog10_tank", null);
        if (!resultSet.next()) {
            String createSql = "CREATE TABLE `blog10_tank` (\n" +
                    "  `uuid` char(36) NOT NULL,\n" +
                    "  `user_uuid` char(36) DEFAULT NULL COMMENT '上传的用户id',\n" +
                    "  `name` varchar(255) DEFAULT NULL COMMENT '文件名称',\n" +
                    "  `matter_uuid` char(36) DEFAULT NULL COMMENT '这个是在tank服务器上的唯一标识',\n" +
                    "  `size` bigint(20) DEFAULT '0' COMMENT '文件大小',\n" +
                    "  `privacy` tinyint(1) DEFAULT '0' COMMENT '文件是否是公有的',\n" +
                    "  `url` varchar(255) DEFAULT NULL,\n" +
                    "  `remark` varchar(512) DEFAULT NULL,\n" +
                    "  `confirmed` tinyint(1) DEFAULT '0',\n" +
                    "  `sort` bigint(20) NOT NULL DEFAULT '0',\n" +
                    "  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                    "  `update_time` timestamp NULL DEFAULT NULL,\n" +
                    "  `deleted` tinyint(1) DEFAULT '0' COMMENT '是否已经删除',\n" +
                    "  PRIMARY KEY (`uuid`),\n" +
                    "  UNIQUE KEY `id_UNIQUE` (`uuid`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文件描述表';";
            PreparedStatement preparedStatement = connection.prepareStatement(createSql);
            preparedStatement.execute();
        }

        //检查表：blog10_user
        resultSet = meta.getTables(null, null, "blog10_user", null);
        if (!resultSet.next()) {
            String createSql = "CREATE TABLE `blog10_user` (\n" +
                    "  `uuid` char(36) NOT NULL,\n" +
                    "  `username` varchar(255) DEFAULT NULL COMMENT '昵称',\n" +
                    "  `password` varchar(255) DEFAULT NULL COMMENT '密码',\n" +
                    "  `role` varchar(45) DEFAULT 'USER',\n" +
                    "  `email` varchar(45) DEFAULT NULL COMMENT '邮箱',\n" +
                    "  `phone` varchar(45) DEFAULT NULL COMMENT '电话',\n" +
                    "  `gender` varchar(45) DEFAULT 'UNKNOWN' COMMENT '性别，默认未知',\n" +
                    "  `city` varchar(45) DEFAULT NULL COMMENT '城市',\n" +
                    "  `description` varchar(512) DEFAULT NULL,\n" +
                    "  `avatar_tank_uuid` char(36) DEFAULT NULL,\n" +
                    "  `avatar_url` varchar(255) DEFAULT NULL COMMENT '头像链接',\n" +
                    "  `last_ip` varchar(255) DEFAULT NULL COMMENT '上次登录ip',\n" +
                    "  `last_time` datetime DEFAULT NULL COMMENT '上次登录使劲按',\n" +
                    "  `email_validate` tinyint(1) DEFAULT '0',\n" +
                    "  `sort` bigint(20) DEFAULT '0',\n" +
                    "  `update_time` timestamp NULL DEFAULT NULL,\n" +
                    "  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
                    "  `deleted` tinyint(1) DEFAULT '0',\n" +
                    "  PRIMARY KEY (`uuid`),\n" +
                    "  UNIQUE KEY `id_UNIQUE` (`uuid`),\n" +
                    "  UNIQUE KEY `email_UNIQUE` (`email`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表描述';";
            PreparedStatement preparedStatement = connection.prepareStatement(createSql);
            preparedStatement.execute();


            //初始化超级管理员
            userService.initAdmin();
        }

        //检查表：blog10_user_knock
        resultSet = meta.getTables(null, null, "blog10_user_knock", null);
        if (!resultSet.next()) {
            String createSql = "CREATE TABLE `blog10_user_knock` (\n" +
                    "  `uuid` char(36) NOT NULL,\n" +
                    "  `session_id` varchar(45) DEFAULT NULL,\n" +
                    "  `user_uuid` char(36) DEFAULT NULL,\n" +
                    "  `username` varchar(45) DEFAULT NULL,\n" +
                    "  `password` varchar(45) DEFAULT NULL,\n" +
                    "  `ip` varchar(45) DEFAULT NULL,\n" +
                    "  `address` varchar(255) DEFAULT NULL,\n" +
                    "  `type` varchar(45) DEFAULT 'OK',\n" +
                    "  `sort` bigint(20) NOT NULL DEFAULT '0',\n" +
                    "  `update_time` timestamp NULL DEFAULT NULL,\n" +
                    "  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                    "  `deleted` tinyint(1) DEFAULT '0' COMMENT '是否已经删除',\n" +
                    "  PRIMARY KEY (`uuid`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='敲门表： 用户登录情况计略，可以统计登陆失败次数.可以帮助我们找出黑客。';";
            PreparedStatement preparedStatement = connection.prepareStatement(createSql);
            preparedStatement.execute();
        }

    }


}
