package com.threshold.webapiauth.api;

import com.threshold.webapiauth.model.Product;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Threshold on 2016/1/12.
 */
public interface IProductsApi {

    @GET("api/products")
    Observable<List<Product>> getProducts();

    @GET("api/products/{id}")
    Observable<Product> getProduct(@Path("id")int id);

    @POST("api/products")
    Observable<Product> addProduct(@Body Product product);

}
