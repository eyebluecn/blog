package cn.zicla.blog.rest.tank.remote;

import lombok.Data;


@Data
public class TankMessage<T> {

    private String code;
    private String msg;
    private T data;
}
