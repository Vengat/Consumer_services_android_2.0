package com.example.vengatr.consumer_services_android_20.rest_classes;

import android.content.Context;
import android.util.Log;

import com.example.vengatr.consumer_services_android_20.model.Invoice;
import com.example.vengatr.consumer_services_android_20.util.CSProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by vengat.r on 8/29/2015.
 */
public class GetInvoice {

    private Invoice invoice;

    private String domain;

    private String newInvoiceUrl;



    public GetInvoice(Context context) {
        CSProperties csProperties = new CSProperties(context);
        domain = csProperties.getDomain();
        newInvoiceUrl = domain+"/invoices";
    }

    public Invoice getNewInvoice() throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        ObjectMapper objectMapper = new ObjectMapper();
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(newInvoiceUrl);
        //HttpResponse response = client.execute(request);

        HttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (ClientProtocolException cpe) {
            Log.d("", "Error occured while getting new Invoice");
        }

        BufferedReader rd = new BufferedReader
                (new InputStreamReader(response.getEntity().getContent()));

        String line = "";
        while ((line = rd.readLine()) != null) {
            stringBuffer.append(line);
        }
        String jsonResponse = stringBuffer.toString();
        Invoice invoice = objectMapper.readValue(jsonResponse, Invoice.class);
        return invoice;
    }
}
