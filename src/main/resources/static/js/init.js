/**
 * 这个js是用来初始化全局配置的。
 *
 */

//判断是否是触屏设备。https://stackoverflow.com/questions/4817029/whats-the-best-way-to-detect-a-touch-screen-device-using-javascript
function is_touch_device() {
    return 'ontouchstart' in window        // works on most browsers
        || navigator.maxTouchPoints;       // works on IE10/11 and Surface
}

$(function () {

    //配置全局toast
    toastr.options = {
        "closeButton": true,
        "debug": false,
        "progressBar": false,
        "preventDuplicates": false,
        "positionClass": "toast-top-center",
        "onclick": null,
        "showDuration": "400",
        "hideDuration": "1000",
        "timeOut": "2000",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    };

    //快速给一个toast.
    var toastrs = $("[data-toastr]");
    toastrs.click(function () {
        var $this = $(this);
        toastr.info($this.data("toastr"));
    });

    //跳转到某个链接
    var $hrefs = $("[data-href]");
    $hrefs.click(function () {
        var $this = $(this);
        location.href = $this.data("href");
    });

    //在新标签中打开一个链接
    var $hrefBlanks = $("[data-href-blank]");
    $hrefBlanks.click(function () {
        var $this = $(this);
        window.open($this.data("href-blank"));
    });

	//打开邮箱
	var $hrefMail = $("[data-href-mail]");
	$hrefMail.click(function () {
		var $this = $(this);
		window.location.href = $this.data("href-mail");
	});


    //点击TOP回到页面的顶端
    var $gotoTops = $("[data-top]");
    $gotoTops.click(function () {
        $('html, body').animate({
            scrollTop: $(".nb-app").offset().top
        }, 500);
    });


    //桌面电脑，右上角悬浮出现菜单，触摸屏点击出现菜单。
    if (!is_touch_device()) {
        var $avatarTool = $(".avatar-tool");
        var inside = false;
        $avatarTool.mouseenter(function () {

            inside = true;
            $avatarTool.addClass("open");

        });
        $avatarTool.mouseleave(function () {
            inside = false;
            setTimeout(function () {
                if (!inside) {
                    $avatarTool.removeClass("open");
                }
            }, 200);
        });
    }



    // Smooth Hash Link Scroll
    $('.smooth-scroll').click(function () {
        var target = $(this.hash);
        target = target.length ? target : $('[name=' + this.hash.slice(1) + ']');
        if (target.length) {
            $('html,body').animate({
                scrollTop: target.offset().top - 65
            }, 500);
            return false;
        }
    });




});