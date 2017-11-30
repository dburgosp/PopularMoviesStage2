package com.example.android.popularmoviesstage2;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by David on 26/09/2017.
 * <p>
 * Public class for managing information about movies. It implements {@link Parcelable} so we can
 * send objects of this class between activities.
 * <p>
 * For more information: : https://guides.codepath.com/android/using-parcelable
 */

public class Movie implements Parcelable {
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        /**
         * Create a new instance of the Parcelable class, instantiating it
         * from the given Parcel whose data had previously been written by
         * {@link Parcelable#writeToParcel Parcelable.writeToParcel()}.
         *
         * @param source The Parcel to read the object's data from.
         * @return Returns a new instance of the Parcelable class.
         */
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        /**
         * Create a new array of the Parcelable class.
         *
         * @param size Size of the array.
         * @return Returns an array of the Parcelable class, with every entry
         * initialized to null.
         */
        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    private String title;
    private String posterPath;
    private String overview;
    private Double voteAverage;
    private String releaseDate;
    private int position;

    /*
     * Getter methods.
     */

    /**
     * Constructor for Movie class.
     *
     * @param title       is the original title of the movie.
     * @param posterPath  is the movie poster image thumbnail.
     * @param overview    is the plot synopsis (called overview in the api).
     * @param voteAverage is the user rating (called voteAverage in the api).
     * @param releaseDate is the release date of the movie.
     * @param position    is the position of the movie into the list.
     */
    public Movie(String title, String posterPath, String overview, Double voteAverage, String releaseDate, int position) {
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.position = position;
    }

    private Movie(Parcel in) {
        this.title = in.readString();
        this.posterPath = in.readString();
        this.overview = in.readString();
        this.voteAverage = (Double) in.readValue(Double.class.getClassLoader());
        this.releaseDate = in.readString();
        this.position = (int) in.readValue(int.class.getClassLoader());
    }

    String getTitle() {
        return title;
    }

    String getPosterPath() {
        return posterPath;
    }

    String getOverview() {
        return overview;
    }

    Double getVoteAverage() {
        return voteAverage;
    }

    String getReleaseDate() {
        return releaseDate;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable
     * instance's marshaled representation. For example, if the object will
     * include a file descriptor in the output of {@link #writeToParcel(Parcel, int)},
     * the return value of this method must include the
     * {@link #CONTENTS_FILE_DESCRIPTOR} bit.
     *
     * @return a bitmask indicating the set of special object types marshaled
     * by this Parcelable object instance.
     * @see #CONTENTS_FILE_DESCRIPTOR
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.posterPath);
        dest.writeString(this.overview);
        dest.writeValue(this.voteAverage);
        dest.writeString(this.releaseDate);
        dest.writeValue(this.position);
    }
}
