package cn.eyeblue.blog.config.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class NotFoundException extends UtilException {

    public NotFoundException() {
        super(ResultCode.NOT_FOUND);
    }

    public NotFoundException(String messagePattern, Object... arguments) {
        super(ResultCode.NOT_FOUND, messagePattern, arguments);

    }

}