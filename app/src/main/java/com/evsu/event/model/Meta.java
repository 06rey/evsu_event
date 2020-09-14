package com.evsu.event.model;

import com.google.gson.annotations.Expose;

public class Meta {

    @Expose
    public Integer data_count;

    public Integer getData_count() {
        return data_count;
    }

    public void setData_count(Integer data_count) {
        this.data_count = data_count;
    }
}
