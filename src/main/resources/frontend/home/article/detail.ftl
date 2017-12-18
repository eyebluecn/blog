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
                        <div id="comment-area">

                            <div class="input-area">
                                <h2>写下你的评论</h2>
                            </div>
                            <div class="tree-area">

                                <div class="cell-area" v-for="(comment,index) in pager.data">

                                    <div class="media">
                                        <div class="pull-left">
                                            <img class="img-circle img-sm" src="/static/img/avatar.png"/>
                                        </div>
                                        <div class="media-body">
                                            <div class="f14 black">
                                                {{comment.name}}
                                            </div>
                                            <div>
                                                {{comment.createTime | simpleDateHourMinute}}
                                            </div>
                                        </div>
                                    </div>

                                    <div class="mv10 f15 color-333">
                                        {{comment.content}}
                                    </div>
                                    <div class="">
                                        <a class="reply-btn" href="#">
                                            <i class="fa fa-comment-o"></i> 回复
                                        </a>

                                        <a class="reply-btn" href="#">
                                            <i class="fa fa-bug"></i> 举报
                                        </a>
                                    </div>

                                    <#--评论回复区-->
                                    <div class="reply-area">

                                        <div class="sub-cell-area" v-for="(subComment,subIndex) in comment.commentPager.data">
                                            <div class="f15">
                                                <span class="black">{{subComment.name}}: </span>
                                                <span>{{subComment.content}} </span>
                                            </div>
                                            <div class="f12">
                                                <span class="mr5">
                                                    {{subComment.createTime | simpleDateHourMinute}}
                                                </span>

                                                <a class="reply-btn" href="#">
                                                    <i class="fa fa-comment-o"></i> 回复
                                                </a>
                                                <a class="reply-btn" href="#">
                                                    <i class="fa fa-bug"></i> 举报
                                                </a>

                                            </div>

                                        </div>

                                        <div v-if="comment.commentPager.totalItems">
                                            <nb-pager :pager="comment.commentPager"
                                                      :callback="comment.refreshCommentPager()"/>
                                        </div>


                                    </div>


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