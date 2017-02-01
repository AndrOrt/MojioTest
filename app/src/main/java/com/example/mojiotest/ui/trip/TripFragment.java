package com.example.mojiotest.ui.trip;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.mojiotest.R;
import com.example.mojiotest.tools.ToolsTime;
import com.example.mojiotest.ui.App;

import java.util.Locale;

import io.moj.java.sdk.MojioClient;
import io.moj.java.sdk.model.Trip;
import io.moj.java.sdk.model.values.Distance;
import io.moj.java.sdk.model.values.Rpm;
import io.moj.java.sdk.model.values.Speed;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripFragment extends Fragment {
    private static final String TRIP_ID = "TRIP_ID";

    private String tripId;
    MojioClient mojioClient;
    Trip trip;
    EditText editTextDuration, editTextDistance, editTextMaxSpeed, editTextMaxRPM;

    public TripFragment() {
    }

    public static TripFragment newInstance(String tripId) {
        TripFragment fragment = new TripFragment();
        Bundle args = new Bundle();
        args.putString(TRIP_ID, tripId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tripId = getArguments().getString(TRIP_ID);
        }

        App app = (App) getActivity().getApplicationContext();
        mojioClient = app.getMojioClient();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTrip();
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

        getActivity().setTitle(R.string.trip);
    }

    private void loadTrip() {
        mojioClient.rest().getTrip(tripId).enqueue(new Callback<Trip>() {
            @Override
            public void onResponse(Call<Trip> call, Response<Trip> response) {
                trip = response.body();
                setTrip();
            }

            @Override
            public void onFailure(Call<Trip> call, Throwable t) {

            }
        });
    }

    private void setTrip() {
        long duration = trip.getDuration();
        editTextDuration.setText(ToolsTime.intervalToString(duration));

        Distance distance = trip.getDistance();
        editTextDistance.setText(String.format("%.2f %s", distance.getValue(), distance.getBaseDistanceUnit().name()));

        Speed speedMax = trip.getMaxSpeed();
        editTextMaxSpeed.setText(String.format("%.2f %s", speedMax.getValue(), speedMax.getBaseSpeedUnit().name()));

        Rpm rpmMax = trip.getMaxRPM();
        editTextMaxRPM.setText(String.format("%.2f %s", rpmMax.getValue(), rpmMax.getBaseRpmUnit().name()));
    }
}
