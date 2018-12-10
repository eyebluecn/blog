package cn.eyeblue.blog.rest.tank.remote;

import lombok.Data;


@Data
public class TankMessage<T extends TankBaseEntity> {

    private WebResultCode code;
    private String msg;
    private T data;
}
