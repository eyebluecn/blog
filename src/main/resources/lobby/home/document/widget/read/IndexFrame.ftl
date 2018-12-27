<#-- 参考 blog-front src/backyard/document/widget/detail/IndexFrame.vue -->

<#import "IndexBlock.ftl" as IndexBlock>

<#macro IndexFrame document preference>
    <#if document.hasChildren()>
    <div class="document-index-read-frame" id="perfect-scrollbar">

        <ul class="visible-mobile global-menu">
             <#list preference.ftlLinks() as link>
                 <li>
                     <a href="${link.url}"
                        target="${link.target}">${link.name}</a>
                 </li>
             </#list>
        </ul>

        <#list document.children as article>
            <@IndexBlock.IndexBlock article=article/>
        </#list>
    </div>
    </#if>
</#macro>