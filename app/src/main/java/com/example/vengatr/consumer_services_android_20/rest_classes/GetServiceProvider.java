package com.example.vengatr.consumer_services_android_20.rest_classes;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import com.example.vengatr.consumer_services_android_20.model.ServiceProvider;
import com.example.vengatr.consumer_services_android_20.util.CSProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by vengat.r on 6/25/2015.
 */
public class GetServiceProvider {

    //ec2-52-74-141-170.ap-southeast-1.compute.amazonaws.com:8080
    //private static final String QUERY_SP_BY_MOBILE = "http://10.0.2.2:8080/serviceProviders/mobileNumber/";

    //private static final String QUERY_SP_BY_MOBILE = "http://ec2-52-74-141-170.ap-southeast-1.compute.amazonaws.com:8080/serviceProviders/mobileNumber/";

    //private static final String IS_SP = "http://10.0.2.2:8080/serviceProviders/isServiceProvider/mobileNumber/";

    //private static final String IS_SP = "http://ec2-52-74-141-170.ap-southeast-1.compute.amazonaws.com:8080/serviceProviders/isServiceProvider/mobileNumber/";

    private String domain;
    private String getSPByMobileURL;
    private String isServiceProviderURL;
    public GetServiceProvider(Context context) {
        CSProperties csProperties = new CSProperties(context);
        domain = csProperties.getDomain();
        Log.i("", "In get Service provider class and the domain is "+domain);
        getSPByMobileURL = domain+"/serviceProviders/mobileNumber/";
        isServiceProviderURL = domain+"/serviceProviders/isServiceProvider/mobileNumber/";
    }

    public ServiceProvider getServiceProvider(long mobileNumber) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        ObjectMapper objectMapper = new ObjectMapper();
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(getSPByMobileURL+mobileNumber);
        //HttpResponse response = client.execute(request);

        HttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (ClientProtocolException cpe) {
            Log.d("", "Error occured while getting sp by mobile number");
        }

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

    public boolean isServiceProvider(long mobileNumber) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        ObjectMapper objectMapper = new ObjectMapper();
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(isServiceProviderURL+mobileNumber);
        //HttpResponse response = client.execute(request);

        HttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (ClientProtocolException cpe) {
            Log.d("", "Error occured while checking if service provider");
        }

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
        if (jsonResponse.equalsIgnoreCase("false")) return false;
        return true;
    }

}
