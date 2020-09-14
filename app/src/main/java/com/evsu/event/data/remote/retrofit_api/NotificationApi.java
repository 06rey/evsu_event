package com.evsu.event.data.remote.retrofit_api;

import com.evsu.event.model.Response;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface NotificationApi {

    @GET("api/notification/{ref}")
    Observable<Response> get(@Path("ref") String refId);

    @POST("api/deleteNotification")
    Observable<Response> delete(@Body RequestBody requestBody);

    @GET("api/countUnreadNotification/{ref}")
    Observable<Response> countUnread(@Path("ref") String refId);

    @GET("api/newNotification/{ref}")
    Observable<Response> getNew(@Path("ref") String refId);

    @POST("api/markRead")
    Observable<Response> read(@Body RequestBody requestBody);

    @POST("api/markReceive")
    Observable<Response> receive(@Body RequestBody requestBody);

}
