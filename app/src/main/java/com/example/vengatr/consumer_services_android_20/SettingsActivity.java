package com.example.vengatr.consumer_services_android_20;

import android.app.Activity;
import android.os.Bundle;

import com.example.vengatr.consumer_services_android_20.fragments.SettingsFragment;


public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }


}
