//定义Base实体。
function Base() {

    //local fields. Used in UI.
    this.errorMessage = null;
    this.editMode = false;
    this.loading = false;

    //加载详情时的loading，这是一种特殊的loading状态，只有详情加载好了，我们才展示整个页面，在LoadingFrame中有用到
    this.detailLoading = false;

}

Base.prototype.render = function (obj) {
    if (obj) {
        $.extend(this, obj)
    }
};

/**
 *
 * @param field 字段名
 * @param Clazz 类型名
 */
Base.prototype.renderList = function (field, Clazz) {

    var beans = this[field];
    if (!beans) {
        //维持默认值
        this[field] = (new this.constructor())[field]
        return
    }

    if (!Clazz) {
        return
    }

    this[field] = []

    for (var i = 0; i < beans.length; i++) {
        var bean = beans[i]
        var clazz = new Clazz()

        clazz.render(bean)

        this[field].push(clazz)
    }
}

//直接render出一个Entity. field字段名，Clazz类名。
Base.prototype.renderEntity = function (field, Clazz) {

    var obj = this[field]
    if (!obj) {
        if (Clazz) {
            var EntityClazz = this.constructor
            obj = (new EntityClazz())[field]

        } else {
            return
        }
    }

    if (Clazz === Date) {

        this[field] = str2Date(obj)
    } else if (Clazz.prototype instanceof Base) {

        //可能此处的该项属性做了特殊处理的。
        //1024*1024 以及 "图片尺寸不超过1M"用var bean = new Clazz(); 就无法反映出来。因为父类render的时候已经将avatar给变成了Object.
        var bean = (new this.constructor())[field]
        if (!bean) {
            bean = new Clazz()
        }

        if (obj !== null) {
            bean.render(obj)
            this[field] = bean
        }

    } else {
        console.error('调用错误！')
    }

}

//we provide a default error handing method. handle with specific errorCallback.
Base.prototype.defaultErrorHandler = function (response, errorCallback) {

    var msg = this.getErrorMessage(response)

    if (typeof errorCallback === 'function') {
        errorCallback(response)
    } else {
        toastr.error(msg);
    }
}

//get errorMessage from response and wrap the value to this.errorMessage.
Base.prototype.getErrorMessage = function (response) {

    var msg = '服务器出错，请稍后再试!'

    if (response === null) {
        msg = '出错啦，请稍后重试！'
    } else if (typeof response === 'string') {
        msg = response
    } else if (response['msg']) {
        msg = response['msg']
    } else if (response['message']) {
        msg = response['message']
    } else {
        var temp = response['data']
        if (temp !== null && typeof temp === 'object') {
            if (temp['message']) {
                msg = temp['message']
            } else if (temp['msg']) {
                msg = temp['msg']
            } else {
                if (temp['error'] && temp['error']['message']) {
                    msg = temp['error']['message']
                }
            }
        }
    }
    this.errorMessage = msg
    return msg
}

//Vue.http.get('/someUrl', [options]).then(successCallback, errorCallback);
//opts中可以传递一些特殊的选项。具体参考：https://github.com/pagekit/vue-resource/blob/develop/docs/http.md
Base.prototype.httpGet = function (url, params, successCallback, errorCallback, opts) {

    if (!opts) {
        opts = {}
    }

    var that = this
    var fullUrl = url

    var options = $.extend({}, opts)
    options['params'] = params

    this.loading = true
    Vue.http.get(fullUrl, options).then(function (response) {

        that.loading = false;
        (typeof successCallback === 'function') && successCallback(response)

    }, function (response) {

        that.loading = false

        console.error(response)

        //有传入错误处理方法，就按你的执行
        if (typeof errorCallback === 'function') {
            errorCallback(response)
        } else {
            //没有传入错误处理的方法就采用默认处理方法：toast弹出该错误信息。
            that.defaultErrorHandler(response)
        }

    })

}

//Vue.http.post('/someUrl', [body], [options]).then(successCallback, errorCallback);
//url is something like this: /article/detail/1
//opts中可以传递一些特殊的选项。具体参考：https://github.com/pagekit/vue-resource/blob/develop/docs/http.md
Base.prototype.httpPost = function (url, params, successCallback, errorCallback, opts) {
    var that = this

    if (!opts) {
        opts = {}
    }

    var fullUrl = url
    var options = $.extend({}, opts)

    //options["emulateJSON"] = !(params instanceof FormData);

    //Post请求临时使用json的方式。
    options['emulateJSON'] = true

    this.loading = true
    Vue.http.post(fullUrl, params, options).then(function (response) {
        that.loading = false

        typeof successCallback === 'function' && successCallback(response)

    }, function (response) {
        that.loading = false

        console.error(response)
        //错误信息一律存放在自己的errorMessage中，user httpLogout将显得不灵活了
        //that.errorMessage = that.getErrorMessage(response)

        //有传入错误处理方法，就按你的执行
        if (typeof errorCallback === 'function') {
            errorCallback(response)
        } else {
            //没有传入错误处理的方法就采用默认处理方法：toast弹出该错误信息。
            that.defaultErrorHandler(response)
        }

    })
}

//获取一个function的名字。
Base.prototype.functionName = function (func) {
    // Match:
    // - ^          the beginning of the string
    // - function   the word 'function'
    // - \s+        at least some white space
    // - ([\w\$]+)  capture one or more valid JavaScript identifier characters
    // - \s*        optionally followed by white space (in theory there won't be any here,
    //              so if performance is an issue this can be omitted[1]
    // - \(         followed by an opening brace
    //
    var result = /^function\s+([\w\$]+)\s*\(/.exec(func.toString())

    return result ? result[1] : '' // for an anonymous function there won't be a match
}

//获取到当前类的单数标签。比如 Project便得到 project
Base.prototype.getTAG = function () {

    var className = this.constructor.name

    if (!className) {
        className = this.functionName(this.constructor)
    }

    return lowerCamel(className)
}

//获取到当前类的复数标签。比如 Project便得到 projects
Base.prototype.getTAGS = function () {

    return toPlural(this.getTAG())
}

//获取到当前实体的url前缀。
Base.prototype.getUrlPrefix = function () {

    return '/api' + lowerSlash(this.getTAG())
}
