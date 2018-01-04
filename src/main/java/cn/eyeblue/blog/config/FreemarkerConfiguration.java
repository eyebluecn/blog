package cn.eyeblue.blog.config;

import cn.eyeblue.blog.util.PathUtil;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import kr.pe.kwonnam.freemarker.inheritance.BlockDirective;
import kr.pe.kwonnam.freemarker.inheritance.ExtendsDirective;
import kr.pe.kwonnam.freemarker.inheritance.PutDirective;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 */
@Configuration
@EnableWebMvc
public class FreemarkerConfiguration extends WebMvcConfigurerAdapter {


    @Autowired
    Config config;

    @Bean
    public ViewResolver viewResolver() {
        FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
        resolver.setCache(false);
        resolver.setPrefix("");
        resolver.setSuffix(".ftl");
        resolver.setContentType("text/html; charset=UTF-8");
        return resolver;
    }


    @Bean
    public FreeMarkerConfigurer freemarkerConfig() throws IOException, TemplateException {
        FreeMarkerConfigurationFactory factory = new FreeMarkerConfigurationFactory();

        if (config.isDebug()) {
            String uri1 = "file:" + PathUtil.getSrcResourcesRootPath() + "/lobby";
            String uri2 = "file:" + PathUtil.getSrcResourcesRootPath() + "/bystatic";
            factory.setTemplateLoaderPaths(uri1, uri2);
        } else {
            factory.setTemplateLoaderPaths("classpath:lobby", "src/main/resources/lobby", "classpath:bystatic", "src/main/resources/bystatic");
        }

        factory.setDefaultEncoding("UTF-8");


        //添加模板继承的标签
        Map<String, Object> freemarkerVariables = new HashMap<String, Object>();
        freemarkerVariables.put("layout", freemarkerLayoutDirectives());
        factory.setFreemarkerVariables(freemarkerVariables);


        FreeMarkerConfigurer result = new FreeMarkerConfigurer();
        result.setConfiguration(factory.createConfiguration());

        //result.getConfiguration().setClassicCompatible(true);

        return result;
    }

    @Bean
    public Map<String, TemplateModel> freemarkerLayoutDirectives() {
        Map<String, TemplateModel> freemarkerLayoutDirectives = new HashMap<String, TemplateModel>();
        freemarkerLayoutDirectives.put("extends", new ExtendsDirective());
        freemarkerLayoutDirectives.put("block", new BlockDirective());
        freemarkerLayoutDirectives.put("put", new PutDirective());

        return freemarkerLayoutDirectives;
    }


}
