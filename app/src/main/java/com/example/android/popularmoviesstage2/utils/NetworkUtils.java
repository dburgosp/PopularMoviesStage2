package com.example.android.popularmoviesstage2.utils;

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
import com.example.android.popularmoviesstage2.classes.Image;
import com.example.android.popularmoviesstage2.classes.Media;
import com.example.android.popularmoviesstage2.classes.Movie;
import com.example.android.popularmoviesstage2.classes.MovieCollection;
import com.example.android.popularmoviesstage2.classes.MovieCompany;
import com.example.android.popularmoviesstage2.classes.MovieCountry;
import com.example.android.popularmoviesstage2.classes.MovieGenre;
import com.example.android.popularmoviesstage2.classes.MovieLanguage;
import com.example.android.popularmoviesstage2.classes.OMDB;
import com.example.android.popularmoviesstage2.classes.Review;
import com.example.android.popularmoviesstage2.classes.Video;

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
    private final static String TAG = NetworkUtils.class.getSimpleName();

    // URLs.
    private final static String TMDB_BASE_URL = "https://api.themoviedb.org/3";
    public final static String TMDB_THUMBNAIL_IMAGE_URL = "https://image.tmdb.org/t/p/w185";
    public final static String TMDB_FULL_IMAGE_URL = "https://image.tmdb.org/t/p/w500";
    public final static String YOUTUBE_BASE_URL = "https://youtube.com/watch?v=";
    public final static String YOUTUBE_VIDEO_PREVIEW_URL = "https://img.youtube.com/vi/";
    public final static String YOUTUBE_VIDEO_MQDEFAULT_IMAGE = "/mqdefault.jpg";
    private final static String OMDB_BASE_URL = "http://www.omdbapi.com";

    // Paths for appending to urls.
    public final static String TMDB_POPULAR_PATH = "popular";
    public final static String TMDB_TOP_RATED_PATH = "top_rated";
    public final static String TMDB_FAVORITES_PATH = "favorites";
    private final static String TMDB_MOVIE_PATH = "movie";
    private final static String TMDB_CREDITS_PATH = "credits";
    private final static String TMDB_REVIEWS_PATH = "reviews";
    private final static String TMDB_VIDEOS_PATH = "videos";
    private final static String TMDB_IMAGES_PATH = "images";

    // Parameters for using with urls.
    private final static String TMDB_PARAM_API_KEY = "api_key";
    private final static String TMDB_PARAM_PAGE = "page";
    private final static String TMDB_PARAM_APPEND_TO_RESPONSE = "append_to_response";
    private final static String OMDB_PARAM_API_KEY = "apikey";
    private final static String OMDB_PARAM_MOVIE_ID = "i";

    // Filters for getting movies lists.
    public final static int OPERATION_GET_MOVIES_LIST = 1;
    public final static int OPERATION_GET_MOVIE_DETAILS = 2;
    public final static int REVIEWS_PER_PAGE = 5;

    // Rating sources from OMDB.
    private final static String OMDB_RATING_SOURCE_IMDB = "Internet Movie Database";
    private final static String OMDB_RATING_SOURCE_ROTTEN_TOMATOES = "Rotten Tomatoes";
    private final static String OMDB_RATING_SOURCE_METACRITIC = "Metacritic";

    // Unique identifiers for loaders.
    public static final int MOVIES_LOADER_ID = 1;
    public static final int MOVIE_DETAILS_LOADER_ID = 2;
    public static final int CAST_CREW_LOADER_ID = 3;
    public static final int MEDIA_LOADER_ID = 4;
    public static final int REVIEWS_LOADER_ID = 5;
    public static final int OMDB_LOADER_ID = 6;

    // API KEY is defined into gradle.properties and referenced from app:build.gradle. The file
    // gradle.properties is included in the .gitignore file, so the API KEY will not be published
    // on GitHub.
    //
    // For more information: https://richardroseblog.wordpress.com/2016/05/29/hiding-secret-api-keys-from-git/
    private final static String TMBD_API_KEY = BuildConfig.TMDB_API_KEY;
    private final static String OMBD_API_KEY = BuildConfig.OMDB_API_KEY;

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
        NetworkInfo activeNetwork = null;
        try {
            activeNetwork = cm.getActiveNetworkInfo();
        } catch (NullPointerException e) {
            Log.e(TAG, "(isConnected) Error getting active network info: " + e);
        }
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    /**
     * Creates an URL from {@link #TMDB_BASE_URL} appending the sort order to the path and using the
     * {@link #TMBD_API_KEY} for authentication.
     *
     * @param sortOrder is the sort order for the movie list.
     * @return an URL for fetching data from TheMovieDB using the given sort order.
     */
    public static URL buildFetchMoviesListURL(String sortOrder, String page) {
        Log.i(TAG, "(buildFetchMoviesListURL) Sort order: " + sortOrder);

        // Build Uri from TMDB_BASE_URL, sort order and TMBD_API_KEY.
        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendPath(TMDB_MOVIE_PATH)
                .appendPath(sortOrder)
                .appendQueryParameter(TMDB_PARAM_API_KEY, TMBD_API_KEY)
                .appendQueryParameter(TMDB_PARAM_PAGE, page)
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
     * Creates an URL from {@link #TMDB_BASE_URL} appending the movie id to the path and using the
     * {@link #TMBD_API_KEY} for authentication.
     *
     * @param movieId is the identifier of the movie.
     * @return an URL for fetching information for a movie from TheMovieDB.
     */
    public static URL buildFetchMovieDetailsURL(int movieId) {
        Log.i(TAG, "(buildFetchMovieDetailsURL) Movie ID: " + movieId);

        // Build Uri from TMDB_BASE_URL, movie ID and TMBD_API_KEY.
        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendPath(TMDB_MOVIE_PATH)
                .appendPath(Integer.toString(movieId))
                .appendQueryParameter(TMDB_PARAM_API_KEY, TMBD_API_KEY)
                .build();

        // Build URL from Uri and return.
        return buildUrlFromUri(builtUri);
    }

    /**
     * Creates an URL from {@link #OMDB_BASE_URL} appending the movie id to the path and using the
     * {@link #OMBD_API_KEY} for authentication.
     *
     * @param imdb is the IMDB identifier of the movie.
     * @return an URL for fetching information for a movie from TheMovieDB.
     */
    public static URL buildFetchOMDBinfoURL(String imdb) {
        Log.i(TAG, "(buildFetchOMDBinfoURL) Movie ID: " + imdb);

        // Build Uri from OMDB_BASE_URL, movie ID and OMBD_API_KEY.
        Uri builtUri = Uri.parse(OMDB_BASE_URL).buildUpon()
                .appendQueryParameter(OMDB_PARAM_MOVIE_ID, imdb)
                .appendQueryParameter(OMDB_PARAM_API_KEY, OMBD_API_KEY)
                .build();

        // Build URL from Uri and return.
        return buildUrlFromUri(builtUri);
    }

    /**
     * Creates an URL from {@link #TMDB_BASE_URL} appending the movie id and the TMDB_CREDITS_PATH to
     * the base path and using the {@link #TMBD_API_KEY} for authentication.
     *
     * @param movieId is the identifier of the movie.
     * @return an URL for fetching a movie cast & crew information from TheMovieDB.
     */
    public static URL buildFetchCastCrewListURL(int movieId) {
        Log.i(TAG, "(buildFetchCastCrewListURL) Movie ID: " + movieId);

        // Build Uri from TMDB_BASE_URL, movie ID, TMDB_CREDITS_PATH and TMBD_API_KEY.
        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendPath(TMDB_MOVIE_PATH)
                .appendPath(Integer.toString(movieId))
                .appendPath(TMDB_CREDITS_PATH)
                .appendQueryParameter(TMDB_PARAM_API_KEY, TMBD_API_KEY)
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
     * Creates an URL from {@link #TMDB_BASE_URL} appending the movie id and the TMDB_REVIEWS_PATH to
     * the base path and using the {@link #TMBD_API_KEY} for authentication.
     *
     * @param movieId     is the identifier of the movie.
     * @param currentPage is the page for searching reviews of the movie.
     * @return an URL for fetching a movie cast & reviews information from TheMovieDB.
     */
    public static URL buildFetchReviewsListURL(int movieId, String currentPage) {
        Log.i(TAG, "(buildFetchReviewsListURL) Movie ID " + movieId + ", page " + currentPage);

        // Build Uri from TMDB_BASE_URL, sort order and TMBD_API_KEY.
        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendPath(TMDB_MOVIE_PATH)
                .appendPath(Integer.toString(movieId))
                .appendPath(TMDB_REVIEWS_PATH)
                .appendQueryParameter(TMDB_PARAM_API_KEY, TMBD_API_KEY)
                .appendQueryParameter(TMDB_PARAM_PAGE, currentPage)
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
     * Creates an URL from {@link #TMDB_BASE_URL} appending the movie id and the VIDEOS_PATH to
     * the base path, using the {@link #TMBD_API_KEY} for authentication and appending videos and images.
     *
     * @param movieId is the identifier of the movie.
     * @return an URL for fetching movie videos information from TheMovieDB.
     */
    public static URL buildFetchMediaListURL(int movieId) {
        Log.i(TAG, "(buildFetchMediaListURL) Movie ID " + movieId);

        // Build Uri from TMDB_BASE_URL, sort order and TMBD_API_KEY.
        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendPath(TMDB_MOVIE_PATH)
                .appendPath(Integer.toString(movieId))
                .appendQueryParameter(TMDB_PARAM_API_KEY, TMBD_API_KEY)
                .appendQueryParameter(TMDB_PARAM_APPEND_TO_RESPONSE, "videos,images")
                .build();

        // Build URL from Uri.
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, "(buildFetchMediaListURL) Error building URL: " + e);
        }

        Log.i(TAG, "(buildFetchMediaListURL) URL: " + url);
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
     * Fetches OMDB for additional information about one movie.
     *
     * @param url is the base URL of OMDB.
     * @return an {@link OMDB} object.
     */
    public static OMDB fetchOMDBinfo(URL url) {
        Log.i(TAG, "(fetchOMDBinfo) URL: " + url);

        // Connect to TheMovieDB and get the JSON document with the results of the query.
        String JSONresponse = null;
        try {
            JSONresponse = getJSONresponse(url);
        } catch (java.io.IOException e) {
            Log.e(TAG, "(fetchOMDBinfo) Error retrieving JSON response: ", e);
        }

        // Parse JSON document into an array of {@link Movie} objects.
        return parseGetOMDBinfoJSON(JSONresponse);
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
     * Fetches videos and images related to a movie from TheMovieDB.
     *
     * @param url is the URL at TheMovieDB.
     * @return an ArrayList of {@link Media} objects.
     */
    public static Media fetchMedia(URL url) {
        Log.i(TAG, "(fetchMedia) URL: " + url);

        // Connect to TheMovieDB and get the JSON document with the results of the query.
        String JSONresponse = null;
        try {
            JSONresponse = getJSONresponse(url);
        } catch (java.io.IOException e) {
            Log.e(TAG, "(fetchMedia) Error retrieving JSON response: ", e);
        }

        // Parse JSON document into an array of {@link Media} objects.
        return parseGetMediaJSON(JSONresponse);
    }

    /**
     * Retrieves a JSON document from an URL previously created.
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

            // Get paging info.
            int page = getIntFromJSON(resultsJSONResponse, "page");
            int total_pages = getIntFromJSON(resultsJSONResponse, "total_pages");

            // Get results array.
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
                        video, vote_average, vote_count, n, page, total_pages);

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
                    vote_count, 0, 0, 0);

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
                    String credit_id = getStringFromJSON(crewJSONResponse, "credit_id");
                    String department = getStringFromJSON(crewJSONResponse, "department");
                    int gender = getIntFromJSON(crewJSONResponse, "gender");
                    int person_id = getIntFromJSON(crewJSONResponse, "id");
                    String job = getStringFromJSON(crewJSONResponse, "job");
                    String name = getStringFromJSON(crewJSONResponse, "name");
                    String profile_path = getStringFromJSON(crewJSONResponse, "profile_path");

                    // Create a new {@link Crew} object with the data retrieved from the JSON
                    // response.
                    Crew crew = new Crew(credit_id, department, gender, person_id, job, name, profile_path);

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
     * Return a {@link OMDB} object that has been built up from parsing the JSON response
     * given from a call to OMDB API.
     *
     * @param JSONresponse is the JSON object to be parsed and converted to a {@link OMDB} object.
     * @return the {@link OMDB} object parsed form the input JSON object.
     */
    private static OMDB parseGetOMDBinfoJSON(String JSONresponse) {
        // If the JSON string is empty or null, then return null.
        if (TextUtils.isEmpty(JSONresponse)) {
            return null;
        }

        // Try to parse the JSON response string. If there's a problem with the way the JSON is
        // formatted, a JSONException exception object will be thrown. Catch the exception so the
        // app doesn't crash, and print the error message to the logs.
        try {
            // Create a JSONObject from the JSON response string.
            JSONObject resultsJSONResponse = new JSONObject(JSONresponse);

            // Extract the required values from the corresponding keys and create default values
            // for user scores.
            String rated = getStringFromJSON(resultsJSONResponse, "Rated");
            String awards = getStringFromJSON(resultsJSONResponse, "Awards");
            String dvd = getStringFromJSON(resultsJSONResponse, "DVD");
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
                    String source = getStringFromJSON(ratingsJSONResponse, "Source");
                    String value = getStringFromJSON(ratingsJSONResponse, "Value");

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

            // Return a new {@link OMDB} object with the data retrieved from the JSON response.
            return new OMDB(rated, awards, dvd, imdb_vote_average, rt_vote_average, mc_vote_average);
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message with the
            // message from the exception.
            Log.e(TAG, "(parseGetOMDBinfoJSON) Error parsing the JSON response: ", e);
            return null;
        }
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

            // Extract the value of the current page and the total number of pages from the base
            // JSON document.
            int page = getIntFromJSON(resultsJSONResponse, "page");
            int total_pages = getIntFromJSON(resultsJSONResponse, "total_pages");

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
                Review review = new Review(id, author, content, url, "", "", "", page, n, total_pages);

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
     * Return a {@link Media} object that has been built up from parsing the JSON response given
     * from a call to TMDB API returning a list of elements.
     *
     * @param JSONresponse is the JSON object to be parsed and converted to a {@link Media} object.
     * @return a {@link Media} object parsed form the input JSON object.
     */
    private static Media parseGetMediaJSON(String JSONresponse) {
        // Create an null Media object.
        Media media = null;

        // If the JSON string is empty or null, then return null.
        if (TextUtils.isEmpty(JSONresponse)) {
            Log.i(TAG, "(parseGetMediaJSON) The JSON string is empty.");
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
                Log.i(TAG, "(parseGetMediaJSON) No \"videos\" or \"images\" sections in the JSON string.");
                return null;
            }

            // Get the movie id.
            int movie_id = getIntFromJSON(resultsJSONResponse, "id");

            // Create an empty ArrayList that we can start adding Videos to, and extract the
            // "videos" array, if exists.
            ArrayList<Video> videos = new ArrayList<>();
            if (!resultsJSONResponse.isNull("videos")) {
                JSONObject videosSectionJSONResponse = resultsJSONResponse.getJSONObject("videos");
                if (!videosSectionJSONResponse.isNull("results")) {
                    JSONArray videosArrayJSONResponse = videosSectionJSONResponse.getJSONArray("results");
                    JSONObject videoJSONResponse;
                    for (int n = 0; n < videosArrayJSONResponse.length(); n++) {
                        // Get a single result at position n within the list of results.
                        videoJSONResponse = videosArrayJSONResponse.getJSONObject(n);

                        // Extract the required values for the corresponding keys.
                        String id = getStringFromJSON(videoJSONResponse, "id");
                        String iso_639_1 = getStringFromJSON(videoJSONResponse, "iso_639_1");
                        String iso_3166_1 = getStringFromJSON(videoJSONResponse, "iso_3166_1");
                        String key = getStringFromJSON(videoJSONResponse, "key");
                        String name = getStringFromJSON(videoJSONResponse, "name");
                        String site = getStringFromJSON(videoJSONResponse, "site");
                        int size = getIntFromJSON(videoJSONResponse, "size");
                        String type = getStringFromJSON(videoJSONResponse, "type");

                        // Create a new {@link Media} object with the data retrieved from the JSON response.
                        Video video = new Video(id, iso_639_1, iso_3166_1, key, name, site, size, type);

                        // Add the new {@link Media} to the list of media.
                        videos.add(video);
                    }
                }

                // Create two empty ArrayLists that we can start adding posters and backdrops Images
                // to, and extract the "images" array, if exists.
                ArrayList<Image> posters = new ArrayList<>();
                ArrayList<Image> backdrops = new ArrayList<>();
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

                // Add info to the Media object.
                media = new Media(movie_id, videos, posters, backdrops);
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message with the
            // message from the exception.
            Log.e(TAG, "(parseGetMediaJSON) Error parsing the JSON response: ", e);
        }

        // Return the list of media.
        return media;
    }

    /**
     * Helper method to get an array list of Image objects from a JSON object given by a .
     *
     * @param resultsJSONResponse is the JSON object to be parsed.
     * @param key                 is the key tobe searched into the JSON object. Available values:
     *                            "posters" and "backdrops".
     * @return an array list of Image objects.
     */
    static private ArrayList<Image> getImagesArrayList(JSONObject resultsJSONResponse, String key) {
        // Create an empty array of Image elements.
        ArrayList<Image> images = new ArrayList<>();

        try {
            JSONArray imagesArrayJSONResponse = resultsJSONResponse.getJSONArray(key);
            JSONObject imageJSONResponse;
            for (int n = 0; n < imagesArrayJSONResponse.length(); n++) {
                // Get a single result at position n within the list of results.
                imageJSONResponse = imagesArrayJSONResponse.getJSONObject(n);

                // Extract the required values for the corresponding keys.
                Double aspect_ratio = getDoubleFromJSON(imageJSONResponse, "aspect_ratio");
                String file_path = getStringFromJSON(imageJSONResponse, "file_path");
                int height = getIntFromJSON(imageJSONResponse, "height");
                String iso_639_1 = getStringFromJSON(imageJSONResponse, "iso_639_1");
                int vote_average = getIntFromJSON(imageJSONResponse, "vote_average");
                int vote_count = getIntFromJSON(imageJSONResponse, "vote_count");
                int width = getIntFromJSON(imageJSONResponse, "width");

                // Create a new {@link Image} object with the data retrieved from the JSON response.
                Image image = new Image(aspect_ratio, file_path, height, iso_639_1, vote_average,
                        vote_count, width);

                // Add the new {@link Media} to the list of media.
                images.add(image);
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message with the
            // message from the exception.
            Log.e(TAG, "(parseGetMediaJSON) Error parsing the JSON response: ", e);
        }

        // Return the previously created array. If the loop in the try/catch statement has produced
        // no result, this array will be empty.
        return images;
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
