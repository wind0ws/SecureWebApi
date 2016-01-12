package com.threshold.webapiauth.api;

import com.threshold.webapiauth.model.FileModel;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import rx.Observable;

/**
 * Created by Threshold on 2016/1/12.
 */
public interface IFileUpDownApi {

    @Streaming  //For downloading,You must annotation as Streaming and return ResponseBody
    @GET("api/fileupdown")
    Observable<ResponseBody> downloadFile(@Query("filename")String fileName);


    @Multipart
    @POST("api/fileupdown")
    Observable<List<FileModel>> uploadFile(@PartMap() Map<String,RequestBody> mapFileAndName);

}
