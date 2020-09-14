package com.evsu.event.data.remote.retrofit_api;

import com.evsu.event.model.Response;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AccountApi {

    // Account

    @POST("api/auth")
    Observable<Response> login(@Body RequestBody requestBody);

    @POST("api/password")
    Observable<Response> changePassword(@Body RequestBody requestBody);

    @POST("api/profile")
    Observable<Response> updateProfile(@Body RequestBody requestBody);
}
