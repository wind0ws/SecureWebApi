package com.threshold.webapiauth.api;

import com.threshold.webapiauth.Constant;
import com.threshold.webapiauth.SecureRequestInterpolator;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;

/**
 * Created by Threshold on 2016/1/12.
 */
public class FileUpDownClient {

    private Retrofit retrofit;

    private String baseUrl;
    private OkHttpClient okHttpClient;
    private IFileUpDownApi iFileUpDownApi;

    public FileUpDownClient(String baseUrl) {
        this.baseUrl = baseUrl;
        initOkClient();
        initRetrofit();
    }

    private void initRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    private void initOkClient() {
        SecureRequestInterpolator interpolator = new SecureRequestInterpolator(Constant.APP_KEY, Constant.APP_SECRET);//this is the key and password shared between server and client
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interpolator)
                .build();
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        if (!this.baseUrl.equals(baseUrl)) {
            this.baseUrl = baseUrl;
            initRetrofit();
        }
    }

    public IFileUpDownApi getFileUpDownApi() {
        if (iFileUpDownApi==null) {
            iFileUpDownApi = retrofit.create(IFileUpDownApi.class);
        }
        return iFileUpDownApi;
    }
}
