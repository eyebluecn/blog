package cn.zicla.blog.rest.tank;

import cn.zicla.blog.rest.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;


@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@Entity
public class Tank extends BaseEntity {


    private String userUuid;

    private String name;

    //用于上传的uploadToken.
    private String uploadToken;

    private String matterUuid;
    private Long size;
    private Boolean privacy;
    private String url;

    //备注信息
    private String remark;

    private boolean confirmed;


    public Tank(String userUuid, String name, String uploadToken, long size,  boolean privacy) {
        this.userUuid = userUuid;
        this.name = name;
        this.uploadToken = uploadToken;
        this.size = size;
        this.privacy = privacy;
        this.confirmed = false;

    }

}
