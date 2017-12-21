package cn.eyeblue.blog.rest.tank.remote;

import lombok.Data;


@Data
public class TankMessage<T extends TankBaseEntity> {

    private int code;
    private String msg;
    private T data;
}
