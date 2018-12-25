<#-- 参考 blog-front src/backyard/document/widget/detail/IndexFrame.vue -->

<#import "IndexBlock.ftl" as IndexBlock>

<#macro IndexFrame document>
    <#if document.hasChildren()>
    <div class="document-index-detail-frame">
        <#list document.children as article>
            <@IndexBlock.IndexBlock article=article/>
        </#list>
    </div>
    </#if>
</#macro>