package com.example.vengatr.consumer_services_android_20.model;

/**
 * Created by vengat.r on 6/11/2015.
 */

import java.io.Serializable;
import java.util.Date;


public class Job implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private long id;

    private JobType jobType; //plumbing, painting etc

    private JobStatus jobStatus;


    private String customerName;


    private long customerMobileNumber;


    private String serviceProviderName;


    private long serviceProviderMobileNumber;


    private String pincode;


    private Customer customer;


    private ServiceProvider serviceProvider;


    private Date dateInitiated;

    private Date dateDone;


    private String description;

    protected Job() {
    }

    public Job(JobType jobType, JobStatus jobStatus, String customerName, String pincode, String description, long customerMobileNumber, long serviceProviderMobileNumber, String serviceProviderName, Date dateInitiated) {
        //this.id = id;
        this.jobType = jobType;
        this.customerName = customerName;
        this.pincode = pincode;
        this.jobStatus = jobStatus;
        this.description = description;
        this.customerMobileNumber = customerMobileNumber;
        this.serviceProviderMobileNumber = serviceProviderMobileNumber;
        this.serviceProviderName = serviceProviderName;
        this.dateInitiated = dateInitiated;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    public JobStatus getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(JobStatus jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getServiceProviderName() {
        return serviceProviderName;
    }

    public void setServiceProviderName(String serviceProviderName) {
        this.serviceProviderName = serviceProviderName;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public Date getDateDone() {
        return dateDone;
    }

    public void setDateDone(Date dateDone) {
        this.dateDone = dateDone;
    }

    public long getId() {
        return id;
    }

    public Date getDateInitiated() {
        return dateInitiated;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getServiceProviderMobileNumber() {
        return serviceProviderMobileNumber;
    }

    public void setServiceProviderMobileNumber(long serviceProviderMobileNumber) {
        this.serviceProviderMobileNumber = serviceProviderMobileNumber;
    }

    public long getCustomerMobileNumber() {
        return customerMobileNumber;
    }

    public void setCustomerMobileNumber(long customerMobileNumber) {
        this.customerMobileNumber = customerMobileNumber;
    }



}

