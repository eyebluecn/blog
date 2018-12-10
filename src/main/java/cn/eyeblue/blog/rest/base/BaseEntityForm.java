package cn.eyeblue.blog.rest.base;


import cn.eyeblue.blog.config.AppContextManager;
import cn.eyeblue.blog.config.exception.BadRequestException;
import cn.eyeblue.blog.config.exception.UtilException;
import cn.eyeblue.blog.rest.user.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.Set;

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

    /**
     * 手动验证这个form的正确性
     */
    public void validate() {
        //手动验证
        int errorNum = 0;
        String errorMessage = "检测到错误信息。";
        Set<ConstraintViolation<BaseEntityForm<E>>> constraintViolationSet = Validation.buildDefaultValidatorFactory().getValidator().validate(this);
        for (ConstraintViolation<BaseEntityForm<E>> item : constraintViolationSet) {
            errorNum++;
            errorMessage += "您填写的“" + item.getPropertyPath() + "”不符合要求：" + item.getMessage() + ";";
        }

        if (errorNum != 0) {
            throw new BadRequestException(errorMessage);
        }
    }

}
