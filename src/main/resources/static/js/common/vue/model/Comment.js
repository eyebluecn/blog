//定义Comment实体。
function Comment() {

    //继承Base的属性
    BaseEntity.call(this, arguments);

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

    //子评论
    this.commentPager = new Pager(Comment, 8)
}

//继承Base的方法
Comment.prototype = new BaseEntity();
Comment.prototype.constructor = Comment;


Comment.prototype.render = function (obj) {
    if (obj) {
        BaseEntity.prototype.render.call(this, obj);

        this.renderEntity("commentPager", Pager)

    }
};

//获取过滤器，必须每次动态生成，否则会造成filter逻辑混乱。
Comment.prototype.getFilters = function () {
    return [
        new Filter('SORT', '按时间', 'orderSort'),
        new Filter('INPUT', '用户Uuid', 'userUuid'),
        new Filter('INPUT', '文章主体', 'articleUuid'),
        new Filter('CHECK', '是否为楼层', 'isFloor'),
        new Filter('INPUT', '楼层', 'floorUuid'),
        new Filter('INPUT', '上一级', 'puuid'),
        new Filter('INPUT', '用户名字', 'name'),
        new Filter('INPUT', '邮箱', 'email'),
        new Filter('INPUT', '内容', 'content'),
        new Filter('CHECK', '是否被举报', 'isReport'),
        new Filter('INPUT', '举报内容', 'report'),
        new Filter('CHECK', '是否需要子评论', 'needSubPager')
    ]
};

Comment.prototype.refreshCommentPager = function () {
    var that = this;
    return function () {
        that.commentPager.httpFastPage()
    }
}