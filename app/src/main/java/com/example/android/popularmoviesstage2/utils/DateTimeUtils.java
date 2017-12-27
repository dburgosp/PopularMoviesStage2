package com.example.android.popularmoviesstage2.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
     * @param dateString is the "releaseDate" obtained from the TMDB API.
     * @return the year extracted from the date, or an empty string if there has been any problem.
     */
    public static String getYear(String dateString) {
        Log.i(TAG, "(getYear) Release date: " + dateString);
        Calendar calendar = Calendar.getInstance();
        String year = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault());

        try {
            calendar.setTime(format.parse(dateString));
            year = String.valueOf(calendar.get(Calendar.YEAR));
        } catch (java.text.ParseException e) {
            Log.e(TAG, "(getYear) Error parsing string: " + e);
        }

        Log.i(TAG, "(getYear) Year: " + year);
        return year;
    }
}
