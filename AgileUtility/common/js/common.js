String.prototype.trim=function(){
	return this.replace(/(^\s*)|(\s*$)/g, "");
}
String.prototype.ltrim=function(){
	return this.replace(/(^\s*)/g,"");
}
String.prototype.rtrim=function(){
	return this.replace(/(\s*$)/g,"");
}

String.prototype.decodeHTML=function(){
	return this.replace(/&amp;/gi, '&').replace(/&#39;/gi, "'").replace(/&quot;/gi, '"').replace(/&lt;/gi, '<').replace(/&gt;/gi, '>');
}

String.prototype.encodeHTML=function(){
	return this.replace(/&/gi, '&amp;').replace(/'/gi, "&#39;").replace(/"/gi, '&quot;').replace(/</gi, '&lt;').replace(/>/gi, '&gt;');
}

function showMessageBar(type, message){
	//0-success; 1-information; 2-alert; 3-danger
	var alertType = 'alert-info';
	
	switch(type){
		case 0:
			alertType = 'alert-success';
			break;
		case 1:
			alertType = 'alert-info';
			break;
		case 2:
			alertType = 'alert-warning';
			break;
		case 3:
			alertType = 'alert-danger';
			break;
		default:
			alertType = 'alert-success';
	}
		
	var now = new Date();
	var eID = now.getSeconds();
	var ele = '';
	ele = ele + '<div class="alert ' + alertType + ' alert-dismissible fade in" role="alert" id="alert' + eID + '">';
	ele = ele + '<button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button>';
	ele = ele + message;
	ele = ele + '</div>';
	
	$xinfo('#idMessageBarEle').append(ele);
	var idAlert = $xinfo('#alert' + eID).alert();
	$xinfo('html,body').animate({scrollTop: $xinfo('#idMessageBarEle').offset().top},'slow');
	
	window.setTimeout(function() {
		idAlert.fadeTo(500, 0).slideUp(500, function(){
			$xinfo(this).remove(); 
		});
	}, 5000);
}	

	
function showMessageBox(type, message){
	//0-success; 1-information; 2-alert; 3-danger
	var alertType = 'alert-info';
	
	switch(type){
		case 0:
			alertType = 'alert-success';
			break;
		case 1:
			alertType = 'alert-info';
			break;
		case 2:
			alertType = 'alert-warning';
			break;
		case 3:
			alertType = 'alert-danger';
			break;
		default:
			alertType = 'alert-success';
	}
	$xinfo("#messageContentID").text(message);
	$xinfo("#alertTypeID").attr("class", "modal-dialog alert " + alertType);
	$xinfo("#messageBoxClicker").trigger("click");
}

function hideMessageBox(){
	//$xinfo("#messageBoxID").hide();
	$xinfo("#idBtnCloseMsgBox").trigger("click");
}


