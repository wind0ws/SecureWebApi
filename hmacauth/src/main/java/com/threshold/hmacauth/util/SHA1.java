package com.threshold.hmacauth.util;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Threshold on 2015/12/28.
 */
public class SHA1 {

    private static final String SHA1 = "SHA-1";
    private static final String UTF8 = "UTF-8";

    private static Map<String,String> secretMap;

    static {
        secretMap = new HashMap<>();
    }

    public static String calculateSecret(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        if (secretMap.containsKey(input)) {
            return secretMap.get(input);
        }
        MessageDigest digest = MessageDigest.getInstance(SHA1);
        digest.update(input.getBytes(UTF8));
        byte[] hash = digest.digest();
        String base64hash = Base64.encodeToString(hash, Base64.NO_WRAP);
        secretMap.put(input, base64hash);
        //System.out.println("secret: " + base64hash);
        return base64hash;
    }
}
