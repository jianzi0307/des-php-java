<?php
class DES
{
	private $key;
 
	function DES($key)
	{
		$this->key = $key;
	}
 
	public function encrypt($str)
	{		
		$str = $this->pksc5pad($str);
		$encrypt_str =  mcrypt_encrypt(MCRYPT_DES, $this->key, $str, MCRYPT_MODE_ECB);
		return $encrypt_str;
	}

	public function decrypt($str)
	{
		$encrypt_str = mcrypt_decrypt(MCRYPT_DES, $this->key, $str, MCRYPT_MODE_ECB);
		$encrypt_str = $this->pksc5unpad($encrypt_str);
		return $encrypt_str;
	}

	private function pksc5pad($source)
	{
		$blocksize = mcrypt_get_block_size(MCRYPT_DES, MCRYPT_MODE_ECB);
		$pad = $blocksize - (strlen($source) % $blocksize);
		return $source .= str_repeat(chr($pad), $pad);
	}

	private function pksc5unpad($string)
	{
		$size = strlen($string);	
		$padding = ord($string[$size - 1]);
		$string = substr($string, 0, -$padding);
		return $string;
	}

}

