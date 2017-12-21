package cn.eyeblue.blog.config.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = false)
@Data
public class UnauthorizedException extends RuntimeException {

    private int code;
    private String message;

    public UnauthorizedException() {
        super("没有权限");
        this.message = "没有权限";
        this.code = ResultCode.UNAUTHORIZED;
    }

    public UnauthorizedException(String message) {
        super(message);
        this.code = ResultCode.UNAUTHORIZED;
        this.message = message;
    }

    public UnauthorizedException(int exceptionCode, String message) {
        super(message);
        this.code = exceptionCode;
        this.message = message;
    }

}