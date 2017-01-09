/**
 * Created by Administrator on 2017/1/8.
 */

$(document).ready(function () {
    signGrid();

});
var signGrid = function () {
    var orderUrl = BaseUrl + "user/allUserList";
    $.ajax({
        url: orderUrl,
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
                var jqTime = $("<td></td>").text(commonTime);
                var jqPeople = $("<td></td>").text(item.name);
                var jqSection = $("<td></td>").text(item.depName);
                var jqEat = $("<td></td>").text(order);
                jqTR.append(jqTime).append(jqPeople).append(jqSection).append(jqEat);
                $("#signGrid").append(jqTR);
            })

        }
    })

}

Date.prototype.toLocaleString = function () {
    return this.getFullYear() + "-" + (this.getMonth() + 1) + "-" + this.getDate() + "- " + this.getHours() + ":" + this.getMinutes() + ":" + this.getSeconds();
};