package com.evsu.event.ui.event;

import com.evsu.event.App;
import com.evsu.event.data.DataManager;
import com.evsu.event.data.repository.UserRepository;
import com.evsu.event.model.EvsuEvent;
import com.evsu.event.ui.BaseViewModel;
import com.evsu.event.ui.ResponseCallback;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableObserver;

public class EventViewModel extends BaseViewModel {

    private UserRepository userRepository;

    private MutableLiveData<List<EvsuEvent>> evsuEventList = new MutableLiveData<>();
    private MutableLiveData<Boolean> attendanceSuccess = new MutableLiveData<>();

    public EventViewModel() {
        userRepository = DataManager.UserRepository();
    }

    public MutableLiveData<List<EvsuEvent>> getEvsuEventList() {
        return evsuEventList;
    }

    public MutableLiveData<Boolean> getAttendanceSuccess() {
        return attendanceSuccess;
    }

    public void getStaffEvent(String eventStatus, ResponseCallback<List<EvsuEvent>> callback) {
        execute(userRepository.getStaffEvent(userRepository.getUser().getUser_id(), eventStatus), new DisposableObserver<List<EvsuEvent>>() {
            @Override
            public void onNext(@NonNull List<EvsuEvent> evsuEvents) {
                evsuEventList.setValue(evsuEvents);
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

    public void attendance(String participantId, String eventId, String mode, ResponseCallback<Boolean> callback) {
        execute(userRepository.attendance(
                App.instance().getActiveUser().getUser_id(),
                participantId,
                eventId,
                mode), new DisposableObserver<Boolean>() {
                            @Override
                            public void onNext(@NonNull Boolean aBoolean) {
                                attendanceSuccess.setValue(aBoolean);
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
