package com.evsu.event.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import com.evsu.event.App;
import com.evsu.event.R;
import com.evsu.event.data.DataManager;
import com.evsu.event.data.repository.UserRepository;
import com.evsu.event.model.Notification;

import java.util.ArrayList;
import java.util.List;

import androidx.core.app.NotificationCompat;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NotificationService extends Service {

    private Intent intent;
    private UserRepository userRepository;
    private CompositeDisposable disposables;

    public static final String NEW_NOTIFICATION_BROADCASTER = "NEW_NOTIFICATION";

    private List<String> notificationIds = new ArrayList<>();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        intent = new Intent(this, App.class);
        userRepository = DataManager.UserRepository();
        disposables = new CompositeDisposable();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        fetchNewNotification();

        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        restartService();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        restartService();
    }

    private void restartService() {
        disposeObserver();
        App.instance().startNotificationService();
    }

    private void disposeObserver() {
        if (!disposables.isDisposed()) {
            disposables.dispose();
        }
    }

    private void fetchNewNotification() {
        userRepository.pollingNewNotification(new Observer<List<Notification>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposables.add(d);
            }

            @Override
            public void onNext(@NonNull List<Notification> notificationList) {
                receiveNotification(notificationList);
                for (Notification notification: notificationList) {
                    if (!notificationIds.contains(notification.getSent_id())) {
                        pushNotification(notification);
                        // Broadcast new notification item
                        sendBroadcast(
                                new Intent(NEW_NOTIFICATION_BROADCASTER)
                                        .putExtra("notification", notification)
                        );

                        notificationIds.add(notification.getSent_id());
                    }
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("EVSU_EVENT", e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onComplete() { }

        }, userRepository.getUser().user_id);
    }

    private void receiveNotification(List<Notification> notificationList) {
        if (notificationList.isEmpty()) {
            return;
        }

        execute(userRepository.receiveNotification(notificationList), new Observer<List<String>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposables.add(d);
            }

            @Override
            public void onNext(@NonNull List<String> ids) {
                for (String id: ids) {
                    while (notificationIds.remove(id)) {}
                }
            }

            @Override
            public void onError(@NonNull  Throwable e) { }

            @Override
            public void onComplete() { }
        });
    }

    private void execute(Observable<List<String>> observable, Observer<List<String>> observer) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private void pushNotification(Notification notification) {

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this , "default" )
            .setContentTitle(notification.getSubject())
            .setContentText(notification.getBody())
            .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_notification)
            .setPriority(android.app.Notification.PRIORITY_HIGH)
            .setDefaults(android.app.Notification.FLAG_AUTO_CANCEL | android.app.Notification.DEFAULT_LIGHTS | android.app.Notification.DEFAULT_VIBRATE | android.app.Notification.DEFAULT_SOUND)
            .setSound(alarmSound)
            .setContentIntent(contentIntent);

        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
            int importance = NotificationManager. IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel("EVENT_NOTIFICATION", "NEW_EVENT" , importance) ;
            mBuilder.setChannelId("EVENT_NOTIFICATION");
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(notificationChannel);
        }

        assert mNotificationManager != null;
        mNotificationManager.notify(( int ) System.currentTimeMillis() , mBuilder.build());
    }

}
