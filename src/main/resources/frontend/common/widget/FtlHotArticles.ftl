<#-- 标签样式 -->
<#macro FtlHotArticles articles>
    <#list articles as article>
        <div class="hot-article">
            <a href="/home/article/${article.uuid}">
                ${article.title}
            </a>
            ${article.hit}阅读
        </div>
    </#list>
</#macro>