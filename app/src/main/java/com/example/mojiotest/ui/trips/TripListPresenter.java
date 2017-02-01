package com.example.mojiotest.ui.trips;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.mojiotest.R;

import java.util.List;

import io.moj.java.sdk.MojioClient;
import io.moj.java.sdk.model.Trip;
import io.moj.java.sdk.model.response.ListResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@InjectViewState
public class TripListPresenter extends MvpPresenter<TripListView> {

    private MojioClient mojioClient;
    private boolean isLoading;

    TripListPresenter(MojioClient mojioClient) {
        this.mojioClient = mojioClient;
        getViewState().setTitle(R.string.trips);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        loadTrips(false);
    }

    void loadTrips(boolean isRefreshing) {
        if (isLoading) {
            return;
        }
        //TODO check Internet Connection
        onLoadingStart(isRefreshing);
        mojioClient.rest().getTrips().enqueue(new Callback<ListResponse<Trip>>() {
            @Override
            public void onResponse(Call<ListResponse<Trip>> call, Response<ListResponse<Trip>> response) {
                List<Trip> list = response.body().getData();
                onLoadingFinish(isRefreshing);
                onLoadingSuccess(list);
            }

            @Override
            public void onFailure(Call<ListResponse<Trip>> call, Throwable t) {
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

    private void onLoadingSuccess(List<Trip> list) {
        getViewState().setTrips(list);
    }

    void onErrorCancel() {
        getViewState().hideError();
    }
}
