/**
 * 
 */
package com.example.vengatr.consumer_services_android_20.model;

/**
 * @author Vengat
 *
 */
public enum JobType {
	PLUMBING("plumbing"),
	ELECTRICAL("electrical"),
	UNDEFINED("undefined");

	String val;
	JobType(String val) {
		this.val = val;
	}

	String getVal() {
		return this.val;
	}
}
