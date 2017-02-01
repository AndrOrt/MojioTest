package com.example.mojiotest.ui.trip;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import io.moj.java.sdk.model.Trip;

@StateStrategyType(AddToEndSingleStrategy.class)
interface TripView extends MvpView {

    void showProgress();

    void hideProgress();

    void showError(String message);

    void showError(int message);

    void hideError();

    void setTrip(Trip trip);

    void onStartLoading();

    void showRefreshing();

    void onFinishLoading();

    void hideRefreshing();

    void setTitle(int title);
}
