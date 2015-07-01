/**
 * 
 */
package com.example.vengatr.consumer_services_android_20.model;

import java.io.Serializable;

/**
 * @author vengat.r
 *
 */

public class Customer implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private long id;
	

	private String name;
	

	private String pincode;
	

	private String address;
	

	private long mobileNumber;
	

	private String city;
	

	//private List<Job> jobs;
	
	protected Customer() {}
	
	public Customer(String name, String address, String city, String pincode, long mobileNumber) {
		this.name = name;
		this.address = address;
		this.city = city;
		this.pincode = pincode;
		this.mobileNumber = mobileNumber;
	}
	
	public Customer(String name, String pincode, long mobileNumber) {
		this.name = name;
		this.pincode = pincode;
		this.mobileNumber = mobileNumber;
	}
	
	@Override
	public String toString() {
	    return String.format("Customer name %s, address %s, city %s, pincode %s, mobileNumber %d%n", this.name, this.address, this.city, this.pincode, this.mobileNumber);	
	}
	
	public boolean equals(Object o) {
		if (o == this) return true;
		
		if (!(o instanceof Customer)) return false;
		
		Customer cust = (Customer) o;
		
		if (!cust.getAddress().equals(this.address)) return false;
		
		if(!cust.getCity().equals(this.city)) return false;
		
		if (cust.getMobileNumber() != this.mobileNumber) return false;
		
		if (!cust.getName().equals(this.name)) return false;
		
		if (!cust.getPincode().equals(this.pincode)) return false;
		
		if(cust.getId() != this.id) return false;
		
		return true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public long getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public long getId() {
		return id;
	}

}
