package com.evsu.event.model;

import com.google.gson.annotations.Expose;

public class User {

    public static final String STUDENT = "student";
    public static final String ORGANIZATION_PRESIDENT = "organization president";

    @Expose
    public String user_id;
    @Expose
    public String user_level_id;
    @Expose
    public String user_level;
    @Expose
    public String username;
    @Expose
    public String user_password;
    @Expose
    public String date_added;
    @Expose
    public String date_modified;
    @Expose
    public String profile_id;
    @Expose
    public String profile;
    @Expose
    public String first_name;
    @Expose
    public String middle_name;
    @Expose
    public String last_name;
    @Expose
    public String birth_date;
    @Expose
    public String college;
    @Expose
    public String course;
    @Expose
    public String year_level;
    @Expose
    public String address;
    @Expose
    public String guardian;
    @Expose
    public String email_add;
    @Expose
    public String contact_no;

    public String full_name;


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_level_id() {
        return user_level_id;
    }

    public String getUser_level() {
        return user_level;
    }

    public void setUser_level(String user_level) {
        this.user_level = user_level;
    }

    public void setUser_level_id(String user_level_id) {
        this.user_level_id = user_level_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public String getDate_modified() {
        return date_modified;
    }

    public void setDate_modified(String date_modified) {
        this.date_modified = date_modified;
    }

    public String getProfile_id() {
        return profile_id;
    }

    public void setProfile_id(String profile_id) {
        this.profile_id = profile_id;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getYear_level() {
        return year_level;
    }

    public void setYear_level(String year_level) {
        this.year_level = year_level;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGuardian() {
        return guardian;
    }

    public void setGuardian(String guardian) {
        this.guardian = guardian;
    }

    public String getEmail_add() {
        return email_add;
    }

    public void setEmail_add(String email_add) {
        this.email_add = email_add;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getFullName() {
        return first_name.concat(" ").concat(middle_name).concat(" ").concat(last_name);
    }

    public void setFullName() {
        full_name =  first_name.concat(" ").concat(middle_name).concat(" ").concat(last_name);
    }
}
