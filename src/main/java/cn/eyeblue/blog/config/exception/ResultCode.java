package cn.eyeblue.blog.config.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum ResultCode {

    OK(HttpStatus.OK, "成功"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "请求不合法"),
    CAPTCHA_ERROR(HttpStatus.BAD_REQUEST, "验证码错误"),
    NEED_CAPTCHA(HttpStatus.BAD_REQUEST, "验证码必填"),
    USERNAME_PASSWORD_ERROR(HttpStatus.BAD_REQUEST, "用户名或密码错误"),
    REQUIRE_GENDER(HttpStatus.METHOD_NOT_ALLOWED, "必须首先填写性别"),
    REQUIRE_PHONE(HttpStatus.METHOD_NOT_ALLOWED, "必须首先认证手机"),
    PARAMS_ERROR(HttpStatus.BAD_REQUEST, "请求参数错误"),
    LOGIN(HttpStatus.UNAUTHORIZED, "未登录，禁止访问"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "没有权限，禁止访问"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "内容不存在"),
    UNKNOWN(HttpStatus.INTERNAL_SERVER_ERROR, "服务器未知错误");

    @Getter
    private HttpStatus httpStatus;

    @Getter
    private String message;

    ResultCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

}
