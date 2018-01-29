package com.example.android.popularmoviesstage2.classes;

/**
 * Public class with static resources for fetching data from IMDB.
 */
public class Imdb {
    private final static String TAG = Imdb.class.getSimpleName();

    // URLs.
    public final static String IMDB_BASE_URL = "http://www.imdb.com/title";

    /**
     * Create a private constructor because no one should ever create a {@link Imdb} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name NetworkUtils (and an object instance of NetworkUtils is not
     * needed).
     */
    private Imdb() {
    }
}
