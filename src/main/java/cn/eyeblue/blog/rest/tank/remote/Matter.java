package cn.eyeblue.blog.rest.tank.remote;

import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class Matter extends TankBaseEntity {

    private String puuid;
    private String userUuid;
    private boolean dir;
    private Boolean alien;
    private String name;
    private String md5;
    private long size;
    private boolean privacy;
    private String path;
    private String url;


}
