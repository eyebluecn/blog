<@layout.extends name="/common/blank.ftl">

    <@layout.put block="head" type="append">


        <@layout.block name="head"></@layout.block>
    </@layout.put>

    <@layout.put block="content" type="replace">


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
                        <a href="/by" class="bold" target="_blank">${session.user.nickname}</a>
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
                            <a href="/by" class="bold" target="_blank">${session.user.nickname}</a>
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


    </@layout.put>

</@layout.extends>


