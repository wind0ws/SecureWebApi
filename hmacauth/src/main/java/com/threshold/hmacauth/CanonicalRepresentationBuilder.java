package com.threshold.hmacauth;


import java.net.URI;

/**
 * Canonical Representation
 * Created by Threshold on 2015/12/28.
 */
public class CanonicalRepresentationBuilder implements IBuildMessageRepresentation  {

    private String userName;

    public CanonicalRepresentationBuilder(String userName) {
        this.userName = userName;
    }

    @Override
    public String buildRequestRepresentation(URI uri,String method,String utcTime, String contentType,String contentMd5) throws Exception {
        String content_Type ;
        if (method.equalsIgnoreCase("GET")) {
            content_Type = "";
        } else {
            content_Type = contentType;
        }
        String absolutePath = uri.getPath();
        return method + "\n" + contentMd5 + "\n" + content_Type + "\n" + utcTime+ "\n" + userName + "\n" + absolutePath;
    }
}
