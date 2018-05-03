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

    public static final int MOVIES_NOW_PLAYING_WHEN_45_DAYS_INDEX = 0;
    public static final int MOVIES_NOW_PLAYING_WHEN_ANY_DATE_INDEX = 1;

    public static final int MOVIES_UPCOMING_HOW_THEATERS_INDEX = 0;
    public static final int MOVIES_UPCOMING_HOW_DIGITAL_INDEX = 1;
    public static final int MOVIES_UPCOMING_HOW_PHYSICAL_INDEX = 2;

    public static final int MOVIES_UPCOMING_WHEN_THIS_WEEK_INDEX = 0;
    public static final int MOVIES_UPCOMING_WHEN_NEXT_WEEK_INDEX = 1;
    public static final int MOVIES_UPCOMING_WHEN_ANY_DATE_INDEX = 2;

    public static final int MOVIES_UPCOMING_WHERE_THIS_REGION_INDEX = 0;
    public static final int MOVIES_UPCOMING_WHERE_ANY_REGION_INDEX = 1;

    private static SharedPreferences sharedPreferences;
    private static Resources res;

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
        sharedPreferences = getDefaultSharedPreferences(context);
        res = context.getResources();
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
        sharedPreferences = getDefaultSharedPreferences(context);
        res = context.getResources();

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
        sharedPreferences = getDefaultSharedPreferences(context);
        res = context.getResources();
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
        sharedPreferences = getDefaultSharedPreferences(context);
        res = context.getResources();

        // Get current certification values array depending on the current region.
        String region = getIsoRegion(context);
        String certification;
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
        sharedPreferences = getDefaultSharedPreferences(context);
        res = context.getResources();
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
        sharedPreferences = getDefaultSharedPreferences(context);
        res = context.getResources();
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
        sharedPreferences = getDefaultSharedPreferences(context);
        res = context.getResources();
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
        sharedPreferences = getDefaultSharedPreferences(context);
        res = context.getResources();
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
        sharedPreferences = getDefaultSharedPreferences(context);
        res = context.getResources();
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
        int index = getUpcomingMoviesIndex(context, TYPE_MOVIES_WHEN);

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
        int index = getUpcomingMoviesIndex(context, TYPE_MOVIES_WHEN);

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
     * Helper method to update a value in the corresponding "Upcoming Movies" ("How", "When" or
     * "Where") preferences array.
     *
     * @param context is the context of the calling activity.
     * @param index   is the index of the element in the corresponding preferences array to be
     *                updated. Admitted values are: MOVIES_UPCOMING_HOW_THEATERS_INDEX,
     *                MOVIES_UPCOMING_HOW_DIGITAL_INDEX, MOVIES_UPCOMING_HOW_PHYSICAL_INDEX,
     *                MOVIES_UPCOMING_WHEN_THIS_WEEK_INDEX, MOVIES_UPCOMING_WHEN_NEXT_WEEK_INDEX,
     *                MOVIES_UPCOMING_WHEN_ANY_DATE_INDEX.
     * @param type    is the type of the preferences array to be used. Admitted values are
     *                TYPE_MOVIES_HOW, TYPE_MOVIES_WHEN.
     */
    public static void setUpcomingMovies(Context context, int index, int type) {
        sharedPreferences = getDefaultSharedPreferences(context);
        res = context.getResources();
        SharedPreferences.Editor editor = sharedPreferences.edit();

        boolean admittedType = true;
        switch (type) {
            case TYPE_MOVIES_HOW:
                if (index == MOVIES_UPCOMING_HOW_THEATERS_INDEX ||
                        index == MOVIES_UPCOMING_HOW_DIGITAL_INDEX ||
                        index == MOVIES_UPCOMING_HOW_PHYSICAL_INDEX) {
                    String upcomingMoviesHowArray[] = res.getStringArray(
                            R.array.preferences_upcoming_movies_how_values_array);
                    String upcomingMoviesHowValue = upcomingMoviesHowArray[index];
                    Log.i(TAG, "(setUpcomingMovies) \"How\" value: " + upcomingMoviesHowValue);
                    editor.putString(res.getString(R.string.preferences_upcoming_movies_how_key),
                            upcomingMoviesHowValue);
                } else
                    admittedType = false;
                break;

            case TYPE_MOVIES_WHEN:
                if (index == MOVIES_UPCOMING_WHEN_THIS_WEEK_INDEX ||
                        index == MOVIES_UPCOMING_WHEN_NEXT_WEEK_INDEX ||
                        index == MOVIES_UPCOMING_WHEN_ANY_DATE_INDEX) {
                    String upcomingMoviesWhenArray[] = res.getStringArray(
                            R.array.preferences_upcoming_movies_when_values_array);
                    String upcomingMoviesWhenValue = upcomingMoviesWhenArray[index];
                    Log.i(TAG, "(setUpcomingMovies) \"When\" value: " + upcomingMoviesWhenValue);
                    editor.putString(res.getString(R.string.preferences_upcoming_movies_when_key),
                            upcomingMoviesWhenValue);
                } else
                    admittedType = false;
                break;

            default:
                admittedType = false;
        }

        if (admittedType)
            editor.apply();
    }

    /**
     * Helper method to update the "Movies Vote Average" preference value.
     *
     * @param context     is the context of the calling activity.
     * @param voteAverage is the new value for the "Movies Vote Average" preference.
     */
    public static void setMoviesVoteAverage(Context context, int voteAverage) {
        SharedPreferences sharedPref = getDefaultSharedPreferences(context);
        res = context.getResources();
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
        res = context.getResources();
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
     *                MOVIES_NOW_PLAYING_HOW_DIGITAL_INDEX, MOVIES_NOW_PLAYING_WHEN_PHYSICAL_INDEX.
     */
    public static void setNowPlayingMovies(Context context, int index) {
        if (index == MOVIES_NOW_PLAYING_HOW_THEATERS_INDEX ||
                index == MOVIES_NOW_PLAYING_HOW_DIGITAL_INDEX ||
                index == MOVIES_NOW_PLAYING_HOW_PHYSICAL_INDEX) {
            SharedPreferences sharedPref = getDefaultSharedPreferences(context);
            res = context.getResources();
            SharedPreferences.Editor editor = sharedPref.edit();

            // Set explicit "How" information.
            String nowPlayingMoviesHowArray[] = res.getStringArray(
                    R.array.preferences_now_playing_movies_how_values_array);
            String nowPlayingMoviesHowValue = nowPlayingMoviesHowArray[index];
            Log.i(TAG, "(setNowPlayingMovies) Explicit \"How\" value: " +
                    nowPlayingMoviesHowValue);
            editor.putString(res.getString(R.string.preferences_now_playing_movies_how_key),
                    nowPlayingMoviesHowValue);

            // Depending on "How" information, set implicit "When" information.
            String nowPlayingMoviesWhenArray[] = res.getStringArray(
                    R.array.preferences_now_playing_movies_when_values_array);
            String nowPlayingMoviesWhenValue = "";
            switch (index) {
                case MOVIES_NOW_PLAYING_HOW_DIGITAL_INDEX:
                case MOVIES_NOW_PLAYING_HOW_PHYSICAL_INDEX:
                    // For now playing movies online and on DVD/Blu-ray, set "Any date" as "When"
                    // value. It means that we will look for movies with release date lower or equal
                    // than today.
                    nowPlayingMoviesWhenValue =
                            nowPlayingMoviesWhenArray[MOVIES_NOW_PLAYING_WHEN_ANY_DATE_INDEX];
                    break;

                case MOVIES_NOW_PLAYING_HOW_THEATERS_INDEX:
                    // For now playing movies on theaters, set "45 days" as "When" value. It means
                    // that we will look for movies with release date lower or equal than today and
                    // greater than 45 days ago.
                    nowPlayingMoviesWhenValue =
                            nowPlayingMoviesWhenArray[MOVIES_NOW_PLAYING_WHEN_45_DAYS_INDEX];
            }
            Log.i(TAG, "(setNowPlayingMovies) Implicit \"When\" value: " +
                    nowPlayingMoviesWhenValue);
            editor.putString(res.getString(R.string.preferences_now_playing_movies_when_key),
                    nowPlayingMoviesWhenValue);

            // Commit changes.
            editor.apply();
        }
    }

    /**
     * Helper method to fetch SharedPreferences for the current array index of the "Now Playing
     * Movies How" value.
     *
     * @param context is the context of the calling activity.
     * @param type    is the type of the preferences array to be used. Admitted values are
     *                TYPE_MOVIES_HOW, TYPE_MOVIES_WHEN.
     * @return an integer with the corresponding array index.
     */
    public static int getNowPlayingMoviesIndex(Context context, int type) {
        sharedPreferences = getDefaultSharedPreferences(context);
        res = context.getResources();

        // Get current "Now Playing Movies" value index into the corresponding ("How" or "When")
        // values array.
        String value = "";
        int index = 0;
        switch (type) {
            case TYPE_MOVIES_HOW: {
                index = getArrayIndex(R.array.preferences_now_playing_movies_how_values_array,
                        R.string.preferences_now_playing_movies_how_key);
                break;
            }

            case TYPE_MOVIES_WHEN: {
                index = getArrayIndex(R.array.preferences_now_playing_movies_when_values_array,
                        R.string.preferences_now_playing_movies_when_key);
                break;
            }
        }
        Log.i(TAG, "(getNowPlayingMoviesIndex) Index for current value: " + index);
        return index;
    }

    /**
     * Helper method to fetch SharedPreferences for the current array index of the "Upcoming Movies
     * How" or "Upcoming Movies When" value.
     *
     * @param context is the context of the calling activity.
     * @param type    is the type of the preferences array to be used. Admitted values are
     *                TYPE_MOVIES_HOW, TYPE_MOVIES_WHEN.
     * @return an integer with the corresponding array index.
     */
    public static int getUpcomingMoviesIndex(Context context, int type) {
        sharedPreferences = getDefaultSharedPreferences(context);
        res = context.getResources();

        // Get current "Upcoming Movies" value index into the corresponding ("How" or "When" values
        // array.
        String value = "";
        int index = 0;
        switch (type) {
            case TYPE_MOVIES_HOW: {
                index = getArrayIndex(R.array.preferences_upcoming_movies_how_values_array,
                        R.string.preferences_upcoming_movies_how_key);
                break;
            }

            case TYPE_MOVIES_WHEN: {
                index = getArrayIndex(R.array.preferences_upcoming_movies_when_values_array,
                        R.string.preferences_upcoming_movies_when_key);
                break;
            }
        }

        Log.i(TAG, "(getUpcomingMoviesIndex) Current value: " + value);
        Log.i(TAG, "(getUpcomingMoviesIndex) Index for current value: " + index);
        return index;
    }

    /**
     * Helper method to fetch SharedPreferences for the current array index of the "Now Playing
     * Movies How" title.
     *
     * @param context is the context of the calling activity.
     * @param type    is the type of the preferences array to be used. Admitted values are
     *                TYPE_MOVIES_HOW, TYPE_MOVIES_WHEN.
     * @return a String with the corresponding array content.
     */
    public static String getNowPlayingMoviesTitle(Context context, int type) {
        res = context.getResources();
        int index = getNowPlayingMoviesIndex(context, type);
        String title;

        switch (type) {
            case TYPE_MOVIES_HOW:
                title = res.getStringArray(
                        R.array.preferences_now_playing_movies_how_list_array)[index];
                break;

            case TYPE_MOVIES_WHEN:
                title = res.getStringArray(
                        R.array.preferences_now_playing_movies_when_list_array)[index];
                break;

            default:
                title = "";
        }

        Log.i(TAG, "(getNowPlayingMoviesTitle) Title for current value: " + title);
        return title;
    }

    /**
     * Helper method to fetch SharedPreferences for the current array index of the "Upcoming
     * Movies How" or "Upcoming Movies When" title.
     *
     * @param context is the context of the calling activity.
     * @param type    is the type of the preferences array to be used. Admitted values are
     *                TYPE_MOVIES_HOW, TYPE_MOVIES_WHEN.
     * @return a String with the corresponding array content.
     */
    public static String getUpcomingMoviesTitle(Context context, int type) {
        res = context.getResources();
        int index = getUpcomingMoviesIndex(context, type);
        String title;

        switch (type) {
            case TYPE_MOVIES_HOW:
                title = res.getStringArray(
                        R.array.preferences_upcoming_movies_how_list_array)[index];
                break;

            case TYPE_MOVIES_WHEN:
                title = res.getStringArray(
                        R.array.preferences_upcoming_movies_when_list_array)[index];
                break;

            default:
                title = "";
        }
        Log.i(TAG, "(getUpcomingMoviesTitle) Title for current value: " + title);
        return title;
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

    /**
     * Private helper method for getting the index of the current value of a SharedPreferences into
     * its corresponding array.
     *
     * @param resValuesArray is the resource number of the array in which the values of the
     *                       preference are defined.
     * @param resKey         is the resource number of the key for the current preferences.
     * @return the index of the current value of the preferences into the values array.
     */
    private static int getArrayIndex(int resValuesArray, int resKey) {
        // Store current preferences into an String array, so we will be able to get easily the
        // index of any given value into the array.
        ArrayList<String> array = new ArrayList<>(Arrays.asList(res.getStringArray(resValuesArray)));

        // Get current value.
        String value = sharedPreferences.getString(res.getString(resKey),
                res.getStringArray(resValuesArray)[0]);
        Log.i(TAG, "(getArrayIndex) Current value: " + value);

        // Return current value index into the preferences array.
        return array.indexOf(value);
    }
}
