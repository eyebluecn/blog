<@layout.extends name="/common/base.ftl">
    <@layout.put block="title" type="replace">
        ${article.title}
    </@layout.put>
    <@layout.put block="head" type="append">

    <#--首先引用vue-->
        <script src="/static/node_modules/vue/dist/vue.min.js"></script>
        <script src="/static/node_modules/vue-resource/dist/vue-resource.min.js"></script>

    <#--然后引用通用库-->
        <script src="/static/js/common/vue/util/Utils.js"></script>
    <#--再引用父类-->
        <script src="/static/js/common/vue/base/Base.js"></script>
        <script src="/static/js/common/vue/base/BaseEntity.js"></script>
    <#--再引用基础共用件-->
        <script src="/static/js/common/vue/base/Filter.js"></script>
        <script src="/static/js/common/vue/base/Pager.js"></script>
    <#--再引入自定义类-->
        <script src="/static/js/common/vue/model/Comment.js"></script>


    <#--再引入自定义组件-->
        <#include "../../common/vue/nb-pager.ftl">
        <@NbPager/>

        <#include "../../common/vue/nb-expanding.ftl">
        <@NbExpanding/>

        <#include "../../common/vue/nb-comment-panel.ftl">
        <@NbCommentPanel/>


        <script src="/static/js/home/article/detail.js"></script>


    <#--为了正常解析markdown文档-->
        <link href="/static/node_modules/editor.md/css/editormd.preview.min.css" rel="stylesheet" type="text/css">
        <script src="/static/node_modules/editor.md/lib/marked.min.js"></script>
        <script src="/static/node_modules/editor.md/lib/prettify.min.js"></script>
        <script src="/static/node_modules/editor.md/lib/raphael.min.js"></script>
        <script src="/static/node_modules/editor.md/lib/underscore.min.js"></script>
        <script src="/static/node_modules/editor.md/lib/sequence-diagram.min.js"></script>
        <script src="/static/node_modules/editor.md/lib/flowchart.min.js"></script>
        <script src="/static/node_modules/editor.md/lib/jquery.flowchart.min.js"></script>
        <script src="/static/node_modules/editor.md/editormd.js"></script>
        <script type="text/javascript">
            $(function () {
                var testEditormdView2 = editormd.markdownToHTML("article-content", {
                    htmlDecode: "style,script,iframe",  // you can filter tags decode
                    emoji: true,
                    taskList: true,
                    tex: true,  // 默认不解析
                    flowChart: true,  // 默认不解析
                    sequenceDiagram: true,  // 默认不解析
                });
            });
        </script>


    </@layout.put>

    <@layout.put block="content" type="replace">

        <div class="row mt40 mb100">
            <div class="col-lg-10 col-lg-offset-1 col-lg-10 col-lg-offset-1">

                <div class="article-content" id="article-content">
                    ${article.html}
                </div>

                <div class="row mt20">

                    <div class="col-xs-12">
                        <div id="comment-area">
                            <div class="title-area">
                                <span class="total">
                                    欢迎评论
                                </span>
                            </div>
                            <div class="input-area">
                                <nb-comment-panel :comment="floorComment" @success="floorCreateSuccess"/>
                            </div>
                            <div class="title-area">
                                <span class="total">
                                    共{{pager.totalItems}}条评论
                                </span>
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
                                        <a class="reply-btn" href="javascript:void(0)"
                                           @click.stop.prevent="prepareReply(comment)">
                                            <i class="fa fa-comment-o"></i> 回复
                                        </a>

                                        <a class="reply-btn" href="#">
                                            <i class="fa fa-bug"></i> 举报
                                        </a>
                                    </div>

                                <#--评论回复区-->
                                    <div class="reply-area">

                                        <div class="sub-cell-area"
                                             v-for="(subComment,subIndex) in comment.commentPager.data">
                                            <div class="f15">
                                                <span class="black">{{subComment.name}}: </span>
                                                <span>{{subComment.content}} </span>
                                            </div>
                                            <div class="f12">
                                                <span class="mr5">
                                                    {{subComment.createTime | simpleDateHourMinute}}
                                                </span>

                                                <a class="reply-btn" href="javascript:void(0)"
                                                   @click.stop.prevent="prepareReply(subComment)">
                                                    <i class="fa fa-comment-o"></i> 回复
                                                </a>
                                                <a class="reply-btn" href="#">
                                                    <i class="fa fa-bug"></i> 举报
                                                </a>

                                            </div>

                                        </div>

                                        <div v-if="comment.commentPager.totalItems">
                                            <nb-pager :pager="comment.commentPager"
                                                      :callback="comment.refreshCommentPager()"
                                                      :emptyhint="'还没有评论，赶紧来抢沙发吧！'"
                                            />
                                        </div>

                                        <nb-expanding>
                                            <div class="mt10" v-show="replyModel
                                        && ((repliedComment.isFloor && comment.uuid==repliedComment.uuid)
                                        || (!repliedComment.isFloor && comment.uuid==repliedComment.floorUuid)) ">
                                                <nb-comment-panel :comment="replyComment" @success="replyCreateSuccess"/>
                                            </div>
                                        </nb-expanding>

                                    </div>


                                </div>

                            </div>

                            <div class="pager-area">
                                <nb-pager :pager="pager" :callback="refresh" :emptyhint="'还没有评论，赶紧来抢沙发吧！'"/>
                            </div>


                        </div>

                    </div>
                </div>

            </div>
        </div>




    </@layout.put>

</@layout.extends>