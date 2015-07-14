package com.example.vengatr.consumer_services_android_20.notifier;

import com.example.vengatr.consumer_services_android_20.listener.AdapterUpdateListener;

/**
 * Created by vengat.r on 7/14/2015.
 */
public class AdapterUpdateNotifier {

    public AdapterUpdateNotifier(AdapterUpdateListener adapterUpdateListener) {
        adapterUpdateListener.onUpdateAdapter();
    }
}
