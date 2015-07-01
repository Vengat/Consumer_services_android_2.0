package com.example.vengatr.consumer_services_android_20;


import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vengatr.consumer_services_android_20.model.ServiceProvider;
import com.example.vengatr.consumer_services_android_20.rest_classes.GetServiceProvider;

import java.io.IOException;

/**
 * Created by vengat.r on 7/1/2015.
 */
public class UserDetailsFragment extends Fragment implements View.OnClickListener {

    public final static String USER_MOBILE_NUMBER = "com.example.vengat.r.consumer_services_android_20.MOBILE_NUMBER";
    public final static String USER_PINCODE = "com.example.vengat.r.consumer_services_android_20.PINCODE";
    public final static String USER_NAME = "com.example.vengat.r.consumer_services_android_20.NAME";
    public final static String USER_TYPE = "com.example.vengat.r.consumer_services_android_20.USER_T";
    private static final String PREFS = "prefs";
    private static final String NAME = "nameKey";
    private static final String PHONE = "phoneKey";
    private static final String PINCODE = "pincodeKey";
    private static final String USER_T = "userTypeKey";

    SharedPreferences sharedPreferences;
    EditText userNameEditText, userPhoneEditText, pincodeEditText;
    Button buttonOk;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_detail, container, false);
        userNameEditText = (EditText) view.findViewById(R.id.editText);
        userPhoneEditText = (EditText) view.findViewById(R.id.editText1);
        pincodeEditText = (EditText) view.findViewById(R.id.editText2);
        Button buttonOk = (Button) view.findViewById(R.id.button_ok);
        buttonOk.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        getDetails();
        new IsUserSPHttpAsyncTask().execute(PHONE);
    }

    public void getDetails() {
        String name = userNameEditText.getText().toString();
        String mobileNumber = userPhoneEditText.getText().toString();
        String pincode = pincodeEditText.getText().toString();

        // Put it into memory (don't forget to commit!)
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //e.putString(PREF_NAME, inputName);
        editor.putString(NAME, name);
        editor.putString(PHONE, mobileNumber);
        editor.putString(PINCODE, pincode);
        editor.putString(USER_T, "customer");
        editor.commit();
    }

    private class IsUserSPHttpAsyncTask extends AsyncTask<String, String, Boolean> {

        @Override
        protected Boolean doInBackground(String... mobile) {
            GetServiceProvider getServiceProvider = new GetServiceProvider();
            ServiceProvider serviceProvider = null;
            try {
                serviceProvider = getServiceProvider.getServiceProvider(Long.parseLong(mobile[0]));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return serviceProvider != null;
        }

        @Override
        protected void onPostExecute(Boolean val) {
            Toast.makeText(getActivity(), "Data Sent!", Toast.LENGTH_LONG).show();
            if (val) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(USER_T, "service provider");
                editor.commit();
            }
        }
    }

}
