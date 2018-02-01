package com.example.android.popularmoviesstage2.classes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Public class for managing the external links (IMDB, Facebook, Instagram and Twitter) related to a
 * movie.
 */
public class TmdbExternalId implements Parcelable {
    private String imdb_id;
    private String facebook_id;
    private String instagram_id;
    private String twitter_id;

    /**
     * Public constructor for new instances of objects of this class.
     *
     * @param imdb_id      unique identifier to append to the IMDB base url.
     * @param facebook_id  unique identifier to append to the Facebook base url.
     * @param instagram_id unique identifier to append to the Instagram base url.
     * @param twitter_id   unique identifier to append to the Twitter base url.
     */
    public TmdbExternalId(String imdb_id, String facebook_id, String instagram_id, String twitter_id) {
        this.imdb_id = imdb_id;
        this.facebook_id = facebook_id;
        this.instagram_id = instagram_id;
        this.twitter_id = twitter_id;
    }

    // Getters and setters.

    public String getImdb_id() {
        return imdb_id;
    }

    public void setImdb_id(String imdb_id) {
        this.imdb_id = imdb_id;
    }

    public String getFacebook_id() {
        return facebook_id;
    }

    public void setFacebook_id(String facebook_id) {
        this.facebook_id = facebook_id;
    }

    public String getInstagram_id() {
        return instagram_id;
    }

    public void setInstagram_id(String instagram_id) {
        this.instagram_id = instagram_id;
    }

    public String getTwitter_id() {
        return twitter_id;
    }

    public void setTwitter_id(String twitter_id) {
        this.twitter_id = twitter_id;
    }

    // Parcelable configuration.

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imdb_id);
        dest.writeString(this.facebook_id);
        dest.writeString(this.instagram_id);
        dest.writeString(this.twitter_id);
    }

    protected TmdbExternalId(Parcel in) {
        this.imdb_id = in.readString();
        this.facebook_id = in.readString();
        this.instagram_id = in.readString();
        this.twitter_id = in.readString();
    }

    public static final Parcelable.Creator<TmdbExternalId> CREATOR = new Parcelable.Creator<TmdbExternalId>() {
        @Override
        public TmdbExternalId createFromParcel(Parcel source) {
            return new TmdbExternalId(source);
        }

        @Override
        public TmdbExternalId[] newArray(int size) {
            return new TmdbExternalId[size];
        }
    };
}
