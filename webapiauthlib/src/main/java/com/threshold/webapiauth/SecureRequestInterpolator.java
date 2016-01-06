package com.threshold.webapiauth;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.threshold.webapiauth.util.MD5;
import com.threshold.webapiauth.util.Util;


import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import okio.Buffer;

/**
 * Created by Threshold on 2015/12/29.
 */
public class SecureRequestInterpolator implements Interceptor {

    private ICalculateSignature iCalculateSignature;
    private ISecretRepository iSecretRepository;
    private IBuildMessageRepresentation iBuildMessageRepresentation;

    private String appKey;

    public SecureRequestInterpolator(String appKey, String appSecret) {
        this.appKey = appKey;
        //this.appSecret = appSecret;
//        Map<String, String> namePasswordRepository = new HashMap<>();
//        namePasswordRepository.put(appKey, appSecret);
        this.iBuildMessageRepresentation = new CanonicalRepresentationBuilder(appKey);
        this.iSecretRepository = new ClientSecretRepository(appKey,appSecret);
        this.iCalculateSignature = new HmacSignatureCalculator();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String secretForAppKey = iSecretRepository.getSecretForAppKey(appKey);
        String utcTime = Util.getUtcTime();
        String contentType = "";
        String contentMd5 = "";
        if (request.body() != null && request.body().contentType() != null) {
            if (!request.method().equalsIgnoreCase("GET")) {
                contentType = request.body().contentType().toString();
               // Request copyReq = request.newBuilder().build();
                Buffer buffer = new Buffer();
                try {
                    request.body().writeTo(buffer);
                    contentMd5 = MD5.getMD5(buffer.readByteArray());
                } catch (IOException | NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            String representation = iBuildMessageRepresentation.buildRequestRepresentation(
                    request.uri(), request.method(), utcTime, contentType, contentMd5);
           // LogUtils.i(representation);
            String signature = iCalculateSignature.signature(secretForAppKey, representation);
           // LogUtils.i(signature);
            Request.Builder requestBuilder = request.newBuilder();
            requestBuilder.header(Configuration.XDateHeader, utcTime)
                    .header(Configuration.AppKeyHeader, appKey)
                    .header(Configuration.ContentMd5Header, contentMd5)
                    .header(Configuration.AuthorizationHeader, String.format(Configuration.AuthorizationFormat, signature));
            return chain.proceed(requestBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chain.proceed(request);
    }
}
