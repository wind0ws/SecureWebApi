package com.threshold.webapiauth;

import okhttp3.HttpUrl;

/**
 * Build Representation For Request
 * Created by Threshold on 2015/12/28.
 */
public interface IBuildMessageRepresentation {
    String buildRequestRepresentation(HttpUrl url, String method, String greenwichTime, String contentType, String contentMd5) throws Exception;
}
