var lottery = {
    allLotteryUser: [],//签到用户
    autoExchangeIndex: 0,
    luckCount: 0,//等待抽奖人数
    userCount: 0,//用户个数
    changeUserDelay: 100,//切换照片的时间延迟，毫秒
    isLotter: false,//是否在抽奖
    getRandom: function (min, max) {
        var r = Math.random() * (max - min);
        var re = Math.round(r + min);
        re = Math.max(Math.min(re, max), min)
        return re;
    },
    //获取用户
    getLotteryUser: function () {
        var _this = this;
        //出现正在获取最新签到成员，请稍候...todo：这里有问题，不能获取全部的签到用户，不然会卡
        var getUserList = BaseUrl + "user/userList";
        $.ajax({
            async: true,
            url: getUserList,
            success: function (data) {
                _this.allLotteryUser = data.data;
                $("#userCount").text(_this.allLotteryUser.length);
            }
        })
    },
    getCnt: function () {
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
    },
    appendUser: function () {//插入li,防止图片切换卡顿
        var _this = this;
        //
        var box_pic_object = "<li><img src='images/default.png' title='求中奖' id='user_img'></li>";
        var box_name_object = "<li><span id='user_name'>求中奖</span></li>";
        for (var i = 0; i < _this.allLotteryUser.length; i++) {
            box_pic_object += "<li class='hide' style='display: none;'><img src='" + _this.allLotteryUser[i].headImgUrl + "' title='用户头像'></li>";
            box_name_object += "<li class='hide' style='display: none;'>" + _this.allLotteryUser[i].name + "</li>";
        }
        //$("#box_pic,#box_name").html();
        $("#box_name").html(box_name_object);
        $("#box_pic").html(box_pic_object);
    },
    exchangeUser: function () {
        var _this = this;
        _this.appendUser();
        if (this.autoExchangeIndex == 0) {//防止多次点击，只有停止清除事件后才能继续点
            if (_this.allLotteryUser.length > 0) {//如果没人，不进行点击
                var i = 1;
                _this.autoExchangeIndex = setInterval(function () {
                    if (_this.isLotter) {
                        $("#box_name li").hide();
                        $("#box_pic li").hide();
                        $("#box_name li:eq(" + i + ")").show();
                        $("#box_pic li:eq(" + i + ")").show();
                        i++;
                        if (i == _this.allLotteryUser.length + 1) {
                            i = 1;
                        }
                    } else {

                    }
                }, _this.changeUserDelay);
            }
            else {
                console.log("暂时没人签到哦~");
                showInfo("暂时没人签到哦~", 0);
                _this.showBeginBtn();
            }
        }
    },
    clearAutoExchange: function () {
        clearInterval(this.autoExchangeIndex);
        this.autoExchangeIndex = 0;
    },
    autoLotter: function (count) {//自动抽奖

    },
    showStopBtn: function () {
        $("#begin_lottery").hide();
        $("#stop_lottery").show();
        console.log("显示结束抽奖按钮");
    },
    showBeginBtn: function () {
        $("#begin_lottery").show();
        $("#stop_lottery").hide();
        console.log("显示开始抽奖按钮");
    },
    autoHanle: 0,
    stopLottery: function () {
        this.isLotter = false;
        var luckyLevel = $("select[name='prize']").val();
        var userNum = $("select[name='userNum']").val();
        lottery.clearAutoExchange();
        lottery.showBeginBtn();
        lottery.lotter(luckyLevel);//先抽一次，然后后续2秒抽一次
        if (userNum > 1) {
            var index = 1;
            lottery.autoHanle = setInterval(function () {
                //抽N次
                if (index < userNum) {
                    lottery.lotter(luckyLevel);
                }
                else {
                    clearInterval(lottery.autoHanle);
                }
                index++;

            }, 2000);
        }


    },
    lotter: function (luckyLevel) {
        var getLotteryUser = BaseUrl + "user/lotterySelect?time=" + this.getRandom(1, 1000);//lotterySelect
        $.ajax({
            url: getLotteryUser,
            success: function (data) {
                if (data.data.length > 0) {//抽出人了
                    var luckyUser = data.data[0]
                    var userName = luckyUser.name;
                    var imgUrl = luckyUser.headImgUrl;
                    var box_pic_object = "<li><img src='" + imgUrl + "' title='中奖啦' ></li>";
                    var box_name_object = "<li><span id='user_name'>" + userName + "</span></li>";
                    $("#box_name").html(box_name_object);
                    $("#box_pic").html(box_pic_object);
                    lottery.userCount++;
                    lottery.showLuckAnimate(imgUrl, luckyLevel, userName);
                    lottery.showLuckyUser(lottery.userCount, imgUrl, userName, luckyLevel);
                }
                else {
                    lottery.showLuckAnimate("images/default.png", "", "小伙伴们都已经中奖啦！", true);
                    if (lottery.autoHanle != 0) {
                        clearInterval(lottery.autoHanle);
                        lottery.autoHanle = 0;

                    }
                }
            }
        });
    },
    startLottery: function () {
        this.isLotter = true;
        console.log("开始抽奖");
        this.showStopBtn();
        console.log("开始变");
        this.exchangeUser();//开始变
    },
    showLuckyUser: function (idx, imgUI, userName, luckyLevel) {
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
    },
    showLuckAnimate: function (imgUl, showLevel, userName, isFinish) {
        if (isFinish) {
            $('body').append('<div class="animate-bg"><div class="light"></div><img class="luckbg" src="images/luckbg.png" /><img src="' + imgUl + '" class="luckUserHead" /><div class="showLuckUserName">' + userName + '</div><div class="showLuckLevel"><span>' + userName + '</span></div></div>');
        }
        else {
            $('body').append('<div class="animate-bg"><div class="light"></div><img class="luckbg" src="images/luckbg.png" /><img src="' + imgUl + '" class="luckUserHead" /><div class="showLuckUserName">' + userName + '</div><div class="showLuckLevel">恭喜<span>' + userName + '</span> 获<span>' + showLevel + '</span>！</div></div>');
        }

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
        $("#bgsound").remove();
    }

};
var isAuto = function () {
    $('.condition').addClass('disabled');
    return false;
}
var beginLuck = function () {
    $("#beginLuck").hide();
    $("#stopLuck").show();
}


/**
 * 页面加载事件
 */
$(document).ready(function () {
    // 获取待抽奖人员
    lottery.getLotteryUser();
    $('#begin_lottery').click(function () {
        lottery.startLottery();
    });
    $("#stop_lottery").click(function () {
        lottery.stopLottery();
    })
    $("#background").fullBg();
    setInterval(function () {
        lottery.getCnt();
    }, 1000 * 5)
});