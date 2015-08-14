package com.example.vengatr.consumer_services_android_20.notifier;

import com.example.vengatr.consumer_services_android_20.JobListFragment;

/**
 * Created by vengat.r on 8/14/2015.
 */
public class JobListFragmentDisplayedNotifier {
    public JobListFragmentDisplayedNotifier(JobListFragment.JobListFragmentDisplayedListener jobListFragmentDisplayedListener) {
        jobListFragmentDisplayedListener.onJobListFragmentDisplayed();
    }
}
