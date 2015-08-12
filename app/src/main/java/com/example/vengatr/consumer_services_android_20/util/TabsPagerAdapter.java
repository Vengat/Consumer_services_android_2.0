package com.example.vengatr.consumer_services_android_20.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.vengatr.consumer_services_android_20.EmptyFragment;
import com.example.vengatr.consumer_services_android_20.JobListActivity;
import com.example.vengatr.consumer_services_android_20.JobListFragment;
import com.example.vengatr.consumer_services_android_20.ReferInviteFragment;
import com.example.vengatr.consumer_services_android_20.ServiceProviderJobsPerspectiveFragment;

/**
 * Created by vengat.r on 8/12/2015.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    SharedPreferences mSharedPreferences;
    private String userType;

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
        userType = JobListActivity.userTypeRendered;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case 0:
                return userType.equalsIgnoreCase("customer") ? new JobListFragment() : new ServiceProviderJobsPerspectiveFragment();
            case 1:
                return new ReferInviteFragment();
                //return new EmptyFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
