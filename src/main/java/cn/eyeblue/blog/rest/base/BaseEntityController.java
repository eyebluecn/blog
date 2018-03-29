package cn.eyeblue.blog.rest.base;

import cn.eyeblue.blog.config.AppContextManager;
import cn.eyeblue.blog.config.exception.LoginException;
import cn.eyeblue.blog.config.exception.UnauthorizedException;
import cn.eyeblue.blog.rest.core.FeatureType;
import cn.eyeblue.blog.rest.user.User;
import cn.eyeblue.blog.rest.user.UserService;
import cn.eyeblue.blog.util.JsonUtil;
import cn.eyeblue.blog.util.NetworkUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;


public abstract class BaseEntityController<E extends BaseEntity, F extends BaseEntityForm<E>> extends BaseController {

    private Class<E> clazz;

    /**
     * 构造函数
     * @param clazz 泛型
     */
    public BaseEntityController(Class<E> clazz) {
        this.clazz = clazz;
    }

    /**
     * 获取dao
     * @return 一个dao
     */
    protected BaseEntityDao<E> getDao() {
        return AppContextManager.getBaseEntityDao(clazz);
    }

    /**
     * 获取Service
     * @return Service
     */
    protected BaseEntityService<E> getService() {
        return AppContextManager.getBaseEntityService(clazz);
    }



    /**
     * 返回一个分页，手动指定一个map规则
     * @param specification 查询规则
     * @param pageable 分页信息
     * @param mapper map信息
     * @return 分页结果
     */
    protected WebResult success(Specification<E> specification, Pageable pageable, Function<E, ? extends Map<String, Object>> mapper) {

        Page<E> pageData = getDao().findAll(specification, pageable);

        int page = pageData.getNumber();
        int pageSize = pageData.getSize();
        long totalItems = pageData.getTotalElements();
        List<E> list = pageData.getContent();

        if (mapper != null) {
            List<Map<String, Object>> collect = list.stream().map(mapper).collect(Collectors.toList());
            Pager<Map<String, Object>> pager = new Pager<>(page, pageSize, totalItems, collect);

            return new WebResult(JsonUtil.toMap(pager));

        } else {
            Pager<E> pager = new Pager<E>(page, pageSize, totalItems, list);

            return new WebResult(JsonUtil.toMap(pager));
        }


    }



    /**
     * 添加一个实例
     * @param form 表单
     * @return 当前实体
     */
    @RequestMapping("/create")
    public WebResult create(@Valid F form) {

        E entity = getService().create(form, checkUser());

        return success(entity);

    }



    /**
     * 删除一个实例
     * @param uuid uuid
     * @return 删除结果
     */
    @RequestMapping("/del/{uuid}")
    public WebResult del(@PathVariable String uuid) {

        E entity = this.check(uuid);
        getService().del(entity, checkUser());
        return success();
    }


    /**
     * 编辑一个实例
     * @param form 表单
     * @return 当前实体
     */
    @RequestMapping("/edit")
    public WebResult edit(@Valid F form) {

        E entity = getService().edit(form, checkUser());

        return success(entity);
    }

    /**
     * 获取详情
     * @param uuid uuid
     * @return 当前实体
     */
    @RequestMapping("/detail/{uuid}")
    public WebResult detail(@PathVariable String uuid) {


        E entity = this.check(uuid);

        return success(entity);

    }


    /**
     * 改变排序位置,需要手动指定放置的位置。
     * sort是用时间戳来表示的。比如是System.currentTimeMillis()
     * 子类中如果要覆盖该方法，则不用再指定 @RequestMapping("/sort") 加上 @Override即可
     * @param uuid1 第一个实体的uuid
     * @param sort1 第一个的sort
     * @param uuid2 第二个实体的uuid
     * @param sort2 第二个的sort
     * @return 排序结果
     */
    @RequestMapping("/sort")
    public WebResult sort(@RequestParam String uuid1, @RequestParam Long sort1, @RequestParam String uuid2, @RequestParam Long sort2) {

        E entity1 = this.check(uuid1);
        entity1.setSort(sort1);
        getDao().save(entity1);

        E entity2 = this.check(uuid2);
        entity2.setSort(sort2);
        getDao().save(entity2);

        return success();

    }


    /**
     * 从数据库中检出一个当前泛型的实例。
     * 找不到抛异常。
     * @param uuid uuid
     * @return 当前实体
     */
    public E check(String uuid) {

        return AppContextManager.check(this.clazz, uuid);
    }


    /**
     * 从数据库中找出一个当前泛型的实例。
     * 找不到返回null
     * @param uuid uuid
     * @return 当前实体
     */
    public E find(String uuid) {
        return AppContextManager.find(this.clazz, uuid);
    }



    /**
     * 返回当前登录的用户，如果没有登录返回null.
     * @return 当前登录用户
     */
    protected User findUser() {

        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = attributes.getRequest();
        HttpSession httpSession = request.getSession(true);

        Object userObject = httpSession.getAttribute(User.TAG);
        if (userObject == null) {
            return null;
        }

        return (User) userObject;
    }

    /**
     * 获取当前的登录的这个user. 没有就抛异常
     *
     * @return User.
     */
    protected User checkUser() {

        User user = this.findUser();
        if (user == null) {
            throw new LoginException();
        }
        return user;

    }

    /**
     *
     * @return 获取到当前这一次的request
     */
    protected HttpServletRequest getCurrentHttpServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
        return attributes.getRequest();
    }


    /**
     *
     * @return 获取到当前这次请求的ip
     */
    protected String getCurrentRequestIp() {
        return NetworkUtil.getIpAddress(getCurrentHttpServletRequest());
    }


    /**
     * 判断自己对于当前实体是否有权限 没有权限直接抛出异常。
     * 管理者权限，个人权限
     * @param manageFeature 管理者的权限
     * @param mineFeature 自己的权限
     * @param entityUserUuid 实体关联的用户
     */
    protected void checkMineEntityPermission(FeatureType manageFeature, FeatureType mineFeature, String entityUserUuid) {

        User operator = checkUser();
        //如果有管理权限。
        if (operator.hasPermission(manageFeature)) {
            return;
        }
        //如果只有查看自己的权限。
        else if (operator.hasPermission(mineFeature)) {
            //必须确保查看的对象属于自己。
            if (Objects.equals(entityUserUuid, operator.getUuid())) {
                return;
            }
        }
        //如果什么都没有。
        throw new UnauthorizedException();
    }

}
