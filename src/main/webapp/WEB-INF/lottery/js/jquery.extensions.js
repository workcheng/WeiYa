jQuery.extend({
    extendGet: function (url, data, dataType, sucess, error) {
        return jQuery.ajax({
            url: url,
            timeout: 60000,
            type: "GET",
            dataType: dataType,
            data: data,
            success: sucess,
            error: error || function () { }
        });
    },
    extendGetJSON: function (url, data, sucess, error) {
        return jQuery.ajax({
            url: url,
            timeout: 60000,
            type: "GET",
            dataType: "json",
            data: data,
            success: sucess,
            error: error || function () { }
        });
    },
    extendNoCacheGetJSON: function (url, data, sucess, error) {
        return jQuery.ajax({
            url: url,
            timeout: 60000,
            type: "GET",
            dataType: "json",
            data: data,
            cache: false,
            success: sucess,
            error: error || function () { }
        });
    },
    extendSyncGetJSON: function (url, data, sucess, error) {
        return jQuery.ajax({
            url: url,
            timeout: 60000,
            type: "GET",
            dataType: "json",
            data: data,
            async: false,
            success: sucess,
            error: error || function () { }
        });
    },
    extendNoCacheAndSyncGetJSON: function (url, data, sucess, error) {
        return jQuery.ajax({
            url: url,
            timeout: 60000,
            type: "GET",
            dataType: "json",
            data: data,
            cache: false,
            async: false,
            success: sucess,
            error: error || function () { }
        });
    },
    extendPost: function (url, data, dataType, sucess, error) {
        return jQuery.ajax({
            url: url,
            timeout: 60000,
            type: "POST",
            dataType: dataType,
            data: data,
            success: sucess,
            error: error || function () { }
        });
    }
});