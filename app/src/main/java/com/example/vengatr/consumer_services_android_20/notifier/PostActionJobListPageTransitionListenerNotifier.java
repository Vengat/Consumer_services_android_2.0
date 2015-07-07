package com.example.vengatr.consumer_services_android_20.notifier;

import com.example.vengatr.consumer_services_android_20.listener.PostActionJobListPageTransitionListener;

/**
 * Created by vengat.r on 7/8/2015.
 */
public class PostActionJobListPageTransitionListenerNotifier {

    public PostActionJobListPageTransitionListenerNotifier(PostActionJobListPageTransitionListener postActionJobListPageTransitionListener) {
        postActionJobListPageTransitionListener.jobListPageTransition();
    }
}
