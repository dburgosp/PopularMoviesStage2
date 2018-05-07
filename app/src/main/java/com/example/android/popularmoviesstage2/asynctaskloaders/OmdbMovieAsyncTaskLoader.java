package com.example.android.popularmoviesstage2.asynctaskloaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.os.OperationCanceledException;
import android.util.Log;

import com.example.android.popularmoviesstage2.classes.Omdb;
import com.example.android.popularmoviesstage2.classes.OmdbMovie;

public class OmdbMovieAsyncTaskLoader extends AsyncTaskLoader<OmdbMovie> {
    private final String TAG = OmdbMovieAsyncTaskLoader.class.getSimpleName();
    private OmdbMovie omdbMovie;
    private String imdbId;

    /**
     * Constructor for this class.
     *
     * @param context is the context of the activity.
     * @param imdbId  is the IMDB identifier of the movie.
     */
    public OmdbMovieAsyncTaskLoader(Context context, String imdbId) {
        super(context);
        this.imdbId = imdbId;
    }

    /**
     * Subclasses must implement this to take care of loading their data,
     * as per {@link #startLoading()}. This is not called by clients directly,
     * but as a result of a call to {@link #startLoading()}.
     */
    @Override
    protected void onStartLoading() {
        if (omdbMovie != null) {
            Log.i(TAG, "(onStartLoading) Reload existing results.");
            deliverResult(omdbMovie);
        } else {
            Log.i(TAG, "(onStartLoading) Load new results.");
            forceLoad();
        }
    }

    /**
     * Called on a worker thread to perform the actual load and to return
     * the result of the load operation.
     * <p>
     * Implementations should not deliver the result directly, but should return them
     * from this method, which will eventually end up calling {@link #deliverResult} on
     * the UI thread.  If implementations need to process the results on the UI thread
     * they may override {@link #deliverResult} and do so there.
     * <p>
     * To support cancellation, this method should periodically check the value of
     * {@link #isLoadInBackgroundCanceled} and terminate when it returns true.
     * Subclasses may also override {@link #cancelLoadInBackground} to interrupt the load
     * directly instead of polling {@link #isLoadInBackgroundCanceled}.
     * <p>
     * When the load is canceled, this method may either return normally or throw
     * {@link OperationCanceledException}.  In either case, the {@link Loader} will
     * call {@link #onCanceled} to perform post-cancellation cleanup and to dispose of the
     * result object, if any.
     *
     * @return The result of the load operation.
     * @throws OperationCanceledException if the load is canceled during execution.
     * @see #isLoadInBackgroundCanceled
     * @see #cancelLoadInBackground
     * @see #onCanceled
     */
    @Override
    public OmdbMovie loadInBackground() {
        if (imdbId == null || imdbId.equals("")) {
            Log.i(TAG, "(loadInBackground) No IMDB identifier to search");
            return null;
        }

        // Perform the network request, parse the response, and extract results.
        Log.i(TAG, "(loadInBackground) IMDB identifier od the movie: " + imdbId);
        return Omdb.getOmdbMovie(imdbId);
    }

    /**
     * Sends the result of the load to the registered listener. Should only be called by subclasses.
     * <p>
     * Must be called from the process's main_menu thread.
     *
     * @param data the result of the load
     */
    @Override
    public void deliverResult(OmdbMovie data) {
        if (data == null)
            Log.i(TAG, "(deliverResult) No results to deliver.");
        else
            Log.i(TAG, "(deliverResult) Result delivered.");
        omdbMovie = data;
        super.deliverResult(data);
    }
}
