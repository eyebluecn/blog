package cn.eyeblue.blog.rest.article;

import lombok.Getter;


public enum ArticleType {
    DOCUMENT("文档"),
    ARTICLE("单篇文章"),
    DOCUMENT_ARTICLE("文档中的文章"),
    DOCUMENT_BLANK("文档中的空节点"),
    DOCUMENT_URL("文档中的链接");


    @Getter
    private String name;

    ArticleType(String name) {
        this.name = name;
    }

}
