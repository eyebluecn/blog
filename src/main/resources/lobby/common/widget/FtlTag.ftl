<#-- 标签样式 -->
<#macro FtlTag tag size="" active=false>

    <#if active>
        <#local url = "/home/user/"+tag.userUuid />
    <#else>
        <#local url = "/home/user/"+tag.userUuid+"?tagUuid="+tag.uuid />
    </#if>

    <a class="tag-collection ${size} ${active?string('active','')}"
       href="${url}">

        <img src="${tag.logoUrl!""}?ir=fill_100_100" alt="100">
        <span class="name">${tag.name}</span>
    </a>
</#macro>