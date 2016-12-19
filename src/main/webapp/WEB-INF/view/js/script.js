'use strict';
var shuffle = function (o) { //v1.0
    for (var j, x, i = o.length; i; j = Math.floor(Math.random() * i), x = o[--i], o[i] = o[j], o[j] = x);
    return o;
};

function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

var config = {
    black_list: [],

    machine_count: 5,
    machine_per_row: 3,
};

$(document).ready(function () {

    var result = [];
    // console.log($.cookie('result'));
    /*
     if ($.cookie('result') !== undefined) {
     result = JSON.parse($.cookie('result'));
     }
     */
    var configJSON = "http://zxc.tunnel.qydev.com/view/config.json";
    $.getJSON(configJSON, function (config_loaded) {
        config = $.extend(config, config_loaded);

        var count = parseInt(getParameterByName('count'));
        if (!isNaN(count)) {
            config.machine_count = count;
        }

        var filter_data = function (d) {
            return d.filter(function (item) {
                return config.black_list.indexOf(item) < 0 && result.indexOf(item) < 0;
            });
        };

        var container = $('#slotMachine-container');
        var box;
        for (var i = 0; i < config.machine_count; i++) {
            if (i % config.machine_per_row == 0) {
                box = $('<div class="slotMachine-box"></div>');
                container.append(box);
            }

            box.append($('<div id="slotMachine' + i + '" class="slotMachine"></div>'));
        }

        var running_count = 0;
        var machine_item = $('.slotMachine');
        var machine_arr = [];

        var machine_process = function (data) {
            data = shuffle(filter_data(data));
            var machine_segment_length = Math.floor(data.length / machine_item.size());

            machine_item.each(function (index) {

                var target = $(this);
                var i = index * machine_segment_length;
                var end = (index == machine_item.size() - 1) ? data.length : (index + 1) * machine_segment_length;
                for (; i < end; i++) {
                    target.append($('<div class="slot">' + data[i] + '</div>'));
                }

                var machine = target.slotMachine({
                    delay: 300,
                });
                machine_arr.push(machine);
            });

            $('#btn-control').click(function () {
                // console.log(running_count);
                if (running_count !== 0 && running_count !== machine_item.size()) {
                    return;
                }

                var btn = $(this);

                $.each(machine_arr, function (index, machine) {

                    if (machine.isRunning) {
                        machine.stop(false);
                        return;
                    }

                    var elem = machine.element;
                    elem.removeClass('active');
                    btn.addClass('running');
                    $('#congra-label').removeClass('active');

                    var complete_cb = function (result_index) {
                        //console.log(result_index);
                        if (result_index === null) {
                            this.next();
                            return;
                        }

                        var result_item = this.$tiles[result_index].innerHTML;
                        if ($.inArray(result_item, result) >= 0) {
                            this.next();
                            return;
                        }

                        running_count--;
                        result.push(result_item);
                        //$.cookie('result', JSON.stringify(result));
                        elem.addClass('active');
                        btn.removeClass('running');
                        $('#congra-label').addClass('active');
                    };

                    if (result.length > 0 &&
                        machine.active < machine.$tiles.length &&
                        machine.$tiles[machine.active] !== undefined) {

                        machine.$tiles[machine.active].remove();
                        machine.$tiles.splice(machine.active, 1);
                    }

                    running_count++;
                    machine.shuffle(undefined, complete_cb);
                });
            });
        };

        //$.getJSON('../server.php?r=lucky&key=' + getParameterByName('key'), machine_process);
    });
});
