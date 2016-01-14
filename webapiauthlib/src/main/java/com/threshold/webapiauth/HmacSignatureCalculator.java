package com.threshold.webapiauth;

import com.threshold.webapiauth.util.HMAC;
import com.threshold.webapiauth.util.TextUtils;

/**
 * Calculate Signature For WebApi
 * Created by Threshold on 2015/12/28.
 */
public class HmacSignatureCalculator implements ICalculateSignature {

    @Override
    public String signature(String secret, String value) {
        if (TextUtils.isEmpty(secret) || TextUtils.isEmpty(value)) {
            return "";
        }
        try {
            return HMAC.calculateHMAC(secret, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
