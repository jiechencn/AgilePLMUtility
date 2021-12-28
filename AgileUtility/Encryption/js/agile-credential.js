var $xwiz=jQuery.noConflict();

$xwiz("document").ready(function(){
	init();
	
	$xwiz("#idBtnSHASubmit").click(function(){
		var alg = $xwiz.trim($xwiz('#idSelSHAType').val());
		var username = $xwiz.trim($xwiz('#idInputSHAUsername').val());
		var password = $xwiz.trim($xwiz('#idInputSHAPassword').val());
		var ed = "e";

		if (username=="" || password==""){
			alert("Input Login ID and Password please.");
			return;
		}
		
		doEnDecrypt(alg, ed, username, password);
	});
	
	$xwiz("#idBtnBFSubmit").click(function(){
		var alg = "BF";
		var ed = $xwiz.trim($xwiz('#idSelEDType').val());
		var password = $xwiz.trim($xwiz('#idInputBFPassword').val());

		if (password==""){
			alert("Input Password please.");
			return;
		}
		
		doEnDecrypt(alg, ed, "", password);
	});
	
	$xwiz("#idSelEDType").change(function(){
		if ($xwiz.trim($xwiz('#idSelEDType').val())=="e")
			$xwiz('#idBtnBFSubmit').text("Encrypt");
		else
			$xwiz('#idBtnBFSubmit').text("Decrypt");
	});
	
	function doEnDecrypt(alg, ed, username, password){
		
		username = username.toLowerCase();  // must lower case
		// call servlet
		$xwiz.ajax({
			type:"get",
			url:"../AgileCredentialUtilServlet",
			data:{"alg":alg, "ed":ed, "username":username, "password":password},
			dataType:"text",
			success:function(dd){
				if (dd.indexOf("ERR:")==0){
					if (alg=="BF"){
						showBFPasswordElement(false);
					}else{
						showSHAPasswordElement(false);
					}
					alert(dd);
				}else{
					if (alg=="BF"){
						showBFPasswordElement(true);
						createBFPasswordContent(alg, username, dd);
					}else{
						showSHAPasswordElement(true);
						createSHAPasswordContent(alg, username, dd);
					}
				}
			},
			error:function(msg){
					if (alg=="BF"){
						showBFPasswordElement(false);
					}else{
						showSHAPasswordElement(false);
					}
				
				alert("Error! Try again later or contact jie.chen@oracle.com for assistance: " + msg);
			}
		});
	}
	
	function createSHAPasswordContent(alg, username, password){
		var ele = "<pre id='idSHAPasswordContent'><code>";
		ele = ele + "-- Backup partial table<br/>";
		ele = ele + "CREATE TABLE agileuser_bk_" + username + " AS SELECT * FROM agileuser NOLOGGING WHERE loginid = '" + username + "';" + "<br/>";
		ele = ele + "-- Update table<br/>";
		ele = ele + "UPDATE agileuser SET login_pwd = '" + password + "' WHERE loginid = '" + username + "';<br/>";
		ele = ele + "COMMIT;";
		ele = ele + "</code></pre>";
		$xwiz('#idSHAPasswordContent').remove();
		$xwiz('#idDivSHAPassword').append(ele);
	}
	function createBFPasswordContent(alg, username, password){
		var ele = "<pre id='idBFPasswordContent'><code>";
		ele = ele + password;
		ele = ele + "</code></pre>";
		$xwiz('#idBFPasswordContent').remove();
		$xwiz('#idDivBFPassword').append(ele);
	}
	
	function init(){
		showSHAPasswordElement(false);
		showBFPasswordElement(false);
	}
	
	
	function showSHAPasswordElement(show){
		if (show){
			$xwiz("#idDivSHAPassword").show();
		}else{
			$xwiz("#idDivSHAPassword").hide();
		}
	}
	function showBFPasswordElement(show){
		if (show){
			$xwiz("#idDivBFPassword").show();
		}else{
			$xwiz("#idDivBFPassword").hide();
		}
	}
	

	
});

