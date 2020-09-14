package com.evsu.event.ui.notification;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.evsu.event.R;
import com.evsu.event.databinding.ActivityEventNotificationBinding;
import com.evsu.event.model.Notification;
import com.evsu.event.service.NotificationService;
import com.evsu.event.ui.BaseActivity;
import com.evsu.event.ui.ResponseCallback;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

public class EventNotificationActivity extends BaseActivity<NotificationViewModel> implements
    NotificationCallback {

    private ActivityEventNotificationBinding activityBinding;
    private BroadcastReceiver broadcastReceiver;

    private Menu toolbarMenu;
    private ArrayAdapter markSpinnerAdapter;
    private List<String> markSpinnerItem;

    private EventNotificationAdapter notificationAdapter;
    private Notification openedNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_event_notification);
        activityBinding.setLifecycleOwner(this);

        Toolbar toolbar = (Toolbar) activityBinding.appToolbar;
        setSupportActionBar(toolbar);
        activityBinding.toolbarBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setToolbarSearchBar();
        setToolbarDeleteNotification();

        activityBinding.notificationRecyclerview.setHasFixedSize(true);
        activityBinding.notificationRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        notificationAdapter = new EventNotificationAdapter(this);
        activityBinding.setNotificationAdapter(notificationAdapter);

        activityBinding.refreshNotification.setOnRefreshListener(this::getNotifications);

        setVmObserver();
        getNotifications();
    }

    @Override
    protected NotificationViewModel createViewModel() {
        return ViewModelProviders.of(this).get(NotificationViewModel.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notification, menu);
        toolbarMenu = menu;
        menu.getItem(0).setVisible(false);
        return true;
    }

    // Toolbar menu action
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (notificationAdapter != null) {
            setDeleteMenuVisibility(true);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (notificationAdapter != null) {
            notificationAdapter.attachNewNotification();
        }
    }

    @Override
    public void onBackPressed() {
        if (activityBinding.searchLayout.getVisibility() == View.VISIBLE) {
            setSearchMenuVisibility(false);
            return;
        }

        if (activityBinding.deleteLayout.getVisibility() == View.VISIBLE) {
            setDeleteMenuVisibility(false);
            return;
        }

        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == resultCode) {
            if (openedNotification != null) {
                notificationAdapter.removeNotification(openedNotification);
                Toast.makeText(this, "Notification deleted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // New notification action
    private void setNewNotificationBroadcastReceiver() {
        if (broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equals(NotificationService.NEW_NOTIFICATION_BROADCASTER)) {
                        Notification notification = intent.getParcelableExtra("notification");
                        if (activityBinding.searchLayout.getVisibility() != View.VISIBLE && activityBinding.deleteLayout.getVisibility() != View.VISIBLE) {
                            notificationAdapter.addNotification(notification);
                        } else {
                            notificationAdapter.addNewNotification(notification);
                        }
                    }
                }
            };

            registerReceiver(broadcastReceiver, new IntentFilter(NotificationService.NEW_NOTIFICATION_BROADCASTER));
        }
    }

    /* *
     * -------------------------------------------------------------------------------
     * TOOLBAR ACTIONS
     * -------------------------------------------------------------------------------
     */

    // Search bar toolbar
    private void setToolbarSearchBar() {
        activityBinding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSearchMenuVisibility(true);
            }
        });

        activityBinding.etSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                notificationAdapter.searchNotification(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        activityBinding.btnClearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityBinding.etSearchBar.setText("");
            }
        });
    }

    // Delete toolbar
    private void setToolbarDeleteNotification() {
        Spinner markSpinner = activityBinding.markSpinner;
        markSpinnerItem = new ArrayList<>();
        markSpinnerItem.add("Mark all");

        markSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, markSpinnerItem) {

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        util.hideSpinnerDropdown(markSpinner);
                        notificationAdapter.selectAllCheckBox(markSpinnerItem.get(0).equals("Mark all"));
                    }
                });
                return view;
            }

            @Override
            public int getCount() {
                return markSpinnerItem.size();
            }
        };

        markSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        markSpinner.setAdapter(markSpinnerAdapter);

        // Delete notification action
        activityBinding.btnDeleteNotification.setOnClickListener(v -> new AlertDialog.Builder(this, R.style.MyAlertDialogStyle)
                .setMessage("Once you delete the notification. It cannot be undone.")
                .setNegativeButton("CANCEL", null)
                .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteNotification(notificationAdapter.getSelectedItems());
                    }
                })
                .create()
                .show());
    }

    private void setSearchMenuVisibility(boolean visible) {
        toolbarMenu.getItem(0).setVisible(!visible);
        activityBinding.titleLayout.setVisibility(visible ? View.GONE : View.VISIBLE);
        activityBinding.searchLayout.setVisibility(visible ? View.VISIBLE : View.GONE);
        activityBinding.refreshNotification.setEnabled(!visible);

        if (!visible) {
            activityBinding.etSearchBar.setText("");
            notificationAdapter.showHiddenHolder();
            notificationAdapter.attachNewNotification();
            noNotificationFound(false);
        }
    }

    private void setDeleteMenuVisibility(boolean visible) {
        if (activityBinding.searchLayout.getVisibility() == View.VISIBLE) {
            activityBinding.searchLayout.setVisibility(View.GONE);
        } else {
            activityBinding.titleLayout.setVisibility(visible ? View.GONE : View.VISIBLE);
        }

        if (!visible) {
            notificationAdapter.getSelectedItems().clear();
            setCheckedSpinnerItem();
            notificationAdapter.attachNewNotification();
        }

        activityBinding.deleteLayout.setVisibility(visible ? View.VISIBLE : View.GONE);
        notificationAdapter.setCheckBoxVisibility(visible ? View.VISIBLE : View.GONE);
        toolbarMenu.getItem(0).setVisible(!visible);
        activityBinding.refreshNotification.setEnabled(!visible);
    }

    private void setCheckedSpinnerItem() {
        int selectedCount = notificationAdapter.getSelectedItems().size();
        int itemCount = notificationAdapter.getItemCount();

        activityBinding.selectedItem.setText(String.valueOf(selectedCount).concat(" selected"));
        markSpinnerItem.set(0, selectedCount == itemCount ? "Unmark all" : "Mark all");
        markSpinnerAdapter.notifyDataSetChanged();
        activityBinding.btnDeleteNotification.setVisibility(selectedCount > 0 ? View.VISIBLE : View.GONE);
    }

    private void noNotificationFound(boolean bool) {
        activityBinding.noNotificationFound.setVisibility(!bool ? View.GONE: View.VISIBLE);
        activityBinding.btnSearch.setVisibility(bool ? View.GONE: View.VISIBLE);
        if (toolbarMenu != null) {
            toolbarMenu.getItem(0).setVisible(!bool && activityBinding.searchLayout.getVisibility() == View.GONE);
        }
    }

    /* *
     * -------------------------------------------------------------------------------
     * Notification item callback
     * -------------------------------------------------------------------------------
     */

    @Override
    public void onNotificationClicked(EventNotificationAdapter.ViewHolder viewHolder) {
        if (activityBinding.deleteLayout.getVisibility() == View.GONE) {
            Intent intent = new Intent(EventNotificationActivity.this, ReadActivity.class);
            intent.putExtra("notification", viewHolder.getItemBinding().getNotification());
            viewHolder.markAskRead(true);
            openedNotification = viewHolder.getItemBinding().getNotification();
            startActivityForResult(intent, 200);
        } else {
            viewHolder.getItemBinding().itemCheckbox.performClick();
        }
    }

    @Override
    public void onNotificationLongClicked(EventNotificationAdapter.ViewHolder viewHolder) {
        if (activityBinding.searchLayout.getVisibility() == View.GONE) {
            if (activityBinding.deleteLayout.getVisibility() == View.GONE) {
                setDeleteMenuVisibility(true);
            }
            viewHolder.getItemBinding().itemCheckbox.setChecked(true);
        }
    }

    @Override
    public void onNotificationChecked(EventNotificationAdapter.ViewHolder viewHolder, boolean isChecked) {
        setCheckedSpinnerItem();
    }

    @Override
    public void onSearchNotification(boolean hasMatch) {
        activityBinding.noNotificationFound.setVisibility(hasMatch ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onNotificationEmpty() {
        noNotificationFound(true);
    }


    /* *
     * -------------------------------------------------------------------------------
     * REPOSITORY CALLS
     * -------------------------------------------------------------------------------
     */

    private void setVmObserver() {
        vm.getNotifications().observe(this, new Observer<List<Notification>>() {
            @Override
            public void onChanged(List<Notification> notifications) {
                notificationAdapter.setNotificationList(notifications);
                noNotificationFound(notifications.size() < 1);
                setNewNotificationBroadcastReceiver();
            }
        });

        vm.getDeleteSuccess().observe(this, aBoolean -> {
            notificationAdapter.removeSelectedNotification();
            setDeleteMenuVisibility(false);
            Toast.makeText(this, "Notification deleted.", Toast.LENGTH_SHORT).show();
        });
    }

    private void getNotifications() {

        vm.notifications(vm.getUser().getUser_id(), new ResponseCallback<List<Notification>>() {
            @Override
            public void onComplete() {
                activityBinding.progressBar.setVisibility(View.GONE);
                activityBinding.refreshNotification.setRefreshing(false);
            }

            @Override
            public void onFailed(Throwable e) {
                util.messageDialog(
                        "Notification",
                        "Something went wrong there. Make sure you are connected to internet and try again.",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onBackPressed();
                            }
                        }
                );
            }

            @Override
            public void onServerError(Throwable e) {
                util.serverErrorDialog(null);
            }

            @Override
            public void onServerUnavailable(Throwable e) {
                util.serverUnavailableDialog(null);
            }
        });
    }

    private void deleteNotification(List<Notification> deleteCandidate) {
        util.showLoadingDialog("Deleting ...");

        vm.deleteNotification(deleteCandidate, new ResponseCallback<Boolean>() {
            @Override
            public void onComplete() {
                util.dismissLoadingDialog();
            }

            @Override
            public void onBadRequest(Throwable e) {
                util.messageDialog(
                        "Notification",
                        "Failed to delete notification.",
                        null
                );

                setDeleteMenuVisibility(false);
            }

            @Override
            public void onError(Throwable e) {
                mResponseCallback.setError(e);
            }
        });
    }

}
