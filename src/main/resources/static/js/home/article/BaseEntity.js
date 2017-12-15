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
