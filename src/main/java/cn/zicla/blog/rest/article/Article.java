package cn.zicla.blog.rest.article;

import cn.zicla.blog.rest.base.BaseEntity;
import cn.zicla.blog.rest.tag.Tag;
import cn.zicla.blog.rest.tank.Tank;
import cn.zicla.blog.rest.user.User;
import cn.zicla.blog.util.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

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

    //点赞
    private Integer agree = 0;

    //字数
    private Integer words = 0;

    //点击数量
    private Integer hit = 1;

    //发布日期
    @JsonFormat(pattern = DateUtil.DEFAULT_FORMAT)
    private Date releaseTime;

    @Transient
    private Tank posterTank;

    @Transient
    private User user;

    //当前用户是否已经对这篇文章点赞了
    @Transient
    private boolean agreed;

    //评论数量
    @Transient
    private Integer commentNum = 0;

    //标签数组对象
    @Transient
    private List<Tag> tagArray;

}


