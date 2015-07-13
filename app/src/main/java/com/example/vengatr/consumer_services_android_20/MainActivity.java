package com.example.vengatr.consumer_services_android_20;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by vengat.r on 7/6/2015.
 */
public class MainActivity extends FragmentActivity implements UserDetailsFragment.EditUserDetailsDialogListener, UserDetailsFragment.OnSharedPreferencesSetListener, UserDetailsFragment.UserDetailsNotFilledListener {

    public final static String USER_MOBILE_NUMBER = "com.example.vengat.r.consumer_services_android_20.MOBILE_NUMBER";
    public final static String USER_PINCODE = "com.example.vengat.r.consumer_services_android_20.PINCODE";
    public final static String USER_NAME = "com.example.vengat.r.consumer_services_android_20.NAME";
    public final static String USER_TYPE = "com.example.vengat.r.consumer_services_android_20.USER_T";


    private static final String PREFS = "prefs";
    private static final String NAME = "nameKey";
    private static final String PHONE = "phoneKey";
    private static final String PINCODE = "pincodeKey";
    private static final String USER_T = "userTypeKey";
    private String mobileNumber, name, pincode, userType;
    private SharedPreferences mSharedPreferences;
    ProgressDialog progressDialog;
    ImageView backgroundImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //backgroundImage.setImageResource(R.drawable.loader);
        mSharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);
        //mSharedPreferences.edit().clear().commit();
        mobileNumber = mSharedPreferences.getString(PHONE, "");
        //mobileNumber="9986020496";
        name = mSharedPreferences.getString(NAME, "");
        pincode = mSharedPreferences.getString(PINCODE, "");
        userType = mSharedPreferences.getString(USER_T, "");
        System.out.println("*****************user type ->"+userType);
        System.out.println("*****************mobile Number ->"+mobileNumber);
        //progressDialog = new ProgressDialog(this);
        //progressDialog.setMessage("One moment please!");
        //progressDialog.setCancelable(false);
        //if (savedInstanceState == null) {
        if (mobileNumber.length() == 0 || name.length() == 0 || pincode.length() == 0 || userType.length() == 0) {
            showEditDialog();
        } else {
            jobListPageTransition();
        }
        //}
    }

    public void jobListPageTransition() {
        Intent intent = new Intent(this, JobListActivity.class);
        intent.putExtra("prefs", mSharedPreferences.getString(PREFS, ""));
        intent.putExtra(USER_MOBILE_NUMBER,  mSharedPreferences.getString(PHONE, ""));
        intent.putExtra(USER_NAME, mSharedPreferences.getString(NAME, ""));
        intent.putExtra(USER_PINCODE, mSharedPreferences.getString(PINCODE, ""));
        intent.putExtra(USER_TYPE, mSharedPreferences.getString(USER_T, ""));
        startActivity(intent);
    }

    private void showEditDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        UserDetailsFragment userDetailsDialog = new UserDetailsFragment();
        userDetailsDialog.show(fragmentManager, "fragment_edit_name");
    }


    @Override
    public void onFinishEditDialog(String inputText) {
        Toast.makeText(this, "Hi, " + name + ", your preferences are set. Thanks! ", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void showDialog() {
        showEditDialog();
    }

    /*
    @Override
    public void onResume() {
        super.onResume();
        jobListPageTransition();
    }*/
}
