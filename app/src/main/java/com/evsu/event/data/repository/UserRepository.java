package com.evsu.event.data.repository;

import com.evsu.event.data.local.SharedPref;
import com.evsu.event.data.remote.RemoteService;
import com.evsu.event.model.EvsuEvent;
import com.evsu.event.model.Notification;
import com.evsu.event.model.Response;
import com.evsu.event.model.User;
import com.google.gson.Gson;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UserRepository {

    private SharedPref sharedPref;

    public UserRepository(SharedPref sharedPref) {
        this.sharedPref = sharedPref;
    }

    // User account

    public Observable<Boolean> login(String username, String password) {
        return RemoteService.instance().login(username, password)
                .map(new Function<Response, Boolean>() {
                    @Override
                    public Boolean apply(Response response) throws Throwable {
                        setUser(response.getData().getUser());
                        return true;
                    }
                });
    }

    public void setUser(User user) {
        user.setFullName();
        sharedPref.getPrefInstance()
                .edit()
                .putString("user", new Gson().toJson(user))
                .apply();
    }

    public User getUser() {
        String userJson = sharedPref.getPrefInstance().getString("user", null);
        return userJson == null ? null : new Gson().fromJson(userJson, User.class);
    }

    public Observable<Boolean> changePassword(String currentPassword, String newPassword, String userId) {
        return RemoteService.instance().changePassword(currentPassword, newPassword, userId)
                .map(new Function<Response, Boolean>() {
                    @Override
                    public Boolean apply(Response response) throws Throwable {
                        return true;
                    }
                });
    }

    public Observable<Boolean> updateProfile(String name, String value, String profileId) {
        return RemoteService.instance().updateProfile(name, value, profileId)
                .map(new Function<Response, Boolean>() {
                    @Override
                    public Boolean apply(Response response) throws Throwable {
                        return true;
                    }
                });
    }

    public void logout() {
        sharedPref.getPrefInstance().edit()
                .remove("user")
                .apply();
    }

    // Notification

    public Observable<List<Notification>> getNotification(String refId) {
        return RemoteService.instance().notification().get(refId)
                .map(new Function<Response, List<Notification>>() {
                    @Override
                    public List<Notification> apply(Response response) throws Throwable {
                        return response.getData().getNotifications();
                    }
                });
    }

    public Observable<List<Notification>> getNewNotification(String refId) {
        return RemoteService.instance().notification().getNew(refId)
                .map(new Function<Response, List<Notification>>() {
                    @Override
                    public List<Notification> apply(Response response) throws Throwable {
                        return response.getData().getNotifications();
                    }
                });
    }

    public Observable<Integer> countUnreadNotification(String refId) {
        return RemoteService.instance().notification().countUnread(refId)
                .map(new Function<Response, Integer>() {
                    @Override
                    public Integer apply(Response response) throws Throwable {
                        return response.getMeta().getData_count();
                    }
                });
    }

    public Observable<Boolean> deleteNotification(List<Notification> notifications) {
        return RemoteService.instance().deleteNotification(notifications)
                .map(new Function<Response, Boolean>() {
                    @Override
                    public Boolean apply(Response response) throws Throwable {
                        return true;
                    }
                });
    }

    public Observable<Boolean> readNotification(List<String> sentIds) {
        return RemoteService.instance().readNotification(sentIds)
                .map(new Function<Response, Boolean>() {
                    @Override
                    public Boolean apply(Response response) throws Throwable {
                        return true;
                    }
                });
    }

    public Observable<List<String>> receiveNotification(List<Notification> notificationList) {
        return RemoteService.instance().receivedNotification(notificationList)
                .map(new Function<Response, List<String>>() {
                    @Override
                    public List<String> apply(Response response) throws Throwable {
                        return response.getData().getReceive_id();
                    }
                });
    }

    // Event
    public Observable<List<EvsuEvent>> getStaffEvent(String userId, String eventStatus) {
        return RemoteService.instance().event().getStaff(userId, eventStatus)
                .map(new Function<Response, List<EvsuEvent>> () {
                    @Override
                    public List<EvsuEvent> apply(Response response) throws Throwable {
                        return response.getData().getEvsuEvents();
                    }
                });
    }

    public Observable<Boolean> attendance(String userId, String participantId, String eventId, String mode) {
        return RemoteService.instance().attendance(userId, participantId, eventId, mode)
                .map(new Function<Response, Boolean> () {
                    @Override
                    public Boolean apply(Response response) throws Throwable {
                        return true;
                    }
                });
    }

    // Server polling

    public void pollingCountUnreadNotification(Observer<Integer> observer, String refId) {
        Observable.interval(5, TimeUnit.SECONDS, Schedulers.io())
                .flatMap(new Function<Long, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> apply(Long aLong) throws Throwable {
                        return countUnreadNotification(refId);
                    }
                })
                .retry()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void pollingNewNotification(Observer<List<Notification>> observer, String refId) {
        Observable.interval(5, TimeUnit.SECONDS, Schedulers.io())
                .flatMap(new Function<Long, Observable<List<Notification>>>() {
                    @Override
                    public Observable<List<Notification>> apply(Long aLong) throws Throwable {
                        return getNewNotification(refId);
                    }
                })
                .retry()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

}
