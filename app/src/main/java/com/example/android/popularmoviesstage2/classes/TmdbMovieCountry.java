package com.example.android.popularmoviesstage2.classes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Public class for managing the countries related to movies.
 */
public class TmdbMovieCountry implements Parcelable {
    private String iso_3166_1;
    private String name;

    /**
     * Constructor for TmdbMovieCountry class objects.
     *
     * @param iso_3166_1 is the ISO 3166-1 code for the country.
     * @param name       is the name of the country, given in the currently selected language for
     *                   retrieving information from the TMDB API.
     */
    public TmdbMovieCountry(String iso_3166_1, String name) {
        this.iso_3166_1 = iso_3166_1;
        this.name = name;
    }

    // Getters and setters.

    public String getIso_3166_1() {
        return iso_3166_1;
    }

    public void setIso_3166_1(String iso_3166_1) {
        this.iso_3166_1 = iso_3166_1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Parcelable configuration.

    protected TmdbMovieCountry(Parcel in) {
        this.iso_3166_1 = in.readString();
        this.name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.iso_3166_1);
        dest.writeString(this.name);
    }

    public static final Parcelable.Creator<TmdbMovieCountry> CREATOR = new Parcelable.Creator<TmdbMovieCountry>() {
        @Override
        public TmdbMovieCountry createFromParcel(Parcel source) {
            return new TmdbMovieCountry(source);
        }

        @Override
        public TmdbMovieCountry[] newArray(int size) {
            return new TmdbMovieCountry[size];
        }
    };
}
