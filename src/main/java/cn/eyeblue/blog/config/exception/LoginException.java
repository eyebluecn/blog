package cn.eyeblue.blog.config.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = false)
@Data
public class LoginException extends RuntimeException {

    private int code;
    private String message;

    public LoginException() {
        super("没有登录，禁止访问");
        this.code = ResultCode.LOGIN;
        this.message = "没有登录，禁止访问";
    }

    public LoginException(String message) {
        super(message);
        this.code = ResultCode.LOGIN;
        this.message = message;
    }

    public LoginException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

}