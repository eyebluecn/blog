<#-- 单行目录样式 -->
<#macro IndexBlock article>


<div class="index-block">

    <div class="line ${(article.type =='DOCUMENT_BLANK')?string('blank','')}">
        <div class="left-part ${article.hasChildren()?string('fat','')} ${(article.type =='DOCUMENT_PLACEHOLDER_ARTICLE')?string('placeholder','')}">
            <a class="title-span"
               href="${article.visitUrl!""}"
               title="${(article.type =='DOCUMENT_PLACEHOLDER_ARTICLE')?string('该文章尚未创建',article.title)}">${article.title}</a>
        </div>
        <a class="right-part" href="${article.visitUrl!""}">
            ${article.visitPath!""}
        </a>
    </div>

    <#if article.hasChildren()>
        <#list article.children as child>
            <@IndexBlock article=child/>
        </#list>
    </#if>


</div>

</#macro>