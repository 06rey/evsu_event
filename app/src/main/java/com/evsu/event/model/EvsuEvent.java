package com.evsu.event.model;

import com.evsu.event.util.AppUtil;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class EvsuEvent {

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    @SerializedName("event_id")
    @Expose
    private String eventId;

    @SerializedName("event_for")
    @Expose
    private String eventFor;

    @SerializedName("event_title")
    @Expose
    private String eventTitle;

    @SerializedName("event_desc")
    @Expose
    private String eventDesc;

    @SerializedName("event_status")
    @Expose
    private String eventStatus;

    @SerializedName("start_date")
    @Expose
    private String startDate;

    @SerializedName("end_date")
    @Expose
    private String endDate;

    @SerializedName("event_sponsor")
    @Expose
    private String eventSponsor;

    @SerializedName("Semester")
    @Expose
    private String semester;

    @SerializedName("school_year")
    @Expose
    private String schoolYear;

    @SerializedName("start_time")
    @Expose
    private String startTime;

    @SerializedName("end_time")
    @Expose
    private String endTime;

    @SerializedName("Event_added")
    @Expose
    private String eventAdded;

    @SerializedName("user_id")
    @Expose
    private String userId;

    @SerializedName("assign_id")
    @Expose
    private String assignId;

    public String getDateShchedule() {
        String startYear = AppUtil.transformDate(startDate, "yyyy", DATE_FORMAT);
        String startMonth = AppUtil.transformDate(startDate, "MMM", DATE_FORMAT);
        String startDay = AppUtil.transformDate(startDate, "d", DATE_FORMAT);

        String endYear = AppUtil.transformDate(endDate, "yyyy", DATE_FORMAT);
        String endMonth = AppUtil.transformDate(endDate, "MMM", DATE_FORMAT);
        String endDay = AppUtil.transformDate(endDate, "d", DATE_FORMAT);

        String from;
        String to;
        String year;

        if (! startYear.equals(endYear)) {
            return AppUtil.transformDate(startDate, "MMM d, yyyy", DATE_FORMAT)
                    .concat(" - ")
                    .concat(AppUtil.transformDate(endDate, "MMM d, yyyy", DATE_FORMAT));
        } else {
            year = startYear;

            if (! startMonth.equals(endMonth)) {
                from = startMonth.concat(" ").concat(startDay);
                to = endMonth.concat(" ").concat(endDay);

                return from.concat(" - ").concat(to).concat(", ").concat(year);
            } else {
                return startMonth.concat(" ").concat(startDay).concat(" - ").concat(endDay).concat(", ").concat(year);
            }
        }
    }

    public String getEventTime() {
        String startTime = AppUtil.transformDate(this.startTime, "h:mm a", "HH:mm:ss");
        String endTime = AppUtil.transformDate(this.endTime, "h:mm a", "HH:mm:ss");

        return startTime.concat(" - ").concat(endTime);
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventFor() {
        return eventFor;
    }

    public void setEventFor(String eventFor) {
        this.eventFor = eventFor;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEventSponsor() {
        return eventSponsor;
    }

    public void setEventSponsor(String eventSponsor) {
        this.eventSponsor = eventSponsor;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(String schoolYear) {
        this.schoolYear = schoolYear;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEventAdded() {
        return eventAdded;
    }

    public void setEventAdded(String eventAdded) {
        this.eventAdded = eventAdded;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAssignId() {
        return assignId;
    }

    public void setAssignId(String assignId) {
        this.assignId = assignId;
    }
}
