package com.example.vengatr.consumer_services_android_20;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.vengatr.consumer_services_android_20.dummy.JobListContent;
import com.example.vengatr.consumer_services_android_20.listener.AdapterUpdateListener;
import com.example.vengatr.consumer_services_android_20.listener.PostActionJobListPageTransitionListener;
import com.example.vengatr.consumer_services_android_20.model.Job;
import com.example.vengatr.consumer_services_android_20.util.CustomerJobAdapter;

import java.util.ArrayList;


/**
 * An activity representing a list of Jobs. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link JobDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link JobListFragment} and the item details
 * (if present) is a {@link JobDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link JobListFragment.Callbacks} interface
 * to listen for item selections.
 *
 */
public class JobListActivity extends ActionBarActivity //FragmentActivity ActionBarActivity
        implements JobListFragment.Callbacks,  ServiceProviderJobsPerspectiveFragment.SPCallbacks, PostActionJobListPageTransitionListener, View.OnClickListener, PostJobFragment.OnPostJobCompletionListener,
        JobListFragment.NoJobsListenerPostExecuteJobListFragment, AdapterUpdateListener {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */

    private boolean mTwoPane;

    private String mobileNumber, pincode, userName, userType;

    private PostJobFragment postJobFragment;

    private Button postNewJobButton;

    SharedPreferences mSharedPreferences;

    CustomerJobAdapter customerJobAdapter;

    JobListFragment jobListFragment;
    ServiceProviderJobsPerspectiveFragment serviceProviderJobsPerspectiveFragment;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list);


        System.out.println("In oncreate job list activity");
        /*Intent intent = getIntent();
        mobileNumber = intent.getStringExtra(MainActivity.USER_MOBILE_NUMBER);
        userName = intent.getStringExtra(MainActivity.USER_NAME);
        pincode = intent.getStringExtra(MainActivity.USER_PINCODE);
        userType = intent.getStringExtra(MainActivity.USER_TYPE);*/

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        createJobListView();

        // TODO: If exposing deep links into your app, handle intents here.
    }

    private void createJobListView() {
        mSharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        mobileNumber = mSharedPreferences.getString("phoneKey", "");
        userName = mSharedPreferences.getString("nameKey", "");
        pincode = mSharedPreferences.getString("pincodeKey", "");
        userType = mSharedPreferences.getString("userTypeKey", "");

        /*
        EmptyFragment emptyJobListFragment = new EmptyFragment();
        emptyJobListFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction()
                .add(R.id.job_list_container, emptyJobListFragment).commit();*/

        if (postJobFragment != null && postJobFragment.isVisible()) getSupportFragmentManager().beginTransaction().hide(postJobFragment).commit();



        if (userType == null || userType.equalsIgnoreCase("customer")) {
            jobListFragment = new JobListFragment();
            jobListFragment.setArguments(getIntent().getExtras());

            // getSupportFragmentManager().beginTransaction()
            //         .hide(emptyJobListFragment).commit();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.job_list_container, jobListFragment).commit();
            System.out.println("OK job list fragment has been dynamically created");

            postNewJobButton = (Button) findViewById(R.id.post_job_button);
            postNewJobButton.setClickable(true);
            postNewJobButton.setVisibility(View.VISIBLE);
            postNewJobButton.setEnabled(true);
            postNewJobButton.setOnClickListener(this);

        } else if (userType.equalsIgnoreCase("service provider")){
            serviceProviderJobsPerspectiveFragment = new ServiceProviderJobsPerspectiveFragment();
            serviceProviderJobsPerspectiveFragment.setArguments(getIntent().getExtras());

            //getSupportFragmentManager().beginTransaction()
            //       .hide(emptyJobListFragment).commit();

            if (postJobFragment != null && postJobFragment.isVisible()) getSupportFragmentManager().beginTransaction().remove(postJobFragment).commit();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.job_list_container, serviceProviderJobsPerspectiveFragment).commit();
            postNewJobButton = (Button) findViewById(R.id.post_job_button);
            postNewJobButton.setVisibility(View.GONE);
            postNewJobButton.setEnabled(false);
            //postNewJobButton.setOnClickListener(null);
        }


        if (findViewById(R.id.job_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            if (userType.equalsIgnoreCase("customer")) {
                ((JobListFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.job_detail_container))
                        .setActivateOnItemClick(true);

            } else {
                ((ServiceProviderJobsPerspectiveFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.job_detail_container))
                        .setActivateOnItemClick(true);
            }

        }

    }



    /**
     * Callback method from {@link JobListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(long id) {

        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            replaceJobDetailFragment(id);
        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            intentWithJobDetailFragment(id);
        }

    }

    @Override
    public void onSPItemSelected(long id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            replaceSPJobDetailFragment(id);

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            System.out.println("I am in onSpItemSelected");
            intentWithSPJobDetailFragment(id);
        }

    }



    public void replaceJobDetailFragment(long id) {
        Bundle arguments = new Bundle();
        arguments.putLong(JobDetailFragment.ARG_ITEM_ID, id);
        arguments.putString("Name", userName);
        arguments.putString("User_Type", userType);
        JobDetailFragment fragment = new JobDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.job_detail_container, fragment)
                .commit();
    }

    public void replaceSPJobDetailFragment(long id) {
        Bundle arguments = new Bundle();
        arguments.putLong(SPJobDetailFragment.ARG_ITEM_ID, id);
        arguments.putString("Name", userName);
        arguments.putString("User_Type", userType);
        SPJobDetailFragment fragment = new SPJobDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.job_detail_container, fragment)
                .commit();
    }

    public void intentWithJobDetailFragment(long id) {
        System.out.println("OK intentWithJobDetailFragment is being sent to JobDetailActivity and the job id is " + id);
        Intent detailIntent = new Intent(this, JobDetailActivity.class);
        detailIntent.putExtra(JobDetailFragment.ARG_ITEM_ID, id);
        detailIntent.putExtra("Name", userName);
        detailIntent.putExtra("User_Type", userType);
        startActivity(detailIntent);
    }

    public void intentWithSPJobDetailFragment(long id) {
        System.out.println("OK intentWithSPJobDetailFragment is being sent to JobDetailActivity and the job id is " + id);
        Intent detailIntent = new Intent(this, JobDetailActivity.class);
        detailIntent.putExtra(SPJobDetailFragment.ARG_ITEM_ID, id);
        detailIntent.putExtra("Name", userName);
        detailIntent.putExtra("User_Type", userType);
        startActivity(detailIntent);
    }


    @Override
    public void jobListPageTransition() {
        if (userType.equalsIgnoreCase("customer")){
            getSupportFragmentManager().beginTransaction().replace(R.id.job_list_container, jobListFragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.job_list_container, serviceProviderJobsPerspectiveFragment).commit();
        }
    }

    @Override
    public void onClick(View v) {
        if (userType.equalsIgnoreCase("customer")){
            progressDialog.show();
            Toast.makeText(this, "Job is being posted", Toast.LENGTH_SHORT).show();
            postNewJobButton.setVisibility(View.GONE);
            if (postJobFragment == null) {
                postJobFragment = new PostJobFragment();
                postJobFragment.setArguments(getIntent().getExtras());
            }

            //if (postJobFragment.getArguments() == null) postJobFragment.setArguments(getIntent().getExtras());

            System.out.println("Clicked post job");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.job_list_container, postJobFragment).commit();
            progressDialog.dismiss();
        }
    }


    @Override
    public void onPostJobCompletion() {
        if(progressDialog != null) progressDialog.dismiss();
        System.out.println("I am at onPostJobCompletion");
        getSupportFragmentManager().beginTransaction().replace(R.id.job_list_container, jobListFragment).commit();
        //finish();
        //startActivity(getIntent());
        postNewJobButton.setVisibility(View.VISIBLE);
        postNewJobButton.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("I am at onResume");
        createJobListView();

        if (userType.equalsIgnoreCase("customer")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.job_list_container, jobListFragment).commit();
            postNewJobButton.setClickable(true);
            postNewJobButton.setVisibility(View.VISIBLE);
            postNewJobButton.setEnabled(true);
            postNewJobButton.setOnClickListener(this);
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.job_list_container, serviceProviderJobsPerspectiveFragment).commit();
            postNewJobButton.setVisibility(View.GONE);
            postNewJobButton.setEnabled(false);
            //postJobButton.setOnClickListener(null);
        }

    }

    @Override
    public void showPostJobFragment() {
        System.out.println("I am at showPostJobFragment");
        postNewJobButton.setVisibility(View.GONE);
        if (postJobFragment == null){
            postJobFragment = new PostJobFragment();
            postJobFragment.setArguments(getIntent().getExtras());
        }
        //if (postJobFragment.getArguments() == null) postJobFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.job_list_container, postJobFragment).commit();
    }

    @Override
    public void onUpdateAdapter() {
        customerJobAdapter.notifyDataSetChanged();
    }

}
