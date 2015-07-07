package com.example.vengatr.consumer_services_android_20.notifier;

import com.example.vengatr.consumer_services_android_20.UserDetailsFragment;

/**
 * Created by vengat.r on 7/6/2015.
 */
public class SharedPreferencesSetNotifier {

    public SharedPreferencesSetNotifier(UserDetailsFragment.OnSharedPreferencesSetListener onSharedPreferencesSetListener) {
        onSharedPreferencesSetListener.jobListPageTransition();
    }

}
