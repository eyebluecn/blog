package cn.zicla.blog.rest.tank;

import cn.zicla.blog.rest.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;


@EqualsAndHashCode(callSuper = false)
@Data
@Entity
public class Tank extends BaseEntity {


    private String userUuid;

    private String name;
    private String matterUuid;
    private Long size;
    private String type;
    private String filter;
    private Boolean privacy;
    private String url;

    //备注信息
    private String remark;


}
