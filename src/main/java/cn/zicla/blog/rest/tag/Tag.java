package cn.zicla.blog.rest.tag;

import cn.zicla.blog.rest.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

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

}

