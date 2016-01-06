package com.threshold.webapiauth;


import java.net.URI;

/**
 * Created by Threshold on 2015/12/28.
 */
public interface IBuildMessageRepresentation {
    String buildRequestRepresentation(URI uri, String method, String utcTime, String contentType, String contentMd5) throws Exception;
}
