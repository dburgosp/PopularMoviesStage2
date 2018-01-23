package com.example.android.popularmoviesstage2.classes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Public class for managing all the data about movie collections.
 */
public class TmdbMovieCollection implements Parcelable {
    private int id;
    private String name;
    private String overview;
    private String poster_path;
    private String backdrop_path;
    private TmdbMovie parts;

    /**
     * Constructor for TmdbMovieCollection class objects.
     *
     * @param id            is the unique identifier of the movie collection on TMDB.
     * @param name          is the name of the movie collection, given in the currently selected
     *                      language for retrieving information from the TMDB API.
     * @param overview      is the overview of the movie collection, given in the currently selected
     *                      language for retrieving information from the TMDB API.
     * @param poster_path   is a string for appending to {@link Tmdb#TMDB_THUMBNAIL_IMAGE_URL}
     *                      and get the poster of the movie collection.
     * @param backdrop_path is a string for appending to {@link Tmdb#TMDB_THUMBNAIL_IMAGE_URL}
     *                      and get the backdrop image of the movie collection.
     * @param parts         is an array of {@link TmdbMovie} elements containing some simple information
     *                      about the movies in the current collection.
     */
    public TmdbMovieCollection(int id, String name, String overview, String poster_path,
                               String backdrop_path, TmdbMovie parts) {
        this.id = id;
        this.name = name;
        this.overview = overview;
        this.poster_path = poster_path;
        this.backdrop_path = backdrop_path;
        this.parts = parts;
    }

    // Getters and setters.

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public TmdbMovie getParts() {
        return parts;
    }

    public void setParts(TmdbMovie parts) {
        this.parts = parts;
    }

    // Parcelable configuration.

    protected TmdbMovieCollection(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.overview = in.readString();
        this.poster_path = in.readString();
        this.backdrop_path = in.readString();
        this.parts = in.readParcelable(TmdbMovie.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.overview);
        dest.writeString(this.poster_path);
        dest.writeString(this.backdrop_path);
        dest.writeParcelable(this.parts, flags);
    }

    public static final Parcelable.Creator<TmdbMovieCollection> CREATOR = new Parcelable.Creator<TmdbMovieCollection>() {
        @Override
        public TmdbMovieCollection createFromParcel(Parcel source) {
            return new TmdbMovieCollection(source);
        }

        @Override
        public TmdbMovieCollection[] newArray(int size) {
            return new TmdbMovieCollection[size];
        }
    };
}