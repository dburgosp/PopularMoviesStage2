package com.example.android.popularmoviesstage2.classes;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.android.popularmoviesstage2.BuildConfig;
import com.example.android.popularmoviesstage2.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Public class with static resources for fetching data from TMDB.
 */
public class Tmdb {
    private final static String TAG = Tmdb.class.getSimpleName();

    // URLs.
    private final static String TMDB_BASE_URL = "https://api.themoviedb.org/3";
    public final static String TMDB_THUMBNAIL_IMAGE_URL = "https://image.tmdb.org/t/p/w185";
    public final static String TMDB_FULL_IMAGE_URL = "https://image.tmdb.org/t/p/w500";

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
    private final static String TMDB_PARAM_LANGUAGE = "language";
    private final static String TMDB_PARAM_REGION = "region";

    // Available release status for movies.
    public final static String TMDB_STATUS_RUMORED = "Rumored";
    public final static String TMDB_STATUS_PLANNED = "Planned";
    public final static String TMDB_STATUS_IN_PRODUCTION = "In Production";
    public final static String TMDB_STATUS_POST_PRODUCTION = "Post Production";
    public final static String TMDB_STATUS_RELEASED = "Released";
    public final static String TMDB_STATUS_CANCELED = "Canceled";

    // Release dates types.
    private static final String TMDB_RELEASE_TYPE_PREMIERE = "Premiere";
    private static final String TMDB_RELEASE_TYPE_THEATRICAL_LIMITED = "Theatrical (limited)";
    private static final String TMDB_RELEASE_TYPE_THEATRICAL = "Theatrical";
    private static final String TMDB_RELEASE_TYPE_DIGITAL = "Digital";
    private static final String TMDB_RELEASE_TYPE_PHYSICAL = "Physical";
    private static final String TMDB_RELEASE_TYPE_TV = "TV";

