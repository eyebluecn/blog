$(function () {

    var vm = new Vue({
        el: '#comment-area',
        data: {
            message: "Hello World1",
            pager: new Pager(Comment, 12),
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

            }
        },

        mounted: function () {
            console.log("comment-area mounted")

            var that = this

            var arr = window.location.pathname.split("/")
            that.articleUuid = arr[arr.length - 1]

            that.refresh();


        }
    });

});