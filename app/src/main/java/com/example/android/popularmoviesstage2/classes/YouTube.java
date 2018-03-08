package com.example.android.popularmoviesstage2.classes;

/**
 * Public class with static resources for fetching data from YouTube.
 */
public class YouTube {
    private final static String TAG = YouTube.class.getSimpleName();

    // URLs.
    public final static String YOUTUBE_SEARCH_VIDEOS_URL = "https://www.youtube.com/results?search_query=";
    public final static String YOUTUBE_WATCH_VIDEO_URL = "https://youtube.com/watch?v=";
    public final static String YOUTUBE_VIDEO_PREVIEW_URL = "https://img.youtube.com/vi/";
    public final static String YOUTUBE_VIDEO_MQDEFAULT_IMAGE = "/mqdefault.jpg";

    /**
     * Create a private constructor because no one should ever create a {@link YouTube} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name YouTube (and an object instance of NetworkUtils is not
     * needed).
     */
    private YouTube() {
    }
}
