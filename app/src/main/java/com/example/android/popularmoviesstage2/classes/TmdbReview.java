package com.example.android.popularmoviesstage2.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class TmdbReview implements Parcelable {
    private String id;
    private String author;
    private String content;
    private String url;
    private String movieTitle;
    private String posterPath;
    private String backdropPath;
    private int page;
    private int position;
    private int total_pages;

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
     * @param page         is the current page of this review into the set of results.
     * @param position     is the position of this review into its page.
     * @param total_pages  is the number of pages containing rewievs for the current movie.
     */
    public TmdbReview(String id, String author, String content, String url, String movieTitle,
                      String posterPath, String backdropPath, int page, int position, int total_pages) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.url = url;
        this.movieTitle = movieTitle;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.page = page;
        this.position = position;
        this.total_pages = total_pages;
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

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
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
        dest.writeInt(this.page);
        dest.writeInt(this.position);
        dest.writeInt(this.total_pages);
    }

    protected TmdbReview(Parcel in) {
        this.id = in.readString();
        this.author = in.readString();
        this.content = in.readString();
        this.url = in.readString();
        this.movieTitle = in.readString();
        this.posterPath = in.readString();
        this.backdropPath = in.readString();
        this.page = in.readInt();
        this.position = in.readInt();
        this.total_pages = in.readInt();
    }

    public static final Parcelable.Creator<TmdbReview> CREATOR = new Parcelable.Creator<TmdbReview>() {
        @Override
        public TmdbReview createFromParcel(Parcel source) {
            return new TmdbReview(source);
        }

        @Override
        public TmdbReview[] newArray(int size) {
            return new TmdbReview[size];
        }
    };
}
