package cn.eyeblue.blog.rest.article;

import cn.eyeblue.blog.config.exception.BadRequestException;
import cn.eyeblue.blog.rest.base.BaseEntity;
import cn.eyeblue.blog.rest.tag.Tag;
import cn.eyeblue.blog.rest.tank.Tank;
import cn.eyeblue.blog.rest.user.User;
import cn.eyeblue.blog.util.StringUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
public class Article extends BaseEntity {

    //第一级菜单的puuid是ROOT.
    public final static String ROOT = "ROOT";

    //发布者
    private String userUuid;

    //标题
    private String title;

    //文章访问路径
    private String path;

    //标签
    private String tags = EMPTY_JSON_ARRAY;

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

    //对应文档的uuid，只有类型是 DOCUMENT_ARTICLE时用到此字段
    private String documentUuid;

    //自己作为文档文章时，标题的父级是什么。第一级菜单的puuid是ROOT
    private String puuid = ROOT;

    //类型：知识库(DOCUMENT)，单篇文章(ARTICLE)，知识库中的文章(DOCUMENT_ARTICLE)
    @Enumerated(EnumType.STRING)
    private ArticleType type = ArticleType.ARTICLE;

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

    //对应的文档
    @Transient
    private Article document;

    //作为文档的目录结构，子目录结构。
    @Transient
    private List<Article> children;

    //该篇文章的url。
    @Transient
    private String visitUrl;
    //该篇文章最终的path。对于DOCUMENT_URL类型使用的是digest.
    @Transient
    private String visitPath;

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

        String pattern = "^[0-9a-z-_]+$";
        boolean isMatch = Pattern.matches(pattern, tempKey);
        if (!isMatch) {
            throw new BadRequestException("路径只能包含小写字母，数字以及“-”,“_”");
        }

    }

    //往自己的children中添加一个子节点。
    public void addChild(Article article) {
        if (children == null) {
            children = new ArrayList<>();
        }

        children.add(article);

        //按照sequence来排序。
        children.sort(Comparator.comparingLong(o -> o.sort));
    }


    //是否有子目录
    public boolean hasChildren() {
        return CollectionUtils.isNotEmpty(this.getChildren());
    }


    //装载本篇文章的访问路径和访问链接
    public void loadVisitUrlAndPath(Article document, User user) {

        if (this.type == ArticleType.ARTICLE) {
            this.visitUrl = StringUtil.format("/a/{}/{}", this.getUser().getUsername(), this.getPath());
            this.visitPath = this.path;
        } else if (this.type == ArticleType.DOCUMENT) {
            this.visitUrl = StringUtil.format("/d/{}/{}", this.getUser().getUsername(), this.getPath());
            this.visitPath = null;
        } else if (this.type == ArticleType.DOCUMENT_PLACEHOLDER_ARTICLE) {

            if (document == null || user == null) {
                this.visitUrl = null;
            } else {
                this.visitUrl = StringUtil.format("/d/{}/{}/{}",
                        user.getUsername(),
                        document.getPath(),
                        this.getPath());
            }
            this.visitPath = this.path;


        } else if (this.type == ArticleType.DOCUMENT_ARTICLE) {

            if (document == null || user == null) {
                this.visitUrl = null;
            } else {
                this.visitUrl = StringUtil.format("/d/{}/{}/{}",
                        user.getUsername(),
                        document.getPath(),
                        this.getPath());
            }
            this.visitPath = this.path;


        } else if (this.type == ArticleType.DOCUMENT_BLANK) {

            this.visitUrl = null;
            this.visitPath = null;

        } else if (this.type == ArticleType.DOCUMENT_URL) {
            this.visitUrl = this.digest;
            this.visitPath = this.digest;
        } else {
            this.visitUrl = null;
            this.visitPath = null;
        }
    }

    ///////////////以下方法提供给ftl使用/////////////////////////


    //是否为blank类型
    public boolean ftlIsTypeDocumentBlank() {
        return this.type == ArticleType.DOCUMENT_BLANK;
    }

    //访问路径
    public String ftlVisitPath() {
        if (this.type == ArticleType.DOCUMENT_PLACEHOLDER_ARTICLE || this.type ==
                ArticleType.DOCUMENT_ARTICLE) {
            return this.path;
        } else if (type == ArticleType.DOCUMENT_URL) {
            return this.digest;
        } else {
            return "";
        }
    }

}


