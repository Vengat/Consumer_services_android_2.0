package com.example.vengatr.consumer_services_android_20.rest_classes;

import com.example.vengatr.consumer_services_android_20.model.Job;
import com.example.vengatr.consumer_services_android_20.model.ServiceProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by vengat.r on 6/24/2015.
 */
public class PutJob {

    private String jobId, customerMobileNumber;

    private final String CANCEL_JOB_URL = "http://10.0.2.2:8080/customers/cancelJob/jobId/"+jobId+"/mobileNumber/"+customerMobileNumber;

    public Job cancelJob(Job job) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        ObjectMapper objectMapper = new ObjectMapper();
        HttpClient client = new DefaultHttpClient();
        String cancel_job_url = "http://10.0.2.2:8080/customers/cancelJob/jobId/"+job.getId()+"/mobileNumber/"+job.getCustomerMobileNumber();
        //String cancel_job_url = "http://ec2-52-74-141-170.ap-southeast-1.compute.amazonaws.com:8080/customers/cancelJob/jobId/"+job.getId()+"/mobileNumber/"+job.getCustomerMobileNumber();
        HttpPut request = new HttpPut(cancel_job_url);
        HttpResponse response = client.execute(request);

        BufferedReader rd = new BufferedReader
                (new InputStreamReader(response.getEntity().getContent()));

        String line = "";
        while ((line = rd.readLine()) != null) {
            stringBuffer.append(line);
        }
        String jsonResponse = stringBuffer.toString();
        Job j = objectMapper.readValue(jsonResponse, Job.class);
        return j;
    }

    public Job assignJob(long jobId, ServiceProvider sp) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String spJson = ow.writeValueAsString(sp);
        System.out.println("Hello your json object "+spJson);
        HttpClient client = new DefaultHttpClient();
        String assign_job_url = "http://localhost:8080/serviceProviders/assignJob/jobId/"+jobId;
        //String assign_job_url = "http://ec2-52-74-141-170.ap-southeast-1.compute.amazonaws.com:8080/serviceProviders/assignJob/jobId/"+jobId;
        HttpPut request = new HttpPut(assign_job_url);
        request.setEntity(new StringEntity(spJson));
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");

        HttpResponse response = client.execute(request);

        BufferedReader rd = new BufferedReader
                (new InputStreamReader(response.getEntity().getContent()));

        String line = "";
        while ((line = rd.readLine()) != null) {
            stringBuffer.append(line);
        }
        String jsonResponse = stringBuffer.toString();
        System.out.println("Hello this is you json response "+jsonResponse);
        Job j = objectMapper.readValue(jsonResponse, Job.class);
        return j;
    }

    public Job closeJob(long jobId) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        ObjectMapper objectMapper = new ObjectMapper();

        HttpClient client = new DefaultHttpClient();
        String close_job_url = "http://localhost:8080/serviceProviders/closeJob/jobId/"+jobId;
        //String close_job_url = "http://ec2-52-74-141-170.ap-southeast-1.compute.amazonaws.com:8080/serviceProviders/closeJob/jobId/"+jobId;
        HttpPut request = new HttpPut(close_job_url);

        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");

        HttpResponse response = client.execute(request);

        BufferedReader rd = new BufferedReader
                (new InputStreamReader(response.getEntity().getContent()));

        String line = "";
        while ((line = rd.readLine()) != null) {
            stringBuffer.append(line);
        }
        String jsonResponse = stringBuffer.toString();
        System.out.println("Hello this is you json response "+jsonResponse);
        Job j = objectMapper.readValue(jsonResponse, Job.class);
        return j;
    }

}
