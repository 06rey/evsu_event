package com.evsu.event.data.repository;

import com.evsu.event.data.local.SharedPref;
import com.evsu.event.data.remote.RemoteService;
import com.evsu.event.model.Response;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Function;


public class AppRepository {

    private SharedPref sharedPref;
    private final static String SETTING_HOSTNAME = "host_name";
    private final static String SETTING_DEBUG_ERROR_DIALOG = "SETTING_DEBUG_ERROR_DIALOG";

    public AppRepository(SharedPref sharedPref) {
        this.sharedPref = sharedPref;
    }

    public boolean setHostname(String hostname) {
        return sharedPref.getSettingPref()
                .edit()
                .putString(SETTING_HOSTNAME, hostname)
                .commit();
    }

    public String getHostname() {
        String url = sharedPref.getSettingPref()
                .getString(SETTING_HOSTNAME, "");

        return url.equals("") ? "http://127.0.0.1" : url;
    }

    public boolean setDebugError(boolean bool) {
        return sharedPref.getSettingPref()
                .edit()
                .putBoolean(SETTING_DEBUG_ERROR_DIALOG, bool)
                .commit();
    }

    public boolean getDebugError() {
        return sharedPref.getSettingPref()
                .getBoolean(SETTING_DEBUG_ERROR_DIALOG, false);
    }

    public Observable<Boolean> ping() {
        return RemoteService.instance()
                .application()
                .pingServer()
                .map(new Function<Response, Boolean>() {
                    @Override
                    public Boolean apply(Response response) throws Throwable {
                        return true;
                    }
                })
                .retry(3);
    }

    public void logResponseBody(String responseBody) {
        sharedPref.getSettingPref().edit()
                .putString("response_body", responseBody)
                .apply();
    }

    public String getResponseBody() {
        return sharedPref.getSettingPref()
                .getString("response_body", "No logged response body");
    }
}
