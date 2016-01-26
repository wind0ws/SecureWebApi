package com.threshold.webapiauth;

/**
 * Constant Value For WebApi.
 * DO NOT CHANGE VALUE IF YOU HAVEN'T CONFIG SERVER.
 * Created by Threshold on 2015/12/28.
 */
public class Configuration {
//    public static final String AppKeyHeader ="X-ApiAuth-AppKey";
    public static final String DateHeader = "Date";
    public static final String XDateHeader = "X-Date";
    public static final String ContentMd5Header = "Content-MD5";
    public static final String AuthorizationHeader = "Authorization";
    public static final String AuthenticationScheme = "ApiAuth";
    public static final String AuthorizationValueFormat = "%s:%s";
    public static final String AuthenticationFormat = AuthenticationScheme+" %s";
    public static final String XDateFormat = "yyyy/MM/dd HH:mm:ss";
//    public static final int  ValidityPeriodInMinutes = 5;
}
