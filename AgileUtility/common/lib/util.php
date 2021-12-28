<?php

//$date_str = "2011-9-11 8:00:01";
//echo $time_str = str_format_time($date_str);
// return 1315728001
function str_format_time($timestamp = ''){
	if (preg_match("/[0-9]{4}-[0-9]{1,2}-[0-9]{1,2} ([0-9]|0[0-9]|1[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])/i", $timestamp)){
		list($date,$time)=explode(" ",$timestamp);
		list($year,$month,$day)=explode("-",$date);
		list($hour,$minute,$seconds )=explode(":",$time);
		$timestamp=gmmktime($hour,$minute,$seconds,$month,$day,$year);
	}else{
		$timestamp=time();
	}
	return $timestamp;
}

function rmHtmlTag($content){
  $content = str_replace ( '&', '&amp;', $content );
  $content = str_replace ( '\'', '&#039;', $content );
  $content = str_replace ( '"', '&quot;', $content );
  $content = str_replace ( '<', '&lt;', $content );
  $content = str_replace ( '>', '&gt;', $content );

  return $content;
}
?>
