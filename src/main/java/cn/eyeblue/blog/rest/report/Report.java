package cn.eyeblue.blog.rest.report;

import cn.eyeblue.blog.rest.article.Article;
import cn.eyeblue.blog.rest.base.BaseEntity;
import cn.eyeblue.blog.rest.comment.Comment;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
public class Report extends BaseEntity {

    //主体uuid
    private String entityUuid;

    //主题名称
    private String entityName;

    //ip
    private String ip;

    //历史类型
    @Enumerated(EnumType.STRING)
    private Type type = Type.REPORT_COMMENT;


    //举报内容。
    private String content;

    @Transient
    private Article article;

    @Transient
    private Comment comment;

    //类型
    public enum Type {
        //举报文章
        REPORT_ARTICLE,
        //举报评论
        REPORT_COMMENT
    }


}


