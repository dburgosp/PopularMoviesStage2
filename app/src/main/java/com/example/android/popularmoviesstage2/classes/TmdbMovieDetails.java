package com.example.android.popularmoviesstage2.classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Public class for managing the complete information of a movie.
 */
public class TmdbMovieDetails {
    // Common variables with {@link TmdbMovie} class.
    private String poster_path;
    private boolean adult;
    private String overview;
    private String release_date;
    private ArrayList<TmdbMovieGenre> genres;
    private int id;
    private String original_title;
    private String original_language;
    private String title;
    private String backdrop_path;
    private Double popularity;
    private int vote_count;
    private boolean video;
    private Double vote_average;

    // Variables that does not belong to {@link TmdbMovie} class.
    private TmdbMovieCollection belongs_to_collection;
    private int budget;
    private String homepage;
    private String imdb_id;
    private ArrayList<TmdbMovieCompany> production_companies;
    private ArrayList<TmdbMovieCountry> production_countries;
    private int revenue;
    private int runtime;
    private ArrayList<TmdbMovieLanguage> spoken_languages;
    private String status;
    private String tagline;

    // Variables from append_to_response argument to the query.
    private ArrayList<TmdbRelease> releases;

    // Variables for paging and positioning the movie in a list.
    private int position;
    private int page;
    private int total_pages;

    /**
     * Constructor for objects of this class, containing the whole information of a movie.
     *
     * @param id                    is the unique identifier of the movie on TMDB.
     * @param adult                 is a boolean value that indicates whether the movie has adult
     *                              content or not.
     * @param backdrop_path         is a string for appending to {@link Tmdb#TMDB_THUMBNAIL_IMAGE_URL}
     *                              and get the backdrop image of the movie.
     * @param belongs_to_collection is a {@link TmdbMovieCollection} object with the information of the
     *                              collection to which the movie belongs.
     * @param budget                is the budget, in US dollars, for the movie.
     * @param genres                is an array with {@link TmdbMovieGenre} objects, containing the
     *                              genres of the movie.
     * @param homepage              is the url of the official web page of the movie.
     * @param imdb_id               is the IMDB code for appending to http://www.imdb.com/title/ and
     *                              get the web page of the movie on IMDB.
     * @param original_language     is a text string with the original language of the movie.
     * @param original_title        is a text string with the original title of the movie.
     * @param overview              is the text overview for the movie, given in the currently
     *                              selected language for retrieving information from the TMDB API.
     * @param popularity            is a double value indicating how popular is the movie on TMDB
     *                              (based on views, ratings, favourite additions, etc.).
     * @param poster_path           is a string for appending to
     *                              {@link Tmdb#TMDB_THUMBNAIL_IMAGE_URL} and get the poster of
     *                              the movie.
     * @param production_companies  is an array of {@link TmdbMovieCompany} objects containing the
     *                              information about the companies that have produced the movie.
     * @param production_countries  is an array of {@link TmdbMovieCountry} objects containing the
     *                              information about the countries in which the movie has been
     *                              produced.
     * @param release_date          is the date in which the movie was released.
     * @param revenue               is the revenue, in US dollars, for the movie.
     * @param runtime               is the movie duration, in minutes.
     * @param spoken_languages      is an array of {@link TmdbMovieLanguage} objects containing the
     *                              information about the languages spoken in the movie.
     * @param status                is the release status of the movie. Allowed values are "Rumored",
     *                              "Planned", "In Production", "Post Production", "Released" and
     *                              "Canceled".
     * @param tagline               is the movie tagline, a sort of subtitle or slogan, given in the
     *                              currently selected language for retrieving information from the
     *                              TMDB API.
     * @param title                 is the movie title, given in the currently selected language for
     *                              retrieving information from the TMDB API.
     * @param video                 is a boolean value that indicates if there is any available
     *                              video for the movie.
     * @param vote_average          is the current users rating for the movie, from 0 to 10.
     * @param vote_count            is the number of persons who have voted for the movie.
     * @param releases              is an array of {@link TmdbRelease} objects containing the
     *                              information about the releases of the movie.
     * @param position              is the position of the movie into the current page.
     * @param page                  is the current page in the list of movies.
     * @param total_pages           is the total number of movie pages.
     */
    public TmdbMovieDetails(int id, boolean adult, String backdrop_path, TmdbMovieCollection belongs_to_collection,
                            int budget, ArrayList<TmdbMovieGenre> genres, String homepage, String imdb_id,
                            String original_language, String original_title, String overview, Double popularity,
                            String poster_path, ArrayList<TmdbMovieCompany> production_companies,
                            ArrayList<TmdbMovieCountry> production_countries, String release_date, int revenue,
                            int runtime, ArrayList<TmdbMovieLanguage> spoken_languages, String status,
                            String tagline, String title, boolean video, Double vote_average, int vote_count,
                            ArrayList<TmdbRelease> releases, int position, int page, int total_pages) {
        this.id = id;
        this.adult = adult;
        this.backdrop_path = backdrop_path;
        this.belongs_to_collection = belongs_to_collection;
        this.budget = budget;
        this.genres = genres;
        this.homepage = homepage;
        this.imdb_id = imdb_id;
        this.original_language = original_language;
        this.original_title = original_title;
        this.overview = overview;
        this.popularity = popularity;
        this.poster_path = poster_path;
        this.production_companies = production_companies;
        this.production_countries = production_countries;
        this.release_date = release_date;
        this.revenue = revenue;
        this.runtime = runtime;
        this.spoken_languages = spoken_languages;
        this.status = status;
        this.tagline = tagline;
        this.title = title;
        this.video = video;
        this.vote_average = vote_average;
        this.vote_count = vote_count;
        this.releases = releases;
        this.position = position;
        this.page = page;
        this.total_pages = total_pages;
    }

    // Getters and setters.

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public TmdbMovieCollection getBelongs_to_collection() {
        return belongs_to_collection;
    }

    public void setBelongs_to_collection(TmdbMovieCollection belongs_to_collection) {
        this.belongs_to_collection = belongs_to_collection;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public ArrayList<TmdbMovieGenre> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<TmdbMovieGenre> genres) {
        this.genres = genres;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getImdb_id() {
        return imdb_id;
    }

    public void setImdb_id(String imdb_id) {
        this.imdb_id = imdb_id;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public ArrayList<TmdbMovieCompany> getProduction_companies() {
        return production_companies;
    }

    public void setProduction_companies(ArrayList<TmdbMovieCompany> production_companies) {
        this.production_companies = production_companies;
    }

    public ArrayList<TmdbMovieCountry> getProduction_countries() {
        return production_countries;
    }

    public void setProduction_countries(ArrayList<TmdbMovieCountry> production_countries) {
        this.production_countries = production_countries;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public int getRevenue() {
        return revenue;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public ArrayList<TmdbMovieLanguage> getSpoken_languages() {
        return spoken_languages;
    }

    public void setSpoken_languages(ArrayList<TmdbMovieLanguage> spoken_languages) {
        this.spoken_languages = spoken_languages;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public Double getVote_average() {
        return vote_average;
    }

    public void setVote_average(Double vote_average) {
        this.vote_average = vote_average;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public ArrayList<TmdbRelease> getReleases() {
        return releases;
    }

    public void setReleases(ArrayList<TmdbRelease> releases) {
        this.releases = releases;
    }
    // Parcelable configuration.
}
