package cn.zicla.blog.config;

import cn.zicla.blog.interceptor.AuthInterceptor;
import cn.zicla.blog.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
@EnableWebMvc
public class ServerConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    Config config;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new AuthInterceptor());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String[] CLASSPATH_RESOURCE_LOCATIONS = new String[1];
        if (config.isDebug()) {
            CLASSPATH_RESOURCE_LOCATIONS[0] = "file://" + PathUtil.getSrcResourcesRootPath() + "/static/";
        } else {
            CLASSPATH_RESOURCE_LOCATIONS[0] = "classpath:/static/";
        }
        registry.addResourceHandler("/static/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
    }
}
