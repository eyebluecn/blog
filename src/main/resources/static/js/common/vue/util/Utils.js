/*******************************时间处理部分*************************************/
//定义一个转换器
/** * 对Date的扩展，将 Date 转化为指定格式的String * 月(M)、日(d)、12小时(h)、24小时(H)、分(m)、秒(s)、周(E)、季度(q)
 可以用 1-2 个占位符 * 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) * eg: * (new
 Date()).pattern("yyyy-MM-dd hh:mm:ss.S")==> 2006-07-02 08:09:04.423
 * (new Date()).pattern("yyyy-MM-dd E HH:mm:ss") ==> 2009-03-10 二 20:09:04
 * (new Date()).pattern("yyyy-MM-dd EE hh:mm:ss") ==> 2009-03-10 周二 08:09:04
 * (new Date()).pattern("yyyy-MM-dd EEE hh:mm:ss") ==> 2009-03-10 星期二 08:09:04
 * (new Date()).pattern("yyyy-M-d h:m:s.S") ==> 2006-7-2 8:9:4.18
 */
if (!Date.prototype.format) {
    Date.prototype.format = function (fmt) {
        var o = {
            'M+': this.getMonth() + 1, //月份
            'd+': this.getDate(), //日
            'h+': this.getHours() % 12 === 0 ? 12 : this.getHours() % 12, //小时
            'H+': this.getHours(), //小时
            'm+': this.getMinutes(), //分
            's+': this.getSeconds(), //秒
            'q+': Math.floor((this.getMonth() + 3) / 3), //季度
            'S': this.getMilliseconds() //毫秒
        }
        var week = {
            '0': '/u65e5',
            '1': '/u4e00',
            '2': '/u4e8c',
            '3': '/u4e09',
            '4': '/u56db',
            '5': '/u4e94',
            '6': '/u516d'
        }
        if (/(y+)/.test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length))
        }
        if (/(E+)/.test(fmt)) {
            fmt = fmt.replace(RegExp.$1, ((RegExp.$1.length > 1) ? (RegExp.$1.length > 2 ? '/u661f/u671f' : '/u5468') : '') + week[this.getDay() + ''])
        }
        for (var k in o) {
            if (new RegExp('(' + k + ')').test(fmt)) {
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length)))
            }
        }
        return fmt
    }
}

if (!Date.prototype.setISO8601) {
    Date.prototype.setISO8601 = function (string) {
        var regexp = '([0-9]{4})(-([0-9]{2})(-([0-9]{2})' +
            '(T([0-9]{2}):([0-9]{2})(:([0-9]{2})(\.([0-9]+))?)?' +
            '(Z|(([-+])([0-9]{2}):([0-9]{2})))?)?)?)?'
        if (string) {
            var d = string.match(new RegExp(regexp))
            var offset = 0
            var date = new Date(d[1], 0, 1)

            if (d[3]) {
                date.setMonth(d[3] - 1)
            }
            if (d[5]) {
                date.setDate(d[5])
            }
            if (d[7]) {
                date.setHours(d[7])
            }
            if (d[8]) {
                date.setMinutes(d[8])
            }
            if (d[10]) {
                date.setSeconds(d[10])
            }
            if (d[12]) {
                date.setMilliseconds(Number('0.' + d[12]) * 1000)
            }
            if (d[14]) {
                offset = (Number(d[16]) * 60) + Number(d[17])
                offset *= ((d[15] === '-') ? 1 : -1)
            }
            offset -= date.getTimezoneOffset()
            var time = (Number(date) + (offset * 60 * 1000))
            this.setTime(Number(time))
        }
    }
}

//将js的时间对象，转换成yyyy-MM-dd格式的字符串
function simpleDate(d, fallback) {
    if (d instanceof Date) {
        return d.format('yyyy-MM-dd')
    } else if (d === null) {
        return fallback
    } else {
        return 'Invalid Date:' + d
    }
}

//将js的时间对象，转换成MM-dd格式的字符串
function simpleMiniDate(d, fallback) {
    if (d instanceof Date) {
        return d.format('MM-dd')
    } else if (d === null) {
        return fallback
    } else {
        return 'Invalid Date:' + d
    }
}

