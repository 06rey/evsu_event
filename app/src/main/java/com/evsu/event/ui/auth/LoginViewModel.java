package com.evsu.event.ui.auth;

import com.evsu.event.data.DataManager;
import com.evsu.event.data.repository.UserRepository;
import com.evsu.event.ui.BaseViewModel;
import com.evsu.event.ui.ResponseCallback;

import androidx.lifecycle.MutableLiveData;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableObserver;

public class LoginViewModel extends BaseViewModel {

    private UserRepository userRepository;

    private MutableLiveData<Void> loginSuccess = new MutableLiveData<>();

    public LoginViewModel() {
        userRepository = DataManager.UserRepository();
    }

    public MutableLiveData<Void> getLoginSuccess() {
        return loginSuccess;
    }

    public void login(String username, String password, ResponseCallback<Boolean> response) {
        execute(userRepository.login(username, password), new DisposableObserver<Boolean>() {
            @Override
            public void onNext(@NonNull Boolean aVoid) {
                loginSuccess.setValue(null);
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
}
