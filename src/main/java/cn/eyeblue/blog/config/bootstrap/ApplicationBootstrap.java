package cn.eyeblue.blog.config.bootstrap;

import cn.eyeblue.blog.config.Config;
import cn.eyeblue.blog.util.PathUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class ApplicationBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    Config config;

    //整个服务器启动的时候。
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        log.info("---------------------Bootstrap Blog " + config.getBlogVersion() + "------------------------------");

        log.info("AppPath = {}", PathUtil.getAppPath());

        log.info("ClassesPath = {}", PathUtil.getClassesPath());

    }
}
