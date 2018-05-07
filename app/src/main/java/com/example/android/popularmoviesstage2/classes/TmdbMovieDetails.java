package com.example.android.popularmoviesstage2.classes;

import java.util.ArrayList;

/**
 * Public class for managing the complete information about a movie.
 */
public class TmdbMovieDetails {
    private TmdbMovie movie;
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
    private TmdbRelease releases;
    private TmdbExternalId externalIds;
    private ArrayList<TmdbKeyword> keywords;
    private ArrayList<TmdbMovie> similarMovies;
    private ArrayList<TmdbMovie> recommendedMovies;

    /**
     * Constructor for objects of this class, containing the whole information of a movie.
     *
     * @param movie                 is a {@link TmdbMovie} object with the basic information about
     *                              the movie.
     * @param belongs_to_collection is a {@link TmdbMovieCollection} object with the information of
     *                              the collection to which the movie belongs.
     * @param budget                is the budget, in US dollars, for the movie.
     * @param homepage              is the url of the official web page of the movie.
     * @param imdb_id               is the IMDB code for appending to http://www.imdb.com/title/ and
     *                              get the web page of the movie on IMDB.
     * @param production_companies  is an array of {@link TmdbMovieCompany} objects containing the
     *                              information about the companies that have produced the movie.
     * @param production_countries  is an array of {@link TmdbMovieCountry} objects containing the
     *                              information about the countries in which the movie has been
     *                              produced.
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
     * @param releases              is a {@link TmdbRelease} object containing the information about
     *                              the releases of the movie at the current country.
     * @param externalIds           is a {@link TmdbExternalId} object containing the information
     *                              about the external links related to the movie.
     * @param keywords              is an array of {@link TmdbKeyword} object containing the
     *                              keywords related to the movie.
     * @param similarMovies         is an array of {@link TmdbMovie} object containing a list of
     *                              movies_menu that are similar to the current one.
     * @param recommendedMovies     is an array of {@link TmdbMovie} object containing a list of
     *                              recommended movies_menu. This is not the same list than similarMovies.
     */
    TmdbMovieDetails(TmdbMovie movie, TmdbMovieCollection belongs_to_collection, int budget,
                     String homepage, String imdb_id,
                     ArrayList<TmdbMovieCompany> production_companies,
                     ArrayList<TmdbMovieCountry> production_countries, int revenue, int runtime,
                     ArrayList<TmdbMovieLanguage> spoken_languages, String status, String tagline,
                     TmdbRelease releases, TmdbExternalId externalIds,
                     ArrayList<TmdbKeyword> keywords, ArrayList<TmdbMovie> similarMovies,
                     ArrayList<TmdbMovie> recommendedMovies) {
        this.movie = movie;
        this.belongs_to_collection = belongs_to_collection;
        this.budget = budget;
        this.homepage = homepage;
        this.imdb_id = imdb_id;
        this.production_companies = production_companies;
        this.production_countries = production_countries;
        this.revenue = revenue;
        this.runtime = runtime;
        this.spoken_languages = spoken_languages;
        this.status = status;
        this.tagline = tagline;
        this.releases = releases;
        this.externalIds = externalIds;
        this.keywords = keywords;
        this.similarMovies = similarMovies;
        this.recommendedMovies = recommendedMovies;
    }

    // Getters and setters.

    public TmdbMovie getMovie() {
        return movie;
    }

    public void setMovie(TmdbMovie movie) {
        this.movie = movie;
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

    public TmdbRelease getReleases() {
        return releases;
    }

    public void setReleases(TmdbRelease releases) {
        this.releases = releases;
    }

    public TmdbExternalId getExternalIds() {
        return externalIds;
    }

    public void setExternalIds(TmdbExternalId externalIds) {
        this.externalIds = externalIds;
    }

    public ArrayList<TmdbKeyword> getKeywords() {
        return keywords;
    }

    public void setKeywords(ArrayList<TmdbKeyword> keywords) {
        this.keywords = keywords;
    }

    public ArrayList<TmdbMovie> getSimilarMovies() {
        return similarMovies;
    }

    public void setSimilarMovies(ArrayList<TmdbMovie> similarMovies) {
        this.similarMovies = similarMovies;
    }

    public ArrayList<TmdbMovie> getRecommendedMovies() {
        return recommendedMovies;
    }

    public void setRecommendedMovies(ArrayList<TmdbMovie> recommendedMovies) {
        this.recommendedMovies = recommendedMovies;
    }
}
