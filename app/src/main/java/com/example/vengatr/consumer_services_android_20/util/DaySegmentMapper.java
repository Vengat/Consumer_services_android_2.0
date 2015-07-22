package com.example.vengatr.consumer_services_android_20.util;

import android.util.Log;

import com.example.vengatr.consumer_services_android_20.model.DaySegment;

/**
 * Created by vengat.r on 7/19/2015.
 */
public class DaySegmentMapper {

    public static DaySegment getDaySegment(String daySegmentValue) {
        DaySegment daySegment = null;
        Log.i("", "Daysegment value is "+daySegmentValue);
        switch (daySegmentValue) {
            case "9-11 AM":
                Log.i("","OK 9-11 is selected");
                daySegment = DaySegment.MORNING;
                break;
            case "11-1 PM":
                daySegment = DaySegment.FORENOON;
                break;
            case "1-3 PM":
                daySegment = DaySegment.AFTERNOON;
                break;
            case "3-5 PM":
                daySegment = DaySegment.EVENING;
                break;
            default:
                daySegment = null;
                break;
        }
        return daySegment;
    }

    public static DaySegment getDaySegmentTime(String time) {
        DaySegment daySegment = null;
        switch (time) {
            case "9":
                daySegment = DaySegment.MORNING;
                break;
            case "10":
                daySegment = DaySegment.MORNING;
                break;
            case "11":
                daySegment = DaySegment.FORENOON;
                break;
            case "12":
                daySegment = DaySegment.FORENOON;
                break;
            case "1":
                daySegment = DaySegment.AFTERNOON;
                break;
            case "2":
                daySegment = DaySegment.AFTERNOON;
                break;
            case "3":
                daySegment = DaySegment.EVENING;
                break;
            case "4":
                daySegment = DaySegment.EVENING;
                break;
            case "5":
                daySegment = DaySegment.EVENING;
                break;
            default:
                daySegment = null;
                break;
        }
        return daySegment;
    }

}
