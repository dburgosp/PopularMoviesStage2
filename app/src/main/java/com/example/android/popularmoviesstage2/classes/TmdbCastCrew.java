package com.example.android.popularmoviesstage2.classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Public class for managing the tmdbCast & tmdbCrew of a movie.
 */
public class TmdbCastCrew implements Parcelable {
    private int id;
    private ArrayList<TmdbCast> tmdbCast;
    private ArrayList<TmdbCrew> tmdbCrew;

    /**
     * Public constructor for new instances of objects of this class.
     *
     * @param id   is the unique identifier of the {@link TmdbMovie}.
     * @param tmdbCast is an array of {@link TmdbCast} objects, the list of members of the movie tmdbCast.
     * @param tmdbCrew is an array of {@link TmdbCrew} objects, the list of members of the movie tmdbCrew.
     */
    public TmdbCastCrew(int id, ArrayList<TmdbCast> tmdbCast, ArrayList<TmdbCrew> tmdbCrew) {
        this.id = id;
        this.tmdbCast = tmdbCast;
        this.tmdbCrew = tmdbCrew;
    }

    // Getters and setters.

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<TmdbCast> getTmdbCast() {
        return tmdbCast;
    }

    public void setTmdbCast(ArrayList<TmdbCast> tmdbCast) {
        this.tmdbCast = tmdbCast;
    }

    public ArrayList<TmdbCrew> getTmdbCrew() {
        return tmdbCrew;
    }

    public void setTmdbCrew(ArrayList<TmdbCrew> tmdbCrew) {
        this.tmdbCrew = tmdbCrew;
    }

    // Parcelable configuration.

    protected TmdbCastCrew(Parcel in) {
        this.id = in.readInt();
        this.tmdbCast = in.createTypedArrayList(TmdbCast.CREATOR);
        this.tmdbCrew = in.createTypedArrayList(TmdbCrew.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeTypedList(this.tmdbCast);
        dest.writeTypedList(this.tmdbCrew);
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
