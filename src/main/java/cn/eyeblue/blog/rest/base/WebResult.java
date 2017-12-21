package cn.eyeblue.blog.rest.base;

import cn.eyeblue.blog.config.exception.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@Data
public class WebResult {


    //一些混淆的字段
    public final static String COOKIE_AUTHENTICATION = "ca_";



    private int code = ResultCode.OK;
    private Map<String, Object> data;
    private String msg = "成功";

    public WebResult(int code, String msg, Map<String, Object> data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }


    public WebResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public WebResult(Map<String, Object> data) {
        this.data = data;
    }

}
