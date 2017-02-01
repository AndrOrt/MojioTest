package com.example.mojiotest.ui.trips;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import io.moj.java.sdk.model.Trip;

@StateStrategyType(AddToEndSingleStrategy.class)
interface TripListView extends MvpView {

    void showProgress();

    void hideProgress();

    void showError(String message);

    void showError(int message);

    void hideError();

    void setTrips(List<Trip> tripList);

    void onStartLoading();

    void showRefreshing();

    void onFinishLoading();

    void hideRefreshing();

    void setTitle(int title);
}
