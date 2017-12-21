package cn.eyeblue.blog.config.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = false)
@Data
public class BadRequestException extends RuntimeException {

    private int code;
    private String message;

    public BadRequestException(String message) {
        super(message);
        this.code = ResultCode.BAD_REQUEST;
        this.message = message;
    }

    public BadRequestException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

}