var getAllUserInfo = [];
var setintIndex = 0;
var luckCount = 0;
var userCount = 1;
var getRandom = function (min, max) {
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
            getAllUserInfo = data.data;
        }
    })
}

/**
 * 获取总数
 */
var getCnt = function () {
    console.log("获取总数");
    var _this = this;
    //出现正在获取最新签到成员，请稍候...todo：这里有问题，不能获取全部的签到用户，不然会卡
    var getUserList = BaseUrl + "user/userListCount";
    $.ajax({
        async: true,
        url: getUserList,
        success: function (data) {
            $("#userCount").text(data.data);
        }
    })
}

/**
 * 获取幸运用户
 */
var getLuckyUser = function (callback) {
    var userNum = $("select[name='userNum']").val();
    var getLotteryUser = BaseUrl + "user/lotteryUserList?time=" + getRandom(1, 1000);//lotterySelect

    $.ajax({
        url: getLotteryUser,
        data: {count: userNum},
        success: function (data) {

            if (typeof(callback) === "function") {
                callback(data);
            }
        }

    });
}
var isAuto = function () {
    setintIndex = setInterval(function () {
        var user_index = getRandom(0, getAllUserInfo.length - 1);
        $("#userName").html(getAllUserInfo[user_index].name);
        $("#userImg").attr("src", getAllUserInfo[user_index].headImgUrl);
    }, 100);
    if (getAllUserInfo.length > 0) {
        var userNum = $("select[name='userNum']").val();
        beginLuck();
    } else {
        showInfo("当前还没有人参加活动！", 0);
    }

    $('.condition').addClass('disabled');
    return false;
}
var beginLuck = function () {
    $("#beginLuck").hide();
    $("#stopLuck").show();
}

/**
 * 停止抽奖
 * @returns {boolean}
 */
var stopLuck = function () {
    $("#beginLuck").show();
    $("#stopLuck").hide();
    var luckyLevel = $("select[name='prize']").val();
    var userNum = $("select[name='userNum']").val();
    var getLotteryUser = BaseUrl + "user/lotterySelect?time=" + getRandom(1, 1000);//lotterySelect
    $.ajax({
        url: getLotteryUser,
        success: function (data) {
            if (data.data.length > 0) {//抽出人了
                var luckyUser = data.data[0];
                var openId = luckyUser.openId;
                $("#userName").html(luckyUser.name);
                $("#userImg").attr("src", luckyUser.headImgUrl);
                var userName = luckyUser.name;
                var imgUl = luckyUser.headImgUrl;
                showLuckAnimate(imgUl, luckyLevel, userName);
                clearInterval(setintIndex);
                showLuckyUser(userCount, imgUl, userName, luckyLevel);
                sengMsg(openId, userName, luckyLevel);//发送信息给中奖用户
                userCount += 1;
            } else {
                showLuckAnimate("images/default.png", "", "小伙伴们都已经中奖啦！", true);
            }
        }

    });


    return false;
}
/**
 * 获奖名单
 * @param idx
 * @param imgUI
 * @param userName
 * @param luckyLevel
 */
var showLuckyUser = function (idx, imgUI, userName, luckyLevel) {
    var listContent = $("<li></li>").addClass("list-content clearfix");
    var listIdx = $("<div></div>").addClass("list-idx").text(idx);
    var listUser = $("<div></div>").addClass("list-user");
    var jqImg = $("<img>").attr("src", imgUI);
    var jqName = $("<p></p>").addClass("text-overflow").text(userName);
    var listPrize = $("<div></div>").addClass("list-prize text-overflow");
    var jqPrize = $("<div></div>").addClass("list-prize-name text-overflow").text(luckyLevel);
    listUser.append(jqImg).append(jqName);
    listContent.append(listIdx).append(listUser).append(listPrize).append(jqPrize);
    $("#luckyUser").find("ul").prepend(listContent);


}

/**
 * 显示抽奖动画
 * @param imgUl
 * @param showLevel
 * @param userName
 */
var showLuckAnimate = function (imgUl, showLevel, userName) {
    $('body').append('<div class="animate-bg"><div class="light"></div><img class="luckbg" src="images/luckbg.png" /><img src="' + imgUl + '" class="luckUserHead" /><div class="showLuckUserName">' + userName + '</div><div class="showLuckLevel">恭喜<span>' + userName + '</span> 获<span>' + showLevel + '</span>！</div></div>');
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

        var userNum = $("select[name='userNum']").val();
        if (userNum != 1) {
            isAuto();
            var t = setTimeout(function () {
                stopLuck();
                luckCount += 1;
            }, 2000);
            if (luckCount == userNum) {
                clearTimeout(t);
                clearInterval(setintIndex);
                luckCount = 0;
                $("#beginLuck").show();
                $("#stopLuck").hide();
            }
        }

    }, 3000);
}
/**
 * 发送获奖信息
 * @param openId
 * @param userName
 * @param luckyLevel
 */
var sengMsg = function (openId, userName, luckyLevel) {
    var degree = "";
    switch (luckyLevel) {
        case "一等奖":
            degree = "0";
            break;
        case "二等奖":
            degree = "1";
            break;
        case "三等奖":
            degree = "2";
            break;
    }
    var msgContent = [];
    msgContent.push({"openId": openId, "name": userName, "degree": degree});
    msgContent = JSON.stringify((msgContent));
    var sendUrl = BaseUrl + "message/sendMsg";
    $.ajax({
        type: 'POST',
        contentType: 'application/json',
        url: sendUrl,
        data: msgContent,
        success: function (json) {
            console.log("send", JSON.stringify(json));
        }
    })
}
$(document).ready(function () {
    getLottery();
    $('#beginLuck').click(function () {
        var userNum = $("select[name='userNum']").val();
        if (userNum > 1) {
            setTimeout(function () {
                stopLuck();
                luckCount = 1;
            }, 2000);
        }
        if (getAllUserInfo.length > 0) {
            isAuto();
        }
    });
    $("#stopLuck").click(function () {
        stopLuck();
    })
    $(window).load(function () {
        $("#background").fullBg();
    });
    setInterval(function () {
        getCnt();
    }, 1000 * 5)

});