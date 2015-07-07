package com.example.vengatr.consumer_services_android_20;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vengatr.consumer_services_android_20.model.ServiceProvider;
import com.example.vengatr.consumer_services_android_20.notifier.SharedPreferencesSetNotifier;
import com.example.vengatr.consumer_services_android_20.rest_classes.GetServiceProvider;

import java.io.IOException;

/**
 * Created by vengat.r on 7/1/2015.
 */
public class UserDetailsFragment extends DialogFragment implements View.OnClickListener, TextView.OnEditorActionListener {

    private static final String PREFS = "prefs";
    private static final String NAME = "nameKey";
    private static final String PHONE = "phoneKey";
    private static final String PINCODE = "pincodeKey";
    private static final String USER_T = "userTypeKey";

    private String name, mobileNumber, pincode;

    SharedPreferences sharedPreferences;
    EditText userNameEditText, userPhoneEditText, pincodeEditText;
    Button buttonOk;
    ProgressDialog progressDialog;
    Context context;

    public UserDetailsFragment() {}

    public interface EditUserDetailsDialogListener {
        void onFinishEditDialog(String inputText);
    }

    public interface OnSharedPreferencesSetListener {
        void jobListPageTransition();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_detail, container);
        userNameEditText = (EditText) view.findViewById(R.id.editText);
        userPhoneEditText = (EditText) view.findViewById(R.id.editText1);
        pincodeEditText = (EditText) view.findViewById(R.id.editText2);
        buttonOk = (Button) view.findViewById(R.id.button_ok);
        buttonOk.setOnClickListener(this);
        userNameEditText.requestFocus();
        setRetainInstance(true);
        getDialog().setTitle("Hello, Welcome!");
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("One moment please!");
        progressDialog.setCancelable(false);
        context = getActivity();
        return view;
    }

    @Override
    public void onClick(View v) {
        getDetails();
        progressDialog.show();
        new IsUserSPHttpAsyncTask().execute(mobileNumber);
        this.dismiss();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text to activity
            EditUserDetailsDialogListener activity = (EditUserDetailsDialogListener) getActivity();
            activity.onFinishEditDialog(userNameEditText.getText().toString());
            this.dismiss();
            return true;
        }
        return false;
    }


    public void getDetails() throws NullPointerException {
        sharedPreferences = getActivity().getSharedPreferences(PREFS, getActivity().MODE_PRIVATE);
        name = userNameEditText.getText().toString();
        mobileNumber = userPhoneEditText.getText().toString();
        pincode = pincodeEditText.getText().toString();
        if (name.length() == 0 || mobileNumber.length() == 0 || pincode.length() == 0) {
            throw new NullPointerException("Phone, Name, Pincode empty");
        }
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
            Toast.makeText(context, "UserDetailsFragment Data Sent!", Toast.LENGTH_LONG).show();
            if (val) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(USER_T, "service provider");
                editor.apply();
            }
            new SharedPreferencesSetNotifier((MainActivity) context);
            progressDialog.dismiss();
        }
    }


}
