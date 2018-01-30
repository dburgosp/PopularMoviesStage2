package com.example.android.popularmoviesstage2.utils;

import android.content.Context;
import android.util.Log;

import com.example.android.popularmoviesstage2.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class DateTimeUtils {
    private static final String TAG = DateTimeUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link DateTimeUtils}
     * object. This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name DateTimeUtils (and an object instance of DateTimeUtils is not
     * needed).
     */
    private DateTimeUtils() {
    }

    /**
     * Helper method to extract the year from a string representing a date in "yyyy-mm-dd" format.
     *
     * @param dateString is the date in "yyyy-mm-dd" format.
     * @return the year extracted from the date, or an empty string if there has been any problem.
     */
    public static String getYear(String dateString) {
        Log.i(TAG, "(getYear) Date: " + dateString);
/*        Calendar calendar = Calendar.getInstance();
        String year = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault());

        try {
            calendar.setTime(format.parse(dateString));
            year = String.valueOf(calendar.get(Calendar.YEAR));
        } catch (java.text.ParseException e) {
            Log.e(TAG, "(getYear) Error parsing string: " + e);
        }

        Log.i(TAG, "(getYear) Year: " + year);
        return year;*/

        Log.i(TAG, "(getYear) Year: " + dateString.substring(0, 3));
        return dateString.substring(0, 4);
    }

    /**
     * Helper method to convert minutes to a string with hours and minutes.
     *
     * @param minutes is the number of minutes.
     * @return the hours and minutes string formatted from the minutes, or an empty string if there
     * has been any problem.
     */
    public static String getHoursAndMinutes(Context context, int minutes) {
        Log.i(TAG, "(getHoursAndMinutes) Minutes: " + minutes);

        String stringHours = "";
        if (minutes >= 60)
            stringHours = minutes / 60 + context.getResources().getString(R.string.string_hours) + " ";

        String stringMinutes = "";
        if (minutes % 60 != 0)
            stringMinutes = minutes % 60 + context.getResources().getString(R.string.string_minutes);

        String hoursAndMinutes = stringHours + stringMinutes;
        Log.i(TAG, "(getHoursAndMinutes) Hours and minutes: " + hoursAndMinutes);
        return hoursAndMinutes;
    }

    /**
     * Helper method to convert a string date to the normal date format for the current country.
     *
     * @param inputDate the original string date.
     * @return a string containing the formatted date.
     */
    public static String getDate(String inputDate) {
        // Parse input date string.
        Date date = new Date();
        SimpleDateFormat dateFormatIn;
        try {
            // Try ISO 8601 format.
            dateFormatIn = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            date = dateFormatIn.parse(inputDate);
        } catch (java.text.ParseException e1) {
            try {
                // If the string can't be parsed with the specified format, try another one.
                dateFormatIn = new SimpleDateFormat("yyyy-mm-dd");
                date = dateFormatIn.parse(inputDate);
            } catch (java.text.ParseException e2) {
                // Return inputDate if we can't parse it.
                Log.e(TAG, "(getDate) Error parsing date: " + e2);
                return inputDate;
            }
        }

        // Format output date string.
        DateFormat dateFormatOut = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
        return dateFormatOut.format(date);
    }
}
