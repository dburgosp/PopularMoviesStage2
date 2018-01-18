package com.example.android.popularmoviesstage2.classes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Public class for managing some info from OMDB API (http://www.omdbapi.com).
 */
public class OMDB implements Parcelable {
    private String rated;
    private String awards;
    private String dvd;
    private Double imdb_vote_average;
    private Double rt_vote_average;
    private Double mc_vote_average;

    /**
     * Constructor for objects of this class.
     *
     * @param rated             is the PG rating of the movie.
     * @param awards            is the string with the awards of this movie.
     * @param dvd               is the DVD release date.
     * @param imdb_vote_average is the users score in IMDB for this movie.
     * @param rt_vote_average   is the users score in Rotten Tomatoes for this movie.
     * @param mc_vote_average   is the users score in Metacritic for this movie.
     */
    public OMDB(String rated, String awards, String dvd, Double imdb_vote_average,
                Double rt_vote_average, Double mc_vote_average) {
        this.rated = rated;
        this.awards = awards;
        this.dvd = dvd;
        this.imdb_vote_average = imdb_vote_average;
        this.rt_vote_average = rt_vote_average;
        this.mc_vote_average = mc_vote_average;
    }

    // Getters and setters.

    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public String getAwards() {
        return awards;
    }

    public void setAwards(String awards) {
        this.awards = awards;
    }

    public String getDvd() {
        return dvd;
    }

    public void setDvd(String dvd) {
        this.dvd = dvd;
    }

    public Double getImdb_vote_average() {
        return imdb_vote_average;
    }

    public void setImdb_vote_average(Double imdb_vote_average) {
        this.imdb_vote_average = imdb_vote_average;
    }

    public Double getRt_vote_average() {
        return rt_vote_average;
    }

    public void setRt_vote_average(Double rt_vote_average) {
        this.rt_vote_average = rt_vote_average;
    }

    public Double getMc_vote_average() {
        return mc_vote_average;
    }

    public void setMc_vote_average(Double mc_vote_average) {
        this.mc_vote_average = mc_vote_average;
    }

    // Parcelable configuration.

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.rated);
        dest.writeString(this.awards);
        dest.writeString(this.dvd);
        dest.writeValue(this.imdb_vote_average);
        dest.writeValue(this.rt_vote_average);
        dest.writeValue(this.mc_vote_average);
    }

    protected OMDB(Parcel in) {
        this.rated = in.readString();
        this.awards = in.readString();
        this.dvd = in.readString();
        this.imdb_vote_average = (Double) in.readValue(Double.class.getClassLoader());
        this.rt_vote_average = (Double) in.readValue(Double.class.getClassLoader());
        this.mc_vote_average = (Double) in.readValue(Double.class.getClassLoader());
    }

    public static final Parcelable.Creator<OMDB> CREATOR = new Parcelable.Creator<OMDB>() {
        @Override
        public OMDB createFromParcel(Parcel source) {
            return new OMDB(source);
        }

        @Override
        public OMDB[] newArray(int size) {
            return new OMDB[size];
        }
    };
}
