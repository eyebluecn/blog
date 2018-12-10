package cn.eyeblue.blog.config.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class LoginException extends UtilException {


    public LoginException() {
        super(ResultCode.LOGIN);
    }

    public LoginException(String messagePattern, Object... arguments) {
        super(ResultCode.LOGIN, messagePattern, arguments);
    }

}