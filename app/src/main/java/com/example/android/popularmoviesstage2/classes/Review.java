package com.example.android.popularmoviesstage2.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable {
    private String id;
    private String author;
    private String content;
    private String url;
    private String movieTitle;
    private String posterPath;
    private String backdropPath;

    /**
     * Constructor for this class.
     *
     * @param id           is the unique identifier of this review.
     * @param author       is the name of the author of the review.
     * @param content      is the content of the review.
     * @param url          is the complete url with the review at TMDB website.
     * @param movieTitle   is the title of the movie.
     * @param posterPath   is the path to append to the base url for getting the movie poster image.
     * @param backdropPath is the path to append to the base url for getting the movie backdrop
     *                     image.
     */
    public Review(String id, String author, String content, String url, String movieTitle, String posterPath, String backdropPath) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.url = url;
        this.movieTitle = movieTitle;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
    }

    // Getters and setters.

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getbackdropPath() {
        return backdropPath;
    }

    public void setbackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    // Parcelable configuration.

    protected Review(Parcel in) {
        this.id = in.readString();
        this.author = in.readString();
        this.content = in.readString();
        this.url = in.readString();
        this.movieTitle = in.readString();
        this.posterPath = in.readString();
        this.backdropPath = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.author);
        dest.writeString(this.content);
        dest.writeString(this.url);
        dest.writeString(this.movieTitle);
        dest.writeString(this.posterPath);
        dest.writeString(this.backdropPath);
    }

    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}
