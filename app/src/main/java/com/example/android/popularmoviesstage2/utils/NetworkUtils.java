package com.example.android.popularmoviesstage2.utils;

/*
 * Created by David on 25/09/2017.
 */

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.android.popularmoviesstage2.BuildConfig;
import com.example.android.popularmoviesstage2.MainActivity;
import com.example.android.popularmoviesstage2.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public final class NetworkUtils {
    public final static String THUMBNAIL_IMAGE_URL = "https://image.tmdb.org/t/p/w185";
    public final static String FULL_IMAGE_URL = "https://image.tmdb.org/t/p/w500";
    public final static String SORT_ORDER_POPULAR = "popular";
    public final static String SORT_ORDER_TOP_RATED = "top_rated";

    private final static String TAG = MainActivity.class.getSimpleName();
    private final static String BASE_URL = "https://api.themoviedb.org/3/movie";
    private final static String PARAM_API_KEY = "api_key";
    // API KEY is defined into gradle.properties and referenced from app:build.gradle. The file
    // gradle.properties is included in the .gitignore file, so the API KEY will not be published
    // on GitHub.
    //
    // For more information: https://richardroseblog.wordpress.com/2016/05/29/hiding-secret-api-keys-from-git/
    private final static String API_KEY = BuildConfig.API_KEY;

    /**
     * Create a private constructor because no one should ever create a {@link NetworkUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name NetworkUtils (and an object instance of NetworkUtils is not
     * needed).
     */
    private NetworkUtils() {
    }

    /**
     * Creates an URL from {@link #BASE_URL} appending the sort order to the path and using the
     * {@link #API_KEY} for authentication.
     *
     * @param sortOrder is the sort order for the movie list.
     * @return an URL for fetching data from TheMovieDB using the given sort order.
     */
    public static URL buildURL(String sortOrder) {
        Log.i(TAG, "(buildURL) Sort order: " + sortOrder);

        // Build Uri from BASE_URL, sort order and API_KEY.
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(sortOrder)
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .build();

        // Build URL from Uri.
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, "(buildURL) Error building URL: " + e);
        }

        Log.i(TAG, "(buildURL) URL: " + url);
        return url;
    }

    /**
     * Fetches TheMovieDB for a list of movies.
     *
     * @param url is the URL at TheMovieDB.
     * @return an ArrayList of {@link Movie} objects.
     */
    public static ArrayList<Movie> fetchMovies(URL url) {
        Log.i(TAG, "(fetchMovies) URL: " + url);

        // Connect to TheMovieDB and get the JSON document with the results of the query.
        String JSONresponse = null;
        try {
            JSONresponse = getJSONresponse(url);
        } catch (java.io.IOException e) {
            Log.e(TAG, "(fetchMovies) Error retrieving JSON response: ", e);
        }

        // Parse JSON document into an array of {@link Movie} objects.
        return parseJSONresponse(JSONresponse);
    }

    /**
     * Retrieves a JSON document from the URL created previously in {@link #buildURL(String)}.
     *
     * @param url is the URL for retrieving the JSON document.
     * @return a String with the JSON document.
     * @throws java.io.IOException from url.openConnection().
     */
    private static String getJSONresponse(URL url) throws java.io.IOException {
        Log.i(TAG, "(getJSONresponse) URL: " + url);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    /**
     * Return a list of {@link Movie} objects that has been built up from parsing the given JSON
     * response.
     *
     * @param JSONresponse is the JSON object to be parsed and converted to a list of {@link Movie}
     *                     objects.
     * @return the list of {@link Movie} objects parsed form the input JSON object.
     */
    private static ArrayList<Movie> parseJSONresponse(String JSONresponse) {
        // If the JSON string is empty or null, then return null.
        if (TextUtils.isEmpty(JSONresponse)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding Movies to.
        ArrayList<Movie> movies = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON is
        // formatted, a JSONException exception object will be thrown. Catch the exception so the
        // app doesn't crash, and print the error message to the logs.
        try {
            // Create a JSONObject from the JSON response string.
            JSONObject baseJSONResponse = new JSONObject(JSONresponse);

            // Extract the JSONArray associated with the key called "results", which represents a
            // list of items (or Movies). If there's no "results" array, exit returning null before
            // trying to extract the JSONArray.
            if (baseJSONResponse.isNull("results")) return null;
            JSONArray moviesArray = baseJSONResponse.getJSONArray("results");

            // For each movie in the array, create an {@link Movie} object.
            JSONObject currentMovie;
            for (int i = 0; i < moviesArray.length(); i++) {
                // Get a single Movie at position i within the list of movies.
                currentMovie = moviesArray.getJSONObject(i);

                // Extract the value for the key called "title", which represents the title of the
                // movie.
                String title = "";
                if (!currentMovie.isNull("title")) title = currentMovie.getString("title");

                // Extract the value for the key called "poster_path", which represents the poster
                // path to append to {@link #IMAGE_URL} for getting the poster of the movie.
                String posterPath = "";
                if (!currentMovie.isNull("poster_path"))
                    posterPath = currentMovie.getString("poster_path");

                // Extract the value for the key called "overview", which represents the synopsis of 
                // the Movie.
                String overview = "";
                if (!currentMovie.isNull("overview")) overview = currentMovie.getString("overview");

                // Extract the value for the key called "vote_average", which represents the user
                // rating of the movie.
                Double voteAverage = 0.0;
                if (!currentMovie.isNull("vote_average"))
                    voteAverage = currentMovie.getDouble("vote_average");

                // Extract the value for the key called "release_date", which represents the
                // release date of the movie.
                String releaseDate = "";
                if (!currentMovie.isNull("release_date"))
                    releaseDate = currentMovie.getString("release_date");

                // Create a new {@link Movie} object with the data retrieved from the JSON response.
                Movie movie = new Movie(title, posterPath, overview, voteAverage, releaseDate, 0);

                // Add the new {@link Movie} to the list of movies.
                movies.add(movie);
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message with the
            // message from the exception.
            Log.e("QueryUtils", "Error parsing the JSON response: ", e);
        }

        // Return the list of movies.
        return movies;
    }
}
