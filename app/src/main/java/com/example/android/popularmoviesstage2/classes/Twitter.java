package com.example.android.popularmoviesstage2.classes;

/**
 * Public class with static resources for fetching data from Twitter.
 */
public class Twitter {
    private final static String TAG = Twitter.class.getSimpleName();

    // URLs.
    public final static String TWITTER_BASE_URL = "https://twitter.com/";

    /**
     * Create a private constructor because no one should ever create a {@link Twitter} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name Twitter (and an object instance of NetworkUtils is not
     * needed).
     */
    private Twitter() {
    }
}
