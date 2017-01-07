
var lottery = {
    allLotteryyUser: [],//签到用户
    autoExchangeIndex: 0,
    luckCount: 0,//等待抽奖人数
    userCount: 0,//用户个数
    changeUserDelay:200,//切换照片的时间延迟，毫秒
    getRandom: function (min, max) {
        var r = Math.random() * (max - min);
        var re = Math.round(r + min);
        re = Math.max(Math.min(re, max), min)
        return re;
    },
    //获取用户
    getLotteryUser: function () {
        var _this=this;
        //出现正在获取最新签到成员，请稍候...todo：这里有问题，不能获取全部的签到用户，不然会卡
        var getUserList = BaseUrl + "user/userList";
        $.ajax({
            async: true,
            url: getUserList,
            success: function (data) {
                _this.allLotteryyUser= data.data;
                $("#userCount").text(_this.allLotteryyUser.length);

            }
        })
    },
    appendUser:function(){//插入li,防止图片切换卡顿
      try{

      } catch (ex)      {

      }

    },
    exchangeUser: function () {
        var _this = this;
        if (this.autoExchangeIndex ==0) {//防止多次点击，只有停止清除事件后才能继续点
            if (_this.allLotteryyUser.length>0) {//如果没人，不进行点击
                _this.autoExchangeIndex = setInterval(function () {
                    var user_index = _this.getRandom(0, _this.allLotteryyUser.length - 1);
                    $("#user_name").html(_this.allLotteryyUser[user_index].name);
                    $("#user_img").attr("src", _this.allLotteryyUser[user_index].headImgUrl);
                }, _this.changeUserDelay);
            }
            else {
                console.log("暂时没人签到哦~");
                _this.showBeginBtn();
            }
        }
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
    stopLottery: function () {
        var luckyLevel = $("select[name='prize']").val();
        var userNum = $("select[name='userNum']").val();
        var getLotteryUser = BaseUrl + "user/lotterySelect?time=" + getRandom(1, 1000);//lotterySelect
        $.ajax({
            url: getLotteryUser,
            success: function (data) {
                var luckyUser = data.data[0]
                $("#userName").html(luckyUser.name);
                $("#userImg").attr("src", luckyUser.headImgUrl);
                var userName = luckyUser.name;
                var imgUl = luckyUser.headImgUrl;
                showLuckAnimate(imgUl, luckyLevel, userName);
                clearInterval(setintIndex);
                showLuckyUser(userCount, imgUl, userName, luckyLevel);
                userCount += 1;
            }

        });
    },
    startLottery: function () {
        console.log("开始抽奖");
        this.showStopBtn();
        console.log("开始变");
        this.exchangeUser();//开始变
    },
    showLuckUser: function (idx, imgUI, userName, luckyLevel) {
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
 * 停止抽奖
 * @returns {boolean}
 */
var stopLuck = function () {

}
 
/**
 * 显示抽奖动画
 * @param imgUl
 * @param showLevel
 * @param userName
 */


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
       // stopLuck();
    })
    $("#background").fullBg();
});