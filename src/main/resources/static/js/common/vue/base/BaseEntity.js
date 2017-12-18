//定义Base实体。
function BaseEntity() {
    //继承Base的属性
    Base.call(this, arguments);

    this.uuid = null
    this.sort = null
    this.createTime = null
    this.modifyTime = null
    this.deleted = false

}

//继承Base的方法
BaseEntity.prototype = new Base();
BaseEntity.prototype.constructor = BaseEntity;

BaseEntity.prototype.render = function (obj) {
    if (obj) {

        Base.prototype.render.call(this, obj);
        this.createTime = str2Date(obj.createTime)
        this.modifyTime = str2Date(obj.modifyTime)
    }
};
//获取过滤器，必须每次动态生成，否则会造成filter逻辑混乱。
BaseEntity.prototype.getFilters = function () {
    return [
        new Filter('SORT', 'ID', 'orderId')
    ]
};

Base.prototype.getUrlCreate = function () {
    var prefix = this.getUrlPrefix()

    return prefix + '/create'
}

Base.prototype.getUrlDel = function (uuid) {
    var prefix = this.getUrlPrefix()

    if (!uuid) {
        return prefix + '/del/{uuid}'
    } else {
        return prefix + '/del/' + uuid
    }

}

Base.prototype.getUrlEdit = function () {
    var prefix = this.getUrlPrefix()

    return prefix + '/edit'
}

Base.prototype.getUrlDetail = function (uuid) {
    var prefix = this.getUrlPrefix()

    if (!uuid) {
        return prefix + '/detail/{uuid}'
    } else {
        return prefix + '/detail/' + uuid
    }

}

Base.prototype.getUrlPage = function () {
    var prefix = this.getUrlPrefix()

    return prefix + '/page'
}

Base.prototype.getUrlSort = function () {
    var prefix = this.getUrlPrefix()

    return prefix + '/sort'
}