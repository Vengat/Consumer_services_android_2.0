/**
 * 
 */
package com.example.vengatr.consumer_services_android_20.model;

/**
 * @author Vengat
 *
 */
public enum JobStatus {
	OPEN("open"),
	ASSIGNED("assigned"),
    WIP("wip"),
    CANCELLED("cancelled"),
    CLOSED("closed");

    String val;
    JobStatus(String val) {
        this.val = val;
    }

    String getVal() {
        return val;
    }
}
