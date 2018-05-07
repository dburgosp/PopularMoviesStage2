package com.example.android.popularmoviesstage2.classes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Public class for managing the languages related to the movies_menu.
 */
public class TmdbMovieLanguage implements Parcelable {
    private String iso_639_1;
    private String name;

    /**
     * @param iso_639_1 is the ISO 639-1 code for identifying the language.
     * @param name      is the name of the language, given in the currently selected language for
     *                  retrieving information from the TMDB API.
     */
    public TmdbMovieLanguage(String iso_639_1, String name) {
        this.iso_639_1 = iso_639_1;
        this.name = name;
    }

    // Getters and setters.

    public String getIso_639_1() {
        return iso_639_1;
    }

    public void setIso_639_1(String iso_639_1) {
        this.iso_639_1 = iso_639_1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Parcelable configuration.

    protected TmdbMovieLanguage(Parcel in) {
        this.iso_639_1 = in.readString();
        this.name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.iso_639_1);
        dest.writeString(this.name);
    }

    public static final Parcelable.Creator<TmdbMovieLanguage> CREATOR = new Parcelable.Creator<TmdbMovieLanguage>() {
        @Override
        public TmdbMovieLanguage createFromParcel(Parcel source) {
            return new TmdbMovieLanguage(source);
        }

        @Override
        public TmdbMovieLanguage[] newArray(int size) {
            return new TmdbMovieLanguage[size];
        }
    };
}
