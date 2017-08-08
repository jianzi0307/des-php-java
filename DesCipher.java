import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64; // 本样例的Base64 API, 来自Apache组织的工具类.

/**
 * DES加解密
 * 
 * @author 
 */
public class DesCipher {
    /**
     * 加密
     *
     * @param plainText
     *            待加密的原始字符串
     * @param key
     *            共享密钥
     * @return 加密后并经过Base64编码的字符串
     */
    public static String encryptData(String plainText, String key) {
        Cipher encryptDesCipher = null;
        try {
            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("GBK")); // GBK
            SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
            SecretKey desSecretKey = factory.generateSecret(desKeySpec);
            encryptDesCipher = Cipher.getInstance("DES/ECB/PKCS5Padding"); // DES/ECB/PKCS5Padding
            encryptDesCipher.init(1, desSecretKey);

            byte[] cryptoText = null;
            cryptoText = encryptDesCipher.doFinal(plainText.getBytes("GBK")); // GBK
            if (cryptoText != null) {
                return new String(Base64.encodeBase64(cryptoText), "GBK"); // GBK
            }
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    /**
     * 解密
     *
     * @param base64EncryptInfo
     *            待解密的字符串
     * @param key
     *            密钥
     * @return 解密后的字符串
     * @throws UnsupportedEncodingException
     */
    public static String decryptData(String base64EncryptInfo, String key) {
        try {
            byte[] encryptInfo = null;
            if (base64EncryptInfo != null) {
                encryptInfo = Base64.decodeBase64(base64EncryptInfo.getBytes("GBK")); // GBK
            }
            Cipher decryptDesCipher = null;
            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("GBK")); // GBK
            SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
            SecretKey desSecretKey = factory.generateSecret(desKeySpec);
            decryptDesCipher = Cipher.getInstance("DES/ECB/PKCS5Padding"); // DES/ECB/PKCS5Padding
            decryptDesCipher.init(2, desSecretKey);

            byte[] plainText = null;
            plainText = decryptDesCipher.doFinal(encryptInfo);
            if (plainText != null) {
                return new String(plainText, "GBK"); // GBK
            }
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}





/**
 * 合成userinfo串
 * @param username 名
 * @param password 密码
 * @param timestamp 时间戳
 * @param key 解密密钥
 * @return
 */
public static String composeUserinfo(String username, String password, long timestamp, String key) {
    try {
        StringBuilder userInfo = (new StringBuilder())
            .append("username=").append(new String(Base64.encodeBase64(username.getBytes("GBK")), "GBK"))
            .append(",password=").append(new String(Base64.encodeBase64(password.getBytes("GBK")), "GBK"))
            .append(",timestamp=").append(timestamp);
        String userInfoStr = DesCipher.encryptData(userInfo.toString(), key);
        userInfoStr = URLEncoder.encode(userInfoStr, "UTF-8"); // UTF-8
        return userInfoStr;
    } catch (Throwable e) {
        e.printStackTrace();
        return null;
    }
}

/**
 * 从userinfo中解析出用户名/密码
 * @param str 处于加密形态的userinfo
 * @param key 解密密钥
 * @return
 */
public static Map<String, String> analyzeUserinfo(String str, String key) {
    try {
        str = URLDecoder.decode(str, "UTF-8"); // UTF-8
        str = DesCipher.decryptData(str, key);
        String[] userinfo = str.split(",");
        Map<String, String> paras = new HashMap<String, String>();
        for (String nameAndVal : userinfo) {
            String[] nv = nameAndVal.split("=");
            if("username".equals(nv[0]) || "password".equals(nv[0])) {
                nv[1] = new String(Base64.decodeBase64(nv[1].getBytes("GBK")), "GBK");
            }
            paras.put(nv[0], nv[1]);
        }
        return paras;
    } catch (Throwable e) {
        e.printStackTrace();
        return null;
    }
}

