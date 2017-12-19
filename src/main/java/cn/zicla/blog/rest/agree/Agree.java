package cn.zicla.blog.rest.agree;

import cn.zicla.blog.rest.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
public class Agree extends BaseEntity {

    //文章uuid
    private String articleUuid;

    //评论uuid
    private String commentUuid;

    //点赞时的ip
    private String ip;

    //点赞类型
    @Enumerated(EnumType.STRING)
    private Type type = Type.COMMENT;

    //性别
    public enum Type {
        ARTICLE,
        COMMENT
    }


}


