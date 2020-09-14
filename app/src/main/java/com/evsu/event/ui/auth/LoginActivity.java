package com.evsu.event.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.evsu.event.App;
import com.evsu.event.R;
import com.evsu.event.databinding.ActivityLoginBinding;
import com.evsu.event.ui.BaseActivity;
import com.evsu.event.ui.home.HomeActivity;
import com.evsu.event.ui.ResponseCallback;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class LoginActivity extends BaseActivity<LoginViewModel> {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setLifecycleOwner(this);
        binding.setUsername("");
        binding.setPassword("");

        vm.getLoginSuccess().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                App.instance().startNotificationService();
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
            }
        });
    }

    @Override
    protected LoginViewModel createViewModel() {
        return ViewModelProviders.of(this).get(LoginViewModel.class);
    }

    // Login click event
    public void onClickLogin(View view) {
        if (binding.getUsername().trim().equals("")) {
            binding.etUsername.setError("Username is required!");
            return;
        }

        if (binding.getPassword().trim().equals("")) {
            binding.etPassword.setError("Password is required!");
            return;
        }

        enableBtnLogin(false);

        vm.login(binding.getUsername(), binding.getPassword() , new ResponseCallback<Boolean>() {

            @Override
            public void onComplete() {
                enableBtnLogin(true);
            }

            @Override
            public void onForbidden(Throwable e) {
                util.messageDialog(
                        "Login Failed",
                        "Incorrect username or password. Try again.",
                        null
                );
            }

            @Override
            public void onError(Throwable e) {
                mResponseCallback.setError(e);
            }
        });
    }

    private void enableBtnLogin(boolean enable) {
        binding.btnLogin.setEnabled(enable);
        binding.btnLogin.setBackground(
                enable ? getResources().getDrawable(R.drawable.primary)
                        : getResources().getDrawable(R.drawable.btn_loading)
        );
        binding.btnLogin.setText(enable ? "Login" : "Logging In ...");
    }

    // Hostname setting
    public void onClickBtnSetting(View view) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_setting, null);
        final EditText txt1 = dialogView.findViewById(R.id.txt1);
        final Switch errorSwitch = dialogView.findViewById(R.id.error_dialog_switch);
        errorSwitch.setChecked(App.instance().getDisplayErrorDialog());

        errorSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (App.instance().setDisplayErrorDialog(isChecked)) {
                errorSwitch.setChecked(isChecked);
                String ena = isChecked ? "enabled" : "disabled";
                Toast.makeText(this, "Http response dialog "+ ena, Toast.LENGTH_SHORT).show();
            }
        });

        txt1.setText(App.instance().getHostname());

        alertBuilder.setView(dialogView)
                .setNegativeButton("CANCEL",null)
                .setPositiveButton("DONE", (dialog, which) -> {
                    if (App.instance().setHostname(txt1.getText().toString())) {
                        Snackbar.make(binding.form, "Hostname successfully saved!", Snackbar.LENGTH_LONG)
                                .show();
                    } else {
                        Snackbar.make(binding.form, "Failed to save hostname! Try again.", Snackbar.LENGTH_LONG)
                                .show();
                    }
                })
                .create()
                .show();
    }
}
