package com.example.android.popularmoviesstage2.asynctaskloaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.os.OperationCanceledException;
import android.util.Log;

import com.example.android.popularmoviesstage2.classes.Tmdb;
import com.example.android.popularmoviesstage2.classes.TmdbMovie;

import java.util.ArrayList;

public class TmdbMoviesAsyncTaskLoader extends AsyncTaskLoader<ArrayList<TmdbMovie>> {
    private final String TAG = TmdbMoviesAsyncTaskLoader.class.getSimpleName();
    private ArrayList<TmdbMovie> moviesArray;
    private String contentType, language, region, certification, sortBy, releaseType,
            initDate, endDate;
    private int voteCount, currentPage;
    private Double voteAverage;
    private ArrayList<Integer> values = new ArrayList<>();

    /**
     * Constructor for this class.
     *
     * @param context       is the context of the activity.
     * @param contentType   is the content type to fetch from TMDB.
     * @param values        is the list of possible values to assign to the contentType parameter.
     * @param currentPage   is the current page to fetch results from TMDB.
     * @param language      is the language for retrieving results from TMDB.
     * @param region        is the region for retrieving results from TMDB.
     * @param sortBy        is the sort order for the list of results.
     * @param certification is the minimum age rating of the movies_menu in the list for the current
     *                      country (region parameter).
     * @param voteCount     is the minimum number of users votes of the movies_menu in the list.
     * @param voteAverage   is the minimum users rating of the movies_menu in the list.
     * @param releaseType   is the value or list of values to filter release types by.
     */
    public TmdbMoviesAsyncTaskLoader(Context context, String contentType, ArrayList<Integer> values,
                                     int currentPage, String language, String region, String sortBy,
                                     String certification, int voteCount, Double voteAverage,
                                     String releaseType, String initDate, String endDate) {
        super(context);
        this.contentType = contentType;
        this.values = values;
        this.currentPage = currentPage;
        this.language = language;
        this.region = region;
        this.sortBy = sortBy;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.certification = certification;
        this.releaseType = releaseType;
        this.initDate = initDate;
        this.endDate = endDate;
    }

    /**
     * Subclasses must implement this to take care of loading their data,
     * as per {@link #startLoading()}. This is not called by clients directly,
     * but as a result of a call to {@link #startLoading()}.
     */
    @Override
    protected void onStartLoading() {
        if (moviesArray != null) {
            Log.i(TAG, "(onStartLoading) Reload existing results.");
            deliverResult(moviesArray);
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
     * To support cancellation, this method should periodically check the values of
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
    public ArrayList<TmdbMovie> loadInBackground() {
        if (Tmdb.isAllowedSortOrder(contentType) &&
                currentPage <= Tmdb.TMDB_MAX_PAGES && currentPage > 0) {
            // Perform the network request, parse the response, and extract results.
            Log.i(TAG, "(loadInBackground) Sort by: " + contentType + ". Page number: " +
                    currentPage);
            return Tmdb.getTmdbMovies(contentType, values, currentPage, language, region, sortBy,
                    certification, voteCount, voteAverage, releaseType, initDate, endDate);
        } else {
            Log.i(TAG, "(loadInBackground) Wrong parameters.");
            return null;
        }
    }

    /**
     * Sends the result of the load to the registered listener. Should only be called by subclasses.
     * <p>
     * Must be called from the process's main_menu thread.
     *
     * @param data the result of the load
     */
    @Override
    public void deliverResult(ArrayList<TmdbMovie> data) {
        if (data == null)
            Log.i(TAG, "(deliverResult) No results to deliver.");
        else
            Log.i(TAG, "(deliverResult) " + data.size() + " element(s) delivered.");
        moviesArray = data;
        super.deliverResult(data);
    }
}
