/**
 * 一些公用方法集合
 */

$(function(){
    "use strict";

    /** datepicker */
    if('datepicker' in $.fn){
        $(".xmdate").datepicker();
    }

    /** editor */
    var editorArea = $("#editor");
    var eidtor;
    if(editorArea.size()){
        editor = new Simditor({
            textarea: $("#editor"),
            placeholder: ""
        });
    }
    
    var fEditorArea = $("#full-editor");
    if(fEditorArea.size()){
        new Simditor({
            textarea: fEditorArea
        });
    }
});

// $(function(){
//     "use strict";
//     /**
//      * semantic ui 遗留下来的公共方法
//      */


//     /** sidebar */
//     $(".xmsidebar-toggle").click(function(){
//         var sidebar = $(this).data("target");
//         $(sidebar).sidebar("toggle");
//     });

//     /** modal */
//     $(".xmmodal-toggle").click(function(){
//         var modal = $(this).data("target");
//         $(modal).modal("setting", "transition", "vertical flip").modal("show");
//     });

//     /** dropdown */
//     $(".xmdropdown").dropdown({ "action": "nothing", "on": "hover"});

//     /** closable */
//     $(".xmclose").click(function(){
//         $(this).parent().fadeOut();
//     });
// });