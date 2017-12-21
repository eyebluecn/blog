package cn.eyeblue.blog.rest.tank;

import cn.eyeblue.blog.rest.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Transient;


@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@Entity
public class Tank extends BaseEntity {


    private String userUuid;

    private String name;


    private String matterUuid;
    private Long size;
    private Boolean privacy;
    private String url;

    //备注信息
    private String remark;

    private boolean confirmed;


    //用于上传的uploadToken.
    @Transient
    private String uploadTokenUuid;
    //客户端需要将文件上传到何处去。
    @Transient
    private String uploadUrl;

    public Tank(String userUuid, String name, long size, boolean privacy) {
        this.userUuid = userUuid;
        this.name = name;
        this.size = size;
        this.privacy = privacy;
        this.confirmed = false;

    }

}
