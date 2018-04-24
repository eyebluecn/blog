package cn.eyeblue.blog.rest.article;

import cn.eyeblue.blog.config.exception.BadRequestException;
import cn.eyeblue.blog.rest.base.BaseEntity;
import cn.eyeblue.blog.rest.tag.Tag;
import cn.eyeblue.blog.rest.tank.Tank;
import cn.eyeblue.blog.rest.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.List;
import java.util.regex.Pattern;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
public class Article extends BaseEntity {

    //发布者
    private String userUuid;

    //标题
    private String title;

    //文章访问路径
    private String path;

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

    //评论数量
    private Integer commentNum = 0;

    //是否接受评论的邮件通知
    private Boolean needNotify = true;

    @Transient
    private Tank posterTank;

    @Transient
    private User user;

    //当前用户是否已经对这篇文章点赞了
    @Transient
    private boolean agreed;

    //标签数组对象
    @Transient
    private List<Tag> tagArray;


    @Data
    public static class Analysis {
        private Integer num;
        private Integer agree;
        private Integer hit;
        private Integer words;
        private Integer commentNum;
    }


    public void validatePath() {

        String tempKey = this.path;

        String pattern = "^[0-9a-z-]+$";
        boolean isMatch = Pattern.matches(pattern, tempKey);
        if (!isMatch) {
            throw new BadRequestException("key值只能包含小写字母，数字以及“-”,“_”");
        }

    }

}


