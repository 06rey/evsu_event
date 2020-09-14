package com.evsu.event.data;

import com.evsu.event.App;
import com.evsu.event.data.local.SharedPref;
import com.evsu.event.data.repository.AppRepository;
import com.evsu.event.data.repository.UserRepository;

public class DataManager {

    private DataManager() {}

    public static AppRepository AppRepository() {
        return new AppRepository(new SharedPref(App.instance()));
    }

    public static UserRepository UserRepository() {
        return new UserRepository(new SharedPref(App.instance()));
    }

}
