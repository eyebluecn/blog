package cn.eyeblue.blog.rest.tank.remote;

import cn.eyeblue.blog.util.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


//tank服务器上对于uploadToken的定义
@EqualsAndHashCode(callSuper = true)
@Data
public class UploadToken extends TankBaseEntity {

    private String userUuid;
    private String folderUuid;
    private String matterUuid;
    @DateTimeFormat(pattern = DateUtil.DEFAULT_FORMAT)
    @JsonFormat(pattern = DateUtil.DEFAULT_FORMAT)
    private Date expireTime;
    private String filename;
    private boolean privacy;
    private long size;
    private String ip;

}
