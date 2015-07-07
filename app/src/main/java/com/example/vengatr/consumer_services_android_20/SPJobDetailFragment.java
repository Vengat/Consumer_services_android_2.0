package com.example.vengatr.consumer_services_android_20;

import android.content.SharedPreferences;
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
import com.example.vengatr.consumer_services_android_20.model.ServiceProvider;
import com.example.vengatr.consumer_services_android_20.notifier.OnAssignOrCloseJobListenerNotifier;
import com.example.vengatr.consumer_services_android_20.rest_classes.GetServiceProvider;
import com.example.vengatr.consumer_services_android_20.rest_classes.PutJob;

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
        }
        getServiceProviderAsyncHttpTask = new GetServiceProviderAsyncHttpTask();
        getServiceProviderAsyncHttpTask.execute(mobileNumber);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sp_fragment_job_detail, container, false);
        Button assign, close;
        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            System.out.println("****" + mItem.getId());
            ((TextView) rootView.findViewById(R.id.jobId)).setText("Job Id : " + mItem.getId());
            ((TextView) rootView.findViewById(R.id.jobType)).setText("Job Type : " + mItem.getJobType().toString());
            ((TextView) rootView.findViewById(R.id.jobStatus)).setText("Job Status : "+mItem.getJobStatus().toString());
            ((TextView) rootView.findViewById(R.id.customerName)).setText("Customer Name : "+mItem.getCustomerName());
            ((TextView) rootView.findViewById(R.id.customerMobileNumber)).setText("Customer Mobile : "+mItem.getCustomerMobileNumber());
            ((TextView) rootView.findViewById(R.id.serviceproviderName)).setText("Service Provider Name : "+mItem.getServiceProviderName());
            ((TextView) rootView.findViewById(R.id.serviceProviderMobileNumber)).setText("Service Provider Mobile : "+mItem.getServiceProviderMobileNumber());
            ((TextView) rootView.findViewById(R.id.pincode)).setText("Pincode : "+mItem.getPincode());
            ((TextView) rootView.findViewById(R.id.dateinitiated)).setText("Date Initiated : "+ mItem.getDateInitiated());
            ((TextView) rootView.findViewById(R.id.dateDone)).setText("Date Done : "+mItem.getDateDone());
            ((TextView) rootView.findViewById(R.id.description)).setText(mItem.getDescription());
            assign = (Button) rootView.findViewById(R.id.assign_button);
            assign.setOnClickListener(this);

            close = (Button) rootView.findViewById(R.id.close_button);
            close.setOnClickListener(this);
        }

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.assign_button) {
            assignJobAsyncHttpTask = new AssignJobAsyncHttpTask();
            if(getServiceProviderAsyncHttpTask .getStatus() == AsyncTask.Status.FINISHED) {
                assignJobAsyncHttpTask.execute(ARG_ITEM_ID);
            }
            System.out.println("Assigned");
        } else if (v.getId() == R.id.close_button) {

        }

    }

    private class GetServiceProviderAsyncHttpTask extends AsyncTask<String, Void, ServiceProvider> {

        @Override
        protected ServiceProvider doInBackground(String... params) {
            GetServiceProvider getServiceProvider = new GetServiceProvider();
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
            currentServiceProvider = sp;
        }
    }

    private class AssignJobAsyncHttpTask extends AsyncTask<String, Void, Job> {

        @Override
        protected Job doInBackground(String... params) {
            Job job = null;
            try {
                if (currentServiceProvider == null) System.out.println("&&&&&&Current Service Provider is null");
                job = new PutJob().assignJob(Long.parseLong(ARG_ITEM_ID), currentServiceProvider);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return job;
        }

        @Override
        protected void onPostExecute(Job job) {
            Toast.makeText(getActivity(), "Job Assigned", Toast.LENGTH_LONG).show();
            new OnAssignOrCloseJobListenerNotifier((JobDetailActivity) getActivity());
        }
    }

    private class CloseJobAsyncHttpTask extends AsyncTask<String, Void, Job> {

        @Override
        protected Job doInBackground(String... params) {
            Job job = null;
            try {
                if (currentServiceProvider == null) System.out.println("&&&&&&Current Service Provider is null");
                job = new PutJob().assignJob(Long.parseLong(ARG_ITEM_ID), currentServiceProvider);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return job;
        }

        @Override
        protected void onPostExecute(Job job) {
            Toast.makeText(getActivity(), "Job Assigned", Toast.LENGTH_LONG).show();
            new OnAssignOrCloseJobListenerNotifier((JobDetailActivity) getActivity());
        }
    }


    public interface OnAssignOrCloseJobListener {
        void jobListPageTransition();
    }

}