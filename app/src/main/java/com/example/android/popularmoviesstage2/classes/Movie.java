package com.example.android.popularmoviesstage2.classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.popularmoviesstage2.utils.NetworkUtils;

import java.util.ArrayList;

/**
 * Public class for managing the complete information of a movie.
 */
public class Movie implements Parcelable {
    private int id;
    private boolean adult;
    private String backdrop_path;
    private MovieCollection belongs_to_collection;
    private int budget;
    private ArrayList<MovieGenre> genres;
    private String homepage;
    private String imdb_id;
    private String original_language;
    private String original_title;
    private String overview;
    private Double popularity;
    private String poster_path;
    private ArrayList<MovieCompany> production_companies;
    private ArrayList<MovieCountry> production_countries;
    private String release_date;
    private int revenue;
    private int runtime;
    private ArrayList<MovieLanguage> spoken_languages;
    private String status;
    private String tagline;
    private String title;
    private boolean video;
    private Double vote_average;
    private int vote_count;
    private int position;
    private int page;
    private int total_pages;

    /**
     * Constructor for objects of this class, containing the whole information of a movie.
     *
     * @param id                    is the unique identifier of the movie on TMDB.
     * @param adult                 is a boolean value that indicates whether the movie has adult
     *                              content or not.
     * @param backdrop_path         is a string for appending to {@link NetworkUtils#THUMBNAIL_IMAGE_URL}
     *                              and get the backdrop image of the movie.
     * @param belongs_to_collection is a {@link MovieCollection} object with the information of the
     *                              collection to which the movie belongs.
     * @param budget                is the budget, in US dollars, for the movie.
     * @param genres                is an array with {@link MovieGenre} objects, containing the
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
     *                              {@link NetworkUtils#THUMBNAIL_IMAGE_URL} and get the poster of
     *                              the movie.
     * @param production_companies  is an array of {@link MovieCompany} objects containing the
     *                              information about the companies that have produced the movie.
     * @param production_countries  is an array of {@link MovieCountry} objects containing the
     *                              information about the countries in which the movie has been
     *                              produced.
     * @param release_date          is the date in which the movie was released.
     * @param revenue               is the revenue, in US dollars, for the movie.
     * @param runtime               is the movie duration, in minutes.
     * @param spoken_languages      is an array of {@link MovieLanguage} objects containing the
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
     * @param position              is the position of the movie into the current page.
     * @param page                  is the current page in the list of movies.
     * @param total_pages           is the total number of movie pages.
     */
    public Movie(int id, boolean adult, String backdrop_path, MovieCollection belongs_to_collection,
                 int budget, ArrayList<MovieGenre> genres, String homepage, String imdb_id,
                 String original_language, String original_title, String overview, Double popularity,
                 String poster_path, ArrayList<MovieCompany> production_companies,
                 ArrayList<MovieCountry> production_countries, String release_date, int revenue,
                 int runtime, ArrayList<MovieLanguage> spoken_languages, String status,
                 String tagline, String title, boolean video, Double vote_average, int vote_count,
                 int position, int page, int total_pages) {
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
        this.position = position;
        this.page = page;
        this.total_pages = total_pages;
    }

