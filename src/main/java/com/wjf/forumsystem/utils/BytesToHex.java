package com.wjf.forumsystem.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class BytesToHex {
    // 将字节数组转换为十六进制字符串
    public static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
    public static String getHash(String password){
        String hashPassword=null;
        try {
            // 创建一个 MessageDigest 实例，指定算法为 SHA-256
            MessageDigest messageDigest= MessageDigest.getInstance("SHA-256");
            // 获取输入字符串的字节数组
            byte[] hash=messageDigest.digest(password.getBytes());
            // 将字节数组转换为十六进制字符串
            hashPassword= BytesToHex.bytesToHex(hash);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return hashPassword;
    }
}
