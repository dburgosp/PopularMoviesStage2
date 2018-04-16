package com.example.android.popularmoviesstage2.classes;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.android.popularmoviesstage2.BuildConfig;
import com.example.android.popularmoviesstage2.utils.DateTimeUtils;
import com.example.android.popularmoviesstage2.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

/**
 * Public class with static resources for fetching data from TMDB.
 */
public class Tmdb {
    private final static String TAG = Tmdb.class.getSimpleName();

    // URLs.
    private final static String TMDB_BASE_URL = "https://api.themoviedb.org/3";
    public final static String TMDB_POSTER_SIZE_W185_URL = "https://image.tmdb.org/t/p/w185";
    public final static String TMDB_POSTER_SIZE_W500_URL = "https://image.tmdb.org/t/p/w500";

    // Content type for fetching movies.
    public final static String TMDB_CONTENT_TYPE_ALL = "all";
    public final static String TMDB_CONTENT_TYPE_POPULAR = "popular";
    public final static String TMDB_CONTENT_TYPE_TOP_RATED = "top_rated";
    public final static String TMDB_CONTENT_TYPE_FAVORITES = "favorites";
    public final static String TMDB_CONTENT_TYPE_NOW_PLAYING = "now_playing";
    public final static String TMDB_CONTENT_TYPE_UPCOMING = "upcoming";
    public final static String TMDB_CONTENT_TYPE_THIS_WEEK_RELEASES = "this_week_releases";
    public final static String TMDB_CONTENT_TYPE_FOR_BUY_AND_RENT = "buy_and_rent";
    public final static String TMDB_CONTENT_TYPE_GENRES = "genre";
    public final static String TMDB_CONTENT_TYPE_KEYWORDS = "keyword";

    // Paths for appending to urls.
    private final static String TMDB_MOVIE_PATH = "movie";
    private final static String TMDB_PERSON_PATH = "person";
    private final static String TMDB_CREDITS_PATH = "credits";
    private final static String TMDB_REVIEWS_PATH = "reviews";
    private final static String TMDB_DISCOVER_PATH = "discover";
    private final static String TMDB_COLLECTION_PATH = "collection";
    private final static String TMDB_VIDEOS_PATH = "videos";
    private final static String TMDB_IMAGES_PATH = "images";

    // Parameters for using with urls.
    private final static String TMDB_PARAM_API_KEY = "api_key";
    private final static String TMDB_PARAM_PAGE = "page";
    private final static String TMDB_PARAM_APPEND_TO_RESPONSE = "append_to_response";
    private final static String TMDB_PARAM_LANGUAGE = "language";
    private final static String TMDB_PARAM_PRIMARY_RELEASE_DATE_LESS = "primary_release_date.lte";
    private final static String TMDB_PARAM_PRIMARY_RELEASE_DATE_GREATER = "primary_release_date.gte";
    private final static String TMDB_PARAM_RELEASE_TYPE = "with_release_type";
    private final static String TMDB_PARAM_REGION = "region";
    private final static String TMDB_PARAM_WITH_GENRES = "with_genres";
    private final static String TMDB_PARAM_WITH_KEYWORDS = "with_keywords";
    private final static String TMDB_PARAM_SORT_BY = "sort_by";
    private final static String TMDB_PARAM_CERTIFICATION_COUNTRY = "certification_country";
    private final static String TMDB_PARAM_CERTIFICATION = "certification.lte";
    private final static String TMDB_PARAM_VOTE_COUNT = "vote_count.gte";
    private final static String TMDB_PARAM_VOTE_AVERAGE = "vote_average.gte";

    // Values for parameters.
    public final static String TMDB_VALUE_POPULARITY_DESC = "popularity.desc";
    public final static String TMDB_VALUE_POPULARITY_ASC = "popularity.asc";
    public final static String TMDB_VALUE_PRIMARY_RELEASE_DATE_DESC = "primary_release_date.desc";
    public final static String TMDB_VALUE_PRIMARY_RELEASE_DATE_ASC = "primary_release_date.asc";
    public final static String TMDB_VALUE_VOTE_AVERAGE_DESC = "vote_average.desc";
    public final static String TMDB_VALUE_VOTE_AVERAGE_ASC = "vote_average.asc";

    // Values.
    public final static int TMDB_MAX_PAGES = 1000;
    public final static int TMDB_RESULTS_PER_PAGE = 20;

    // API KEY is defined into gradle.properties and referenced from app:build.gradle. The file
    // gradle.properties is included in the .gitignore file, so the API KEY will not be published
    // on GitHub.
    //
    // For more information:
    // https://richardroseblog.wordpress.com/2016/05/29/hiding-secret-api-keys-from-git/
    private final static String TMBD_API_KEY = BuildConfig.TMDB_API_KEY;

    /**
     * Create a private constructor because no one should ever create a {@link Tmdb} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name NetworkUtils (and an object instance of NetworkUtils is not
     * needed).
     */
    private Tmdb() {
    }

