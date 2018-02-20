package com.example.android.popularmoviesstage2.classes;

import java.util.ArrayList;

/**
 * Public class for managing the releases of a movie.
 */
public class TmdbRelease {
    // Available release status for movies.
    public final static String TMDB_RELEASE_STATUS_RUMORED = "Rumored";
    public final static String TMDB_RELEASE_STATUS_PLANNED = "Planned";
    public final static String TMDB_RELEASE_STATUS_IN_PRODUCTION = "In Production";
    public final static String TMDB_RELEASE_STATUS_POST_PRODUCTION = "Post Production";
    public final static String TMDB_RELEASE_STATUS_RELEASED = "Released";
    public final static String TMDB_RELEASE_STATUS_CANCELED = "Canceled";

    // Release types.
    public static final int TMDB_RELEASE_TYPE_PREMIERE = 1;
    public static final int TMDB_RELEASE_TYPE_THEATRICAL_LIMITED = 2;
    public static final int TMDB_RELEASE_TYPE_THEATRICAL = 3;
    public static final int TMDB_RELEASE_TYPE_DIGITAL = 4;
    public static final int TMDB_RELEASE_TYPE_PHYSICAL = 5;
    public static final int TMDB_RELEASE_TYPE_TV = 6;

    // Private variables.
    private String iso_3166_1;
    private ArrayList<TmdbReleaseDate> releaseDateArrayList;

    /**
     * Constructor for objects of this class.
     *
     * @param iso_3166_1           is the country in which the movie is released.
     * @param releaseDateArrayList is the array of {@link TmdbReleaseDate} objects with all the
     *                             releases in the given country.
     */
    public TmdbRelease(String iso_3166_1, ArrayList<TmdbReleaseDate> releaseDateArrayList) {
        this.iso_3166_1 = iso_3166_1;
        this.releaseDateArrayList = releaseDateArrayList;
    }

    // Getters and setters.

    public String getIso_3166_1() {
        return iso_3166_1;
    }

    public void setIso_3166_1(String iso_3166_1) {
        this.iso_3166_1 = iso_3166_1;
    }

    public ArrayList<TmdbReleaseDate> getReleaseDateArrayList() {
        return releaseDateArrayList;
    }

    public void setReleaseDateArrayList(ArrayList<TmdbReleaseDate> releaseDateArrayList) {
        this.releaseDateArrayList = releaseDateArrayList;
    }
}
