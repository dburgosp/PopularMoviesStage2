package com.example.android.popularmoviesstage2.classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Public class for managing the cast & crew of a movie.
 */
public class CastCrew implements Parcelable {
    public static final Parcelable.Creator<CastCrew> CREATOR = new Parcelable.Creator<CastCrew>() {
        @Override
        public CastCrew createFromParcel(Parcel source) {
            return new CastCrew(source);
        }

        @Override
        public CastCrew[] newArray(int size) {
            return new CastCrew[size];
        }
    };
    private int id;
    private ArrayList<Cast> cast;
    private ArrayList<Crew> crew;

    // Getters and setters.

    /**
     * Public constructor for new instances of objects of this class.
     *
     * @param id   is the unique identifier of the {@link Movie}.
     * @param cast is an array of {@link Cast} objects, the list of members of the movie cast.
     * @param crew is an array of {@link Crew} objects, the list of members of the movie crew.
     */
    public CastCrew(int id, ArrayList<Cast> cast, ArrayList<Crew> crew) {
        this.id = id;
        this.cast = cast;
        this.crew = crew;
    }

    protected CastCrew(Parcel in) {
        this.id = in.readInt();
        this.cast = in.createTypedArrayList(Cast.CREATOR);
        this.crew = in.createTypedArrayList(Crew.CREATOR);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Cast> getCast() {
        return cast;
    }

    public void setCast(ArrayList<Cast> cast) {
        this.cast = cast;
    }

    public ArrayList<Crew> getCrew() {
        return crew;
    }

    public void setCrew(ArrayList<Crew> crew) {
        this.crew = crew;
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
}
