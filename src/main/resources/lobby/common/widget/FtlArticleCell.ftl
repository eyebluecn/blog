<#-- 标签样式 -->
<#macro FtlArticleCell article showUser=true activeTagUuid="">
<div class="article-cell">
    <#if showUser>
            <div class="media">
                <div class="pull-left">
                    <a href="/home/user/${article.user.uuid}">
                        <#if article.user.avatarUrl?? && article.user.avatarUrl!="">
                            <img class="img-circle w40 h40" src="${article.user.avatarUrl}?imageProcess=resize&imageResizeM=fill&imageResizeW=100&imageResizeH=100"/>
                        <#else>
                            <img class="img-circle w40 h40" src="/static/img/avatar.png"/>
                        </#if>
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
    </#if>

    <div class="media">
                        <#if article.posterUrl?? && article.posterUrl!="">
                        <div class="pull-right">
                            <a href="/home/article/${article.uuid}">
                                <img src="${article.posterUrl}?imageProcess=resize&imageResizeM=fit&imageResizeW=100"/>
                            </a>
                        </div>
                        </#if>

        <div class="media-body">
            <div class="title">
                <a href="/home/article/${article.uuid}">
                    <#if article.top>
                        <i class="fa fa-rocket text-danger" title="置顶文章"></i>
                    </#if>
                    ${article.title}
                </a>
            </div>
            <div class="digest">
                ${article.digest!""}
            </div>
            <div>
                                <#list article.tagArray as tag>
                                    <@FtlTag.FtlTag tag=tag active=(activeTagUuid==tag.uuid)/>
                                </#list>
            </div>
        </div>
    </div>

    <#if !showUser>
                <div class="">
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
    </#if>

</div>
</#macro>