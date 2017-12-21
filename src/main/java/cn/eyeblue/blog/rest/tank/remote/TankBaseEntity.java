package cn.eyeblue.blog.rest.tank.remote;

import cn.eyeblue.blog.util.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class TankBaseEntity {
    private String uuid;
    private long sort;
    @DateTimeFormat(pattern = DateUtil.DEFAULT_FORMAT)
    @JsonFormat(pattern = DateUtil.DEFAULT_FORMAT)
    private Date modifyTime;
    @DateTimeFormat(pattern = DateUtil.DEFAULT_FORMAT)
    @JsonFormat(pattern = DateUtil.DEFAULT_FORMAT)
    private Date createTime;
}
