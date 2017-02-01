package com.example.mojiotest.ui.trip;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.example.mojiotest.R;
import com.example.mojiotest.tools.ToolsTime;
import com.example.mojiotest.ui.App;

import io.moj.java.sdk.MojioClient;
import io.moj.java.sdk.model.Trip;
import io.moj.java.sdk.model.values.Distance;
import io.moj.java.sdk.model.values.Rpm;
import io.moj.java.sdk.model.values.Speed;

public class TripFragment extends MvpAppCompatFragment implements TripView {
    private static final String TRIP_ID = "TRIP_ID";

    SwipeRefreshLayout swipeRefreshLayout;
    ProgressDialog progressDialog;
    MaterialDialog errorDialog;
    MaterialDialog.Builder errorBuilder;
    EditText editTextDuration, editTextDistance, editTextMaxSpeed, editTextMaxRPM;

    @InjectPresenter
    TripPresenter presenter;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_trip, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editTextDuration = (EditText) view.findViewById(R.id.editTextDuration);
        editTextDistance = (EditText) view.findViewById(R.id.editTextDistance);
        editTextMaxSpeed = (EditText) view.findViewById(R.id.editTextMaxSpeed);
        editTextMaxRPM = (EditText) view.findViewById(R.id.editTextMaxRPM);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeRefreshLayout.setOnRefreshListener(() -> presenter.loadTrip(true));
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgress();
        hideError();
    }

    public TripFragment() {
    }

    public static TripFragment newInstance(String tripId) {
        TripFragment fragment = new TripFragment();
        Bundle args = new Bundle();
        args.putString(TRIP_ID, tripId);
        fragment.setArguments(args);
        return fragment;
    }

    @ProvidePresenter
    TripPresenter provideTripPresenter() {
        App app = (App) getActivity().getApplicationContext();
        MojioClient mojioClient = app.getMojioClient();
        String tripId = getArguments().getString(TRIP_ID);
        return new TripPresenter(mojioClient, tripId);
    }

    @Override
    public void setTrip(Trip trip) {
        long duration = trip.getDuration();
        editTextDuration.setText(ToolsTime.intervalToString(duration));

        Distance distance = trip.getDistance();
        editTextDistance.setText(String.format("%.2f %s", distance.getValue(), distance.getBaseDistanceUnit().name()));

        Speed speedMax = trip.getMaxSpeed();
        editTextMaxSpeed.setText(String.format("%.2f %s", speedMax.getValue(), speedMax.getBaseSpeedUnit().name()));

        Rpm rpmMax = trip.getMaxRPM();
        editTextMaxRPM.setText(String.format("%.2f %s", rpmMax.getValue(), rpmMax.getBaseRpmUnit().name()));
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


}
