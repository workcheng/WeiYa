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
                // var weixin = location.href.split('?')[1];
                //var code = weixin.split('=')[1];
                //获取用户信息
                $.ajax({
                    url: getUserInfo, //这个地址是服务器配置JSSDK的地址
                    data: {           // 这个地址是发生jssdk调用的url地址
                        // 用于服务器配置
                        id: 'ofj0MwjAxBL7kh_m89q0R2FiNqEY',
                        lang: ''
                    },
                    success: function (json) {
                        var userInfo = json.data;
                        $("#headImgUrl").attr("src", userInfo.headImgUrl);
                        $("#nickName").text(userInfo.nickname);
                        saveMsgClick(userInfo.headImgUrl);
                        //var tipId = window.setInterval("saveMsgClick()", 10000);
                    }
                })
            });
        }
    })
});
/**
 * 发送按钮事件
 * @param headImgUrl
 */
var saveMsgClick = function (headImgUrl) {
    $("#saveMsg").click(function () {
        var $btn = $(this);
        if ($btn.hasClass("disabled")) return;
        $btn.addClass("disabled");
        if (($(".wordsedit1 textarea").val() == "") || ($(".wordsedit1 textarea").val() == "null")) {
            $(".error_dialog p").html("请输入留言！");
            $(".error_dialog").show();
            setTimeout(function () {
                $(".error_dialog").hide()
            }, 3000);
            $btn.removeClass("disabled");
            return;
        }
        var msgUrl = BaseUrl + "message/danmu";
        var msgInfo = $("#msgDanmu").val();
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
                console.log("data", JSON.stringify(data));
            }
        })
    })
}