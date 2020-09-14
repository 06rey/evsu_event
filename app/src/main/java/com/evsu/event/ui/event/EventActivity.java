package com.evsu.event.ui.event;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;

import com.evsu.event.R;
import com.evsu.event.databinding.ActivityEventBinding;
import com.evsu.event.databinding.DialogAttendanceBinding;
import com.evsu.event.ui.BaseActivity;
import com.evsu.event.ui.ResponseCallback;
import com.evsu.event.ui.ViewUtil;
import com.evsu.event.util.Constant;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.lang.reflect.Type;
import java.util.Map;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;

public class EventActivity extends BaseActivity<EventViewModel> {

    public static String selectedEventId;

    private ActivityEventBinding activityBinding;
    private EventFragment eventFragment;
    private ViewUtil util;

    private String participantName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_event);
        activityBinding.setLifecycleOwner(this);

        EventPagerAdapter eventPagerAdapter = new EventPagerAdapter(getSupportFragmentManager(), activityBinding.tabLayoutEvent.getTabCount());
        activityBinding.viewPagerEvent.setAdapter(eventPagerAdapter);
        activityBinding.viewPagerEvent.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(activityBinding.tabLayoutEvent));
        activityBinding.tabLayoutEvent.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                activityBinding.viewPagerEvent.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        util = new ViewUtil(this);
        vm.getAttendanceSuccess().observe(this, aBoolean -> Toast.makeText(this, "Attendance successfully save", Toast.LENGTH_SHORT).show() );
    }

    @Override
    protected EventViewModel createViewModel() {
        return ViewModelProviders.of(this).get(EventViewModel.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {

                Type type = new TypeToken<Map<String, String>>(){}.getType();
                Map<String, String> user = new Gson().fromJson(result.getContents(), type);
                participantDetailsDialog(user);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void participantDetailsDialog(Map<String, String> participant) {
        DialogAttendanceBinding dialogBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(this),
                    R.layout.dialog_attendance,
                    null,
                    false
                );

        AlertDialog dialogAttendance = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle)
                .setView(dialogBinding.getRoot())
                .setCancelable(false)
                .create();

        dialogBinding.fullName.setText(participant.get("full_name"));
        dialogBinding.userLevel.setText(participant.get("user_level").toUpperCase());
        dialogBinding.college.setText(participant.get("college"));

        if (participant.containsKey("course")) {
            dialogBinding.course.setText(participant.get("course"));
        }

        dialogBinding.btnClose.setOnClickListener(v -> dialogAttendance.dismiss());

        dialogBinding.btnTimeIn.setOnClickListener(v -> {
            checkAttendance(participant, Constant.TIME_IN);
        });

        dialogBinding.btnTimeOut.setOnClickListener(v -> {
            checkAttendance(participant, Constant.TIME_OUT);
        });

        dialogAttendance.show();
    }

    private void checkAttendance(Map<String, String> participant, String mode) {
        final Context context = this;

        util.showLoadingDialog("Loading ...");
        participantName = participant.get("full_name");

        vm.attendance(
                participant.get("user_id"),
                EventActivity.selectedEventId,
                mode,
                new ResponseCallback<Boolean>() {
                    @Override
                    public void onComplete() {
                        util.dismissLoadingDialog();
                    }

                    @Override
                    public void onBadRequest(Throwable e) {
                        new AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
                                .setMessage(participantName+ " is not participant of this event.")
                                .setPositiveButton("OK", null)
                                .create()
                                .show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mResponseCallback.setError(e);
                    }
                });
    }

    // Tab layout view pager adapter
    class EventPagerAdapter extends FragmentPagerAdapter {

        private int tabSize;

        public EventPagerAdapter(FragmentManager fm, int tabSize) {
            super(fm);
            this.tabSize = tabSize;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                eventFragment = EventFragment.newInstance(EventFragment.UPCOMING);
            } else if (position == 1) {
                eventFragment = EventFragment.newInstance(EventFragment.ONGOING);
            }

            return eventFragment;
        }

        @Override
        public int getCount() {
            return tabSize;
        }
    }

}
