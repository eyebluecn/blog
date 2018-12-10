package cn.eyeblue.blog.rest.base;

import cn.eyeblue.blog.config.AppContextManager;
import cn.eyeblue.blog.config.exception.ResultCode;
import cn.eyeblue.blog.rest.user.User;
import cn.eyeblue.blog.util.JsonUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class BaseController extends BaseBigBean {


    /**
     * @return 成功消息
     */
    protected WebResult success() {
        return new WebResult(ResultCode.OK, "成功");
    }

    /**
     * @param message 信息
     * @return 信息结果
     */
    protected WebResult success(String message) {
        return new WebResult(ResultCode.OK, message);
    }

    /**
     * @param code    结果码
     * @param message 消息
     * @return 结果消息
     */
    protected WebResult success(ResultCode code, String message) {
        return new WebResult(code, message);
    }

    /**
     * @param base 基础对象
     * @return 对象信息
     */
    protected WebResult success(Base base) {
        WebResult webResult = new WebResult(ResultCode.OK, "成功");
        if (base != null) {
            webResult.setData(base.detailMap());
        }
        return webResult;

    }

    /**
     * @param object 对象
     * @return 对象信息
     */
    protected WebResult successObject(Object object) {
        WebResult webResult = new WebResult(ResultCode.OK, "成功");
        Map<String, Object> map = new HashMap<>();

        map.put("result", object);
        webResult.setData(map);
        return webResult;

    }

    /**
     * @param map 键值对
     * @return 键值对结果
     */
    protected WebResult success(Map<String, Object> map) {
        WebResult webResult = new WebResult(ResultCode.OK, "成功");
        webResult.setData(map);
        return webResult;
    }

    /**
     * @param pager 分页信息
     * @return 分页结果
     */
    protected WebResult success(Pager<? extends BaseEntity> pager) {
        WebResult webResult = new WebResult(ResultCode.OK, "成功");

        List<Map<String, Object>> mapList = pager.getData().stream().map(BaseEntity::map).collect(Collectors.toList());

        Map<String, Object> map = JsonUtil.toMap(pager);
        map.put("data", mapList);

        webResult.setData(map);
        return webResult;

    }

    /**
     * @param pager  分页信息
     * @param mapper 处理实体的方式
     * @param <E>    实体
     * @return 一个结果
     */
    protected <E> WebResult success(Pager<E> pager, Function<E, ? extends Map<String, Object>> mapper) {
        WebResult webResult = new WebResult(ResultCode.OK, "成功");

        List<Map<String, Object>> mapList = pager.getData().stream().map(mapper).collect(Collectors.toList());

        Map<String, Object> map = JsonUtil.toMap(pager);
        map.put("data", mapList);

        webResult.setData(map);
        return webResult;
    }


    /**
     * 返回当前登录的用户，如果没有登录返回null.
     *
     * @return 当前登录用户
     */
    protected User findUser() {
        return AppContextManager.findUser();
    }

    /**
     * 获取当前的登录的这个user. 没有就抛异常
     *
     * @return User.
     */
    protected User checkUser() {
        return AppContextManager.checkUser();
    }



}
