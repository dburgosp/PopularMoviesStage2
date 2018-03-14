package com.example.android.popularmoviesstage2.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.classes.Tmdb;

import static android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences;

public class MyPreferences {

    public MyPreferences() {

    }

    /**
     * Helper method to get the sort order string value for the "sort_by" parameter in the search
     * url for fetching results from TMDB.
     *
     * @param context is the context of the calling activity.
     * @return the corresponding sort order string value for using with the "sort_by" parameter in
     * the search url for fetching results from TMDB.
     */
    public static String getSortOrder(Context context) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        String sortByPreferences = sharedPreferences.getString(
                context.getResources().getString(R.string.preferences_sort_by_key),
                context.getResources().getString(R.string.preferences_sort_by_popularity_desc_value));

        // Ascending sort order by popularity.
        if (sortByPreferences.equals(context.getResources().getString(
                R.string.preferences_sort_by_popularity_asc_value))) {
            return Tmdb.TMDB_VALUE_POPULARITY_ASC;
        }

        // Descending sort order by primary release date.
        if (sortByPreferences.equals(context.getResources().getString(
                R.string.preferences_sort_by_year_desc_value))) {
            return Tmdb.TMDB_VALUE_PRIMARY_RELEASE_DATE_DESC;
        }

        // Ascending sort order by primary release date.
        if (sortByPreferences.equals(context.getResources().getString(
                R.string.preferences_sort_by_year_asc_value))) {
            return Tmdb.TMDB_VALUE_PRIMARY_RELEASE_DATE_ASC;
        }

        // Descending sort order by users rating.
        if (sortByPreferences.equals(context.getResources().getString(
                R.string.preferences_sort_by_rating_desc_value))) {
            return Tmdb.TMDB_VALUE_VOTE_AVERAGE_DESC;
        }

        // Ascending sort order by users rating.
        if (sortByPreferences.equals(context.getResources().getString(
                R.string.preferences_sort_by_rating_asc_value))) {
            return Tmdb.TMDB_VALUE_VOTE_AVERAGE_ASC;
        }

        // Default value: descending sort order by popularity.
        return Tmdb.TMDB_VALUE_POPULARITY_DESC;
    }
}
