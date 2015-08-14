package com.example.vengatr.consumer_services_android_20.notifier;

import com.example.vengatr.consumer_services_android_20.ReferInviteFragment;

/**
 * Created by vengat.r on 8/14/2015.
 */
public class ReferInviteFragmentDisplayedNotifier {
    public ReferInviteFragmentDisplayedNotifier(ReferInviteFragment.ReferInviteFragmentDisplayedListener referInviteFragmentDisplayedListener) {
        referInviteFragmentDisplayedListener.onReferInviteFragmentDisplayed();
    }
}
