package com.example.android.popularmoviesstage2.asynctaskloaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.os.OperationCanceledException;
import android.util.Log;

import com.example.android.popularmoviesstage2.classes.Omdb;
import com.example.android.popularmoviesstage2.classes.Tmdb;
import com.example.android.popularmoviesstage2.classes.TmdbMoviesParameters;

import java.util.ArrayList;

public class GenericAsyncTaskLoader extends AsyncTaskLoader<Object> {
    public static final int ASYNC_TASK_LOADER_TYPE_TMDB_MOVIES_LIST = 1;
    public static final int ASYNC_TASK_LOADER_TYPE_OMDB_MOVIE_DETAILS = 2;
    public static final int ASYNC_TASK_LOADER_TYPE_TMDB_CAST_CREW = 3;
    public static final int ASYNC_TASK_LOADER_TYPE_TMDB_MEDIA = 4;
    private final String TAG = GenericAsyncTaskLoader.class.getSimpleName();

    private Object mData;
    private String mContentType, mIMDBid;
    private int mCurrentPage, mMovieId;
    private ArrayList<Integer> mValues;
    private TmdbMoviesParameters mTMDBMoviesParameters;
    private int mAsyncTaskLoaderType;

    /**
     * Constructor for this class (fetching an ArrayList<TmdbMovie> from TMDB).
     *
     * @param context              is the context of the activity.
     * @param contentType          is the content type to fetch from TMDB.
     * @param values               is the list of possible mValues to assign to the mContentType
     *                             parameter.
     * @param currentPage          is the current page to fetch results from TMDB.
     * @param tmdbMoviesParameters are the parameters to append to the url for fetching the movies
     *                             list.
     * @param asyncTaskLoaderType  {@link GenericAsyncTaskLoader#ASYNC_TASK_LOADER_TYPE_TMDB_MOVIES_LIST}.
     */
    public GenericAsyncTaskLoader(Context context, String contentType, ArrayList<Integer> values,
                                  int currentPage, TmdbMoviesParameters tmdbMoviesParameters,
                                  int asyncTaskLoaderType) {
        super(context);
        mContentType = contentType;
        mValues = values;
        mCurrentPage = currentPage;
        mTMDBMoviesParameters = tmdbMoviesParameters;
        mAsyncTaskLoaderType = asyncTaskLoaderType;
    }

    /**
     * Constructor for this class (fetching an OmdbMovie from OMDB).
     *
     * @param context             is the context of the activity.
     * @param IMDBid              is the IMDB identifier of the movie.
     * @param asyncTaskLoaderType {@link GenericAsyncTaskLoader#ASYNC_TASK_LOADER_TYPE_OMDB_MOVIE_DETAILS}.
     */
    public GenericAsyncTaskLoader(Context context, String IMDBid, int asyncTaskLoaderType) {
        super(context);
        mIMDBid = IMDBid;
        mAsyncTaskLoaderType = asyncTaskLoaderType;
    }

    /**
     * Constructor for this class (fetching a TmdbCastCrew or a TmdbMedia object from TMDB).
     *
     * @param context             is the context of the calling activity.
     * @param movieId             is the unique identifier of the movie at TMDB.
     * @param asyncTaskLoaderType {@link GenericAsyncTaskLoader#ASYNC_TASK_LOADER_TYPE_TMDB_MEDIA},
     *                            {@link GenericAsyncTaskLoader#ASYNC_TASK_LOADER_TYPE_TMDB_CAST_CREW}.
     */
    public GenericAsyncTaskLoader(Context context, int movieId, int asyncTaskLoaderType) {
        super(context);
        mMovieId = movieId;
        mAsyncTaskLoaderType = asyncTaskLoaderType;
    }

    /**
     * Subclasses must implement this to take care of loading their data,
     * as per {@link #startLoading()}. This is not called by clients directly,
     * but as a result of a call to {@link #startLoading()}.
     */
    @Override
    protected void onStartLoading() {
        String methodTag = TAG + "." + Thread.currentThread().getStackTrace()[2].getMethodName();
        if (mData != null) {
            Log.i(methodTag, "Reload existing results.");
            deliverResult(mData);
        } else {
            Log.i(methodTag, "Load new results.");
            forceLoad();
        }
    }

