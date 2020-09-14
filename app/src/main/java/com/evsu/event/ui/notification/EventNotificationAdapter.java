package com.evsu.event.ui.notification;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evsu.event.BR;
import com.evsu.event.R;
import com.evsu.event.databinding.ItemNotificationBinding;
import com.evsu.event.model.Notification;
import com.evsu.event.ui.ViewUtil;
import com.evsu.event.util.AppUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class EventNotificationAdapter extends RecyclerView.Adapter<EventNotificationAdapter.ViewHolder> {

    private final String OLD_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final String DATE_FORMAT = "EEE, MMM d, ''yy 'at' h:mm a";

    private NotificationCallback notificationCallback;
    private List<Notification> notificationList;
    private List<Notification> newNotificationList = new ArrayList<>();
    private List<Notification> selectedItems = new ArrayList<>();

    private List<ViewHolder> viewHolderList = new ArrayList<>();
    private List<ViewHolder> hiddenViewHolderList = new ArrayList<>();

    public EventNotificationAdapter(Context context) {
        this.notificationList = new ArrayList<>();
        if (context instanceof NotificationCallback) {
            notificationCallback = (NotificationCallback) context;
        }
    }

    public void setNotificationList(List<Notification> notificationList) {
        viewHolderList.clear();
        this.notificationList = notificationList;
        notifyNotificationDataSetChanged();
    }

    public void addNewNotification(Notification notification) {
        newNotificationList.add(notification);
    }

    public void removeNotification(Notification notification) {
        notificationList.remove(notification);
        notifyNotificationDataSetChanged();
    }

    private void notifyNotificationDataSetChanged() {
        notifyDataSetChanged();
        if (notificationList.isEmpty()) {
            Log.d("ddd", "notifyNotificationDataSetChanged");
            notificationCallback.onNotificationEmpty();
        }
    }

    public void attachNewNotification() {
        Log.d("ddd", "attachNewNotification");
        for (Notification notification: newNotificationList) {
            notificationList.add(0, notification);
        }
        newNotificationList.clear();
        notifyNotificationDataSetChanged();
    }



    public void addNotification(Notification notification) {
        notificationList.add(0, notification);
        notifyNotificationDataSetChanged();
    }

    public void removeSelectedNotification() {
        for (Notification notification: selectedItems) {
            notificationList.remove(notification);
        }

        notifyNotificationDataSetChanged();
    }

    public void setCheckBoxVisibility(int visibility) {
        for (ViewHolder holder: viewHolderList) {
            holder.itemBinding.itemCheckbox.setVisibility(visibility);

            if (visibility == View.GONE) {
                holder.itemBinding.itemCheckbox.setChecked(false);
            }
        }
    }

    public void selectAllCheckBox(boolean checked) {
        for (ViewHolder holder: viewHolderList) {
            holder.itemBinding.itemCheckbox.setChecked(checked);
        }
    }

    public List<Notification> getSelectedItems() {
        return selectedItems;
    }

    public void searchNotification(String text) {
        boolean hasMatch = false;

        for (ViewHolder holder: viewHolderList) {
            Notification notification = holder.getItemBinding().getNotification();
            holder.itemBinding.itemBody.setText(notification.getBody());
            String textBody = holder.itemBinding.itemBody.getText().toString().toLowerCase();

            if (textBody.contains(text.toLowerCase()) || holder.itemBinding.itemTitle.getText().toString().toLowerCase().contains(text.toLowerCase())) {
                if (textBody.contains(text.toLowerCase())) {
                    ViewUtil.create().setHighLightedText(holder.itemBinding.itemBody, text);
                }

                if (notification.isHidden()) {
                    notification.setHidden(false);
                    hideItemHolder(holder, false);
                }

                hasMatch = true;
            } else {

                if (!notification.isHidden()) {
                    notification.setHidden(true);
                    hideItemHolder(holder, true);
                    hiddenViewHolderList.add(holder);
                }
            }
        }

        if (text.equals("")) {
            showHiddenHolder();
        }

        notificationCallback.onSearchNotification(hasMatch);
    }

    public void showHiddenHolder() {
        for (ViewHolder holder: hiddenViewHolderList) {
            holder.getItemBinding().getNotification().setHidden(false);
            hideItemHolder(holder, false);
        }
        hiddenViewHolderList.clear();
    }

    private void hideItemHolder(ViewHolder holder, boolean hidden) {
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        holder.itemView.setVisibility(hidden ? View.GONE : View.VISIBLE);
        params.height = hidden ? 0 : ViewGroup.LayoutParams.WRAP_CONTENT;
        holder.itemView.setLayoutParams(params);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNotificationBinding dataBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_notification,
                parent, false
        );

        return new ViewHolder(dataBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = notificationList.get(position);
        holder.bind(notification);
        holder.getItemBinding().itemDate.setText(AppUtil.transformDate(notification.getDate_sent(), DATE_FORMAT, OLD_DATE_FORMAT));

        holder.getItemBinding().itemRoot.setOnClickListener(v -> {
            notificationCallback.onNotificationClicked(holder);
        });

        holder.getItemBinding().itemRoot.setOnLongClickListener(v -> {
            notificationCallback.onNotificationLongClicked(holder);
            return false;
        });

        holder.getItemBinding().itemCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedItems.add(notification);
            } else {
                while(selectedItems.remove(notification)) {}
            }

            notificationCallback.onNotificationChecked(holder, isChecked);
        });

        viewHolderList.add(holder);
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    // View Holder

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemNotificationBinding itemBinding;

        public ViewHolder(ItemNotificationBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        public void bind(Notification notification) {
            itemBinding.setVariable(BR.notification, notification);
            itemBinding.executePendingBindings();
            markAskRead(!notification.isUnread());
        }

        public void markAskRead(boolean read) {
            itemBinding.itemBody.setTypeface(null, read ? Typeface.NORMAL : Typeface.BOLD);
            itemBinding.itemBody.setTextColor(
                    read ? itemBinding.getRoot().getContext().getResources().getColor(R.color.readGray)
                            : itemBinding.getRoot().getContext().getResources().getColor(R.color.black)
            );
            itemBinding.itemUnreadMark.setVisibility(read ? View.GONE : View.VISIBLE);
            itemBinding.getNotification().setRead(read ? "1" : "0");
        }

        public ItemNotificationBinding getItemBinding() {
            return itemBinding;
        }
    }

}
