package cn.zicla.blog.rest.article;

import cn.zicla.blog.config.exception.UtilException;
import cn.zicla.blog.rest.base.BaseEntityForm;
import cn.zicla.blog.rest.user.User;
import cn.zicla.blog.util.DateUtil;
import cn.zicla.blog.util.JsonUtil;
import cn.zicla.blog.util.ValidationUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.ValidationUtils;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;


@EqualsAndHashCode(callSuper = false)
@Data
public class ArticleForm extends BaseEntityForm<Article> {

    //标题
    @NotNull
    @Size(min = 1, max = 255, message = "名称必填并且最长255字")
    private String title;

    //标签
    @NotNull
    @Size(min = 1, max = 1024, message = "标签必填并且最长1024字")
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

    //内容
    @Size(max = 100000, message = "markdown最长100000字")
    private String markdown;

    //html
    @NotNull
    @Size(min = 1, max = 100000, message = "html必填并且最长100000字")
    private String html;


    //是否是私有文章
    private Boolean privacy = false;

    //是否置顶
    private Boolean top = false;


    //发布日期
    @NotNull
    @DateTimeFormat(pattern = DateUtil.DEFAULT_FORMAT)
    private Date releaseTime;


    public ArticleForm() {
        super(Article.class);
    }

    @Override
    protected void update(Article article, User operator) {
        article.setTitle(title);

        JsonUtil.toStringList(tags);
        article.setTags(tags);

        article.setPosterTankUuid(posterTankUuid);
        article.setPosterUrl(posterUrl);
        article.setAuthor(author);
        article.setDigest(digest);
        article.setIsMarkdown(isMarkdown);

        if (isMarkdown) {
            if (!ValidationUtil.checkParam(markdown)) {
                throw new UtilException("markdown内容必填");
            }

        }

        article.setMarkdown(markdown);
        article.setHtml(html);

        article.setPrivacy(privacy);
        article.setTop(top);
        article.setReleaseTime(releaseTime);
    }

    public Article create(User operator) {

        Article entity = new Article();
        this.update(entity, operator);
        entity.setUserUuid(operator.getUuid());
        return entity;
    }

}

