<#-- 分页 -->
<#macro NbCommentPanel>

<script type="text/x-template" id="nb-comment-panel">
    <div class="nb-comment-panel-area">
        <div class="row">
            <label class="col-sm-2 control-label">昵称</label>
            <div class="col-sm-10">
                <input type="text" v-model="comment.name" :disabled="!(!userUsername)" placeholder="输入您的称呼" class="form-control">
            </div>
        </div>
        <div class="row mt10">
            <label class="col-sm-2 control-label">联系方式</label>
            <div class="col-sm-10">
                <input type="text" v-model="comment.email" :disabled="!(!userEmail)" placeholder="输入您的邮箱或手机号，不公开" class="form-control">
            </div>
        </div>
        <div class="row mt10">
            <label class="col-sm-2 control-label">内容</label>
            <div class="col-sm-10">
                <textarea rows="6" v-model="comment.content" placeholder="输入您的评论" class="form-control"></textarea>
            </div>
        </div>

        <div class="row mt10">
            <div class="col-xs-12 text-right">
                <button class="btn btn-primary" @click.prevent.stop="submit" :disabled="comment.loading">
                    <i class="fa fa-send" v-show="!comment.loading"></i>
                    <i class="fa fa-spinner fa-spin" v-show="comment.loading"></i>
                    {{comment.loading?"正在提交...":"提交"}}
                </button>
            </div>
        </div>
        <div class="mt10" v-if="comment.errorMessage">
            <div class="alert alert-danger">
                {{comment.errorMessage}}
            </div>
        </div>

    </div>
</script>

<script type="text/javascript">

    // 注册
    Vue.component('nb-comment-panel', {
        template: '#nb-comment-panel',
        data: function () {
            return {
                name: "你好啊！"
            }
        },
        props: {
            comment: {
                type: Comment,
                required: true
            },
            userUsername: {
                type: String,
                required: false
            },
            userEmail: {
                type: String,
                required: false
            }
        },
        computed: {},
        watch: {},
        methods: {
            submit: function () {
                var that = this
                this.comment.httpCreate(function (response) {

                    that.$emit("success")

                }, function (response) {
                    console.error("知道失败了！")
                    that.$emit("failure")
                })
            }
        },
        created:function () {

        },
        mounted: function () {

            if (this.userUsername && this.userEmail) {
                this.comment.name = this.userUsername
                this.comment.email = this.userEmail
            } else {
                //从本地加载持久化的姓名。
                this.comment.loadNameAndEmail()
            }


        }
    })

</script>

</#macro>