$(function(){
	$.htmlEncode = function(e) {
        return e.replace(/&/g, "&amp;").replace(/ /g, "&nbsp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/\n/g, "<br />").replace(/"/g, "&quot;").replace(/'/g, "&#39;");
    };
    
    $.htmlDecode = function(e) {
        return e.replace(/&#39;/g, "'").replace(/<[Bb][Rr]\s*(\/)?\s*>/g, "\n").replace(/&nbsp;/g, " ").replace(/&lt;/g, "<").replace(/&gt;/g, ">").replace(/&quot;/g, '"').replace(/&amp;/g, "&");
    };
    
    $.htmlFilter = function(e){
    	return e==null?"" : e.replace(/<\s*\/?[^>]*\s*>/g,'').replace(/"/g , '');
    }
    $.filterScript = function(e){
    	return e.replace(/<script>/g,"&lt;script&gt;").replace(/<\/script>/g,"&lt;/script&gt;");
    }
}());