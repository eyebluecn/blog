package cn.eyeblue.blog.config.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class UtilException extends RuntimeException {
    private int code;
    private String message;

    public UtilException(String message) {
        super(message);

        this.code = ResultCode.SERVER_ERROR;
        this.message = message;
    }

    public UtilException(Throwable throwable) {
        super(throwable);

        this.code = ResultCode.SERVER_ERROR;
        this.message = throwable.getMessage();
    }


    public UtilException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}