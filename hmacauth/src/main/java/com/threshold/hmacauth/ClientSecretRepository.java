package com.threshold.hmacauth;

import android.text.TextUtils;

import com.threshold.hmacauth.util.LogUtils;
import com.threshold.hmacauth.util.SHA1;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * Created by Threshold on 2015/12/28.
 */
public class ClientSecretRepository implements ISecretRepository {

    private Map<String, String> userPasswordMap;

    public ClientSecretRepository(Map<String, String> userPasswordMap) {
        this.userPasswordMap = userPasswordMap;
    }

    @Override
    public String getSecretForUser(String username) {
        String password = userPasswordMap.get(username);
        if (TextUtils.isEmpty(password)) {
            LogUtils.w("Can't Get Your Password for " + username);
            return "";
        }
        try {
            return SHA1.calculateSecret(password);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
