package com.example.vengatr.consumer_services_android_20;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.vengatr.consumer_services_android_20.dummy.JobListContent;
import com.example.vengatr.consumer_services_android_20.listener.DaySegmentOnItemSelectedListener;
import com.example.vengatr.consumer_services_android_20.listener.JobTypeOnItemSelectedListener;
import com.example.vengatr.consumer_services_android_20.model.DaySegment;
import com.example.vengatr.consumer_services_android_20.model.Job;
import com.example.vengatr.consumer_services_android_20.model.JobStatus;
import com.example.vengatr.consumer_services_android_20.model.JobType;
import com.example.vengatr.consumer_services_android_20.notifier.OnPostJobCompletionNotifier;
import com.example.vengatr.consumer_services_android_20.rest_classes.GetJob;
import com.example.vengatr.consumer_services_android_20.util.CSProperties;
import com.example.vengatr.consumer_services_android_20.util.DateManipulation;
import com.example.vengatr.consumer_services_android_20.util.DaySegmentMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static com.example.vengatr.consumer_services_android_20.rest_classes.PostJob.POST;

/**
 * Created by vengat.r on 7/8/2015.
 */
public class PostJobFragment extends Fragment implements View.OnClickListener {



    private RelativeLayout calendarArea;
    private EditText jobDescriptionEditText, postJobDateDisplayEditText, postJobTimeDisplayEditText;
    private Spinner jobTypeSelector, daySegmentSelector;
    private Button postJobButton;
    private DatePicker datePicker;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    private String jobTypeSpinnerSelectionValue = null;
    private String daySegmentSpinnerSelectionValue = null;

    private String mobileNumber, userName, pincode, userType;

    private ProgressDialog progressDialog;

    private TextView dateValidityTextView;

    //ec2-52-74-141-170.ap-southeast-1.compute.amazonaws.com
    //private static final String QUERY_URL_POST_PUT_JOB = "http://ec2-52-74-141-170.ap-southeast-1.compute.amazonaws.com:8080/jobs";
    private String postJobUrl;// = "http://10.0.2.2:8080/jobs";

    private SharedPreferences mSharedPreferences;

    private int year;
    private int month;
    private int day;
    private int selectedYear;
    private int selectedMonth;
    private int selectedDay;
    private int hour;
    private int minute;
    private DaySegment selectedDaySegment;
    private Date preferredDate;
    private Date jobPreferredDate;
    private Date dateInitiated;
    private View fragmentView;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        System.out.println("***In post job fragment***");
        View view = inflater.inflate(R.layout.post_job_fragment, container, false);
        fragmentView = view;
        //setCurrentDateOnEdit(view);
        //setCurrentTimeOnView(view);
        dateValidityTextView = (TextView) view.findViewById(R.id.valid_job_text_view);
        jobDescriptionEditText = (EditText) view.findViewById(R.id.editTextDescription);

        //jobTypeSelector = (Spinner) view.findViewById(R.id.job_types_spinner);
        //createSpinnerJobType(view);
        //createSpinnerDaySegment(view);
        postJobButton = (Button) view.findViewById(R.id.postJobButton);
        postJobButton.setOnClickListener(this);



