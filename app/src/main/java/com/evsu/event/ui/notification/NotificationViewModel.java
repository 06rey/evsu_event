package com.evsu.event.ui.notification;

import com.evsu.event.data.DataManager;
import com.evsu.event.data.repository.UserRepository;
import com.evsu.event.model.Notification;
import com.evsu.event.model.User;
import com.evsu.event.ui.BaseViewModel;
import com.evsu.event.ui.ResponseCallback;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableObserver;

public class NotificationViewModel extends BaseViewModel {

    private UserRepository userRepository;

    private MutableLiveData<List<Notification>> notifications = new MutableLiveData<>();
    private MutableLiveData<Boolean> deleteSuccess = new MutableLiveData<>();
    private MutableLiveData<Boolean> readSuccess = new MutableLiveData<>();

    public NotificationViewModel() {
        userRepository = DataManager.UserRepository();
    }

    public MutableLiveData<List<Notification>> getNotifications() {
        return notifications;
    }

    public MutableLiveData<Boolean> getDeleteSuccess() {
        return deleteSuccess;
    }

    public MutableLiveData<Boolean> getReadSuccess() {
        return readSuccess;
    }

    public User getUser() {
        return userRepository.getUser();
    }

    public void notifications(String userId, ResponseCallback<List<Notification>> callback) {
        execute(userRepository.getNotification(userId), new DisposableObserver<List<Notification>>() {
            @Override
            public void onNext(@NonNull List<Notification> notificationList) {
                notifications.setValue(notificationList);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                callback.setError(e);
            }

            @Override
            public void onComplete() {
                callback.onComplete();
            }
        });
    }

    public void readNotification(String sentId) {
        List<String> sentIds = new ArrayList<>();
        sentIds.add(sentId);

        execute(userRepository.readNotification(sentIds), new DisposableObserver<Boolean>() {
            @Override
            public void onNext(@NonNull Boolean aBoolean) {
                readSuccess.setValue(aBoolean);
            }

            @Override
            public void onError(@NonNull  Throwable e) {
                readNotification(sentId);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void deleteNotification(List<Notification> notifications, ResponseCallback<Boolean> callback) {
        execute(userRepository.deleteNotification(notifications), new DisposableObserver<Boolean>() {
            @Override
            public void onNext(@NonNull Boolean aBoolean) {
                deleteSuccess.setValue(aBoolean);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                callback.setError(e);
            }

            @Override
            public void onComplete() {
                callback.onComplete();
            }
        });
    }

}
