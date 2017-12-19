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
public class History extends BaseEntity {

    //主体uuid
    private String entityUuid;

    //ip
    private String ip;

    //历史类型
    @Enumerated(EnumType.STRING)
    private Type type = Type.VISIT_ARTICLE;

    //类型
    public enum Type {
        //给文章点赞
        AGREE_ARTICLE,
        //给评论点赞
        AGREE_COMMENT,
        //访问文章
        VISIT_ARTICLE
    }


}


