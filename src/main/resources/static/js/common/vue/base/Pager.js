function Pager(Clazz, pageSize, page) {
    //继承Base的属性
    Base.call(this, arguments);

    if (!pageSize) {
        pageSize = 12
    }
    if (!page) {
        page = 0
    }

    this.page = page
    this.pageSize = pageSize
    this.totalItems = 0
    this.totalPages = 0
    this.data = []

    //this field means whether add filter query to the URL.
    this.history = null
    //list attributes.
    if (Clazz && (Clazz.prototype instanceof BaseEntity)) {
        this.Clazz = Clazz

        //考虑兼容模式，允许自定义URL_PAGE.
        var urlPage = Clazz.prototype.URL_PAGE
        if (!urlPage) {
            urlPage = Clazz.prototype.getUrlPage()
        }
        if (urlPage) {
            this.URL_PAGE = urlPage
        } else {
            console.error('The Clazz MUST define a prototype named \'URL_PAGE\'')
        }

        if (Clazz.prototype.getFilters) {

            //这个地方的Filter不能用同一个，会出问题的。
            this.FILTERS = Clazz.prototype.getFilters()

        } else {
            console.error('The Clazz MUST define a prototype method named \'getFilters\'')
        }

    } else {
        console.error('You MUST specify a Clazz extended Base')
    }

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
