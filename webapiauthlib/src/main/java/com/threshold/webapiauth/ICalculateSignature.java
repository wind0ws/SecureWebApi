package com.threshold.webapiauth;

/**
 * Created by Threshold on 2015/12/28.
 */
public interface ICalculateSignature {
    String signature(String secret, String value);
}