    /**
     * Fetches TMDB for a list of movies.
     *
     * @param contentType   is the content type that we are looking for.
     * @param currentPage   is the page number to fetch.
     * @param language      is the language of the results.
     * @param region        is the region for getting results.
     * @param values        is the list of possible values for the contentType parameter.
     * @param sortOrder     is the sort order for the list of results.
     * @param certification is the minimum age rating of the movies in the list for the current
     *                      country (region parameter).
     * @param vote_count    is the minimum number of users votes of the movies in the list.
     * @param vote_average  is the minimum users rating of the movies in the list.
     * @return an array of {@link TmdbMovie} objects.
     */
    public static ArrayList<TmdbMovie> getTmdbMovies(String contentType, int currentPage,
                                                     String language, String region,
                                                     ArrayList<Integer> values, String sortOrder,
                                                     String certification, int vote_count,
                                                     Double vote_average) {
        Log.i(TAG, "(getTmdbMovies) Content type: " + contentType + ". Page number: " +
                currentPage + ". Language: " + language + ". Region: " + region +
                ". Sort order: " + sortOrder);

        /* ------------ */
        /* Get the JSON */
        /* ------------ */

        // Build the uniform resource identifier (uri) for fetching data from TMDB API.
        Boolean builtInQuery = false;
        Uri.Builder builder = Uri.parse(TMDB_BASE_URL).buildUpon();
        switch (contentType) {
            case TMDB_CONTENT_TYPE_ALL: {
                // Get all movies.
                builder.appendPath(TMDB_DISCOVER_PATH)
                        .appendPath(TMDB_MOVIE_PATH)
                        .appendQueryParameter(TMDB_PARAM_SORT_BY, sortOrder);
                break;
            }
            case TMDB_CONTENT_TYPE_NOW_PLAYING: {
                // Get movies with release type 2 or 3 and with release date between 45 days ago and
                // today.
                String initDate = DateTimeUtils.getStringAddedDaysToDate(
                        DateTimeUtils.getCurrentDate(), -45);
                String currentDate = DateTimeUtils.getStringCurrentDate();
                String releaseTypes = TmdbRelease.TMDB_RELEASE_TYPE_THEATRICAL + "|" +
                        TmdbRelease.TMDB_RELEASE_TYPE_THEATRICAL_LIMITED;
                builder.appendPath(TMDB_DISCOVER_PATH)
                        .appendPath(TMDB_MOVIE_PATH)
                        .appendQueryParameter(TMDB_PARAM_PRIMARY_RELEASE_DATE_GREATER, initDate)
                        .appendQueryParameter(TMDB_PARAM_PRIMARY_RELEASE_DATE_LESS, currentDate)
                        .appendQueryParameter(TMDB_PARAM_RELEASE_TYPE, releaseTypes)
                        .appendQueryParameter(TMDB_PARAM_SORT_BY, sortOrder);
                break;
            }
            case TMDB_CONTENT_TYPE_THIS_WEEK_RELEASES: {
                // Get movies with release type 2 or 3 that are going to be released this week.
                String monday = DateTimeUtils.getStringWeekday(DateTimeUtils.getCurrentDate(),
                        DateTimeUtils.WEEK_DAY_MONDAY);
                String sunday = DateTimeUtils.getStringWeekday(DateTimeUtils.getCurrentDate(),
                        DateTimeUtils.WEEK_DAY_SUNDAY);
                String releaseTypes = TmdbRelease.TMDB_RELEASE_TYPE_THEATRICAL + "|" +
                        TmdbRelease.TMDB_RELEASE_TYPE_THEATRICAL_LIMITED;
                builder.appendPath(TMDB_DISCOVER_PATH)
                        .appendPath(TMDB_MOVIE_PATH)
                        .appendQueryParameter(TMDB_PARAM_PRIMARY_RELEASE_DATE_GREATER, monday)
                        .appendQueryParameter(TMDB_PARAM_PRIMARY_RELEASE_DATE_LESS, sunday)
                        .appendQueryParameter(TMDB_PARAM_RELEASE_TYPE, releaseTypes)
                        .appendQueryParameter(TMDB_PARAM_SORT_BY, sortOrder);
                break;
            }
            case TMDB_CONTENT_TYPE_UPCOMING: {
                // Get movies with release type 2 or 3 and release date greater than today.
                String initDate = DateTimeUtils.getStringAddedDaysToDate(
                        DateTimeUtils.getCurrentDate(), 1);
                String releaseTypes = TmdbRelease.TMDB_RELEASE_TYPE_THEATRICAL + "|" +
                        TmdbRelease.TMDB_RELEASE_TYPE_THEATRICAL_LIMITED;
                builder.appendPath(TMDB_DISCOVER_PATH)
                        .appendPath(TMDB_MOVIE_PATH)
                        .appendQueryParameter(TMDB_PARAM_PRIMARY_RELEASE_DATE_GREATER, initDate)
                        .appendQueryParameter(TMDB_PARAM_RELEASE_TYPE, releaseTypes)
                        .appendQueryParameter(TMDB_PARAM_SORT_BY, sortOrder);
                break;
            }
            case TMDB_CONTENT_TYPE_GENRES: {
                // Get movies with genres = list of comma-separated values.
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < values.size(); i++) {
                    stringBuilder.append(Integer.toString(values.get(i)));
                    if ((i + 1) < values.size())
                        stringBuilder.append(",");
                }
                builder.appendPath(TMDB_DISCOVER_PATH)
                        .appendPath(TMDB_MOVIE_PATH)
                        .appendQueryParameter(TMDB_PARAM_WITH_GENRES, stringBuilder.toString())
                        .appendQueryParameter(TMDB_PARAM_SORT_BY, sortOrder);
                break;
            }
            case TMDB_CONTENT_TYPE_KEYWORDS: {
                // Get movies with keywords = list of comma-separated values.
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < values.size(); i++) {
                    stringBuilder.append(Integer.toString(values.get(i)));
                    if ((i + 1) < values.size())
                        stringBuilder.append(",");
                }
                builder.appendPath(TMDB_DISCOVER_PATH)
                        .appendPath(TMDB_MOVIE_PATH)
                        .appendQueryParameter(TMDB_PARAM_WITH_KEYWORDS, stringBuilder.toString())
                        .appendQueryParameter(TMDB_PARAM_SORT_BY, sortOrder);
                break;
            }
            default: {
                // Sort by "popular" or "top_rated" means only appending the sort string as a path
                // to the url.
                builder.appendPath(TMDB_MOVIE_PATH)
                        .appendPath(contentType);
                builtInQuery = true;
                break;
            }
        }

        if (!builtInQuery) {
            // If the query is not a built-in-query (popular, top_rated...) add more parameters.
            if (!region.equals("") && !region.isEmpty() &&
                    !certification.equals("") && !certification.isEmpty()) {
                builder.appendQueryParameter(TMDB_PARAM_CERTIFICATION_COUNTRY, region);
                builder.appendQueryParameter(TMDB_PARAM_CERTIFICATION, certification);
            }
            if (vote_count > 0)
                builder.appendQueryParameter(TMDB_PARAM_VOTE_COUNT, Integer.toString(vote_count));
            if (vote_average > 0.0)
                builder.appendQueryParameter(TMDB_PARAM_VOTE_AVERAGE, Double.toString(vote_average));
        }

        // Common parts in all cases to build the Uri.
        builder.appendQueryParameter(TMDB_PARAM_API_KEY, TMBD_API_KEY);
        if (!language.equals("") && !language.isEmpty())
            builder.appendQueryParameter(TMDB_PARAM_LANGUAGE, language);
        if (!region.equals("") && !region.isEmpty())
            builder.appendQueryParameter(TMDB_PARAM_REGION, region);
        if (currentPage > 0)
            builder.appendQueryParameter(TMDB_PARAM_PAGE, Integer.toString(currentPage));

        // Finally, build the Uri.
        Uri builtUri = builder.build();

        // Use the built Uri to get the JSON document with the results of the query.
        String JSONresponse;
        try

        {
            JSONresponse = NetworkUtils.getJSONresponse(builtUri);
        } catch (
                java.io.IOException e)

        {
            // If getJSONresponse has thrown an exception, exit returning null.
            Log.e(TAG, "(getTmdbMovies) Error retrieving JSON response: ", e);
            return null;
        }

        /* -------------- */
        /* Parse the JSON */
        /* -------------- */

        // If the JSON string is empty or null, then return null.
        if (TextUtils.isEmpty(JSONresponse))

        {
            Log.i(TAG, "(getTmdbMovies) The JSON string is empty.");
            return null;
        }

        // Create an empty array of TmdbMovie objects to append data.
        ArrayList<TmdbMovie> movies = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON is
        // formatted, a JSONException exception object will be thrown.
        try

        {
            // Create a JSONObject from the JSON response string.
            JSONObject resultsJSONResponse = new JSONObject(JSONresponse);

            // If there is no "results" section exit returning null. Otherwise, create a new
            // JSONArray for parsing results.
            if (resultsJSONResponse.isNull("results")) {
                Log.i(TAG, "(getTmdbMovies) No \"results\" section in the JSON string.");
                return null;
            }

            // Get paging info.
            int page = NetworkUtils.getIntFromJSON(resultsJSONResponse, "page");
            int total_pages = NetworkUtils.getIntFromJSON(resultsJSONResponse, "total_pages");
            int total_results = NetworkUtils.getIntFromJSON(resultsJSONResponse, "total_results");

            // Get results array.
            JSONArray arrayJSONResponse = resultsJSONResponse.getJSONArray("results");
            JSONObject baseJSONResponse;
            for (int n = 0; n < arrayJSONResponse.length(); n++) {
                // Get a single result at position n within the list of results, extract the movie
                // info and append it to the movies array..
                baseJSONResponse = arrayJSONResponse.getJSONObject(n);
                TmdbMovie movie = getMovie(baseJSONResponse, n, page, total_pages, total_results);
                movies.add(movie);
            }
        } catch (
                JSONException e)

        {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash.
            Log.e(TAG, "(getTmdbMovies) Error parsing the JSON response: ", e);
        }

        // Return the movies array.
        return movies;
    }

    /**
     * Fetches TMDB for a list of persons.
     *
     * @param currentPage is the page number to fetch.
     * @param language    is the language of the results.
     * @return an array of {@link TmdbPerson} objects.
     */
    public static ArrayList<TmdbPerson> getTmdbPeople(int currentPage, String language) {
        Log.i(TAG, "(getTmdbPeople) Page number: " + currentPage + ". Language: " + language);

        /* ------------ */
        /* Get the JSON */
        /* ------------ */

        // Build the uniform resource identifier (uri) for fetching data from TMDB API.
        Uri.Builder builder = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendPath(TMDB_PERSON_PATH)
                .appendPath(TMDB_CONTENT_TYPE_POPULAR)
                .appendQueryParameter(TMDB_PARAM_API_KEY, TMBD_API_KEY);
        if (!language.equals("") && !language.isEmpty())
            builder.appendQueryParameter(TMDB_PARAM_LANGUAGE, language);
        if (currentPage > 0)
            builder.appendQueryParameter(TMDB_PARAM_PAGE, Integer.toString(currentPage));
        Uri builtUri = builder.build();

        // Use the built Uri to get the JSON document with the results of the query.
        String JSONresponse;
        try {
            JSONresponse = NetworkUtils.getJSONresponse(builtUri);
        } catch (java.io.IOException e) {
            // If getJSONresponse has thrown an exception, exit returning null.
            Log.e(TAG, "(getTmdbPeople) Error retrieving JSON response: ", e);
            return null;
        }

        /* -------------- */
        /* Parse the JSON */
        /* -------------- */

        // If the JSON string is empty or null, then return null.
        if (TextUtils.isEmpty(JSONresponse)) {
            Log.i(TAG, "(getTmdbPeople) The JSON string is empty.");
            return null;
        }

        // Create an empty array of TmdbPerson objects to append data.
        ArrayList<TmdbPerson> persons = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON is
        // formatted, a JSONException exception object will be thrown.
        try {
            // Create a JSONObject from the JSON response string.
            JSONObject resultsJSONResponse = new JSONObject(JSONresponse);

            // If there is no "results" section exit returning null. Otherwise, create a new
            // JSONArray for parsing results.
            if (resultsJSONResponse.isNull("results")) {
                Log.i(TAG, "(getTmdbPeople) No \"results\" section in the JSON string.");
                return null;
            }

            // Get paging info.
            int page = NetworkUtils.getIntFromJSON(resultsJSONResponse, "page");
            int total_pages = NetworkUtils.getIntFromJSON(resultsJSONResponse, "total_pages");
            int total_results = NetworkUtils.getIntFromJSON(resultsJSONResponse, "total_results");

            // Get results array.
            JSONArray arrayJSONResponse = resultsJSONResponse.getJSONArray("results");
            JSONObject baseJSONResponse;
            for (int n = 0; n < arrayJSONResponse.length(); n++) {
                // Get a single result at position n within the list of results, extract the person
                // info and append it to the persons array.
                baseJSONResponse = arrayJSONResponse.getJSONObject(n);
                TmdbPerson person = getPerson(baseJSONResponse, n, page, total_pages, total_results);
                persons.add(person);
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash.
            Log.e(TAG, "(getTmdbPeople) Error parsing the JSON response: ", e);
        }

        // Return the persons array.
        return persons;
    }

    /**
     * Fetches TMDB for the list of movies of a given collection.
     *
     * @param collectionId is the unique identifier of the collection.
     * @param language     is the language of the results.
     * @param region       is the region for getting results.
     * @return a {@link TmdbMovieCollection} object.
     */
    public static TmdbMovieCollection getTmdbCollection(int collectionId, String language,
                                                        String region) {
        Log.i(TAG, "(getTmdbCollection) Collection ID: " + collectionId + ". Language: " +
                language + ". Region: " + region);

        /* ------------ */
        /* Get the JSON */
        /* ------------ */

        // Build the uniform resource identifier (uri) for fetching data from TMDB API.
        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendPath(TMDB_COLLECTION_PATH)
                .appendPath(Integer.toString(collectionId))
                .appendQueryParameter(TMDB_PARAM_API_KEY, TMBD_API_KEY)
                .appendQueryParameter(TMDB_PARAM_LANGUAGE, language)
                .appendQueryParameter(TMDB_PARAM_REGION, region)
                .build();

        // Use the built uri to get the JSON document with the results of the query.
        String JSONresponse;
        try {
            JSONresponse = NetworkUtils.getJSONresponse(builtUri);
        } catch (java.io.IOException e) {
            // If getJSONresponse has thrown an exception, exit returning null.
            Log.e(TAG, "(getTmdbCollection) Error retrieving JSON response: ", e);
            return null;
        }

        /* -------------- */
        /* Parse the JSON */
        /* -------------- */

        // If the JSON string is empty or null, then return null.
        if (TextUtils.isEmpty(JSONresponse)) {
            Log.i(TAG, "(getTmdbCollection) The JSON string is empty.");
            return null;
        }

        // Create an empty TmdbMovieCollection object.
        TmdbMovieCollection collection = null;

        // Try to parse the JSON response string. If there's a problem with the way the JSON is
        // formatted, a JSONException exception object will be thrown.
        try {
            // Create a JSONObject from the JSON response string.
            JSONObject resultsJSONResponse = new JSONObject(JSONresponse);

            // If there is no "parts" section exit returning null. Otherwise, create a new
            // JSONArray for parsing results.
            if (resultsJSONResponse.isNull("parts")) {
                Log.i(TAG, "(TmdbMovieCollection) No \"parts\" section in the JSON string.");
                return null;
            }

            // Extract the required values for the corresponding keys.
            int id = NetworkUtils.getIntFromJSON(resultsJSONResponse, "id");
            String name = NetworkUtils.getStringFromJSON(resultsJSONResponse, "name");
            String overview = NetworkUtils.getStringFromJSON(resultsJSONResponse, "overview");
            String poster_path = NetworkUtils.getStringFromJSON(resultsJSONResponse, "poster_path");
            String backdrop_path = NetworkUtils.getStringFromJSON(resultsJSONResponse, "backdrop_path");

            // Get parts array.
            JSONArray partsJSONarray = resultsJSONResponse.getJSONArray("parts");
            JSONObject partJSONObject;
            ArrayList<TmdbMovie> parts = new ArrayList<>();
            for (int n = 0; n < partsJSONarray.length(); n++) {
                // Get a single result at position n within the list of results, extract the movie
                // info and append it to the movies array.
                partJSONObject = partsJSONarray.getJSONObject(n);
                TmdbMovie movie = getMovie(partJSONObject, n, 0, 0, 0);
                parts.add(movie);
            }

            // Create the TmdbMovieCollection object.
            collection = new TmdbMovieCollection(id, name, overview, poster_path, backdrop_path, parts);
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash.
            Log.e(TAG, "(TmdbMovieCollection) Error parsing the JSON response: " + e);
        }

        // Return the movies array.
        return collection;
    }

    /**
     * Private helper method to extract a {@link TmdbMovie} object from a JSON.
     *
     * @param baseJSONResponse is the JSONObject containing the movie info.
     * @param n                is the current index of the element into the movies array.
     * @param page             is the current page of the movie element.
     * @param total_pages      is the number of pages of the current query.
     * @param total_results    is the number of movies of the current query.
     * @return the {@link TmdbMovie} object parsed from the JSON.
     * @throws JSONException from getJSONArray and getJSONObject calls.
     */
    private static TmdbMovie getMovie(JSONObject baseJSONResponse, int n, int page, int total_pages,
                                      int total_results) throws JSONException {
        // Extract the required values for the corresponding keys.
        boolean adult = NetworkUtils.getBooleanFromJSON(baseJSONResponse, "adult");
        String backdrop_path = NetworkUtils.getStringFromJSON(baseJSONResponse, "backdrop_path");
        int id = NetworkUtils.getIntFromJSON(baseJSONResponse, "id");
        String original_language = NetworkUtils.getStringFromJSON(baseJSONResponse, "original_language");
        String original_title = NetworkUtils.getStringFromJSON(baseJSONResponse, "original_title");
        String overview = NetworkUtils.getStringFromJSON(baseJSONResponse, "overview");
        Double popularity = NetworkUtils.getDoubleFromJSON(baseJSONResponse, "popularity");
        String poster_path = NetworkUtils.getStringFromJSON(baseJSONResponse, "poster_path");
        String release_date = NetworkUtils.getStringFromJSON(baseJSONResponse, "release_date");
        String title = NetworkUtils.getStringFromJSON(baseJSONResponse, "title");
        Boolean video = NetworkUtils.getBooleanFromJSON(baseJSONResponse, "video");
        Double vote_average = NetworkUtils.getDoubleFromJSON(baseJSONResponse, "vote_average");
        int vote_count = NetworkUtils.getIntFromJSON(baseJSONResponse, "vote_count");

        // Extract the JSONArray associated with the key called "genres" or "genre_ids", which
        // represents the list of genres which the movie belongs to.
        ArrayList<TmdbMovieGenre> genres = new ArrayList<>();
        JSONArray genresArray = null;
        if (!baseJSONResponse.isNull("genres")) {
            // For each genre in the array, create an {@link TmdbMovieGenre} object.
            genresArray = baseJSONResponse.getJSONArray("genres");
            JSONObject currentGenre;
            for (int i = 0; i < genresArray.length(); i++) {
                // Get a single genre at position i within the list of genres.
                currentGenre = genresArray.getJSONObject(i);

                // Extract the required values for the corresponding keys.
                int genre_id = NetworkUtils.getIntFromJSON(currentGenre, "id");
                String genre_name = NetworkUtils.getStringFromJSON(currentGenre, "name");

                // Create a new {@link TmdbMovieGenre} object and add it to the array.
                TmdbMovieGenre genre = new TmdbMovieGenre(genre_id, genre_name);
                genres.add(genre);
            }
        } else if (!baseJSONResponse.isNull("genre_ids")) {
            // We have only an array of integer genre identifiers.
            genresArray = baseJSONResponse.getJSONArray("genre_ids");
            for (int i = 0; i < genresArray.length(); i++) {
                // Extract the int value of the genre identifier, which has no key.
                int genre_id = (int) genresArray.get(i);

                // Create a new {@link TmdbMovieGenre} object and add it to the array.
                TmdbMovieGenre genre = new TmdbMovieGenre(genre_id, "");
                genres.add(genre);
            }
        }

        // Return the {@link TmdbMovie} object parsed from the JSON.
        return new TmdbMovie(id, adult, backdrop_path, genres, original_language,
                original_title, overview, popularity, poster_path, release_date, title,
                video, vote_average, vote_count, n, page, total_pages, total_results);
    }

    /**
     * Private helper method to extract a {@link TmdbPerson} object from a JSON.
     *
     * @param baseJSONResponse is the JSONObject containing the person info.
     * @param n                is the current index of the element into the persons array.
     * @param page             is the current page of the person element.
     * @param total_pages      is the number of pages of the current query.
     * @param total_results    is the number of persons of the current query.
     * @return the {@link TmdbPerson} object parsed from the JSON.
     * @throws JSONException from getJSONArray and getJSONObject calls.
     */
    private static TmdbPerson getPerson(JSONObject baseJSONResponse, int n, int page, int total_pages,
                                        int total_results) {
        // Extract the required values for the corresponding keys.
        boolean adult = NetworkUtils.getBooleanFromJSON(baseJSONResponse, "adult");
        String profile_path = NetworkUtils.getStringFromJSON(baseJSONResponse, "profile_path");
        int id = NetworkUtils.getIntFromJSON(baseJSONResponse, "id");
        String name = NetworkUtils.getStringFromJSON(baseJSONResponse, "name");

        // Return the {@link TmdbPerson} object parsed from the JSON.
        return new TmdbPerson(id, name, profile_path);
    }

    /**
     * Private helper method to extract a {@link TmdbPerson} object from a JSON.
     *
     * @param baseJSONResponse is the JSONObject containing the movie info.
     * @return the {@link TmdbPerson} object parsed from the JSON.
     * @throws JSONException from getJSONArray and getJSONObject calls.
     */

    private static TmdbPerson getPerson(JSONObject baseJSONResponse) throws JSONException {
        // Extract the required values for the corresponding keys.
        boolean adult = NetworkUtils.getBooleanFromJSON(baseJSONResponse, "adult");
        String biography = NetworkUtils.getStringFromJSON(baseJSONResponse, "biography");
        String birthday = NetworkUtils.getStringFromJSON(baseJSONResponse, "birthday");
        String deathday = NetworkUtils.getStringFromJSON(baseJSONResponse, "deathday");
        int gender = NetworkUtils.getIntFromJSON(baseJSONResponse, "gender");
        String homepage = NetworkUtils.getStringFromJSON(baseJSONResponse, "homepage");
        int id = NetworkUtils.getIntFromJSON(baseJSONResponse, "id");
        String imdb_id = NetworkUtils.getStringFromJSON(baseJSONResponse, "imdb_id");
        String name = NetworkUtils.getStringFromJSON(baseJSONResponse, "name");
        String place_of_birth = NetworkUtils.getStringFromJSON(baseJSONResponse, "place_of_birth");
        Double popularity = NetworkUtils.getDoubleFromJSON(baseJSONResponse, "popularity");
        String profile_path = NetworkUtils.getStringFromJSON(baseJSONResponse, "profile_path");

        // Extract the JSONArray associated with the key called "also_known_as", which represents
        // the list of other possible names for the person.
        ArrayList<String> also_known_as = new ArrayList<>();
        if (!baseJSONResponse.isNull("also_known_as")) {
            JSONArray alsoKnownAsJsonArray = baseJSONResponse.getJSONArray("also_known_as");
            for (int i = 0; i < alsoKnownAsJsonArray.length(); i++) {
                // For each element in the array, get the String alias and add it to the alias array.
                String alsoKnownAs = alsoKnownAsJsonArray.getJSONObject(i).toString();
                also_known_as.add(alsoKnownAs);
            }
        }

        // Return the {@link TmdbPerson} object parsed from the JSON.
        return new TmdbPerson(adult, also_known_as, biography, birthday, deathday, gender, homepage,
                id, imdb_id, name, place_of_birth, popularity, profile_path);
    }

    /**
     * Fetches TMDB for detailed information about a single movie.
     *
     * @param movieId  is the identifier of the movie in TMDB.
     * @param language is the language of the results.
     * @return a {@link TmdbMovieDetails} object.
     */
    public static TmdbMovieDetails getTmdbMovieDetails(int movieId, String language) {
        Log.i(TAG, "(getTmdbMovieDetails) Movie ID: " + movieId + ". Language: " + language);

        /* ------------ */
        /* Get the JSON */
        /* ------------ */

        // Build the uniform resource identifier (uri) for fetching data from TMDB API.
        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendPath(TMDB_MOVIE_PATH)
                .appendPath(Integer.toString(movieId))
                .appendQueryParameter(TMDB_PARAM_API_KEY, TMBD_API_KEY)
                .appendQueryParameter(TMDB_PARAM_LANGUAGE, language)
                .appendQueryParameter(TMDB_PARAM_APPEND_TO_RESPONSE, "keywords,release_dates,external_ids,recommendations,similar")
                .build();

        // Use the built uri to get the JSON document with the results of the query.
        String JSONresponse;
        try {
            JSONresponse = NetworkUtils.getJSONresponse(builtUri);
        } catch (java.io.IOException e) {
            // If getJSONresponse has thrown an exception, exit returning null.
            Log.e(TAG, "(getTmdbMovieDetails) Error retrieving JSON response: ", e);
            return null;
        }

        /* -------------- */
        /* Parse the JSON */
        /* -------------- */

        // If the JSON string is empty or null, then return null.
        if (TextUtils.isEmpty(JSONresponse)) {
            Log.i(TAG, "(getTmdbMovieDetails) The JSON string is empty.");
            return null;
        }

        // Try to parse the JSON response string. If there's a problem with the way the JSON is
        // formatted, a JSONException exception object will be thrown. 
        try {
            // Create a JSONObject from the JSON response string.
            JSONObject baseJSONResponse = new JSONObject(JSONresponse);

            // Extract the required values for the corresponding keys.
            TmdbMovie movie = getMovie(baseJSONResponse, 0, 0, 0, 0);
            int budget = NetworkUtils.getIntFromJSON(baseJSONResponse, "budget");
            String homepage = NetworkUtils.getStringFromJSON(baseJSONResponse, "homepage");
            String imdb_id = NetworkUtils.getStringFromJSON(baseJSONResponse, "imdb_id");
            int revenue = NetworkUtils.getIntFromJSON(baseJSONResponse, "revenue");
            int runtime = NetworkUtils.getIntFromJSON(baseJSONResponse, "runtime");
            String status = NetworkUtils.getStringFromJSON(baseJSONResponse, "status");
            String tagline = NetworkUtils.getStringFromJSON(baseJSONResponse, "tagline");

            // Extract the value for the key called "belongs_to_collection", which represents an
            // optional JSON object with information about the collection which the current movie
            // belongs to.
            TmdbMovieCollection movieCollection = null;
            if (!baseJSONResponse.isNull("belongs_to_collection")) {
                JSONObject belongs_to_collection = baseJSONResponse.getJSONObject("belongs_to_collection");

                // Extract the required values for the corresponding keys.
                int belongs_to_collection_id =
                        NetworkUtils.getIntFromJSON(belongs_to_collection, "id");
                String belongs_to_collection_name =
                        NetworkUtils.getStringFromJSON(belongs_to_collection, "name");
                String belongs_to_collection_poster_path =
                        NetworkUtils.getStringFromJSON(belongs_to_collection, "poster_path");
                String belongs_to_collection_backdrop_path =
                        NetworkUtils.getStringFromJSON(belongs_to_collection, "backdrop_path");

                // Create the {@link TmdbMovieCollection} object with this information.
                movieCollection = new TmdbMovieCollection(belongs_to_collection_id,
                        belongs_to_collection_name, "", belongs_to_collection_poster_path,
                        belongs_to_collection_backdrop_path, null);
            }

            // Extract the JSONArray associated with the key called "production_companies",
            // which represents the list of companies participating in the production of the
            // movie.
            ArrayList<TmdbMovieCompany> production_companies = new ArrayList<>();
            if (!baseJSONResponse.isNull("production_companies")) {
                JSONArray productionCompaniesArray =
                        baseJSONResponse.getJSONArray("production_companies");

                // For each company in the array, create an {@link TmdbMovieCompany} object.
                JSONObject currentCompany;
                for (int i = 0; i < productionCompaniesArray.length(); i++) {
                    // Get a single company at position i within the list of companies.
                    currentCompany = productionCompaniesArray.getJSONObject(i);

                    // Extract the required values for the corresponding keys.
                    int company_id = NetworkUtils.getIntFromJSON(currentCompany, "id");
                    String company_name = NetworkUtils.getStringFromJSON(currentCompany, "name");

                    // Create a new {@link TmdbMovieCompany} object and add it to the array.
                    TmdbMovieCompany movieCompany = new TmdbMovieCompany("", "",
                            "", company_id, "", company_name,
                            null);
                    production_companies.add(movieCompany);
                }
            }

            // Extract the JSONArray associated with the key called "production_countries",
            // which represents the list of countries in which the movie has been produced.
            ArrayList<TmdbMovieCountry> production_countries = new ArrayList<>();
            if (!baseJSONResponse.isNull("production_countries")) {
                JSONArray productionCountriesArray =
                        baseJSONResponse.getJSONArray("production_countries");

                // For each country in the array, create an {@link TmdbMovieCountry} object.
                JSONObject currentCountry;
                for (int i = 0; i < productionCountriesArray.length(); i++) {
                    // Get a single country at position i within the list of countries.
                    currentCountry = productionCountriesArray.getJSONObject(i);

                    // Extract the required values for the corresponding keys.
                    String iso_3166_1 = NetworkUtils.getStringFromJSON(currentCountry, "iso_3166_1");
                    String country_name = NetworkUtils.getStringFromJSON(currentCountry, "name");

                    // Create a new {@link TmdbMovieCountry} object and add it to the array.
                    TmdbMovieCountry movieCountry = new TmdbMovieCountry(iso_3166_1, country_name);
                    production_countries.add(movieCountry);
                }
            }

            // Extract the JSONArray associated with the key called "spoken_languages", which
            // represents the list of languages spoken in the movie.
            ArrayList<TmdbMovieLanguage> spoken_languages = new ArrayList<>();
            if (!baseJSONResponse.isNull("spoken_languages")) {
                JSONArray spokenLanguagesesArray = baseJSONResponse.getJSONArray("spoken_languages");

                // For each language in the array, create an {@link TmdbMovieLanguage} object.
                JSONObject currentLanguage;
                for (int i = 0; i < spokenLanguagesesArray.length(); i++) {
                    // Get a single language at position i within the list of languages.
                    currentLanguage = spokenLanguagesesArray.getJSONObject(i);

                    // Extract the required values for the corresponding keys.
                    String iso_639_1 = NetworkUtils.getStringFromJSON(currentLanguage, "iso_639_1");
                    String language_name = NetworkUtils.getStringFromJSON(currentLanguage, "name");

                    // Create a new {@link TmdbMovieLanguage} object and add it to the array.
                    TmdbMovieLanguage movieLanguage = new TmdbMovieLanguage(iso_639_1, language_name);
                    spoken_languages.add(movieLanguage);
                }
            }

            // Extract release dates.
            TmdbRelease releases = null;
            if (!baseJSONResponse.isNull("release_dates")) {
                JSONObject releasesObject = baseJSONResponse.getJSONObject("release_dates");
                releases = getMovieReleases(releasesObject);
            }

            // Extract external links.
            TmdbExternalId externalIds = null;
            if (!baseJSONResponse.isNull("external_ids")) {
                JSONObject externalIdsObject = baseJSONResponse.getJSONObject("external_ids");
                externalIds = getExternalLinks(externalIdsObject);
            }

            // Extract keywords array.
            ArrayList<TmdbKeyword> keywords = null;
            if (!baseJSONResponse.isNull("keywords")) {
                JSONObject keywordsObject = baseJSONResponse.getJSONObject("keywords");
                keywords = getKeywords(keywordsObject);
            }

            // Extract similar movies array.
            ArrayList<TmdbMovie> similarMovies = null;
            if (!baseJSONResponse.isNull("similar")) {
                JSONObject moviesObject = baseJSONResponse.getJSONObject("similar");
                similarMovies = getMovies(moviesObject);
            }

            // Extract recommended movies array.
            ArrayList<TmdbMovie> recommendedMovies = null;
            if (!baseJSONResponse.isNull("recommendations")) {
                JSONObject moviesObject = baseJSONResponse.getJSONObject("recommendations");
                recommendedMovies = getMovies(moviesObject);
            }

            // Return a {@link TmdbMovieDetails} object with the data retrieved from the JSON
            // response.
            return new TmdbMovieDetails(movie, movieCollection, budget, homepage, imdb_id,
                    production_companies, production_countries, revenue, runtime, spoken_languages,
                    status, tagline, releases, externalIds, keywords, similarMovies,
                    recommendedMovies);
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash.
            Log.e(TAG, "(getTmdbMovieDetails) Error parsing the JSON response: ", e);
            return null;
        }
    }

    /**
     * Fetches TMDB for information about the cast and crew of a movie.
     *
     * @param movieId is the identifier of the movie in TMDB.
     * @return a {@link TmdbCastCrew} object.
     */
    public static TmdbCastCrew getTmdbCastAndCrew(int movieId) {
        Log.i(TAG, "(getTmdbCastAndCrew) Movie ID: " + movieId);

        /* ------------ */
        /* Get the JSON */
        /* ------------ */

        // Build the uniform resource identifier (uri) for fetching data from TMDB API.
        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendPath(TMDB_MOVIE_PATH)
                .appendPath(Integer.toString(movieId))
                .appendPath(TMDB_CREDITS_PATH)
                .appendQueryParameter(TMDB_PARAM_API_KEY, TMBD_API_KEY)
                .build();

        // Use the built uri to get the JSON document with the results of the query.
        String JSONresponse;
        try {
            JSONresponse = NetworkUtils.getJSONresponse(builtUri);
        } catch (java.io.IOException e) {
            // If getJSONresponse has thrown an exception, exit returning null.
            Log.e(TAG, "(getTmdbCastAndCrew) Error retrieving JSON response: ", e);
            return null;
        }

        /* -------------- */
        /* Parse the JSON */
        /* -------------- */

        // If the JSON string is empty or null, then return null.
        if (TextUtils.isEmpty(JSONresponse)) {
            return null;
        }

        // Create new empty objects for the TmdbCastCrew object to be returned.
        int movie_id = 0;
        ArrayList<TmdbCast> castArrayList = new ArrayList<>();
        ArrayList<TmdbCrew> crewArrayList = new ArrayList<>();
        TmdbCastCrew castCrew = new TmdbCastCrew(movie_id, castArrayList, crewArrayList);

        // Try to parse the JSON response string. If there's a problem with the way the JSON is
        // formatted, a JSONException exception object will be thrown. 
        try {
            // Create a JSONObject from the JSON response string.
            JSONObject resultsJSONResponse = new JSONObject(JSONresponse);

            // Extract the value for the key called "id", which represents the unique identifier of
            // the movie.
            movie_id = NetworkUtils.getIntFromJSON(resultsJSONResponse, "id");

            // If there is a "cast" section, create a new JSONArray for parsing results.
            if (!resultsJSONResponse.isNull("cast")) {
                JSONArray castArrayJSONResponse = resultsJSONResponse.getJSONArray("cast");
                JSONObject castJSONResponse;
                for (int n = 0; n < castArrayJSONResponse.length(); n++) {
                    // Get a single result at position n within the list of results.
                    castJSONResponse = castArrayJSONResponse.getJSONObject(n);

                    // Extract the required values from the corresponding keys.
                    String character = NetworkUtils.getStringFromJSON(castJSONResponse, "character");
                    int person_id = NetworkUtils.getIntFromJSON(castJSONResponse, "id");
                    String name = NetworkUtils.getStringFromJSON(castJSONResponse, "name");
                    String profile_path = NetworkUtils.getStringFromJSON(castJSONResponse, "profile_path");

                    // Create a new {@link TmdbCast} object with the data retrieved from the JSON
                    // response.
                    TmdbCast cast = new TmdbCast(character, person_id, name, profile_path);

                    // Add the new {@link TmdbCast} to the list of persons from the movie cast.
                    castArrayList.add(cast);
                }
            }

            // If there is a "crew" section, create a new JSONArray for parsing results.
            if (!resultsJSONResponse.isNull("crew")) {
                JSONArray crewArrayJSONResponse = resultsJSONResponse.getJSONArray("crew");
                JSONObject crewJSONResponse;
                for (int n = 0; n < crewArrayJSONResponse.length(); n++) {
                    // Get a single result at position n within the list of results.
                    crewJSONResponse = crewArrayJSONResponse.getJSONObject(n);

                    // Extract the required values from the corresponding keys.
                    String credit_id = NetworkUtils.getStringFromJSON(crewJSONResponse, "credit_id");
                    String department = NetworkUtils.getStringFromJSON(crewJSONResponse, "department");
                    int gender = NetworkUtils.getIntFromJSON(crewJSONResponse, "gender");
                    int person_id = NetworkUtils.getIntFromJSON(crewJSONResponse, "id");
                    String job = NetworkUtils.getStringFromJSON(crewJSONResponse, "job");
                    String name = NetworkUtils.getStringFromJSON(crewJSONResponse, "name");
                    String profile_path = NetworkUtils.getStringFromJSON(crewJSONResponse, "profile_path");

                    // Create a new {@link TmdbCrew} object with the data retrieved from the JSON
                    // response.
                    TmdbCrew crew = new TmdbCrew(credit_id, department, gender, person_id, job, name, profile_path);

                    // Add the new {@link TmdbMovie} to the list of persons from the movie crew.
                    crewArrayList.add(crew);
                }
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. 
            Log.e(TAG, "(getTmdbCastAndCrew) Error parsing the JSON response: ", e);
        }

        // Update the TmdbCastCrew object with the data retrieved from the JSON object.
        try {
            castCrew.setId(movie_id);
            castCrew.setCast(castArrayList);
            castCrew.setCrew(crewArrayList);
        } catch (NullPointerException e) {
            Log.e(TAG, "(getTmdbCastAndCrew) Error parsing the JSON response: ", e);
        }

        // Return the TmdbCastCrew object.
        return castCrew;
    }

    /**
     * Fetches TMDB for detailed information about a person.
     *
     * @param personId is the identifier of the person in TMDB.
     * @param language is the language of the results.
     * @return a {@link TmdbPersonDetails} object.
     */
    public static TmdbPersonDetails getTmdbPersonDetails(int personId, String language) {
        Log.i(TAG, "(getTmdbPersonDetails) Person ID: " + personId + ". Language: " + language);

        /* ------------ */
        /* Get the JSON */
        /* ------------ */

        // Build the uniform resource identifier (uri) for fetching data from TMDB API.
        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendPath(TMDB_PERSON_PATH)
                .appendPath(Integer.toString(personId))
                .appendQueryParameter(TMDB_PARAM_API_KEY, TMBD_API_KEY)
                .appendQueryParameter(TMDB_PARAM_LANGUAGE, language)
                .appendQueryParameter(TMDB_PARAM_APPEND_TO_RESPONSE, "external_ids,movie_credits,tv_credits,images,tagged_images")
                .build();

        // Use the built uri to get the JSON document with the results of the query.
        String JSONresponse;
        try {
            JSONresponse = NetworkUtils.getJSONresponse(builtUri);
        } catch (java.io.IOException e) {
            // If getJSONresponse has thrown an exception, exit returning null.
            Log.e(TAG, "(getTmdbPersonDetails) Error retrieving JSON response: ", e);
            return null;
        }

        /* -------------- */
        /* Parse the JSON */
        /* -------------- */

        // If the JSON string is empty or null, then return null.
        if (TextUtils.isEmpty(JSONresponse)) {
            Log.i(TAG, "(getTmdbPersonDetails) The JSON string is empty.");
            return null;
        }

        // Try to parse the JSON response string. If there's a problem with the way the JSON is
        // formatted, a JSONException exception object will be thrown.
        try {
            // Create a JSONObject from the JSON response string.
            JSONObject baseJSONResponse = new JSONObject(JSONresponse);

            // Extract the required values for the corresponding keys.
            TmdbPerson person = getPerson(baseJSONResponse);

            // Extract external links.
            TmdbExternalId externalIds = null;
            if (!baseJSONResponse.isNull("external_ids")) {
                JSONObject externalIdsObject = baseJSONResponse.getJSONObject("external_ids");
                externalIds = getExternalLinks(externalIdsObject);
            }

            // Return a {@link TmdbPersonDetails} object with the data retrieved from the JSON
            // response.
            return new TmdbPersonDetails(person, externalIds);
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash.
            Log.e(TAG, "(getTmdbPersonDetails) Error parsing the JSON response: ", e);
            return null;
        }
    }

    /**
     * Fetches TMDB for information about the list of user reviews of a movie.
     *
     * @param movieId     is the unique identifier of the movie in TMDB.
     * @param currentPage is the page for searching reviews of the movie.
     * @return an array of {@link TmdbReview} objects.
     */
    public static ArrayList<TmdbReview> getTmdbReviews(int movieId, String currentPage) {
        Log.i(TAG, "(getTmdbReviews) Movie ID " + movieId + ", page " + currentPage);

        /* ------------ */
        /* Get the JSON */
        /* ------------ */

        // Build the uniform resource identifier (uri) for fetching data from TMDB API.
        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendPath(TMDB_MOVIE_PATH)
                .appendPath(Integer.toString(movieId))
                .appendPath(TMDB_REVIEWS_PATH)
                .appendQueryParameter(TMDB_PARAM_API_KEY, TMBD_API_KEY)
                .appendQueryParameter(TMDB_PARAM_PAGE, currentPage)
                .build();

        // Use the built uri to get the JSON document with the results of the query.
        String JSONresponse;
        try {
            JSONresponse = NetworkUtils.getJSONresponse(builtUri);
        } catch (java.io.IOException e) {
            // If getJSONresponse has thrown an exception, exit returning null.
            Log.e(TAG, "(getTmdbReviews) Error retrieving JSON response: ", e);
            return null;
        }

        /* -------------- */
        /* Parse the JSON */
        /* -------------- */

        // If the JSON string is empty or null, then return null.
        if (TextUtils.isEmpty(JSONresponse)) {
            Log.i(TAG, "(parseGetReviewsListJSON) The JSON string is empty.");
            return null;
        }

        // Create an empty ArrayList that we can start adding Reviews to.
        ArrayList<TmdbReview> reviews = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON is
        // formatted, a JSONException exception object will be thrown. Catch the exception so the
        // app doesn't crash, and print the error message to the logs.
        try {
            // Create a JSONObject from the JSON response string.
            JSONObject resultsJSONResponse = new JSONObject(JSONresponse);

            // If there is no "results" section exit returning null. Otherwise, create a new
            // JSONArray for parsing results.
            if (resultsJSONResponse.isNull("results")) {
                Log.i(TAG, "(parseGetReviewsListJSON) No \"results\" section in the JSON string.");
                return null;
            }

            // Extract the value of the current page and the total number of pages from the base
            // JSON document.
            int page = NetworkUtils.getIntFromJSON(resultsJSONResponse, "page");
            int total_pages = NetworkUtils.getIntFromJSON(resultsJSONResponse, "total_pages");

            JSONArray arrayJSONResponse = resultsJSONResponse.getJSONArray("results");
            JSONObject baseJSONResponse;
            for (int n = 0; n < arrayJSONResponse.length(); n++) {
                // Get a single result at position n within the list of results.
                baseJSONResponse = arrayJSONResponse.getJSONObject(n);

                // Extract the required values for the corresponding keys.
                String id = NetworkUtils.getStringFromJSON(baseJSONResponse, "id");
                String author = NetworkUtils.getStringFromJSON(baseJSONResponse, "author");
                String content = NetworkUtils.getStringFromJSON(baseJSONResponse, "content");
                String url = NetworkUtils.getStringFromJSON(baseJSONResponse, "url");

                // Create a new {@link TmdbReview} object with the data retrieved from the JSON
                // response.
                TmdbReview review = new TmdbReview(id, author, content, url, "",
                        "", "", page, n, total_pages);

                // Add the new {@link TmdbReview} to the list of reviews.
                reviews.add(review);
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message with the
            // message from the exception.
            Log.e(TAG, "(parseGetReviewsListJSON) Error parsing the JSON response: ", e);
        }

        // Return the list of reviews.
        return reviews;
    }

    /**
     * Fetches TMDB for information about the external links (IMDB, Facebook, Instagram, Twitter)
     * related to a movie.
     *
     * @param externalIdsObject is the JSON object containing the external links information.
     * @return a {@link TmdbExternalId} object.
     */
    private static TmdbExternalId getExternalLinks(JSONObject externalIdsObject) {
        // Extract the required values for the corresponding keys.
        String imdb_id = NetworkUtils.getStringFromJSON(externalIdsObject, "imdb_id");
        String facebook_id = NetworkUtils.getStringFromJSON(externalIdsObject, "facebook_id");
        String instagram_id = NetworkUtils.getStringFromJSON(externalIdsObject, "instagram_id");
        String twitter_id = NetworkUtils.getStringFromJSON(externalIdsObject, "twitter_id");

        // Create and return the {@link TmdbExternalId} object with this information.
        return new TmdbExternalId(imdb_id, facebook_id, instagram_id, twitter_id);
    }

    /**
     * Fetches TMDB for information about the keywords related to a movie.
     *
     * @param keywordsJSONObject is the JSON object containing the keywords info.
     * @return an array of {@link TmdbKeyword} objects.
     */
    private static ArrayList<TmdbKeyword> getKeywords(JSONObject keywordsJSONObject) {
        // Create an empty array of TmdbKeyword objects to add data.
        ArrayList<TmdbKeyword> keywordArrayList = new ArrayList<>();

        // Try to parse the JSON object. If there's a problem with the way the JSON is
        // formatted, a JSONException exception object will be thrown.
        try {
            // If there is no "keywords" section exit returning null. Otherwise, create a new
            // JSONArray for parsing results.
            if (keywordsJSONObject.isNull("keywords")) {
                Log.i(TAG, "(getKeywords) No \"keywords\" section in the JSON string.");
                return null;
            }

            // Get keywords array.
            JSONArray keywordsJSONArray = keywordsJSONObject.getJSONArray("keywords");
            JSONObject jsonObject;
            for (int n = 0; n < keywordsJSONArray.length(); n++) {
                // Get a single result at position n within the list of results.
                jsonObject = keywordsJSONArray.getJSONObject(n);

                // Extract the required values for the corresponding keys.
                int id = NetworkUtils.getIntFromJSON(jsonObject, "id");
                String name = NetworkUtils.getStringFromJSON(jsonObject, "name");

                // Add the new keywords object to the array.
                TmdbKeyword keyword = new TmdbKeyword(id, name);
                keywordArrayList.add(keyword);
            }
        } catch (JSONException e) {
            Log.e(TAG, "(getKeywords) Error parsing JSON: " + e);
        }

        return keywordArrayList;
    }

    /**
     * Fetches TMDB for a list of similar or recommended movies.
     *
     * @param moviesJSONObject is the JSON object containing the list of movies.
     * @return an array of {@link TmdbMovie} objects.
     */
    private static ArrayList<TmdbMovie> getMovies(JSONObject moviesJSONObject) {
        // Create an empty array of TmdbMovie objects to add data.
        ArrayList<TmdbMovie> movieArrayList = new ArrayList<>();

        // Try to parse the JSON object. If there's a problem with the way the JSON is
        // formatted, a JSONException exception object will be thrown.
        try {
            // If there is no "results" section exit returning null. Otherwise, create a new
            // JSONArray for parsing results.
            if (moviesJSONObject.isNull("results")) {
                Log.i(TAG, "(getMovies) No \"results\" section in the JSON string.");
                return null;
            }

            // Get the movies array.
            JSONArray moviesJSONArray = moviesJSONObject.getJSONArray("results");
            JSONObject jsonObject;
            for (int n = 0; n < moviesJSONArray.length(); n++) {
                // Get a single result at position n within the list of results.
                jsonObject = moviesJSONArray.getJSONObject(n);

                // Extract the current movie and add it to the array.
                TmdbMovie movie = getMovie(jsonObject, 0, 0, 0, 0);
                movieArrayList.add(movie);
            }
        } catch (JSONException e) {
            Log.e(TAG, "(getMovies) Error parsing JSON: " + e);
        }

        return movieArrayList;
    }

    /**
     * Fetches TMDB for information about the release dates of a movie at the current country.
     *
     * @param resultsJSONObject is the JSON object containing the release dates array.
     * @return a {@link TmdbRelease} object.
     */
    private static TmdbRelease getMovieReleases(JSONObject resultsJSONObject) {
        TmdbRelease releases = null;
        boolean countryFound = false;

        // Try to parse the JSON response string. If there's a problem with the way the JSON is
        // formatted, a JSONException exception object will be thrown.
        try {
            // If there is no "results" section exit returning null. Otherwise, create a new
            // JSONArray for parsing results.
            if (resultsJSONObject.isNull("results")) {
                Log.i(TAG, "(getMovieReleases) No \"results\" section in the JSON string.");
                return null;
            }

            // Get results array.
            JSONArray resultsJSONArray = resultsJSONObject.getJSONArray("results");
            JSONObject currentResultJSONObject;
            String currentCountry = Locale.getDefault().getCountry();
            for (int n = 0; n < resultsJSONArray.length(); n++) {
                // Get a single result at position n within the list of results.
                currentResultJSONObject = resultsJSONArray.getJSONObject(n);

                // Create an empty array of TmdbReleaseDate objects to add data.
                ArrayList<TmdbReleaseDate> releaseDateArrayList = new ArrayList<>();

                // Extract only current country info.
                String iso_3166_1 = NetworkUtils.getStringFromJSON(currentResultJSONObject, "iso_3166_1");
                if (iso_3166_1.equals(currentCountry)) {
                    // Extract the JSONArray associated with the key called "release_dates", which
                    // represents the list of releases related to the movie.
                    if (!currentResultJSONObject.isNull("release_dates")) {
                        JSONArray releaseDatesArray = currentResultJSONObject.getJSONArray("release_dates");

                        // For each release date in the array, create an {@link TmdbReleaseDate} object.
                        JSONObject currentReleaseDatesJSONObject;
                        for (int i = 0; i < releaseDatesArray.length(); i++) {
                            // Get a single release date at position i within the list of release dates.
                            currentReleaseDatesJSONObject = releaseDatesArray.getJSONObject(i);

                            // Extract the required values for the corresponding keys.
                            String certification = NetworkUtils.getStringFromJSON(currentReleaseDatesJSONObject, "certification");
                            String iso_639_1 = NetworkUtils.getStringFromJSON(currentReleaseDatesJSONObject, "iso_639_1");
                            String note = NetworkUtils.getStringFromJSON(currentReleaseDatesJSONObject, "note");
                            String release_date = NetworkUtils.getStringFromJSON(currentReleaseDatesJSONObject, "release_date");
                            int type = NetworkUtils.getIntFromJSON(currentReleaseDatesJSONObject, "type");

                            // Create a new {@link TmdbReleaseDate} object and add it to the array.
                            TmdbReleaseDate releaseDate = new TmdbReleaseDate(certification, iso_639_1, note, release_date, type);
                            releaseDateArrayList.add(releaseDate);
                            countryFound = true;
                        }
                    }
                } else
                    countryFound = false;

                // Create a new {@link TmdbRelease} object with the data retrieved from the JSON
                // response, only if there is at least one release date related to the current
                // country. Previously, sort the {@link TmdbReleaseDate} array by date.
                if (countryFound) {
                    Collections.sort(releaseDateArrayList, new Comparator<TmdbReleaseDate>() {
                        @Override
                        public int compare(TmdbReleaseDate r1, TmdbReleaseDate r2) {
                            return r1.getRelease_date().compareTo(r2.getRelease_date());
                        }
                    });
                    releases = new TmdbRelease(iso_3166_1, releaseDateArrayList);
                }
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash.
            Log.e(TAG, "(getMovieReleases) Error parsing the JSON response: ", e);
        }

        // Return the releases object.
        return releases;
    }

    /**
     * Fetches TMDB for videos, posters and backdrops related to a movie.
     *
     * @param movieId is unique the identifier of the movie in TMDB.
     * @return a {@link TmdbMedia} object.
     */
    public static TmdbMedia getTmdbMedia(int movieId) {
        Log.i(TAG, "(getTmdbMedia) Movie ID " + movieId);

        /* ------------ */
        /* Get the JSON */
        /* ------------ */

        // Build the uniform resource identifier (uri) for fetching data from TMDB API.
        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendPath(TMDB_MOVIE_PATH)
                .appendPath(Integer.toString(movieId))
                .appendQueryParameter(TMDB_PARAM_API_KEY, TMBD_API_KEY)
                .appendQueryParameter(TMDB_PARAM_APPEND_TO_RESPONSE, "videos,images")
                .build();

        // Use the built uri to get the JSON document with the results of the query.
        String JSONresponse;
        try {
            JSONresponse = NetworkUtils.getJSONresponse(builtUri);
        } catch (java.io.IOException e) {
            // If getJSONresponse has thrown an exception, exit returning null.
            Log.e(TAG, "(getTmdbMedia) Error retrieving JSON response: ", e);
            return null;
        }

        /* -------------- */
        /* Parse the JSON */
        /* -------------- */

        // If the JSON string is empty or null, then return null.
        if (TextUtils.isEmpty(JSONresponse)) {
            Log.i(TAG, "(getTmdbMedia) The JSON string is empty.");
            return null;
        }

        // Try to parse the JSON response string. If there's a problem with the way the JSON is
        // formatted, a JSONException exception object will be thrown. Catch the exception so the
        // app doesn't crash, and print the error message to the logs.
        try {
            // Create a JSONObject from the JSON response string.
            JSONObject resultsJSONResponse = new JSONObject(JSONresponse);

            // If there is no "videos" or "images" section exit returning null. Otherwise, create a
            // new JSONArray for parsing results.
            if (resultsJSONResponse.isNull("videos") && resultsJSONResponse.isNull("images")) {
                Log.i(TAG, "(getTmdbMedia) No \"videos\" or \"images\" sections in the JSON string.");
                return null;
            }

            // Get the movie id.
            int movie_id = NetworkUtils.getIntFromJSON(resultsJSONResponse, "id");

            // Create an empty ArrayList that we can start adding Videos to, and extract the
            // "videos" array, if exists.
            ArrayList<TmdbVideo> tmdbVideos = new ArrayList<>();
            if (!resultsJSONResponse.isNull("videos")) {
                JSONObject videosSectionJSONResponse = resultsJSONResponse.getJSONObject("videos");
                if (!videosSectionJSONResponse.isNull("results")) {
                    JSONArray videosArrayJSONResponse = videosSectionJSONResponse.getJSONArray("results");
                    JSONObject videoJSONResponse;
                    for (int n = 0; n < videosArrayJSONResponse.length(); n++) {
                        // Get a single result at position n within the list of results.
                        videoJSONResponse = videosArrayJSONResponse.getJSONObject(n);

                        // Extract the required values for the corresponding keys.
                        String id = NetworkUtils.getStringFromJSON(videoJSONResponse, "id");
                        String iso_639_1 = NetworkUtils.getStringFromJSON(videoJSONResponse, "iso_639_1");
                        String iso_3166_1 = NetworkUtils.getStringFromJSON(videoJSONResponse, "iso_3166_1");
                        String key = NetworkUtils.getStringFromJSON(videoJSONResponse, "key");
                        String name = NetworkUtils.getStringFromJSON(videoJSONResponse, "name");
                        String site = NetworkUtils.getStringFromJSON(videoJSONResponse, "site");
                        int size = NetworkUtils.getIntFromJSON(videoJSONResponse, "size");
                        String type = NetworkUtils.getStringFromJSON(videoJSONResponse, "type");

                        // Create a new {@link TmdbMedia} object with the data retrieved from the JSON response.
                        TmdbVideo tmdbVideo = new TmdbVideo(id, iso_639_1, iso_3166_1, key, name, site, size, type);

                        // Add the new {@link TmdbMedia} to the list of tmdbMedia.
                        tmdbVideos.add(tmdbVideo);
                    }
                }
            }

            // Create two empty ArrayLists that we can start adding posters and backdrops Images
            // to, and extract the "images" array, if exists.
            ArrayList<TmdbImage> posters = new ArrayList<>();
            ArrayList<TmdbImage> backdrops = new ArrayList<>();
            if (!resultsJSONResponse.isNull("images")) {
                JSONObject imagesSectionJSONResponse = resultsJSONResponse.getJSONObject("images");

                // Extract posters information.
                if (!imagesSectionJSONResponse.isNull("posters")) {
                    posters = getImagesArrayList(imagesSectionJSONResponse, "posters");
                }
                // Extract backdrops information.
                if (!imagesSectionJSONResponse.isNull("backdrops")) {
                    backdrops = getImagesArrayList(imagesSectionJSONResponse, "backdrops");
                }
            }

            // Create and return the TmdbMedia object.
            return new TmdbMedia(movie_id, tmdbVideos, posters, backdrops);
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash.
            Log.e(TAG, "(getTmdbMedia) Error parsing the JSON response: ", e);
            return null;
        }
    }

    /**
     * Helper method to determine whether a sort order is allowed by the TMDB API or not.
     *
     * @param sortOrder is the sort order.
     * @return true if the sort order is allowed, false otherwise.
     */
    public static boolean isAllowedSortOrder(String sortOrder) {
        return sortOrder.equals(TMDB_CONTENT_TYPE_POPULAR) ||
                sortOrder.equals(TMDB_CONTENT_TYPE_TOP_RATED) ||
                sortOrder.equals(TMDB_CONTENT_TYPE_FAVORITES) ||
                sortOrder.equals(TMDB_CONTENT_TYPE_UPCOMING) ||
                sortOrder.equals(TMDB_CONTENT_TYPE_NOW_PLAYING) ||
                sortOrder.equals(TMDB_CONTENT_TYPE_THIS_WEEK_RELEASES) ||
                sortOrder.equals(TMDB_CONTENT_TYPE_GENRES) ||
                sortOrder.equals(TMDB_CONTENT_TYPE_KEYWORDS) ||
                sortOrder.equals(TMDB_CONTENT_TYPE_ALL);
    }

    /**
     * Private helper method to get an array list of TmdbImage objects from a JSON "image" object.
     *
     * @param resultsJSONResponse is the JSON object to be parsed.
     * @param key                 is the key tobe searched into the JSON object. Available values:
     *                            "posters" and "backdrops".
     * @return an array list of TmdbImage objects.
     */
    static private ArrayList<TmdbImage> getImagesArrayList(JSONObject resultsJSONResponse, String key) {
        // Create an empty array of TmdbImage elements.
        ArrayList<TmdbImage> tmdbImages = new ArrayList<>();

        try {
            JSONArray imagesArrayJSONResponse = resultsJSONResponse.getJSONArray(key);
            JSONObject imageJSONResponse;
            for (int n = 0; n < imagesArrayJSONResponse.length(); n++) {
                // Get a single result at position n within the list of results.
                imageJSONResponse = imagesArrayJSONResponse.getJSONObject(n);

                // Extract the required values for the corresponding keys.
                Double aspect_ratio = NetworkUtils.getDoubleFromJSON(imageJSONResponse, "aspect_ratio");
                String file_path = NetworkUtils.getStringFromJSON(imageJSONResponse, "file_path");
                int height = NetworkUtils.getIntFromJSON(imageJSONResponse, "height");
                String iso_639_1 = NetworkUtils.getStringFromJSON(imageJSONResponse, "iso_639_1");
                int vote_average = NetworkUtils.getIntFromJSON(imageJSONResponse, "vote_average");
                int vote_count = NetworkUtils.getIntFromJSON(imageJSONResponse, "vote_count");
                int width = NetworkUtils.getIntFromJSON(imageJSONResponse, "width");

                // Create a new {@link TmdbImage} object with the data retrieved from the JSON response.
                TmdbImage tmdbImage = new TmdbImage(aspect_ratio, file_path, height, iso_639_1,
                        vote_average, vote_count, width);

                // Add the new {@link TmdbMedia} to the list of media.
                tmdbImages.add(tmdbImage);
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message with the
            // message from the exception.
            Log.e(TAG, "(getTmdbMedia) Error parsing the JSON response: ", e);
        }

        // Return the previously created array. If the loop in the try/catch statement has produced
        // no result, this array will be empty.
        return tmdbImages;
    }
}
