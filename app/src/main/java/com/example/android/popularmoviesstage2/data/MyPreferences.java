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
     * Helper method to fetch SharedPreferences for the current sort order string value of the
     * "sort_by" parameter to use in the search url for retrieving results from TMDB.
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

        if (sortByPreferences.equals(context.getResources().getString(
                R.string.preferences_sort_by_popularity_asc_value))) {
            // Ascending sort order by popularity.
            return TMDB_VALUE_POPULARITY_ASC;
        } else if (sortByPreferences.equals(context.getResources().getString(
                R.string.preferences_sort_by_year_desc_value))) {
            // Descending sort order by primary release date.
            return TMDB_VALUE_PRIMARY_RELEASE_DATE_DESC;
        } else if (sortByPreferences.equals(context.getResources().getString(
                R.string.preferences_sort_by_year_asc_value))) {
            // Ascending sort order by primary release date.
            return TMDB_VALUE_PRIMARY_RELEASE_DATE_ASC;
        } else if (sortByPreferences.equals(context.getResources().getString(
                R.string.preferences_sort_by_rating_desc_value))) {
            // Descending sort order by users rating.
            return TMDB_VALUE_VOTE_AVERAGE_DESC;
        } else if (sortByPreferences.equals(context.getResources().getString(
                R.string.preferences_sort_by_rating_asc_value))) {
            // Ascending sort order by users rating.
            return TMDB_VALUE_VOTE_AVERAGE_ASC;
        } else {
            // Default value: descending sort order by popularity.
            return TMDB_VALUE_POPULARITY_DESC;
        }
    }

    /**
     * Helper method to fetch SharedPreferences for the human-readable string value of the current
     * sort order.
     *
     * @param context is the context of the calling activity.
     * @return the current sort order written in a human-readable language.
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
     * Helper method to fetch SharedPreferences for the language string value for the "language"
     * parameter to use in the search url for retrieving results from TMDB.
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

        if (sortByPreferences.equals(context.getResources().getString(
                R.string.preferences_language_en_value))) {
            // English.
            return "en";
        } else if (sortByPreferences.equals(context.getResources().getString(
                R.string.preferences_language_es_value))) {
            // Spanish.
            return "es";
        } else {
            // Default value: system language.
            return Locale.getDefault().getLanguage();
        }
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
