package com.evsu.event.ui.account;

import android.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.evsu.event.R;
import com.evsu.event.databinding.ActivityAccountBinding;
import com.evsu.event.model.User;
import com.evsu.event.ui.BaseActivity;
import com.evsu.event.ui.auth.LoginActivity;
import com.evsu.event.ui.ResponseCallback;
import com.evsu.event.util.InputDialogInterface;

public class AccountActivity extends BaseActivity<AccountViewModel> {

    private ActivityAccountBinding binding;
    private AlertDialog changePasswordDialog;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_account);
        binding.setLifecycleOwner(this);
        binding.setHandler(this);

        setUserProfile();
        vm.getChangePasswordSuccess().observe(this, aVoid -> {
            Toast.makeText(this, "Password changed successfully.",Toast.LENGTH_LONG).show();
            changePasswordDialog.dismiss();
        });
    }

    @Override
    protected AccountViewModel createViewModel() {
        return ViewModelProviders.of(this).get(AccountViewModel.class);
    }

    private void setUserProfile() {
        user = vm.getUser();
        String userLevel = user.getUser_level().toLowerCase();

        if (userLevel.equals("student") || userLevel.equals("organization president")) {
            binding.userLevel.setVisibility(View.GONE);
        }else{
            binding.course.setVisibility(View.GONE);
            binding.yearLevel.setVisibility(View.GONE);
        }

        binding.setUser(user);
    }

    // Click events

    public void onClickChangePassword(View view) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_change_password, null);

        final EditText etCurrent = dialogView.findViewById(R.id.et_current);
        final EditText etNew = dialogView.findViewById(R.id.et_new);
        final EditText etConfirm = dialogView.findViewById(R.id.et_confirm);

        changePasswordDialog = alertBuilder.setTitle("Change Password")
                .setView(dialogView)
                .setCancelable(false)
                .setPositiveButton("CHANGE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //
                    }
                })
                .setNegativeButton("CANCEL", null)
                .create();

        changePasswordDialog.show();

        changePasswordDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentPassword = etCurrent.getText().toString().trim();
                String newPassword = etNew.getText().toString().trim();
                String confirmPassword = etConfirm.getText().toString().trim();

                if (currentPassword.equals("")) {
                    etCurrent.setError("Current password is required!");
                    return;
                }

                if (newPassword.equals("")) {
                    etNew.setError("New password is required!");
                    return;
                }

                if (currentPassword.equals(newPassword)) {
                    etNew.setError("This is your current password.");
                    return;
                }

                if (confirmPassword.equals("")) {
                    etConfirm.setError("Please confirm new password.");
                    return;
                }

                if (!newPassword.equals(confirmPassword)) {
                    etConfirm.setError("Confirm password does not match new password!");
                    return;
                }

                changePassword(currentPassword, newPassword);
            }
        });
    }

    // Change password
    private void changePassword(String currentPassword, String newPassword) {
        util.showLoadingDialog(null);

        vm.changePassword(currentPassword, newPassword, user.user_id, new ResponseCallback<Boolean>() {
            @Override
            public void onComplete() {
                util.dismissLoadingDialog();
            }

            @Override
            public void onForbidden(Throwable e) {
                util.messageDialog(
                        "Change Password Failed",
                        "Incorrect current password. Please try again.",
                        null
                );
            }

            @Override
            public void onError(Throwable e) {
                mResponseCallback.setError(e);
            }
        });
    }

    // Profile detail click events

    public void onClickProfileDetail(View view, String name) {
        String oldValue = "";
        switch (name) {
            case "Email":
                oldValue = user.getEmail_add();
                break;
            case "Contact number":
                oldValue = user.getContact_no();
                break;
            case "Address":
                oldValue = user.getAddress();
                break;
            case "Guardian":
                oldValue = user.getGuardian();
                break;
        }

        final String detail = oldValue;

        util.textInputDialog(name, oldValue, new InputDialogInterface.OnSetListener<String>() {
            @Override
            public void onSet(AlertDialog dialog, EditText editText, String value) {
                if (value.equals(detail)) {
                    editText.setError("No changes made.");
                }else{
                    util.showLoadingDialog(null);
                    updateProfile(name, value);
                    dialog.dismiss();
                }
            }
        });
    }

    private void updateProfile(String name, String newValue) {
        final Context context = this;

        vm.getProfileUpdateSuccess().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                switch (name) {
                    case "Email":
                        user.setEmail_add(newValue);
                        break;
                    case "Contact number":
                        user.setContact_no(newValue);
                        break;
                    case "Address":
                        user.setAddress(newValue);
                        break;
                    case "Guardian":
                        user.setGuardian(newValue);
                        break;
                }
                vm.setUser(user);
                binding.setUser(user);
                Toast.makeText(context, name.concat(" successfully updated."),Toast.LENGTH_LONG).show();

                vm.getProfileUpdateSuccess().removeObserver(this);
            }
        });

        vm.updateProfile(
                name.toLowerCase().replace(" ", "_"),
                newValue, user.profile_id,
                new ResponseCallback<Boolean>() {
                    @Override
                    public void onComplete() {
                        util.dismissLoadingDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mResponseCallback.setError(e);
                    }
                }
        );
    }

    public void onClickLogout(View view) {
        util.confirmDialog(
                "Are you sure you want to logout your account?",
                (dialog, which) -> {
                    vm.logout();
                    startActivity(new Intent(AccountActivity.this, LoginActivity.class));
                    finish();
                },
                null
        );
    }
}
