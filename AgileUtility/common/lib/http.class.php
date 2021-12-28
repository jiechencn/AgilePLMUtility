<?php
class XHttpReq {
	public function __construct() {
		!extension_loaded ( 'curl' ) && exit ( 'Error: CURL not loaded.' );
		header ( 'Content-Type: text/html; charset=utf-8' );
	}
	function sendPost($url, $refer="", $post_data, $statusOrBody=true, $cookie) {
		$options = array( 
        CURLOPT_RETURNTRANSFER => true,         // return web page 
        CURLOPT_HEADER         => true,        // return headers 
        CURLOPT_FOLLOWLOCATION => true,         // follow redirects 
        CURLOPT_ENCODING       => "",           // handle all encodings 
        CURLOPT_USERAGENT      => "User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.111 Safari/537.36",     // who am i 
        CURLOPT_AUTOREFERER    => true,         // set referer on redirect 
        CURLOPT_CONNECTTIMEOUT => 120,          // timeout on connect 
        CURLOPT_TIMEOUT        => 120,          // timeout on response 
        CURLOPT_MAXREDIRS      => 10,           // stop after 10 redirects 
        CURLOPT_SSL_VERIFYHOST => 0,            // don't verify ssl 
        CURLOPT_SSL_VERIFYPEER => false,        // 
        CURLOPT_VERBOSE        => 1,                // 
				CURLOPT_REFERER				 => $refer,
				CURLOPT_POST					 => 1,
				CURLOPT_POSTFIELDS		 => $post_data,
				CURLOPT_NOBODY			=> false,
				CURLOPT_COOKIE			=> $cookie,
				CURLOPT_HTTPHEADER,  array("Content-Type: application/x-www-form-urlencoded", "Content-length: ".strlen($post_data))
    );

		$ch = curl_init($url); 
		curl_setopt_array($ch,$options); 
		$content = curl_exec($ch); 
		$statusCode = curl_getinfo($ch, CURLINFO_HTTP_CODE); 
		curl_close($ch); 
		return $statusOrBody?$statusCode:$content;

	}
	
	function sendGet($url, $refer="", $get_data, $statusOrBody=true, $cookie) {
		$options = array( 
        CURLOPT_RETURNTRANSFER => true,         // return web page 
        CURLOPT_HEADER         => true,        // return headers 
        CURLOPT_FOLLOWLOCATION => true,         // follow redirects 
        CURLOPT_ENCODING       => "",           // handle all encodings 
        CURLOPT_USERAGENT      => "User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.111 Safari/537.36",     // who am i 
        CURLOPT_AUTOREFERER    => true,         // set referer on redirect 
        CURLOPT_CONNECTTIMEOUT => 120,          // timeout on connect 
        CURLOPT_TIMEOUT        => 120,          // timeout on response 
        CURLOPT_MAXREDIRS      => 10,           // stop after 10 redirects 
        CURLOPT_SSL_VERIFYHOST => 0,            // don't verify ssl 
        CURLOPT_SSL_VERIFYPEER => false,        // 
        CURLOPT_VERBOSE        => 1,                // 
				CURLOPT_REFERER				 => $refer,
				CURLOPT_NOBODY			=> false,
				CURLOPT_COOKIE			=> $cookie,
				CURLOPT_HTTPHEADER,  array("Content-Type: application/x-www-form-urlencoded", "Content-length: ".strlen($get_data))
    );

		$ch = curl_init($url."?".$get_data); 
		curl_setopt_array($ch,$options); 
		$content = curl_exec($ch); 
		$statusCode = curl_getinfo($ch, CURLINFO_HTTP_CODE); 
		curl_close($ch); 
		return $statusOrBody?$statusCode:$content;
	}
	
	function __destruct() {
		//print "Destroying " . $this->name . " ";
	}
}

?>