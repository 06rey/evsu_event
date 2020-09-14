package com.evsu.event.data.remote.retrofit_api;

import com.evsu.event.model.Response;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

public interface ApplicationApi {

    @GET("api/pingServer")
    Observable<Response> pingServer();

}
