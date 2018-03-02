package cn.eyeblue.blog.config;

import cn.eyeblue.blog.config.exception.NotFoundException;
import cn.eyeblue.blog.config.exception.UtilException;
import cn.eyeblue.blog.rest.base.BaseEntity;
import cn.eyeblue.blog.rest.base.BaseEntityDao;
import cn.eyeblue.blog.rest.base.BaseEntityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.ResolvableType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * 一个用作搭建Spring Bean和非Spring Bean的桥梁
 */
@Service
@Slf4j
public class AppContextManager implements ApplicationContextAware {
    private static ApplicationContext appCtx;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appCtx = applicationContext;
    }

    public static ApplicationContext getAppContext() {
        return appCtx;
    }


    public static <T> T getBean(Class<T> requiredType) throws BeansException {
        return getAppContext().getBean(requiredType);
    }

    /**
     * 获取带泛型的bean. 例如: CrudRepository<Article,Long> 那么传入参数：CrudRepository.class, Article.class, Long.class
     */
    public static Object getGenericBean(Class<?> sourceClass, Class<?>... generics) {

        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(sourceClass, generics);

        if (resolvableType != null) {
            String[] beanNames = AppContextManager.getAppContext().getBeanNamesForType(resolvableType);
            if (beanNames != null) {
                if (beanNames.length > 0 && beanNames[0] != null) {
                    return AppContextManager.getAppContext().getBean(beanNames[0]);
                } else {
                    log.error("出现数组长度为0的情况了！");
                }

            }

        }

        throw new UtilException("不存在" + resolvableType.toString() + "的相关Bean,请及时排查错误！");

    }


    @SuppressWarnings("unchecked")
    public static <T extends BaseEntity> BaseEntityDao<T> getBaseEntityDao(Class<T> clazz) {

        if (clazz == null) {
            throw new UtilException("clazz不能为空！");
        }

        return (BaseEntityDao<T>) getGenericBean(BaseEntityDao.class, clazz);

    }

    @SuppressWarnings("unchecked")
    public static <T extends BaseEntity> BaseEntityService<T> getBaseEntityService(Class<T> clazz) {

        if (clazz == null) {
            throw new UtilException("clazz不能为空！");
        }

        return (BaseEntityService<T>) getGenericBean(BaseEntityService.class, clazz);

    }


    //检出一个指定类型的实例。考虑deleted字段。找不到抛异常。
    public static <T extends BaseEntity> T check(Class<T> clazz, String uuid) {

        if (uuid == null) {
            throw new UtilException("uuid必须指定");
        }

        BaseEntityDao<T> baseDao = getBaseEntityDao(clazz);



        Optional<T> optionalT = baseDao.findById(uuid);
        if (optionalT.isPresent()) {
            T t = optionalT.get();
            if (t.deleted) {
                throw new NotFoundException("您访问的内容不存在或者已经被删除");
            }
            return t;
        } else {
            throw new NotFoundException("您访问的内容不存在或者已经被删除");
        }

    }

    //检出一个指定类型的实例。不考虑deleted字段。找不到抛异常。
    public static <T extends BaseEntity> T checkDeeply(Class<T> clazz, String uuid) {

        if (uuid == null) {
            throw new UtilException("id必须指定");
        }

        BaseEntityDao<T> baseDao = getBaseEntityDao(clazz);


        Optional<T> optionalT = baseDao.findById(uuid);
        if (optionalT.isPresent()) {
            return optionalT.get();
        } else {
            throw new NotFoundException("您访问的内容不存在或者已经被删除");
        }
    }



    //找出一个指定类型的实例。考虑deleted字段。找不到返回null
    public static <T extends BaseEntity> T find(Class<T> clazz, String uuid) {

        if (uuid == null) {
            return null;
        }
        BaseEntityDao<T> baseDao = getBaseEntityDao(clazz);

        Optional<T> optionalT = baseDao.findById(uuid);
        if (optionalT.isPresent()) {
            T t = optionalT.get();
            if (t.deleted) {
                return null;
            }
            return t;
        } else {
            return null;
        }
    }

    //找出一个指定类型的实例，不考虑deleted字段。找不到返回null
    public static <T extends BaseEntity> T findDeeply(Class<T> clazz, String uuid) {

        if (uuid == null) {
            return null;
        }
        BaseEntityDao<T> baseDao = getBaseEntityDao(clazz);

        Optional<T> optionalT = baseDao.findById(uuid);
        return optionalT.orElse(null);
    }


    //根据一个pageSize，根据一个复杂查询条件，再根据针对每个的处理方式，按照分页依次处理。
    public static <E extends BaseEntity> void pageHandle(Class<E> clazz, Specification<E> specification, int pageSize, Consumer<? super E> consumer) {

        BaseEntityDao<E> dao = AppContextManager.getBaseEntityDao(clazz);

        Pageable pageRequest = new PageRequest(0, pageSize);

        Page<E> pageData = dao.findAll(specification, pageRequest);
        int totalPages = pageData.getTotalPages();
        for (int p = 0; p < totalPages; p++) {
            pageRequest = new PageRequest(p, pageSize);
            pageData = dao.findAll(specification, pageRequest);
            pageData.getContent().forEach(consumer);
        }
    }


}