//将js的时间对象，转换成yyyy-MM-dd HH:mm:ss格式的字符串
function simpleDateTime(d, fallback) {
    if (d instanceof Date) {
        return d.format('yyyy-MM-dd HH:mm:ss')
    } else if (d === null) {
        return fallback
    } else {
        console.error('时间格式错误：' + d)
        return 'Invalid Date:' + d
    }
}

//将js的时间对象，转换成yyyy-MM-dd HH:mm格式的字符串
function simpleDateHourMinute(d, fallback) {
    if (d instanceof Date) {
        return d.format('yyyy-MM-dd HH:mm')
    } else if (d === null) {
        return fallback
    } else {
        console.error('时间格式错误：' + d)
        return 'Invalid Date:' + d
    }
}

//将js的时间对象，转换成HH:mm:ss格式的字符串
function simpleTime(d, fallback) {

    if (d instanceof Date) {
        return d.format('HH:mm:ss')
    } else if (d === null) {
        return fallback
    } else {
        return 'Invalid Date:' + d
    }
}

//将js的时间对象，转换成HH:mm格式的字符串
function simpleMinute(d, fallback) {

    if (d instanceof Date) {
        return d.format('HH:mm')
    } else if (d === null) {
        return fallback
    } else {
        return 'Invalid Date:' + d
    }
}

//将js的时间对象，转换成dd格式的字符串
function simpleDay(d, fallback) {

    if (d instanceof Date) {
        return d.format('dd')
    } else if (d === null) {
        return fallback
    } else {
        return 'Invalid Date:' + d
    }
}

//将js的时间对象，转换成yyyy-MM格式的字符串
function simpleYearAndMonth(d, fallback) {

    if (d instanceof Date) {
        return d.format('yyyy-MM')
    } else if (d === null) {
        return fallback
    } else {
        return 'Invalid Date:' + d
    }
}

//将js的时间对象，转换成人性化的时间。当天：15:34 2017-04-03
function humanTime(d, fallback) {

    var now = new Date()
    if (d instanceof Date) {

        if (now.toDateString() === d.toDateString()) {
            return d.format('HH:mm')
        } else {
            return d.format('yyyy-MM-dd')
        }
    } else if (d === null) {
        return fallback
    } else {
        return 'Invalid Date:' + d
    }
}

//将时间戳转换成易读的格式
function unixTimeStamp2simpleDate(timestamp) {

    if ((typeof timestamp) === 'number') {
        var unixTimestamp = new Date(timestamp)

        return unixTimestamp.format('yyyy-MM-dd')

    } else {

        return 'Invalid timestamp'

    }
}

//将时间戳转换成易读的格式
function unixTimeStamp2simpleDateTime(timestamp) {

    if ((typeof timestamp) === 'number') {

        var unixTimestamp = new Date(timestamp)

        return unixTimestamp.format('yyyy-MM-dd HH:mm')

    } else {

        return 'Invalid timestamp'

    }
}

//将java时间字符串转换成易读的格式
function str2simpleDate(str) {
    if (!str) {
        return 'Invalid time'
    }

    var d = new Date()
    d.setISO8601(str)

    return d.format('yyyy-MM-dd')

}

//将java时间字符串转换成易读的格式
function str2simpleDateTime(str) {

    if (!str) {
        return 'Invalid time'
    }

    var d = new Date()
    d.setISO8601(str)
    return d.format('yyyy-MM-dd HH:mm')

}

//将java时间字符串转换成易读的格式
function str2DateTime(str) {

    if (!str) {
        return 'Invalid time'
    }

    var d = new Date()
    d.setISO8601(str)
    return d.format('yyyy-MM-dd HH:mm:ss')

}

//将java时间字符串转化成js date
function str2Date(str) {
    if (!str) {
        return null
    }

    if (str instanceof Date) {
        return str
    }

    //尝试转换时间戳
    if (typeof str === 'number') {
        return new Date(str)
    }
    //尝试转换yyyy-MM-dd HH:mm:ss 这种格式
    if (str.length === 19) {
        return new Date(Date.parse(str.replace(/-/g, '/')))
    }

    //尝试转换yyyy-MM-dd这种格式
    if (str.length === 10) {
        return new Date(str)
    }

    var d = new Date()
    try {
        d.setISO8601(str)
        return d
    } catch (e) {
        console.error('error date format:' + str)
        return null
    }

}

