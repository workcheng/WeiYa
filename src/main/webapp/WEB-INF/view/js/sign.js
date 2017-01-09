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
            alert(JSON.stringify(json))
        }
    })
    var jqTR = $("<tr></tr>");
    var jqTime = $("<td>点餐了</td>");
    var jqPeople = $("<td>点餐了</td>");
    var jqSection = $("<td>点餐了</td>");
    var jqEat = $("<td>点餐了</td>");
    jqTR.append(jqTime).append(jqPeople).append(jqSection).append(jqEat);
    $("#signGrid").append(jqTR);
}