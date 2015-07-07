package com.example.vengatr.consumer_services_android_20.notifier;

import com.example.vengatr.consumer_services_android_20.SPJobDetailFragment;

/**
 * Created by vengat.r on 7/8/2015.
 */
public class OnAssignOrCloseJobListenerNotifier {

    public OnAssignOrCloseJobListenerNotifier(SPJobDetailFragment.OnAssignOrCloseJobListener onAssignOrCloseJobListener) {
        onAssignOrCloseJobListener.jobListPageTransition();
    }
}
