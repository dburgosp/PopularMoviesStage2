package com.example.android.popularmoviesstage2.asynctaskloaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.os.OperationCanceledException;
import android.util.Log;

import com.example.android.popularmoviesstage2.classes.OMDB;
import com.example.android.popularmoviesstage2.utils.NetworkUtils;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.net.URL;

public class OMDBAsyncTaskLoader extends AsyncTaskLoader<OMDB> {
    private final String TAG = OMDBAsyncTaskLoader.class.getSimpleName();
    private OMDB omdb;
    private URL url;

    /**
     * Constructor for this class.
     *
     * @param context   is the context of the activity.
     * @param url       is the url to fetch.
     */
    public OMDBAsyncTaskLoader(Context context, URL url) {
        super(context);
        this.url = url;
    }

    /**
     * Subclasses must implement this to take care of loading their data,
     * as per {@link #startLoading()}. This is not called by clients directly,
     * but as a result of a call to {@link #startLoading()}.
     */
    @Override
    protected void onStartLoading() {
        if (omdb != null) {
            Log.i(TAG, "(onStartLoading) Reload existing results.");
            deliverResult(omdb);
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
    public OMDB loadInBackground() {
        if (url == null) {
            Log.i(TAG, "(loadInBackground) No URL to load.");
            return null;
        }

        // Perform the network request, parse the response, and extract results.
        Log.i(TAG, "(loadInBackground) URL: " + url.toString());
        return NetworkUtils.fetchOMDBinfo(url);
    }

    /**
     * Sends the result of the load to the registered listener. Should only be called by subclasses.
     * <p>
     * Must be called from the process's main thread.
     *
     * @param data the result of the load
     */
    @Override
    public void deliverResult(OMDB data) {
        if (data == null)
            Log.i(TAG, "(deliverResult) No results to deliver.");
        else
            Log.i(TAG, "(deliverResult) Result delivered.");
        omdb = data;
        super.deliverResult(data);
    }

    public OMDBAsyncTaskLoader(Context context) {
        super(context);
    }

    /**
     * Set amount to throttle updates by.  This is the minimum time from
     * when the last {@link #loadInBackground()} call has completed until
     * a new load is scheduled.
     *
     * @param delayMS Amount of delay, in milliseconds.
     */
    @Override
    public void setUpdateThrottle(long delayMS) {
        super.setUpdateThrottle(delayMS);
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
    }

    @Override
    protected boolean onCancelLoad() {
        return super.onCancelLoad();
    }

    /**
     * Called if the task was canceled before it was completed.  Gives the class a chance
     * to clean up post-cancellation and to properly dispose of the result.
     *
     * @param data The value that was returned by {@link #loadInBackground}, or null
     *             if the task threw {@link OperationCanceledException}.
     */
    @Override
    public void onCanceled(OMDB data) {
        super.onCanceled(data);
    }

    /**
     * Calls {@link #loadInBackground()}.
     * <p>
     * This method is reserved for use by the loader framework.
     * Subclasses should override {@link #loadInBackground} instead of this method.
     *
     * @return The result of the load operation.
     * @throws OperationCanceledException if the load is canceled during execution.
     * @see #loadInBackground
     */
    @Override
    protected OMDB onLoadInBackground() {
        return super.onLoadInBackground();
    }

    /**
     * Called on the main thread to abort a load in progress.
     * <p>
     * Override this method to abort the current invocation of {@link #loadInBackground}
     * that is running in the background on a worker thread.
     * <p>
     * This method should do nothing if {@link #loadInBackground} has not started
     * running or if it has already finished.
     *
     * @see #loadInBackground
     */
    @Override
    public void cancelLoadInBackground() {
        super.cancelLoadInBackground();
    }

    /**
     * Returns true if the current invocation of {@link #loadInBackground} is being canceled.
     *
     * @return True if the current invocation of {@link #loadInBackground} is being canceled.
     * @see #loadInBackground
     */
    @Override
    public boolean isLoadInBackgroundCanceled() {
        return super.isLoadInBackgroundCanceled();
    }

    /**
     * Locks the current thread until the loader completes the current load
     * operation. Returns immediately if there is no load operation running.
     * Should not be called from the UI thread: calling it from the UI
     * thread would cause a deadlock.
     * <p>
     * Use for testing only.  <b>Never</b> call this from a UI thread.
     *
     * @hide
     */
    @Override
    public void waitForLoader() {
        super.waitForLoader();
    }

    @Override
    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        super.dump(prefix, fd, writer, args);
    }
}
