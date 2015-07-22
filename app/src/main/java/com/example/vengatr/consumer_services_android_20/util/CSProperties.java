package com.example.vengatr.consumer_services_android_20.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by vengat.r on 7/22/2015.
 */
public class CSProperties {


    private Context context;
    private Properties properties;
    private AssetManager assetManager;


    public CSProperties(Context context) {
        this.context = context;
        //creates a new object ‘Properties’
        properties = new Properties();
    }


    public Properties getProperties() {
        try {
            //access to the folder ‘assets’
            AssetManager am = context.getAssets();
            //opening the file
            InputStream inputStream = am.open("config.properties");
            //loading of the properties
            properties.load(inputStream);
        }
        catch (IOException e) {
            Log.e("PropertiesReader", e.toString());
        }
        return properties;
    }


    public String getEnvironment() {
        return getProperties().getProperty("env", "stage");
    }

    public String getDomain() {
        Log.i("", "properties.getProperty(\"local.domain\") "+getProperties().getProperty("local.domain"));
        return getEnvironment().equalsIgnoreCase("stage") ? getProperties().getProperty("stage.domain") : getProperties().getProperty("local.domain");
    }


}
