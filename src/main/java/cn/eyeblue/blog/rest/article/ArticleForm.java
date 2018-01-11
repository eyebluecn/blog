package cn.eyeblue.blog.rest.article;

import cn.eyeblue.blog.config.AppContextManager;
import cn.eyeblue.blog.config.exception.UtilException;
import cn.eyeblue.blog.rest.base.BaseEntityForm;
import cn.eyeblue.blog.rest.core.FeatureType;
import cn.eyeblue.blog.rest.tag.Tag;
import cn.eyeblue.blog.rest.tag.TagService;
import cn.eyeblue.blog.rest.user.User;
import cn.eyeblue.blog.util.JsonUtil;
import cn.eyeblue.blog.util.ValidationUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @Size(max = 2147483647, message = "markdown最长2147483647字")
    private String markdown;

    //html
    @NotNull
    @Size(min = 1, max = 2147483647, message = "html必填并且最长2147483647字")
    private String html;

    //文章字数
    @NotNull
    private Integer words;

    //是否是私有文章
    private Boolean privacy = false;

    //是否置顶
    private Boolean top = false;

    //是否接受评论通知。
    private Boolean needNotify = true;


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

        //对于是否置顶，只有管理员有这个权限，其他人默认false.
        if (operator.hasPermission(FeatureType.USER_MANAGE)) {
            article.setTop(top);
        } else {
            article.setTop(false);
        }

        article.setNeedNotify(needNotify);

        //tag比较复杂，后面统一设置。
        List<String> tagUuids = JsonUtil.toStringList(tags);
        TagService tagService = AppContextManager.getBean(TagService.class);

        List<Tag> tagList = tagService.checkTags(tagUuids, article.getUserUuid());
        List<String> okTags = tagList.stream().map(Tag::getUuid).collect(Collectors.toList());
        article.setTags(JsonUtil.toJson(okTags));
    }

    public Article create(User operator) {

        Article entity = new Article();
        entity.setUserUuid(operator.getUuid());
        this.update(entity, operator);
        return entity;
    }

}

