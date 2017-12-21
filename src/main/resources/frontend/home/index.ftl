<@layout.extends name="/common/base.ftl">

    <@layout.put block="head" type="append">

    </@layout.put>

    <@layout.put block="content" type="replace">

        <#import "../common/widget/FtlPager.ftl" as FtlPager>
        <#import "../common/widget/FtlTag.ftl" as FtlTag>

    <div class="page-index">
        <div class="row">
            <div class="col-sm-8">

            <#list articlePager.data as article>
                <div class="article-cell">
                    <div class="media">
                        <div class="pull-left">
                            <img class="img-circle w40 h40" src="${article.user.avatarUrl!"/static/img/avatar.png"}"/>
                        </div>
                        <div class="media-body">
                            <div class="f16">
                                ${article.user.username}
                            </div>
                            <div class="mix">
                                <span class="mr10">
                                    ${article.createTime?string("yyyy-MM-dd HH:mm")}
                                </span>
                                <span class="mr10">
                                    字数 ${article.words}
                                </span>
                                <span class="mr10">
                                    阅读 ${article.hit}
                                </span>
                                <span class="mr10">
                                    评论 ${article.commentNum}
                                </span>
                                <span class="mr10">
                                    点赞 ${article.agree}
                                </span>
                            </div>
                        </div>
                    </div>

                    <div class="media">
                        <#if article.posterUrl?? && article.posterUrl!="">
                        <div class="pull-right">
                            <a href="/home/article/${article.uuid}">
                                <img src="${article.posterUrl}?imageProcess=resize&imageResizeM=fill&imageResizeW=100&imageResizeH=100"/>
                            </a>
                        </div>
                        </#if>

                        <div class="media-body">
                            <div class="title">
                                <a href="/home/article/${article.uuid}">
                                    ${article.title}
                                </a>
                            </div>
                            <div class="digest">
                                ${article.digest!""}
                            </div>
                            <div>
                                <#list article.tagArray as tag>
                                    <@FtlTag.FtlTag tag=tag/>
                                </#list>
                            </div>
                        </div>
                    </div>


                </div>
            </#list>
            </div>
            <div class="col-sm-4 mt20">

                <div class="hot-area">
                    <div>
                        热门推荐
                    </div>

                    <#list hotArticlePager.data as article>
                        <div class="hot-article">
                            <a href="/home/article/${article.uuid}">
                                ${article.title}
                            </a>
                            ${article.hit}阅读
                        </div>
                    </#list>
                </div>



            </div>
        </div>

        <div>
        <@FtlPager.FtlPager pager=articlePager/>
        </div>

    </div>

    </@layout.put>

</@layout.extends>