    /**
     * Called on a worker thread to perform the actual load and to return the result of the load
     * operation.
     *
     * @return The result of the load operation.
     * @throws OperationCanceledException if the load is canceled during execution.
     * @see #isLoadInBackgroundCanceled
     * @see #cancelLoadInBackground
     * @see #onCanceled
     */
    @Override
    public Object loadInBackground() {
        String methodTag = TAG + "." + Thread.currentThread().getStackTrace()[2].getMethodName();
        switch (mAsyncTaskLoaderType) {
            case ASYNC_TASK_LOADER_TYPE_TMDB_MOVIES_LIST:
                // We are fetching an ArrayList<TmdbMovie> from TMDB.
                Log.i(methodTag, "Fetching an ArrayList<TmdbMovie> from TMDB...");
                if (Tmdb.isAllowedSortOrder(mContentType) &&
                        mCurrentPage <= Tmdb.TMDB_MAX_PAGES && mCurrentPage > 0) {
                    // Perform the network request, parse the response, and extract results.
                    Log.i(methodTag, "Sort by: " + mContentType + ". Page number: " +
                            mCurrentPage);
                    return Tmdb.getTmdbMovies(mContentType, mValues, mCurrentPage,
                            mTMDBMoviesParameters);
                } else {
                    Log.i(methodTag, "Wrong parameters.");
                    return null;
                }

            case ASYNC_TASK_LOADER_TYPE_OMDB_MOVIE_DETAILS:
                // We are fetching an OmdbMovie from OMDB.
                Log.i(methodTag, "Fetching an OmdbMovie object from OMDB...");
                if (mIMDBid != null && !mIMDBid.equals("")) {
                    // Perform the network request, parse the response, and extract results.
                    Log.i(methodTag, "IMDB identifier of the movie: " + mIMDBid);
                    return Omdb.getOmdbMovie(mIMDBid);
                } else {
                    Log.i(methodTag, "No IMDB identifier to search");
                    return null;
                }

            case ASYNC_TASK_LOADER_TYPE_TMDB_CAST_CREW:
                // We are fetching a TmdbCastCrew object from TMDB.
                Log.i(methodTag, "Fetching a TmdbCastCrew object from TMDB...");
                if (mMovieId > 0) {
                    // Perform the network request, parse the response, and extract results.
                    Log.i(methodTag, "Movie ID: " + mMovieId);
                    return Tmdb.getTmdbCastAndCrew(mMovieId);
                } else {
                    Log.i(methodTag, "Wrong TMDB movie ID.");
                    return null;
                }

            case ASYNC_TASK_LOADER_TYPE_TMDB_MEDIA:
                // We are fetching a TmdbMedia object from TMDB.
                Log.i(methodTag, "Fetching a TmdbMedia object from TMDB...");
                if (mMovieId > 0) {
                    // Perform the network request, parse the response, and extract results.
                    Log.i(methodTag, "Movie ID: " + mMovieId);
                    return Tmdb.getTmdbMedia(mMovieId);
                } else {
                    Log.i(methodTag, "Wrong TMDB movie ID.");
                    return null;
                }

            default:
                // Unexpected mAsyncTaskLoaderType value.
                Log.i(methodTag, "Wrong mAsyncTaskLoaderType value: " + mAsyncTaskLoaderType);
                return null;
        }
    }

    /**
     * Sends the result of the load to the registered listener. Should only be called by subclasses.
     * Must be called from the process's main thread.
     *
     * @param data the result of the load
     */
    @Override
    public void deliverResult(Object data) {
        String methodTag = TAG + "." + Thread.currentThread().getStackTrace()[2].getMethodName();
        if (data == null)
            Log.i(methodTag, "No results to deliver.");
        else
            Log.i(methodTag, "Results fetched and delivered.");
        mData = data;
        super.deliverResult(data);
    }
}
