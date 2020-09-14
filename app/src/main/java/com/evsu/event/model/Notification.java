package com.evsu.event.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

public class Notification implements Parcelable {

    @Expose
    public String notification_id;
    @Expose
    public String sent_id;
    @Expose
    public String subject;
    @Expose
    public String body;
    @Expose
    public String date_sent;
    @Expose
    public String read;

    public boolean selected = false;
    public boolean hidden = false;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    protected Notification(Parcel in) {
        notification_id = in.readString();
        sent_id = in.readString();
        subject = in.readString();
        body = in.readString();
        date_sent = in.readString();
        read = in.readString();
    }

    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };

    public String getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(String notification_id) {
        this.notification_id = notification_id;
    }

    public String getSent_id() {
        return sent_id;
    }

    public void setSent_id(String sent_id) {
        this.sent_id = sent_id;
    }


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate_sent() {
        return date_sent;
    }

    public void setDate_sent(String date_sent) {
        this.date_sent = date_sent;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public boolean isUnread() {
        return Integer.parseInt(read) == 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(notification_id);
        dest.writeString(sent_id);
        dest.writeString(subject);
        dest.writeString(body);
        dest.writeString(date_sent);
        dest.writeString(read);
    }
}
