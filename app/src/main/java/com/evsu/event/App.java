package com.evsu.event;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.evsu.event.data.DataManager;
import com.evsu.event.model.User;
import com.evsu.event.service.NetworkReceiver;
import com.evsu.event.service.NotificationService;
import com.evsu.event.util.AppUtil;

import java.util.ArrayList;
import java.util.List;

public class App extends Application {

    public static final String APP_NAME = "APP_NAME";
    public static final String APP_SETTING = "APP_SETTING";

    private static App instance;
    private Intent notificationIntent;

    private final String NOTIFICATION_SERVICE = "com.evsu.event.service.NotificationService";

    private List<OnLogoutListener> logoutListenerList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        registerReceiver(new NetworkReceiver(), new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));

        notificationIntent = new Intent(this, NotificationService.class);
        startNotificationService();
    }

    public static App instance() {
        return instance;
    }

    public void startNotificationService() {
        if (hasActiveUser()) {
            String userLevel = getActiveUser().getUser_level().toLowerCase();
            if (userLevel.equals("student") || userLevel.equals("organization president")) {
                if (!AppUtil.isServiceRunning(NOTIFICATION_SERVICE, this)) {
                    startService(notificationIntent);
                    Log.d("ddd", "Application notification service started.");
                } else {
                    Log.d("ddd", "Application notification service already running.");
                }
            }
        }
    }

    public boolean hasActiveUser() {
        return DataManager.UserRepository().getUser() != null;
    }

    public User getActiveUser() {
        return DataManager.UserRepository().getUser();
    }

    public void stopNotificationService() {
        if (AppUtil.isServiceRunning(NOTIFICATION_SERVICE, this)) {
            stopService(notificationIntent);
        }
    }

    public void logout() {
        DataManager.UserRepository().logout();
        stopNotificationService();

        for (OnLogoutListener listener: logoutListenerList) {
            listener.onLogout();
        }

        logoutListenerList.clear();
    }

    public void addLogoutListener(OnLogoutListener logoutListener) {
        logoutListenerList.add(logoutListener);
    }

    public void removeLogoutListener(OnLogoutListener logoutListener) {
        while (logoutListenerList.remove(logoutListener)) {}
    }

    public interface OnLogoutListener {
        void onLogout();
    }

    // Setting
    public boolean setHostname(String host) {
        return DataManager.AppRepository().setHostname(host);
    }

    public String getHostname() {
        return DataManager.AppRepository().getHostname();
    }

    public boolean setDisplayErrorDialog(boolean bool) {
        return DataManager.AppRepository().setDebugError(bool);
    }

    public boolean getDisplayErrorDialog() {
        return DataManager.AppRepository().getDebugError();
    }

    public String getLoggedResponseBody() {
        return DataManager.AppRepository().getResponseBody();
    }
}
