<@layout.extends name="/common/base.ftl">
    <@layout.put block="title" type="replace">
    ${article.title}
    </@layout.put>
    <@layout.put block="head" type="append">

    </@layout.put>

    <@layout.put block="content" type="replace">
        ${article.html}
    </@layout.put>

</@layout.extends>