<@layout.extends name="/common/blank.ftl">
    <@layout.put block="title" type="replace">
        ${document.title}
    </@layout.put>
    <@layout.put block="head" type="append">

    <#--引入侧边滚动-->
    <script src="/static/fork/perfect-scrollbar-1.4.0/perfect-scrollbar.min.js"></script>
    <link href="/static/fork/perfect-scrollbar-1.4.0/perfect-scrollbar.css" rel="stylesheet" type="text/css">

            <script type="text/javascript">
                $(function () {

                    //启用perfect-scrollbar
                    var ps = new PerfectScrollbar('#perfect-scrollbar');

                });
            </script>


    </@layout.put>

    <@layout.put block="content" type="replace">
        <#import "widget/read/IndexFrame.ftl" as IndexFrame>
        <#import "../article/widget/ArticleBody.ftl" as ArticleBody>




    <div class="page-document-read">
        <div class="upper-part">
            <div class="logo-title">
                <a class="logo-a" href="/">
                    <img class="logo" src="${preference.logoUrl!"/static/img/logo.png"}"/>
                </a>
                <a class="title" href="${document.visitUrl!""}">
                    ${document.title}
                </a>
            </div>
            <div class="preference-navi">
                <#if preference.menuName1?? && preference.menuName1!="">
                        <a href="${preference.menuUrl1!""}"
                           target="${preference.menuUrl1?starts_with("http")?string('_blank','_self')}">${preference.menuName1}</a>
                </#if>
                    <#if preference.menuName2?? && preference.menuName2!="">
                        <a href="${preference.menuUrl2!""}"
                           target="${preference.menuUrl2?starts_with("http")?string('_blank','_self')}">${preference.menuName2}</a>
                    </#if>
                    <#if preference.menuName3?? && preference.menuName3!="">
                        <a href="${preference.menuUrl3!""}"
                           target="${preference.menuUrl3?starts_with("http")?string('_blank','_self')}">${preference.menuName3}</a>
                    </#if>
                    <#if preference.menuName4?? && preference.menuName4!="">
                        <a href="${preference.menuUrl4!""}"
                           target="${preference.menuUrl4?starts_with("http")?string('_blank','_self')}">${preference.menuName4}</a>
                    </#if>
                    <#if preference.menuName5?? && preference.menuName5!="">
                        <a href="${preference.menuUrl5!""}"
                           target="${preference.menuUrl5?starts_with("http")?string('_blank','_self')}">${preference.menuName5}</a>
                    </#if>
            </div>
        </div>
        <div class="middle-part">
            <div class="left-part">

                <@IndexFrame.IndexFrame document=document/>

            </div>
            <div class="right-part">
                <#if article.type == "DOCUMENT_PLACEHOLDER_ARTICLE">
                    <span class="italic">
                        <span class="bold">${article.title}</span>
                        尚未创建
                    </span>
                <#else>

                    <@ArticleBody.ArticleBody article=article session=session/>

                </#if>

            </div>
        </div>
    </div>

    </@layout.put>

</@layout.extends>