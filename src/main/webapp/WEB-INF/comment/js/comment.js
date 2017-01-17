/**
 * Created by zxc on 2017/1/8.
 */
$(document).ready(function () {
    var weixinUrl = location.href.split('#')[0];
    var url = BaseUrl + "sign/url";
    // 当前的网页请求地址
    $.ajax({
        url: url, //这个地址是服务器配置JSSDK的地址
        data: {           // 这个地址是发生jssdk调用的url地址,用于服务器配置
            url: weixinUrl
        },
        success: function (json) {
            var data = json.data;
            var config = {};
            for (var k in json.data) {
                config[k] = json.data[k];
            }
            config.debug = false;// 添加你需要的JSSDK的权限
            config.appId = config.appid;
            config.jsApiList = ['hideOptionMenu'];
            config.timestamp = parseInt(config.timestamp);
            config.nonceStr = config.noncestr;
            config.signature = config.signature
            wx.config(config);
            wx.ready(function () {
                wx.hideOptionMenu();
                var getUserInfo = BaseUrl + "sign/getUserInfo";
                var weixin = location.href.split('?')[1];
                var code = weixin.split('=')[1];
                //获取用户信息
                $.ajax({
                    url: getUserInfo, //这个地址是服务器配置JSSDK的地址
                    data: {           // 这个地址是发生jssdk调用的url地址
                        // 用于服务器配置
                        id: code,
                        lang: ''
                    },
                    success: function (json) {
                        var userInfo = json.data;
                        $("#headImgUrl").attr("src", userInfo.headImgUrl);
                        $("#nickName").text(userInfo.nickname);
                        saveMsgClick(userInfo.headImgUrl);
                    }
                })
            });
        }
    })
    /*$('.emotion').qqFace({
     id: 'facebox',
     assign: 'msgDanmu',
     path: BaseUrl + "comment/arclist/"	//表情存放的路径

     });*/
});
/**
 * 发送按钮事件
 * @param headImgUrl
 */
var saveMsgClick = function (headImgUrl) {
    $("#saveMsg").click(function () {
        if (($(".wordsedit1 input").val() == "") || ($(".wordsedit1 input").val() == "null")) {
            showInfo("请输入留言！");
            return;
        }
        var _now = new Date();
        var difTime = _now - new Date(localStorage.lastTime);  //时间差的毫秒数
        if (isNaN(difTime)) {
            localStorage.lastTime = _now;
        }
        if (difTime < 5 * 1000) {
            showInfo("请不要发送太频繁哦~~");
            return false;
        }
        localStorage.setItem("lastTime", _now);
        var msgUrl = BaseUrl + "message/danmu";
        var msgInfo = $("#msgDanmu").val();
        var time = getNowFormatDate(new Date());

        localStorage.msgInfo = msgInfo;
        localStorage.msgTime = time;
        localStorage.setItem("msgInfo", msgInfo);
        localStorage.setItem("msgTime", time);
        var danmuMsg = {
            "content": msgInfo,
            "headImgUrl": headImgUrl
        };
        danmuMsg = JSON.stringify(danmuMsg);
        $.ajax({
            type: 'POST',
            contentType: 'application/json',
            url: msgUrl,
            data: danmuMsg,
            success: function (data) {
                if (data.status == "1000") {
                    showInfo("上墙成功!");
                    showMsgLists(localStorage.msgTime, localStorage.msgInfo);
                    $(".wordsedit1 input").val("");
                } else {
                    var msg = data.data;
                    showInfo(msg);
                    $(".wordsedit1 input").val("");
                }

            }
        });
    })
}
var showInfo = function (txt) {
    $(".error_dialog p").html(txt);
    $(".error_dialog").show();
    setTimeout(function () {
        $(".error_dialog").hide()
    }, 3000);
}

var showMsgLists = function (time, msgInfo) {
    var wordRecord = $("<div></div>").addClass("words_record");
    var msg = $("<p></p>").addClass("word").text(msgInfo);
    var msgTime = $("<p></p>").addClass("word-time")
    var msgTimes = $("<span></span>").addClass("times").text(time);
    var msgState = $("<span></span>").addClass("state").text("已上墙");
    msgTime.append(msgTimes).append(msgState);
    wordRecord.append(msg).append(msgTime);
    $(".lists").prepend(wordRecord);
}

