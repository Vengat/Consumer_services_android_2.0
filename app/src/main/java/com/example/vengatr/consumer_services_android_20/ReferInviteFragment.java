package com.example.vengatr.consumer_services_android_20;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by vengat.r on 8/12/2015.
 */
public class ReferInviteFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "ReferInviteFragment";

    private Context context;
    private View fragmentView;
    private ImageButton smsImageButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();

        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_refer_invite, container, false);
        fragmentView = view;

        smsImageButton = (ImageButton) view.findViewById(R.id.sms_image_button);
        smsImageButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sms_image_button) {
            sendSMS();
        }
    }

    protected void sendSMS() {
        Log.i(TAG, "Send SMS");
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);

        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address"  , "");
        smsIntent.putExtra("sms_body"  , getSMSMessage());

        try {
            startActivity(smsIntent);
            Log.i(TAG, "SMS Sent...");
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context,
                    "SMS failed, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }

    public String getSMSMessage() {
        String  message = "Namaskara, Omelee services provides the best in class \n services for Plumbing and Electrical repairs. \n" +
                "Download the app today from Google play store @ link %s. Your friend gets discounts for the first service with the code OM903";

        return message;
    }

}
