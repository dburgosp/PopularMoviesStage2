package com.example.android.popularmoviesstage2.classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Public class for managing the cast & crew of a movie.
 */
public class TmdbCastCrew implements Parcelable {
    private int id;
    private ArrayList<TmdbCast> cast;
    private ArrayList<TmdbCrew> crew;

    /**
     * Public constructor for new instances of objects of this class.
     *
     * @param id   is the unique identifier of the {@link TmdbMovie}.
     * @param cast is an array of {@link TmdbCast} objects, the list of members of the movie cast.
     * @param crew is an array of {@link TmdbCrew} objects, the list of members of the movie crew.
     */
    public TmdbCastCrew(int id, ArrayList<TmdbCast> cast, ArrayList<TmdbCrew> crew) {
        this.id = id;
        this.cast = cast;
        this.crew = crew;
    }

    // Getters and setters.

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<TmdbCast> getCast() {
        return cast;
    }

    public void setCast(ArrayList<TmdbCast> cast) {
        this.cast = cast;
    }

    public ArrayList<TmdbCrew> getCrew() {
        return crew;
    }

    public void setCrew(ArrayList<TmdbCrew> crew) {
        this.crew = crew;
    }

    // Parcelable configuration.

    protected TmdbCastCrew(Parcel in) {
        this.id = in.readInt();
        this.cast = in.createTypedArrayList(TmdbCast.CREATOR);
        this.crew = in.createTypedArrayList(TmdbCrew.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeTypedList(this.cast);
        dest.writeTypedList(this.crew);
    }

    public static final Parcelable.Creator<TmdbCastCrew> CREATOR = new Parcelable.Creator<TmdbCastCrew>() {
        @Override
        public TmdbCastCrew createFromParcel(Parcel source) {
            return new TmdbCastCrew(source);
        }

        @Override
        public TmdbCastCrew[] newArray(int size) {
            return new TmdbCastCrew[size];
        }
    };
}
