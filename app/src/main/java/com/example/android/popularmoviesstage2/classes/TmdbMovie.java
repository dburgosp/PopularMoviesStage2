package com.example.android.popularmoviesstage2.classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Public class for managing the short information of a movie.
 */
public class TmdbMovie implements Parcelable {
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
    private int position;
    private int page;
    private int total_pages;
    private int total_results;

    /**
     * Constructor for objects of this class.
     *
     * @param id                is the unique identifier of the movie on TMDB.
     * @param adult             is a boolean value that indicates whether the movie has adult
     *                          content or not.
     * @param backdrop_path     is a string for appending to {@link Tmdb#TMDB_POSTER_SIZE_W185_URL}
     *                          and get the backdrop image of the movie.
     * @param genres            is an array with {@link TmdbMovieGenre} objects, containing the
     *                          genres of the movie.
     * @param original_language is a text string with the original language of the movie.
     * @param original_title    is a text string with the original title of the movie.
     * @param overview          is the text overview for the movie, given in the currently
     *                          selected language for retrieving information from the TMDB API.
     * @param popularity        is a double value indicating how popular is the movie on TMDB
     *                          (based on views, ratings, favourite additions, etc.).
     * @param poster_path       is a string for appending to
     *                          {@link Tmdb#TMDB_POSTER_SIZE_W185_URL} and get the poster of
     *                          the movie.
     * @param release_date      is the date in which the movie was released.
     * @param title             is the movie title, given in the currently selected language for
     *                          retrieving information from the TMDB API.
     * @param video             is a boolean value that indicates if there is any available
     *                          video for the movie.
     * @param vote_average      is the current users rating for the movie, from 0 to 10.
     * @param vote_count        is the number of persons who have voted for the movie.
     * @param position          is the position of the movie into the current page.
     * @param page              is the current page in the list of movies.
     * @param total_pages       is the total number of movie pages.
     * @param total_results     is the total number of movies.
     */
    public TmdbMovie(int id, boolean adult, String backdrop_path, ArrayList<TmdbMovieGenre> genres,
                     String original_language, String original_title, String overview,
                     Double popularity, String poster_path, String release_date, String title,
                     boolean video, Double vote_average, int vote_count, int position, int page,
                     int total_pages, int total_results) {
        this.id = id;
        this.adult = adult;
        this.backdrop_path = backdrop_path;
        this.genres = genres;
        this.original_language = original_language;
        this.original_title = original_title;
        this.overview = overview;
        this.popularity = popularity;
        this.poster_path = poster_path;
        this.release_date = release_date;
        this.title = title;
        this.video = video;
        this.vote_average = vote_average;
        this.vote_count = vote_count;
        this.position = position;
        this.page = page;
        this.total_pages = total_pages;
        this.total_results = total_results;
    }

    // Getters and setters.

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public ArrayList<TmdbMovieGenre> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<TmdbMovieGenre> genres) {
        this.genres = genres;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }
    // Parcelable configuration.

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.poster_path);
        dest.writeByte(this.adult ? (byte) 1 : (byte) 0);
        dest.writeString(this.overview);
        dest.writeString(this.release_date);
        dest.writeTypedList(this.genres);
        dest.writeInt(this.id);
        dest.writeString(this.original_title);
        dest.writeString(this.original_language);
        dest.writeString(this.title);
        dest.writeString(this.backdrop_path);
        dest.writeValue(this.popularity);
        dest.writeInt(this.vote_count);
        dest.writeByte(this.video ? (byte) 1 : (byte) 0);
        dest.writeValue(this.vote_average);
        dest.writeInt(this.position);
        dest.writeInt(this.page);
        dest.writeInt(this.total_pages);
        dest.writeInt(this.total_results);
    }

    protected TmdbMovie(Parcel in) {
        this.poster_path = in.readString();
        this.adult = in.readByte() != 0;
        this.overview = in.readString();
        this.release_date = in.readString();
        this.genres = in.createTypedArrayList(TmdbMovieGenre.CREATOR);
        this.id = in.readInt();
        this.original_title = in.readString();
        this.original_language = in.readString();
        this.title = in.readString();
        this.backdrop_path = in.readString();
        this.popularity = (Double) in.readValue(Double.class.getClassLoader());
        this.vote_count = in.readInt();
        this.video = in.readByte() != 0;
        this.vote_average = (Double) in.readValue(Double.class.getClassLoader());
        this.position = in.readInt();
        this.page = in.readInt();
        this.total_pages = in.readInt();
        this.total_results = in.readInt();
    }

    public static final Parcelable.Creator<TmdbMovie> CREATOR = new Parcelable.Creator<TmdbMovie>() {
        @Override
        public TmdbMovie createFromParcel(Parcel source) {
            return new TmdbMovie(source);
        }

        @Override
        public TmdbMovie[] newArray(int size) {
            return new TmdbMovie[size];
        }
    };
}
