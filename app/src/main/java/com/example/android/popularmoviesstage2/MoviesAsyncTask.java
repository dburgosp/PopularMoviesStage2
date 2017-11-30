package com.example.android.popularmoviesstage2;

import android.os.AsyncTask;
import android.util.Log;

import com.example.android.popularmoviesstage2.utils.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

/*
 * Created by David on 02/10/2017.
 */

class MoviesAsyncTask extends AsyncTask<URL, Void, ArrayList<Movie>> {
    private final String TAG = MoviesAsyncTask.class.getSimpleName();
    private AsyncTaskCompleteListener<ArrayList<Movie>> listener;

    /**
     * Constructor for this class.
     *
     * @param listener a listener that implements the interface {@link AsyncTaskCompleteListener}.
     *                 It will manage the result of the AsyncTask when it is done on
     *                 {@link #onPostExecute}.
     */
    MoviesAsyncTask(AsyncTaskCompleteListener<ArrayList<Movie>> listener) {
        this.listener = listener;
        Log.i(TAG, "(MoviesAsyncTask) Async Task created.");
    }

    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param params The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected ArrayList<Movie> doInBackground(URL... params) {
        URL searchUrl = params[0];
        Log.i(TAG, "(doInBackground) URL: " + searchUrl);
        return NetworkUtils.fetchMovies(searchUrl);
    }

    /**
     * <p>Runs on the UI thread after {@link #doInBackground}. The
     * specified result is the value returned by {@link #doInBackground}.</p>
     * <p>
     * <p>This method won't be invoked if the task was cancelled.</p>
     *
     * @param searchResults The result of the operation computed by {@link #doInBackground}.
     * @see #onPreExecute
     * @see #doInBackground
     * @see #onCancelled(Object)
     */
    @Override
    protected void onPostExecute(ArrayList<Movie> searchResults) {
        super.onPostExecute(searchResults);
        listener.onTaskComplete(searchResults);
        Log.i(TAG, "(onPostExecute) Async Task completed.");
    }

    /**
     * This is a useful callback mechanism so we can abstract our AsyncTasks out into separate,
     * re-usable and testable classes yet still retain a hook back into the calling activity.
     * Basically, it'll make classes cleaner and easier to unit test.
     * <p>
     * For more information: http://www.jameselsey.co.uk/blogs/techblog/extracting-out-your-asynctasks-into-separate-classes-makes-your-code-cleaner/
     *
     * @param <T>
     */
    interface AsyncTaskCompleteListener<T> {
        /**
         * Invoked when the AsyncTask has completed its execution.
         *
         * @param result The resulting object from the AsyncTask.
         */
        void onTaskComplete(T result);
    }
}
