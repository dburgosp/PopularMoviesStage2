package com.example.android.popularmoviesstage2.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import static android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences;

public class MyPreferences {
    private static final String TAG = MyPreferences.class.getSimpleName();

    public static final int TYPE_MOVIES_HOW = 0;
    public static final int TYPE_MOVIES_WHEN = 1;
    public static final int TYPE_MOVIES_WHERE = 2;

    public static final int MOVIES_NOW_PLAYING_HOW_THEATERS_INDEX = 0;
    public static final int MOVIES_NOW_PLAYING_HOW_DIGITAL_INDEX = 1;
    public static final int MOVIES_NOW_PLAYING_HOW_PHYSICAL_INDEX = 2;

    public static final int MOVIES_UPCOMING_HOW_THEATERS_INDEX = 0;
    public static final int MOVIES_UPCOMING_HOW_DIGITAL_INDEX = 1;
    public static final int MOVIES_UPCOMING_HOW_PHYSICAL_INDEX = 2;

    public static final int MOVIES_UPCOMING_WHEN_THIS_WEEK_INDEX = 0;
    public static final int MOVIES_UPCOMING_WHEN_NEXT_WEEK_INDEX = 1;
    public static final int MOVIES_UPCOMING_WHEN_ANY_DATE_INDEX = 2;

    public static final int MOVIES_UPCOMING_WHERE_THIS_REGION_INDEX = 0;
    public static final int MOVIES_UPCOMING_WHERE_ANY_REGION_INDEX = 1;

    /**
     * Empty constructor.
     */
    public MyPreferences() {

    }

