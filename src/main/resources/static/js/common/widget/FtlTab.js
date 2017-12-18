/*
 html结构：

 <div class="nb-tabs yellow-theme">
 <div class="type-bar">
 <span>
 <span class="type active">1</span>
 <span class="type">2</span>
 <span class="type">3</span>
 </span>
 <span class="more">

 </span>
 </div>
 <div class="content-block">
 <div class="nb-tab-panel active">1</div>
 <div class="nb-tab-panel">2</div>
 <div class="nb-tab-panel">3</div>
 </div>
 </div>
 */


$(function () {

    var $nbTabs = $(".nb-tabs");
    var $types = $nbTabs.find(".type-bar").find(".type");

    $types.hover(function () {
        var $this = $(this);
        var index = $(this).index();

        var $typeBar = $this.closest(".type-bar");
        var $parentFtlTab = $typeBar.closest(".nb-tabs");

        var $childTypes = $typeBar.find(".type");
        var $nbTabPanels = $parentFtlTab.find(".nb-tab-panel");

        $childTypes.removeClass("active");
        $nbTabPanels.removeClass("active");
        $(this).addClass("active");
        $nbTabPanels.eq(index).addClass("active");
    });

});