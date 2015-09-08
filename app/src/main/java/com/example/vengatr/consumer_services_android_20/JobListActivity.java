package com.example.vengatr.consumer_services_android_20;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vengatr.consumer_services_android_20.listener.AdapterUpdateListener;
import com.example.vengatr.consumer_services_android_20.listener.PostActionJobListPageTransitionListener;
import com.example.vengatr.consumer_services_android_20.util.CustomerJobAdapter;
import com.example.vengatr.consumer_services_android_20.util.TabListener;
import com.example.vengatr.consumer_services_android_20.util.TabsPagerAdapter;


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
        implements JobListFragment.Callbacks, ServiceProviderJobsPerspectiveFragment.SPCallbacks, PostActionJobListPageTransitionListener, View.OnClickListener, PostJobFragment.OnPostJobCompletionListener,
        JobListFragment.NoJobsListenerPostExecuteJobListFragment, AdapterUpdateListener, SelectJobTypeFragment.OnPostSelectJobTypeListener, ReferInviteFragment.ReferInviteFragmentDisplayedListener,
        JobListFragment.JobListFragmentDisplayedListener {

    private static String TAG = "JobListActivity";
    public static String userTypeRendered;
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */

    private boolean mTwoPane;

    private String mobileNumber, pincode, userName, userType;

    private String jobTypeSelected;

    private Button postNewJobButton;

    SharedPreferences mSharedPreferences;

    CustomerJobAdapter customerJobAdapter;

    private PostJobFragment postJobFragment;
    private SelectJobTypeFragment selectJobTypeFragment;
    JobListFragment jobListFragment;
    ServiceProviderJobsPerspectiveFragment serviceProviderJobsPerspectiveFragment;
    private ReferInviteFragment referInviteFragment;
    ProgressDialog progressDialog;

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private android.support.v7.app.ActionBar actionBar;
    // Tab titles
    private android.support.v7.app.ActionBar.Tab jobsTab, referTab;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list);

        System.out.println("In oncreate job list activity");

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);


        createJobListView();
        showReferInviteFragment();
        createTabsPager();

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setCustomView(getLayoutInflater().inflate(R.layout.action_bar_layout, null));
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
        TextView omelee = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.app_name);

        // TODO: If exposing deep links into your app, handle intents here.
    }


    private void createJobListView() {
        mSharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        mobileNumber = mSharedPreferences.getString("phoneKey", "");
        userName = mSharedPreferences.getString("nameKey", "");
        pincode = mSharedPreferences.getString("pincodeKey", "");
        userType = mSharedPreferences.getString("userTypeKey", "");
        userTypeRendered = userType;

        if (postJobFragment != null && postJobFragment.isVisible()) {
            getSupportFragmentManager().beginTransaction().hide(postJobFragment).commit();
        }

        if (selectJobTypeFragment != null && selectJobTypeFragment.isVisible()){
            getSupportFragmentManager().beginTransaction().hide(selectJobTypeFragment).commit();
        }


        if (userType == null || userType.equalsIgnoreCase("customer")) {
            jobListFragment = new JobListFragment();
            jobListFragment.setArguments(getIntent().getExtras());


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

            if (postJobFragment != null && postJobFragment.isVisible()) {
                getSupportFragmentManager().beginTransaction().remove(postJobFragment).commit();
            }

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

    private void removeTabs() {
        if (actionBar == null) {
            actionBar = getSupportActionBar();
        }

        actionBar.setNavigationMode(android.support.v7.app.ActionBar.NAVIGATION_MODE_STANDARD);
        if (actionBar.getTabCount() > 0)actionBar.removeAllTabs();
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
    }

    private void createTabsPager() {
        // Initialization
        if (actionBar != null && actionBar.getTabCount() > 0) return;
        //viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getSupportActionBar();
        /*if (actionBar != null) {
            actionBar.setLogo(R.drawable.golden_star);
        }*/
        //mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        //viewPager.setAdapter(mAdapter);
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            //getSupportActionBar().setIcon(R.drawable.omelee_icon);
        }
        actionBar.setNavigationMode(android.support.v7.app.ActionBar.NAVIGATION_MODE_TABS);


        /*viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
        TextView omelee = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.app_name);
*/
        Log.i(TAG, "Tab count "+actionBar.getTabCount());
        if (actionBar.getTabCount() == 0) {
            RelativeLayout tabView1 = (RelativeLayout) getLayoutInflater().inflate(R.layout.tab_layout, null);
            RelativeLayout tabView2 = (RelativeLayout) getLayoutInflater().inflate(R.layout.tab_layout, null);
            jobsTab = actionBar.newTab().setText("Jobs");
            jobsTab.setCustomView(tabView1);
            TextView jobsTabTextView = (TextView) tabView1.findViewById(R.id.tabText);
            jobsTabTextView.setText("Jobs");
            referTab = actionBar.newTab().setText("Refer");
            referTab.setCustomView(tabView2);
            TextView referTabTextView = (TextView) tabView2.findViewById(R.id.tabText);
            referTabTextView.setText("Refer");
        } else if (actionBar.getTabCount() > 0) {
            return;
        }

        Log.i(TAG, "userType "+userType);
        if (userType.equalsIgnoreCase("customer")) {
            jobsTab.setTabListener(new TabListener(jobListFragment));
            referTab.setTabListener(new TabListener(referInviteFragment));
            actionBar.addTab(jobsTab);
            actionBar.addTab(referTab);
            actionBar.setStackedBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.yellow)));
        } else {
            //jobsTab.setTabListener(new TabListener(serviceProviderJobsPerspectiveFragment));
            if (actionBar.getTabCount() > 0) actionBar.removeAllTabs();
            actionBar.setStackedBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blue)));
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
            createTabsPager();
            getSupportFragmentManager().beginTransaction().replace(R.id.job_list_container, jobListFragment).commit();
        } else {
            removeTabs();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.job_list_container, serviceProviderJobsPerspectiveFragment).commit();
        }
    }

    @Override
    public void onClick(View v) {
        if (userType.equalsIgnoreCase("customer")){
            progressDialog.show();
            Toast.makeText(this, "Job is being posted", Toast.LENGTH_SHORT).show();
            removeTabs();
            showSelectJobTypeFragment();
            progressDialog.dismiss();
        }
    }


    @Override
    public void onPostJobCompletion() {
        if(progressDialog != null) progressDialog.dismiss();
        System.out.println("I am at onPostJobCompletion");
        getSupportFragmentManager().beginTransaction().replace(R.id.job_list_container, jobListFragment).commit();
        createTabsPager();
        postNewJobButton.setVisibility(View.VISIBLE);
        postNewJobButton.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("I am at onResume");
        createJobListView();
        showReferInviteFragment();
        createTabsPager();

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
        }

    }

    //@Override
    public void showPostJobFragment() {
        System.out.println("I am at showPostJobFragment");
        removeTabs();
        postNewJobButton.setVisibility(View.GONE);
        postNewJobButton.setEnabled(false);
        if (postJobFragment == null){
            postJobFragment = new PostJobFragment();
            //postJobFragment.setArguments(getIntent().getExtras());
        }
        Bundle arguments = new Bundle();
        //arguments.putAll(getIntent().getExtras());
        arguments.putString(PostJobFragment.jobType, getJobTypeSelected());
        postJobFragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.job_list_container, postJobFragment).commit();
    }

    @Override
    public void onUpdateAdapter() {
        customerJobAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPostSelectJobType(String jobTypeSelected) {
        setJobTypeSelected(jobTypeSelected);
        showPostJobFragment();
    }

    @Override
    public void showSelectJobTypeFragment() {
        postNewJobButton.setVisibility(View.GONE);
        removeTabs();
        if (selectJobTypeFragment == null) {
            selectJobTypeFragment = new SelectJobTypeFragment();
            selectJobTypeFragment.setArguments(getIntent().getExtras());
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.job_list_container, selectJobTypeFragment).commit();
    }

    public String getJobTypeSelected() {
        return jobTypeSelected;
    }

    public void setJobTypeSelected(String jobTypeSelected) {
        this.jobTypeSelected = jobTypeSelected;
    }

    private void showReferInviteFragment() {
        if (referInviteFragment == null) {
            referInviteFragment = new ReferInviteFragment();
        }

        if (!referInviteFragment.getUserVisibleHint()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.job_list_container, referInviteFragment).commit();
        }
        //postNewJobButton = (Button) findViewById(R.id.post_job_button);
        //postNewJobButton.setVisibility(View.GONE);
        //postNewJobButton.setEnabled(false);
    }

    private void hidePostNewJobButton() {
        if (postNewJobButton != null) {
            postNewJobButton.setVisibility(View.GONE);
            postNewJobButton.setEnabled(false);
            //postJobButton.setOnClickListener(null);
        }
    }

    private void showPostNewJobButton() {
        if (postNewJobButton == null) postNewJobButton = (Button) findViewById(R.id.post_job_button);
        postNewJobButton.setVisibility(View.VISIBLE);
        postNewJobButton.setEnabled(true);
        postNewJobButton.setOnClickListener(this);
    }

    @Override
    public void onReferInviteFragmentDisplayed() {
        hidePostNewJobButton();
    }

    @Override
    public void onJobListFragmentDisplayed() {
        showPostNewJobButton();
    }
}
