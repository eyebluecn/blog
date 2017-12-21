package cn.eyeblue.blog.config.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class ApplicationBootstrap implements ApplicationListener<ContextRefreshedEvent> {


    //整个服务器启动的时候。
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        log.info("---------------------Bootstrap------------------------------");

    }
}
