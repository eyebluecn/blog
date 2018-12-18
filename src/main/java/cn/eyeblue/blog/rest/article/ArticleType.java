package cn.eyeblue.blog.rest.article;

import lombok.Getter;


public enum ArticleType {
    DOCUMENT("文档"),
    ARTICLE("单篇文章"),
    DOCUMENT_PLACEHOLDER_ARTICLE("文档-占位文章"),
    DOCUMENT_ARTICLE("文档-文章"),
    DOCUMENT_BLANK("文档-空节点"),
    DOCUMENT_URL("文档-链接");


    @Getter
    private String name;

    ArticleType(String name) {
        this.name = name;
    }

}
