package cn.eyeblue.blog.config;

import cn.eyeblue.blog.config.exception.LoginException;
import cn.eyeblue.blog.config.exception.ServerException;
import cn.eyeblue.blog.rest.base.BaseEntity;
import cn.eyeblue.blog.rest.base.BaseEntityDao;
import cn.eyeblue.blog.rest.base.BaseEntityService;
import cn.eyeblue.blog.rest.base.Pager;
import cn.eyeblue.blog.rest.user.User;
import cn.eyeblue.blog.rest.user.UserService;
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
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    public static Object getBean(String beanName) throws BeansException {
        return getAppContext().getBean(beanName);
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

        throw new ServerException("不存在" + resolvableType.toString() + "的相关Bean,请及时排查错误！");

    }


    @SuppressWarnings("unchecked")
    public static <T extends BaseEntity> BaseEntityDao<T> getBaseEntityDao(Class<T> clazz) {

        if (clazz == null) {
            throw new ServerException("clazz不能为空！");
        }

        return (BaseEntityDao<T>) getGenericBean(BaseEntityDao.class, clazz);

    }

    @SuppressWarnings("unchecked")
    public static <T extends BaseEntity> BaseEntityService<T> getBaseEntityService(Class<T> clazz) {

        if (clazz == null) {
            throw new ServerException("clazz不能为空！");
        }

        return (BaseEntityService<T>) getGenericBean(BaseEntityService.class, clazz);

    }


    //检出一个指定类型的实例。找不到抛异常。
    public static <T extends BaseEntity> T check(Class<T> clazz, String uuid) {

        if (uuid == null) {
            throw new ServerException("uuid必须指定");
        }

        BaseEntityDao<T> baseDao = getBaseEntityDao(clazz);

        Optional<T> optionalT = baseDao.findById(uuid);
        if (optionalT.isPresent()) {
            return optionalT.get();
        } else {
            throw new ServerException("您访问的内容不存在或者已经被删除." + clazz.getSimpleName() + " uuid=" + uuid);
        }

    }


    //找出一个指定类型的实例。找不到返回null
    public static <T extends BaseEntity> T find(Class<T> clazz, String uuid) {

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

        Pageable pageRequest = PageRequest.of(0, pageSize);

        Page<E> pageData = dao.findAll(specification, pageRequest);
        int totalPages = pageData.getTotalPages();
        for (int p = 0; p < totalPages; p++) {
            pageRequest = PageRequest.of(p, pageSize);
            pageData = dao.findAll(specification, pageRequest);
            pageData.getContent().forEach(consumer);
        }
    }


    //将某个list中的每个实体E 的属性 F 装填上。采用这种方式可以保证只查询一次数据库。如果有候选的list，那么我们就直接用现成的。满足filter的才会去设置。典型例子是Bill.
    public static <E, F extends BaseEntity> void fillFields(
            List<E> list,
            Class<F> clazz,
            Function<E, String> function,
            BiConsumer<E, F> biConsumer,
            Predicate<E> predicate
    ) {
        fillFields(list, null, clazz, function, biConsumer, predicate);
    }

    public static <E, F extends BaseEntity> void fillFields(
            List<E> list,
            Class<F> clazz,
            Function<E, String> function,
            BiConsumer<E, F> biConsumer
    ) {
        fillFields(list, null, clazz, function, biConsumer, null);
    }


    public static <E, F extends BaseEntity> void fillFields(
            List<E> list,
            Pager<F> candidatePager,
            Class<F> clazz,
            Function<E, String> function,
            BiConsumer<E, F> biConsumer
    ) {
        fillFields(list, candidatePager, clazz, function, biConsumer, null);
    }


    public static <E, F extends BaseEntity> void fillFields(
            List<E> list,
            Pager<F> candidatePager,
            Class<F> clazz,
            Function<E, String> function,
            BiConsumer<E, F> biConsumer,
            Predicate<E> predicate
    ) {

        if (list == null || clazz == null || function == null || biConsumer == null) {
            log.warn("fillFields方法入参有误 " +
                            "list == null {} " +
                            "clazz == null {} " +
                            "function == null {} " +
                            "biConsumer == null {}",
                    list == null,
                    clazz == null,
                    function == null,
                    biConsumer == null
            );
            return;
        }
        if (list.size() == 0) {
            //列表为空直接放弃
            return;
        }

        //如果条件筛选没有设置，那么一律当作true处理
        if (predicate == null) {
            predicate = (e) -> true;
        }

        //如果有候选的pager 那么直接从候选pager中装填，如果没有，那么采用去查的方式。
        Iterable<F> candidates = null;
        if (candidatePager != null && candidatePager.getData() != null) {
            candidates = candidatePager.getData();
        } else {
            BaseEntityDao<F> dao = AppContextManager.getBaseEntityDao(clazz);
            List<String> uuids = list.stream().filter(predicate).map(function).collect(Collectors.toList());
            candidates = dao.findAllById(uuids);
        }

        //将属性实体装在一个map中，便于检索
        Map<String, F> map = new HashMap<>();
        candidates.forEach(entity -> {
            map.put(entity.getUuid(), entity);
        });

        list.forEach(e -> {
            //从map中分别获取到每个实体。可能f为null，那么这个属性就当没有了。
            String key = function.apply(e);
            if (key != null) {
                F f = map.get(key);
                biConsumer.accept(e, f);
            }
        });
    }


    //请求当前链路中的User.

    /**
     * 返回当前登录的用户，如果没有登录返回null.
     *
     * @return 当前登录用户
     */
    public static User findUser() {

        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = attributes.getRequest();

        UserService userService = AppContextManager.getBean(UserService.class);

        return userService.getCachedUser(request);
    }

    /**
     * 获取当前的登录的这个user. 没有就抛异常
     *
     * @return User.
     */
    public static User checkUser() {

        User user = findUser();
        if (user == null) {
            throw new LoginException();
        }
        return user;

    }

    /**
     * 打印出所有装在了的bean
     */
    public static void printBeans() {
        System.out.print(Arrays.asList(getAppContext().getBeanDefinitionNames()));
    }


}
