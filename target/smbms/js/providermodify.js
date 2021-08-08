// var proCode = null;
var proName = null;
var proContact = null;
var proPhone = null;
var proAddress = null;
var saveBtn = null;
var backBtn = null;

$(function(){
	// proCode = $("#proCode");
	proName = $("#proName");
	proContact = $("#proContact");
	proPhone = $("#proPhone");
	proAddress = $("#proAddress");
	saveBtn = $("#save");
	backBtn = $("#back");
	
	//初始化的时候，要把所有的提示信息变为：* 以提示必填项，更灵活，不要写在页面上
	// proCode.next().html("*");
	proName.next().html("*");
	proContact.next().html("*");
	proPhone.next().html("*");
	proAddress.next().html("*");

	/*
	 * 验证
	 * 失焦\获焦
	 * jquery的方法传递
	 */

	// proCode.bind("blur",function(){
	// 	//ajax后台验证--userCode是否已存在
	// 	//user.do?method=ucexist&userCode=**
	// 	$.ajax({
	// 		type:"GET",//请求类型
	// 		url:path+"/jsp/provider.do",//请求的url
	// 		data:{method:"pcexist",proCode:proCode.val()},//请求参数
	// 		dataType:"json",//ajax接口（请求url）返回的数据类型
	// 		success:function(data){//data：返回数据（json对象）
	// 			if(data.proCode == "exist"){//账号已存在，错误提示
	// 				validateTip(proCode.next(),{"color":"red"},imgNo+ " 该供应商编码已存在",false);
	// 			}else if (data.userCode == "proCodeIsNull"){
	// 				validateTip(proCode.next(),{"color":"red"},imgNo+ " 供应商编码不能为空",false);
	// 			} else{//账号可用，正确提示
	// 				validateTip(proCode.next(),{"color":"green"},imgYes+" 该供应商可以使用",true);
	// 			}
	// 		},
	// 		error:function(data){//当访问时候，404，500 等非200的错误状态码
	// 			validateTip(proCode.next(),{"color":"red"},imgNo+" 您访问的页面不存在",false);
	// 		}
	// 	});
	//
	// }).bind("focus",function(){
	// 	//显示友情提示
	// 	validateTip(proCode.next(),{"color":"#666666"},"* 请输入供应商编码",false);
	// }).focus();


	proName.on("focus",function(){
		validateTip(proName.next(),{"color":"#666666"},"* 请输入供应商名称",false);
	}).on("blur",function(){
		if(proName.val() != null && proName.val() != ""){
			validateTip(proName.next(),{"color":"green"},imgYes,true);
		}else{
			validateTip(proName.next(),{"color":"red"},imgNo+" 供应商名称不能为空，请重新输入",false);
		}

	});


	proContact.on("focus",function(){
		validateTip(proContact.next(),{"color":"#666666"},"* 请输入联系人",false);
	}).on("blur",function(){
		if(proContact.val() != null && proContact.val() != ""){
			validateTip(proContact.next(),{"color":"green"},imgYes,true);
		}else{
			validateTip(proContact.next(),{"color":"red"},imgNo+" 联系人不能为空，请重新输入",false);
		}
		
	});
	
	proPhone.on("focus",function(){
		validateTip(proPhone.next(),{"color":"#666666"},"* 请输入手机号",false);
	}).on("blur",function(){
		var patrn=/^(13[0-9]|15[0-9]|18[0-9])\d{8}$/;
		if(proPhone.val().match(patrn)){
			validateTip(proPhone.next(),{"color":"green"},imgYes,true);
		}else{
			validateTip(proPhone.next(),{"color":"red"},imgNo + " 您输入的手机号格式不正确",false);
		}
	});

	proAddress.on("focus",function(){
		validateTip(proAddress.next(),{"color":"#666666"},"* 请输入地址",false);
	}).on("blur",function(){
		if(proAddress.val() != null && proAddress.val() != ""){
			validateTip(proAddress.next(),{"color":"green"},imgYes,true);
		}else{
			validateTip(proAddress.next(),{"color":"red"},imgNo+" 地址不能为空，请重新输入",false);
		}

	});

	saveBtn.on("click",function(){
		proName.blur();
		proContact.blur();
		proPhone.blur();
		proAddress.blur();
		if(proName.attr("validateStatus") == "true" &&
			proContact.attr("validateStatus") == "true" &&
			proPhone.attr("validateStatus") == "true" &&
			proAddress.attr("validateStatus") == "true"){
			if(confirm("是否确认提交数据")){
				$("#providerForm").submit();
			}
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