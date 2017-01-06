var alldataarr = [];
var setintIndex = 0;
function getRandom(min, max) {
    var r = Math.random() * (max - min);
    var re = Math.round(r + min);
    re = Math.max(Math.min(re, max), min)

    return re;
}
//获取用户
var getLottery = function () {
    //出现正在获取最新签到成员，请稍候...
    var getUserList = BaseUrl + "user/userList";
    $.ajax({
        async: true,
        url: getUserList,
        success: function (data) {
            alldataarr = data.data;
            //$.each(alldataarr, function (index, item) {
            //    var str = '<li id="'+item.openId+'" data-headpath="' + item.headImgUrl + '" data-userid="' + item.openId + '"><img src="' + item.headImgUrl + '"><br><span>' + item.name + '</span></li>';
            //    luckUl = $("#luck_user ul");
            //    luckUl.append(str);
            //})
            //luckUserList();

            //停止


        }
    })
}


var isAuto = function () {
    //换人var alldataarr=[];

    setintIndex = setInterval(function () {
        var user_index = getRandom(0, alldataarr.length);

        $("#span_name").html(alldataarr[user_index].name);
        $("#img_user").attr("src", alldataarr[user_index].headImgUrl);

    }, 100);

    beginLuck();
    $('#lottery .condition').addClass('disabled');
    return false;
}
var beginLuck = function () {  //key 0:只抽一个人奖 1:自动抽奖

    $("#stopLuck").show();
    $("#beginLuck").hide();


}

//停止抽奖
var stopLuck = function () {
    //
    clearInterval(setintIndex);
//后台选择
    var data = [1, 2, 3];
    var index = 0;
    setInterval(function () {

        console.log("恭喜您中奖了" + data[index]);
        showLuckAnimate("", "", "");
        index += 1;
    }, 1000);

    $("#beginLuck").show();
    $("#stopLuck").hide();
    return false;
}


//显示抽奖动画
var showLuckAnimate = function (imgUl, showLevel, userName) {
    $('body').append('<div class="animate-bg"><div class="light"></div><img class="luckbg" src="images/luckbg.png" /><img src="' + imgUl + '" class="luckUserHead" /><div class="showLuckUserName">' + userName + '</div><div class="showLuckLevel">恭喜<span>' + userName + '</span> 获<span>' + luckLevel + '</span>！</div></div>');
    $(".luckbg").animate({
        "width": "800px",
        "height": "518px",
        "margin-top": "-330px",
        "margin-left": "-400px",
        "opacity": "1"
    }, "fast");
    $(".luckUserHead").animate({"margin-top": "-275px"});
    $(".showLuckLevel").animate({"margin-top": "40px"});
    $(".showLuckUserName").animate({"opacity": "1"});
    setTimeout(function () {
        $(".animate-bg").animate({"opacity": "0"}, "slow", function () {
            $(".animate-bg").remove();
        });
        $("#bgsound").remove();
    }, 3000);
}
