package com.evsu.event.ui.home;

import com.evsu.event.data.DataManager;
import com.evsu.event.data.repository.UserRepository;
import com.evsu.event.model.User;
import com.evsu.event.ui.BaseViewModel;

import androidx.lifecycle.MutableLiveData;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class HomeViewModel extends BaseViewModel {

    private UserRepository userRepository;

    private MutableLiveData<Integer> unreadNotificationCount = new MutableLiveData<>();


    public HomeViewModel() {
        userRepository = DataManager.UserRepository();
    }

    public MutableLiveData<Integer> getUnreadNotificationCount() {
        return unreadNotificationCount;
    }

    public User getUser() {
        return userRepository.getUser();
    }

    public void countUnreadNotification(String userId) {
        userRepository.pollingCountUnreadNotification(new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposables.add(d);
            }

            @Override
            public void onNext(@NonNull Integer integer) {
                unreadNotificationCount.setValue(integer);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {}
        }, userId);
    }

}
