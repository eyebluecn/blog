package cn.eyeblue.blog.config.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = false)
@Data
public class LoginExpiredException extends RuntimeException {

    private int code;
    private String message;

    public LoginExpiredException() {
        super("登录信息已过期，请重新登录");
        this.code = ResultCode.LOGIN_EXPIRED;
        this.message = "登录信息已过期，请重新登录";
    }

    public LoginExpiredException(String message) {
        super(message);
        this.code = ResultCode.LOGIN_EXPIRED;
        this.message = message;
    }

    public LoginExpiredException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

}