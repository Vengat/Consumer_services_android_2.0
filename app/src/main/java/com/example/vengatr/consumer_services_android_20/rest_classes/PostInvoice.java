package com.example.vengatr.consumer_services_android_20.rest_classes;

import android.content.Context;
import android.util.Log;

import com.example.vengatr.consumer_services_android_20.model.Invoice;
import com.example.vengatr.consumer_services_android_20.util.CSProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by vengat.r on 8/29/2015.
 */
public class PostInvoice {

    private static final String TAG = "PostInvoice";

    private Invoice invoice;

    private String domain;

    private String postInvoiceUrl;

    private String displayInvoiceUrl;

    public PostInvoice(Context context) {
        CSProperties csProperties = new CSProperties(context);
        domain = csProperties.getDomain();
        postInvoiceUrl = domain+"/invoices";
        displayInvoiceUrl = domain+"/invoices/display";
    }

    public Invoice postInvoice(Invoice inv) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String invJson = ow.writeValueAsString(inv);
        System.out.println("Hello your json object "+invJson);
        HttpClient client = new DefaultHttpClient();
        //String postInvoiceUrl = this.domain+"/serviceProviders/assignJob/jobId/"+jobId;
        //String assign_job_url = "http://ec2-52-74-141-170.ap-southeast-1.compute.amazonaws.com:8080/serviceProviders/assignJob/jobId/"+jobId;
        HttpPost request = new HttpPost(postInvoiceUrl);
        request.setEntity(new StringEntity(invJson));
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
        Log.i(TAG, "Hello this is you json response " + jsonResponse);
        Invoice invoice = objectMapper.readValue(jsonResponse, Invoice.class);
        return invoice;
    }

    public Invoice displayInvoice(Invoice inv) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String invJson = ow.writeValueAsString(inv);
        System.out.println("Hello your json object "+invJson);
        HttpClient client = new DefaultHttpClient();
        HttpPost request = new HttpPost(displayInvoiceUrl);
        request.setEntity(new StringEntity(invJson));
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
        Log.i(TAG, "Hello this is you json response " + jsonResponse);
        Invoice invoice = objectMapper.readValue(jsonResponse, Invoice.class);
        return invoice;
    }

}
