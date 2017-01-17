/**
 * Created by Administrator on 2017/1/8.
 */

$(document).ready(function () {
    selectChange();
});
var signGrid = function (index) {
    var orderUrl = BaseUrl + "user/allUserList";
    $.ajax({
        url: orderUrl,
        data: {
            index: index
        },
        success: function (json) {
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
                    case 1:
                        order = "是";
                }
                var jqTR = $("<tr></tr>");
                var jqPeople = $("<td></td>").text(item.name).css({width: "40%"});
                var jqSection = $("<td></td>").text(item.depName).css({width: "40%"});
                var jqRank = $("<td></td>").text(item.depRank).css({width: "20%"});
                jqTR.append(jqPeople).append(jqSection).append(jqRnk);
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
function PrefixInteger(num, n) {
    return (Array(n).join(0) + num).slice(-n);
}

Date.prototype.toLocaleString = function () {
    return this.getFullYear() + "-" + PrefixInteger((this.getMonth() + 1), 2) + "-" + PrefixInteger(this.getDate(), 2) + "  " + PrefixInteger(this.getHours(), 2) + ":" + PrefixInteger(this.getMinutes(), 2) + ":" + PrefixInteger(this.getSeconds(), 2);
};