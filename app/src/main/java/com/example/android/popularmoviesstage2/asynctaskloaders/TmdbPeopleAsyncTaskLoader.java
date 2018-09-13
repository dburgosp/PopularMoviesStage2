package com.example.android.popularmoviesstage2.asynctaskloaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.os.OperationCanceledException;
import android.util.Log;

import com.example.android.popularmoviesstage2.classes.Tmdb;
import com.example.android.popularmoviesstage2.classes.TmdbPerson;

import java.util.ArrayList;

public class TmdbPeopleAsyncTaskLoader extends AsyncTaskLoader<ArrayList<TmdbPerson>> {
    private final String TAG = TmdbPeopleAsyncTaskLoader.class.getSimpleName();
    private ArrayList<TmdbPerson> personArrayList;
    private String contentType, language;
    private int currentPage;
    private ArrayList<Integer> values = new ArrayList<>();

    /**
     * Constructor for this class.
     *
     * @param context     is the context of the activity.
     * @param contentType is the sort order of the movies_menu list.
     * @param currentPage is the current page to fetch results from TMDB.
     * @param language    is the language for retrieving results from TMDB.
     */
    public TmdbPeopleAsyncTaskLoader(Context context, String contentType, int currentPage,
                                     String language) {
        super(context);
        this.contentType = contentType;
        this.currentPage = currentPage;
        this.language = language;
    }

    /**
     * Subclasses must implement this to take care of loading their data,
     * as per {@link #startLoading()}. This is not called by clients directly,
     * but as a result of a call to {@link #startLoading()}.
     */
    @Override
    protected void onStartLoading() {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        if (personArrayList != null) {
            Log.i(TAG + "." + methodName, "Reload existing results.");
            deliverResult(personArrayList);
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
    public ArrayList<TmdbPerson> loadInBackground() {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        if (currentPage <= Tmdb.TMDB_MAX_PAGES && currentPage > 0) {
            // Perform the network request, parse the response, and extract results.
            Log.i(TAG + "." + methodName, "Sort by: " + contentType + ". Page number: " +
                    currentPage);
            return Tmdb.getTmdbPeople(currentPage, language);
        } else {
            Log.i(TAG + "." + methodName, "Wrong parameters.");
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
    public void deliverResult(ArrayList<TmdbPerson> data) {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        if (data == null)
            Log.i(TAG + "." + methodName, "No results to deliver.");
        else
            Log.i(TAG + "." + methodName, "" + data.size() + " element(s) delivered.");
        personArrayList = data;
        super.deliverResult(data);
    }
}
