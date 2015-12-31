package com.threshold.hmacauth.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Threshold on 2015/12/28.
 */
public class MD5 {

    private static final String UTF8 = "UTF-8";

    /**
     * 这个方法生成的Md5 String可以与.Net生成的Md5（byte[]）用BitConverter转换出来的一致!
     * 注意在.Net中取Content-MD5的String用Convert.Base64String(byte[])来获取，获得的即是本方法的值
     * @param str 内容
     * @param encoding 编码方式
     * @return
     * @throws Exception
     */
    public static String getMD5(String str, String encoding) throws Exception {
        return getMD5(str.getBytes(encoding));
    }

    public static String getMD5(byte[] bytes) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(bytes);
        byte[] md5Bytes = md.digest();
        StringBuilder sb = new StringBuilder(32);
        for (byte aMd5Byte : md5Bytes) {
            int val = aMd5Byte & 0xff;
            if (val < 0xf) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(val));
        }
        return sb.toString().toUpperCase();
    }


    public static String getMD5(String content) throws Exception {
        return getMD5(content, UTF8);
    }

}
