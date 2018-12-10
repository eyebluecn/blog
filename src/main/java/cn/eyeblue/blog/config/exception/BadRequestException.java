package cn.eyeblue.blog.config.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = false)
@Data
public class BadRequestException extends UtilException {


    public BadRequestException() {
        super(ResultCode.BAD_REQUEST);
    }

    public BadRequestException(String messagePattern, Object... arguments) {
        super(ResultCode.BAD_REQUEST, messagePattern, arguments);
    }

    public BadRequestException(ResultCode resultCode) {
        super(resultCode);
    }


}