package cn.eyeblue.blog.rest.base;


import cn.eyeblue.blog.config.AppContextManager;
import cn.eyeblue.blog.config.exception.UtilException;
import cn.eyeblue.blog.rest.user.User;

public abstract class BaseEntityForm<E extends BaseEntity> extends BaseForm {

    private Class<E> clazz;

    public BaseEntityForm(Class<E> clazz) {
        this.clazz = clazz;
    }

    protected abstract void update(E entity, User operator);

    public E create(User operator) {

        E entity;
        try {
            entity = clazz.newInstance();
        } catch (Exception e) {
            throw new UtilException("构建实体时出错！");
        }
        this.update(entity, operator);
        return entity;
    }

    public E get(User operator) {

        E entity = this.check(uuid);

        this.update(entity, operator);

        return entity;
    }


    /**
     * 从数据库中检出一个当前泛型的实例。
     * 找不到抛异常。
     */
    public E check(String uuid) {

        return AppContextManager.check(this.clazz, uuid);
    }

    /**
     * 从数据库中找出一个当前泛型的实例。
     */
    public E find(String uuid) {
        return AppContextManager.find(this.clazz, uuid);
    }

}
