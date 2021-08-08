var confirmBtn = null;
var backBtn = null;

$(function(){
    confirmBtn = $("#confirm");
    backBtn = $("#back");

   confirmBtn.on("click",function(){
       //alert("modify: "+referer);
       if(referer != undefined
           && null != referer
           && "" != referer
           && "null" != referer
           && referer.length > 4){
           window.location.href = referer;
       }else{
           history.back(-1);
       }
       });

    backBtn.on("click",function(){
        //alert("modify: "+referer);
        if(referer != undefined
            && null != referer
            && "" != referer
            && "null" != referer
            && referer.length > 4){
            window.location.href = referer;
        }else{
            history.back(-1);
        }
    });
});