package com.example.android.popularmoviesstage2.classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Public class for managing the short information about a tv series, extracted from a page of
 * results.
 */
public class TmdbTVSeries implements Parcelable {
    private String poster_path;
    private Double popularity;
    private int id;
    private String backdrop_path;
    private Double vote_average;
    private String overview;
    private String first_air_date;
    private ArrayList<String> origin_country;
    private ArrayList<Integer> genre_ids;
    private String original_language;
    private int vote_count;
    private String name;
    private String original_name;
    private int position;
    private int page;
    private int total_pages;
    private int total_results;

    /**
     * Constructor for objects of this class.
     *
     * @param poster_path       is a string for appending to {@link Tmdb#TMDB_POSTER_SIZE_W185_URL}
     *                          and get the poster of the tv series.
     * @param popularity        is a double value indicating how popular is the tv series on TMDB
     *                          (based on views, ratings, favourite additions, etc.)
     * @param id                is the unique identifier of the tv series on TMDB.
     * @param backdrop_path     is a string for appending to {@link Tmdb#TMDB_POSTER_SIZE_W185_URL}
     *                          and get the backdrop image of the tv series.
     * @param vote_average      is the current users rating for the tv series, from 0 to 10.
     * @param overview          is the text overview for the tv series, given in the currently
     *                          selected language for retrieving information from the TMDB API.
     * @param first_air_date    is the date in which the tv series was released.
     * @param origin_country    is an array with the ISO 3166-1 codes of the countries of the tv
     *                          series.
     * @param genre_ids         is an array containing the {@link TmdbGenre} ids of the tv series.
     * @param original_language is a text string with the original language of the tv series.
     * @param vote_count        is the number of persons who have voted for the tv series.
     * @param name              is the tv series title, given in the currently selected language for
     *                          retrieving information from the TMDB API.
     * @param original_name     is a text string with the original title of the tv series.
     * @param position          is the position of the tv series into the current page.
     * @param page              is the current page in the list of tv series.
     * @param total_pages       is the total number of pages with tv series in the list.
     * @param total_results     is the total number of tv series in the list.
     */
    public TmdbTVSeries(String poster_path, Double popularity, int id, String backdrop_path,
                        Double vote_average, String overview, String first_air_date,
                        ArrayList<String> origin_country, ArrayList<Integer> genre_ids,
                        String original_language, int vote_count, String name, String original_name,
                        int position, int page, int total_pages, int total_results) {
        this.poster_path = poster_path;
        this.popularity = popularity;
        this.id = id;
        this.backdrop_path = backdrop_path;
        this.vote_average = vote_average;
        this.overview = overview;
        this.first_air_date = first_air_date;
        this.origin_country = origin_country;
        this.genre_ids = genre_ids;
        this.original_language = original_language;
        this.vote_count = vote_count;
        this.name = name;
        this.original_name = original_name;
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

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public Double getVote_average() {
        return vote_average;
    }

    public void setVote_average(Double vote_average) {
        this.vote_average = vote_average;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getFirst_air_date() {
        return first_air_date;
    }

    public void setFirst_air_date(String first_air_date) {
        this.first_air_date = first_air_date;
    }

    public ArrayList<String> getOrigin_country() {
        return origin_country;
    }

    public void setOrigin_country(ArrayList<String> origin_country) {
        this.origin_country = origin_country;
    }

    public ArrayList<Integer> getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(ArrayList<Integer> genre_ids) {
        this.genre_ids = genre_ids;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginal_name() {
        return original_name;
    }

    public void setOriginal_name(String original_name) {
        this.original_name = original_name;
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
        dest.writeValue(this.popularity);
        dest.writeInt(this.id);
        dest.writeString(this.backdrop_path);
        dest.writeValue(this.vote_average);
        dest.writeString(this.overview);
        dest.writeString(this.first_air_date);
        dest.writeStringList(this.origin_country);
        dest.writeList(this.genre_ids);
        dest.writeString(this.original_language);
        dest.writeInt(this.vote_count);
        dest.writeString(this.name);
        dest.writeString(this.original_name);
        dest.writeInt(this.position);
        dest.writeInt(this.page);
        dest.writeInt(this.total_pages);
        dest.writeInt(this.total_results);
    }

    protected TmdbTVSeries(Parcel in) {
        this.poster_path = in.readString();
        this.popularity = (Double) in.readValue(Double.class.getClassLoader());
        this.id = in.readInt();
        this.backdrop_path = in.readString();
        this.vote_average = (Double) in.readValue(Double.class.getClassLoader());
        this.overview = in.readString();
        this.first_air_date = in.readString();
        this.origin_country = in.createStringArrayList();
        this.genre_ids = new ArrayList<Integer>();
        in.readList(this.genre_ids, Integer.class.getClassLoader());
        this.original_language = in.readString();
        this.vote_count = in.readInt();
        this.name = in.readString();
        this.original_name = in.readString();
        this.position = in.readInt();
        this.page = in.readInt();
        this.total_pages = in.readInt();
        this.total_results = in.readInt();
    }

    public static final Creator<TmdbTVSeries> CREATOR = new Creator<TmdbTVSeries>() {
        @Override
        public TmdbTVSeries createFromParcel(Parcel source) {
            return new TmdbTVSeries(source);
        }

        @Override
        public TmdbTVSeries[] newArray(int size) {
            return new TmdbTVSeries[size];
        }
    };
}