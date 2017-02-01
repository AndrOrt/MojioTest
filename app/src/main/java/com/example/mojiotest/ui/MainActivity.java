package com.example.mojiotest.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.mojiotest.R;
import com.example.mojiotest.ui.trips.TripListFragment;

import io.moj.java.sdk.MojioClient;

/**
 * Created by Andrei_Ortyashov on 2/1/2017.
 */
public class MainActivity extends AppCompatActivity implements TripListFragment.OnTripClickListener {

    App app;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app = ((App) getApplicationContext());
        final MojioClient mojioClient = app.getMojioClient();

        fragmentManager = getSupportFragmentManager();

/*        mojioClient.login("andr-ort@mail.ru", "EpamMojio").enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Toast.makeText(getApplicationContext(), response.body().getUserName(), Toast.LENGTH_SHORT).show();
                showTrips();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error Login", Toast.LENGTH_SHORT).show();
            }
        });*/

        if (savedInstanceState == null) {
            showTrips();
        }
    }

    private void showTrips() {
        TripListFragment fragment = new TripListFragment();
        fragmentManager.beginTransaction()
                .add(R.id.content, fragment, TripListFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public void viewTrip(String tripId) {
        Toast.makeText(getApplicationContext(), tripId, Toast.LENGTH_SHORT).show();
    }
}
