$(function () {
    var $pageSizeSelects = $(".pageSizeSelect");
    $pageSizeSelects.on('change', function (e) {
        var optionSelected = $("option:selected", this);
        location.href = this.value;
    });

});