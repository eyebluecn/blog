package cn.eyeblue.blog.util;

import cn.eyeblue.blog.config.AppContextManager;
import cn.eyeblue.blog.config.exception.UtilException;
import cn.eyeblue.blog.rest.base.BaseEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class JsonUtil {


    public static <T> T toObject(String content, Class<T> clazz) {

        ObjectMapper mapper = AppContextManager.getBean(ObjectMapper.class);
        try {

            return mapper.readValue(content, clazz);

        } catch (IOException e) {

            throw new UtilException("无法将json字符串转换为对象！");
        }
    }


    //从一个jsonString中去获取对应类型的对象
    public static <T> T toGenericObject(TypeReference<T> typeReference, String jsonString) {

        if (jsonString == null) {
            throw new UtilException("jsonString必须指定");
        }

        ObjectMapper objectMapper = AppContextManager.getBean(ObjectMapper.class);
        try {
            return objectMapper.readValue(jsonString, typeReference);
        } catch (IOException e) {
            log.warn(e.getMessage());
            throw new UtilException("信息有误，无法从字符串转成对象！");
        }

    }

    //从一个jsonString中去获取List<Long>
    public static List<Long> toLongList(String jsonString) {


        if (jsonString == null) {
            return new ArrayList<>();
        }

        return toGenericObject(new TypeReference<List<Long>>() {
        }, jsonString);
    }

    //从一个jsonString中去获取List<String>
    public static List<String> toStringList(String jsonString) {
        if (jsonString == null) {
            return new ArrayList<>();
        }

        return toGenericObject(new TypeReference<List<String>>() {
        }, jsonString);
    }


    //检出一个指定类型的实例列表。找不到抛异常。ids为json字符串，例如[1,5]
    public static <T extends BaseEntity> List<T> checkList(Class<T> clazz, String idsString) {

        if (idsString == null) {
            throw new UtilException("id必须指定");
        }

        List<String> idList = toStringList(idsString);

        List<T> entityList = new ArrayList<>();
        for (String id : idList) {
            T entity = AppContextManager.check(clazz, id);
            entityList.add(entity);
        }
        return entityList;
    }

    //找出一个指定类型的实例。找不到返回null。ids为json字符串，例如[1,5]
    public static <T extends BaseEntity> List<T> findList(Class<T> clazz, String idsString) {

        if (idsString == null) {
            return null;
        }

        ObjectMapper objectMapper = AppContextManager.getBean(ObjectMapper.class);

        TypeReference<List<Long>> longListTypeReference = new TypeReference<List<Long>>() {
        };
        List<String> idList;
        List<T> entityList = new ArrayList<>();
        try {
            idList = objectMapper.readValue(idsString, longListTypeReference);
            for (String tankId : idList) {
                T entity = AppContextManager.find(clazz, tankId);
                if (entity == null) {
                    return null;
                }
                entityList.add(entity);
            }
        } catch (IOException e) {
            log.error("信息有误，无法获取到对象数组！");
            return null;
        }
        return entityList;
    }

    //将对象转换成json字符串。
    public static String toJson(Object obj) {
        ObjectMapper objectMapper = AppContextManager.getBean(ObjectMapper.class);

        try {

            return objectMapper.writeValueAsString(obj);

        } catch (JsonProcessingException e) {

            log.error(obj.toString());
            throw new UtilException("json字符串转换时出错！");

        }
    }

    //将一个对象转换成 Map<String,Object>
    public static Map<String, Object> toMap(Object obj) {
        ObjectMapper mapper = AppContextManager.getBean(ObjectMapper.class);

        TypeReference<Map<String, Object>> typeReference = new TypeReference<Map<String, Object>>() {
        };

        return mapper.convertValue(obj, typeReference);
    }


}
