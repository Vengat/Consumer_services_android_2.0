package com.example.vengatr.consumer_services_android_20.notifier;

import com.example.vengatr.consumer_services_android_20.listener.JobListPageTransitionListener;

/**
 * Created by vengat.r on 8/30/2015.
 */
public class JobListPageTransitionNotifier {
    public JobListPageTransitionNotifier(JobListPageTransitionListener jobListPageTransitionListener) {
        jobListPageTransitionListener.navigateToJobList();
    }
}
