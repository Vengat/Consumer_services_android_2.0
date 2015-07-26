package com.example.vengatr.consumer_services_android_20;

import android.app.Activity;
import android.content.Context;
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
import com.example.vengatr.consumer_services_android_20.notifier.AgreedJobNotifier;
import com.example.vengatr.consumer_services_android_20.notifier.CancelJobListenerNotifier;
import com.example.vengatr.consumer_services_android_20.rest_classes.GetJob;
import com.example.vengatr.consumer_services_android_20.rest_classes.PutJob;
import com.example.vengatr.consumer_services_android_20.util.CustomerJobAdapter;
import com.example.vengatr.consumer_services_android_20.util.DateManipulation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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
    private Context context;

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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    public void convertDatePerTimeZone(Date date) {
        Calendar cal = Calendar.getInstance();
        TimeZone timeZone = TimeZone.getTimeZone("UTC+13:30");
        cal.setTimeZone(timeZone);
        cal.setTime(date);
        System.out.println("************************" + cal.get(Calendar.DAY_OF_MONTH));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_job_detail, container, false);
        rootView1 = rootView;
        Button cancel, agree, unassign;
        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            System.out.println("****" + mItem.getId());
            ((TextView) rootView.findViewById(R.id.jobId)).setText("Job Id : " + mItem.getId());
            ((TextView) rootView.findViewById(R.id.jobType)).setText("Job Type : " + mItem.getJobType().toString());
            ((TextView) rootView.findViewById(R.id.jobStatus)).setText("Job Status : " + mItem.getJobStatus().toString());
            ((TextView) rootView.findViewById(R.id.daySegment)).setText("Job day segment : "+mItem.getDaySegment().getDaySegment());
            ((TextView) rootView.findViewById(R.id.customerName)).setText("Customer Name : " + mItem.getCustomerName());
            ((TextView) rootView.findViewById(R.id.customerMobileNumber)).setText("Customer Mobile : "+mItem.getCustomerMobileNumber());
            ((TextView) rootView.findViewById(R.id.serviceproviderName)).setText("Service Provider Name : "+mItem.getServiceProviderName());
            ((TextView) rootView.findViewById(R.id.serviceProviderMobileNumber)).setText("Service Provider Mobile : "+mItem.getServiceProviderMobileNumber());
            ((TextView) rootView.findViewById(R.id.pincode)).setText("Pincode : "+mItem.getPincode());
            ((TextView) rootView.findViewById(R.id.dateinitiated)).setText("Date Initiated : "+ DateManipulation.dateFormatIST(mItem.getDateInitiated()));
            ((TextView) rootView.findViewById(R.id.customer_preferred_date)).setText("Preferred Date : " + DateManipulation.dateFormatIST(mItem.getDatePreferred()));
            convertDatePerTimeZone(mItem.getDatePreferred());
            if (mItem.getDateDone() == null) {
                ((TextView) rootView.findViewById(R.id.dateDone)).setText("Date Done : In Progress");
            } else {
                ((TextView) rootView.findViewById(R.id.dateDone)).setText("Date Done : "+ DateManipulation.dateFormatIST(mItem.getDateDone()));
            }

            ((TextView) rootView.findViewById(R.id.description)).setText(mItem.getDescription());
            cancel = (Button) rootView.findViewById(R.id.cancel_button);
            cancel.setOnClickListener(this);

            agree = (Button) rootView.findViewById(R.id.agree_button);
            agree.setEnabled(false);
            agree.setVisibility(View.GONE);
            agree.setOnClickListener(null);


            if (mItem.getJobStatus().toString().equalsIgnoreCase("assigned")) {
                agree.setEnabled(true);
                agree.setVisibility(View.VISIBLE);
                agree.setOnClickListener(this);
            }

            unassign = (Button) rootView.findViewById(R.id.unassign_button);
            unassign.setOnClickListener(this);
            if (!mItem.getJobStatus().toString().equalsIgnoreCase("agreed") && !mItem.getJobStatus().toString().equalsIgnoreCase("assigned")) {
                unassign.setEnabled(false);
                unassign.setVisibility(View.GONE);
                unassign.setOnClickListener(null);
            }


        }

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancel_button) {
            new CancelJobAsyncHttpTask().execute(String.valueOf(jobId));
            //new CancelJobListenerNotifier((JobDetailActivity) getActivity());
        } else if (v.getId() == R.id.agree_button) {
            new AgreedJobAsyncHttpTask().execute(String.valueOf(jobId));
        } else if (v.getId() == R.id.unassign_button) {
            new UnassignJobAsyncHttpTask().execute(String.valueOf(jobId));
        }
    }

    private class CancelJobAsyncHttpTask extends AsyncTask<String, Void, Job> {

        @Override
        protected Job doInBackground(String... urls) {
            GetJob getJob = new GetJob(context);
            Job job = null;
            Job cancelledJob = null;
            try {
                job =  getJob.getJobById(jobId);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                cancelledJob = new PutJob(context).cancelJob(job);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return cancelledJob;
        }

        @Override
        protected void onPostExecute(Job job) {
            Toast.makeText(getActivity(), "Job cancelled!", Toast.LENGTH_LONG).show();
            new JobListContent().removeJob(job);
            new CancelJobListenerNotifier((JobDetailActivity) getActivity());
        }
    }

    public interface CancelJobListener {
        void jobListPageTransition();
    }


    private class AgreedJobAsyncHttpTask extends AsyncTask<String, Void, Job> {

        @Override
        protected Job doInBackground(String... urls) {
            GetJob getJob = new GetJob(context);
            Job job = null;
            Job agreedJob = null;
            try {
                job =  getJob.getJobById(jobId);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                agreedJob = new PutJob(context).agreeJob(job);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return agreedJob;
        }

        @Override
        protected void onPostExecute(Job job) {
            Toast.makeText(getActivity(), "Job agreed!", Toast.LENGTH_LONG).show();
            new JobListContent().updateJob(job);
            new AgreedJobNotifier((JobDetailActivity) getActivity());
        }
    }

    public interface AgreedJobListener {
        void jobListPageTransition();
    }



    private class UnassignJobAsyncHttpTask extends AsyncTask<String, Void, Job> {

        @Override
        protected Job doInBackground(String... urls) {
            GetJob getJob = new GetJob(context);
            Job job = null;
            Job unassignedJob = null;
            try {
                job =  getJob.getJobById(jobId);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                unassignedJob = new PutJob(context).unassignJob(job);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return unassignedJob;
        }

        @Override
        protected void onPostExecute(Job job) {
            Toast.makeText(getActivity(), "Job unassigned!", Toast.LENGTH_LONG).show();
            new JobListContent().updateJob(job);
            new AgreedJobNotifier((JobDetailActivity) getActivity());
        }
    }
}
