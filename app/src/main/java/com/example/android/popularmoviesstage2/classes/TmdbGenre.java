package com.example.android.popularmoviesstage2.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class TmdbGenre implements Parcelable {
    private int id;
    private String name;

    /**
     * Constructor for TmdbGenre class objects.
     *
     * @param id   is the identifier of the genre.
     * @param name is the name of the genre, given in the currently selected language for
     *             retrieving information form the TMDB API.
     */
    public TmdbGenre(int id, String name) {
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

    protected TmdbGenre(Parcel in) {
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

    public static final Parcelable.Creator<TmdbGenre> CREATOR = new Parcelable.Creator<TmdbGenre>() {
        @Override
        public TmdbGenre createFromParcel(Parcel source) {
            return new TmdbGenre(source);
        }

        @Override
        public TmdbGenre[] newArray(int size) {
            return new TmdbGenre[size];
        }
    };
}
