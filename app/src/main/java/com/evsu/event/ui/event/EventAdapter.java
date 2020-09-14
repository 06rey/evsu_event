package com.evsu.event.ui.event;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evsu.event.BR;
import com.evsu.event.R;
import com.evsu.event.databinding.ItemEvsuEventBinding;
import com.evsu.event.model.EvsuEvent;
import com.evsu.event.ui.EventCallback;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private List<EvsuEvent> evsuEventList = new ArrayList<>();
    private EventCallback eventCallback;

    public List<EvsuEvent> getEvsuEventList() {
        return evsuEventList;
    }

    public EventAdapter(EventCallback eventCallback) {
        this.eventCallback = eventCallback;
    }

    public void setEvsuEventList(List<EvsuEvent> evsuEventList) {
        this.evsuEventList = evsuEventList;
        notifyDataListChanged();
    }

    public void addItem(EvsuEvent evsuEvent) {
        if (evsuEvent != null) {
            evsuEventList.add(0, evsuEvent);
            notifyDataListChanged();
        }

    }

    public void notifyDataListChanged() {
        notifyDataSetChanged();
        eventCallback.onEmptyResult(evsuEventList.isEmpty());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemEvsuEventBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_evsu_event,
                parent, false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EvsuEvent evsuEvent = evsuEventList.get(position);

        holder.bind(evsuEvent);
        holder.itemBinding.btnCheckAttendance.setVisibility(evsuEvent.getEventStatus().toLowerCase().equals("ongoing") ? View.VISIBLE : View.GONE);
        holder.itemBinding.time.setText(evsuEvent.getEventTime());
        holder.itemBinding.date.setText(evsuEvent.getDateShchedule());
        holder.itemBinding.btnCheckAttendance.setOnClickListener(v -> {
            eventCallback.onCheckAttendance(evsuEvent.getEventId());
        });
    }

    @Override
    public int getItemCount() {
        return evsuEventList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemEvsuEventBinding itemBinding;

        public ViewHolder(ItemEvsuEventBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        public void bind(EvsuEvent evsuEvent) {
            itemBinding.setVariable(BR.event, evsuEvent);
            itemBinding.executePendingBindings();
        }
    }
}
