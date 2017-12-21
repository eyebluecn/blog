<#-- 分页（Pager对象、链接URL、参数Map、最大页码显示数） -->
<#macro FtlPager pager baseUrl=baseUrl colOffset = 3 emptyHint="没有符合条件的项目">
    <#local pageNumber = pager.page+1 />
    <#local pageSize = pager.pageSize />
    <#local pageCount = pager.totalPages />
    <#local itemCount = pager.totalItems />
    <#local parameter = "" />
    <#local maxShowPageCount = (colOffset * 2 + 1) />


    <#list requestParams?keys as key>
        <#if requestParams[key]?? && key!="page" && key!="pageSize">
            <#local parameter = parameter + "&" + key + "=" + requestParams[key] />
        </#if>
    </#list>

    <#local pageSizeOptionParameter = parameter/>
    <#local parameter = parameter + "&pageSize=" + pageSize />

    <#if baseUrl?contains("?")>
        <#local baseUrl = baseUrl + "&" />
    <#else>
        <#local baseUrl = baseUrl + "?" />
    </#if>
    <#local firstPageUrl = baseUrl + "page=0" + parameter />
    <#local lastPageUrl = baseUrl + "page=" + (pageCount-1) + parameter />
    <#local prePageUrl = baseUrl + "page=" + (pageNumber - 2) + parameter />
    <#local nextPageUrl = baseUrl + "page=" + pageNumber + parameter />
    <#local pageSizeOptionUrl = baseUrl + "page=0" + pageSizeOptionParameter />

    <#local startPageNumber = 1 />
    <#local endPageNumber = 1 />

    <#if (pageCount <= maxShowPageCount)>
        <#local startPageNumber = 1 />
        <#local endPageNumber = pageCount />

    <#else>

        <#if ( pageNumber * 2 < maxShowPageCount)>
            <#local startPageNumber = 1 />
            <#local endPageNumber = maxShowPageCount />

        <#elseif ((pageNumber+colOffset) > pageCount)>
            <#local startPageNumber = (pageCount - maxShowPageCount) + 1 />
            <#local endPageNumber = pageCount />
        <#else>
            <#local startPageNumber = (pageNumber - colOffset) />
            <#local endPageNumber = (pageNumber + colOffset)  />
        </#if>

    </#if>

    <#if (pageCount == 0) || (pageNumber > pageCount)>
    <div class="italic text-center">
        ${emptyHint}
    </div>
    </#if>

    <#if (pageCount > 1) && (pageNumber <= pageCount)>
    <div class="text-center">
        <nav>
            <ul class="pagination mt20 mb0">

                <#if (pageNumber > 1)>
                    <li>
                        <a href="${firstPageUrl}">&laquo;</a>
                    </li>
                    <li>
                        <a href="${prePageUrl}">&lsaquo;</a>
                    </li>
                </#if>

                <#list startPageNumber .. endPageNumber as index>
                    <#if pageNumber != index>

                        <li>
                            <a href="${baseUrl + "page=" + (index-1) + parameter}">${index}</a>
                        </li>
                    <#else>
                        <li class="active">
                            <a href="javascript:void(0)">${index}</a>
                        </li>

                    </#if>
                </#list>


                <#if (pageNumber < pageCount)>
                    <li>
                        <a href="${nextPageUrl}">&rsaquo;</a>
                    </li>
                    <li>
                        <a href="${lastPageUrl}">&raquo;</a>
                    </li>
                </#if>

            </ul>
        </nav>


    </div>
    </#if>

    <#if (pageCount > 1) && (pageNumber <= pageCount)>
    <div class="text-center mt10">
        每页
        <select class="pageSizeSelect" title="">

            <#list [5, 10, 15, 20, 30, 50] as opt>
                <#if (opt != pageSize)>
                    <option value="${pageSizeOptionUrl}&pageSize=${opt}">${opt}</option>
                <#else>
                    <option disabled selected>${opt}</option>
                </#if>

            </#list>
        </select>
        条

        共 ${itemCount} 条
    </div>

    </#if>
</#macro>