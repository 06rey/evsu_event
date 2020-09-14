package com.evsu.event.data.remote;

import com.evsu.event.data.remote.retrofit_api.AccountApi;
import com.evsu.event.data.remote.retrofit_api.ApplicationApi;
import com.evsu.event.data.remote.retrofit_api.EventApi;
import com.evsu.event.data.remote.retrofit_api.NotificationApi;
import com.evsu.event.model.Notification;
import com.evsu.event.model.Response;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RemoteService {

    private static RemoteService instance;
    private HttpClient httpClient;

    private RemoteService() {
        httpClient = new HttpClient();
    }

    public static RemoteService instance() {
        if (instance == null) {
            instance = new RemoteService();
        }

        return instance;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    // Application
    public ApplicationApi application() {
        return httpClient.retrofit().create(ApplicationApi.class);
    }

    // Acount

    public AccountApi account() {
        return httpClient.retrofit().create(AccountApi.class);
    }

    public Observable<Response> login(String username, String password) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("username", username)
                .addFormDataPart("password", password)
                .build();

        return account().login(requestBody);
    }

    public Observable<Response> changePassword(String currentPassword, String newPassword, String userId) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("current_password", currentPassword)
                .addFormDataPart("new_password", newPassword)
                .addFormDataPart("user_id", userId)
                .build();

        return account().changePassword(requestBody);
    }

    public Observable<Response> updateProfile(String name, String newValue, String profileId) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("column", name)
                .addFormDataPart("value", newValue)
                .addFormDataPart("profile_id", profileId)
                .build();

        return account().updateProfile(requestBody);
    }

    // Notification

    public NotificationApi notification() {
        return httpClient.retrofit().create(NotificationApi.class);
    }

    public Observable<Response> deleteNotification(List<Notification> notifications) {
        MultipartBody.Builder body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        for (Notification notification: notifications) {
            body.addFormDataPart(notification.getSent_id(), notification.getSent_id());
        }

        return notification().delete(body.build());
    }

    public Observable<Response> readNotification(List<String> sentIds) {
        MultipartBody.Builder body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        for (String id: sentIds) {
            body.addFormDataPart(id, id);
        }

        return notification().read(body.build());
    }

    public Observable<Response> receivedNotification(List<Notification> notificationList) {
        MultipartBody.Builder body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        for (Notification notification: notificationList) {
            body.addFormDataPart(notification.getSent_id(), notification.getSent_id());
        }

        return notification().receive(body.build());
    }

    // Event

    public EventApi event() {
        return httpClient.retrofit().create(EventApi.class);
    }

    public Observable<Response> attendance(String userId, String participantId, String eventId, String mode) {
        MultipartBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", userId)
                .addFormDataPart("participant_id", participantId)
                .addFormDataPart("event_id", eventId)
                .addFormDataPart("mode", mode)
                .build();

        return event().attendance(body);
    }

}
