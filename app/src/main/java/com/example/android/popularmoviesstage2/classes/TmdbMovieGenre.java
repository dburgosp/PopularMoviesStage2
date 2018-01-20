package com.example.android.popularmoviesstage2.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class TmdbMovieGenre implements Parcelable {
    private int id;
    private String name;

    /**
     * Constructor for TmdbMovieGenre class objects.
     *
     * @param id   is the identifier of the movie genre.
     * @param name is the name of the movie genre, given in the currently selected language for
     *             retrieving information form the TMDB API.
     */
    public TmdbMovieGenre(int id, String name) {
        this.id = id;
        this.name = name;
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

    // Parcelable configuration.

    protected TmdbMovieGenre(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
    }

    public static final Parcelable.Creator<TmdbMovieGenre> CREATOR = new Parcelable.Creator<TmdbMovieGenre>() {
        @Override
        public TmdbMovieGenre createFromParcel(Parcel source) {
            return new TmdbMovieGenre(source);
        }

        @Override
        public TmdbMovieGenre[] newArray(int size) {
            return new TmdbMovieGenre[size];
        }
    };
}
