package cn.eyeblue.blog.rest.article;

import lombok.Getter;


public enum ArticleType {
    DOCUMENT("知识库"),
    ARTICLE("单篇文章"),
    DOCUMENT_ARTICLE("知识库中的文章");


    @Getter
    private String name;

    ArticleType(String name) {
        this.name = name;
    }

}
