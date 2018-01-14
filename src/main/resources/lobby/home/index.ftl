<@layout.extends name="/common/base.ftl">

    <@layout.put block="head" type="append">

    </@layout.put>

    <@layout.put block="content" type="replace">

        <#import "../common/widget/FtlPager.ftl" as FtlPager>
        <#import "../common/widget/FtlTag.ftl" as FtlTag>
        <#import "../common/widget/FtlArticleCell.ftl" as FtlArticleCell>
        <#import "../common/widget/FtlHotArticles.ftl" as FtlHotArticles>

    <div class="row page-index">
        <div class="col-sm-8">

            <#list articlePager.data as article>
                <@FtlArticleCell.FtlArticleCell article=article showUser=true activeTagUuid=""/>
            </#list>
        </div>
        <div class="col-sm-4 mt20">

            <div class="p15">
                <div>
                    热门推荐
                </div>

                        <@FtlHotArticles.FtlHotArticles articles=hotArticlePager.data/>


            </div>


        </div>

        <div class="col-sm-12">
        <@FtlPager.FtlPager pager=articlePager/>
        </div>

    </div>

    </@layout.put>

</@layout.extends>