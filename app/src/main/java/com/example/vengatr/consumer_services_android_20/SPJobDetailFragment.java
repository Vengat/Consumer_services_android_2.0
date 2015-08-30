package com.example.vengatr.consumer_services_android_20;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vengatr.consumer_services_android_20.dummy.JobListContent;
import com.example.vengatr.consumer_services_android_20.listener.ServiceProviderBillClickListener;
import com.example.vengatr.consumer_services_android_20.model.Job;
import com.example.vengatr.consumer_services_android_20.model.ServiceProvider;
import com.example.vengatr.consumer_services_android_20.notifier.OnAssignOrCloseJobListenerNotifier;
import com.example.vengatr.consumer_services_android_20.notifier.ServiceProviderBillClickNotifier;
import com.example.vengatr.consumer_services_android_20.rest_classes.GetServiceProvider;
import com.example.vengatr.consumer_services_android_20.rest_classes.PutJob;
import com.example.vengatr.consumer_services_android_20.util.DateManipulation;

import java.io.IOException;

/**
 * Created by vengat.r on 7/6/2015.
 */
public class SPJobDetailFragment extends Fragment implements View.OnClickListener {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Job mItem;
    private SharedPreferences mSharedPreferences;
    private static final String PREFS = "prefs";
    private static final String NAME = "nameKey";
    private static final String PHONE = "phoneKey";
    private String mobileNumber;

    private ServiceProvider currentServiceProvider;

    private GetServiceProviderAsyncHttpTask getServiceProviderAsyncHttpTask;
    private AssignJobAsyncHttpTask assignJobAsyncHttpTask;
    private StartJobAsyncHttpTask startJobAsyncHttpTask;
    private CloseJobAsyncHttpTask closeJobAsyncHttpTask;

    private long jobId;
    private Context context;

