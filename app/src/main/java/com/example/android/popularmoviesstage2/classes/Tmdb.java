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
    public static final int TMDB_RELEASE_TYPE_PREMIERE = 1;
    public static final int TMDB_RELEASE_TYPE_THEATRICAL_LIMITED = 2;
    public static final int TMDB_RELEASE_TYPE_THEATRICAL = 3;
    public static final int TMDB_RELEASE_TYPE_DIGITAL = 4;
    public static final int TMDB_RELEASE_TYPE_PHYSICAL = 5;
    public static final int TMDB_RELEASE_TYPE_TV = 6;

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
     * @param language    is the language of the results.
     * @return an array of {@link TmdbMovie} objects.
     */

    public static ArrayList<TmdbMovie> getTmdbMovies(String sortOrder, String currentPage, String language) {
        Log.i(TAG, "(getTmdbMovies) Sort order: " + sortOrder + ". Page number: " +
                currentPage + ". Language: " + language);

        /* ------------ */
        /* Get the JSON */
        /* ------------ */

        // Build the uniform resource indentifier (uri) for fetching data from TMDB API.
        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendPath(TMDB_MOVIE_PATH)
                .appendPath(sortOrder)
                .appendQueryParameter(TMDB_PARAM_API_KEY, TMBD_API_KEY)
                .appendQueryParameter(TMDB_PARAM_PAGE, currentPage)
                .appendQueryParameter(TMDB_PARAM_LANGUAGE, language)
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
        ArrayList<TmdbMovie> movies = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON is
        // formatted, a JSONException exception object will be thrown.
        try {
            // Create a JSONObject from the JSON response string.
            JSONObject resultsJSONResponse = new JSONObject(JSONresponse);
            // If there is no "results" section exit returning null. Otherwise, create a new
            // JSONArray for parsing results.
            if (resultsJSONResponse.isNull("results")) {
                Log.i(TAG, "(getMovie) No \"results\" section in the JSON string.");
                return null;
            }

            // Get paging info.
            int page = NetworkUtils.getIntFromJSON(resultsJSONResponse, "page");
            int total_pages = NetworkUtils.getIntFromJSON(resultsJSONResponse, "total_pages");

            // Get results array.
            JSONArray arrayJSONResponse = resultsJSONResponse.getJSONArray("results");
            JSONObject baseJSONResponse;
            for (int n = 0; n < arrayJSONResponse.length(); n++) {
                // Get a single result at position n within the list of results, extract the movie
                // info and append it to the movies array..
                baseJSONResponse = arrayJSONResponse.getJSONObject(n);
                TmdbMovie movie = getMovie(baseJSONResponse, n, page, total_pages);
                movies.add(movie);
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash.
            Log.e(TAG, "(getMovie) Error parsing the JSON response: ", e);
        }

        // Return the movies array.
        return movies;
    }

    /**
     * Private helper method to extract a {@link TmdbMovie} object from a JSON.
     *
     * @param baseJSONResponse is the JSONObject containing the movie info.
     * @param n                is the current index of the element into the movies array.
     * @param page             is the current page of the movie element.
     * @param total_pages      is the number of pages of the current query.
     * @return the {@link TmdbMovie} object parsed from the JSON.
     * @throws JSONException from getJSONArray and getJSONObject calls.
     */
    private static TmdbMovie getMovie(JSONObject baseJSONResponse, int n, int page, int total_pages) throws JSONException {
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
        // the list of genres which the movie belongs to.
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
                TmdbMovieGenre genre = new TmdbMovieGenre(genre_id, genre_name);
                genres.add(genre);
            }
        }

        // Return the {@link TmdbMovie} object parsed from the JSON.
        return new TmdbMovie(id, adult, backdrop_path, genres, original_language,
                original_title, overview, popularity, poster_path, release_date, title,
                video, vote_average, vote_count, n, page, total_pages);
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
            TmdbMovie movie = getMovie(baseJSONResponse, 0, 0, 0);
            int budget = NetworkUtils.getIntFromJSON(baseJSONResponse, "budget");
            String homepage = NetworkUtils.getStringFromJSON(baseJSONResponse, "homepage");
            String imdb_id = NetworkUtils.getStringFromJSON(baseJSONResponse, "imdb_id");
            int revenue = NetworkUtils.getIntFromJSON(baseJSONResponse, "revenue");
            int runtime = NetworkUtils.getIntFromJSON(baseJSONResponse, "runtime");
            String status = NetworkUtils.getStringFromJSON(baseJSONResponse, "status");
            String tagline = NetworkUtils.getStringFromJSON(baseJSONResponse, "tagline");

            // Extract the JSONArray associated with the key called "genres", which represents
            // the list of genres which the movie belongs to.
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
                    TmdbMovieGenre movieGenre = new TmdbMovieGenre(genre_id, genre_name);
                    genres.add(movieGenre);
                }
            }

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

            // Return a {@link TmdbMovieDetails} object with the data retrieved from the JSON
            // response.
            return new TmdbMovieDetails(movie, movieCollection, budget, homepage, imdb_id,
                    production_companies, production_countries, revenue, runtime, spoken_languages,
                    status, tagline, releases, externalIds, keywords);
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
                    // Extract the JSONArray associated with the key called "genres", which represents
                    // the list of genres which the tmdbMovie belongs to.
                    if (!currentResultJSONObject.isNull("release_dates")) {
                        JSONArray releaseDatesArray = currentResultJSONObject.getJSONArray("release_dates");

                        // For each release date in the array, create an {@link TmdbReleaseDate} object.
                        JSONObject currentReleaseDatesJSONObject;
                        for (int i = 0; i < releaseDatesArray.length(); i++) {
                            // Get a single release date at position i within the list of genres.
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
