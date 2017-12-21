package cn.eyeblue.blog.rest.base;

import cn.eyeblue.blog.config.AppContextManager;
import cn.eyeblue.blog.rest.user.User;
import org.springframework.transaction.annotation.Transactional;


public abstract class BaseEntityService<E extends BaseEntity> extends BaseService {


    private Class<E> clazz;

    protected BaseEntityDao<E> getDao() {
        return AppContextManager.getBaseEntityDao(clazz);
    }

    public BaseEntityService(Class<E> clazz) {
        this.clazz = clazz;
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

    @Transactional
    public E create(BaseEntityForm<E> form, User operator) {

        E entity = form.create(operator);
        getDao().save(entity);
        return entity;
    }

    @Transactional
    public void del(E entity, User operator) {

        entity.setDeleted(true);
        getDao().save(entity);

    }

    @Transactional
    public E edit(BaseEntityForm<E> form, User operator) {

        E entity = form.get(operator);
        getDao().save(entity);
        return entity;
    }


    @Transactional
    public E save(E entity) {

        entity = getDao().save(entity);

        return entity;
    }


}
