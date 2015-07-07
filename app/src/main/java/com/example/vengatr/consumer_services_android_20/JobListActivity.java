package com.example.vengatr.consumer_services_android_20;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.vengatr.consumer_services_android_20.listener.PostActionJobListPageTransitionListener;


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
public class JobListActivity extends FragmentActivity //FragmentActivity ActionBarActivity
        implements JobListFragment.Callbacks,  ServiceProviderJobsPerspectiveFragment.Callbacks, PostActionJobListPageTransitionListener, View.OnClickListener, PostJobFragment.OnPostJobCompletionListener {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private String mobileNumber, pincode, userName, userType;

    private PostJobFragment postJobFragment;

    private Button postJobButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list);



        Intent intent = getIntent();
        mobileNumber = intent.getStringExtra(MainActivity.USER_MOBILE_NUMBER);
        userName = intent.getStringExtra(MainActivity.USER_NAME);
        pincode = intent.getStringExtra(MainActivity.USER_PINCODE);
        userType = intent.getStringExtra(MainActivity.USER_TYPE);

        if (userType.equalsIgnoreCase("customer")) {
            JobListFragment jobListFragment = new JobListFragment();
            jobListFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.job_list_container, jobListFragment).commit();
            System.out.println("OK job list fragment has been dynamically created");

        } else {
            ServiceProviderJobsPerspectiveFragment serviceProviderJobsPerspectiveFragment = new ServiceProviderJobsPerspectiveFragment();
            serviceProviderJobsPerspectiveFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.job_list_container, serviceProviderJobsPerspectiveFragment).commit();
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

        postJobButton = (Button) findViewById(R.id.post_job_button);
        postJobButton.setOnClickListener(this);

        // TODO: If exposing deep links into your app, handle intents here.
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
            if (userType.equalsIgnoreCase("customer")) {
                replaceJobDetailFragment(id);
            } else {
                replaceSPJobDetailFragment(id);
            }
        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            if (userType.equalsIgnoreCase("customer")) {
                intentWithJobDetailFragment(id);
            } else {
                intentWithSPJobDetailFragment(id);
            }
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
        Intent detailIntent = new Intent(this, JobDetailActivity.class);
        detailIntent.putExtra(SPJobDetailFragment.ARG_ITEM_ID, id);
        detailIntent.putExtra("Name", userName);
        detailIntent.putExtra("User_Type", userType);
        startActivity(detailIntent);
    }


    @Override
    public void jobListPageTransition() {

    }

    @Override
    public void onClick(View v) {
        postJobFragment = new PostJobFragment();
        postJobFragment.setArguments(getIntent().getExtras());

        getSupportFragmentManager().beginTransaction()
                .add(R.id.job_list_container, postJobFragment).commit();
    }

    @Override
    public void onPostJobCompletion() {
        getSupportFragmentManager().beginTransaction().remove(postJobFragment).commit();
    }
}
