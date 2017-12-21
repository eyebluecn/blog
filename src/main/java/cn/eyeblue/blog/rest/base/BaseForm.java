package cn.eyeblue.blog.rest.base;

import cn.eyeblue.blog.config.AppContextManager;
import lombok.Getter;
import lombok.Setter;


public abstract class BaseForm {


    @Getter
    @Setter
    protected String uuid;

    //检出一个指定类型的实例。找不到抛异常。
    protected <T extends BaseEntity> T check(Class<T> clazz, String uuid) {
        return AppContextManager.check(clazz, uuid);
    }

    //找出一个指定类型的实例。找不到返回null
    protected <T extends BaseEntity> T find(Class<T> clazz, String uuid) {
        return AppContextManager.find(clazz, uuid);
    }

}