//将时间戳转换成易读的格式
function str2simpleTime(str) {

    if (!str) {
        return 'Invalid time'
    }
    var d = new Date()
    d.setISO8601(str)
    return d.format('HH:mm')
}

//将时间戳转换成毫秒形式
function str2timeStamp(str) {

    if (!str) {
        return 'Invalid time'
    }
    var d = new Date()
    d.setISO8601(str)
    return d.getTime()
}

//将日期转换成相对于今天的星期几。昨天，明天，今天
function date2Weekday(date) {
    var today = new Date()
    var dateYear = date.getFullYear()
    var dateMonth = date.getMonth()
    var dateDate = date.getDate()
    var todayYear = today.getFullYear()
    var todayMonth = today.getMonth()
    var todayDate = today.getDate()
    if (dateYear === todayYear && dateMonth === todayMonth && Math.abs(dateDate - todayDate) <= 1) {
        if (dateDate === todayDate) {
            return '今天'
        } else if (dateDate === (todayDate + 1)) {
            return '明天'
        } else if (dateDate === (todayDate - 1)) {
            return '昨天'
        }
    } else {
        if (date.getDay() === 0) {
            return '星期日'
        } else if (date.getDay() === 1) {
            return '星期一'
        } else if (date.getDay() === 2) {
            return '星期二'
        } else if (date.getDay() === 3) {
            return '星期三'
        } else if (date.getDay() === 4) {
            return '星期四'
        } else if (date.getDay() === 5) {
            return '星期五'
        } else if (date.getDay() === 6) {
            return '星期六'
        }
    }
}

//返回前一天的日期
function preDay(date) {
    return new Date(date.getTime() - 24 * 60 * 60 * 1000)
}

//返回后一天的日期
function nextDay(date) {
    return new Date(date.getTime() + 24 * 60 * 60 * 1000)
}

//这个是为了兼容pdf预览时pebble的过滤器。
function date(d, format) {

    if (d instanceof Date) {
        return d.format(format)
    } else {
        return ''
    }

}


/*******************************=字符串处理部分*************************************/

function startWith(str, prefix) {
    if (typeof prefix === 'undefined' || prefix === null || prefix === '' || typeof str === 'undefined' || str === null || str.length === 0 || prefix.length > str.length) {
        return false
    }

    return str.substr(0, prefix.length) === prefix
}

function endWith(str, suffix) {
    if (suffix === null || suffix === '' || str === null || str.length === 0 || suffix.length > str.length) {
        return false
    }

    return str.substring(str.length - suffix.length) === suffix
}

//获取文件后缀名
function getExtension(filename) {

    if (filename === null || filename === '') {
        return ''
    }
    var index1 = filename.lastIndexOf('.')
    if (index1 === -1) {
        return ''
    }
    var index2 = filename.length
    return filename.substring(index1, index2)
}

//一个字符串包含子字符串
function containStr(father, child) {

    if (father === null || father === '') {
        return false
    }
    return father.indexOf(child) !== -1
}

//把一个大小转变成方便读的格式
//human readable file size
function humanFileSize(bytes, si) {
    var thresh = si ? 1024 : 1000
    if (Math.abs(bytes) < thresh) {
        return bytes + ' B'
    }
    var units = si
        ? ['KiB', 'MiB', 'GiB', 'TiB', 'PiB', 'EiB', 'ZiB', 'YiB']
        : ['kB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB']
    var u = -1
    do {
        bytes /= thresh
        ++u
    } while (Math.abs(bytes) >= thresh && u < units.length - 1)
    return bytes.toFixed(1) + ' ' + units[u]
}

