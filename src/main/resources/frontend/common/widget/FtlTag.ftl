<#-- 标签样式 -->
<#macro FtlTag tag>
    <a class="tag-collection" target="_blank" href="#">

        <img src="${tag.logoUrl!""}?imageProcess=resize&imageResizeM=fill&imageResizeW=100&imageResizeH=100" alt="100">
        <span class="name">${tag.name}</span>
    </a>
</#macro>