package com.threshold.webapiauth.api;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.threshold.webapiauth.BuildConfig;
import com.threshold.webapiauth.Constant;
import com.threshold.webapiauth.SecureRequestInterpolator;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;
import timber.log.Timber;

/**
 * Created by Threshold on 2016/1/12.
 */
public class ApiClient {

    private Retrofit retrofit;
    private IProductsApi iProductsApi;
    private IFileUpDownApi iFileUpDownApi;

    private String baseUrl;
    OkHttpClient client;
    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();

    public ApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
        initOkClient();
        initRetrofit();
    }

    private void initOkClient() {
        HttpLoggingInterceptor.Logger logger=new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Timber.d(message);
            }
        };
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(logger);
        if (BuildConfig.DEBUG) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        }
        SecureRequestInterpolator interpolator = new SecureRequestInterpolator(Constant.APP_KEY, Constant.APP_SECRET);//this is the key and password shared between server and client
        client = new OkHttpClient.Builder()
                .addInterceptor(interpolator)
                .addInterceptor(loggingInterceptor)
                .build();
    }

    private void initRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
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

    public IProductsApi getProductsApi() {
        if (iProductsApi == null) {
            iProductsApi = retrofit.create(IProductsApi.class);
        }
        return iProductsApi;
    }

    public IFileUpDownApi getFileUpDownApi() {
        if (iFileUpDownApi == null) {
            iFileUpDownApi = retrofit.create(IFileUpDownApi.class);
        }
        return iFileUpDownApi;
    }


}
