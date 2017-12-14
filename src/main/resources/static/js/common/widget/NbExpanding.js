//用法：需要展开或收起的div指定  data-expanding-id="activitySign" data-expanding-show="false"
//触发的开关：data-expanding-target="activitySign"
//id决定了哪个开关触发哪个dom.

$(function () {


    function expand($dom) {

        if (!$dom) {
            console.error("请指定jQuery对象");
            return;
        }
        var expandingShow = $dom.data("expanding-show");

        if (expandingShow === true || expandingShow === "true") {
            $dom.data("expanding-show", "false");
            $dom.velocity("slideUp", {duration: 377});
        } else {
            $dom.data("expanding-show", "true");
            $dom.velocity("slideDown", {duration: 377});
        }

    }



    //为触发开关绑定点击事件。
    var $expandingTriggers = $("[data-expanding-target]");
    $expandingTriggers.click(function () {

        var $this = $(this);
        var target = $this.data("expanding-target");
        if (target) {
            var $expandings = $("[data-expanding-id='" + target + "']");
            expand($expandings);
        }
    });


});