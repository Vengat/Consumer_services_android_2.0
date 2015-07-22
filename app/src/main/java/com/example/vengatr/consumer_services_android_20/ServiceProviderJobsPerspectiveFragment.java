package com.example.vengatr.consumer_services_android_20;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.vengatr.consumer_services_android_20.dummy.JobListContent;
import com.example.vengatr.consumer_services_android_20.model.Job;
import com.example.vengatr.consumer_services_android_20.rest_classes.GetJob;
import com.example.vengatr.consumer_services_android_20.util.CSProperties;
import com.example.vengatr.consumer_services_android_20.util.JobAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class ServiceProviderJobsPerspectiveFragment extends ListFragment {
    // private static String QUERY_URL_GET_MATCHING_JOBS_BY_MOBILE_NUMBER = "http://10.0.2.2:8080/serviceProviders/openAssignJobs/mobileNumber/";
    //ec2-52-74-141-170.ap-southeast-1.compute.amazonaws.com

    protected String getJobsURL;// =  "http://10.0.2.2:8080/serviceProviders/openAssignAgreedJobs/mobileNumber/";

    //protected String url =  "http://ec2-52-74-141-170.ap-southeast-1.compute.amazonaws.com:8080/serviceProviders/openAssignAgreedJobs/mobileNumber/";


    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private SPCallbacks mCallbacks = sDummyCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    private List<Job> jobs;


    ProgressDialog progressDialog;

    protected SharedPreferences mSharedPreferences;

    protected String mobileNumber;

    private String userType;

    private Context context;


    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface SPCallbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onSPItemSelected(long id);
    }

    /**
     * A dummy implementation of the {@link SPCallbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static SPCallbacks sDummyCallbacks = new SPCallbacks() {
        @Override
        public void onSPItemSelected(long id) {
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ServiceProviderJobsPerspectiveFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSharedPreferences = getActivity().getSharedPreferences("prefs", getActivity().MODE_PRIVATE);
        mobileNumber = mSharedPreferences.getString("phoneKey", "");
        userType = mSharedPreferences.getString("userTypeKey", "");

        // TODO: replace with a real list adapter.

    }


    @Override
    public void onResume() {
        super.onResume();
        System.out.println("I am onResume Service Provider Jobs Perspective");
        JobAdapter jobAdapter = new JobAdapter(getActivity(), (ArrayList<Job>) JobListContent.ITEMS);
        setListAdapter(jobAdapter);
        jobAdapter.notifyDataSetChanged();
        getJobsURL =  new CSProperties(context).getDomain()+"/serviceProviders/openAssignAgreedJobs/mobileNumber/";
        getJobs(getJobsURL + mobileNumber);
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
        System.out.println("*********************************onAttach********************************");
        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof SPCallbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (SPCallbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        mCallbacks.onSPItemSelected(JobListContent.ITEMS.get(position).getId());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
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
                e.printStackTrace();
            }
            return jobs;
        }

        @Override
        protected void onPostExecute(List<Job> jobs) {
            Toast.makeText(context, "SP Jobs perspective Data Sent!", Toast.LENGTH_LONG).show();
            if (jobs == null) return;
            if (jobs.isEmpty()) return;
            Log.i("", "Job list for sp is not null not empty");
            new JobListContent().setJobs(jobs);

            /*
            setListAdapter(new ArrayAdapter<Job>(
                    getActivity(),
                    android.R.layout.simple_list_item_activated_1,
                    //R.layout.custom_job_list,
                    android.R.id.text1,
                    JobListContent.ITEMS));*/
            JobAdapter jobAdapter = new JobAdapter(context, (ArrayList<Job>) JobListContent.ITEMS);
            setListAdapter(jobAdapter);
            jobAdapter.notifyDataSetChanged();
        }
    }
}
