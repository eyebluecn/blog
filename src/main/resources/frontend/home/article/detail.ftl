<@layout.extends name="/common/base.ftl">
    <@layout.put block="title" type="replace">
        ${article.title}
    </@layout.put>
    <@layout.put block="head" type="append">

        <script src="/static/node_modules/vue/dist/vue.min.js"></script>
        <script src="/static/node_modules/vue-resource/dist/vue-resource.min.js"></script>

        <script src="/static/js/common/vue/util/Utils.js"></script>

        <script src="/static/js/common/vue/base/Base.js"></script>
        <script src="/static/js/common/vue/base/BaseEntity.js"></script>
        <script src="/static/js/common/vue/base/Filter.js"></script>
        <script src="/static/js/common/vue/base/Pager.js"></script>
        <script src="/static/js/common/vue/model/Comment.js"></script>


    <#--引入vue需要的NbPager-->
        <#include "../../common/vue/nb-pager.ftl">
        <@NbPager/>

        <#include "../../common/vue/nb-simple.ftl">
        <@NbSimple/>


        <script src="/static/js/home/article/detail.js"></script>
    </@layout.put>

    <@layout.put block="content" type="replace">

        <div class="row mt40 mb100">
            <div class="col-lg-10 col-lg-offset-1 col-lg-10 col-lg-offset-1">

                <div class="article-content">
                    ${article.html}
                </div>

                <div class="row mt20">
                    <div class="col-xs-12">
                        <h2>写下你的评论</h2>
                    </div>

                    <div class="col-xs-12">
                        <div id="comment-area">

                            <div class="input-area">
                                {{message}}
                            </div>
                            <div class="tree-area">

                                <div v-for="(comment,index) in pager.data">
                                    {{comment.content}}
                                </div>

                            </div>

                            <div class="pager-area">
                                <nb-pager :pager="pager" :callback="refresh"/>
                            </div>


                        </div>

                    </div>
                </div>

            </div>
        </div>




    </@layout.put>

</@layout.extends>