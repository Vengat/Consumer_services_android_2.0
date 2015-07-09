package com.example.vengatr.consumer_services_android_20;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.vengatr.consumer_services_android_20.listener.JobTypeOnItemSelectedListener;
import com.example.vengatr.consumer_services_android_20.model.Job;
import com.example.vengatr.consumer_services_android_20.model.JobStatus;
import com.example.vengatr.consumer_services_android_20.model.JobType;
import com.example.vengatr.consumer_services_android_20.notifier.OnPostJobCompletionNotifier;
import com.example.vengatr.consumer_services_android_20.rest_classes.GetJob;

import org.json.JSONException;

import java.io.IOException;

import static com.example.vengatr.consumer_services_android_20.rest_classes.PostJob.POST;

/**
 * Created by vengat.r on 7/8/2015.
 */
public class PostJobFragment extends Fragment implements View.OnClickListener {

    private EditText jobDescriptionEditText;
    private Spinner jobTypeSelector;
    private Button postJobButton;

    private String jobTypeSpinnerSelectionValue = null;

    private String mobileNumber, userName, pincode, userType;

    private ProgressDialog progressDialog;

    //ec2-52-74-141-170.ap-southeast-1.compute.amazonaws.com
    private static final String QUERY_URL_POST_PUT_JOB = "http://ec2-52-74-141-170.ap-southeast-1.compute.amazonaws.com:8080/jobs";
    //private static final String QUERY_URL_POST_PUT_JOB = "http://10.0.2.2:8080/jobs";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        System.out.println("***In post job fragment***");
        View view = inflater.inflate(R.layout.post_job_fragment, container, false);
        jobDescriptionEditText = (EditText) view.findViewById(R.id.editTextDescription);
        //jobTypeSelector = (Spinner) view.findViewById(R.id.job_types_spinner);
        createSpinner(view);
        postJobButton = (Button) view.findViewById(R.id.postJobButton);
        postJobButton.setOnClickListener(this);

        Intent intent = getActivity().getIntent();
        mobileNumber = intent.getStringExtra(MainActivity.USER_MOBILE_NUMBER);
        userName = intent.getStringExtra(MainActivity.USER_NAME);
        pincode = intent.getStringExtra(MainActivity.USER_PINCODE);
        userType = intent.getStringExtra(MainActivity.USER_TYPE);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Posting your job");
        progressDialog.setCancelable(false);
        return view;
    }

    public void createSpinner(View v) {
        jobTypeSelector = (Spinner) v.findViewById(R.id.job_types_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.job_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobTypeSelector.setAdapter(adapter);
        //jobTypeSelector.setOnItemSelectedListener(new JobTypeOnItemSelectedListener());
        jobTypeSelector.setOnItemSelectedListener(new JobTypeOnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3)
            {
                Toast.makeText(getActivity(), jobTypeSelector.getSelectedItem().toString(),
                        Toast.LENGTH_LONG).show();
                jobTypeSpinnerSelectionValue = jobTypeSelector.getSelectedItem().toString();

            }
            public void onNothingSelected(AdapterView<?> arg0)
            {
                // TODO Auto-generated method stub
            }


                                                  });
        //jobTypeSpinnerSelectionValue = jobTypeSelector.getSelectedItem().toString();
        System.out.println("**************"+jobTypeSpinnerSelectionValue);
    }

    @Override
    public void onClick(View v) {
        progressDialog.show();
        new PostJobHttpAsyncTask().execute(QUERY_URL_POST_PUT_JOB);
    }

    private class PostJobHttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {


            GetJob getJob = new GetJob();
            Job job = null;
            try {
                job =  getJob.getNewJob();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("*******Job customer name" + job.getCustomerName(), "");
            job.setJobType(JobType.valueOf(jobTypeSpinnerSelectionValue));
            //job.setJobType(JobType.PLUMBING);
            job.setJobStatus(JobStatus.OPEN);
            job.setCustomerName(userName);
            job.setCustomerMobileNumber(Long.parseLong(mobileNumber));
            job.setPincode(pincode);
            job.setDescription(jobDescriptionEditText.getText().toString());

            String result = "";

            try {
                result = POST(urls[0], job);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getActivity(), "Job request sent!", Toast.LENGTH_LONG).show();
            new OnPostJobCompletionNotifier((JobListActivity) getActivity());
            progressDialog.dismiss();
        }
    }

    public interface OnPostJobCompletionListener {
        void onPostJobCompletion();
    }



}
