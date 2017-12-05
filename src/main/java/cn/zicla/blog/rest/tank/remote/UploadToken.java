package cn.zicla.blog.rest.tank.remote;

import lombok.Data;

import java.util.Date;

@Data
public class UploadToken {
    private String uuid;
    private long sort;
    private Date modifyTime;
    private Date createTime;

    private String userUuid;
    private String folderUuid;
    private String matterUuid;
    private Date expireTime;
    private String filename;
    private boolean privacy;
    private String ip;

}
