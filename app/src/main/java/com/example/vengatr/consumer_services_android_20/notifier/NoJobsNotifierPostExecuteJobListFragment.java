package com.example.vengatr.consumer_services_android_20.notifier;


import com.example.vengatr.consumer_services_android_20.JobListFragment;

/**
 * Created by vengat.r on 7/9/2015.
 */
public class NoJobsNotifierPostExecuteJobListFragment {

    public NoJobsNotifierPostExecuteJobListFragment(JobListFragment.NoJobsListenerPostExecuteJobListFragment noJobsListenerPostExecuteJobListFragment) {
        noJobsListenerPostExecuteJobListFragment.showSelectJobTypeFragment();
    }

}
