package cn.eyeblue.blog.config.handler;

import cn.eyeblue.blog.config.AppContextManager;
import cn.eyeblue.blog.config.exception.ResultCode;
import cn.eyeblue.blog.config.exception.UtilException;
import cn.eyeblue.blog.rest.base.WebResult;
import cn.eyeblue.blog.rest.common.DingdingService;
import cn.eyeblue.blog.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 全局异常处理
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    //表单提交时报错。
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BindException.class})
    @ResponseBody
    public WebResult handleBindException(HttpServletRequest req, BindException exception) {

        String defaultMessage = "以下参数不符合要求。";
        String message = defaultMessage;
        List<ObjectError> errorList = exception.getAllErrors();
        for (ObjectError objectError : errorList) {

            if (objectError instanceof FieldError) {
                FieldError fieldError = (FieldError) objectError;

                if (fieldError.getRejectedValue() == null) {
                    message += StringUtil.format("{}:{};", fieldError.getField(), objectError.getDefaultMessage());
                } else {
                    message += StringUtil.format("{}:{};", fieldError.getRejectedValue(), objectError.getDefaultMessage());
                }

            } else {
                message += objectError.getDefaultMessage();
            }
        }

        if (message.equals(defaultMessage)) {
            message = ExceptionUtils.getRootCauseMessage(exception);
        }

        WebResult webResult = new WebResult(ResultCode.PARAMS_ERROR, message);

        log.error("------BindException------");
        log.error(ExceptionUtils.getRootCauseMessage(exception), exception);

        return webResult;
    }


    //请求参数不符合要求。
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseBody
    public WebResult handleMissingServletRequestParameterException(HttpServletRequest req, MissingServletRequestParameterException exception) {


        String message = StringUtil.format("要求填写的参数{}({})不符合要求", exception.getParameterName(), exception.getParameterType());
        WebResult webResult = new WebResult(ResultCode.PARAMS_ERROR, message);

        log.error("------MissingServletRequestParameterException------");
        log.error(ExceptionUtils.getRootCauseMessage(exception), exception);


        return webResult;
    }

    //自定义的异常。
    @ExceptionHandler({UtilException.class})
    @ResponseBody
    public WebResult handleUtilException(HttpServletRequest request,
                                         HttpServletResponse response,
                                         UtilException exception) {

        //按照各自的异常进行返回。
        response.setStatus(exception.getCode().getHttpStatus().value());

        WebResult webResult = new WebResult(exception.getCode(), exception.getMessage());

        log.error("------" + exception.getClass().getName() + "------");
        log.error(ExceptionUtils.getRootCauseMessage(exception), exception);


        return webResult;
    }

    //其他不能处理的异常。
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})
    @ResponseBody
    public WebResult handleException(HttpServletRequest req, Exception exception) {

        WebResult webResult = new WebResult(ResultCode.UNKNOWN, StringUtil.format("未知错误，{}", ExceptionUtils.getRootCauseMessage(exception)));

        log.error("------Exception------");
        log.error(ExceptionUtils.getRootCauseMessage(exception), exception);


        //发送钉钉消息。
        DingdingService dingdingService = AppContextManager.getBean(DingdingService.class);
        dingdingService.sendMvcExceptionInfo(exception);

        return webResult;
    }
}
