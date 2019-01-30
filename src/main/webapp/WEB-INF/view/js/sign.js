/**
 * Created by Administrator on 2017/1/8.
 */

$(document).ready(function () {
    selectChange();
    signGrid(2);
});
var signGrid = function (index) {
    var orderUrl = BaseUrl + "user/allUserList";
    $.ajax({
        url: orderUrl,
        data: {
            index: index
        },
        success: function (json) {
            if (json.data.users.length <= 16) {
                $("#signContent").css({width: "1000"});
                $(".bu").hide();
            }
            $(".number").text(json.data.orderCount);
            var userList = json.data.users;
            $.each(userList, function (index, item) {
                var unixTimestamp = new Date(item.signDate);
                var commonTime = unixTimestamp.toLocaleString();
                var isOrder = item.order;
                var order = "";
                switch (isOrder) {
                    case 0:
                        order = "否";
                        break;
                    case 1:
                        order = "是";
                        break;
                }
                var jqTR = $("<tr></tr>");
                var jqTime = $("<td></td>").text(commonTime).css({width: "20%"});
                var jqPeople = $("<td></td>").text(item.name).css({width: "20%"});
                var jqSection = $("<td></td>").text(item.depName).css({width: "30%"});
                var img = $("<img src='"+item.headImgUrl+"'>").css({"height":"30px","padding":"0","margin":"0"});
                var jqEat = $("<td></td>").css({"width":"5%"});
                jqEat.append(img);
                jqTR.append(jqTime).append(jqPeople).append(jqSection).append(jqEat);
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