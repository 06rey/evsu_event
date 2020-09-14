package com.evsu.event.ui;

import android.util.Log;

import retrofit2.HttpException;

public abstract class ResponseCallback<T> {

    public void onSuccess(T data) {}
    public void onComplete() {}
    public void onNotFound() {}
    public void onForbidden(Throwable e) {}
    public void onBadRequest(Throwable e) {}
    public void onServerError(Throwable e) {}
    public void onServerUnavailable(Throwable e) {}
    public void onFailed(Throwable e) {}
    public void onError(Throwable e) {}

    public void setError(Throwable e) {
        Log.d("ddd", "Http error: "+ e.getMessage());
        e.printStackTrace();
        onError(e);

        if (e instanceof HttpException) {

            HttpException httpException = (HttpException)e;

            if (httpException.code() == 400) {
                onBadRequest(e);
            }else if (httpException.code() == 403) {
                onForbidden(e);
            }else if (httpException.code() == 404) {
                onNotFound();
            }else if (httpException.code() == 500) {
                onServerError(e);
            }else if (httpException.code() == 503) {
                onServerUnavailable(e);
            }else{
                onFailed(e);
            }
        }else{
            onFailed(e);
        }

        onComplete();
    }

}
