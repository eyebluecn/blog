package cn.eyeblue.blog.rest.tag;

import cn.eyeblue.blog.rest.base.BaseEntity;
import cn.eyeblue.blog.rest.tank.Tank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Transient;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
public class Tag extends BaseEntity {

    //创建者
    private String userUuid;

    //标题
    private String name;


    //图片
    private String logoTankUuid;

    //Url
    private String logoUrl;

    @Transient
    private Tank logoTank;

}