        /*
        Intent intent = getActivity().getIntent();
        mobileNumber = intent.getStringExtra(MainActivity.USER_MOBILE_NUMBER);
        userName = intent.getStringExtra(MainActivity.USER_NAME);
        pincode = intent.getStringExtra(MainActivity.USER_PINCODE);
        userType = intent.getStringExtra(MainActivity.USER_TYPE);*/
        mSharedPreferences = getActivity().getSharedPreferences("prefs", getActivity().MODE_PRIVATE);
        mobileNumber = mSharedPreferences.getString("phoneKey", "");
        userName = mSharedPreferences.getString("nameKey", "");
        pincode = mSharedPreferences.getString("pincodeKey", "");
        userType = mSharedPreferences.getString("userTypeKey", "");

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Posting your job");
        progressDialog.setCancelable(false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        createSpinnerJobType(fragmentView);
        setCurrentDateOnEdit(fragmentView);
        createSpinnerDaySegment(fragmentView);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity == null) Log.e("IS NULL", "NULLNULLNULLNULLNULLNULLNULLNULLNULLNULLNULL");
        context = activity;
    }

    public void createSpinnerJobType(View v) {
        jobTypeSelector = (Spinner) v.findViewById(R.id.job_types_spinner);
        /*ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.job_types, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.job_types, R.layout.selected_item);

        adapter.setDropDownViewResource(R.layout.dropdown_item);

        jobTypeSelector.setAdapter(adapter);
        //jobTypeSelector.setOnItemSelectedListener(new JobTypeOnItemSelectedListener());
        jobTypeSelector.setOnItemSelectedListener(new JobTypeOnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                Toast.makeText(getActivity(), jobTypeSelector.getSelectedItem().toString(),
                        Toast.LENGTH_LONG).show();
                jobTypeSpinnerSelectionValue = jobTypeSelector.getSelectedItem().toString().toUpperCase();

            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }


        });
        //jobTypeSpinnerSelectionValue = jobTypeSelector.getSelectedItem().toString();
        System.out.println("**************" + jobTypeSpinnerSelectionValue);
    }

    public ArrayAdapter<CharSequence> createSpinnerValuesOfDay() {
        ArrayAdapter<CharSequence> adapter = null;
        String date = String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(day);
        Log.i("", "Date selected is " + date);
        Date jobPreferredDate = null;
        try {
            jobPreferredDate = DateManipulation.convertStringToDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (preferredDate == null) preferredDate = jobPreferredDate;

        String applicableDaySegments = DateManipulation.getApplicableDaySegment(preferredDate);

        switch(applicableDaySegments) {
            case "future_date":
                /*adapter = ArrayAdapter.createFromResource(getActivity(),
                        R.array.day_segments, android.R.layout.simple_spinner_item);*/
                adapter = ArrayAdapter.createFromResource(getActivity(),
                        R.array.day_segments, R.layout.selected_item);
                break;
            case "ar_early_morning":
                adapter = ArrayAdapter.createFromResource(getActivity(),
                        R.array.day_segments, R.layout.selected_item);
                break;
            case "at_morning":
                adapter = ArrayAdapter.createFromResource(getActivity(),
                        R.array.day_segments_morning_done, R.layout.selected_item);
                break;
            case "at_forenoon":
                adapter = ArrayAdapter.createFromResource(getActivity(),
                        R.array.day_segments_forenoon_done, R.layout.selected_item);
                break;
            case "at_afternoon":
                adapter = ArrayAdapter.createFromResource(getActivity(),
                        R.array.day_segments_afternoon_done, R.layout.selected_item);
                break;
            case "at_evening":
                adapter = ArrayAdapter.createFromResource(getActivity(),
                        R.array.day_segments_evening_done, R.layout.selected_item);
                break;
            default:
                adapter = ArrayAdapter.createFromResource(getActivity(),
                        R.array.day_segments, R.layout.selected_item);
                break;
        }
        adapter.notifyDataSetChanged();
        return adapter;
    }

    public void createSpinnerDaySegment(View v) {
        calendarArea = (RelativeLayout) v.findViewById(R.id.calendar_area);
        daySegmentSelector = (Spinner) v.findViewById(R.id.day_segment_values);

       /* ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.day_segments, android.R.layout.simple_spinner_item);*/
        ArrayAdapter<CharSequence> adapter = createSpinnerValuesOfDay();
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.dropdown_item);
        daySegmentSelector.setAdapter(adapter);
        daySegmentSpinnerSelectionValue = daySegmentSelector.getSelectedItem().toString();
        daySegmentSelector.setOnItemSelectedListener(new DaySegmentOnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                Toast.makeText(getActivity(), daySegmentSelector.getSelectedItem().toString(),
                        Toast.LENGTH_LONG).show();
                daySegmentSpinnerSelectionValue = daySegmentSelector.getSelectedItem().toString();

            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }


        });

        System.out.println("**************" + daySegmentSpinnerSelectionValue);
    }

    public void setCurrentDateOnEdit(View v) {
        postJobDateDisplayEditText = (EditText) v.findViewById(R.id.set_job_date);

        final Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

        // set current date into textview
        postJobDateDisplayEditText.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(month + 1).append("-").append(day).append("-")
                .append(year).append(" "));

        try {
            preferredDate = DateManipulation.convertStringToDate(String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(day));
        } catch (ParseException e) {
            preferredDate = null;
            e.printStackTrace();
        }

        dateInitiated = preferredDate;
        Log.i("Date selected", ""+preferredDate.toString());
        postJobDateDisplayEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int inType = postJobDateDisplayEditText.getInputType(); // backup the input type
                postJobDateDisplayEditText.setInputType(InputType.TYPE_NULL); // disable soft input
                postJobDateDisplayEditText.onTouchEvent(event); // call native handler
                postJobDateDisplayEditText.setInputType(inType); // restore input type
                return true; // consume touch even
            }
        });

        postJobDateDisplayEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //bring up the date picker dialog from here
                datePickerDialog = new DatePickerDialog(getActivity(), datePickerListener, year, month, day);
                datePickerDialog.setTitle("Schedule Job, Please!");
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            Log.i("Selected day ", ""+selectedDay);
            Log.i("Selected month ", ""+selectedMonth);
            // set selected date into textview
            postJobDateDisplayEditText.setText(new StringBuilder().append(month + 1)
                    .append("-").append(day).append("-").append(year)
                    .append(" "));


            // set selected date into datepicker also
            //datePicker.init(year, month, day, null);
            try {
                preferredDate = DateManipulation.convertStringToDate(String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(day));
            } catch (ParseException e) {
                preferredDate = null;
                e.printStackTrace();
            }
            Log.d("Pref date", preferredDate.toString());
            createSpinnerDaySegment(fragmentView);

        }
    };

    /*********************************************************************************************************************/
    /**                                                                                                                  */
    /**Place holder for the time picker code...................Check at the end of the class.............................*/
    /**                                                                                                                  */
    /*********************************************************************************************************************/
    @Override
    public void onClick(View v) {
        String date = String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(day);
        Date jobPreferredDate = null;
        try {
                jobPreferredDate = DateManipulation.convertStringToDate(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        if (preferredDate == null) preferredDate = jobPreferredDate;
        if (daySegmentSpinnerSelectionValue.equalsIgnoreCase("Select next day")) {
            dateValidityTextView.setTextColor(Color.RED);
            dateValidityTextView.setText("You have chosen an invalid time of day. We operate between 9-5. Please select a later date");
            return;
        }
        selectedDaySegment = DaySegmentMapper.getDaySegment(daySegmentSpinnerSelectionValue);

        Log.d("", selectedDaySegment.toString());
        if (selectedDaySegment == null) {
            dateValidityTextView.setTextColor(Color.RED);
            dateValidityTextView.setText("You have chosen an invalid time of day. We operate between 9-5");
            return;
        }
        if (!DateManipulation.isDateEligibleForPosting(preferredDate, DaySegmentMapper.getDaySegment(daySegmentSpinnerSelectionValue))) {
            dateValidityTextView.setTextColor(Color.RED);
            dateValidityTextView.setText("You have chosen either an invalid date or time of day. We operate between 9-5");
            return;
        }
        //preferredDate = jobPreferredDate;

        progressDialog.show();
        postJobUrl = new CSProperties(context).getDomain()+"/jobs";
        new PostJobHttpAsyncTask().execute(postJobUrl);
    }

    private class PostJobHttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {


            GetJob getJob = new GetJob(getActivity());
            Job job = null;
            try {
                job =  getJob.getNewJob();
            } catch (IOException e) {
                e.printStackTrace();
            }

            job.setJobType(JobType.valueOf(jobTypeSpinnerSelectionValue));
            //job.setJobType(JobType.PLUMBING);
            job.setJobStatus(JobStatus.OPEN);
            job.setCustomerName(userName);
            job.setCustomerMobileNumber(Long.parseLong(mobileNumber));
            job.setPincode(pincode);
            job.setDescription(jobDescriptionEditText.getText().toString());
            Log.d("", selectedDaySegment.toString());
            job.setDaySegment(selectedDaySegment);
            job.setDateInitiated(dateInitiated);
            Log.i("Date Initiated", "*"+dateInitiated);
            job.setDatePreferred(preferredDate);
            Log.d("Pref date", preferredDate.toString());

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
            jobDescriptionEditText.setText("");
            progressDialog.dismiss();
        }
    }

    public interface OnPostJobCompletionListener {
        void onPostJobCompletion();
    }



}







    /*

        private static String pad(int c) {
            if (c >= 10)
                return String.valueOf(c);
            else
                return "0" + String.valueOf(c);
        }


        public void setCurrentTimeOnView(View v) {

            postJobTimeDisplayEditText = (EditText) v.findViewById(R.id.set_job_time);

            final Calendar c = Calendar.getInstance();
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);

            // set current time into textview
            postJobTimeDisplayEditText.setText(
                    new StringBuilder().append(pad(hour))
                            .append(":").append(pad(minute)));

            postJobTimeDisplayEditText.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int inType = postJobTimeDisplayEditText.getInputType(); // backup the input type
                    postJobTimeDisplayEditText.setInputType(InputType.TYPE_NULL); // disable soft input
                    postJobTimeDisplayEditText.onTouchEvent(event); // call native handler
                    postJobTimeDisplayEditText.setInputType(inType); // restore input type
                    return true; // consume touch even
                }
            });

            postJobTimeDisplayEditText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //bring up the date picker dialog from here
                    timePickerDialog = new TimePickerDialog(getActivity(), timePickerListener, hour, minute, false);
                    timePickerDialog.setTitle("Schedule Job, Please!");
                    timePickerDialog.show();
                }
            });

        }

        private TimePickerDialog.OnTimeSetListener timePickerListener =
                new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker view, int selectedHour,
                                          int selectedMinute) {
                        hour = selectedHour;
                        minute = selectedMinute;

                        // set current time into textview
                        postJobTimeDisplayEditText.setText(new StringBuilder().append(pad(hour))
                                .append(":").append(pad(minute)));

                        // set current time into timepicker

                    }
                };
                */