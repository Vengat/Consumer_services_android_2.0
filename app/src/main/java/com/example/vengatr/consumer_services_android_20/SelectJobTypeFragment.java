package com.example.vengatr.consumer_services_android_20;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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

import com.example.vengatr.consumer_services_android_20.notifier.OnPostSelectJobTypeNotifier;

/**
 * Created by vengat.r on 8/11/2015.
 *
 * This fragment replaces the spinner that is being used to select the job type to post
 */
public class SelectJobTypeFragment extends Fragment implements View.OnClickListener {

    /*private ImageButton plumbingButton;
    private ImageButton electricalButton;*/

    private Button plumbingButton;
    private Button electricalButton;
    private ProgressDialog progressDialog;

    private View fragmentView;
    private Activity context;
    private static final String TAG = "SelectJobTypeFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        // Inflate the layout for this fragment
        Log.i(TAG,"About to create the view of fragment");
        View view = inflater.inflate(R.layout.fragment_jobtypes, container, false);
        fragmentView = view;

        plumbingButton = (Button) view.findViewById(R.id.plumbing_job_button);
        plumbingButton.setOnClickListener(this);

        electricalButton = (Button) view.findViewById(R.id.electrical_job_button);
        electricalButton.setOnClickListener(this);

        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);

        return view;
    }

    @Override
    public void onClick(View v) {
        progressDialog.show();
        if (v.getId() == R.id.plumbing_job_button) {
            new OnPostSelectJobTypeNotifier((JobListActivity) context, "plumbing");
        } else if (v.getId() == R.id.electrical_job_button) {
            new OnPostSelectJobTypeNotifier((JobListActivity) context, "electrical");
        }
        progressDialog.dismiss();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity == null) Log.e(TAG, "activity is null at onAttach");
        context = activity;
    }

    @Override
    public void onResume() {
        super.onResume();
        plumbingButton = (Button) fragmentView.findViewById(R.id.plumbing_job_button);
        plumbingButton.setOnClickListener(this);

        electricalButton = (Button) fragmentView.findViewById(R.id.electrical_job_button);
        electricalButton.setOnClickListener(this);
    }

    public interface OnPostSelectJobTypeListener {
        void onPostSelectJobType(String jobTypeSelected);
    }

}
