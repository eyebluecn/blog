package cn.eyeblue.blog.config;

import cn.eyeblue.blog.interceptor.AuthInterceptor;
import cn.eyeblue.blog.interceptor.SecurityInterceptor;
import cn.eyeblue.blog.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableWebMvc
public class ServerConfiguration implements WebMvcConfigurer {

    @Autowired
    Config config;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加权限拦截器
        registry.addInterceptor(new AuthInterceptor());
        //添加监控拦截器
        registry.addInterceptor(new SecurityInterceptor());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        //Freemarker使用到的
        String[] staticLocations1 = new String[1];
        if (config.isDebug()) {
            staticLocations1[0] = "file://" + PathUtil.getSrcResourcesRootPath() + "/static/";
        } else {
            staticLocations1[0] = "classpath:/static/";
        }
        registry.addResourceHandler("/static/**").addResourceLocations(staticLocations1);


        //后台前端使用到的。
        String[] staticLocations2 = new String[1];
        if (config.isDebug()) {
            staticLocations2[0] = "file://" + PathUtil.getSrcResourcesRootPath() + "/bystatic/";
        } else {
            staticLocations2[0] = "classpath:/bystatic/";
        }
        registry.addResourceHandler("/bystatic/**").addResourceLocations(staticLocations2);

    }
}
