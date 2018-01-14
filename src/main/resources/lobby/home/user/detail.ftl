<@layout.extends name="/common/base.ftl">

    <@layout.put block="head" type="append">

    </@layout.put>

    <@layout.put block="content" type="replace">

        <#import "../../common/widget/FtlPager.ftl" as FtlPager>
        <#import "../../common/widget/FtlTag.ftl" as FtlTag>
        <#import "../../common/widget/FtlArticleCell.ftl" as FtlArticleCell>
        <#import "../../common/widget/FtlHotArticles.ftl" as FtlHotArticles>
        <#import "../../common/widget/FtlUserAchievement.ftl" as FtlUserAchievement>

    <div class="page-user-detail row">
        <div class="col-sm-4">

            <div>

                <div>
                    <@FtlUserAchievement.FtlUserAchievement user=author/>
                </div>

                <div class="mt20 mb15">
                    TA的文章分类
                </div>
                <div>
                    <#list tagPager.data as tag>
                        <@FtlTag.FtlTag tag=tag size="lg" active=(tagUuid==tag.uuid)/>
                    </#list>
                </div>

                <div class="mt20">
                    TA的热门文章
                </div>
                <div>

                        <@FtlHotArticles.FtlHotArticles articles=hotArticlePager.data/>
                </div>

            </div>

        </div>

        <div class="col-sm-8">

            <#list articlePager.data as article>
                <@FtlArticleCell.FtlArticleCell article=article showUser=false activeTagUuid=tagUuid/>
            </#list>

                <@FtlPager.FtlPager pager=articlePager emptyHint="该分类下暂无文章"/>
        </div>



    </div>

    </@layout.put>

</@layout.extends>