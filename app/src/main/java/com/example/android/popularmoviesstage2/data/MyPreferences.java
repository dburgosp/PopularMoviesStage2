package com.example.android.popularmoviesstage2.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.android.popularmoviesstage2.R;

import java.util.Locale;

import static android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.example.android.popularmoviesstage2.classes.Tmdb.TMDB_VALUE_POPULARITY_ASC;
import static com.example.android.popularmoviesstage2.classes.Tmdb.TMDB_VALUE_POPULARITY_DESC;
import static com.example.android.popularmoviesstage2.classes.Tmdb.TMDB_VALUE_PRIMARY_RELEASE_DATE_ASC;
import static com.example.android.popularmoviesstage2.classes.Tmdb.TMDB_VALUE_PRIMARY_RELEASE_DATE_DESC;
import static com.example.android.popularmoviesstage2.classes.Tmdb.TMDB_VALUE_VOTE_AVERAGE_ASC;
import static com.example.android.popularmoviesstage2.classes.Tmdb.TMDB_VALUE_VOTE_AVERAGE_DESC;

public class MyPreferences {

    /**
     * Empty constructor.
     */
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

        // Default value: descending sort order by popularity.
        String retValue = TMDB_VALUE_POPULARITY_DESC;

        // Ascending sort order by popularity.
        if (sortByPreferences.equals(context.getResources().getString(
                R.string.preferences_sort_by_popularity_asc_value)))
            retValue = TMDB_VALUE_POPULARITY_ASC;

        // Descending sort order by primary release date.
        if (sortByPreferences.equals(context.getResources().getString(
                R.string.preferences_sort_by_year_desc_value)))
            retValue = TMDB_VALUE_PRIMARY_RELEASE_DATE_DESC;

        // Ascending sort order by primary release date.
        if (sortByPreferences.equals(context.getResources().getString(
                R.string.preferences_sort_by_year_asc_value)))
            retValue = TMDB_VALUE_PRIMARY_RELEASE_DATE_ASC;

        // Descending sort order by users rating.
        if (sortByPreferences.equals(context.getResources().getString(
                R.string.preferences_sort_by_rating_desc_value)))
            retValue = TMDB_VALUE_VOTE_AVERAGE_DESC;

        // Ascending sort order by users rating.
        if (sortByPreferences.equals(context.getResources().getString(
                R.string.preferences_sort_by_rating_asc_value)))
            retValue = TMDB_VALUE_VOTE_AVERAGE_ASC;

        return retValue;
    }

    /**
     * Helper method to get the human-readable string value of the current sort order.
     *
     * @param context is the context of the calling activity.
     * @return the current sort order written in a human-readable language.
     */
    public static String getSortOrderTitle(Context context) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        String sortByPreferences = sharedPreferences.getString(
                context.getResources().getString(R.string.preferences_sort_by_key),
                context.getResources().getString(R.string.preferences_sort_by_popularity_desc_value));

        // Default value: descending sort order by popularity.
        String retValue = context.getResources().getString(
                R.string.preferences_sort_by_popularity_desc_title);

        // Ascending sort order by popularity.
        if (sortByPreferences.equals(context.getResources().getString(
                R.string.preferences_sort_by_popularity_asc_value)))
            retValue = context.getResources().getString(
                    R.string.preferences_sort_by_popularity_asc_title);

        // Descending sort order by primary release date.
        if (sortByPreferences.equals(context.getResources().getString(
                R.string.preferences_sort_by_year_desc_value)))
            retValue = context.getResources().getString(
                    R.string.preferences_sort_by_year_desc_title);

        // Ascending sort order by primary release date.
        if (sortByPreferences.equals(context.getResources().getString(
                R.string.preferences_sort_by_year_asc_value)))
            retValue = context.getResources().getString(
                    R.string.preferences_sort_by_year_asc_title);

        // Descending sort order by users rating.
        if (sortByPreferences.equals(context.getResources().getString(
                R.string.preferences_sort_by_rating_desc_value)))
            retValue = context.getResources().getString(
                    R.string.preferences_sort_by_rating_desc_title);

        // Ascending sort order by users rating.
        if (sortByPreferences.equals(context.getResources().getString(
                R.string.preferences_sort_by_rating_asc_value)))
            retValue = context.getResources().getString(
                    R.string.preferences_sort_by_rating_asc_title);

        return retValue;
    }

    /**
     * Helper method to get the language string value for the "language" parameter in the search
     * url for fetching results from TMDB.
     *
     * @param context is the context of the calling activity.
     * @return the corresponding language for using with the "language" parameter in the search url
     * for fetching results from TMDB.
     */
    public static String getLanguage(Context context) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        String sortByPreferences = sharedPreferences.getString(
                context.getResources().getString(R.string.preferences_language_key),
                context.getResources().getString(R.string.preferences_language_locale_value));

        // Default value: system language.
        String retValue = Locale.getDefault().getLanguage();

        // English.
        if (sortByPreferences.equals(context.getResources().getString(
                R.string.preferences_language_en_value)))
            retValue = "en";

        // Spanish.
        if (sortByPreferences.equals(context.getResources().getString(
                R.string.preferences_language_es_value)))
            retValue = "es";

        return retValue;
    }
}
