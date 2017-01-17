/**
 * Created by Administrator on 2017/1/8.
 */

$(document).ready(function () {
    selectChange();
});
var signGrid = function (index) {
    var luckyUrl = BaseUrl + "user/luckyUserList";
    $.ajax({
        url: luckyUrl,
        data: {
            degree: index
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
    $("select[name='degree']").change(function () {
        $("#signGrid").html("");
        var index = $("select[name='degree']").val();
        signGrid(index);
    })
}