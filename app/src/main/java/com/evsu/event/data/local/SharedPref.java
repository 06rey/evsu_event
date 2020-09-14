package com.evsu.event.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.evsu.event.App;

public class SharedPref {

    private Context context;

    public SharedPref(Context context) {
        this.context = context;
    }

    public SharedPreferences getPrefInstance() {
        return context.getSharedPreferences(App.APP_NAME, 0);
    }

    public SharedPreferences getSettingPref() {
        return context.getSharedPreferences(App.APP_SETTING, 0);
    }

}
