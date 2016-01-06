package com.threshold.webapiauth.util;


import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Threshold on 2015/12/28.
 */
public class HMAC {

    private final static String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    private final static String HMAC_SHA256_ALGORITHM = "HmacSHA256";

    private static Map<String,SecretKeySpec> signingKeyMap;

    static {
        signingKeyMap = new HashMap<>();
    }

    public static String calculateHMAC(String secret, String data) throws Exception {
        SecretKeySpec signingKey;
        if (signingKeyMap.containsKey(secret)) {
            signingKey= signingKeyMap.get(secret);
        } else {
            signingKey = new SecretKeySpec(secret.getBytes("UTF-8"),
                HMAC_SHA256_ALGORITHM);
            signingKeyMap.put(secret, signingKey);
        }
//        SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes("UTF-8"),
//                HMAC_SHA256_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(data.getBytes("UTF-8"));
        return Base64.encodeToString(rawHmac, Base64.NO_WRAP);
    }
}
