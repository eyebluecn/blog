$(function () {


    var vm = new Vue({
        el: '#comment-area',
        data: {
            pager: new Pager(Comment, 12),
            //作为顶级评论
            floorComment: new Comment(),
            //被回复的那条评论
            repliedComment: new Comment(),
            //准备的评论
            replyComment: new Comment(),
            replyModel: false,
            articleUuid: null
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

                console.log("难道没来！")
                var that = this

                that.refresh()
                //that.repliedComment.refreshCommentPager()();

                toastr.info("回复成功！");

                //清空里面的老数据。
                that.replyComment.content = null
                that.replyModel = false
            }

        },

        mounted: function () {
            console.log("comment-area mounted")

            var that = this

            var arr = window.location.pathname.split("/")
            that.articleUuid = arr[arr.length - 1]
            that.refresh();

            that.floorComment.articleUuid = that.articleUuid
            that.floorComment.isFloor = true

        }
    });

});