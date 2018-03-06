package com.example.android.popularmoviesstage2.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.WindowManager;

import com.example.android.popularmoviesstage2.R;

public class DisplayUtils {
    private static final String TAG = DisplayUtils.class.getSimpleName();

    // Width and height in pixels for posters in the full movies list without any margin at all,
    // depending on display dimensions, current device orientation and desired number of elements to
    // be displayed for each column ("vertical_span_count" and "horizontal_span_count" defined in
    // dimens.xml).
    private int fullListPosterWidthPixels;
    private int fullListPosterHeightPixels;

    // Width and height in pixels for backdrops displaying on full screen with a regular_padding.
    private int fullDisplayBackdropWidthPixels;
    private int fullDisplayBackdropHeightPixels;

    // Number of column elements to be displayed in a vertical GridLayoutManager showing the main
    // list of movies.
    private int spanCount;

    /**
     * Constructor for objects of class DisplayUtils.
     *
     * @param context is the context for getting display information.
     */
    public DisplayUtils(Context context) {
        int displayWidthPixels, displayHeightPixels, displayDensityDpi;
        float displayDensity, displayScaledDensity, displayXdpi, displayYdpi;
        Resources resources = context.getResources();

        // Get current display metrics.
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager =
                (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        try {
            windowManager.getDefaultDisplay().getMetrics(metrics);

            displayWidthPixels = metrics.widthPixels;
            displayHeightPixels = metrics.heightPixels;
            displayDensityDpi = metrics.densityDpi;
            displayDensity = metrics.density;
            displayScaledDensity = metrics.scaledDensity;
            displayXdpi = metrics.xdpi;
            displayYdpi = metrics.ydpi;
        } catch (java.lang.NullPointerException e) {
            // If getDefaultDisplay() throws a NullPointerException, return default values.
            displayWidthPixels = resources.getInteger(R.integer.default_display_width_pixels);
            displayHeightPixels = resources.getInteger(R.integer.default_display_height_pixels);
            displayDensityDpi = resources.getInteger(R.integer.default_display_density_dpi);

            TypedValue typedValue = new TypedValue();

            resources.getValue(R.string.default_display_density, typedValue, true);
            displayDensity = Float.valueOf(typedValue.string.toString());

            resources.getValue(R.string.default_display_scaled_density, typedValue, true);
            displayScaledDensity = Float.valueOf(typedValue.string.toString());

            resources.getValue(R.string.default_display_xdpi, typedValue, true);
            displayXdpi = Float.valueOf(typedValue.string.toString());

            resources.getValue(R.string.default_display_ydpi, typedValue, true);
            displayYdpi = Float.valueOf(typedValue.string.toString());

            Log.e(TAG, "Error while getting default display: " + e);
        }

        Log.i(TAG, "density: " + displayDensity);
        Log.i(TAG, "densityDpi: " + displayDensityDpi);
        Log.i(TAG, "scaledDensity: " + displayScaledDensity);
        Log.i(TAG, "xdpi, ydpi: " + displayXdpi + ", " + displayYdpi);
        Log.i(TAG, "displayWidthPixels: " + displayWidthPixels);
        Log.i(TAG, "displayHeightPixels: " + displayHeightPixels);

        // Height and width in pixels for the vertical GridLayoutManager displaying movie posters.
        if (resources.getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // When display orientation is portrait, we use screen width for setting the width of
            // the photographs.
            spanCount = resources.getInteger(R.integer.default_vertical_span_count);
            Log.i(TAG, "Portrait orientation");
        } else {
            // When display orientation is landscape, we use screen height for setting the width of
            // the photographs.
            spanCount = resources.getInteger(R.integer.default_horizontal_span_count);
            Log.i(TAG, "Landscape orientation");
        }

        // Number of columns for the movies list in the RecyclerView (spanCount) is different
        // depending on device rotation, so poster width and height for this list also depend on
        // device rotation.
        fullListPosterWidthPixels = (displayWidthPixels - 4) / spanCount;
        fullListPosterHeightPixels = 3 * fullListPosterWidthPixels / 2;
        Log.i(TAG, "Poster width for movies full list with no margins: " +
                fullListPosterWidthPixels);
        Log.i(TAG, "Poster height for movies full list with no margins: " +
                fullListPosterHeightPixels);

        // Sizes for backdrops.
        fullDisplayBackdropWidthPixels = displayWidthPixels;
        fullDisplayBackdropHeightPixels = fullDisplayBackdropWidthPixels * 9 / 16;
        Log.i(TAG, "Backdrop width in full size with regular_padding: " +
                fullDisplayBackdropWidthPixels);
        Log.i(TAG, "Backdrop height in full size with regular_padding: " +
                fullDisplayBackdropHeightPixels);
    }

    // Getters.

    public int getFullListPosterWidthPixels() {
        return fullListPosterWidthPixels;
    }

    public int getFullListPosterHeightPixels() {
        return fullListPosterHeightPixels;
    }

    public int getFullDisplayBackdropWidthPixels() {
        return fullDisplayBackdropWidthPixels;
    }

    public int getFullDisplayBackdropHeightPixels() {
        return fullDisplayBackdropHeightPixels;
    }

    public int getSpanCount() {
        return spanCount;
    }
}
