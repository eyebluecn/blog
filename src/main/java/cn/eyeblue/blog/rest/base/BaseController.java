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

    protected WebResult error() {
        return new WebResult(ResultCode.UTIL_EXCEPTION, "出错了");
    }

    protected WebResult error(String message) {
        return new WebResult(ResultCode.UTIL_EXCEPTION, message);
    }

    protected WebResult error(int code, String message) {
        return new WebResult(code, message);
    }

    protected WebResult success() {
        return new WebResult(ResultCode.OK, "成功");
    }

    protected WebResult success(String message) {
        return new WebResult(ResultCode.OK, message);
    }

    protected WebResult success(int code, String message) {
        return new WebResult(code, message);
    }


    protected WebResult success(BaseEntity baseEntity) {
        WebResult webResult = new WebResult(ResultCode.OK, "成功");
        if (baseEntity != null) {
            webResult.setData(baseEntity.detailMap());
        }
        return webResult;

    }

    protected WebResult successObject(Object object) {
        WebResult webResult = new WebResult(ResultCode.OK, "成功");
        Map<String, Object> map = new HashMap<>();

        map.put("result", object);
        webResult.setData(map);
        return webResult;

    }

    protected WebResult success(Map<String, Object> map) {
        WebResult webResult = new WebResult(ResultCode.OK, "成功");
        webResult.setData(map);
        return webResult;
    }

    protected WebResult success(Pager<? extends BaseEntity> pager) {
        WebResult webResult = new WebResult(ResultCode.OK, "成功");

        List<Map<String, Object>> mapList = pager.getData().stream().map(BaseEntity::map).collect(Collectors.toList());

        Map<String, Object> map = JsonUtil.toMap(pager);
        map.put("data", mapList);

        webResult.setData(map);
        return webResult;

    }

    protected <E> WebResult success(Pager<E> pager, Function<E, ? extends Map<String, Object>> mapper) {
        WebResult webResult = new WebResult(ResultCode.OK, "成功");

        List<Map<String, Object>> mapList = pager.getData().stream().map(mapper).collect(Collectors.toList());

        Map<String, Object> map = JsonUtil.toMap(pager);
        map.put("data", mapList);

        webResult.setData(map);
        return webResult;
    }

    //对page和pageSize进行验证
    public PageRequest getPageRequest(int page, int pageSize, Sort sort) {

        if (page < 0 || pageSize < 1 || pageSize > 50) {
            throw new UtilException("Exceed the limitation.");
        }

        return new PageRequest(page, pageSize, sort);
    }


}
