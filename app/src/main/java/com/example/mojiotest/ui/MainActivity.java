package com.example.mojiotest.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.mojiotest.R;
import com.example.mojiotest.ui.trip.TripFragment;
import com.example.mojiotest.ui.trips.TripListFragment;

/**
 * Created by Andrei_Ortyashov on 2/1/2017.
 */
public class MainActivity extends AppCompatActivity implements
        FragmentManager.OnBackStackChangedListener,
        TripListFragment.OnTripClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            TripListFragment fragment = new TripListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content, fragment, TripListFragment.class.getSimpleName())
                    .commit();
        }

        getSupportFragmentManager().addOnBackStackChangedListener(this);
        shouldDisplayHomeUp();
    }

    private void shouldDisplayHomeUp() {
        boolean canback = getSupportFragmentManager().getBackStackEntryCount() >= 1;
        getSupportActionBar().setDisplayHomeAsUpEnabled(canback);
    }

    @Override
    public boolean onSupportNavigateUp() {
        getSupportFragmentManager().popBackStack();
        return true;
    }

    @Override
    public void viewTrip(String tripId) {
        TripFragment fragment = TripFragment.newInstance(tripId);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, fragment, TripFragment.class.getSimpleName())
                .addToBackStack(TripFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public void onBackStackChanged() {
        shouldDisplayHomeUp();
    }
}
