package com.evsu.event.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.evsu.event.App;
import com.evsu.event.data.DataManager;
import com.evsu.event.util.AppUtil;

public class NetworkReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("ddd", "NetworkReceiver emitted");

        if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
            App.instance().startNotificationService();
        }
    }

}
