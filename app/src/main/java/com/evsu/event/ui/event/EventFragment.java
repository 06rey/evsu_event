package com.evsu.event.ui.event;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evsu.event.R;
import com.evsu.event.databinding.FragmentEvsuEventBinding;
import com.evsu.event.model.EvsuEvent;
import com.evsu.event.ui.BaseFragment;
import com.evsu.event.ui.EventCallback;
import com.evsu.event.ui.PortraitCapture;
import com.evsu.event.ui.ResponseCallback;
import com.evsu.event.ui.ViewUtil;

import com.google.zxing.integration.android.IntentIntegrator;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

public class EventFragment extends BaseFragment<EventViewModel> implements
        EventCallback {

    public final static String UPCOMING = "upcoming";
    public final static String ONGOING = "ongoing";

    private FragmentEvsuEventBinding fragmentBinding;
    private EventAdapter adapter;
    private IntentIntegrator qrScanner;
    private ViewUtil util;

    public static EventFragment newInstance(String status) {
        EventFragment eventFragment = new EventFragment();
        Bundle bundle = new Bundle();

        bundle.putString("status", status);
        eventFragment.setArguments(bundle);
        return eventFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_evsu_event, container, false);

        adapter = new EventAdapter(this);
        fragmentBinding.noEvent.setText("No ".concat(getEventStatus()).concat(" event."));
        fragmentBinding.recyclerView.setHasFixedSize(true);
        fragmentBinding.recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        fragmentBinding.setAdapter(adapter);

        util = new ViewUtil(getContext());
        qrScanner = new IntentIntegrator(getActivity());
        qrScanner.setCaptureActivity(PortraitCapture.class);
        qrScanner.setOrientationLocked(true);

        vm.getEvsuEventList().observe(this, evsuEvents -> adapter.setEvsuEventList(evsuEvents));

        fragmentBinding.swipeRefresh.setOnRefreshListener(this::getEventList);
        getEventList();

        return fragmentBinding.getRoot();
    }

    @Override
    protected EventViewModel createViewModel() {
        return ViewModelProviders.of(this).get(EventViewModel.class);
    }

    private void getEventList() {

        vm.getStaffEvent(getEventStatus(), new ResponseCallback<List<EvsuEvent>>() {
            @Override
            public void onComplete() {
                fragmentBinding.progressBar.setVisibility(View.GONE);
                fragmentBinding.swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                mResponseCallback.setError(e);
            }
        });
    }

    private String getEventStatus() {
        return getArguments().getString("status", EventFragment.UPCOMING);
    }


    @Override
    public void onEmptyResult(boolean isEmpty) {
        fragmentBinding.noEvent.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onCheckAttendance(String eventId) {
        EventActivity.selectedEventId = eventId;
        qrScanner.initiateScan();
    }
}
