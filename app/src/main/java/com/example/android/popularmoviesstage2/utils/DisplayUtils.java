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

    // Width and height in pixels for posters in the main movie list, depending on display
    // dimensions, current device orientation and desired number of elements to be displayed for
    // each column ("vertical_span_count" and "horizontal_span_count" defined in dimens.xml).
    private int listPosterWidthPixels;
    private int listPosterHeightPixels;

    // Width and height in pixels for posters in the main movie list, depending on display
    // dimensions, current device orientation and desired number of elements to be displayed for
    // each column ("vertical_span_count" and "horizontal_span_count" defined in dimens.xml).
    private int detailsPosterWidthPixels;
    private int detailsPosterHeightPixels;

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
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
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
            detailsPosterWidthPixels = (displayWidthPixels / 3) - ((resources.getDimensionPixelSize(R.dimen.regular_padding)) * 2);
            spanCount = (int) resources.getInteger(R.integer.default_vertical_span_count);
            Log.i(TAG, "Portrait orientation");
        } else {
            // When display orientation is landscape, we use screen height for setting the width of
            // the photographs.
            detailsPosterWidthPixels = (displayHeightPixels / 3) - ((resources.getDimensionPixelSize(R.dimen.regular_padding)) * 2);
            spanCount = (int) resources.getInteger(R.integer.default_horizontal_span_count);
            Log.i(TAG, "Landscape orientation");
        }
        detailsPosterHeightPixels = 3 * detailsPosterWidthPixels / 2;
        Log.i(TAG, "Poster width for details activity: " + detailsPosterWidthPixels);
        Log.i(TAG, "Poster height for details activity: " + detailsPosterHeightPixels);

        // Number of columns for the movies list in the RecyclerView (spanCount) is different
        // depending on device rotation, so poster width and height for this list also depend on
        // device rotation.
        listPosterWidthPixels = displayWidthPixels / spanCount;
        listPosterHeightPixels = 3 * listPosterWidthPixels / 2;
        Log.i(TAG, "Poster width for movies list: " + listPosterWidthPixels);
        Log.i(TAG, "Poster height for movies list: " + listPosterHeightPixels);
    }

    // Getters.

    public int getListPosterWidthPixels() {
        return listPosterWidthPixels;
    }

    public int getListPosterHeightPixels() {
        return listPosterHeightPixels;
    }

    public int getDetailsPosterWidthPixels() {
        return detailsPosterWidthPixels;
    }

    public int getDetailsPosterHeightPixels() {
        return detailsPosterHeightPixels;
    }

    public int getSpanCount() {
        return spanCount;
    }
}
