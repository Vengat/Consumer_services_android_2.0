package com.example.vengatr.consumer_services_android_20.model;



import java.io.Serializable;
import java.util.Set;


public class ServiceProvider implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private long id;
	

	private long mobileNumber;
	

	private String name;
	

	private Set<JobType> jobTypes;

	private Set<String> pincodesServiced;
	//private List<Job> jobs;
	
	protected ServiceProvider() {
		
	}

	public ServiceProvider(long mobileNumber, String name, Set<JobType> jobTypes, Set<String> pincodesServiced) {
		this.mobileNumber = mobileNumber;
		this.name = name;
		this.jobTypes = jobTypes;
		this.pincodesServiced = pincodesServiced;
	}

	public long getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<JobType> getJobTypes() {
		return jobTypes;
	}

	public void setJobTypes(Set<JobType> jobTypes) {
		this.jobTypes = jobTypes;
	}

	public long getId() {
		return id;
	}

	public void setPincodesServiced(Set<String> pincodes) {
		this.pincodesServiced = pincodes;
	}

	public Set<String> getPincodesServiced() {
		return this.pincodesServiced;
	}
		

}
