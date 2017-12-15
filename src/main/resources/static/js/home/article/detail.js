//定义Comment实体。
function Comment() {
    //文章uui
    this.articleUuid = null;
    //创建者
    this.userUuid = null;
    //是否是楼层评论
    this.isFloor = false;
    //如果不是楼层评论，那么应该附着的楼层uuid
    this.floorUuid = null;
    //回复的uuid
    this.puuid = null;
    //评论者姓名
    this.name = null;
    //评论者邮箱
    this.email = null;
    //评论内容
    this.content = null;
    //是否被举报
    this.isReport = false;
    //举报内容
    this.report = null;
}

$(function () {

    var vm = new Vue({
        el: '#comment-area',
        data: {
            message: "Hello World"
        }
    });

});