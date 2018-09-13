package com.example.android.popularmoviesstage2.asynctaskloaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.os.OperationCanceledException;
import android.util.Log;

import com.example.android.popularmoviesstage2.classes.Tmdb;
import com.example.android.popularmoviesstage2.classes.TmdbCastCrew;

public class TmdbCastCrewAsyncTaskLoader extends AsyncTaskLoader<TmdbCastCrew> {
    private final String TAG = TmdbCastCrewAsyncTaskLoader.class.getSimpleName();
    private TmdbCastCrew tmdbCastCrew;
    private int movieId;

    /**
     * Constructor for this class.
     *
     * @param context is the context of the calling activity.
     * @param movieId is the unique identifier of the movie at TMDB.
     */
    public TmdbCastCrewAsyncTaskLoader(Context context, int movieId) {
        super(context);
        this.movieId = movieId;
    }

    /**
     * Subclasses must implement this to take care of loading their data,
     * as per {@link #startLoading()}. This is not called by clients directly,
     * but as a result of a call to {@link #startLoading()}.
     */
    @Override
    protected void onStartLoading() {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        if (tmdbCastCrew != null) {
            Log.i(TAG + "." + methodName, "Reload existing results.");
            deliverResult(tmdbCastCrew);
        } else {
            Log.i(TAG + "." + methodName, "Load new results.");
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
    public TmdbCastCrew loadInBackground() {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        if (movieId <= 0) {
            Log.i(TAG + "." + methodName, "Wrong parameters.");
            return null;
        }

        // Perform the network request, parse the response, and extract results.
        Log.i(TAG + "." + methodName, "Movie ID: " + movieId);
        return Tmdb.getTmdbCastAndCrew(movieId);
    }

    /**
     * Sends the result of the load to the registered listener. Should only be called by subclasses.
     * <p>
     * Must be called from the process's main_menu thread.
     *
     * @param data the result of the load
     */
    @Override
    public void deliverResult(TmdbCastCrew data) {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        if (data == null)
            Log.i(TAG + "." + methodName, "No results to deliver.");
        tmdbCastCrew = data;
        super.deliverResult(data);
    }
}