/**
 * Created by Administrator on 2017/1/8.
 */

$(document).ready(function () {
    signGrid();
});
var signGrid = function (index) {
    var degree = $("select[name='degree']").val();
    var luckyUrl = BaseUrl + "user/luckyUserList";
    $.ajax({
        url: luckyUrl,
        data: {
            index: degree
        },
        success: function (json) {
            var userList = json.data;
            $.each(userList, function (index, item) {
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
                var jqPeople = $("<td></td>").text(item.name).css({width: "40%"});
                var jqSection = $("<td></td>").text(item.depName).css({width: "40%"});
                var jqRank = $("<td></td>").text(rank).css({width: "20%"});
                jqTR.append(jqPeople).append(jqSection).append(jqRank);
                $("#signGrid").append(jqTR);
            })

        }
    })
}

var selectChange = function () {
    $("select[name='signTime']").change(function () {
        $("#signGrid").html("");
        var time = $("select[name='signTime']").val();
        signGrid(time);
    })
}