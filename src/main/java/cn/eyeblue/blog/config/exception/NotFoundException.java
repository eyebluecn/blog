package cn.eyeblue.blog.config.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class NotFoundException extends RuntimeException {
    private int code;
    private String message;

    public NotFoundException(String message) {
        super(message);
        this.code = ResultCode.NOT_FOUND;
        this.message = message;
    }

    public NotFoundException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }


}