<?php
require('./Des.php');

function composeUserInfo($username, $password, $timestamp, $key)
{
	$u = base64_encode(iconv("UTF-8","GBK//IGNORE",$username));
	$p = base64_encode(iconv("UTF-8","GBK//IGNORE",$password));
	$key = iconv("UTF-8","GBK//IGNORE",$key);
	$str = 'username='.$u.',password='.$p.',timestamp='.$timestamp;
	$crypt = new DES($key);
	$str = iconv("UTF-8","GBK//IGNORE",$str);
	$userInfo = rawurlencode(mb_convert_encoding(base64_encode($crypt->encrypt($str)),'utf-8','gbk'));
	return $userInfo;
}

function analyzeUserinfo($str, $key)
{
	$str = base64_decode(urldecode($str));
	$crypt = new DES($key);
	$str = $crypt->decrypt($str);
	return $str;
}

$key= '12345678';
$str = composeUserInfo('testUser', 'testUser@1.com',1420070408000, $key);
echo "--->".$str.PHP_EOL;
$str = analyzeUserinfo($str, $key);
echo "--->".$str.PHP_EOL;
