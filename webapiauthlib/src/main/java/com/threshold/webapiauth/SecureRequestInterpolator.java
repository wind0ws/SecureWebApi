package com.threshold.webapiauth;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import com.threshold.webapiauth.util.Base64;
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
        String secretForAppKey = iSecretRepository.getSecretForAppKey(appKey);
        String greenwichTime = Util.getGreenwichDate(); //Util.getUtcTime();
        String contentType = "";
        String contentMd5 = "";
        Request request = chain.request();
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
                    request.url(), request.method(), greenwichTime, contentType, contentMd5);
           // LogUtils.i(representation);
            String signature = iCalculateSignature.signature(secretForAppKey, representation);
            String auth= Base64.encodeToString(
                    String.format(Configuration.AuthorizationValueFormat, appKey, signature).getBytes(Util.UTF8), Base64.NO_WRAP);
            String authorization =String.format(Configuration.AuthenticationFormat,auth);
           // LogUtils.i(signature);
            Request.Builder requestBuilder = request.newBuilder();

            requestBuilder.header(Configuration.DateHeader,greenwichTime)
                    /*.header(Configuration.XDateHeader, greenwichTime)*/
                    /*.header(Configuration.AppKeyHeader, appKey)*/
                    .header(Configuration.ContentMd5Header, contentMd5)
                    .header(Configuration.AuthorizationHeader, authorization);
            return chain.proceed(requestBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chain.proceed(request);
    }
}
