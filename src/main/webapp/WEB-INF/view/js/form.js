/**
 * Created by Administrator on 2017/1/8.
 */

$(document).ready(function () {
    getOrderInfo();
    // 根据实际填写接口的配置地点
    // 这里的接口地址是基于node-weixin配置的。
    var weixinUrl = location.href.split('#')[0];
    $('body').append('<div id="loading"><img src="/view/images/loading.gif" style="width: 150px;margin-left: 97px;"></div>');
    wx.hideOptionMenu();
    var getUserInfo = BaseUrl + "sign/getUserInfo";//"auth/user_info";
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
            var openId = userInfo.openId;
            var memberUrl = BaseUrl + "user/isMember";
            $.ajax({
                url: memberUrl,
                data: {
                    id: openId
                },
                success: function (json) {
                    var data = json.data;
                    if(json.status == "1001"){
                        alert("内部错误");
                        return;
                    }
                    if (data == "NOT_SIGN") {
                        $("#loading").hide();
                        $("#partySign").show();
                        userSignClick(userInfo.headImgUrl, openId);
                    } else if (json.status == "2005") {
                        $("#partySign").hide();
                        $("#loading").hide();
                        $("#reSign").hide();
                        $("#unStart").show();
                    } else {
                        $("#partySign").hide();
                        $("#loading").hide();
                        $("#reSign").show();
                        $("#unStart").hide();
                    }
                }
            })
        }
    })
})
;

var getOrderInfo = function () {
    $("#isOrder").click(function () {
        if ($("#isOrder").is(':checked')) {
            $(this).val(1);
        } else {
            $(this).val(0);
        }
    });
}

var userSignClick = function (headImgUrl, openId) {
    $('#btnSign').click(function () {
        var $btn = $(this);
        if ($btn.hasClass("disabled")) return;
        $btn.addClass("disabled");
        if ($("#userName").val() == "") {
            $(".error_dialog p").html("请输入姓名！");
            $(".error_dialog").show();
            setTimeout(function () {
                $(".error_dialog").hide()
            }, 3000);
            $btn.removeClass("disabled");
            return;
        }
        var addUserUrl = BaseUrl + "user/save";
        var signInfo = {
            "name": $("input[name='userName']").val(),
            "depName": $("select[name='dept']").val(),
            "order": $("#isOrder").val(),
            "headImgUrl": headImgUrl,
            "openId": openId,
            "signFlag": 1
        };
        signInfo = JSON.stringify(signInfo);
        $.ajax({
            type: 'POST',
            url: addUserUrl,
            contentType: 'application/json',
            data: signInfo,
            success: function (json) {
                var msg = json.message;
                if (msg == "已签到") {
                    $("#partySign").hide();
                    $("#loading").hide();
                    $("#unStart").hide();
                    $("#signSuccess").show();
                }
            }
        })
    })
}