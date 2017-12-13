package cn.zicla.blog.rest.article;

import cn.zicla.blog.rest.base.BaseEntity;
import cn.zicla.blog.util.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
public class Article extends BaseEntity {

    //发布者
    private String userUuid;

    //标题
    private String title;

    //标签
    private String tags;

    //封面图片
    private String posterTankUuid;

    //封面图片Url
    private String posterUrl;

    //作者
    private String author;

    //摘要
    private String digest;

    //是否是markdown格式
    private Boolean isMarkdown = true;

    //markdown内容
    private String markdown;

    //html内容
    private String html;

    //是否是私有文章
    private Boolean privacy = false;

    //是否置顶
    private Boolean top = false;

    //点击数量
    private Integer hit = 1;

    //发布日期
    @JsonFormat(pattern = DateUtil.DEFAULT_FORMAT)
    private Date releaseTime;

}


