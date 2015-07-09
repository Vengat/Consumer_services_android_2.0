package com.example.vengatr.consumer_services_android_20.notifier;

import com.example.vengatr.consumer_services_android_20.UserDetailsFragment;

/**
 * Created by vengat.r on 7/9/2015.
 */
public class UserDetailsNotFilledNotifier {
    public UserDetailsNotFilledNotifier(UserDetailsFragment.UserDetailsNotFilledListener userDetailsNotFilledListener) {
        userDetailsNotFilledListener.showDialog();
    }
}
