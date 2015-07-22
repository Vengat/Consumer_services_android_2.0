package com.example.vengatr.consumer_services_android_20.notifier;

import com.example.vengatr.consumer_services_android_20.JobDetailFragment;

/**
 * Created by vengat.r on 7/20/2015.
 */
public class AgreedJobNotifier {

    public AgreedJobNotifier(JobDetailFragment.AgreedJobListener agreedJobListener) {
        agreedJobListener.jobListPageTransition();
    }
}
