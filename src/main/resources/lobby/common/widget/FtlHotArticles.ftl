<#-- 标签样式 -->
<#macro FtlHotArticles articles>
    <#list articles as article>
        <div class="hot-article">
            <a href="/a/${article.user.username}/${article.path}">
                ${article.title}
            </a>
            ${article.hit}阅读
        </div>
    </#list>
</#macro>