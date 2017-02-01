package com.example.mojiotest.ui.trips;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mojiotest.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import io.moj.java.sdk.model.Trip;
import io.moj.java.sdk.model.values.Address;
import io.moj.java.sdk.model.values.Location;

/**
 * Created by Andrei_Ortyashov on 2/1/2017.
 */
class TripViewAdapter extends RecyclerView.Adapter<TripViewAdapter.ViewHolder> {
    private List<Trip> list;
   private TripListFragment.OnTripClickListener mListener;
    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm dd MMM yyyy");

    public TripViewAdapter(TripListFragment.OnTripClickListener mListener) {
        this.list = new ArrayList<>();
        this.mListener = mListener;
    }

    @Override
    public TripViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip, parent, false);
        return new TripViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TripViewAdapter.ViewHolder holder, int position) {
        holder.trip = list.get(position);
        holder.bindViewHolder();

        holder.mView.setOnClickListener(v -> mListener.viewTrip(holder.trip.getId()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setTrips(List<Trip> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        Trip trip;

        TextView tvCreatedOn;
        TextView tvStartAddress;
        TextView tvEndAddress;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvCreatedOn = (TextView) view.findViewById(R.id.tvCreatedOn);
            tvStartAddress = (TextView) view.findViewById(R.id.tvStartAddress);
            tvEndAddress = (TextView) view.findViewById(R.id.tvEndAddress);
        }

        public void bindViewHolder() {
            long create = trip.getCreatedOn();
            String CreateOn = DATE_FORMAT.format(create);
            tvCreatedOn.setText(CreateOn);

            Location locationStart = trip.getStartLocation();
            Address startAddress = locationStart.getAddress();
            tvStartAddress.setText(startAddress.getFormattedAddress());

            Location locationEnd = trip.getEndLocation();
            Address endAddress = locationEnd.getAddress();
            tvEndAddress.setText(endAddress.getFormattedAddress());
        }
    }
}