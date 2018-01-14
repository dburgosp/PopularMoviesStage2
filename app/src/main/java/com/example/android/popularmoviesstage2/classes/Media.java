package com.example.android.popularmoviesstage2.classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Public class for managing the media info (videos, posters and backdrops) of a movie.
 */
public class Media implements Parcelable {
    private int id;
    private ArrayList<Video> videos;
    private ArrayList<Image> posters;
    private ArrayList<Image> backdrops;

    /**
     * Public constructor for new instances of objects of this class.
     *
     * @param id        is the unique identifier of the {@link Movie}.
     * @param videos    is an array of {@link Video} objects, the list of videos related to the
     *                  movie.
     * @param posters   is an array of {@link Image} objects, the list of posters related to the
     *                  movie.
     * @param backdrops is an array of {@link Image} objects, the list of backdrops related to the
     *                  movie.
     */
    public Media(int id, ArrayList<Video> videos, ArrayList<Image> posters, ArrayList<Image> backdrops) {
        this.id = id;
        this.videos = videos;
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

    public ArrayList<Video> getVideos() {
        return videos;
    }

    public void setVideos(ArrayList<Video> videos) {
        this.videos = videos;
    }

    public ArrayList<Image> getPosters() {
        return posters;
    }

    public void setPosters(ArrayList<Image> posters) {
        this.posters = posters;
    }

    public ArrayList<Image> getBackdrops() {
        return backdrops;
    }

    public void setBackdrops(ArrayList<Image> backdrops) {
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
        dest.writeTypedList(this.videos);
        dest.writeTypedList(this.posters);
        dest.writeTypedList(this.backdrops);
    }

    protected Media(Parcel in) {
        this.id = in.readInt();
        this.videos = in.createTypedArrayList(Video.CREATOR);
        this.posters = in.createTypedArrayList(Image.CREATOR);
        this.backdrops = in.createTypedArrayList(Image.CREATOR);
    }

    public static final Parcelable.Creator<Media> CREATOR = new Parcelable.Creator<Media>() {
        @Override
        public Media createFromParcel(Parcel source) {
            return new Media(source);
        }

        @Override
        public Media[] newArray(int size) {
            return new Media[size];
        }
    };
}
