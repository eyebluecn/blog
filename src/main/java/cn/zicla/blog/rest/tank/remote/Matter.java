package cn.zicla.blog.rest.tank.remote;

import cn.zicla.blog.util.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@EqualsAndHashCode(callSuper = true)
@Data
public class Matter extends TankBaseEntity {

    private String puuid;
    private String userUuid;
    private boolean dir;
    private String name;
    private String md5;
    private long size;
    private boolean privacy;
    private String path;
    private String url;


}
