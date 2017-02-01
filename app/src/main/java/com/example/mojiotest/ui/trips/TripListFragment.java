package com.example.mojiotest.ui.trips;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.example.mojiotest.R;
import com.example.mojiotest.ui.App;

import java.util.List;

import io.moj.java.sdk.MojioClient;
import io.moj.java.sdk.model.Trip;

public class TripListFragment extends MvpAppCompatFragment implements TripListView {

    private OnTripClickListener mListener;

    TripViewAdapter adapter;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressDialog progressDialog;
    MaterialDialog errorDialog;
    MaterialDialog.Builder errorBuilder;

    @InjectPresenter
    TripListPresenter presenter;

    public TripListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        errorBuilder = new MaterialDialog.Builder(getActivity())
                .iconRes(R.drawable.ic_stop)
                .limitIconToDefaultSize()
                .positiveText(android.R.string.ok)
                .onPositive((dialog, which) -> presenter.onErrorCancel());
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgress();
        hideError();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.swipe_recycle_view, container, false);
    }

    @ProvidePresenter
    TripListPresenter provideTripListPresenter() {
        App app = (App) getActivity().getApplicationContext();
        MojioClient mojioClient = app.getMojioClient();
        return new TripListPresenter(mojioClient);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeRefreshLayout.setOnRefreshListener(() -> presenter.loadTrips(true));

        LinearLayoutManager mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mManager);

        adapter = new TripViewAdapter(mListener);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTripClickListener) {
            mListener = (OnTripClickListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnTripClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void showProgress() {
        if (progressDialog == null || !progressDialog.isShowing()) {
            progressDialog = ProgressDialog.show(getActivity(), null, null);
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progressDialog.setContentView(R.layout.layout_progress);
        }
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showError(String message) {
        errorDialog = errorBuilder
                .title(message)
                .show();
    }

    @Override
    public void showError(int message) {
        errorDialog = errorBuilder
                .title(message)
                .show();
    }

    @Override
    public void hideError() {
        if (errorDialog != null && errorDialog.isShowing()) {
            errorDialog.dismiss();
        }
    }

    @Override
    public void setTrips(List<Trip> tripList) {
        adapter.setTrips(tripList);
    }

    @Override
    public void showRefreshing() {
        swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(true));
    }

    @Override
    public void hideRefreshing() {
        swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(false));
    }

    @Override
    public void setTitle(int title) {
        getActivity().setTitle(title);
    }

    @Override
    public void onStartLoading() {
        swipeRefreshLayout.setEnabled(false);
    }

    @Override
    public void onFinishLoading() {
        swipeRefreshLayout.setEnabled(true);
    }

    public interface OnTripClickListener {
        void viewTrip(String tripId);
    }
}
