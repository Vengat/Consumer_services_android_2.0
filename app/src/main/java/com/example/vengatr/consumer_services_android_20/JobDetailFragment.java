package com.example.vengatr.consumer_services_android_20;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vengatr.consumer_services_android_20.dummy.JobListContent;
import com.example.vengatr.consumer_services_android_20.model.Job;
import com.example.vengatr.consumer_services_android_20.model.JobStatus;
import com.example.vengatr.consumer_services_android_20.model.JobType;
import com.example.vengatr.consumer_services_android_20.notifier.CancelJobListenerNotifier;
import com.example.vengatr.consumer_services_android_20.rest_classes.GetJob;
import com.example.vengatr.consumer_services_android_20.rest_classes.PutJob;
import com.example.vengatr.consumer_services_android_20.util.CustomerJobAdapter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A fragment representing a single Job detail screen.
 * This fragment is either contained in a {@link JobListActivity}
 * in two-pane mode (on tablets) or a {@link JobDetailActivity}
 * on handsets.
 */
public class JobDetailFragment extends Fragment implements View.OnClickListener {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Job mItem;
    private long jobId;
    View rootView1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public JobDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = JobListContent.JOB_ITEM_MAP.get(getArguments().getLong(ARG_ITEM_ID));
            jobId = mItem.getId();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_job_detail, container, false);
        rootView1 = rootView;
        Button cancel;
        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            System.out.println("****" + mItem.getId());
            ((TextView) rootView.findViewById(R.id.jobId)).setText("Job Id : " + mItem.getId());
            ((TextView) rootView.findViewById(R.id.jobType)).setText("Job Type : "+mItem.getJobType().toString());
            ((TextView) rootView.findViewById(R.id.jobStatus)).setText("Job Status : "+mItem.getJobStatus().toString());
            ((TextView) rootView.findViewById(R.id.customerName)).setText("Customer Name : "+mItem.getCustomerName());
            ((TextView) rootView.findViewById(R.id.customerMobileNumber)).setText("Customer Mobile : "+mItem.getCustomerMobileNumber());
            ((TextView) rootView.findViewById(R.id.serviceproviderName)).setText("Service Provider Name : "+mItem.getServiceProviderName());
            ((TextView) rootView.findViewById(R.id.serviceProviderMobileNumber)).setText("Service Provider Mobile : "+mItem.getServiceProviderMobileNumber());
            ((TextView) rootView.findViewById(R.id.pincode)).setText("Pincode : "+mItem.getPincode());
            ((TextView) rootView.findViewById(R.id.dateinitiated)).setText("Date Initiated : "+ mItem.getDateInitiated());
            ((TextView) rootView.findViewById(R.id.dateDone)).setText("Date Done : "+mItem.getDateDone());
            ((TextView) rootView.findViewById(R.id.description)).setText(mItem.getDescription());
            cancel = (Button) rootView.findViewById(R.id.cancel_button);
            cancel.setOnClickListener(this);
        }

        return rootView;
    }

    @Override
    public void onClick(View v) {
        new CancelJobAsyncHttpTask().execute(String.valueOf(jobId));
        //new CancelJobListenerNotifier((JobDetailActivity) getActivity());
    }

    private class CancelJobAsyncHttpTask extends AsyncTask<String, Void, Job> {

        @Override
        protected Job doInBackground(String... urls) {
            GetJob getJob = new GetJob();
            Job job = null;
            Job cancelledJob = null;
            try {
                job =  getJob.getJobById(jobId);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                cancelledJob = new PutJob ().cancelJob(job);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("*******Job customer name" + job.getCustomerName(), "");
            return cancelledJob;
        }

        @Override
        protected void onPostExecute(Job job) {
            Toast.makeText(getActivity(), "Job cancelled!", Toast.LENGTH_LONG).show();
            Log.d("*******Job customer name" + job.getCustomerName(), "");
            new JobListContent().removeJob(job);
            new CancelJobListenerNotifier((JobDetailActivity) getActivity());
        }
    }

    public interface CancelJobListener {
        void jobListPageTransition();
    }



}
