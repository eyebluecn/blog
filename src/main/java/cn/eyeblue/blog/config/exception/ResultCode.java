package cn.eyeblue.blog.config.exception;

/**
 * Created by MaWenge on 2017/6/25.
 */
public class ResultCode {

    //正常
    public static final int OK = 200;


    //未登录
    public static final int LOGIN = -400;
    //没有权限
    public static final int UNAUTHORIZED = -401;
    //请求错误
    public static final int BAD_REQUEST = -402;


    //没有找到
    public static final int NOT_FOUND = -404;


    //登录过期
    public static final int LOGIN_EXPIRED = -405;

    //提交的表单验证不通过
    public static final int FORM_INVALID = -410;

    //请求太频繁
    public static final int FREQUENCY = -420;


    //服务器出错。
    public static final int SERVER_ERROR = -500;


    //远程服务不可用
    public static final int NOT_AVAILABLE = -501;

    //并发异常
    public static final int CONCURRENCY = -511;

    //远程微服务没有找到
    public static final int SERVICE_NOT_FOUND = -600;

    //远程微服务连接超时
    public static final int SERVICE_TIME_OUT = -610;


    //通用的异常
    public static final int UTIL_EXCEPTION = -700;
}
