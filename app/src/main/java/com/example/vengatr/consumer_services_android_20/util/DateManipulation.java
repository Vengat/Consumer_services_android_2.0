package com.example.vengatr.consumer_services_android_20.util;

import android.util.Log;

import com.example.vengatr.consumer_services_android_20.model.DaySegment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class DateManipulation {

    public static String dateFormatIST(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
        return dateFormat.format(date);
    }

    public static Date dateUTCtoIST(Date date) {
        Calendar cal = Calendar.getInstance();
        TimeZone timeZone = TimeZone.getTimeZone("IST");
        cal.setTimeZone(timeZone);
        cal.setTime(date);
        return new Date(cal.getTimeInMillis());
    }

    public static Date dateISTToUTC(Date date) {
        Calendar cal = Calendar.getInstance();
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        cal.setTimeZone(timeZone);
        cal.setTime(date);
        return new Date(cal.getTimeInMillis());
    }

	public static Date getYesterdayDate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		Log.d("", "Yesterday " + new Date(cal.getTimeInMillis()));
		return new Date(cal.getTimeInMillis());
	}
	
	public static Date getTomorrowDate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, +1);
		Log.d("", "Tomorrow " + new Date(cal.getTimeInMillis()));
		return new Date(cal.getTimeInMillis());
	}
	
	public static boolean validAssignDate(Date date) {
		Log.d("", "date.after(getYesterdayDate()) " + date.after(getYesterdayDate()));
		return date.after(getYesterdayDate());
	}
	
	public static boolean isTodayDate(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("d", Locale.ENGLISH);
		return dateFormat.format(date).equals(dateFormat.format(new Date()));
	}
	
	public static boolean isSegmentAssignableToday(Date date, DaySegment daySegment) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 0);
		DateFormat df = new SimpleDateFormat("HH");
		String time = df.format(date);
		Log.d("", " df.format(date) " + df.format(date));
		Log.d("", "daySegment " + DaySegment.valueOf(daySegment.toString()));
		String dSegment = DaySegment.valueOf(daySegment.toString()).getDaySegment();
		Log.d("", "date.equals(new Date(cal.getTimeInMillis())) " + date.equals(new Date(cal.getTimeInMillis())));
        Log.i("time is", "*"+time);
		//Check if the date is today's
		if (isTodayDate(date)) {
            if (Long.parseLong(time) >=0 && Long.parseLong(time) < 9) {
                Log.i("", "dSegment "+dSegment);
                Log.i("", "dSegment "+dSegment.isEmpty());
                if (!dSegment.isEmpty()) {
                    Log.i("", "^^^^^^^Customer is in the 0-9 slot^^^^^^^^");
                    return true;
                }
                //If the CURRENT time is 9-11 then except 9-11 segment all other greater segments are applicable
            } else if (Long.parseLong(time) >= 9 && Long.parseLong(time) < 11) {
				 if (!dSegment.equals("9-11") || !dSegment.isEmpty()) {
					 return true;
				 }
			 } else if (Long.parseLong(time) >= 11 && Long.parseLong(time) < 1) {
				 if (!dSegment.equals("9-11") && !dSegment.equals("11-1") || !dSegment.isEmpty()) {
					 return true;
				 }
			 } else if (Long.parseLong(time) >= 1 && Long.parseLong(time) < 3) {
				 if (!dSegment.equals("9-11") && !dSegment.equals("11-1") && !dSegment.equals("1-3") || !dSegment.isEmpty()) {
					 return true;
				 }
			 } else {//if (Long.parseLong(time) >= 3 && Long.parseLong(time) < 9) {
				 Log.d("", "Day is over for services");
				return false;
			 }
		} 
		return false;
	}
	
	
	public static boolean isDateInFuture(Date date) {
		return date.after(new Date());
	}
	
	public static boolean validAssignDateDaySegment(Date date, DaySegment daySegment) {
		 if (!validAssignDate(date)) return false;
		 Log.d("", "Not yesterday's date");
		 if (isDateInFuture(date)) return true;
	     if (!isSegmentAssignableToday(date, daySegment)) return false;
		 Log.d("", "isSegmentAssignableToday ");
		 return true;
	}

	public static Date convertStringToDate(String dateInString) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		return formatter.parse(dateInString);
	}

	public static boolean isDateEligibleForPosting(Date date, DaySegment daySegment) {
		return validAssignDateDaySegment(date, daySegment);
	}

    public static String[] getApplicableDaySegments(Date date) {
        if (isDateInFuture(date)) return new String[]{"9-11", "11-1", "1-3", "3-5"};
        if (isTodayDate(date)) {
            DateFormat df = new SimpleDateFormat("h", Locale.ENGLISH);
            String time = df.format(new Date());
            //If the CURRENT time is 9-11 then except 9-11 segment all other greater segments are applicable
            if (Long.parseLong(time) >= 9 && Long.parseLong(time) < 11) {
                return new String[]{"11-1", "1-3", "3-5"};
            } else if (Long.parseLong(time) >= 11 && Long.parseLong(time) < 1) {
                return new String[]{"1-3", "3-5"};
            } else if (Long.parseLong(time) >= 1 && Long.parseLong(time) < 3) {
                return new String[]{"3-5"};
            } else {//if (Long.parseLong(time) >= 3 && Long.parseLong(time) < 9) {
                Log.d("", "Day is over for services");
                return new String[]{"Select next day"};
            }
        }
        return new String[] {"Select valid working day"};
    }

	public static String getApplicableDaySegment(Date date) {
		if (isDateInFuture(date)) return "future_date";
        Log.i("", "Not a future date");
		if (isTodayDate(date)) {
			DateFormat df = new SimpleDateFormat("HH", Locale.ENGLISH);
			String time = df.format(new Date());
			Log.i("", "Todays date");
            Log.i("", "Time in getApplicableDaySegmen---------------------------------------------------------t "+time);
			//If the CURRENT time is 9-11 then except 9-11 segment all other greater segments are applicable
			if (Long.parseLong(time) >=0 && Long.parseLong(time) < 9) {
				Log.i("", "^^^^^^^Customer is in the 0-9 slot^^^^^^^^");
				return "at_early_morning";
			} else if (Long.parseLong(time) >= 9 && Long.parseLong(time) < 11) {
                Log.i("", "^^^^^^^Customer is in the 9-11 slot^^^^^^^^");
				return "at_morning";
			} else if (Long.parseLong(time) >= 11 && Long.parseLong(time) < 1) {
                Log.i("", "^^^^^^^Customer is in the 11-1 slot^^^^^^^^");
				return "at_forenoon";
			} else if (Long.parseLong(time) >= 1 && Long.parseLong(time) < 3) {
                Log.i("", "^^^^^^^Customer is in the 1-3 slot^^^^^^^^");
				return "at_afternoon";
			} else {//if (Long.parseLong(time) >= 3 && Long.parseLong(time) < 9) {
				Log.d("", "^^^^^^^^^^^^^^Day is over for services^^^^^^^^^^^^^^^");
				return "at_evening";
			}
		}
		return "Select valid working day";
	}

}
