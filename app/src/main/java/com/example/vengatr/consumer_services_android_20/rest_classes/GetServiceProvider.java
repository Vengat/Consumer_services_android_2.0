package com.example.vengatr.consumer_services_android_20.rest_classes;

import com.example.vengatr.consumer_services_android_20.model.ServiceProvider;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by vengat.r on 6/25/2015.
 */
public class GetServiceProvider {

    private static final String QUERY_SP_BY_MOBILE = "http://10.0.2.2:8080/serviceProviders/mobileNumber/";

    public ServiceProvider getServiceProvider(long mobileNumber) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        ObjectMapper objectMapper = new ObjectMapper();
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(QUERY_SP_BY_MOBILE+mobileNumber);
        HttpResponse response = client.execute(request);

        BufferedReader rd = new BufferedReader
                (new InputStreamReader(response.getEntity().getContent()));

        String line = "";
        while ((line = rd.readLine()) != null) {
            stringBuffer.append(line);
        }
        String jsonResponse = stringBuffer.toString();
        System.out.println("********************************************************");
        System.out.println(jsonResponse);
        System.out.println("********************************************************");
        ServiceProvider sp;
        try {
            sp = objectMapper.readValue(jsonResponse, ServiceProvider.class);
        } catch(Exception e) {
            sp = null;
        }
        return sp;
    }

}
