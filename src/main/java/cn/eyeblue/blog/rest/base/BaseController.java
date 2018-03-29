package cn.eyeblue.blog.rest.base;

import cn.eyeblue.blog.config.exception.ResultCode;
import cn.eyeblue.blog.config.exception.UtilException;
import cn.eyeblue.blog.util.JsonUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class BaseController {

    /**
     *
     * @return 错误结果
     */
    protected WebResult error() {
        return new WebResult(ResultCode.UTIL_EXCEPTION, "出错了");
    }

    /**
     *
     * @param message 描述
     * @return 结果
     */
    protected WebResult error(String message) {
        return new WebResult(ResultCode.UTIL_EXCEPTION, message);
    }

    /**
     *
     * @param code 结果码
     * @param message 描述
     * @return 结果
     */
    protected WebResult error(int code, String message) {
        return new WebResult(code, message);
    }

    /**
     *
     * @return 成功消息
     */
    protected WebResult success() {
        return new WebResult(ResultCode.OK, "成功");
    }

    /**
     *
     * @param message 信息
     * @return 信息结果
     */
    protected WebResult success(String message) {
        return new WebResult(ResultCode.OK, message);
    }

    /**
     *
     * @param code 结果码
     * @param message 消息
     * @return 结果消息
     */
    protected WebResult success(int code, String message) {
        return new WebResult(code, message);
    }

    /**
     *
     * @param baseEntity 基础对象
     * @return 对象信息
     */
    protected WebResult success(BaseEntity baseEntity) {
        WebResult webResult = new WebResult(ResultCode.OK, "成功");
        if (baseEntity != null) {
            webResult.setData(baseEntity.detailMap());
        }
        return webResult;

    }

    /**
     *
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
     *
     * @param map 键值对
     * @return 键值对结果
     */
    protected WebResult success(Map<String, Object> map) {
        WebResult webResult = new WebResult(ResultCode.OK, "成功");
        webResult.setData(map);
        return webResult;
    }

    /**
     *
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
     *
     * @param pager 分页信息
     * @param mapper 处理实体的方式
     * @param <E> 实体
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
     * 对page和pageSize进行验证
     * @param page 当前页码 0基
     * @param pageSize 每页大小
     * @param sort 排序方式
     * @return 分页请求
     */
    public PageRequest getPageRequest(int page, int pageSize, Sort sort) {

        if (page < 0 || pageSize < 1 || pageSize > 50) {
            throw new UtilException("Exceed the limitation.");
        }

        return new PageRequest(page, pageSize, sort);
    }


}
