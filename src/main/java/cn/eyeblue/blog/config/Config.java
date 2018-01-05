package cn.eyeblue.blog.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 顺便配置了web初始化的时候的工作。
 */
@Configuration
@Getter
public class Config {

    @Value("${spring.profiles.active}")
    private String profilesActive;


    public boolean isDebug() {
        return "dev".equals(this.profilesActive);
    }

}
