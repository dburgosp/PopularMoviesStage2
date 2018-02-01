package com.example.android.popularmoviesstage2.classes;

/**
 * Public class with static resources for fetching data from Instagram.
 */
public class Instagram {
    private final static String TAG = Instagram.class.getSimpleName();

    // URLs.
    public final static String INSTAGRAM_BASE_URL = "https://www.instagram.com";

    /**
     * Create a private constructor because no one should ever create a {@link Instagram} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name Instagram (and an object instance of NetworkUtils is not
     * needed).
     */
    private Instagram() {
    }
}
