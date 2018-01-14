<!DOCTYPE html>
<html>
<head>
    <title>

    <@layout.block name="title">
        ${preference.name}
    </@layout.block>

    </title>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="renderer" content="webkit">
    <meta name="viewport" content="user-scalable=no,width=device-width,initial-scale=1,maximum-scale=1">
    <meta name="msapplication-tap-highlight" content="no">
    <meta name="apple-mobile-web-app-capable" content="yes">

    <#if preference.faviconUrl?? && preference.faviconUrl!="">
        <link rel="shortcut icon" type="image/x-icon" href="${preference.faviconUrl}">
    <#else>
        <link rel="shortcut icon" type="image/x-icon" href="/static/img/favicon.ico">
    </#if>

    <link href="/static/fork/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
    <link href="/static/fork/toastr/build/toastr.min.css" rel="stylesheet" type="text/css">
    <link href="/static/css/main.css" rel="stylesheet" type="text/css">

    <script src="/static/fork/jquery/dist/jquery.min.js"></script>
    <script src="/static/fork/bootstrap/dist/js/bootstrap.min.js"></script>

    <script src="/static/fork/velocity-animate/velocity.js"></script>
    <script src="/static/fork/toastr/build/toastr.min.js"></script>

    <script src="/static/js/common/widget/FtlPager.js"></script>
    <script src="/static/js/common/widget/FtlTab.js"></script>
    <script src="/static/js/common/widget/FtlExpanding.js"></script>


    <script src="/static/js/init.js"></script>

<@layout.block name="head"></@layout.block>

</head>
<body>

<div class="nb-app">

    <div class="section-navigation">
        <div class="container">
            <div class="row">
                <div class="col-lg-10 col-lg-offset-1 col-md-10 col-md-offset-1">
                    <a href="/">
                        <img src="${preference.logoUrl!"/static/img/logo.png"}" class="logo"/>
                        <span class="site-title">
                        ${preference.name}
                        </span>
                    </a>
                    <div class="menus hidden-xs">
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

                    <#if session.user.role!="GUEST">
                        <a href="/by" class="bold" target="_blank">${session.user.username}</a>
                    </#if>

                    </div>
                    <div class="visible-xs pull-right">
                        <i class="fa fa-navicon f17 ln60 cursor text-primary" data-expanding-target="menuSlide"></i>

                    </div>

                </div>
            </div>
        </div>
    </div>

    <div class="section-body">
        <div class="container">

            <div class="row">

                <#--放在这里为了让其收缩自如-->
                <div data-expanding-id="menuSlide" data-expanding-show="false"
                     class="menuExpand hidden-sm hidden-md hidden-lg">
                    <ul>
                    <#if preference.menuName1?? && preference.menuName1!="">
                    <li><a href="${preference.menuUrl1!""}"
                           target="${preference.menuUrl1?starts_with("http")?string('_blank','_self')}">${preference.menuName1}</a>
                    </li>
                    </#if>
                    <#if preference.menuName2?? && preference.menuName2!="">
                    <li><a href="${preference.menuUrl2!""}"
                           target="${preference.menuUrl2?starts_with("http")?string('_blank','_self')}">${preference.menuName2}</a>
                    </li>
                    </#if>
                    <#if preference.menuName3?? && preference.menuName3!="">
                    <li><a href="${preference.menuUrl3!""}"
                           target="${preference.menuUrl3?starts_with("http")?string('_blank','_self')}">${preference.menuName3}</a>
                    </li>
                    </#if>
                    <#if preference.menuName4?? && preference.menuName4!="">
                    <li><a href="${preference.menuUrl4!""}"
                           target="${preference.menuUrl4?starts_with("http")?string('_blank','_self')}">${preference.menuName4}</a>
                    </li>
                    </#if>
                    <#if preference.menuName5?? && preference.menuName5!="">
                    <li><a href="${preference.menuUrl5!""}"
                           target="${preference.menuUrl5?starts_with("http")?string('_blank','_self')}">${preference.menuName5}</a>
                    </li>
                    </#if>
                    <#if session.user.role!="GUEST">
                        <li>
                            <a href="/by" class="bold" target="_blank">${session.user.username}</a>
                        </li>
                    </#if>
                    </ul>
                </div>
                <div class="col-lg-10 col-lg-offset-1 col-md-10 col-md-offset-1">
                    <div class="page-inner-container">
                    <@layout.block name="content"></@layout.block>
                    </div>
                </div>
            </div>

        </div>

    </div>


    <div class="section-footer">

        <div class="container">
            <div class="row">
                <div class="col-lg-10 col-lg-offset-1 col-md-10 col-md-offset-1">

                    <div>
                    <#if preference.footerLine1?? && preference.footerLine1!="">
                        <span class="inline-block">${preference.footerLine1}</span>
                    </#if>
                    <#if preference.footerLine2?? && preference.footerLine2!="">
                        <span class="inline-block">${preference.footerLine2}</span>
                    </#if>
                    </div>

                    <!-- 版本号：cn.eyeblue.blog:1.0.1 -->
                    <!-- 开源不易，请不要移除掉这里的代码，蓝眼博客谢谢您 ^_^ -->
                    <div>
                        <span class="shuiliandong">Powered by</span> <a href="https://github.com/eyebluecn/blog"
                                                                        target="_blank"><img
                            class="w20"
                            src="/static/img/eyeblue-blog.png"/>
                        蓝眼博客</a>
                    </div>
                    <script type="text/javascript">
                        <#--这里设置一个隐藏的后台入口，点击powered进入后台-->
                        $(function () {
                            $(".shuiliandong").click(function () {
                                window.open("/by")
                            });
                        });
                    </script>
                </div>
            </div>
        </div>
    </div>


</div>

</body>
</html>
