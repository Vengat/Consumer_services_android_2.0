package com.example.vengatr.consumer_services_android_20;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.example.vengatr.consumer_services_android_20.listener.JobListPageTransitionListener;
import com.example.vengatr.consumer_services_android_20.listener.ServiceProviderBillClickListener;


/**
 * An activity representing a single Job detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link JobListActivity}.
 * <p/>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link JobDetailFragment}.
 */
public class JobDetailActivity extends ActionBarActivity implements JobDetailFragment.CancelJobListener, SPJobDetailFragment.OnAssignOrCloseJobListener,
        JobDetailFragment.AgreedJobListener, ServiceProviderBillClickListener, JobListPageTransitionListener {

    private String userType, userName;
    private long jobId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);

        Intent intent = getIntent();
        userName = intent.getStringExtra("Name");
        userType = intent.getStringExtra("User_Type");
        System.out.println("OK lets see what the user type is " + userType);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setCustomView(getLayoutInflater().inflate(R.layout.action_bar_layout, null));
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
        // Show the Up button in the action bar.

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //

        if (savedInstanceState == null) {
            if (userType.equalsIgnoreCase("customer")) {
                // Create the detail fragment and add it to the activity
                // using a fragment transaction.
                Bundle arguments = new Bundle();
                jobId = getIntent().getLongExtra(JobDetailFragment.ARG_ITEM_ID, 0);
                arguments.putLong(JobDetailFragment.ARG_ITEM_ID,
                        getIntent().getLongExtra(JobDetailFragment.ARG_ITEM_ID, 0));
                JobDetailFragment fragment = new JobDetailFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.job_detail_container, fragment)
                        .commit();
            } else {
                Bundle arguments = new Bundle();
                jobId = getIntent().getLongExtra(JobDetailFragment.ARG_ITEM_ID, 0);
                arguments.putLong(SPJobDetailFragment.ARG_ITEM_ID,
                        getIntent().getLongExtra(SPJobDetailFragment.ARG_ITEM_ID, 0));
                SPJobDetailFragment fragment = new SPJobDetailFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.job_detail_container, fragment)
                        .commit();
            }
        }

        /*
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putLong(JobDetailFragment.ARG_ITEM_ID,
                    getIntent().getLongExtra(JobDetailFragment.ARG_ITEM_ID, 0));
            JobDetailFragment fragment = new JobDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.job_detail_container, fragment)
                    .commit();
        }*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, JobListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void jobListPageTransition() {
        Intent intent = new Intent(this, JobListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void showInvoiceFragment() {
        Bundle arguments = new Bundle();
        InvoiceFragment invoiceFragment = new InvoiceFragment();
        arguments.putLong(InvoiceFragment.ARG_ITEM_ID,
                getIntent().getLongExtra(InvoiceFragment.ARG_ITEM_ID, 0));
        invoiceFragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.job_detail_container, invoiceFragment)
                .commit();
    }

    @Override
    public void navigateToJobList() {
        jobListPageTransition();
    }
}
