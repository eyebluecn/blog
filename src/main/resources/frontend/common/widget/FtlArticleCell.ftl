<#-- 标签样式 -->
<#macro FtlArticleCell article>
<div class="article-cell">
    <div class="media">
        <div class="pull-left">
            <a href="/home/user/${article.user.uuid}">
                <img class="img-circle w40 h40" src="${article.user.avatarUrl!"/static/img/avatar.png"}"/>
            </a>
        </div>
        <div class="media-body">
            <div class="f16">
                <a class="author-name" href="/home/user/${article.user.uuid}">
                    ${article.user.username}
                </a>

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
</#macro>