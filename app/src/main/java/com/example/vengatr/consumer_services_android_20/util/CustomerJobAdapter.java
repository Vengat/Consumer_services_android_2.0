package com.example.vengatr.consumer_services_android_20.util;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vengatr.consumer_services_android_20.R;
import com.example.vengatr.consumer_services_android_20.model.Job;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vengat.r on 7/3/2015.
 */
public class CustomerJobAdapter extends ArrayAdapter<Job> {

    //List<Job> jobs;
    public CustomerJobAdapter(Context context, ArrayList<Job> jobs) {
        super(context, 0, jobs);
        //this.jobs = jobs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        Job job = getItem(position);

        ViewHolder viewHolder;
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.job_item, parent, false);
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.job_item, parent, false);
            viewHolder.jobIdTextView = (TextView) convertView.findViewById(R.id.jobId);
            viewHolder.serviceProviderNameTextView = (TextView) convertView.findViewById(R.id.user_name);
            viewHolder.serviceProviderPhoneTextView = (TextView) convertView.findViewById(R.id.user_phone);
            viewHolder.jobTypeImageView = (ImageView) convertView.findViewById(R.id.img_jobType);
            viewHolder.jobStatusImageView = (ImageView) convertView.findViewById(R.id.img_jobStatus);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Lookup view for data population


        //TextView jobId = (TextView) convertView.findViewById(R.id.jobId);
        viewHolder.jobIdTextView.setPadding(4, 4, 4, 4);
        viewHolder.jobIdTextView.setTypeface(Typeface.SANS_SERIF);
        //TextView customerName = (TextView) convertView.findViewById(R.id.customer_name);
        viewHolder.serviceProviderNameTextView.setTypeface(Typeface.SANS_SERIF);
        viewHolder.serviceProviderNameTextView.setPadding(4, 4, 4, 4);
        //TextView spName = (TextView) convertView.findViewById(R.id.service_provider_name);
        viewHolder.serviceProviderPhoneTextView.setTypeface(Typeface.SANS_SERIF);
        viewHolder.serviceProviderPhoneTextView.setPadding(4, 4, 4, 4);

        //ImageView jobTypeImageView = (ImageView) convertView.findViewById(R.id.img_jobType);
        //ImageView jobStatusImageView = (ImageView) convertView.findViewById(R.id.img_jobStatus);

        // Populate the data into the template view using the data object
        viewHolder.jobIdTextView.setText(String.valueOf("Job ID : "+job.getId()));
        String phone = String.valueOf(job.getServiceProviderMobileNumber());
        if (phone.equals("0")) phone = "Not assigned yet";
        viewHolder.serviceProviderPhoneTextView.setText(phone);
        String spName = job.getServiceProviderName();
        if (spName.length() == 0) spName = "Service Provider not assigned yet";
        viewHolder.serviceProviderNameTextView.setText(spName);
        if(job.getJobType().toString().equalsIgnoreCase("plumbing")) {
            viewHolder.jobTypeImageView.setImageResource(R.drawable.plumbing);
        } else if(job.getJobType().toString().equalsIgnoreCase("electrical")) {
            viewHolder.jobTypeImageView.setImageResource(R.drawable.electrician);
        }

        if(job.getJobStatus().toString().equalsIgnoreCase("open")) {
            viewHolder.jobStatusImageView.setImageResource(R.drawable.yellow_traffic_light);
        } else if(job.getJobStatus().toString().equalsIgnoreCase("assigned")) {
            viewHolder.jobStatusImageView.setImageResource(R.drawable.green_traffic_light);
        } else if(job.getJobStatus().toString().equalsIgnoreCase("cancelled")) {
            viewHolder.jobStatusImageView.setImageResource(R.drawable.red_traffic_light);
        }

        // Return the completed view to render on screen
        return convertView;
    }

    /*
    public void updateData(ArrayList<Job> j) {
        // update the adapter's dataset
        jobs = j;
        notifyDataSetChanged();
    }
    */

    private static class ViewHolder {
        TextView jobIdTextView;
        TextView serviceProviderNameTextView;
        TextView serviceProviderPhoneTextView;
        ImageView jobTypeImageView;
        ImageView jobStatusImageView;
    }
}
