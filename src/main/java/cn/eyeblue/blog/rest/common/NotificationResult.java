package cn.eyeblue.blog.rest.common;

import lombok.Data;


/**
 * 第三方服务会返回来通知的情况如何，这个类就是为了统一管理这种返回结果的。
 */
@Data
public class NotificationResult {

    public final static String TAG = "notificationResult";

    private Status status;
    private String message;


    public enum Status {
        //成功
        OK,
        //失败
        ERROR,
        //未知状态
        UNKNOWN
    }


}
