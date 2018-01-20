package com.example.android.popularmoviesstage2.classes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Public class for managing the videos related to a movie.
 */
public class TmdbVideo implements Parcelable {
    private String id;
    private String iso_639_1;
    private String iso_3166_1;
    private String key;
    private String name;
    private String site;
    private int size;
    private String type;

    /**
     * @param id         is the unique identifier of the video.
     * @param iso_639_1  is the ISO 639-1 code for the country of the video.
     * @param iso_3166_1 is the ISO 3166-1 code for the language of the video.
     * @param key        is the path for appending to the YouTube base url to get the video file.
     * @param name       is the name of the video.
     * @param site       is the site where the video file is hosted. Allowed values: YouTube.
     * @param size       is the vertical resolution of the video. Allowed Values: 360, 480, 720, 1080.
     * @param type       is type of the video. Allowed Values: Trailer, Teaser, Clip, Featurette.
     */
    public TmdbVideo(String id, String iso_639_1, String iso_3166_1, String key, String name,
                     String site, int size, String type) {
        this.id = id;
        this.iso_639_1 = iso_639_1;
        this.iso_3166_1 = iso_3166_1;
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
        this.type = type;
    }

    // Getters and setters.

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIso_639_1() {
        return iso_639_1;
    }

    public void setIso_639_1(String iso_639_1) {
        this.iso_639_1 = iso_639_1;
    }

    public String getIso_3166_1() {
        return iso_3166_1;
    }

    public void setIso_3166_1(String iso_3166_1) {
        this.iso_3166_1 = iso_3166_1;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // Parcelable configuration.

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.iso_639_1);
        dest.writeString(this.iso_3166_1);
        dest.writeString(this.key);
        dest.writeString(this.name);
        dest.writeString(this.site);
        dest.writeInt(this.size);
        dest.writeString(this.type);
    }

    protected TmdbVideo(Parcel in) {
        this.id = in.readString();
        this.iso_639_1 = in.readString();
        this.iso_3166_1 = in.readString();
        this.key = in.readString();
        this.name = in.readString();
        this.site = in.readString();
        this.size = in.readInt();
        this.type = in.readString();
    }

    public static final Parcelable.Creator<TmdbVideo> CREATOR = new Parcelable.Creator<TmdbVideo>() {
        @Override
        public TmdbVideo createFromParcel(Parcel source) {
            return new TmdbVideo(source);
        }

        @Override
        public TmdbVideo[] newArray(int size) {
            return new TmdbVideo[size];
        }
    };
}