    ProgressDialog progressDialog;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SPJobDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences = getActivity().getSharedPreferences(PREFS, getActivity().MODE_PRIVATE);
        mobileNumber = mSharedPreferences.getString(PHONE, "");

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = JobListContent.JOB_ITEM_MAP.get(getArguments().getLong(ARG_ITEM_ID));
            jobId = mItem.getId();
        }
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        getServiceProviderAsyncHttpTask = new GetServiceProviderAsyncHttpTask();
        progressDialog.show();
        getServiceProviderAsyncHttpTask.execute(mobileNumber);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sp_fragment_job_detail, container, false);
        Button assign, start, close, bill;
        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            System.out.println("****" + mItem.getId());
            ((TextView) rootView.findViewById(R.id.jobId)).setText("Job Id : " + mItem.getId());
            ((TextView) rootView.findViewById(R.id.jobType)).setText("Job Type : " + mItem.getJobType().toString());
            ((TextView) rootView.findViewById(R.id.jobStatus)).setText("Job Status : " + mItem.getJobStatus().toString());
            ((TextView) rootView.findViewById(R.id.customerName)).setText("Customer Name : "+mItem.getCustomerName());
            ((TextView) rootView.findViewById(R.id.customerMobileNumber)).setText("Customer Mobile : "+mItem.getCustomerMobileNumber());
            //((TextView) rootView.findViewById(R.id.serviceproviderName)).setText("Service Provider Name : "+mItem.getServiceProviderName());
            //((TextView) rootView.findViewById(R.id.serviceProviderMobileNumber)).setText("Service Provider Mobile : "+(mItem.getServiceProviderMobileNumber() != 0 ? mItem.getServiceProviderMobileNumber() : ""));
            ((TextView) rootView.findViewById(R.id.pincode)).setText("Pincode : "+mItem.getPincode());
            ((TextView) rootView.findViewById(R.id.dateinitiated)).setText("Date Initiated : "+ DateManipulation.dateFormatIST(mItem.getDateInitiated()));
            ((TextView) rootView.findViewById(R.id.customer_preferred_date)).setText("Scheduled Date : "+ DateManipulation.dateFormatIST(mItem.getDatePreferred()));
            ((TextView) rootView.findViewById(R.id.daySegment)).setText("Job day segment : "+mItem.getDaySegment().getDaySegment());

            if (mItem.getDateDone() == null) {
                ((TextView) rootView.findViewById(R.id.dateStarted)).setText("Date Sarted : Not started yet");
            } else {
                ((TextView) rootView.findViewById(R.id.dateStarted)).setText("Date Done : "+ DateManipulation.dateFormatIST(mItem.getDateDone()));
            }
            
            if (mItem.getDateDone() == null) {
                ((TextView) rootView.findViewById(R.id.dateDone)).setText("Date Done : In Progress");
            } else {
                ((TextView) rootView.findViewById(R.id.dateDone)).setText("Date Done : "+ DateManipulation.dateFormatIST(mItem.getDateDone()));
            }
            ((TextView) rootView.findViewById(R.id.description)).setText(mItem.getDescription());


            assign = (Button) rootView.findViewById(R.id.assign_button);
            assign.setOnClickListener(this);

            if (mItem.getJobStatus().toString().equalsIgnoreCase("assigned") || mItem.getJobStatus().toString().equalsIgnoreCase("agreed")) {
                assign.setEnabled(false);
                assign.setVisibility(View.GONE);
                assign.setOnClickListener(null);
            }

            if (mItem.getJobStatus().toString().equalsIgnoreCase("agreed") && mItem.getServiceProviderMobileNumber() == Long.parseLong(mobileNumber)) {
                start = (Button) rootView.findViewById(R.id.start_button);
                start.setOnClickListener(this);
            } else {
                start = (Button) rootView.findViewById(R.id.start_button);
                start.setEnabled(false);
                start.setVisibility(View.GONE);
                start.setOnClickListener(null);
            }

            if (mItem.getJobStatus().toString().equalsIgnoreCase("wip") && mItem.getServiceProviderMobileNumber() == Long.parseLong(mobileNumber)) {
                close = (Button) rootView.findViewById(R.id.close_button);
                close.setOnClickListener(this);
            } else {
                close = (Button) rootView.findViewById(R.id.close_button);
                close.setEnabled(false);
                close.setVisibility(View.GONE);
                close.setOnClickListener(null);
            }

            if (mItem.getJobStatus().toString().equalsIgnoreCase("closed") && mItem.getServiceProviderMobileNumber() == Long.parseLong(mobileNumber)) {
                bill = (Button) rootView.findViewById(R.id.bill_button);
                bill.setOnClickListener(this);
            } else {
                bill = (Button) rootView.findViewById(R.id.bill_button);
                bill.setEnabled(false);
                bill.setVisibility(View.GONE);
                bill.setOnClickListener(null);
            }

        }

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.assign_button) {
            progressDialog.show();
            assignJobAsyncHttpTask = new AssignJobAsyncHttpTask();
            if(getServiceProviderAsyncHttpTask .getStatus() == AsyncTask.Status.FINISHED) {
                assignJobAsyncHttpTask.execute(String.valueOf(jobId));
            }
            System.out.println("Assigned");
        } else if (v.getId() == R.id.close_button) {
            progressDialog.show();
            closeJobAsyncHttpTask = new CloseJobAsyncHttpTask();
            if(getServiceProviderAsyncHttpTask .getStatus() == AsyncTask.Status.FINISHED) {
                closeJobAsyncHttpTask.execute(String.valueOf(jobId));
            }
        } else if (v.getId() == R.id.start_button) {
            progressDialog.show();
            startJobAsyncHttpTask = new StartJobAsyncHttpTask();
            if(getServiceProviderAsyncHttpTask .getStatus() == AsyncTask.Status.FINISHED) {
                startJobAsyncHttpTask.execute(String.valueOf(jobId));
            }
        } else if (v.getId() == R.id.bill_button) {
            new ServiceProviderBillClickNotifier((ServiceProviderBillClickListener) getActivity());
        }
    }

    private class GetServiceProviderAsyncHttpTask extends AsyncTask<String, Void, ServiceProvider> {

        @Override
        protected ServiceProvider doInBackground(String... params) {
            GetServiceProvider getServiceProvider = new GetServiceProvider(context);
            ServiceProvider sp = null;

            try {
                sp =  getServiceProvider.getServiceProvider(Long.parseLong(mobileNumber));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return sp;
        }

        @Override
        protected void onPostExecute(ServiceProvider sp) {
            progressDialog.dismiss();
            currentServiceProvider = sp;
        }
    }

    private class AssignJobAsyncHttpTask extends AsyncTask<String, Void, Job> {

        @Override
        protected Job doInBackground(String... params) {
            Job job = null;
            try {
                if (currentServiceProvider == null) System.out.println("&&&&&&Current Service Provider is null");
                job = new PutJob(context).assignJob(Long.parseLong(params[0]), currentServiceProvider);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return job;
        }

        @Override
        protected void onPostExecute(Job job) {
            Toast.makeText(getActivity(), "Job Assigned", Toast.LENGTH_LONG).show();
            new JobListContent().updateJob(job);
            new OnAssignOrCloseJobListenerNotifier((JobDetailActivity) getActivity());
            progressDialog.dismiss();
        }
    }

    private class StartJobAsyncHttpTask extends AsyncTask<String, Void, Job> {

        @Override
        protected Job doInBackground(String... params) {
            Job job = null;
            try {
                if (currentServiceProvider == null) System.out.println("&&&&&&Current Service Provider is null");
                job = new PutJob(context).startJob(Long.parseLong(params[0]), currentServiceProvider);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return job;
        }

        @Override
        protected void onPostExecute(Job job) {
            Toast.makeText(getActivity(), "Job Assigned", Toast.LENGTH_LONG).show();
            new JobListContent().updateJob(job);
            new OnAssignOrCloseJobListenerNotifier((JobDetailActivity) getActivity());
            progressDialog.dismiss();
        }
    }

    private class CloseJobAsyncHttpTask extends AsyncTask<String, Void, Job> {

        @Override
        protected Job doInBackground(String... params) {
            Job job = null;
            try {
                if (currentServiceProvider == null) System.out.println("&&&&&&Current Service Provider is null");
                job = new PutJob(context).closeJob(Long.parseLong(params[0]));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return job;
        }

        @Override
        protected void onPostExecute(Job job) {
            Toast.makeText(getActivity(), "Job Assigned", Toast.LENGTH_LONG).show();
            new JobListContent().removeJob(job);
            new OnAssignOrCloseJobListenerNotifier((JobDetailActivity) getActivity());
            progressDialog.dismiss();
        }
    }


    public interface OnAssignOrCloseJobListener {
        void jobListPageTransition();
    }

}
