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
    //评论者头像
    this.avatarUrl = null;
    //评论者邮箱
    this.email = null;
    //评论内容
    this.content = null;


    //评论时的ip
    this.ip = null;

    //用户对于这个评论是否已经点赞了。
    this.agreed = false;

    //子评论
    this.commentPager = new Pager(Comment, 10)
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
        that.commentPager.setFilterValue("orderSort", "DESC")
        that.commentPager.setFilterValue("articleUuid", that.articleUuid)
        that.commentPager.setFilterValue("floorUuid", that.uuid)
        that.commentPager.setFilterValue("isFloor", false)
        that.commentPager.setFilterValue("needSubPager", false)
        that.commentPager.httpFastPage()
    }
}

//创建评论
Comment.prototype.httpCreate = function (successCallback, errorCallback) {
    var that = this;

    //参数验证。
    if (!that.articleUuid) {
        that.errorMessage = "articleUuid必填";
        return
    }
    if (!that.isFloor) {
        if (!that.floorUuid) {
            that.errorMessage = "floorUuid必填";
            return
        }
    }
    if (!that.name) {
        that.errorMessage = "请填上您的昵称";
        return
    }
    if (!that.email) {
        that.errorMessage = "请填上您的邮箱";
        return
    }
    if (!that.content) {
        that.errorMessage = "请填上您的评论内容";
        return
    }

    //向本地进行持久化。
    this.saveNameAndEmail();

    var form = {
        articleUuid: that.articleUuid,
        isFloor: that.isFloor,
        floorUuid: that.floorUuid,
        puuid: that.puuid,
        name: that.name,
        email: that.email,
        content: that.content
    }

    that.httpPost("/api/comment/create", form, function (response) {
        that.render(response.data.data);

        if (typeof successCallback === "function") {
            successCallback(response);
        }

    }, function (response) {

        if (typeof errorCallback === "function") {
            errorCallback(response);
        }

    });


}

//点赞。
Comment.prototype.httpAgree = function (successCallback, errorCallback) {

    var that = this
    that.httpPost("/api/comment/agree", {"commentUuid": that.uuid}, function (response) {

        that.agree++
        that.agreed = true

        if (typeof successCallback === "function") {
            successCallback(response);
        }

    }, function (response) {

        if (typeof errorCallback === "function") {
            errorCallback(response);
        }

    });
}

//取消点赞。
Comment.prototype.httpCancelAgree = function (successCallback, errorCallback) {

    var that = this
    that.httpPost("/api/comment/cancel/agree", {"commentUuid": that.uuid}, function (response) {

        that.agree--
        that.agreed = false

        if (typeof successCallback === "function") {
            successCallback(response);
        }

    }, function (response) {

        if (typeof errorCallback === "function") {
            errorCallback(response);
        }

    });
}


//点赞。
Comment.prototype.httpReport = function (content, successCallback, errorCallback) {

    if (!content) {
        this.errorMessage = "请输入举报内容";
        if (typeof errorCallback === "function") {
            errorCallback("请输入举报内容");
        }
        return
    }
    this.errorMessage = null;

    var that = this
    that.httpPost("/api/comment/report", {
        "commentUuid": that.uuid,
        "content": content
    }, successCallback, errorCallback);

}


//从本地加载昵称和邮箱。
Comment.prototype.loadNameAndEmail = function () {
    this.name = readLocalStorage("CommentName")
    this.email = readLocalStorage("CommentEmail")
}

//向本地保存Name和Email。
Comment.prototype.saveNameAndEmail = function () {
    saveToLocalStorage("CommentName", this.name)
    saveToLocalStorage("CommentEmail", this.email)

}