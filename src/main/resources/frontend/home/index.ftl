<@layout.extends name="/common/base.ftl">
    <@layout.put block="title" type="replace">
    个人博客 | 博客首页
    </@layout.put>
    <@layout.put block="head" type="append">

    </@layout.put>

    <@layout.put block="content" type="replace">

        <#import "../common/widget/NbPager.ftl" as NbPager>

        <h1>这里就是博客首页的内容吗？</h1>

    <div>
        分页的东东
    </div>
    <div>
        <@NbPager.NbPager pager=articlePager/>
    </div>



    </@layout.put>

</@layout.extends>