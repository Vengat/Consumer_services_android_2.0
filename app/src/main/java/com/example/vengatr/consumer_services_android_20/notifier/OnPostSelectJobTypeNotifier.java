package com.example.vengatr.consumer_services_android_20.notifier;

import com.example.vengatr.consumer_services_android_20.SelectJobTypeFragment;

/**
 * Created by vengat.r on 8/11/2015.
 */
public class OnPostSelectJobTypeNotifier {

    public OnPostSelectJobTypeNotifier(SelectJobTypeFragment.OnPostSelectJobTypeListener onPostSelectJobTypeListener, String jobTypeSelected) {
        onPostSelectJobTypeListener.onPostSelectJobType(jobTypeSelected);
    }
}
