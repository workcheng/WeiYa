(function ($) {
    $.fn.barrager = function (danmu) {
        var ishide = $('#btn_change>li[data-name="messagewalldanmu"]').data('ishide');
        var time = new Date().getTime();
        var strHtml = '<span class="danmaku" id="danmu_' + time + '" style="display:' + (ishide ? 'none' : 'block') + '; font-family:\'' + danmu.font + '\';' + (danmu.bold ? ' font-weight:bold;' : '') + ' font-size:' + danmu.size + 'px; color:' + danmu.color + '; height:' + (danmu.size + 20) + 'px; line-height:' + (danmu.size + 20) + 'px; background:rgba(0,0,0,' + danmu.opacity + '); border-radius:8px; border:1px solid rgba(255,255,255,0.5); padding:5px 10px 5px 5px; white-space:nowrap; z-index:999999; position:absolute; left: ' + $('#danmu').data('width') + 'px;">' + danmu.info + '</span>';
        $('body').append(strHtml);
        var rowindex = $('#danmu').data('rowindex');
        $('#danmu_' + time).css('top', (danmu.size + 50) * rowindex);
        rowindex += 1;
        if (rowindex == $('#danmu').data('rowcount'))
            rowindex = 0;
        $('#danmu').data('rowindex', rowindex);
        $('#danmu_' + time).animate({ left: -($('#danmu_' + time).width() + 100) }, danmu.speed * 1000, 'linear', function () {
            $(this).remove();
        });
    }
})(jQuery);