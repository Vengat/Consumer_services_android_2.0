package com.example.vengatr.consumer_services_android_20.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.vengatr.consumer_services_android_20.JobListActivity;
import com.example.vengatr.consumer_services_android_20.dummy.JobListContent;
import com.example.vengatr.consumer_services_android_20.model.Job;
import com.example.vengatr.consumer_services_android_20.notifier.NoJobsNotifierPostExecuteJobListFragment;
import com.example.vengatr.consumer_services_android_20.rest_classes.GetJob;
import com.example.vengatr.consumer_services_android_20.util.CustomerJobAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vengat.r on 9/14/2015.
 */
public class JobListService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Service is created", Toast.LENGTH_LONG).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service is started", Toast.LENGTH_LONG).show();
        context = this;
        long startTime = System.currentTimeMillis();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service is stopped", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

    protected void getJobs(String url) {
        new GetJobsAsyncTask().execute(url);
    }

    private class GetJobsAsyncTask extends AsyncTask<String, String, List<Job>> {

        @Override
        protected List<Job> doInBackground(String... urls) {
            List<Job> jobs = null;
            GetJob getJob = new GetJob(context);
            try {
                jobs = getJob.getJobs(urls[0]);
            } catch (IOException e) {
                jobs = null;
                e.printStackTrace();
            }
            return jobs;
        }

        @Override
        protected void onPostExecute(List<Job> jobs) {
            Toast.makeText(context, "Jobs", Toast.LENGTH_LONG).show();
            boolean displayJobs = false;
            //ArrayList<Job> customerJobs = (ArrayList<Job>) jobs;
            if (jobs == null) return;
            if (!jobs.isEmpty()) {
                new JobListContent().setJobs(jobs);

            /*
            setListAdapter(new ArrayAdapter<Job>(
                    getActivity(),
                    android.R.layout.simple_list_item_activated_1,
                    //R.layout.custom_job_list,
                    android.R.id.text1,
                    JobListContent.ITEMS));*/

                new JobListContent().removeClosedJobs();
                CustomerJobAdapter customerJobAdapter = new CustomerJobAdapter(context, (ArrayList<Job>) JobListContent.ITEMS);
                //setListAdapter(customerJobAdapter);
                customerJobAdapter.notifyDataSetChanged();

                for (Job job: JobListContent.ITEMS) {
                    String jobStatus = job.getJobStatus().toString();
                    Log.i("", "Job status to be displayed is " + jobStatus);
                    if (jobStatus.equalsIgnoreCase("open") || jobStatus.equalsIgnoreCase("closed") || jobStatus.equalsIgnoreCase("assigned") || jobStatus.equalsIgnoreCase("agreed")) {
                        displayJobs = true;
                    }
                }

                if (!displayJobs) {
                    System.out.println("No-jobs notifier notified haha");
                    new NoJobsNotifierPostExecuteJobListFragment((JobListActivity) context);
                }

            } else {
                System.out.println("No-jobs notifier notified");
                new NoJobsNotifierPostExecuteJobListFragment((JobListActivity) context);
            }

        }
    }


}
