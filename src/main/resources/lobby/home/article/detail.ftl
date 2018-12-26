<@layout.extends name="/common/base.ftl">
    <@layout.put block="title" type="replace">
        ${article.title}
    </@layout.put>
    <@layout.put block="head" type="append">

    </@layout.put>

    <@layout.put block="content" type="replace">
        <#import "./widget/ArticleBody.ftl" as ArticleBody>

        <@ArticleBody.ArticleBody article=article session=session/>

    </@layout.put>

</@layout.extends>