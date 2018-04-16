package com.example.android.popularmoviesstage2.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;

import com.example.android.popularmoviesstage2.R;

import static android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences;

public class MyPreferences {
    private static final String TAG = MyPreferences.class.getSimpleName();

    /**
     * Empty constructor.
     */
    public MyPreferences() {

    }

    /**
     * Helper method to fetch SharedPreferences for the current sort order.
     *
     * @param context is the context of the calling activity.
     * @return a string with the sort order value stored in the preferences file.
     */
    public static String getSortOrder(Context context) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        Resources res = context.getResources();
        String sortOrder = sharedPreferences.getString(
                res.getString(R.string.preferences_sort_by_key),
                res.getStringArray(R.array.preferences_sort_by_values_array)[0]);
        Log.i(TAG, "(getSortOrder) Current sort order: " + sortOrder);
        return sortOrder;
    }

    /**
     * Helper method to fetch SharedPreferences for the human-readable string value of the current
     * sort order.
     *
     * @param context is the context of the calling activity.
     * @return a string with the current sort order written in a human-readable language.
     */
    public static String getSortOrderTitle(Context context) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        String sortByPreferences = sharedPreferences.getString(
                context.getResources().getString(R.string.preferences_sort_by_key),
                context.getResources().getString(R.string.preferences_sort_by_popularity_desc_value));

        if (sortByPreferences.equals(context.getResources().getString(
                R.string.preferences_sort_by_popularity_asc_value))) {
            // Ascending sort order by popularity.
            return context.getResources().getString(
                    R.string.preferences_sort_by_popularity_asc_title);
        } else if (sortByPreferences.equals(context.getResources().getString(
                R.string.preferences_sort_by_year_desc_value))) {
            // Descending sort order by primary release date.
            return context.getResources().getString(
                    R.string.preferences_sort_by_year_desc_title);
        } else if (sortByPreferences.equals(context.getResources().getString(
                R.string.preferences_sort_by_year_asc_value))) {
            // Ascending sort order by primary release date.
            return context.getResources().getString(
                    R.string.preferences_sort_by_year_asc_title);
        } else if (sortByPreferences.equals(context.getResources().getString(
                R.string.preferences_sort_by_rating_desc_value))) {
            // Descending sort order by users rating.
            return context.getResources().getString(
                    R.string.preferences_sort_by_rating_desc_title);
        } else if (sortByPreferences.equals(context.getResources().getString(
                R.string.preferences_sort_by_rating_asc_value))) {
            // Ascending sort order by users rating.
            return context.getResources().getString(
                    R.string.preferences_sort_by_rating_asc_title);
        } else {
            // Default value: descending sort order by popularity.
            return context.getResources().getString(
                    R.string.preferences_sort_by_popularity_desc_title);
        }
    }

    /**
     * Helper method to fetch SharedPreferences for the current language.
     *
     * @param context is the context of the calling activity.
     * @return a string with the ISO 639-1 code of the language stored in the preferences file.
     */
    public static String getIsoLanguage(Context context) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        Resources res = context.getResources();
        String isoLanguage = sharedPreferences.getString(
                res.getString(R.string.preferences_language_key),
                res.getStringArray(R.array.preferences_language_values_array)[0]);
        Log.i(TAG, "(getIsoLanguage) ISO code for current selected language: " + isoLanguage);
        return isoLanguage;
    }

    /**
     * Helper method to fetch SharedPreferences for the language string value for the "language"
     * parameter to use in the search url for retrieving results from TMDB.
     *
     * @param context is the context of the calling activity.
     * @return the corresponding language for using with the "language" parameter in the search url
     * for fetching results from TMDB.
     */
    public static String getUpcomingMoviesHow(Context context) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        String upcomingMoviesHowPreferences = sharedPreferences.getString(
                context.getResources().getString(R.string.preferences_upcoming_movies_how_key),
                context.getResources().getString(
                        R.string.preferences_upcoming_movies_how_theaters_value));

        if (upcomingMoviesHowPreferences.equals(context.getResources().getString(
                R.string.preferences_upcoming_movies_how_digital_value))) {
            // Digital releases.
            return "4";
        } else if (upcomingMoviesHowPreferences.equals(context.getResources().getString(
                R.string.preferences_upcoming_movies_how_theaters_value))) {
            // DVD/Blu-ray.
            return "5";
        } else {
            // Default value: in theaters.
            return "3|2|1";
        }
    }
}