    // Maximum number of pages.
    public final static int TMDB_MAX_PAGES = 1000;

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
     * @param sortOrder   is the sort order for the movie list.
     * @param currentPage is the page number to fetch.
     * @return an array of {@link TmdbMovie} objects.
     */
    public static ArrayList<TmdbMovie> getTmdbMovies(String sortOrder, String currentPage) {
        Log.i(TAG, "(getTmdbMovies) Sort order: " + sortOrder + ". Page number: " + currentPage);

        /* ------------ */
        /* Get the JSON */
        /* ------------ */

        // Build the uniform resource indentifier (uri) for fetching data from TMDB API.
        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendPath(TMDB_MOVIE_PATH)
                .appendPath(sortOrder)
                .appendQueryParameter(TMDB_PARAM_API_KEY, TMBD_API_KEY)
                .appendQueryParameter(TMDB_PARAM_PAGE, currentPage)
                .build();

        // Use the built uri to get the JSON document with the results of the query.
        String JSONresponse;
        try {
            JSONresponse = NetworkUtils.getJSONresponse(builtUri);
        } catch (java.io.IOException e) {
            // If getJSONresponse has thrown an exception, exit returning null.
            Log.e(TAG, "(getTmdbMovies) Error retrieving JSON response: ", e);
            return null;
        }

        /* -------------- */
        /* Parse the JSON */
        /* -------------- */

        // If the JSON string is empty or null, then return null.
        if (TextUtils.isEmpty(JSONresponse)) {
            Log.i(TAG, "(getTmdbMovies) The JSON string is empty.");
            return null;
        }

        // Create an empty array of TmdbMovie objects to append data.
        ArrayList<TmdbMovie> tmdbMovies = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON is
        // formatted, a JSONException exception object will be thrown. 
        try {
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

            // Get results array.
            JSONArray arrayJSONResponse = resultsJSONResponse.getJSONArray("results");
            JSONObject baseJSONResponse;
            for (int n = 0; n < arrayJSONResponse.length(); n++) {
                // Get a single result at position n within the list of results.
                baseJSONResponse = arrayJSONResponse.getJSONObject(n);

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

                // Extract the JSONArray associated with the key called "genres", which represents
                // the list of genres which the tmdbMovie belongs to.
                ArrayList<TmdbMovieGenre> genres = new ArrayList<>();
                if (!baseJSONResponse.isNull("genres")) {
                    JSONArray genresArray = baseJSONResponse.getJSONArray("genres");

                    // For each genre in the array, create an {@link TmdbMovieGenre} object.
                    JSONObject currentGenre;
                    for (int i = 0; i < genresArray.length(); i++) {
                        // Get a single genre at position i within the list of genres.
                        currentGenre = genresArray.getJSONObject(i);

                        // Extract the required values for the corresponding keys.
                        int genre_id = NetworkUtils.getIntFromJSON(currentGenre, "id");
                        String genre_name = NetworkUtils.getStringFromJSON(currentGenre, "name");

                        // Create a new {@link TmdbMovieGenre} object and add it to the array.
                        TmdbMovieGenre tmdbMovieGenre = new TmdbMovieGenre(genre_id, genre_name);
                        genres.add(tmdbMovieGenre);
                    }
                }

                // Create a new {@link TmdbMovie} object with the data retrieved from the JSON response.
                TmdbMovie tmdbMovie = new TmdbMovie(id, adult, backdrop_path, genres, original_language,
                        original_title, overview, popularity, poster_path, release_date, title,
                        video, vote_average, vote_count, n, page, total_pages);

                // Append current element to the movies array.
                tmdbMovies.add(tmdbMovie);
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. 
            Log.e(TAG, "(getTmdbMovies) Error parsing the JSON response: ", e);
        }

        // Return the tmdbMovie.
        return tmdbMovies;
    }

    /**
     * Fetches TMDB for detailed information about a single movie.
     *
     * @param movieId is the identifier of the movie in TMDB.
     * @return a {@link TmdbMovieDetails} object.
     */
    public static TmdbMovieDetails getTmdbMovieDetails(int movieId) {
        Log.i(TAG, "(getTmdbMovieDetails) Movie ID: " + movieId);

        /* ------------ */
        /* Get the JSON */
        /* ------------ */

        // Build the uniform resource identifier (uri) for fetching data from TMDB API.
        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendPath(TMDB_MOVIE_PATH)
                .appendPath(Integer.toString(movieId))
                .appendQueryParameter(TMDB_PARAM_API_KEY, TMBD_API_KEY)
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
            int budget = NetworkUtils.getIntFromJSON(baseJSONResponse, "budget");
            String homepage = NetworkUtils.getStringFromJSON(baseJSONResponse, "homepage");
            String imdb_id = NetworkUtils.getStringFromJSON(baseJSONResponse, "imdb_id");
            int revenue = NetworkUtils.getIntFromJSON(baseJSONResponse, "revenue");
            int runtime = NetworkUtils.getIntFromJSON(baseJSONResponse, "runtime");
            String status = NetworkUtils.getStringFromJSON(baseJSONResponse, "status");
            String tagline = NetworkUtils.getStringFromJSON(baseJSONResponse, "tagline");

            // Extract the JSONArray associated with the key called "genres", which represents
            // the list of genres which the tmdbMovie belongs to.
            ArrayList<TmdbMovieGenre> genres = new ArrayList<>();
            if (!baseJSONResponse.isNull("genres")) {
                JSONArray genresArray = baseJSONResponse.getJSONArray("genres");

                // For each genre in the array, create an {@link TmdbMovieGenre} object.
                JSONObject currentGenre;
                for (int i = 0; i < genresArray.length(); i++) {
                    // Get a single genre at position i within the list of genres.
                    currentGenre = genresArray.getJSONObject(i);

                    // Extract the required values for the corresponding keys.
                    int genre_id = NetworkUtils.getIntFromJSON(currentGenre, "id");
                    String genre_name = NetworkUtils.getStringFromJSON(currentGenre, "name");

                    // Create a new {@link TmdbMovieGenre} object and add it to the array.
                    TmdbMovieGenre tmdbMovieGenre = new TmdbMovieGenre(genre_id, genre_name);
                    genres.add(tmdbMovieGenre);
                }
            }

            // Extract the value for the key called "belongs_to_collection", which represents an
            // optional JSON object with information about the collection which the current tmdbMovie
            // belongs to.
            TmdbMovieCollection tmdbMovieCollection = null;
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
                tmdbMovieCollection = new TmdbMovieCollection(belongs_to_collection_id,
                        belongs_to_collection_name, "", belongs_to_collection_poster_path,
                        belongs_to_collection_backdrop_path, null);
            }

            // Extract the JSONArray associated with the key called "production_companies",
            // which represents the list of companies participating in the production of the
            // tmdbMovie.
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
                    TmdbMovieCompany tmdbMovieCompany = new TmdbMovieCompany("", "",
                            "", company_id, "", company_name,
                            null);
                    production_companies.add(tmdbMovieCompany);
                }
            }

            // Extract the JSONArray associated with the key called "production_countries",
            // which represents the list of countries in which the tmdbMovie has been produced.
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
                    TmdbMovieCountry tmdbMovieCountry = new TmdbMovieCountry(iso_3166_1, country_name);
                    production_countries.add(tmdbMovieCountry);
                }
            }

            // Extract the JSONArray associated with the key called "spoken_languages", which
            // represents the list of languages spoken in the tmdbMovie.
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
                    TmdbMovieLanguage tmdbMovieLanguage = new TmdbMovieLanguage(iso_639_1, language_name);
                    spoken_languages.add(tmdbMovieLanguage);
                }
            }

            // TODO: parse release_dates array.

            // Return a {@link TmdbMovieDetails} object with the data retrieved from the JSON
            // response.
            return new TmdbMovieDetails(id, adult, backdrop_path, tmdbMovieCollection, budget,
                    genres, homepage, imdb_id, original_language, original_title, overview,
                    popularity, poster_path, production_companies, production_countries,
                    release_date, revenue, runtime, spoken_languages, status, tagline, title, video,
                    vote_average, vote_count, null, 0, 0, 0);
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
        ArrayList<TmdbCast> tmdbCastArrayList = new ArrayList<>();
        ArrayList<TmdbCrew> tmdbCrewArrayList = new ArrayList<>();
        TmdbCastCrew tmdbCastCrew = new TmdbCastCrew(movie_id, tmdbCastArrayList, tmdbCrewArrayList);

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
                    TmdbCast tmdbCast = new TmdbCast(character, person_id, name, profile_path);

                    // Add the new {@link TmdbCast} to the list of persons from the movie tmdbCast.
                    tmdbCastArrayList.add(tmdbCast);
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
                    TmdbCrew tmdbCrew = new TmdbCrew(credit_id, department, gender, person_id, job, name, profile_path);

                    // Add the new {@link TmdbMovie} to the list of persons from the movie cast.
                    tmdbCrewArrayList.add(tmdbCrew);
                }
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. 
            Log.e(TAG, "(getTmdbCastAndCrew) Error parsing the JSON response: ", e);
        }

        // Update the TmdbCastCrew object with the data retrieved from the JSON object.
        try {
            tmdbCastCrew.setId(movie_id);
            tmdbCastCrew.setCast(tmdbCastArrayList);
            tmdbCastCrew.setCrew(tmdbCrewArrayList);
        } catch (NullPointerException e) {
            Log.e(TAG, "(getTmdbCastAndCrew) Error parsing the JSON response: ", e);
        }

        // Return the TmdbCastCrew object.
        return tmdbCastCrew;
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
        ArrayList<TmdbReview> tmdbReviews = new ArrayList<>();

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

                // Create a new {@link TmdbReview} object with the data retrieved from the JSON response.
                TmdbReview tmdbReview = new TmdbReview(id, author, content, url, "", "", "", page, n, total_pages);

                // Add the new {@link TmdbReview} to the list of tmdbReviews.
                tmdbReviews.add(tmdbReview);
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message with the
            // message from the exception.
            Log.e(TAG, "(parseGetReviewsListJSON) Error parsing the JSON response: ", e);
        }

        // Return the list of tmdbReviews.
        return tmdbReviews;
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
        return sortOrder.equals(TMDB_POPULAR_PATH) ||
                sortOrder.equals(TMDB_TOP_RATED_PATH) ||
                sortOrder.equals(TMDB_FAVORITES_PATH);
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
