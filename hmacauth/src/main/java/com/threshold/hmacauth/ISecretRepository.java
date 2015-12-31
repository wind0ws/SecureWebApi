package com.threshold.hmacauth;

/**
 * Created by Threshold on 2015/12/28.
 */
public interface ISecretRepository {
    String getSecretForUser(String username);
}
