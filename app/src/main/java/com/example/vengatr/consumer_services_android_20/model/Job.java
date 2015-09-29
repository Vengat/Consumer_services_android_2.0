package com.example.vengatr.consumer_services_android_20.model;

/**
 * Created by vengat.r on 6/11/2015.
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.ser.DateTimeSerializer;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.TimeZone;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Job implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private long id;

    private JobType jobType; //plumbing, painting etc

    private JobStatus jobStatus;

    private DaySegment daySegment;

    private String customerName;


    private long customerMobileNumber;


    private String serviceProviderName;


    private long serviceProviderMobileNumber;


    private String pincode;


    private Customer customer;


    private ServiceProvider serviceProvider;

    private Invoice invoice;

    //@JsonDeserialize(using=DateTimeDeserializer.class)
    //@JsonProperty(value = "dtInitiated")
    @JsonSerialize(using = DateTimeSerializer.class)
    private DateTime dateInitiated;

    //@JsonDeserialize(using=DateTimeDeserializer.class)
    //@JsonProperty(value = "dtPreferred")
    @JsonSerialize(using = DateTimeSerializer.class)
    private DateTime datePreferred;

    //@JsonDeserialize(using=DateTimeDeserializer.class)
    //@JsonProperty(value = "strtTime")
    @JsonSerialize(using = DateTimeSerializer.class)
    private DateTime startTime;

    //@JsonDeserialize(using=DateTimeDeserializer.class)
    //@JsonProperty(value = "dtDone")
    @JsonSerialize(using = DateTimeSerializer.class)
    private DateTime dateDone;

    private String description;

    private TimeZone timeZone;

    protected Job() {
    }

    public Job(JobType jobType, JobStatus jobStatus, String customerName, String pincode, String description, long customerMobileNumber, long serviceProviderMobileNumber, String serviceProviderName, DateTime datePreferred, DaySegment daySegment) {
        //this.id = id;
        this.jobType = jobType;
        this.customerName = customerName;
        this.pincode = pincode;
        this.jobStatus = jobStatus;
        this.description = description;
        this.customerMobileNumber = customerMobileNumber;
        this.serviceProviderMobileNumber = serviceProviderMobileNumber;
        this.serviceProviderName = serviceProviderName;
        this.dateInitiated = new DateTime();
        this.datePreferred = datePreferred;
        this.daySegment = daySegment;
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

    public DateTime getDateDone() {
        return dateDone;
    }

    public void setDateDone(DateTime dateDone) {
        this.dateDone = dateDone;
    }

    public long getId() {
        return id;
    }

    public DateTime getDateInitiated() {
        return dateInitiated;
    }

    public void setDateInitiated(DateTime dateInitiated) {
        this.dateInitiated = dateInitiated;
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

    public DaySegment getDaySegment() {
        return daySegment;
    }

    public void setDaySegment(DaySegment daySegment) {
        this.daySegment = daySegment;
    }

    public DateTime getDatePreferred() {
        return datePreferred;
    }

    public void setDatePreferred(DateTime datePreferred) {
        this.datePreferred = datePreferred;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public DateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(DateTime startTime) {
        this.startTime = startTime;
    }
}

