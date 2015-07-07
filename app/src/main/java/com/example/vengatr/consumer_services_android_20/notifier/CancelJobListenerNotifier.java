package com.example.vengatr.consumer_services_android_20.notifier;

import com.example.vengatr.consumer_services_android_20.JobDetailFragment;

/**
 * Created by vengat.r on 7/8/2015.
 */
public class CancelJobListenerNotifier {

    public CancelJobListenerNotifier(JobDetailFragment.CancelJobListener cancelJobListener) {
        cancelJobListener.jobListPageTransition();
    }
}
