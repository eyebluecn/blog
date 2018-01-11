package cn.eyeblue.blog.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 顺便配置了web初始化的时候的工作。
 */
@Configuration
@Getter
public class Config {

    @Value("${server.debug}")
    private Boolean serverDebug;

    @Value("blog.version")
    private String blogVersion;

    public boolean isDebug() {
        return serverDebug;
    }

}
