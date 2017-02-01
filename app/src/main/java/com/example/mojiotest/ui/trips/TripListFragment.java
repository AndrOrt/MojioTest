package com.example.mojiotest.ui.trips;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mojiotest.R;
import com.example.mojiotest.ui.App;

import java.util.List;

import io.moj.java.sdk.MojioClient;
import io.moj.java.sdk.model.Trip;
import io.moj.java.sdk.model.response.ListResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Andrei_Ortyashov on 2/1/2017.
 */
public class TripListFragment extends Fragment {

    private OnTripClickListener mListener;

    TripViewAdapter adapter;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    MojioClient mojioClient;

    public TripListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App app = (App) getActivity().getApplicationContext();
        mojioClient = app.getMojioClient();

        loadTrips();
    }

    private void loadTrips() {
        mojioClient.rest().getTrips().enqueue(new Callback<ListResponse<Trip>>() {
            @Override
            public void onResponse(Call<ListResponse<Trip>> call, Response<ListResponse<Trip>> response) {
                List<Trip> tripList = response.body().getData();
                adapter.setTrips(tripList);
            }

            @Override
            public void onFailure(Call<ListResponse<Trip>> call, Throwable t) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.swipe_recycle_view, container, false);
        return view;
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

        swipeRefreshLayout.setOnRefreshListener(() -> loadTrips());

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

    public interface OnTripClickListener {
        void viewTrip(String tripId);
    }
}
