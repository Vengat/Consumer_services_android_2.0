package com.example.vengatr.consumer_services_android_20.notifier;

import com.example.vengatr.consumer_services_android_20.PostJobFragment;

/**
 * Created by vengat.r on 7/8/2015.
 */
public class OnPostJobCompletionNotifier {

    public OnPostJobCompletionNotifier(PostJobFragment.OnPostJobCompletionListener onPostJobCompletionListener) {
        onPostJobCompletionListener.onPostJobCompletion();
    }
}
