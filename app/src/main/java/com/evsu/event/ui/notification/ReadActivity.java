package com.evsu.event.ui.notification;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.os.Bundle;

import com.evsu.event.R;
import com.evsu.event.databinding.ActivityReadBinding;
import com.evsu.event.model.Notification;
import com.evsu.event.ui.BaseActivity;
import com.evsu.event.ui.ResponseCallback;
import com.evsu.event.util.AppUtil;

import java.util.ArrayList;
import java.util.List;

public class ReadActivity extends BaseActivity<NotificationViewModel> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityReadBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_read);
        binding.setLifecycleOwner(this);


        Notification notification = getIntent().getParcelableExtra("notification");

        vm.readNotification(notification.getSent_id());

        binding.setNotification(notification);

        binding.txtTime.setText(AppUtil.transformDate(notification.getDate_sent(), "EEE, MMM d, ''yy 'at' h:mm a", "yyyy-MM-dd HH:mm:ss"));

        binding.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(this, R.style.MyAlertDialogStyle)
                    .setMessage("Once you delete the notification. It cannot be undone.")
                    .setNegativeButton("CANCEL", null)
                    .setPositiveButton("DELETE", (dialog, which) -> {
                        deleteNotification(notification);
                    })
                    .create()
                    .show();
        });

        vm.getDeleteSuccess().observe(this, (Observer<Boolean>) aBoolean -> {
            setResult(200);
            onBackPressed();
        });
    }

    @Override
    protected NotificationViewModel createViewModel() {
        return ViewModelProviders.of(this).get(NotificationViewModel.class);
    }

    private void deleteNotification(Notification notification) {
        List<Notification> list = new ArrayList<>();
        list.add(notification);

        util.showLoadingDialog("Deleting ...");

        vm.deleteNotification(list, new ResponseCallback<Boolean>() {
            @Override
            public void onComplete() {
                util.dismissLoadingDialog();
            }

            @Override
            public void onBadRequest(Throwable e) {
                onBackPressed();
            }

            @Override
            public void onError(Throwable e) {
                mResponseCallback.setError(e);
            }
        });
    }
}
