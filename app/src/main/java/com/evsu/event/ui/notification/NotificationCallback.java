package com.evsu.event.ui.notification;

public interface NotificationCallback {

    void onNotificationClicked(EventNotificationAdapter.ViewHolder viewHolder);

    void onNotificationLongClicked(EventNotificationAdapter.ViewHolder viewHolder);

    void onNotificationChecked(EventNotificationAdapter.ViewHolder viewHolder, boolean isChecked);

    void onSearchNotification(boolean hasMatch);

    void onNotificationEmpty();
}
