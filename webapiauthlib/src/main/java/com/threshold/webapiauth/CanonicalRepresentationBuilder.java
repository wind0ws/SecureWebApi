package com.threshold.webapiauth;


import java.net.URI;

import okhttp3.HttpUrl;

/**
 * Canonical Representation
 * Created by Threshold on 2015/12/28.
 */
public class CanonicalRepresentationBuilder implements IBuildMessageRepresentation  {

    private String appKey;

    public CanonicalRepresentationBuilder(String appKey) {
        this.appKey = appKey;
    }

    @Override
    public String buildRequestRepresentation(HttpUrl url,String method,String greenwichTime, String contentType,String contentMd5) throws Exception {
        String content_Type ;
        if (method.equalsIgnoreCase("GET")) {
            content_Type = "";
        } else {
            content_Type = contentType;
        }
        String absolutePath = url.uri().getPath();
        return method + "\n" + contentMd5 + "\n" + content_Type + "\n" + greenwichTime + "\n" + appKey + "\n" + absolutePath;
    }
}