//把数字转换成中文大写金额
function numberCapital(num) {
    var strOutput = ''
    var strUnit = '仟佰拾亿仟佰拾万仟佰拾元角分'
    num += '00'
    var intPos = num.indexOf('.')
    if (intPos >= 0) {

        num = num.substring(0, intPos) + num.substr(intPos + 1, 2)
    }
    strUnit = strUnit.substr(strUnit.length - num.length)
    for (var i = 0; i < num.length; i++) {

        strOutput += '零壹贰叁肆伍陆柒捌玖'.substr(num.substr(i, 1), 1) + strUnit.substr(i, 1)
    }
    return strOutput.replace(/零角零分$/, '整').replace(/零[仟佰拾]/g, '零').replace(/零{2,}/g, '零').replace(/零([亿|万])/g, '$1').replace(/零+元/, '元').replace(/亿零{0,3}万/, '亿').replace(/^元/, '零元')
}

//转换成首字母小写的驼峰法
function lowerCamel(str) {

    if (!str) {
        console.error('不能转换空的驼峰字符串。')
        return str
    }

    return str.replace(/(?:^\w|[A-Z]|\b\w)/g, function (letter, index) {
        return index === 0 ? letter.toLowerCase() : letter.toUpperCase()
    }).replace(/\s+/g, '')
}

//转换成全部小写的使用 /分隔的字符串
function lowerSlash(str) {
    return str.replace(/(?:^\w|[A-Z]|\b\w)/g, function (letter, index) {
        return '/' + letter.toLowerCase()
    }).replace(/\s+/g, '')
}

/*
 名词变复数归纳总结
 1.一般情况下，在名词后加“s”或“es”.
 2.以s,sh,ch,x结尾的名字，在名词后直接加“es”.
 3.以o结尾的名字，有两种情况：
 1）有生命的名词，在名词后加“es”.
 如：tomato-tomatoes potato-potatoes
 2)无生命的名字，在名字后加“s”.
 如：photo-photos radio-radios
 注意：使用java一律采用加“s”的策略
 4.以辅音字母+y结尾的名词,将y改变为i,再加-es.
 元音字母+y结尾的名词则直接加s
 */
function toPlural(singular) {

    if (!singular) {
        console.error('不能转换空字符为复数形式。')
        return singular
    }
    var length = singular.length
    //一个字母的直接加个s.
    if (length === 1) {
        return singular + 's'
    }

    var lastChar = singular[length - 1]
    var lastSecondChar = singular[length - 2]
    if (lastChar === 's' || lastChar === 'x' || (lastChar === 'h' && (lastSecondChar === 's' || lastSecondChar === 'c'))) {
        return singular + 'es'
    } else if (lastChar === 'y' && (lastSecondChar !== 'a' && lastSecondChar !== 'e' && lastSecondChar !== 'i' && lastSecondChar !== 'o' && lastSecondChar !== 'u')) {
        return singular.substring(0, length - 1) + 'ies'
    } else {
        return singular + 's'
    }

}


/*******************************=Util处理部分*************************************/





//check whether an obj is number.
function isInteger(obj) {
    return typeof obj === 'number' && obj % 1 === 0
}

function isAndroid() {
    var u = navigator.memberAgent;


    //android终端
    return u.indexOf('Android') > -1 || u.indexOf('Adr') > -1;

}

function isIOS() {
    var u = navigator.memberAgent;

    //ios终端
    return !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/);
}


//check whether an obj is empty
function isEmptyObject(obj) {

    for (var key in obj) {
        return false;
    }
    return true
}


//两个id是否相等
function isIdEqual(id1, id2) {

    return (id1 + "") === (id2 + "");


}

function isLocalStorageNameSupported() {
    var testKey = 'test';
    var storage = window.localStorage;
    try {
        storage.setItem(testKey, '1');
        storage.removeItem(testKey);
        return true;
    } catch (error) {
        return false;
    }
}


function readLocalStorage(key) {
    if (isLocalStorageNameSupported()) {
        return window.localStorage[key];
    } else {
        console.error("not support localStorage.");
        return null;
    }
}

function saveToLocalStorage(key, content) {
    if (isLocalStorageNameSupported()) {
        window.localStorage[key] = content;
    } else {
        console.error("not support localStorage.");
    }
}

function removeLocalStorage(key) {
    if (isLocalStorageNameSupported()) {
        window.localStorage.removeItem(key);
    } else {
        console.error("not support localStorage.");
    }
}


//注册到vue过滤器中
Vue.filter('simpleDateHourMinute', simpleDateHourMinute)