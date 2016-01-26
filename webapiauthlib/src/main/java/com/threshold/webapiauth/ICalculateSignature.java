package com.threshold.webapiauth;

/**
 * Calculate Signature from Representation String and add signature to Http authorization header
 * Created by Threshold on 2015/12/28.
 */
public interface ICalculateSignature {
    String signature(String secret, String value);
}
