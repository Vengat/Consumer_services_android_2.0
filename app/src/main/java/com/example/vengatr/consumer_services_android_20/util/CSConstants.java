package com.example.vengatr.consumer_services_android_20.util;

/**
 * Created by vengat.r on 7/21/2015.
 */
public interface CSConstants {

    String LOG = "com.consumerservices.app";

    //This gets a new job
    String QUERY_NEW_JOB_URL = "/jobs";

    String QUERY_JOB_BY_ID = "/jobs/id/";

    String QUERY_URL_GET_JOBS_BY_MOBILE_NUMBER = "/customers/jobs/mobileNumber/";

    String QUERY_SP_BY_MOBILE = "/serviceProviders/mobileNumber/";

    String IS_SP = "/serviceProviders/isServiceProvider/mobileNumber/";
}
