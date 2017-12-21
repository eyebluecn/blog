<#-- 分页 -->
<#macro NbReportPanel>

<script type="text/x-template" id="nb-report-panel">
    <div class="nb-report-panel-area">

        <div class="italic mt10">
            举报"{{comment.content}}"
        </div>

        <div class="row mt10">
            <label class="col-sm-2 control-label pt5">举报原因</label>
            <div class="col-sm-10">
                <select class="form-control" v-model="selectContent">
                    <option value="垃圾广告">垃圾广告</option>
                    <option value="违法违禁">违法违禁</option>
                    <option value="色情低俗">色情低俗</option>
                    <option value="人生攻击">人生攻击</option>
                    <option value="侵犯隐私">侵犯隐私</option>
                    <option value="其他">其他</option>
                </select>
                <div class="mt10" v-if="selectContent=='其他'">
                    <textarea rows="3" v-model="content" placeholder="输入您的举报原因" class="form-control"></textarea>
                </div>
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
                selectContent: "垃圾广告",
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

                if (that.selectContent !== '其他') {
                    that.content = that.selectContent
                }
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