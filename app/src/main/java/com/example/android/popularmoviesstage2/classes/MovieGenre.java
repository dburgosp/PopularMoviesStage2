package com.example.android.popularmoviesstage2.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieGenre implements Parcelable {
    private int id;
    private String name;

    /**
     * Constructor for MovieGenre class objects.
     *
     * @param id   is the identifier of the movie genre.
     * @param name is the name of the movie genre, given in the currently selected language for
     *             retrieving information form the TMDB API.
     */
    public MovieGenre(int id, String name) {
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

    protected MovieGenre(Parcel in) {
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

    public static final Parcelable.Creator<MovieGenre> CREATOR = new Parcelable.Creator<MovieGenre>() {
        @Override
        public MovieGenre createFromParcel(Parcel source) {
            return new MovieGenre(source);
        }

        @Override
        public MovieGenre[] newArray(int size) {
            return new MovieGenre[size];
        }
    };
}
