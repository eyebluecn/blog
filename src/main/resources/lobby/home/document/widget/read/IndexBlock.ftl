<#-- 单行目录样式 -->
<#macro IndexBlock article>


<div class="index-block">

    <div class="line
${(article.type =='DOCUMENT_BLANK')?string('blank','')}
${article.hasChildren()?string('fat','')}
${(article.type =='DOCUMENT_PLACEHOLDER_ARTICLE')?string('placeholder','')}">

        <a class="title-span"
           href="${article.visitUrl!""}"
           target="${(article.type =='DOCUMENT_URL')?string('_blank','_self')}"
           title="${(article.type =='DOCUMENT_PLACEHOLDER_ARTICLE')?string('该文章尚未创建',article.title)}">${article.title}</a>

    </div>

    <#if article.hasChildren()>
        <#list article.children as child>
            <@IndexBlock article=child/>
        </#list>
    </#if>


</div>

</#macro>