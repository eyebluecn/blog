$(function () {

    var vm = new Vue({
        el: '#comment-area',
        data: {
            message: "Hello World",
            comment: new Comment()
        },
        mounted: function () {
            console.log("comment-area mounted")
            var that = this

            this.comment.httpGet("/api/comment/detail/0b39bd0e-d743-42e5-bdd0-5ec28de0b462", {}, function (response) {
                console.log("成功了！");
                that.comment.render(response.body.data)
                console.log(that.comment)

            }, function (response) {
                console.log(response)
                console.log("失败了")
            })

        }
    });

});