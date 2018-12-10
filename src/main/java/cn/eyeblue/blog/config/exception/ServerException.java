package cn.eyeblue.blog.config.exception;

import cn.eyeblue.blog.util.StringUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 出现这个异常代表 服务器不正常，需要改代码。
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class ServerException extends RuntimeException {

    protected ResultCode code;
    protected String message;

    public ServerException(String messagePattern, Object... arguments) {

        super(StringUtil.format(messagePattern, arguments));

        this.code = ResultCode.UNKNOWN;
        this.message = StringUtil.format(messagePattern, arguments);
    }

    public ServerException(ResultCode resultCode) {
        super(resultCode.getMessage());

        this.code = resultCode;
        this.message = resultCode.getMessage();
    }

    public ServerException(Throwable throwable) {
        super(throwable);

        this.code = ResultCode.UNKNOWN;
        this.message = throwable.getMessage();
    }

    public ServerException(ResultCode resultCode, String messagePattern, Object... arguments) {
        super(StringUtil.format(messagePattern, arguments));

        this.code = resultCode;
        this.message = StringUtil.format(messagePattern, arguments);
    }

}