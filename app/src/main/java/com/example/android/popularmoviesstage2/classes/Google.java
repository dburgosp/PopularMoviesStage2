package com.example.android.popularmoviesstage2.classes;

/**
 * Public class with static resources for fetching data from Google.
 */
public class Google {
    private final static String TAG = Google.class.getSimpleName();

    // URLs.
    public final static String GOOGLE_LOOK_FOR_IMAGES_URL = "https://www.google.com/search?tbm=isch&q=";

    /**
     * Create a private constructor because no one should ever create a {@link Google} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name Google (and an object instance of NetworkUtils is not
     * needed).
     */
    private Google() {
    }
}