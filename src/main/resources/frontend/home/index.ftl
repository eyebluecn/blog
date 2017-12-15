<@layout.extends name="/common/base.ftl">
    <@layout.put block="title" type="replace">
    个人博客 | 博客首页
    </@layout.put>
    <@layout.put block="head" type="append">

    </@layout.put>

    <@layout.put block="content" type="replace">

        <#import "../common/widget/NbPager.ftl" as NbPager>

        <#list articlePager.data as article>
        <div class="border-bottom mt20">
            <div>
                <h3>
                    <a href="/home/article/${article.uuid}">
                        ${article.title}
                    </a>
                </h3>
            </div>
            <div>
                <p>${article.createTime}</p>
            </div>
        </div>

        </#list>

    <div>
        <@NbPager.NbPager pager=articlePager/>
    </div>



    </@layout.put>

</@layout.extends>