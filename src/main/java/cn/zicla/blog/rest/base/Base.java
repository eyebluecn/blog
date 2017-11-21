package cn.zicla.blog.rest.base;

import cn.zicla.blog.util.JsonUtil;
import cn.zicla.blog.util.StringUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.CaseFormat;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Model @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})可以让jackson忽略那些hibernate正在懒加载的字段。
 */
public abstract class Base implements Serializable {

    //获取当前对象的TAG和TAGS
    @JsonIgnore
    public String getTAG() {
        return Base.getTAG(this.getClass());
    }

    @JsonIgnore
    public String getTAGS() {
        return StringUtil.toPlural(this.getTAG());
    }

    //获取当前对象的TAG和TAGS
    @JsonIgnore
    public static String getTAG(Class<?> clazz) {

        String name = clazz.getSimpleName();
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, name);
    }

    @JsonIgnore
    public static String getTAGS(Class<?> clazz) {
        return StringUtil.toPlural(Base.getTAG(clazz));
    }


    /**
     * 极简模式，只返回关键数据。一般用于 小权限数据，或者是为了加快接口速度。
     */
    public Map<String, Object> simpleMap() {
        return new HashMap<>();
    }


    public Map<String, Object> map() {

        return JsonUtil.toMap(this);
    }


    public Map<String, Object> detailMap() {

        return this.map();
    }

    //将map中的所有字段转换成json字符串
    public String toJsonString() {
        return JsonUtil.toJson(this);
    }

    //将simpleMap中的所有字段转换成json字符串
    public String toSimpleJsonString() {
        return JsonUtil.toJson(this.simpleMap());
    }


}