    /**
     * Constructor for objects of this class, containing a subset of information about a movie.
     *
     * @param id                is the unique identifier of the movie on TMDB.
     * @param adult             is a boolean value that indicates whether the movie has adult
     *                          content or not.
     * @param backdrop_path     is a string for appending to {@link NetworkUtils#THUMBNAIL_IMAGE_URL}
     *                          and get the backdrop image of the movie.
     * @param genres            is an array with {@link MovieGenre} objects, containing the
     *                          genres of the movie.
     * @param original_language is a text string with the original language of the movie.
     * @param original_title    is a text string with the original title of the movie.
     * @param overview          is the text overview for the movie, given in the currently
     *                          selected language for retrieving information from the TMDB API.
     * @param popularity        is a double value indicating how popular is the movie on TMDB
     *                          (based on views, ratings, favourite additions, etc.).
     * @param poster_path       is a string for appending to
     *                          {@link NetworkUtils#THUMBNAIL_IMAGE_URL} and get the poster of
     *                          the movie.
     * @param release_date      is the date in which the movie was released.
     * @param title             is the movie title, given in the currently selected language for
     *                          retrieving information from the TMDB API.
     * @param video             is a boolean value that indicates if there is any available
     *                          video for the movie.
     * @param vote_average      is the current users rating for the movie, from 0 to 10.
     * @param vote_count        is the number of persons who have voted for the movie.
     * @param position          is the position of the movie into the list.
     * @param position              is the position of the movie into the current page.
     * @param page                  is the current page in the list of movies.
     * @param total_pages           is the total number of movie pages.
     */
    public Movie(int id, boolean adult, String backdrop_path, ArrayList<MovieGenre> genres,
                 String original_language, String original_title, String overview, Double popularity,
                 String poster_path, String release_date, String title, boolean video,
                 Double vote_average, int vote_count, int position, int page, int total_pages) {
        this.id = id;
        this.adult = adult;
        this.backdrop_path = backdrop_path;
        this.belongs_to_collection = null;
        this.budget = 0;
        this.genres = genres;
        this.homepage = "";
        this.imdb_id = "";
        this.original_language = original_language;
        this.original_title = original_title;
        this.overview = overview;
        this.popularity = popularity;
        this.poster_path = poster_path;
        this.production_companies = null;
        this.production_countries = null;
        this.release_date = release_date;
        this.revenue = 0;
        this.runtime = 0;
        this.spoken_languages = null;
        this.status = "";
        this.tagline = "";
        this.title = title;
        this.video = video;
        this.vote_average = vote_average;
        this.vote_count = vote_count;
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

    public MovieCollection getBelongs_to_collection() {
        return belongs_to_collection;
    }

    public void setBelongs_to_collection(MovieCollection belongs_to_collection) {
        this.belongs_to_collection = belongs_to_collection;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public ArrayList<MovieGenre> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<MovieGenre> genres) {
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

    public ArrayList<MovieCompany> getProduction_companies() {
        return production_companies;
    }

    public void setProduction_companies(ArrayList<MovieCompany> production_companies) {
        this.production_companies = production_companies;
    }

    public ArrayList<MovieCountry> getProduction_countries() {
        return production_countries;
    }

    public void setProduction_countries(ArrayList<MovieCountry> production_countries) {
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

    public ArrayList<MovieLanguage> getSpoken_languages() {
        return spoken_languages;
    }

    public void setSpoken_languages(ArrayList<MovieLanguage> spoken_languages) {
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

    // Parcelable configuration.

    protected Movie(Parcel in) {
        this.id = in.readInt();
        this.adult = in.readByte() != 0;
        this.backdrop_path = in.readString();
        this.belongs_to_collection = in.readParcelable(MovieCollection.class.getClassLoader());
        this.budget = in.readInt();
        this.genres = in.createTypedArrayList(MovieGenre.CREATOR);
        this.homepage = in.readString();
        this.imdb_id = in.readString();
        this.original_language = in.readString();
        this.original_title = in.readString();
        this.overview = in.readString();
        this.popularity = (Double) in.readValue(Double.class.getClassLoader());
        this.poster_path = in.readString();
        this.production_companies = in.createTypedArrayList(MovieCompany.CREATOR);
        this.production_countries = in.createTypedArrayList(MovieCountry.CREATOR);
        this.release_date = in.readString();
        this.revenue = in.readInt();
        this.runtime = in.readInt();
        this.spoken_languages = in.createTypedArrayList(MovieLanguage.CREATOR);
        this.status = in.readString();
        this.tagline = in.readString();
        this.title = in.readString();
        this.video = in.readByte() != 0;
        this.vote_average = (Double) in.readValue(Double.class.getClassLoader());
        this.vote_count = in.readInt();
        this.position = in.readInt();
        this.page = in.readInt();
        this.total_pages = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeByte(this.adult ? (byte) 1 : (byte) 0);
        dest.writeString(this.backdrop_path);
        dest.writeParcelable(this.belongs_to_collection, flags);
        dest.writeInt(this.budget);
        dest.writeTypedList(this.genres);
        dest.writeString(this.homepage);
        dest.writeString(this.imdb_id);
        dest.writeString(this.original_language);
        dest.writeString(this.original_title);
        dest.writeString(this.overview);
        dest.writeValue(this.popularity);
        dest.writeString(this.poster_path);
        dest.writeTypedList(this.production_companies);
        dest.writeTypedList(this.production_countries);
        dest.writeString(this.release_date);
        dest.writeInt(this.revenue);
        dest.writeInt(this.runtime);
        dest.writeTypedList(this.spoken_languages);
        dest.writeString(this.status);
        dest.writeString(this.tagline);
        dest.writeString(this.title);
        dest.writeByte(this.video ? (byte) 1 : (byte) 0);
        dest.writeValue(this.vote_average);
        dest.writeInt(this.vote_count);
        dest.writeInt(this.position);
        dest.writeInt(this.page);
        dest.writeInt(this.total_pages);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
