package com.example.vengatr.consumer_services_android_20.notifier;

import com.example.vengatr.consumer_services_android_20.listener.NoInternetConnectivityListener;

/**
 * Created by vengat.r on 7/24/2015.
 */
public class NoInternetConnectivityNotifier {

    public NoInternetConnectivityNotifier(NoInternetConnectivityListener noInternetConnectivityListener) {
        noInternetConnectivityListener.onNoInternetConnectivity();
    }
}
