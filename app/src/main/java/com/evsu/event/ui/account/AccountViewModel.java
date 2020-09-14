package com.evsu.event.ui.account;

import com.evsu.event.App;
import com.evsu.event.data.DataManager;
import com.evsu.event.data.repository.UserRepository;
import com.evsu.event.model.User;
import com.evsu.event.ui.BaseViewModel;
import com.evsu.event.ui.ResponseCallback;

import androidx.lifecycle.MutableLiveData;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableObserver;

public class AccountViewModel extends BaseViewModel {

    private UserRepository userRepository;

    private MutableLiveData<Void> changePasswordSuccess = new MutableLiveData<>();
    private MutableLiveData<Void> profileUpdateSuccess = new MutableLiveData<>();

    public AccountViewModel() {
        userRepository = DataManager.UserRepository();
    }

    public MutableLiveData<Void> getChangePasswordSuccess() {
        return changePasswordSuccess;
    }

    public MutableLiveData<Void> getProfileUpdateSuccess() {
        return profileUpdateSuccess;
    }

    public User getUser() {
        return userRepository.getUser();
    }

    public void setUser(User user) {
        userRepository.setUser(user);
    }

    public void changePassword(String currentPassword, String newPassword, String userId, ResponseCallback<Boolean> response) {
        execute(userRepository.changePassword(currentPassword, newPassword, userId), new DisposableObserver<Boolean>() {
            @Override
            public void onNext(@NonNull Boolean aBoolean) {
                changePasswordSuccess.setValue(null);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                response.setError(e);
            }

            @Override
            public void onComplete() {
                response.onComplete();
            }
        });
    }

    public void updateProfile(String name, String value, String profileId, ResponseCallback<Boolean> response) {
        execute(userRepository.updateProfile(name, value, profileId), new DisposableObserver<Boolean>() {
            @Override
            public void onNext(@NonNull Boolean aBoolean) {
                profileUpdateSuccess.setValue(null);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                response.setError(e);
            }

            @Override
            public void onComplete() {
                response.onComplete();
            }
        });
    }

    public void logout() {
        App.instance().logout();
    }
}
