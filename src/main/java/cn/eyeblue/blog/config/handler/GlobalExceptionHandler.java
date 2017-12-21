package cn.eyeblue.blog.config.handler;

import cn.eyeblue.blog.config.exception.*;
import cn.eyeblue.blog.rest.base.WebResult;
import lombok.extern.slf4j.Slf4j;
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
import java.util.List;

/**
 * 全局异常处理
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {


    //没有找到的异常
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NotFoundException.class})
    @ResponseBody
    public WebResult handle(HttpServletRequest req, NotFoundException exception) {

        WebResult webResult = new WebResult(exception.getCode(), exception.getMessage());

        log.error("------NotFoundException------");
        log.error(exception.getMessage());
        exception.printStackTrace();


        return webResult;
    }

    //没有登录，这是一种特殊的异常
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({LoginException.class})
    @ResponseBody
    public WebResult handle(HttpServletRequest req, LoginException exception) {


        WebResult webResult = new WebResult(exception.getCode(), exception.getMessage());

        log.error("------LoginException------");
        log.error(exception.getMessage());
        exception.printStackTrace();


        return webResult;


    }


    //没有授权
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({UnauthorizedException.class})
    @ResponseBody
    public WebResult handle(HttpServletRequest req, UnauthorizedException exception) {


        WebResult webResult = new WebResult(exception.getCode(), exception.getMessage());

        log.error("------UnauthorizedException------");
        log.error(exception.getMessage());
        exception.printStackTrace();


        return webResult;

    }

    //没有授权
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BadRequestException.class})
    @ResponseBody
    public WebResult handle(HttpServletRequest req, BadRequestException exception) {


        WebResult webResult = new WebResult(exception.getCode(), exception.getMessage());


        log.error("------BadRequestException------");
        log.error(exception.getMessage());
        exception.printStackTrace();


        return webResult;

    }

    //表单提交时报错。
    @ResponseStatus(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE)
    @ExceptionHandler({BindException.class})
    @ResponseBody
    public WebResult handle(HttpServletRequest req, BindException exception) {

        String message = "";
        List<ObjectError> errorList = exception.getAllErrors();
        for (ObjectError objectError : errorList) {

            if (objectError instanceof FieldError) {
                FieldError fieldError = (FieldError) objectError;

                if (fieldError.getRejectedValue() == null) {
                    message += "您填写的“" + fieldError.getField() + "”不符合要求：" + objectError.getDefaultMessage() + ";";
                } else {
                    message += "您填写的“" + fieldError.getRejectedValue() + "”不符合要求：" + objectError.getDefaultMessage() + ";";
                }

            } else {
                message += objectError.getDefaultMessage();
            }


        }

        if (message.equals("")) {
            message = exception.getMessage();
        }

        WebResult webResult = new WebResult(ResultCode.FORM_INVALID, message);

        log.error("------BindException------");
        log.error(exception.getMessage());
        exception.printStackTrace();

        return webResult;
    }


    //请求参数不符合要求。
    @ResponseStatus(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE)
    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseBody
    public WebResult handle(HttpServletRequest req, MissingServletRequestParameterException exception) {


        String message = "要求填写的参数\"" + exception.getParameterName() + "\"(" + exception.getParameterType() + ")不符合要求";

        WebResult webResult = new WebResult(ResultCode.FORM_INVALID, message);

        log.error("------MissingServletRequestParameterException------");
        log.error(exception.getMessage());
        exception.printStackTrace();

        return webResult;
    }


    //自定义的异常。
    @ExceptionHandler({LoginExpiredException.class})
    @ResponseBody
    public WebResult errorCode(HttpServletRequest req, LoginExpiredException exception) {

        WebResult webResult = new WebResult(exception.getCode(), exception.getMessage());
        log.debug("------LoginExpiredException------");
        exception.printStackTrace();
        return webResult;

    }


    //自定义的异常。
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({UtilException.class})
    @ResponseBody
    public WebResult errorCode(HttpServletRequest req, UtilException exception) {


        WebResult webResult = new WebResult(exception.getCode(), exception.getMessage());

        log.error("------UtilException------");
        log.error(exception.getMessage());
        exception.printStackTrace();


        return webResult;

    }


    //方法支持的异常
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})
    @ResponseBody
    public WebResult handleAllException(HttpServletRequest req, Exception exception) {

        WebResult webResult = new WebResult(ResultCode.SERVER_ERROR, "服务器未知错误，请稍后再试");

        log.error("------Exception------");
        log.error(exception.getMessage());
        exception.printStackTrace();


        return webResult;

    }
}
