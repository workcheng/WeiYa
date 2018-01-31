/**
 * Created by Administrator on 2017/1/8.
 */

$(document).ready(function () {
    selectChange();
    signGrid(0);
});
var signGrid = function (index) {
    var luckyUrl = BaseUrl + "user/luckyUserList";
    $.ajax({
        url: luckyUrl,
        data: {
            degree: index
        },
        success: function (json) {
            if (json.data.length <= 16) {
                $("#winnerContent").css({width: "1217px"});
                $(".bu").hide();
            }
            var userList = json.data;
            $.each(userList, function (index, item) {
                var unixTimestamp = new Date(item.hitTime);
                var hitTime = unixTimestamp.toLocaleString();
                var unixTimestamp = new Date(item.signDate);
                var signTime = unixTimestamp.toLocaleString();
                var rank = "";
                switch (item.degree) {
                    case 0:
                        rank = "一等奖";
                        break;
                    case 1:
                        rank = "二等奖";
                        break;
                    case 2:
                        rank = "三等奖";
                        break;
                }
                var jqTR = $("<tr></tr>");
                var jqPeople = $("<td></td>").text(item.name).css({width: "14%"}).attr({title: item.name});
                if (item.nickName) {
                    var nickName = $("<td></td>").text(item.nickName).css({width: "14%"}).attr({title: item.nickName});
                } else {
                    nickName = $("<td></td>").text("-").css({width: "14%"}).attr({title: "-"});
                }
                var jqSection = $("<td></td>").text(item.depName).css({width: "14%"}).attr({title: item.depName});
                var jqRank = $("<td></td>").text(rank).css({width: "14%"}).attr({title: item.openId});
                var signTime = $("<td></td>").text(signTime).css({width: "21%"}).attr({title: signTime});
                var wardTime = $("<td></td>").text(hitTime).css({width: "21%"}).attr({title: hitTime});
                jqTR.append(jqPeople).append(nickName).append(jqSection).append(jqRank).append(signTime).append(wardTime);
                $("#signGrid").append(jqTR);
            })

        }
    })
}

var selectChange = function () {
    $("select[name='degree']").change(function () {
        $("#signGrid").html("");
        var index = $("select[name='degree']").val();
        signGrid(index);
    })
}