package cn.zicla.blog.rest.article;

import cn.zicla.blog.config.AppContextManager;
import cn.zicla.blog.config.exception.UtilException;
import cn.zicla.blog.rest.base.BaseEntity;
import cn.zicla.blog.rest.base.BaseEntityForm;
import cn.zicla.blog.rest.tag.Tag;
import cn.zicla.blog.rest.tag.TagService;
import cn.zicla.blog.rest.user.User;
import cn.zicla.blog.util.DateUtil;
import cn.zicla.blog.util.JsonUtil;
import cn.zicla.blog.util.ValidationUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


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

    //摘要
    @NotNull
    @Size(min = 1, max = 500, message = "摘要必填并且最长500字")
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

    //文章字数
    @NotNull
    private Integer words;

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

        article.setPosterTankUuid(posterTankUuid);
        article.setPosterUrl(posterUrl);
        article.setDigest(digest);
        article.setIsMarkdown(isMarkdown);

        if (isMarkdown) {
            if (!ValidationUtil.checkParam(markdown)) {
                throw new UtilException("markdown内容必填");
            }
        }

        article.setMarkdown(markdown);
        article.setHtml(html);
        article.setWords(words);
        article.setPrivacy(privacy);
        article.setTop(top);
        article.setReleaseTime(releaseTime);


        //tag比较复杂，后面统一设置。
        List<String> tagUuids = JsonUtil.toStringList(tags);
        TagService tagService = AppContextManager.getBean(TagService.class);
        List<Tag> tagList = tagService.checkTags(tagUuids, operator);
        List<String> okTags = tagList.stream().map(Tag::getUuid).collect(Collectors.toList());
        article.setTags(JsonUtil.toJson(okTags));
    }

    public Article create(User operator) {

        Article entity = new Article();
        this.update(entity, operator);
        entity.setUserUuid(operator.getUuid());
        return entity;
    }

}

