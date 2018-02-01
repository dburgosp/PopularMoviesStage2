package com.example.android.popularmoviesstage2.classes;

/**
 * Public class with static resources for fetching data from Facebook.
 */
public class Facebook {
    private final static String TAG = Facebook.class.getSimpleName();

    // URLs.
    public final static String FACEBOOK_BASE_URL = "https://www.facebook.com";

    /**
     * Create a private constructor because no one should ever create a {@link Facebook} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name Facebook (and an object instance of NetworkUtils is not
     * needed).
     */
    private Facebook() {
    }
}
