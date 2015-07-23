package com.example.vengatr.consumer_services_android_20.rest_classes;

import android.util.Log;

import com.example.vengatr.consumer_services_android_20.model.Job;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by vengat.r on 6/17/2015.
 */
public class PostJob {



    public static String POST(String url, Job job) throws JSONException, IOException {
        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";

            // 3. build jsonObject
            /*
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("id", job.getId());
            jsonObject.accumulate("jobType", job.getJobType());
            jsonObject.accumulate("jobStatus", job.getJobStatus());
            jsonObject.accumulate("customerName", job.getCustomerName());
            jsonObject.accumulate("customerMobileNumber", job.getCustomerMobileNumber());
            jsonObject.accumulate("serviceProviderName", job.getServiceProviderName());
            jsonObject.accumulate("serviceProviderMobileNumber", job.getServiceProviderMobileNumber());
            jsonObject.accumulate("pincode", job.getPincode());
            jsonObject.accumulate("dateInitiated", job.getDateInitiated());
            jsonObject.accumulate("dateDone", job.getDateDone());
            jsonObject.accumulate("description", job.getDescription());


            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();
            */
            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            ObjectMapper mapper = new ObjectMapper();
            Log.i("Pref date", "POST "+job.getDatePreferred().toString());
            json = mapper.writeValueAsString(job);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = null;

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

//        // 11. return result
        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }



}
