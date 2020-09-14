package com.evsu.event.data.remote.retrofit_api;

import com.evsu.event.model.Response;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface EventApi {

    @GET("api/staffEvent/{userId}/{status}")
    Observable<Response> getStaff(@Path("userId") String userId, @Path("status") String eventStatus);

    @POST("api/attendance")
    Observable<Response> attendance(@Body RequestBody body);

}
