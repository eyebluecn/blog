<@layout.extends name="/common/blank.ftl">
    <@layout.put block="title" type="replace">
        ${article.title}
    </@layout.put>
    <@layout.put block="head" type="append">

    <#--引入侧边滚动-->
    <script src="/static/fork/perfect-scrollbar-1.4.0/perfect-scrollbar.min.js"></script>
    <link href="/static/fork/perfect-scrollbar-1.4.0/perfect-scrollbar.css" rel="stylesheet" type="text/css">

            <script type="text/javascript">
                $(function () {

                    //启用perfect-scrollbar
                    var ps = new PerfectScrollbar('#perfect-scrollbar');

                    var $sideMenu = $("#side-menu");
                    var $trigger = $("#side-menu-trigger");
                    //在小屏幕控制左边菜单收起。
                    $trigger.click(function () {

                        if ($sideMenu.hasClass("show-drawer")) {
                            $sideMenu.removeClass("show-drawer");

                            $trigger.addClass("fa-navicon");
                            $trigger.addClass("text-primary");
                            $trigger.removeClass("fa-close");
                            $trigger.removeClass("text-danger");

                        } else {
                            $sideMenu.addClass("show-drawer");

                            $trigger.removeClass("fa-navicon");
                            $trigger.removeClass("text-primary");
                            $trigger.addClass("fa-close");
                            $trigger.addClass("text-danger");
                        }

                    });


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
                <div class="visible-pc pull-right">
                    <#list preference.ftlLinks() as link>
                        <a href="${link.url}"
                           target="${link.target}">${link.name}</a>
                    </#list>
                </div>
                <div class="visible-mobile pull-right">
                    <i class="fa fa-navicon f17 ln60 cursor text-primary" id="side-menu-trigger"></i>
                </div>
            </div>
        </div>
        <div class="middle-part">
            <div class="left-part" id="side-menu">

                <@IndexFrame.IndexFrame document=document preference=preference/>

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