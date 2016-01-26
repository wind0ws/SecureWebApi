package com.threshold.webapiauth;

/**
 * Provide Secret For AppKey
 * Created by Threshold on 2015/12/28.
 */
public interface ISecretRepository {
    String getSecretForAppKey(String appKey);
}
