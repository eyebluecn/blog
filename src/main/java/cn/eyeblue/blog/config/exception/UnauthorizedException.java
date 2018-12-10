package cn.eyeblue.blog.config.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = false)
@Data
public class UnauthorizedException extends UtilException {

    public UnauthorizedException() {
        super(ResultCode.UNAUTHORIZED);
    }

    public UnauthorizedException(String messagePattern, Object... arguments) {
        super(ResultCode.UNAUTHORIZED, messagePattern, arguments);
    }

}