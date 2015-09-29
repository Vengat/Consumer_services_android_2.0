package com.example.vengatr.consumer_services_android_20;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.vengatr.consumer_services_android_20.dummy.JobListContent;
import com.example.vengatr.consumer_services_android_20.model.Job;
import com.example.vengatr.consumer_services_android_20.notifier.JobListFragmentDisplayedNotifier;
import com.example.vengatr.consumer_services_android_20.notifier.NoJobsNotifierPostExecuteJobListFragment;
import com.example.vengatr.consumer_services_android_20.rest_classes.GetJob;
import com.example.vengatr.consumer_services_android_20.service.JobListService;
import com.example.vengatr.consumer_services_android_20.util.CSProperties;
import com.example.vengatr.consumer_services_android_20.util.CustomerJobAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A list fragment representing a list of Jobs. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link JobDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class JobListFragment extends ListFragment {



    public static String TAG = "JobListFragment";
    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    private List<Job> jobs;

    private String getJobsURL; // ="http://10.0.2.2:8080/customers/jobs/mobileNumber/";

    //protected String url ="http://ec2-52-74-141-170.ap-southeast-1.compute.amazonaws.com:8080/customers/jobs/mobileNumber/";

    ProgressDialog progressDialog;

    protected SharedPreferences mSharedPreferences;

    protected String mobileNumber;

    private Context context;


    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(long id);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(long id) {
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public JobListFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSharedPreferences = getActivity().getSharedPreferences("prefs",getActivity().MODE_PRIVATE);
        /////mSharedPreferences.edit().clear().commit();
        mobileNumber = mSharedPreferences.getString("phoneKey", "");

        //startJobListService();
        // TODO: replace with a real list adapter.

    }



    @Override
    public void onResume() {
        super.onResume();

        /*
        setListAdapter(new ArrayAdapter<Job>(
                getActivity(),
                android.R.layout.simple_list_item_activated_1,
                //R.layout.custom_job_list,
                android.R.id.text1,
                JobListContent.ITEMS));*/

        new JobListFragmentDisplayedNotifier((JobListFragmentDisplayedListener) context);
        getJobsURL= new CSProperties(context).getDomain()+"/customers/jobs/mobileNumber/";
        //new JobListContent().removeClosedJobs();
        CustomerJobAdapter customerJobAdapter = new CustomerJobAdapter(getActivity(), (ArrayList<Job>) JobListContent.ITEMS);
        setListAdapter(customerJobAdapter);
        customerJobAdapter.notifyDataSetChanged();
        Log.i(TAG, "getJobsURL+mobileNumber " + getJobsURL + mobileNumber);
        getJobs(getJobsURL + mobileNumber);
        /*
        JobAdapter jobAdapter = new JobAdapter(getActivity(), (ArrayList<Job>) JobListContent.ITEMS);
        ListView listView = (ListView) getView().findViewById(R.id.jobs);
        listView.setAdapter(jobAdapter);*/
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

        if (activity == null) Log.e(TAG, "NULLNULLNULLNULLNULLNULLNULLNULLNULLNULLNULL");
        context = activity;
        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
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
        mCallbacks.onItemSelected(JobListContent.ITEMS.get(position).getId());
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
                jobs = null;
                e.printStackTrace();
            }
            return jobs;
        }

        @Override
        protected void onPostExecute(List<Job> jobs) {
            Toast.makeText(context, "JobListFragment Data Sent!", Toast.LENGTH_LONG).show();
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

                //new JobListContent().removeClosedJobs();
                CustomerJobAdapter customerJobAdapter = new CustomerJobAdapter(context, (ArrayList<Job>) JobListContent.ITEMS);
                setListAdapter(customerJobAdapter);
                customerJobAdapter.notifyDataSetChanged();

                for (Job job: JobListContent.ITEMS) {
                    String jobStatus = job.getJobStatus().toString();
                    Log.i("", "Job status to be displayed is "+jobStatus);
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

    public interface NoJobsListenerPostExecuteJobListFragment {
        void showSelectJobTypeFragment();
        //void showPostJobFragment();
    }

    public interface JobListFragmentDisplayedListener {
        void onJobListFragmentDisplayed();
    }

    @Override
    public void onStop() {
        super.onStop();
        //stopJobListService();
    }

    public void startJobListService() {
        Intent i = new Intent(context, JobListService.class);
        getActivity().startService(new Intent(getActivity(), JobListService.class));
    }

    public void stopJobListService() {
        getActivity().stopService(new Intent(getActivity(), JobListService.class));
    }
}
