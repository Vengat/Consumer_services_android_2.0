package com.example.vengatr.consumer_services_android_20.model;

public enum DaySegment {
	MORNING("9-11"),
	FORENOON("11-1"),
	AFTERNOON("1-3"),
	EVENING("3-5");
	
	String daySegment;
	DaySegment(String daySegment) {
		this.daySegment = daySegment;
	}
	
	public String getDaySegment() {
		return this.daySegment;
	}
}
