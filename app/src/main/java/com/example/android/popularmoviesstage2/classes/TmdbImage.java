package com.example.android.popularmoviesstage2.classes;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Public class for managing the images (posters and backdrops) related to a movie.
 */
public class TmdbImage implements Parcelable {
    private Double aspect_ratio;
    private String file_path;
    private int height;
    private String iso_639_1;
    private int vote_average;
    private int vote_count;
    private int width;
    private int position; // Relative to the adapter that contains this image.

    /**
     * Public constructor for new instances of objects of this class.
     *
     * @param aspect_ratio is the aspect ratio of the image.
     * @param file_path    is the path to append to the base url for getting this image.
     * @param height       is the height of the file.
     * @param iso_639_1    is the ISO 639-1 code with the country related to this image.
     * @param vote_average is the user score for this image.
     * @param vote_count   is the number of people who have voted this image.
     * @param width        is the width of the image.
     */
    public TmdbImage(Double aspect_ratio, @NonNull String file_path, int height, String iso_639_1,
                     int vote_average, int vote_count, int width) {
        this.aspect_ratio = aspect_ratio;
        this.file_path = file_path;
        this.height = height;
        this.iso_639_1 = iso_639_1;
        this.vote_average = vote_average;
        this.vote_count = vote_count;
        this.width = width;
        this.position = 0;
    }

    // Getters and setters.

    public Double getAspect_ratio() {
        return aspect_ratio;
    }

    public void setAspect_ratio(Double aspect_ratio) {
        this.aspect_ratio = aspect_ratio;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getIso_639_1() {
        return iso_639_1;
    }

    public void setIso_639_1(String iso_639_1) {
        this.iso_639_1 = iso_639_1;
    }

    public int getVote_average() {
        return vote_average;
    }

    public void setVote_average(int vote_average) {
        this.vote_average = vote_average;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    // Parcelable configuration.

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.aspect_ratio);
        dest.writeString(this.file_path);
        dest.writeInt(this.height);
        dest.writeString(this.iso_639_1);
        dest.writeInt(this.vote_average);
        dest.writeInt(this.vote_count);
        dest.writeInt(this.width);
        dest.writeInt(this.position);
    }

    protected TmdbImage(Parcel in) {
        this.aspect_ratio = (Double) in.readValue(Double.class.getClassLoader());
        this.file_path = in.readString();
        this.height = in.readInt();
        this.iso_639_1 = in.readString();
        this.vote_average = in.readInt();
        this.vote_count = in.readInt();
        this.width = in.readInt();
        this.position = in.readInt();
    }

    public static final Parcelable.Creator<TmdbImage> CREATOR = new Parcelable.Creator<TmdbImage>() {
        @Override
        public TmdbImage createFromParcel(Parcel source) {
            return new TmdbImage(source);
        }

        @Override
        public TmdbImage[] newArray(int size) {
            return new TmdbImage[size];
        }
    };
}
