package com.example.android.popularmoviesstage2.classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Public class for managing the media info (tmdbVideos, posters and backdrops) of a movie.
 */
public class TmdbMedia implements Parcelable {
    private int id;
    private ArrayList<TmdbVideo> tmdbVideos;
    private ArrayList<TmdbImage> posters;
    private ArrayList<TmdbImage> backdrops;

    /**
     * Public constructor for new instances of objects of this class.
     *
     * @param id        is the unique identifier of the {@link TmdbMovie}.
     * @param tmdbVideos    is an array of {@link TmdbVideo} objects, the list of tmdbVideos related to the
     *                  movie.
     * @param posters   is an array of {@link TmdbImage} objects, the list of posters related to the
     *                  movie.
     * @param backdrops is an array of {@link TmdbImage} objects, the list of backdrops related to the
     *                  movie.
     */
    TmdbMedia(int id, ArrayList<TmdbVideo> tmdbVideos, ArrayList<TmdbImage> posters, ArrayList<TmdbImage> backdrops) {
        this.id = id;
        this.tmdbVideos = tmdbVideos;
        this.posters = posters;
        this.backdrops = backdrops;
    }

    // Getters and setters.

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<TmdbVideo> getTmdbVideos() {
        return tmdbVideos;
    }

    public void setTmdbVideos(ArrayList<TmdbVideo> tmdbVideos) {
        this.tmdbVideos = tmdbVideos;
    }

    public ArrayList<TmdbImage> getPosters() {
        return posters;
    }

    public void setPosters(ArrayList<TmdbImage> posters) {
        this.posters = posters;
    }

    public ArrayList<TmdbImage> getBackdrops() {
        return backdrops;
    }

    public void setBackdrops(ArrayList<TmdbImage> backdrops) {
        this.backdrops = backdrops;
    }

    // Parcelable configuration.

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeTypedList(this.tmdbVideos);
        dest.writeTypedList(this.posters);
        dest.writeTypedList(this.backdrops);
    }

    protected TmdbMedia(Parcel in) {
        this.id = in.readInt();
        this.tmdbVideos = in.createTypedArrayList(TmdbVideo.CREATOR);
        this.posters = in.createTypedArrayList(TmdbImage.CREATOR);
        this.backdrops = in.createTypedArrayList(TmdbImage.CREATOR);
    }

    public static final Parcelable.Creator<TmdbMedia> CREATOR = new Parcelable.Creator<TmdbMedia>() {
        @Override
        public TmdbMedia createFromParcel(Parcel source) {
            return new TmdbMedia(source);
        }

        @Override
        public TmdbMedia[] newArray(int size) {
            return new TmdbMedia[size];
        }
    };
}
