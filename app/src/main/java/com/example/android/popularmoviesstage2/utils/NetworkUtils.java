package com.example.android.popularmoviesstage2.utils;

/*
 * Created by David on 25/09/2017.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.android.popularmoviesstage2.BuildConfig;
import com.example.android.popularmoviesstage2.classes.Cast;
import com.example.android.popularmoviesstage2.classes.CastCrew;
import com.example.android.popularmoviesstage2.classes.Crew;
import com.example.android.popularmoviesstage2.classes.Movie;
import com.example.android.popularmoviesstage2.classes.MovieCollection;
import com.example.android.popularmoviesstage2.classes.MovieCompany;
import com.example.android.popularmoviesstage2.classes.MovieCountry;
import com.example.android.popularmoviesstage2.classes.MovieGenre;
import com.example.android.popularmoviesstage2.classes.MovieLanguage;
import com.example.android.popularmoviesstage2.classes.Review;

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
    public final static String SORT_ORDER_FAVORITES = "favorites";
    public final static int OPERATION_GET_MOVIES_LIST = 1;
    public final static int OPERATION_GET_MOVIE_DETAILS = 2;

    private final static String TAG = NetworkUtils.class.getSimpleName();
    private final static String BASE_URL = "https://api.themoviedb.org/3";
    private final static String MOVIE_PATH = "movie";
    private final static String CREDITS_PATH = "credits";
    private final static String REVIEWS_PATH = "reviews";
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
     * Helper method to determine if there is an active network connection.
     *
     * @return true if we are connected to the internet, false otherwise.
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    /**
     * Creates an URL from {@link #BASE_URL} appending the sort order to the path and using the
     * {@link #API_KEY} for authentication.
     *
     * @param sortOrder is the sort order for the movie list.
     * @return an URL for fetching data from TheMovieDB using the given sort order.
     */
    public static URL buildFetchMoviesListURL(String sortOrder) {
        Log.i(TAG, "(buildFetchMoviesListURL) Sort order: " + sortOrder);

        // Build Uri from BASE_URL, sort order and API_KEY.
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(MOVIE_PATH)
                .appendPath(sortOrder)
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .build();

        // Build URL from Uri.
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, "(buildFetchMoviesListURL) Error building URL: " + e);
        }

        Log.i(TAG, "(buildFetchMoviesListURL) URL: " + url);
        return url;
    }

    /**
     * Creates an URL from {@link #BASE_URL} appending the movie id to the path and using the
     * {@link #API_KEY} for authentication.
     *
     * @param movieId is the identifier of the movie.
     * @return an URL for fetching information for a movie from TheMovieDB.
     */
    public static URL buildFetchMovieDetailsURL(int movieId) {
        Log.i(TAG, "(buildFetchMovieDetailsURL) Movie ID: " + movieId);

        // Build Uri from BASE_URL, movie ID and API_KEY.
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(MOVIE_PATH)
                .appendPath(Integer.toString(movieId))
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .build();

        // Build URL from Uri and return.
        return buildUrlFromUri(builtUri);
    }

    /**
     * Creates an URL from {@link #BASE_URL} appending the movie id and the CREDITS_PATH to
     * the base path and using the {@link #API_KEY} for authentication.
     *
     * @param movieId is the identifier of the movie.
     * @return an URL for fetching a movie cast & crew information from TheMovieDB.
     */
    public static URL buildFetchCastCrewListURL(int movieId) {
        Log.i(TAG, "(buildFetchCastCrewListURL) Movie ID: " + movieId);

        // Build Uri from BASE_URL, movie ID, CREDITS_PATH and API_KEY.
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(MOVIE_PATH)
                .appendPath(Integer.toString(movieId))
                .appendPath(CREDITS_PATH)
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .build();

        // Build URL from Uri.
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, "(buildFetchCastCrewListURL) Error building URL: " + e);
        }

        Log.i(TAG, "(buildFetchCastCrewListURL) URL: " + url);
        return url;
    }

    /**
     * Creates an URL from {@link #BASE_URL} appending the movie id and the REVIEWS_PATH to
     * the base path and using the {@link #API_KEY} for authentication.
     *
     * @param movieId is the identifier of the movie.
     * @return an URL for fetching a movie cast & crew information from TheMovieDB.
     */
    public static URL buildFetchReviewsListURL(int movieId) {
        Log.i(TAG, "(buildFetchReviewsListURL) Movie ID: " + movieId);

        // Build Uri from BASE_URL, sort order and API_KEY.
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(MOVIE_PATH)
                .appendPath(Integer.toString(movieId))
                .appendPath(REVIEWS_PATH)
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .build();

        // Build URL from Uri.
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, "(buildFetchReviewsListURL) Error building URL: " + e);
        }

        Log.i(TAG, "(buildFetchReviewsListURL) URL: " + url);
        return url;
    }

    /**
     * Private helper method for building a Url from a given Uri.
     *
     * @param builtUri is the given Uri.
     * @return the Url built from the given Uri.
     */
    private static URL buildUrlFromUri(Uri builtUri) {
        URL url = null;
        try {
            url = new URL(builtUri.toString());
            Log.i(TAG, "(buildUrlFromUri) Built URL: " + url);
        } catch (MalformedURLException e) {
            Log.e(TAG, "(buildUrlFromUri) Error building URL: " + e);
        }
        return url;
    }

    /**
     * Fetches TheMovieDB for information about movies.
     *
     * @param url       is the URL at TheMovieDB.
     * @param operation is the operation type (OPERATION_GET_MOVIES_LIST or
     *                  OPERATION_GET_MOVIE_DETAILS).
     * @return an ArrayList of {@link Movie} objects.
     */
    public static ArrayList<Movie> fetchMovies(URL url, int operation) {
        Log.i(TAG, "(fetchMovies) URL: " + url);

        // Connect to TheMovieDB and get the JSON document with the results of the query.
        String JSONresponse = null;
        try {
            JSONresponse = getJSONresponse(url);
        } catch (java.io.IOException e) {
            Log.e(TAG, "(fetchMovies) Error retrieving JSON response: ", e);
        }

        // Parse JSON document into an array of {@link Movie} objects.
        switch (operation) {
            case OPERATION_GET_MOVIES_LIST:
                return parseGetMoviesListJSON(JSONresponse);

            case OPERATION_GET_MOVIE_DETAILS:
                return parseGetMovieDetailsJSON(JSONresponse);

            default:
                Log.i(TAG, "(fetchMovies) Operation not supported.");
                return null;
        }
    }

    /**
     * Fetches TheMovieDB for information about cast & crew.
     *
     * @param url is the URL at TheMovieDB.
     * @return a {@link CastCrew} object.
     */
    public static CastCrew fetchCastCrew(URL url) {
        Log.i(TAG, "(fetchMovies) URL: " + url);

        // Connect to TheMovieDB and get the JSON document with the results of the query.
        String JSONresponse = null;
        try {
            JSONresponse = getJSONresponse(url);
        } catch (java.io.IOException e) {
            Log.e(TAG, "(fetchCastCrew) Error retrieving JSON response: ", e);
        }

        // Parse JSON document into an array of {@link CastCrew} objects.
        return parseGetCastCrewListJSON(JSONresponse);
    }

    /**
     * Fetches reviews about a movie from TheMovieDB.
     *
     * @param url is the URL at TheMovieDB.
     * @return an ArrayList of {@link Review} objects.
     */
    public static ArrayList<Review> fetchReviews(URL url) {
        Log.i(TAG, "(fetchReviews) URL: " + url);

        // Connect to TheMovieDB and get the JSON document with the results of the query.
        String JSONresponse = null;
        try {
            JSONresponse = getJSONresponse(url);
        } catch (java.io.IOException e) {
            Log.e(TAG, "(fetchReviews) Error retrieving JSON response: ", e);
        }

        // Parse JSON document into an array of {@link Movie} objects.
        return parseGetReviewsListJSON(JSONresponse);
    }

    /**
     * Retrieves a JSON document from the URL created previously in {@link #buildFetchMoviesListURL(String)}.
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
     * Return a list of {@link Movie} objects that has been built up from parsing the JSON response
     * given from a call to TMDB API returning a list of elements ("Get Popular", "Get Top Rated",
     * etc.). The information required at this point is very simple, so we only need here the poster
     * of the movie (for showing its thumbnail image in a list) and the id of the movie (for being
     * able to identify it and get detailed information when the thumbnail is clicked or touched).
     *
     * @param JSONresponse is the JSON object to be parsed and converted to a list of {@link Movie}
     *                     objects.
     * @return the list of {@link Movie} objects parsed form the input JSON object.
     */
    private static ArrayList<Movie> parseGetMoviesListJSON(String JSONresponse) {
        // If the JSON string is empty or null, then return null.
        if (TextUtils.isEmpty(JSONresponse)) {
            Log.i(TAG, "(parseGetMoviesListJSON) The JSON string is empty.");
            return null;
        }

        // Create an empty ArrayList that we can start adding Movies to. We will only add one
        // movie here to the array.
        ArrayList<Movie> movies = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON is
        // formatted, a JSONException exception object will be thrown. Catch the exception so the
        // app doesn't crash, and print the error message to the logs.
        try {
            // Create a JSONObject from the JSON response string.
            JSONObject resultsJSONResponse = new JSONObject(JSONresponse);

            // If there is no "results" section exit returning null. Otherwise, create a new
            // JSONArray for parsing results.
            if (resultsJSONResponse.isNull("results")) {
                Log.i(TAG, "(parseGetMoviesListJSON) No \"results\" section in the JSON string.");
                return null;
            }

            JSONArray arrayJSONResponse = resultsJSONResponse.getJSONArray("results");
            JSONObject baseJSONResponse;
            for (int n = 0; n < arrayJSONResponse.length(); n++) {
                // Get a single result at position n within the list of results.
                baseJSONResponse = arrayJSONResponse.getJSONObject(n);

                // Extract the required values for the corresponding keys.
                boolean adult = getBooleanFromJSON(baseJSONResponse, "adult");
                String backdrop_path = getStringFromJSON(baseJSONResponse, "backdrop_path");
                int id = getIntFromJSON(baseJSONResponse, "id");
                String original_language = getStringFromJSON(baseJSONResponse, "original_language");
                String original_title = getStringFromJSON(baseJSONResponse, "original_title");
                String overview = getStringFromJSON(baseJSONResponse, "overview");
                Double popularity = getDoubleFromJSON(baseJSONResponse, "popularity");
                String poster_path = getStringFromJSON(baseJSONResponse, "poster_path");
                String release_date = getStringFromJSON(baseJSONResponse, "release_date");
                String title = getStringFromJSON(baseJSONResponse, "title");
                Boolean video = getBooleanFromJSON(baseJSONResponse, "video");
                Double vote_average = getDoubleFromJSON(baseJSONResponse, "vote_average");
                int vote_count = getIntFromJSON(baseJSONResponse, "vote_count");

                // Extract the JSONArray associated with the key called "genres", which represents
                // the list of genres which the movie belongs to.
                ArrayList<MovieGenre> genres = new ArrayList<>();
                if (!baseJSONResponse.isNull("genres")) {
                    JSONArray genresArray = baseJSONResponse.getJSONArray("genres");

                    // For each genre in the array, create an {@link MovieGenre} object.
                    JSONObject currentGenre;
                    for (int i = 0; i < genresArray.length(); i++) {
                        // Get a single genre at position i within the list of genres.
                        currentGenre = genresArray.getJSONObject(i);

                        // Extract the required values for the corresponding keys.
                        int genre_id = getIntFromJSON(currentGenre, "id");
                        String genre_name = getStringFromJSON(currentGenre, "name");

                        // Create a new {@link MovieGenre} object and add it to the array.
                        MovieGenre movieGenre = new MovieGenre(genre_id, genre_name);
                        genres.add(movieGenre);
                    }
                }

                // Create a new {@link Movie} object with the data retrieved from the JSON response.
                Movie movie = new Movie(id, adult, backdrop_path, genres, original_language,
                        original_title, overview, popularity, poster_path, release_date, title,
                        video, vote_average, vote_count, 0);

                // Add the new {@link Movie} to the list of movies.
                movies.add(movie);
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message with the
            // message from the exception.
            Log.e(TAG, "(parseGetMoviesListJSON) Error parsing the JSON response: ", e);
        }

        // Return the list of movies.
        return movies;
    }

    /**
     * Return a {@link Movie} object built up from parsing the JSON response given from the "Get
     * Details" (GET /movie/{movie_id}) call to TMDB API.
     *
     * @param JSONresponse is the JSON object to be parsed and converted to a {@link Movie} object.
     * @return the {@link Movie} object with the detailed information about the movie.
     */
    private static ArrayList<Movie> parseGetMovieDetailsJSON(String JSONresponse) {
        // If the JSON string is empty or null, then return null.
        if (TextUtils.isEmpty(JSONresponse)) {
            Log.i(TAG, "(parseGetMovieDetailsJSON) The JSON string is empty.");
            return null;
        }

        // Create an empty ArrayList that we can start adding Movies to. We will only add one
        // movie here to the array.
        ArrayList<Movie> movies = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON is
        // formatted, a JSONException exception object will be thrown. Catch the exception so the
        // app doesn't crash, and print the error message to the logs.
        try {
            // Create a JSONObject from the JSON response string.
            JSONObject baseJSONResponse = new JSONObject(JSONresponse);

            // Extract the required values for the corresponding keys.
            boolean adult = getBooleanFromJSON(baseJSONResponse, "adult");
            String backdrop_path = getStringFromJSON(baseJSONResponse, "backdrop_path");
            int id = getIntFromJSON(baseJSONResponse, "id");
            String original_language = getStringFromJSON(baseJSONResponse, "original_language");
            String original_title = getStringFromJSON(baseJSONResponse, "original_title");
            String overview = getStringFromJSON(baseJSONResponse, "overview");
            Double popularity = getDoubleFromJSON(baseJSONResponse, "popularity");
            String poster_path = getStringFromJSON(baseJSONResponse, "poster_path");
            String release_date = getStringFromJSON(baseJSONResponse, "release_date");
            String title = getStringFromJSON(baseJSONResponse, "title");
            Boolean video = getBooleanFromJSON(baseJSONResponse, "video");
            Double vote_average = getDoubleFromJSON(baseJSONResponse, "vote_average");
            int vote_count = getIntFromJSON(baseJSONResponse, "vote_count");
            int budget = getIntFromJSON(baseJSONResponse, "budget");
            String homepage = getStringFromJSON(baseJSONResponse, "homepage");
            String imdb_id = getStringFromJSON(baseJSONResponse, "imdb_id");
            int revenue = getIntFromJSON(baseJSONResponse, "revenue");
            int runtime = getIntFromJSON(baseJSONResponse, "runtime");
            String status = getStringFromJSON(baseJSONResponse, "status");
            String tagline = getStringFromJSON(baseJSONResponse, "tagline");

            // Extract the JSONArray associated with the key called "genres", which represents
            // the list of genres which the movie belongs to.
            ArrayList<MovieGenre> genres = new ArrayList<>();
            if (!baseJSONResponse.isNull("genres")) {
                JSONArray genresArray = baseJSONResponse.getJSONArray("genres");

                // For each genre in the array, create an {@link MovieGenre} object.
                JSONObject currentGenre;
                for (int i = 0; i < genresArray.length(); i++) {
                    // Get a single genre at position i within the list of genres.
                    currentGenre = genresArray.getJSONObject(i);

                    // Extract the required values for the corresponding keys.
                    int genre_id = getIntFromJSON(currentGenre, "id");
                    String genre_name = getStringFromJSON(currentGenre, "name");

                    // Create a new {@link MovieGenre} object and add it to the array.
                    MovieGenre movieGenre = new MovieGenre(genre_id, genre_name);
                    genres.add(movieGenre);
                }
            }

            // Extract the value for the key called "belongs_to_collection", which represents an
            // optional JSON object with information about the collection which the current movie
            // belongs to.
            MovieCollection movieCollection = null;
            JSONObject belongs_to_collection = null;
            if (!baseJSONResponse.isNull("belongs_to_collection")) {
                belongs_to_collection = baseJSONResponse.getJSONObject("belongs_to_collection");

                // Extract the required values for the corresponding keys.
                int belongs_to_collection_id = getIntFromJSON(belongs_to_collection, "id");
                String belongs_to_collection_name = getStringFromJSON(belongs_to_collection, "name");
                String belongs_to_collection_poster_path = getStringFromJSON(belongs_to_collection, "poster_path");
                String belongs_to_collection_backdrop_path = getStringFromJSON(belongs_to_collection, "backdrop_path");

                // Create the {@link MovieCollection} object with this information.
                movieCollection = new MovieCollection(belongs_to_collection_id,
                        belongs_to_collection_name, "", belongs_to_collection_poster_path,
                        belongs_to_collection_backdrop_path, null);
            }

            // Extract the JSONArray associated with the key called "production_companies",
            // which represents the list of companies participating in the production of the
            // movie.
            ArrayList<MovieCompany> production_companies = new ArrayList<>();
            if (!baseJSONResponse.isNull("production_companies")) {
                JSONArray productionCompaniesArray = baseJSONResponse.getJSONArray("production_companies");

                // For each company in the array, create an {@link MovieCompany} object.
                JSONObject currentCompany;
                for (int i = 0; i < productionCompaniesArray.length(); i++) {
                    // Get a single company at position i within the list of companies.
                    currentCompany = productionCompaniesArray.getJSONObject(i);

                    // Extract the required values for the corresponding keys.
                    int company_id = getIntFromJSON(currentCompany, "id");
                    String company_name = getStringFromJSON(currentCompany, "name");

                    // Create a new {@link MovieCompany} object and add it to the array.
                    MovieCompany movieCompany = new MovieCompany("", "",
                            "", company_id, "", company_name,
                            null);
                    production_companies.add(movieCompany);
                }
            }

            // Extract the JSONArray associated with the key called "production_countries",
            // which represents the list of countries in which the movie has been produced.
            ArrayList<MovieCountry> production_countries = new ArrayList<>();
            if (!baseJSONResponse.isNull("production_countries")) {
                JSONArray productionCountriesArray = baseJSONResponse.getJSONArray("production_countries");

                // For each country in the array, create an {@link MovieCountry} object.
                JSONObject currentCountry;
                for (int i = 0; i < productionCountriesArray.length(); i++) {
                    // Get a single country at position i within the list of countries.
                    currentCountry = productionCountriesArray.getJSONObject(i);

                    // Extract the required values for the corresponding keys.
                    String iso_3166_1 = getStringFromJSON(currentCountry, "iso_3166_1");
                    String country_name = getStringFromJSON(currentCountry, "name");

                    // Create a new {@link MovieCountry} object and add it to the array.
                    MovieCountry movieCountry = new MovieCountry(iso_3166_1, country_name);
                    production_countries.add(movieCountry);
                }
            }

            // Extract the JSONArray associated with the key called "spoken_languages", which
            // represents the list of languages spoken in the movie.
            ArrayList<MovieLanguage> spoken_languages = new ArrayList<>();
            if (!baseJSONResponse.isNull("spoken_languages")) {
                JSONArray spokenLanguagesesArray = baseJSONResponse.getJSONArray("spoken_languages");

                // For each language in the array, create an {@link MovieLanguage} object.
                JSONObject currentLanguage;
                for (int i = 0; i < spokenLanguagesesArray.length(); i++) {
                    // Get a single language at position i within the list of languages.
                    currentLanguage = spokenLanguagesesArray.getJSONObject(i);

                    // Extract the required values for the corresponding keys.
                    String iso_639_1 = getStringFromJSON(currentLanguage, "iso_639_1");
                    String language_name = getStringFromJSON(currentLanguage, "name");

                    // Create a new {@link MovieLanguage} object and add it to the array.
                    MovieLanguage movieLanguage = new MovieLanguage(iso_639_1, language_name);
                    spoken_languages.add(movieLanguage);
                }
            }

            // Create a new {@link Movie} object with the data retrieved from the JSON response.
            Movie movie = new Movie(id, adult, backdrop_path, movieCollection, budget, genres,
                    homepage, imdb_id, original_language, original_title, overview, popularity,
                    poster_path, production_companies, production_countries, release_date, revenue,
                    runtime, spoken_languages, status, tagline, title, video, vote_average,
                    vote_count, 0);

            // Add the new {@link Movie} to the list of movies.
            movies.add(movie);
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message with the
            // message from the exception.
            Log.e(TAG, "(parseGetMovieDetailsJSON) Error parsing the JSON response: ", e);
        }

        // Return the list of movies.
        return movies;
    }

    /**
     * Return a {@link CastCrew} object that has been built up from parsing the JSON response
     * given from a call to TMDB API.
     *
     * @param JSONresponse is the JSON object to be parsed and converted to a {@link CastCrew}
     *                     object.
     * @return the {@link CastCrew} object parsed form the input JSON object.
     */
    private static CastCrew parseGetCastCrewListJSON(String JSONresponse) {
        // If the JSON string is empty or null, then return null.
        if (TextUtils.isEmpty(JSONresponse)) {
            return null;
        }

        // Create new empty objects for the CastCrew to be returned.
        int movie_id = 0;
        ArrayList<Cast> castArrayList = new ArrayList<>();
        ArrayList<Crew> crewArrayList = new ArrayList<>();
        CastCrew castCrew = new CastCrew(movie_id, castArrayList, crewArrayList);

        // Try to parse the JSON response string. If there's a problem with the way the JSON is
        // formatted, a JSONException exception object will be thrown. Catch the exception so the
        // app doesn't crash, and print the error message to the logs.
        try {
            // Create a JSONObject from the JSON response string.
            JSONObject resultsJSONResponse = new JSONObject(JSONresponse);

            // Extract the value for the key called "id", which represents the unique identifier of
            // the movie.
            movie_id = getIntFromJSON(resultsJSONResponse, "id");

            // If there is a "cast" section, create a new JSONArray for parsing results.
            if (!resultsJSONResponse.isNull("cast")) {
                JSONArray castArrayJSONResponse = resultsJSONResponse.getJSONArray("cast");
                JSONObject castJSONResponse;
                for (int n = 0; n < castArrayJSONResponse.length(); n++) {
                    // Get a single result at position n within the list of results.
                    castJSONResponse = castArrayJSONResponse.getJSONObject(n);

                    // Extract the required values from the corresponding keys.
                    String character = getStringFromJSON(castJSONResponse, "character");
                    int person_id = getIntFromJSON(castJSONResponse, "id");
                    String name = getStringFromJSON(castJSONResponse, "name");
                    String profile_path = getStringFromJSON(castJSONResponse, "profile_path");

                    // Create a new {@link Cast} object with the data retrieved from the JSON
                    // response.
                    Cast cast = new Cast(character, person_id, name, profile_path);

                    // Add the new {@link Cast} to the list of persons from the movie cast.
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
                    int person_id = getIntFromJSON(crewJSONResponse, "id");
                    String job = getStringFromJSON(crewJSONResponse, "job");
                    String name = getStringFromJSON(crewJSONResponse, "name");
                    String profile_path = getStringFromJSON(crewJSONResponse, "profile_path");

                    // Create a new {@link Crew} object with the data retrieved from the JSON
                    // response.
                    Crew crew = new Crew(person_id, job, name, profile_path);

                    // Add the new {@link Movie} to the list of persons from the movie cast.
                    crewArrayList.add(crew);
                }
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message with the
            // message from the exception.
            Log.e(TAG, "(parseGetCastCrewListJSON) Error parsing the JSON response: ", e);
        }

        // Update the CastCrew object with the data retrieved from the JSON object.
        try {
            castCrew.setId(movie_id);
            castCrew.setCast(castArrayList);
            castCrew.setCrew(crewArrayList);
        } catch (NullPointerException e) {
            Log.e(TAG, "(parseGetCastCrewListJSON) Error parsing the JSON response: ", e);
        }

        // Return the CastCrew object.
        return castCrew;
    }

    /**
     * Return a list of {@link Review} objects that has been built up from parsing the JSON response
     * given from a call to TMDB API returning a list of elements.
     *
     * @param JSONresponse is the JSON object to be parsed and converted to a list of {@link Review}
     *                     objects.
     * @return the list of {@link Review} objects parsed form the input JSON object.
     */
    private static ArrayList<Review> parseGetReviewsListJSON(String JSONresponse) {
        // If the JSON string is empty or null, then return null.
        if (TextUtils.isEmpty(JSONresponse)) {
            Log.i(TAG, "(parseGetReviewsListJSON) The JSON string is empty.");
            return null;
        }

        // Create an empty ArrayList that we can start adding Reviews to.
        ArrayList<Review> reviews = new ArrayList<>();

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

            JSONArray arrayJSONResponse = resultsJSONResponse.getJSONArray("results");
            JSONObject baseJSONResponse;
            for (int n = 0; n < arrayJSONResponse.length(); n++) {
                // Get a single result at position n within the list of results.
                baseJSONResponse = arrayJSONResponse.getJSONObject(n);

                // Extract the required values for the corresponding keys.
                String id = getStringFromJSON(baseJSONResponse, "id");
                String author = getStringFromJSON(baseJSONResponse, "author");
                String content = getStringFromJSON(baseJSONResponse, "content");
                String url = getStringFromJSON(baseJSONResponse, "url");

                // Create a new {@link Review} object with the data retrieved from the JSON response.
                Review review = new Review(id, author, content, url, "", "", "");

                // Add the new {@link Review} to the list of reviews.
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
     * Helper method to get the String value of a key into a JSON object.
     *
     * @param jsonObject is the JSON object that is being parsed.
     * @param key        is the key to search into the JSON object.
     * @return the String value associated to the given key, or an empty String if the key does not
     * exist.
     */

    static private String getStringFromJSON(JSONObject jsonObject, String key) {
        String value = "";
        if (!jsonObject.isNull(key)) try {
            value = jsonObject.getString(key);
        } catch (JSONException e) {
            // If an error is thrown when executing the "try" block, catch the exception here, so
            // the app doesn't crash. Print a log message with the message from the exception.
            Log.e(TAG, "(getStringFromJSON) Error parsing JSON response: ", e);
        }
        return value;
    }

    /**
     * Helper method to get the int value of a key into a JSON object.
     *
     * @param jsonObject is the JSON object that is being parsed.
     * @param key        is the key to search into the JSON object.
     * @return the int value associated to the given key, or 0 if the key does not exist.
     */
    static private int getIntFromJSON(JSONObject jsonObject, String key) {
        int value = 0;
        if (!jsonObject.isNull(key)) try {
            value = jsonObject.getInt(key);
        } catch (JSONException e) {
            // If an error is thrown when executing the "try" block, catch the exception here, so
            // the app doesn't crash. Print a log message with the message from the exception.
            Log.e(TAG, "(getIntFromJSON) Error parsing JSON response: ", e);
        }
        return value;
    }

    /**
     * Helper method to get the boolean value of a key into a JSON object.
     *
     * @param jsonObject is the JSON object that is being parsed.
     * @param key        is the key to search into the JSON object.
     * @return the boolean value associated to the given key, or false if the key does not exist.
     */
    static private boolean getBooleanFromJSON(JSONObject jsonObject, String key) {
        boolean value = false;
        if (!jsonObject.isNull(key)) try {
            value = jsonObject.getBoolean(key);
        } catch (JSONException e) {
            // If an error is thrown when executing the "try" block, catch the exception here, so
            // the app doesn't crash. Print a log message with the message from the exception.
            Log.e(TAG, "(getBooleanFromJSON) Error parsing JSON response: ", e);
        }
        return value;
    }

    /**
     * Helper method to get the Double value of a key into a JSON object.
     *
     * @param jsonObject is the JSON object that is being parsed.
     * @param key        is the key to search into the JSON object.
     * @return the Double value associated to the given key, or 0.0 if the key does not exist.
     */
    static private Double getDoubleFromJSON(JSONObject jsonObject, String key) {
        Double value = 0.0;
        if (!jsonObject.isNull(key)) try {
            value = jsonObject.getDouble(key);
        } catch (JSONException e) {
            // If an error is thrown when executing the "try" block, catch the exception here, so
            // the app doesn't crash. Print a log message with the message from the exception.
            Log.e(TAG, "(getBooleanFromJSON) Error parsing JSON response: ", e);
        }
        return value;
    }
}
