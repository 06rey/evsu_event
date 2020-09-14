package com.evsu.event.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("user")
    @Expose
    public User user;

    @SerializedName("notifications")
    @Expose
    public List<Notification> notifications;

    @SerializedName("receive_id")
    @Expose
    public List<String> receive_id;

    @SerializedName("event")
    @Expose
    public List<EvsuEvent> evsuEvents;

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<String> getReceive_id() {
        return receive_id;
    }

    public void setReceive_id(List<String> receive_id) {
        this.receive_id = receive_id;
    }

    public List<EvsuEvent> getEvsuEvents() {
        return evsuEvents;
    }

    public void setEvsuEvents(List<EvsuEvent> evsuEvents) {
        this.evsuEvents = evsuEvents;
    }
}
