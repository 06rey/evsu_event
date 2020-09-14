package com.evsu.event.data.remote;

import com.evsu.event.data.repository.AppRepository;

import java.io.IOException;
import okhttp3.Response;

public class ResponseInterceptor implements okhttp3.Interceptor {

    private AppRepository appRepository;

    public ResponseInterceptor(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());

        assert response.body() != null;
        appRepository.logResponseBody(response.peekBody(response.body().contentLength()).string());

        return response;
    }
}
