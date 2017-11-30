package cn.zicla.blog.rest.article;

import cn.zicla.blog.rest.base.BaseEntityForm;
import cn.zicla.blog.rest.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = false)
@Data
public class ArticleForm extends BaseEntityForm<Article> {


    public ArticleForm() {
        super(Article.class);
    }


    @Override
    protected void update(Article entity, User operator) {

    }

    @Override
    public Article get(User operator) {

        Article article = this.check(uuid);

        this.update(article, operator);

        return article;
    }


}

