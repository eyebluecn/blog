$(function () {


    var vm = new Vue({
        el: '#interactive-area',
        data: {
            pager: new Pager(Comment, 12),
            //作为顶级评论
            floorComment: new Comment(),
            //被回复的那条评论
            repliedComment: new Comment(),
            //准备的评论
            replyComment: new Comment(),
            //是否是评论模式
            replyModel: false,
            //被举报的评论
            reportComment: new Comment(),
            //是否是举报模式
            reportModel: false,

            articleUuid: null,
            articleUserUuid: null,
            //文章原本的点赞数
            articleAgree: 0,
            //当前用户对于文章的点赞情况
            articleAgreed: false,
            //当前登录的用户
            userUuid: null,
            //用户昵称
            userUsername: null,
            //用户邮箱
            userEmail: null
        },
        methods: {
            refresh: function () {
                var that = this

                that.pager.setFilterValue("orderSort", "DESC")
                that.pager.setFilterValue("articleUuid", that.articleUuid)
                that.pager.setFilterValue("isFloor", true)
                that.pager.setFilterValue("needSubPager", true)

                //开始获取pager中的内容。
                that.pager.httpFastPage();

            },
            agree: function (comment) {
                comment.httpAgree();
            },
            cancelAgree: function (comment) {
                comment.httpCancelAgree();
            },
            floorCreateSuccess: function () {
                var that = this
                this.refresh();

                toastr.info("评论成功！");

                //清空里面的老数据。
                that.floorComment.content = null
            },

            //准备回复
            prepareReply: function (comment) {
                var that = this


                if (that.replyModel && comment.uuid === that.repliedComment.uuid) {
                    that.replyModel = false
                    return
                }

                that.repliedComment.render(comment)
                that.replyModel = true
                that.reportModel = false

                that.replyComment.articleUuid = that.articleUuid
                that.replyComment.isFloor = false
                if (that.repliedComment.isFloor) {
                    that.replyComment.floorUuid = that.repliedComment.uuid
                } else {
                    that.replyComment.floorUuid = that.repliedComment.floorUuid
                }
                that.replyComment.puuid = that.repliedComment.uuid
                that.replyComment.content = "@" + that.repliedComment.name + " "

            },
            replyCreateSuccess: function () {

                var that = this

                that.refresh()
                //that.repliedComment.refreshCommentPager()();

                toastr.info("回复成功！");

                //清空里面的老数据。
                that.replyComment.content = null
                that.replyModel = false
            },

            //准备举报
            prepareReport: function (comment) {
                var that = this

                if (that.reportModel && comment.uuid === that.repliedComment.uuid) {
                    that.reportModel = false
                    return
                }

                that.repliedComment.render(comment)
                that.replyModel = false
                that.reportModel = true


                that.reportComment.uuid = comment.uuid
                that.reportComment.content = comment.content


            },
            reportSuccess: function () {

                var that = this

                that.reportModel = false

                toastr.info("举报成功，我们将尽快处理，感谢您的举报！");

                that.reportComment.uuid = null
                that.reportComment.content = null

            },
            //赞文章
            agreeArticle: function () {
                var that = this
                var base = new Base()
                base.httpPost("/api/article/agree", {"articleUuid": that.articleUuid}, function (response) {

                    that.articleAgree++
                    that.articleAgreed = true


                    that.$refs.articleAgreeBtn.blur();

                });
            },
            articleCancelAgree: function () {
                var that = this
                var base = new Base()
                base.httpPost("/api/article/cancel/agree", {"articleUuid": that.articleUuid}, function (response) {

                    that.articleAgree--
                    that.articleAgreed = false

                    that.$refs.articleCancelAgreeBtn.blur();

                });
            }

        },

        created: function () {

            var that = this

            var arr = window.location.pathname.split("/")
            that.articleUuid = arr[arr.length - 1]


            that.floorComment.articleUuid = that.articleUuid
            that.floorComment.isFloor = true

            var $articleAppendix = $(".article-appendix");
            that.articleUserUuid = $articleAppendix.data("articleuseruuid");
            that.userUuid = $articleAppendix.data("useruuid");
            that.userUsername = $articleAppendix.data("userusername");
            that.userEmail = $articleAppendix.data("useremail");
            that.articleAgree = parseInt($articleAppendix.data("agree"));
            that.articleAgreed = $articleAppendix.data("agreed") === "true" || $articleAppendix.data("agreed") === true;


        },
        mounted: function () {

            this.refresh();
        }
    });

});