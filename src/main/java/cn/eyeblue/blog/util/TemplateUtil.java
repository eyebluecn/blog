package cn.eyeblue.blog.util;

import cn.eyeblue.blog.config.exception.UtilException;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import static freemarker.template.Configuration.VERSION_2_3_27;

/**
 * 专门用来渲染模板的。
 */
public class TemplateUtil {

    //Freemarker提供的模板引擎
    public static String render(String content, Map<String, Object> context) {

        try {

            Configuration cfg = new Configuration(VERSION_2_3_27);
            cfg.setObjectWrapper(new DefaultObjectWrapper(VERSION_2_3_27));

            Template t = new Template(TemplateUtil.class.getSimpleName(), new StringReader(content), cfg);

            Writer out = new StringWriter();
            t.process(context, out);

            return out.toString();

        } catch (Throwable e) {

            throw new UtilException(e);
        }

    }
}
