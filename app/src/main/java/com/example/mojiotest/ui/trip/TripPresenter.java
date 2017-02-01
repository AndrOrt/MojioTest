package com.example.mojiotest.ui.trip;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.mojiotest.R;

import io.moj.java.sdk.MojioClient;
import io.moj.java.sdk.model.Trip;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@InjectViewState
public class TripPresenter extends MvpPresenter<TripView> {

    private MojioClient mojioClient;
    private boolean isLoading;
    private final String tripId;
    private Trip trip;

    TripPresenter(MojioClient mojioClient, String tripId) {
        this.mojioClient = mojioClient;
        this.tripId = tripId;
        getViewState().setTitle(R.string.trip);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        loadTrip(false);
    }

    void loadTrip(boolean isRefreshing) {
        if (isLoading) {
            return;
        }
        //TODO check Internet Connection
        onLoadingStart(isRefreshing);
        mojioClient.rest().getTrip(tripId).enqueue(new Callback<Trip>() {
            @Override
            public void onResponse(Call<Trip> call, Response<Trip> response) {
                onLoadingFinish(isRefreshing);
                onLoadingSuccess(response.body());
            }

            @Override
            public void onFailure(Call<Trip> call, Throwable t) {
                onLoadingFinish(isRefreshing);
                onLoadingFailed(t);
            }
        });
    }

    private void onLoadingStart(boolean isRefreshing) {
        isLoading = true;
        getViewState().hideError();
        getViewState().onStartLoading();
        showProgress(isRefreshing);
    }

    private void showProgress(boolean isRefreshing) {
        if (isRefreshing) {
            getViewState().showRefreshing();
        } else {
            getViewState().showProgress();
        }
    }

    private void onLoadingFailed(Throwable throwable) {
        isLoading = false;
        getViewState().showError(throwable.toString());
    }

    private void onLoadingFinish(boolean isRefreshing) {
        isLoading = false;
        getViewState().onFinishLoading();
        hideProgress(isRefreshing);
    }

    private void hideProgress(boolean isRefreshing) {
        if (isRefreshing) {
            getViewState().hideRefreshing();
        } else {
            getViewState().hideProgress();
        }
    }

    private void onLoadingSuccess(Trip trip) {
        this.trip = trip;
        getViewState().setTrip(trip);
    }

    void onErrorCancel() {
        getViewState().hideError();
    }
}
