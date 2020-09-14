package com.evsu.event.ui;

import androidx.lifecycle.ViewModel;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public abstract class BaseViewModel extends ViewModel {

    protected CompositeDisposable disposables;

    public BaseViewModel() {
        disposables = new CompositeDisposable();
    }

    protected <T> void execute(Observable<T> observable, DisposableObserver<T> observer) {
        Observable<T> newObservable = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        disposables.add(newObservable.subscribeWith(observer));
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        if (!disposables.isDisposed()) {
            disposables.dispose();
        }
    }
}
