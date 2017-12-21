<@layout.extends name="/common/base.ftl">

    <@layout.put block="head" type="append">

    </@layout.put>

    <@layout.put block="content" type="replace">

        <#import "../../common/widget/FtlPager.ftl" as FtlPager>
        <#import "../../common/widget/FtlTag.ftl" as FtlTag>
        <#import "../../common/widget/FtlArticleCell.ftl" as FtlArticleCell>
        <#import "../../common/widget/FtlHotArticles.ftl" as FtlHotArticles>

    <div class="page-user-detail">
        <div class="row">
            <div class="col-sm-4 mt20">

                <div class="p15">

                    <div class="text-center">
                        <div>
                            <img class="img-circle w100 h100" src="${author.avatarUrl!"/static/img/avatar.png"}"/>
                        </div>
                        <div class="f18 bold mv15">
                            ${author.username}
                        </div>

                    </div>


                    <div>

                        <div class="achievement-cell">
                            <i class="fa fa-pencil-square"></i>
                            <span>撰写了 ${author.articleNum} 篇文章</span>
                        </div>

                        <div class="achievement-cell">
                            <i class="fa fa-font"></i>
                            <span>总共有 ${author.articleWords} 字</span>
                        </div>

                        <div class="achievement-cell">
                            <i class="fa fa-book"></i>
                            <span>被阅读 ${author.articleHit} 次</span>
                        </div>

                        <div class="achievement-cell">
                            <i class="fa fa-thumbs-o-up"></i>
                            <span>获得了 ${author.articleAgreeNum} 次赞</span>
                        </div>

                        <div class="achievement-cell">
                            <i class="fa fa-commenting-o"></i>
                            <span>共收到 ${author.commentNum} 条评论</span>
                        </div>



                    </div>

                    <div class="mt20">
                        TA的热门文章
                    </div>

                        <@FtlHotArticles.FtlHotArticles articles=hotArticlePager.data/>
                </div>

            </div>

            <div class="col-sm-8">

            <#list articlePager.data as article>
                <@FtlArticleCell.FtlArticleCell article=article/>
            </#list>
            </div>

        </div>

        <div>
        <@FtlPager.FtlPager pager=articlePager/>
        </div>

    </div>

    </@layout.put>

</@layout.extends>