    /**
     * Helper method to fetch SharedPreferences for the current sort order for movies lists.
     *
     * @param context is the context of the calling activity.
     * @return a string with the sort order for movies lists value stored in the preferences file.
     */
    public static String getMoviesSortOrder(Context context) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        Resources res = context.getResources();
        String sortBy = sharedPreferences.getString(
                res.getString(R.string.preferences_movies_sort_by_key),
                res.getStringArray(R.array.preferences_movies_sort_by_values_array)[0]);
        String order = sharedPreferences.getString(
                res.getString(R.string.preferences_order_key),
                res.getStringArray(R.array.preferences_order_values_array)[0]);
        String sortByOrder = sortBy + order;
        Log.i(TAG, "(getMoviesSortOrder) Current sort order: " + sortByOrder);
        return sortByOrder;
    }

    /**
     * Helper method to fetch SharedPreferences for the human-readable string value of the current
     * sort order for movies lists.
     *
     * @param context is the context of the calling activity.
     * @return a string with the current sort order for movies lists written in a human-readable
     * language.
     */
    public static String getMoviesSortOrderTitle(Context context) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        Resources res = context.getResources();

        // Get current "movies sort by" value index into the corresponding values array.
        ArrayList<String> sortByArrayList = new ArrayList<>(
                Arrays.asList(res.getStringArray(R.array.preferences_movies_sort_by_values_array)));
        String sortByValue = sharedPreferences.getString(
                res.getString(R.string.preferences_movies_sort_by_key),
                sortByArrayList.get(0));
        int sortByIndex = sortByArrayList.indexOf(sortByValue);

        // Get current "order" value index into the corresponding values array.
        ArrayList<String> orderArrayList = new ArrayList<>(
                Arrays.asList(res.getStringArray(R.array.preferences_order_values_array)));
        String orderValue = sharedPreferences.getString(
                res.getString(R.string.preferences_order_key),
                orderArrayList.get(0));
        int orderIndex = orderArrayList.indexOf(orderValue);

        // Get the complete sort order title.
        String title = "";
        switch (sortByIndex) {
            case 0: {
                // Sort by popularity.
                if (orderIndex == 0)
                    title = res.getString(
                            R.string.preferences_movies_sort_by_popularity_desc_title);
                else
                    title = res.getString(
                            R.string.preferences_movies_sort_by_popularity_asc_title);
                break;
            }
            case 1: {
                // Sort by original title.
                if (orderIndex == 0)
                    title = res.getString(
                            R.string.preferences_movies_sort_by_original_title_desc_title);
                else
                    title = res.getString(
                            R.string.preferences_movies_sort_by_original_title_asc_title);
                break;
            }
            case 2: {
                // Sort by vote average.
                if (orderIndex == 0)
                    title = res.getString(
                            R.string.preferences_movies_sort_by_vote_average_desc_title);
                else
                    title = res.getString(
                            R.string.preferences_movies_sort_by_vote_average_asc_title);
                break;
            }
            case 3: {
                // Sort by release date.
                if (orderIndex == 0)
                    title = res.getString(
                            R.string.preferences_movies_sort_by_primary_release_date_desc_title);
                else
                    title = res.getString(
                            R.string.preferences_movies_sort_by_primary_release_date_asc_title);
                break;
            }
            case 4: {
                // Sort by revenue.
                if (orderIndex == 0)
                    title = res.getString(
                            R.string.preferences_movies_sort_by_revenue_desc_title);
                else
                    title = res.getString(
                            R.string.preferences_movies_sort_by_revenue_asc_title);
                break;
            }
        }
        Log.i(TAG, "(getMoviesSortOrderTitle) Current sort order title: " + title);
        return title;
    }

    /**
     * Helper method to fetch SharedPreferences for the current language.
     *
     * @param context is the context of the calling activity.
     * @return a string with the ISO 639-1 code of the language stored in the preferences file.
     */
    public static String getIsoLanguage(Context context) {
        // Get current language value.
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        Resources res = context.getResources();
        String isoLanguage = sharedPreferences.getString(
                res.getString(R.string.preferences_language_key),
                res.getStringArray(R.array.preferences_language_values_array)[0]);

        if (isoLanguage.equals(res.getStringArray(R.array.preferences_language_values_array)[0])) {
            // System language.
            isoLanguage = Locale.getDefault().getLanguage();
        } else if (isoLanguage.equals(
                res.getStringArray(R.array.preferences_language_values_array)[1])) {
            // No language.
            isoLanguage = "";
        }
        Log.i(TAG, "(getIsoLanguage) ISO code for current selected language: " + isoLanguage);
        return isoLanguage;
    }

    /**
     * Helper method to fetch SharedPreferences for the current certification level for movies.
     *
     * @param context is the context of the calling activity.
     * @return a string with the ISO 639-1 code of the language stored in the preferences file.
     */
    public static String getMoviesCertification(Context context) {
        String certification;

        // Get current certification values array depending on the current region.
        String region = getIsoRegion(context);
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        Resources res = context.getResources();
        int certificationValuesArrayResId = res.getIdentifier(
                "preferences_certification_" + region + "_values_array", "string",
                context.getPackageName());

        if (certificationValuesArrayResId > 0) {
            // Get current certification level value for current region, if available (highest value
            // by default, so there's no certification restriction).
            ArrayList<String> certificationValuesArray = new ArrayList<>(
                    Arrays.asList(res.getStringArray(certificationValuesArrayResId)));
            certification = sharedPreferences.getString(
                    res.getString(R.string.preferences_certification_key),
                    certificationValuesArray.get(certificationValuesArray.size() - 1));
            Log.i(TAG, "(getMoviesCertification) Certification level for current region (" +
                    region + "): " + certification);
        } else {
            // No certification for movies at the current country.
            certification = "";
            Log.i(TAG, "(getMoviesCertification) No certificationc for current region (" +
                    region + ")");
        }
        return certification;
    }

    /**
     * Helper method to fetch SharedPreferences for the current release type filter for upcoming
     * movies.
     *
     * @param context is the context of the calling activity.
     * @return a string with the current release type filter for upcoming movies stored in the
     * preferences file.
     */
    public static String getUpcomingMoviesReleaseType(Context context) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        Resources res = context.getResources();
        String upcomingMoviesReleaseType = sharedPreferences.getString(
                res.getString(R.string.preferences_upcoming_movies_how_key),
                res.getStringArray(R.array.preferences_upcoming_movies_how_values_array)[0]);
        Log.i(TAG, "(getUpcomingMoviesReleaseType) Release type for upcoming movies: " +
                upcomingMoviesReleaseType);
        return upcomingMoviesReleaseType;
    }

    /**
     * Helper method to fetch SharedPreferences for the current release type filter for now playing
     * movies.
     *
     * @param context is the context of the calling activity.
     * @return a string with the current release type filter for now playing movies stored in the
     * preferences file.
     */
    public static String getNowPlayingMoviesReleaseType(Context context) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        Resources res = context.getResources();
        String nowPlayingMoviesReleaseType = sharedPreferences.getString(
                res.getString(R.string.preferences_now_playing_movies_how_key),
                res.getStringArray(R.array.preferences_now_playing_movies_how_values_array)[0]);
        Log.i(TAG, "(getNowPlayingMoviesReleaseType) Release type for now playing movies: " +
                nowPlayingMoviesReleaseType);
        return nowPlayingMoviesReleaseType;
    }

    /**
     * Helper method to fetch SharedPreferences for the current region.
     *
     * @param context is the context of the calling activity.
     * @return a string with the ISO 3166-1 code of the region stored in the preferences file.
     */
    public static String getIsoRegion(Context context) {
        // Get current region value.
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        Resources res = context.getResources();
        String isoRegion = sharedPreferences.getString(
                res.getString(R.string.preferences_region_key),
                res.getStringArray(R.array.preferences_region_values_array)[0]);

        if (isoRegion.equals(res.getStringArray(R.array.preferences_region_values_array)[0])) {
            // System country.
            isoRegion = Locale.getDefault().getCountry();
        }
        Log.i(TAG, "(getIsoRegion) ISO code for current selected region: " + isoRegion);
        return isoRegion;
    }

    /**
     * Helper method to fetch SharedPreferences for the current movies vote average value.
     *
     * @param context is the context of the calling activity.
     * @return a Double with the current movies vote average value stored in the preferences file.
     */
    public static Double getMoviesVoteAverage(Context context) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        Resources res = context.getResources();
        Double voteAverage = Double.valueOf(sharedPreferences.getString(
                res.getString(R.string.preferences_movies_vote_average_key),
                res.getString(R.string.preferences_movies_vote_average_value)));
        Log.i(TAG, "(getMoviesVoteAverage) Movies vote average: " + voteAverage);
        return voteAverage;
    }

    /**
     * Helper method to fetch SharedPreferences for the current movies vote count value.
     *
     * @param context is the context of the calling activity.
     * @return an integer with the current movies vote count value stored in the preferences file.
     */
    public static int getMoviesVoteCount(Context context) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        Resources res = context.getResources();
        int voteCount = Integer.valueOf(sharedPreferences.getString(
                res.getString(R.string.preferences_movies_vote_count_key),
                res.getString(R.string.preferences_movies_vote_count_value)));
        Log.i(TAG, "(getMoviesVoteCount) Movies vote count: " + voteCount);
        return voteCount;
    }

    /**
     * Helper method to fetch SharedPreferences for the current initial value of the dates range for
     * filtering upcoming movies.
     *
     * @param context is the context of the calling activity.
     * @return a string with the current initial value of the dates range for filtering upcoming
     * movies.
     */
    public static String getUpcomingMoviesInitDate(Context context) {
        // Get current "Upcoming Movies When" value index into the corresponding values array.
        int index = getUpcomingMoviesWhenIndex(context);

        // Calculate the current date value from the selected item.
        String initDate;
        switch (index) {
            case 0: // This week. Init date is this week monday.
                initDate = DateTimeUtils.getStringWeekday(DateTimeUtils.getCurrentDate(),
                        DateTimeUtils.WEEK_DAY_MONDAY);
                break;

            case 1: // Next week. Init date is next week monday.
                initDate = DateTimeUtils.getStringWeekday(DateTimeUtils.getAddedDaysToDate(
                        DateTimeUtils.getCurrentDate(), 7), DateTimeUtils.WEEK_DAY_MONDAY);
                break;

            default: // case 2: any date. Init date is tomorrow.
                initDate = DateTimeUtils.getStringAddedDaysToDate(
                        DateTimeUtils.getCurrentDate(), 1);
        }

        Log.i(TAG, "(getUpcomingMoviesInitDate) Init date: " + initDate);
        return initDate;
    }

    /**
     * Helper method to fetch SharedPreferences for the current end value of the dates range for
     * filtering upcoming movies.
     *
     * @param context is the context of the calling activity.
     * @return a string with the current end value of the dates range for filtering upcoming
     * movies.
     */
    public static String getUpcomingMoviesEndDate(Context context) {
        // Get current "Upcoming Movies When" value index into the corresponding values array.
        int index = getUpcomingMoviesWhenIndex(context);

        // Calculate the current date value from the selected item.
        String endDate;
        switch (index) {
            case 0: // This week. End date value is this sunday.
                endDate = DateTimeUtils.getStringWeekday(DateTimeUtils.getCurrentDate(),
                        DateTimeUtils.WEEK_DAY_SUNDAY);
                break;

            case 1: // Next week. End date is next week sunday.
                endDate = DateTimeUtils.getStringWeekday(DateTimeUtils.getAddedDaysToDate(
                        DateTimeUtils.getCurrentDate(), 7), DateTimeUtils.WEEK_DAY_SUNDAY);
                break;

            default: // case 2: any date. End value for the date range is not set.
                endDate = "";
        }

        Log.i(TAG, "(getUpcomingMoviesEndDate) End date: " + endDate);
        return endDate;
    }

    /**
     * Helper method to update a value in the "Upcoming Movies How" preferences array.
     *
     * @param context is the context of the calling activity.
     * @param index   is the index of the element in the corresponding preferences array to be
     *                updated. Available values are: MOVIES_UPCOMING_HOW_THEATERS_INDEX,
     *                MOVIES_UPCOMING_HOW_DIGITAL_INDEX, MOVIES_UPCOMING_HOW_PHYSICAL_INDEX.
     */
    public static void setUpcomingMoviesHow(Context context, int index) {
        if (index == MOVIES_UPCOMING_HOW_THEATERS_INDEX || index == MOVIES_UPCOMING_HOW_DIGITAL_INDEX ||
                index == MOVIES_UPCOMING_HOW_PHYSICAL_INDEX) {
            SharedPreferences sharedPref = getDefaultSharedPreferences(context);
            Resources res = context.getResources();
            SharedPreferences.Editor editor = sharedPref.edit();
            String upcomingMoviesHowArray[] = res.getStringArray(
                    R.array.preferences_upcoming_movies_how_values_array);
            editor.putString(res.getString(R.string.preferences_upcoming_movies_how_key),
                    upcomingMoviesHowArray[index]);
            editor.apply();
        }
    }

    /**
     * Helper method to update a value in the "Upcoming Movies When" preferences array.
     *
     * @param context is the context of the calling activity.
     * @param index   is the index of the element in the corresponding preferences array to be
     *                updated. Available values are: MOVIES_UPCOMING_WHEN_THIS_WEEK_INDEX,
     *                MOVIES_UPCOMING_WHEN_NEXT_WEEK_INDEX, MOVIES_UPCOMING_WHEN_ANY_DATE_INDEX.
     */
    public static void setUpcomingMoviesWhen(Context context, int index) {
        if (index == MOVIES_UPCOMING_WHEN_THIS_WEEK_INDEX || index == MOVIES_UPCOMING_WHEN_NEXT_WEEK_INDEX ||
                index == MOVIES_UPCOMING_WHEN_ANY_DATE_INDEX) {
            SharedPreferences sharedPref = getDefaultSharedPreferences(context);
            Resources res = context.getResources();
            SharedPreferences.Editor editor = sharedPref.edit();
            String upcomingMoviesWhenArray[] = res.getStringArray(
                    R.array.preferences_upcoming_movies_when_values_array);
            editor.putString(res.getString(R.string.preferences_upcoming_movies_when_key),
                    upcomingMoviesWhenArray[index]);
            editor.apply();
        }
    }

    /**
     * Helper method to update a value in the "Upcoming Movies Where" preferences array.
     *
     * @param context is the context of the calling activity.
     * @param index   is the index of the element in the corresponding preferences array to be
     *                updated. Available values are: MOVIES_UPCOMING_WHERE_THIS_REGION_INDEX,
     *                MOVIES_UPCOMING_WHERE_ANY_REGION_INDEX.
     */
    public static void setUpcomingMoviesWhere(Context context, int index) {
        if (index == MOVIES_UPCOMING_WHERE_THIS_REGION_INDEX || index == MOVIES_UPCOMING_WHERE_ANY_REGION_INDEX) {
            SharedPreferences sharedPref = getDefaultSharedPreferences(context);
            Resources res = context.getResources();
            SharedPreferences.Editor editor = sharedPref.edit();
            String upcomingMoviesWhereArray[] = res.getStringArray(
                    R.array.preferences_upcoming_movies_where_values_array);
            editor.putString(res.getString(R.string.preferences_upcoming_movies_where_key),
                    upcomingMoviesWhereArray[index]);
            editor.apply();
        }
    }

    /**
     * Helper method to update the "Movies Vote Average" preference value.
     *
     * @param context     is the context of the calling activity.
     * @param voteAverage is the new value for the "Movies Vote Average" preference.
     */
    public static void setMoviesVoteAverage(Context context, int voteAverage) {
        SharedPreferences sharedPref = getDefaultSharedPreferences(context);
        Resources res = context.getResources();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(res.getString(R.string.preferences_movies_vote_average_key), voteAverage);
        editor.apply();
    }

    /**
     * Helper method to update the "Movies Vote Count" preference value.
     *
     * @param context   is the context of the calling activity.
     * @param voteCount is the new value for the "Movies Vote Count" preference.
     */
    public static void setMoviesVoteCount(Context context, int voteCount) {
        SharedPreferences sharedPref = getDefaultSharedPreferences(context);
        Resources res = context.getResources();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(res.getString(R.string.preferences_movies_vote_count_key), voteCount);
        editor.apply();
    }

    /**
     * Helper method to update a value in the "Now Playing Movies How" preferences array.
     *
     * @param context is the context of the calling activity.
     * @param index   is the index of the element in the corresponding preferences array to be
     *                updated. Available values are MOVIES_NOW_PLAYING_HOW_THEATERS_INDEX,
     *                MOVIES_NOW_PLAYING_HOW_DIGITAL_INDEX, MOVIES_NOW_PLAYING_HOW_PHYSICAL_INDEX.
     */
    public static void setNowPlayingMoviesHow(Context context, int index) {
        if (index == MOVIES_NOW_PLAYING_HOW_THEATERS_INDEX || index == MOVIES_NOW_PLAYING_HOW_DIGITAL_INDEX ||
                index == MOVIES_NOW_PLAYING_HOW_PHYSICAL_INDEX) {
            SharedPreferences sharedPref = getDefaultSharedPreferences(context);
            Resources res = context.getResources();
            SharedPreferences.Editor editor = sharedPref.edit();
            String nowPlayingMoviesHowArray[] = res.getStringArray(
                    R.array.preferences_now_playing_movies_how_values_array);
            editor.putString(res.getString(R.string.preferences_now_playing_movies_how_key),
                    nowPlayingMoviesHowArray[index]);
            editor.apply();
        }
    }

    /**
     * Helper method to update a value in the corresponding "Upcoming Movies" ("How", "When" or
     * "Where") preferences array.
     *
     * @param context is the context of the calling activity.
     * @param index   is the index of the element in the corresponding preferences array to be
     *                updated.
     * @param type    is the type of the preferences array to be used. Admitted values are
     *                TYPE_MOVIES_HOW, TYPE_MOVIES_WHEN, TYPE_MOVIES_WHERE.
     */
    public static void setUpcomingMovies(Context context, int index, int type) {
        SharedPreferences sharedPref = getDefaultSharedPreferences(context);
        Resources res = context.getResources();
        SharedPreferences.Editor editor = sharedPref.edit();

        boolean admittedType = true;
        switch (type) {
            case TYPE_MOVIES_HOW:
                String upcomingMoviesHowArray[] = res.getStringArray(
                        R.array.preferences_upcoming_movies_how_values_array);
                editor.putString(res.getString(R.string.preferences_upcoming_movies_how_key),
                        upcomingMoviesHowArray[index]);
                break;

            case TYPE_MOVIES_WHEN:
                String upcomingMoviesWhenArray[] = res.getStringArray(
                        R.array.preferences_upcoming_movies_when_values_array);
                editor.putString(res.getString(R.string.preferences_upcoming_movies_when_key),
                        upcomingMoviesWhenArray[index]);
                break;

            case TYPE_MOVIES_WHERE:
                String upcomingMoviesWhereArray[] = res.getStringArray(
                        R.array.preferences_upcoming_movies_where_values_array);
                editor.putString(res.getString(R.string.preferences_upcoming_movies_where_key),
                        upcomingMoviesWhereArray[index]);
                break;

            default:
                admittedType = false;
        }

        if (admittedType)
            editor.apply();
    }

    /**
     * Helper method to fetch SharedPreferences for the current array index of the "Now Playing
     * Movies How" value.
     *
     * @param context is the context of the calling activity.
     * @return an integer with the corresponding array index.
     */
    public static int getNowPlayingMoviesHowIndex(Context context) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        Resources res = context.getResources();

        // Store current preferences into an array, so we will be able to get easily the index of
        // a given value into the array.
        ArrayList<String> moviesHowArray = new ArrayList<>(Arrays.asList(res.getStringArray(
                R.array.preferences_now_playing_movies_how_values_array)));

        // Get current value.
        String moviesHowValue = sharedPreferences.getString(
                res.getString(R.string.preferences_now_playing_movies_how_key),
                res.getStringArray(R.array.preferences_now_playing_movies_how_values_array)[0]);
        Log.i(TAG, "(getNowPlayingMoviesHowIndex) Current value: " + moviesHowValue);

        // Get and return current value index into the preferences array.
        int moviesHowIndex = moviesHowArray.indexOf(moviesHowValue);
        Log.i(TAG, "(getNowPlayingMoviesHowIndex) Index for current value: " + moviesHowIndex);
        return moviesHowIndex;
    }

    /**
     * Helper method to fetch SharedPreferences for the current array index of the "Upcoming
     * Movies How" value.
     *
     * @param context is the context of the calling activity.
     * @return an integer with the corresponding array index.
     */
    public static int getUpcomingMoviesHowIndex(Context context) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        Resources res = context.getResources();

        // Store current preferences into an array, so we will be able to get easily the index of
        // a given value into the array.
        ArrayList<String> moviesHowArray = new ArrayList<>(Arrays.asList(res.getStringArray(
                R.array.preferences_upcoming_movies_how_values_array)));

        // Get current value.
        String moviesHowValue = sharedPreferences.getString(
                res.getString(R.string.preferences_upcoming_movies_how_key),
                res.getStringArray(R.array.preferences_upcoming_movies_how_values_array)[0]);
        Log.i(TAG, "(getUpcomingMoviesHowIndex) Current value: " + moviesHowValue);

        // Get and return current value index into the preferences array.
        int moviesHowIndex = moviesHowArray.indexOf(moviesHowValue);
        Log.i(TAG, "(getUpcomingMoviesHowIndex) Index for current value: " + moviesHowIndex);
        return moviesHowIndex;
    }

    /**
     * Private helper method for getting current "Upcoming Movies When" value index into the
     * corresponding values array.
     *
     * @param context is the context of the calling activity.
     * @return an integer with the corresponding array index.
     */
    private static int getUpcomingMoviesWhenIndex(Context context) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        Resources res = context.getResources();

        // Store current preferences into an array, so we will be able to get easily the index of
        // a given value into the array.
        ArrayList<String> moviesWhenArray = new ArrayList<>(Arrays.asList(res.getStringArray(
                R.array.preferences_upcoming_movies_when_values_array)));

        // Get current value.
        String value = sharedPreferences.getString(
                res.getString(R.string.preferences_upcoming_movies_when_key),
                res.getStringArray(R.array.preferences_upcoming_movies_when_values_array)[0]);
        Log.i(TAG, "(getUpcomingMoviesWhenIndex) Current value:" + value);

        // Get and return current value index into the preferences array.
        int index = moviesWhenArray.indexOf(value);
        Log.i(TAG, "(getUpcomingMoviesWhenIndex) Index for current value:" + index);
        return index;
    }

    /**
     * Helper method to fetch SharedPreferences for the current array index of the "Upcoming
     * Movies Where" value.
     *
     * @param context is the context of the calling activity.
     * @return an integer with the corresponding array index.
     */
    public static int getUpcomingMoviesWhereIndex(Context context) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        Resources res = context.getResources();

        // Store current preferences into an array, so we will be able to get easily the index of
        // a given value into the array.
        ArrayList<String> moviesWhereArray = new ArrayList<>(Arrays.asList(res.getStringArray(
                R.array.preferences_upcoming_movies_where_values_array)));

        // Get current value.
        String moviesWhereValue = sharedPreferences.getString(
                res.getString(R.string.preferences_upcoming_movies_where_key),
                res.getStringArray(R.array.preferences_upcoming_movies_where_values_array)[0]);
        Log.i(TAG, "(getNowPlayingMoviesWhereIndex) Current value: " + moviesWhereValue);

        // Get and return current value index into the preferences array.
        int moviesWhereIndex = moviesWhereArray.indexOf(moviesWhereValue);
        Log.i(TAG, "(getUpcomingMoviesWhereIndex) Index for current value: " +
                moviesWhereIndex);
        return moviesWhereIndex;
    }

    /**
     * Helper method to fetch SharedPreferences for the current array index of the "Now Playing
     * Movies How" title.
     *
     * @param context is the context of the calling activity.
     * @return a String with the corresponding array content.
     */
    public static String getNowPlayingMoviesHowTitle(Context context) {
        Resources res = context.getResources();
        int index = getNowPlayingMoviesHowIndex(context);
        String title =
                res.getStringArray(R.array.preferences_now_playing_movies_how_list_array)[index];
        Log.i(TAG, "(getNowPlayingMoviesHowTitle) Title for current value: " + title);
        return title;
    }

    /**
     * Helper method to fetch SharedPreferences for the current array index of the "Upcoming
     * Movies How" title.
     *
     * @param context is the context of the calling activity.
     * @return a String with the corresponding array content.
     */
    public static String getUpcomingMoviesHowTitle(Context context) {
        Resources res = context.getResources();
        int index = getUpcomingMoviesHowIndex(context);
        String title =
                res.getStringArray(R.array.preferences_upcoming_movies_how_list_array)[index];
        Log.i(TAG, "(getUpcomingMoviesHowTitle) Title for current value: " + title);
        return title;
    }

    /**
     * Helper method to fetch SharedPreferences for the current array index of the "Upcoming
     * Movies When" title.
     *
     * @param context is the context of the calling activity.
     * @return a String with the corresponding array content.
     */
    public static String getUpcomingMoviesWhenTitle(Context context) {
        Resources res = context.getResources();
        int index = getUpcomingMoviesWhenIndex(context);
        String title =
                res.getStringArray(R.array.preferences_upcoming_movies_when_list_array)[index];
        Log.i(TAG, "(getUpcomingMoviesWhenTitle) Title for current value: " + title);
        return title;
    }

    /**
     * Helper method to fetch SharedPreferences for the current array index of the "Upcoming
     * Movies Where" title.
     *
     * @param context is the context of the calling activity.
     * @return a String with the corresponding array content.
     */
    public static String getUpcomingMoviesWhereTitle(Context context) {
        Resources res = context.getResources();
        int index = getUpcomingMoviesWhereIndex(context);
        String title =
                res.getStringArray(R.array.preferences_upcoming_movies_where_list_array)[index];
        Log.i(TAG, "(getUpcomingMoviesWhereTitle) Title for current value: " + title);
        return title;
    }

    /**
     * Helper method to fetch SharedPreferences for the current array index of the "Upcoming Movies
     * How" value.
     *
     * @param context is the context of the calling activity.
     * @param type    is the type of the preferences array to be used. Admitted values are
     *                TYPE_MOVIES_HOW, TYPE_MOVIES_WHEN, TYPE_MOVIES_WHERE.
     * @return an integer with the corresponding array index.
     */
    public static int getUpcomingMoviesIndex(Context context, int type) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        Resources res = context.getResources();

        // Get current "Upcoming Movies" value index into the corresponding ("How", "When" or
        // "Where") values array.
        String value;
        int index;
        switch (type) {
            case TYPE_MOVIES_HOW: {
                ArrayList<String> moviesHowArray = new ArrayList<>(Arrays.asList(res.getStringArray(
                        R.array.preferences_upcoming_movies_how_values_array)));
                value = sharedPreferences.getString(
                        res.getString(R.string.preferences_upcoming_movies_how_key),
                        res.getStringArray(R.array.preferences_upcoming_movies_how_values_array)[0]);
                index = moviesHowArray.indexOf(value);
                break;
            }

            case TYPE_MOVIES_WHEN: {
                ArrayList<String> moviesWhenArray = new ArrayList<>(Arrays.asList(res.getStringArray(
                        R.array.preferences_upcoming_movies_when_values_array)));
                value = sharedPreferences.getString(
                        res.getString(R.string.preferences_upcoming_movies_when_key),
                        res.getStringArray(R.array.preferences_upcoming_movies_when_values_array)[0]);
                index = moviesWhenArray.indexOf(value);
                break;
            }

            default: { // case TYPE_MOVIES_WHERE:
                ArrayList<String> moviesWhereArray = new ArrayList<>(Arrays.asList(res.getStringArray(
                        R.array.preferences_upcoming_movies_where_values_array)));
                value = sharedPreferences.getString(
                        res.getString(R.string.preferences_upcoming_movies_where_key),
                        res.getStringArray(R.array.preferences_upcoming_movies_where_values_array)[0]);
                index = moviesWhereArray.indexOf(value);
                break;
            }
        }

        Log.i(TAG, "(getUpcomingMoviesIndex) Current value: " + value);
        Log.i(TAG, "(getUpcomingMoviesIndex) Index for current value: " + index);
        return index;
    }

    /**
     * Helper method for creating a new RadioButton for Preferences with a custom style.
     *
     * @param text  is the text to be shown in the RadioButton.
     * @param index is the index of the new RadioButton into the RadioGroup that contains it.
     * @return the newly created RadioButton.
     */
    public static RadioButton createRadioButton(Context context, String text, int index) {
        RadioButton radioButton = new RadioButton(context);
        radioButton.setId(index);
        radioButton.setPadding(6, 6, 6, 6);
        radioButton.setBackground(context.getDrawable(R.drawable.rounded_button));

        // Layout params for the new RadioButton.
        LinearLayout.LayoutParams params = new RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.MATCH_PARENT,
                RadioGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 6, 0, 0);
        radioButton.setLayoutParams(params);

        // Text for the new RadioButton.
        radioButton.setText(text);
        radioButton.setTextColor(context.getResources().getColor(R.color.colorWhite));
        radioButton.setTypeface(null, Typeface.BOLD);

        // Set colors for button and background when RadioButton is checked or unchecked.
        ColorStateList buttonColorStateList = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked}
                },
                new int[]{
                        context.getResources().getColor(R.color.colorBlueGreyPrimary),
                        context.getResources().getColor(R.color.colorAccent),
                }
        );
        radioButton.setButtonTintList(buttonColorStateList);
        ColorStateList backgroundColorStateList = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked}
                },
                new int[]{
                        context.getResources().getColor(R.color.colorAccent),
                        context.getResources().getColor(R.color.colorWhite),
                }
        );
        radioButton.setBackgroundTintList(backgroundColorStateList);

        return radioButton;
    }
}
