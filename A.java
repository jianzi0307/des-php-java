package com.pkuvr.login_server;

import org.apache.commons.codec.binary.Base64;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jianzi0307 on 16/9/29.
 */
public class A {
    public static void main(String[] args) {
        String key = "12345678";
        String r = composeUserinfo("testUser", "testUser@1.com", 1420070408000L, key);
        System.out.println(r);
        String encryStr = "FeqONybLFwIJjMC%2BLYXbM73S9moeHXqYaPnuGPVTwIC4d7fXY2eMUCJIHCr0Skw7ZpPBGQpJLn8JpEckuU0GniREueVLBe5qvyH8TzhAvs0%3D";
        Map<String, String> o = analyzeUserinfo(encryStr, key);
        System.out.println(o);
    }

    /**
     * 合成userinfo串
     *
     * @param username  名
     * @param password  密码
     * @param timestamp 时间戳
     * @param key       解密密钥
     * @return
     */
    public static String composeUserinfo(String username, String password, long timestamp, String key) {
        try {
            StringBuilder userInfo = (new StringBuilder())
                    .append("username=").append(new String(Base64.encodeBase64(username.getBytes("GBK")), "GBK"))
                    .append(",password=").append(new String(Base64.encodeBase64(password.getBytes("GBK")), "GBK"))
                    .append(",timestamp=").append(timestamp);
            System.out.println(userInfo.toString());
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
     *
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
                if ("username".equals(nv[0]) || "password".equals(nv[0])) {
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
}

