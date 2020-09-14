package com.evsu.event.ui;

public interface EventCallback {

    void onEmptyResult(boolean isEmpty);
    void onCheckAttendance(String eventId);

}
