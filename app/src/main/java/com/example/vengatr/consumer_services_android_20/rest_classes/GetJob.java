package com.example.vengatr.consumer_services_android_20.rest_classes;

import android.util.Log;

import com.example.vengatr.consumer_services_android_20.model.Job;
import com.example.vengatr.consumer_services_android_20.util.CSConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by vengat.r on 6/17/2015.
 */
public class GetJob {

    //ec2-52-74-141-170.ap-southeast-1.compute.amazonaws.com:8080

    private static final String QUERY_NEW_JOB_URL = "http://10.0.2.2:8080/jobs";

    //private static final String QUERY_NEW_JOB_URL = "http://ec2-52-74-141-170.ap-southeast-1.compute.amazonaws.com:8080/jobs";

    private static final String QUERY_JOB_BY_ID = "http://10.0.2.2:8080/jobs/id/";//append the id

    //private static final String QUERY_JOB_BY_ID = "http://ec2-52-74-141-170.ap-southeast-1.compute.amazonaws.com:8080/jobs/id/";//append the id

    private static final String QUERY_URL_GET_JOBS_BY_MOBILE_NUMBER = "http://10.0.2.2:8080/customers/jobs/mobileNumber/";

   // private static final String QUERY_URL_GET_JOBS_BY_MOBILE_NUMBER = "http://ec2-52-74-141-170.ap-southeast-1.compute.amazonaws.com:8080/customers/jobs/mobileNumber/";

    private Job job;


    /*
{
    "id": 0,
    "jobType": "UNDEFINED",
    "jobStatus": "OPEN",
    "customerName": "Your name",
    "customerMobileNumber": 987654321,
    "serviceProviderName": "Service Provider Name",
    "serviceProviderMobileNumber": 123456789,
    "pincode": "Pincode",
    "dateInitiated": 1434455317217,
    "dateDone": null,
    "description": "job description"
} */

    public List<Job> getJobs(String url) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        ObjectMapper objectMapper = new ObjectMapper();
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);

        HttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (ClientProtocolException cpe) {
            Log.d("","Error occured while getting job list");
        }


        BufferedReader rd = new BufferedReader
                (new InputStreamReader(response.getEntity().getContent()));

        String line = "";
        while ((line = rd.readLine()) != null) {
            stringBuffer.append(line);
        }
        String jsonResponse = stringBuffer.toString();
        List<Job> jobs = objectMapper.readValue(jsonResponse, TypeFactory.defaultInstance().constructCollectionType(List.class,
                Job.class));
        return jobs;
    }


    public Job getNewJob() throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        ObjectMapper objectMapper = new ObjectMapper();
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(QUERY_NEW_JOB_URL);
        //HttpResponse response = client.execute(request);

        HttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (ClientProtocolException cpe) {
            Log.d("","Error occured while getting new job");
        }

        BufferedReader rd = new BufferedReader
                (new InputStreamReader(response.getEntity().getContent()));

        String line = "";
        while ((line = rd.readLine()) != null) {
            stringBuffer.append(line);
        }
        String jsonResponse = stringBuffer.toString();
        Job job = objectMapper.readValue(jsonResponse, Job.class);
        return job;
    }

    public Job getJobById(long id) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        ObjectMapper objectMapper = new ObjectMapper();
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(QUERY_JOB_BY_ID+id);
        //HttpResponse response = client.execute(request);

        HttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (ClientProtocolException cpe) {
            Log.d("","Error occured while getting job by id");
        }

        BufferedReader rd = new BufferedReader
                (new InputStreamReader(response.getEntity().getContent()));

        String line = "";
        while ((line = rd.readLine()) != null) {
            stringBuffer.append(line);
        }
        String jsonResponse = stringBuffer.toString();
        Job job = objectMapper.readValue(jsonResponse, Job.class);
        return job;
    }

    private void setJob(Job job) {
        this.job = job;
    }

    public Job getJob() {
        return this.job;
    }

}
