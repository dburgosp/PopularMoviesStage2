package com.example.android.popularmoviesstage2.utils;

import android.content.Context;
import android.util.Log;

import com.example.android.popularmoviesstage2.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class DateTimeUtils {
    private static final String TAG = DateTimeUtils.class.getSimpleName();

    // Public date formats.
    public static final int DATE_FORMAT_LONG = DateFormat.LONG;
    public static final int DATE_FORMAT_SHORT = DateFormat.SHORT;
    public static final int DATE_FORMAT_MEDIUM = DateFormat.MEDIUM;

    // Public week days.
    public static final int WEEK_DAY_MONDAY = Calendar.MONDAY;
    public static final int WEEK_DAY_TUESDAY = Calendar.TUESDAY;
    public static final int WEEK_DAY_WEDNESDAY = Calendar.WEDNESDAY;
    public static final int WEEK_DAY_THURSDAY = Calendar.THURSDAY;
    public static final int WEEK_DAY_FRIDAY = Calendar.MONDAY;
    public static final int WEEK_DAY_SATURDAY = Calendar.SATURDAY;
    public static final int WEEK_DAY_SUNDAY = Calendar.SUNDAY;

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
        String year = "";
        if (dateString != null && !dateString.equals("") && !dateString.isEmpty())
            year = dateString.substring(0, 4);
        Log.i(TAG, "(getYear) Year: " + year);
        return year;
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
     * Helper method to convert a string date to the long date format for the current country.
     *
     * @param inputDate  is the original string date, in ISO 8601 or "yyyy-MM-dd" format.
     * @param dateFormat is the date format to be applied. Available values are
     *                   {@link DateTimeUtils#DATE_FORMAT_LONG},
     *                   {@link DateTimeUtils#DATE_FORMAT_SHORT},
     *                   {@link DateTimeUtils#DATE_FORMAT_MEDIUM}
     * @return a string containing the formatted date.
     */
    public static String getStringDate(String inputDate, int dateFormat) {
        // Parse input date string.
        Date date = new Date();
        SimpleDateFormat dateFormatIn;
        try {
            // Try ISO 8601 format.
            dateFormatIn = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                    Locale.getDefault());
            date = dateFormatIn.parse(inputDate);
        } catch (java.text.ParseException e1) {
            try {
                // If the string can't be parsed with the specified format, try another one.
                dateFormatIn = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                date = dateFormatIn.parse(inputDate);
            } catch (java.text.ParseException e2) {
                // Return inputDate if we can't parse it.
                Log.e(TAG, "(getStringDate) Error parsing date: " + e2);
                return inputDate;
            }
        }

        // Format output date string.
        DateFormat dateFormatOut = DateFormat.getDateInstance(dateFormat, Locale.getDefault());
        return dateFormatOut.format(date);
    }

    /**
     * Helper method to get the current date in "yyyy-MM-dd" format.
     *
     * @return the current system date.
     */
    public static Date getCurrentDate() {
        Date date = new Date();
        return date;
    }

    /**
     * Helper method to get the date of a given weekday into the week of a given date.
     *
     * @param date    is the date of the week.
     * @param weekDay is the weekday. Available values are:
     *                {@link DateTimeUtils#WEEK_DAY_MONDAY},
     *                {@link DateTimeUtils#WEEK_DAY_TUESDAY},
     *                {@link DateTimeUtils#WEEK_DAY_WEDNESDAY},
     *                {@link DateTimeUtils#WEEK_DAY_THURSDAY},
     *                {@link DateTimeUtils#WEEK_DAY_FRIDAY},
     *                {@link DateTimeUtils#WEEK_DAY_SATURDAY},
     *                {@link DateTimeUtils#WEEK_DAY_SUNDAY}.
     * @return a string with the date of the given weekday into the given week date.
     */
    public static String getStringWeekday(Date date, int weekDay) {
        // Get monday of the current week.
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, weekDay);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // Format the date and return it.
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String outDate = dateFormat.format(calendar.getTime());
        return outDate;
    }

    /**
     * Helper method to get a string with the current date in "yyyy-MM-dd" format.
     *
     * @return a string containing the current date.
     */
    public static String getStringCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        String currentDate = dateFormat.format(date);
        return currentDate;
    }

    /**
     * Helper method to add or subtract an amount of days to a given date.
     *
     * @param date is the date.
     * @param days is the number of days to add or subtract.
     * @return the new date.
     */
    public static Date getAddedDaysToDate(Date date, int days) {
        // Add days to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, days);

        // Return the date.
        Date outDate = calendar.getTime();
        return outDate;
    }

    /**
     * Helper method to add or subtract an amount of days to a given date.
     *
     * @param date is the date.
     * @param days is the number of days to add or subtract.
     * @return a string with the new date.
     */
    public static String getStringAddedDaysToDate(Date date, int days) {
        // Add days to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, days);

        // Format the date and return it.
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String outDate = dateFormat.format(calendar.getTime());
        return outDate;
    }
}
