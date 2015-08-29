package com.example.vengatr.consumer_services_android_20.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Invoice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1149987956134162218L;
	
	private long id;
	
	private String customerName;
	
	private long customerMobileNumber;
	
	private String serviceProviderName;
	

	private long serviceProviderMobileNumber;
	
	private Customer customer;
	
	private ServiceProvider serviceProvider;
	
	private Job job;

	private String couponCode;
	
	private BigDecimal labourCharges;
	
	private BigDecimal materialCharges;
	
	private BigDecimal totalCharges;
	
	private BigDecimal discountedLabourCharges;
	
	private BigDecimal discountedMaterialCharges;
	
	private BigDecimal discountedTotalCharges;
	
	private long jobId;
	
	private Date invoiceDate;

	private static int ROUNDING_MODE = BigDecimal.ROUND_CEILING;
	
	protected Invoice() {
		
	}
	
	public Invoice(long jobId, long customerMobileNumber, String customerName, long serviceProviderMobileNumber, String serviceProviderName, BigDecimal labourCharges, BigDecimal materialCharges) {
		this.jobId = jobId;
		this.customerMobileNumber = customerMobileNumber;
		this.customerName = customerName;
		this.serviceProviderMobileNumber = serviceProviderMobileNumber;
		this.serviceProviderName = serviceProviderName;
		this.labourCharges = labourCharges;
		this.materialCharges = materialCharges;
		this.invoiceDate = new Date();
	}
	
	public BigDecimal getLabourCharges() {
		return labourCharges;
	}

	public void setLabourCharges(BigDecimal labourCharges) throws IllegalArgumentException {
		if (labourCharges.compareTo(BigDecimal.ZERO) < 0 && labourCharges != null) throw new IllegalArgumentException("Charges should be non-negative labour charges");;
		this.labourCharges = labourCharges;
		this.labourCharges = rounded(this.labourCharges);
	}

	public BigDecimal getMaterialCharges() {
		return materialCharges;
	}

	public void setMaterialCharges(BigDecimal materialCharges) throws IllegalArgumentException {
		if (materialCharges.compareTo(BigDecimal.ZERO) < 0 && materialCharges != null) throw new IllegalArgumentException("Charges should be non-negative material charges");
		this.materialCharges = materialCharges;
		this.materialCharges = rounded(this.materialCharges);
	}

	public BigDecimal getTotalCharges() {
		return totalCharges;
	}

	public void setTotalCharges(BigDecimal totalCharges) throws IllegalArgumentException {
		if (totalCharges.compareTo(BigDecimal.ZERO) < 0 && totalCharges != null) throw new IllegalArgumentException("Charges should be non-negative material charges");
		this.totalCharges = getLabourCharges().add(getMaterialCharges());
		this.totalCharges = rounded(this.totalCharges);
	}
	
	public BigDecimal getDiscountedLabourCharges() {
		return discountedLabourCharges;
	}

	public void setDiscountedLabourCharges(BigDecimal discountedLabourCharges) {
		if (discountedLabourCharges.compareTo(BigDecimal.ZERO) < 0 && discountedLabourCharges != null) throw new IllegalArgumentException("Charges should be non-negative labour charges");
		this.discountedLabourCharges = rounded(discountedLabourCharges);
	}

	public BigDecimal getDiscountedMaterialCharges() {
		return discountedMaterialCharges;
	}

	public void setDiscountedMaterialCharges(BigDecimal discountedMaterialCharges) {
		if (discountedMaterialCharges.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("Charges should be non-negative material charges");
		this.discountedMaterialCharges = rounded(discountedMaterialCharges);
	}

	public BigDecimal getDiscountedTotalCharges() {
		return discountedTotalCharges;
	}

	public void setDiscountedTotalCharges(BigDecimal discountedTotalCharges) {
		if (discountedTotalCharges.compareTo(BigDecimal.ZERO) < 0 && discountedTotalCharges != null) throw new IllegalArgumentException("Charges should be non-negative total charges");
		this.discountedTotalCharges = rounded(discountedTotalCharges);
	}
	
	public long getId() {
		return id;
	}
	
	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public long getCustomerMobileNumber() {
		return customerMobileNumber;
	}

	public void setCustomerMobileNumber(long customerMobileNumber) {
		this.customerMobileNumber = customerMobileNumber;
	}

	public String getServiceProviderName() {
		return serviceProviderName;
	}

	public void setServiceProviderName(String serviceProviderName) {
		this.serviceProviderName = serviceProviderName;
	}

	public long getServiceProviderMobileNumber() {
		return serviceProviderMobileNumber;
	}

	public void setServiceProviderMobileNumber(long serviceProviderMobileNumber) {
		this.serviceProviderMobileNumber = serviceProviderMobileNumber;
	}
	
	private BigDecimal rounded(BigDecimal amount){
	    return amount.setScale(2, ROUNDING_MODE);
    }
	 
	public long getJobId() {
	    return jobId;
	}

	public void setJobId(long jobId) {
	    this.jobId = jobId;
	}

	public Date getInvoiceDate() {
	    return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
	    this.invoiceDate = invoiceDate;
	}
	

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

}
