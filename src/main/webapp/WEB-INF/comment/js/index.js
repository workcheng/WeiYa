var pageConfigWidth = 750;
var pageMaxWidth = 800;
var _w, _zoom, _hd, _orientationChange, _doc = document, __style = _doc.getElementById("_zoom");
__style || (_hd = _doc.getElementsByTagName("head")[0], __style = _doc.createElement("style"), _hd.appendCHild(_style)), _orientationChange = function () {
    _w = _doc.documentElement.clientWidth || _doc.body.clientWidth, _w = _w > pageMaxWidth ? pageMaxWidth : _w, _zoom = _w / pageConfigWidth, __style.innerHTML = ".zoom {zoom:" + _zoom + ";-webkit-text-size-adjust:auto!important;}"
}, _orientationChange(), window.addEventListener("resize", _orientationChange, !1);
loadedPercent(20);
Date.prototype.Format = function (a) {
    var c = {
        "M+": this.getMonth() + 1,
        "d+": this.getDate(),
        "H+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
        "q+": Math.floor((this.getMonth() + 3) / 3),
        S: this.getMilliseconds()
    };
    if (/(y+)/.test(a)) {
        a = a.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length))
    }
    for (var b in c) {
        if (new RegExp("(" + b + ")").test(a)) {
            a = a.replace(RegExp.$1, (RegExp.$1.length == 1) ? (c[b]) : (("00" + c[b]).substr(("" + c[b]).length)))
        }
    }
    return a
};
function loadedPercent(a) {
    if (parseInt(document.getElementById("J_precent").innerHTML) < a) {
        document.getElementById("J_precent").innerHTML = a + "%"
    }
    if (a == 100) {
        document.getElementById("J_loading").style.display = "none"
    }
}
function finishLoading() {
    $("#t_loading").hide();
    document.body.scrollTop = 0;
    setTimeout(function () {
        $("#container [isusing=yes]").css("opacity", "0").show().animate({opacity: 1}, 30, "ease-in-out")
    }, 0);
    loadedPercent(100)
}
$(function () {
    finishLoading()
});
function showPage() {
    $("#loading").hide();
    $(".cover").hide()
};