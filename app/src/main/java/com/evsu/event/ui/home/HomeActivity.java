package com.evsu.event.ui.home;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.evsu.event.App;
import com.evsu.event.R;
import com.evsu.event.databinding.ActivityHomeBinding;
import com.evsu.event.model.User;
import com.evsu.event.ui.QrActivity;
import com.evsu.event.ui.account.AccountActivity;
import com.evsu.event.ui.BaseActivity;
import com.evsu.event.ui.auth.LoginActivity;
import com.evsu.event.ui.event.EventActivity;
import com.evsu.event.ui.notification.EventNotificationActivity;

public class HomeActivity extends BaseActivity<HomeViewModel> {

    private ActivityHomeBinding binding;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        binding.setLifecycleOwner(this);

        user = App.instance().getActiveUser();
        String userLevel = user.getUser_level().toLowerCase();

        if (userLevel.equals(User.STUDENT) || userLevel.equals(User.ORGANIZATION_PRESIDENT)) {
            binding.menuEvents.setVisibility(View.GONE);
            vm.getUnreadNotificationCount().observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(Integer count) {
                    binding.txtUnreadCount.setText(String.valueOf(count));
                    binding.txtUnreadCount.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
                }
            });

            vm.countUnreadNotification(user.getUser_id());
        }else{
            binding.menuNotification.setVisibility(View.GONE);
        }
    }

    @Override
    protected HomeViewModel createViewModel() {
        return ViewModelProviders.of(this).get(HomeViewModel.class);
    }

    // Click events

    public void onClickLogout(View view) {
        util.confirmDialog(
                "Are you sure you want to logout your account?",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        App.instance().logout();
                        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                        finish();
                    }
                },
                null
        );
    }

    public void onClickProfile(View view) {
        startActivity(new Intent(HomeActivity.this, AccountActivity.class));
    }

    public void onClickNotification(View view) {
        startActivity(new Intent(HomeActivity.this, EventNotificationActivity.class));
    }

    public void onClickQr(View view) {
        Intent intent = new Intent(HomeActivity.this, QrActivity.class);
        startActivity(intent);
    }

    public void onClickEvent(View view) {
        startActivity(new Intent(HomeActivity.this, EventActivity.class));
    }
}
