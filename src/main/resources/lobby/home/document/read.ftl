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

    <div class="page-document-read">
        <div class="upper-part">
            <a class="logo-a" href="/">
                <img class="logo" src="${preference.logoUrl!"/static/img/logo.png"}"/>
            </a>

            <a class="title">
                ${document.title}
            </a>
        </div>
        <div class="middle-part">
            <div class="left-part">

                <@IndexFrame.IndexFrame document=document/>

            </div>
            <div class="right-part">
                ${article.html}
            </div>
        </div>
    </div>

    </@layout.put>

</@layout.extends>