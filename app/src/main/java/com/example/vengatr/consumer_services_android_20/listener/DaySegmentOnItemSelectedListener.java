package com.example.vengatr.consumer_services_android_20.listener;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

/**
 * Created by vengat.r on 7/19/2015.
 */
public class DaySegmentOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(parent.getContext(),
                "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
