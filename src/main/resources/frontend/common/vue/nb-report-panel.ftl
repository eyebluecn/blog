<#-- 分页 -->
<#macro NbReportPanel>

<script type="text/x-template" id="nb-report-panel">
    <div class="nb-report-panel-area">

        <div class="italic mt10">
            举报"{{comment.content}}"
        </div>

        <div class="row mt10">
            <label class="col-sm-2 control-label">举报原因</label>
            <div class="col-sm-10">
                <textarea rows="6" v-model="content" placeholder="输入您的举报原因" class="form-control"></textarea>
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
    Vue.component('nb-report-panel', {
        template: '#nb-report-panel',
        data: function () {
            return {
                content: null
            }
        },
        props: {
            comment: {
                type: Comment,
                required: true
            }
        },
        computed: {},
        watch: {},
        methods: {
            submit: function () {
                var that = this
                this.comment.httpReport(that.content, function (response) {

                    that.$emit("success")

                })
            }
        },
        mounted: function () {
            this.content = null
        }
    })

</script>

</#macro>