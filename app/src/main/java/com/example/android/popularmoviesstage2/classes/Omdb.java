package com.example.android.popularmoviesstage2.classes;

import android.net.Uri;
import android.util.Log;

import com.example.android.popularmoviesstage2.BuildConfig;
import com.example.android.popularmoviesstage2.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Public class with static resources for fetching data from OMDB.
 */
public class Omdb {
    private final static String TAG = Omdb.class.getSimpleName();

    // URLs.
    private final static String OMDB_BASE_URL = "http://www.omdbapi.com";

    // Parameters for using with urls.
    private final static String OMDB_PARAM_API_KEY = "apikey";
    private final static String OMDB_PARAM_MOVIE_ID = "i";

    // Rating sources from OMDB.
    private final static String OMDB_RATING_SOURCE_IMDB = "Internet Movie Database";
    private final static String OMDB_RATING_SOURCE_ROTTEN_TOMATOES = "Rotten Tomatoes";
    private final static String OMDB_RATING_SOURCE_METACRITIC = "Metacritic";

    // API KEY is defined into gradle.properties and referenced from app:build.gradle. The file
    // gradle.properties is included in the .gitignore file, so the API KEY will not be published
    // on GitHub.
    //
    // For more information:
    // https://richardroseblog.wordpress.com/2016/05/29/hiding-secret-api-keys-from-git/
    private final static String OMBD_API_KEY = BuildConfig.OMDB_API_KEY;

    /**
     * Fetches OMDB for information about a single movie.
     *
     * @param imdb is the IMDB code of the movie to search in the OMDB API.
     * @return an {@link OmdbMovie} object.
     */
    public static OmdbMovie getOmdbMovie(String imdb) {
        Log.i(TAG, "(getOmdbMovie) IMDB movie id: " + imdb);

        /* ------------ */
        /* Get the JSON */
        /* ------------ */

        // Build the uniform resource identifier (uri) for fetching data from OMDB API.
        Uri builtUri = Uri.parse(OMDB_BASE_URL).buildUpon()
                .appendQueryParameter(OMDB_PARAM_MOVIE_ID, imdb)
                .appendQueryParameter(OMDB_PARAM_API_KEY, OMBD_API_KEY)
                .build();

        // Use the built uri to get the JSON document with the results of the query.
        String JSONresponse;
        try {
            JSONresponse = NetworkUtils.getJSONresponse(builtUri);
        } catch (java.io.IOException e) {
            // If getJSONresponse has thrown an exception, exit returning null.
            Log.e(TAG, "(getOmdbMovie) Error retrieving JSON response: ", e);
            return null;
        }

        /* -------------- */
        /* Parse the JSON */
        /* -------------- */

        // If there is no data to parse, exit returning null.
        if (android.text.TextUtils.isEmpty(JSONresponse)) {
            return null;
        }

        // Try to parse the JSON response string. If there's a problem with the way the JSON is
        // formatted, a JSONException exception object will be thrown.
        try {
            // Create a JSONObject from the JSON response string.
            JSONObject resultsJSONResponse = new JSONObject(JSONresponse);

            // Extract the required values from the corresponding keys and create default values
            // for user scores.
            String rated = NetworkUtils.getStringFromJSON(resultsJSONResponse, "Rated");
            String awards = NetworkUtils.getStringFromJSON(resultsJSONResponse, "Awards");
            String dvd = NetworkUtils.getStringFromJSON(resultsJSONResponse, "DVD");
            Double imdb_vote_average = 0.0;
            Double rt_vote_average = 0.0;
            Double mc_vote_average = 0.0;

            // If there is a "Ratings" section, create a new JSONArray for parsing results.
            if (!resultsJSONResponse.isNull("Ratings")) {
                JSONArray ratingsArrayJSONResponse = resultsJSONResponse.getJSONArray("Ratings");
                JSONObject ratingsJSONResponse;
                for (int n = 0; n < ratingsArrayJSONResponse.length(); n++) {
                    // Get a single result at position n within the list of results.
                    ratingsJSONResponse = ratingsArrayJSONResponse.getJSONObject(n);

                    // Extract the required values from the corresponding keys.
                    String source = NetworkUtils.getStringFromJSON(ratingsJSONResponse, "Source");
                    String value = NetworkUtils.getStringFromJSON(ratingsJSONResponse, "Value");

                    // Parse values.
                    switch (source) {
                        case OMDB_RATING_SOURCE_IMDB: {
                            String arraySplit[] = value.split("/");
                            imdb_vote_average = Double.valueOf(arraySplit[0]);
                            break;
                        }
                        case OMDB_RATING_SOURCE_ROTTEN_TOMATOES: {
                            String arraySplit[] = value.split("%");
                            rt_vote_average = Double.valueOf(arraySplit[0]) / 10;
                            break;
                        }
                        case OMDB_RATING_SOURCE_METACRITIC: {
                            String arraySplit[] = value.split("/");
                            mc_vote_average = Double.valueOf(arraySplit[0]) / 10;
                            break;
                        }
                    }
                }
            }

            // Return a new {@link OmdbMovie} object with the data retrieved from the JSON response.
            return new OmdbMovie(rated, awards, dvd, imdb_vote_average, rt_vote_average, mc_vote_average);
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash.
            Log.e(TAG, "(getOmdbMovie) Error parsing the JSON response: ", e);
            return null;
        }
    }
}
