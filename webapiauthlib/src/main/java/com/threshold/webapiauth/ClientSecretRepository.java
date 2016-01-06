package com.threshold.webapiauth;



import com.threshold.webapiauth.util.SHA1;
import com.threshold.webapiauth.util.TextUtils;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Threshold on 2015/12/28.
 */
public class ClientSecretRepository implements ISecretRepository {

    private Map<String, String> appKeySecret;

    private ClientSecretRepository(Map<String, String> appKeySecret) {
        this.appKeySecret = appKeySecret;
    }

    private String appKey;

    public ClientSecretRepository(String appKey,String appSecret) {
        this.appKey=appKey;
        Map<String, String> appKeySecret = new HashMap<>();
        appKeySecret.put(appKey,appSecret);
        this.appKeySecret =appKeySecret;
    }

    public String getAppKey() {
        return appKey;
    }

    @Override
    public String getSecretForAppKey(String appKey) {
        String password = appKeySecret.get(appKey);
        if (TextUtils.isEmpty(password)) {
          //  LogUtils.w("Can't Get Your Password for " + username);
            return "";
        }
        return password;//this password is secret
//        try {
//            return SHA1.calculateSecret(password);
//        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return "";
    }
}
