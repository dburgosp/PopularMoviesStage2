package com.example.android.popularmoviesstage2.activities;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.asynctaskloaders.TmdbMoviesAsyncTaskLoader;
import com.example.android.popularmoviesstage2.classes.Tmdb;
import com.example.android.popularmoviesstage2.classes.TmdbMovie;
import com.example.android.popularmoviesstage2.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    // Annotate fields with @BindView and views ID for Butter Knife to find and automatically cast
    // the corresponding views.
    @BindView(R.id.home_all_movies_cardview)
    CardView allMoviesCardView;
    @BindView(R.id.home_all_series_cardview)
    CardView allSeriesCardView;
    @BindView(R.id.home_all_people_cardview)
    CardView allPeopleCardView;

    @BindView(R.id.home_upcoming_movies_viewflipper)
    ViewFlipper upcomingMoviesViewFlipper;
    @BindView(R.id.home_upcoming_movies_image)
    ImageView upcomingMoviesImageView;

    @BindView(R.id.connection_status_layout)
    LinearLayout connectionStatusLayout;
    @BindView(R.id.connection_status_text)
    TextView connectionStatusText;
    @BindView(R.id.connection_status_loading_indicator)
    ProgressBar connectionStatusLoadingIndicator;
    
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        // Initially clear the layout and instantiate classes for opening new
        // threads to retrieve data from TMDB.
        clearLayout();
        new MainActivity.MainActivityMoviesList(NetworkUtils.TMDB_NOW_PLAYING_MOVIES_LOADER_ID);
        new MainActivity.MainActivityMoviesList(NetworkUtils.TMDB_UPCOMING_MOVIES_LOADER_ID);

        Log.i(TAG, "(onCreate) Activity created");
    }

    /**
     * Called when an item in the navigation menu is selected.
     *
     * @param item The selected item
     * @return true to display the item as the selected item
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    /* -------------- */
    /* HELPER METHODS */
    /* -------------- */

    /**
     * Helper method to initially clear all the info sections in the layout.
     */
    void clearLayout() {
    }


    /* ----------- */
    /* INNER CLASS */
    /* ----------- */

    // Private inner class to retrieve a given list of movies.
    private class MainActivityMoviesList implements LoaderManager.LoaderCallbacks<ArrayList<TmdbMovie>> {
        private final String TAG = MainActivity.MainActivityMoviesList.class.getSimpleName();

        // Constructor for objects of this class.
        MainActivityMoviesList(int loaderId) {
            // Create an AsyncTaskLoader for retrieving the list of movies.
            if (loaderId >= 0)
                getSupportLoaderManager().initLoader(loaderId, null, this);
        }

        /* ------ */
        /* LOADER */
        /* ------ */

        /**
         * Instantiate and return a new Loader for the given ID.
         *
         * @param id   The ID whose loaderId is to be created.
         * @param args Any arguments supplied by the caller.
         * @return Return a new Loader instance that is ready to start loading.
         */
        @Override
        public Loader<ArrayList<TmdbMovie>> onCreateLoader(int id, Bundle args) {
            connectionStatusLayout.setVisibility(View.VISIBLE);
            connectionStatusText.setTextColor(getResources().getColor(R.color.colorWhite));
            connectionStatusText.setVisibility(View.VISIBLE);

            if (NetworkUtils.isConnected(MainActivity.this)) {
                // There is an available connection. Fetch results from TMDB.
                connectionStatusText.setText(getString(R.string.fetching_info));
                connectionStatusLoadingIndicator.setVisibility(View.VISIBLE);
                switch (id) {
                    case NetworkUtils.TMDB_NOW_PLAYING_MOVIES_LOADER_ID:
                        return new TmdbMoviesAsyncTaskLoader(MainActivity.this,
                                Tmdb.TMDB_CONTENT_TYPE_NOW_PLAYING, 1,
                                Locale.getDefault().getLanguage(),
                                Locale.getDefault().getCountry());
                    case NetworkUtils.TMDB_THIS_WEEK_RELEASES_MOVIES_LOADER_ID:
                        return new TmdbMoviesAsyncTaskLoader(MainActivity.this,
                                Tmdb.TMDB_CONTENT_TYPE_THIS_WEEK_RELEASES, 1,
                                Locale.getDefault().getLanguage(),
                                Locale.getDefault().getCountry());
                    default:
                        return new TmdbMoviesAsyncTaskLoader(MainActivity.this,
                                Tmdb.TMDB_CONTENT_TYPE_UPCOMING, 1,
                                Locale.getDefault().getLanguage(),
                                Locale.getDefault().getCountry());
                }
            } else {
                // There is no connection. Show error message.
                connectionStatusText.setText(getResources().getString(R.string.no_connection));
                connectionStatusLoadingIndicator.setVisibility(View.INVISIBLE);
                Log.i(TAG, "(onCreateLoader) No internet connection.");
                return null;
            }
        }

        /**
         * Called when a previously created loaderId has finished its load.  Note
         * that normally an application is <em>not</em> allowed to commit fragment
         * transactions while in this call, since it can happen after an
         * activity's state is saved.  See {@link FragmentManager#beginTransaction()
         * FragmentManager.openTransaction()} for further discussion on this.
         * <p>
         * <p>This function is guaranteed to be called prior to the release of
         * the last data that was supplied for this Loader.  At this point
         * you should remove all use of the old data (since it will be released
         * soon), but should not do your own release of the data since its Loader
         * owns it and will take care of that.  The Loader will take care of
         * management of its data so you don't have to.  In particular:
         * <p>
         * <ul>
         * <li> <p>The Loader will monitor for changes to the data, and report
         * them to you through new calls here.  You should not monitor the
         * data yourself.  For example, if the data is a {@link Cursor}
         * and you place it in a {@link CursorAdapter}, use
         * the {@link CursorAdapter(Context, Cursor, int)} constructor <em>without</em> passing
         * in either {@link CursorAdapter#FLAG_AUTO_REQUERY}
         * or {@link CursorAdapter#FLAG_REGISTER_CONTENT_OBSERVER}
         * (that is, use 0 for the flags argument).  This prevents the CursorAdapter
         * from doing its own observing of the Cursor, which is not needed since
         * when a change happens you will get a new Cursor throw another call
         * here.
         * <li> The Loader will release the data once it knows the application
         * is no longer using it.  For example, if the data is
         * a {@link Cursor} from a {@link CursorLoader},
         * you should not call close() on it yourself.  If the Cursor is being placed in a
         * {@link CursorAdapter}, you should use the
         * {@link CursorAdapter#swapCursor(Cursor)}
         * method so that the old Cursor is not closed.
         * </ul>
         *
         * @param loader The Loader that has finished.
         * @param data   The data generated by the Loader.
         */
        @Override
        public void onLoadFinished(Loader<ArrayList<TmdbMovie>> loader, ArrayList<TmdbMovie> data) {
            // Hide connection layout.
            connectionStatusLayout.setVisibility(View.GONE);

            // Check if there is an available connection.
            if (NetworkUtils.isConnected(MainActivity.this)) {
                // If there is a valid result, then update its data into the current {@link TmdbMovieDetails}
                // object.
                if (data != null && !data.isEmpty() && data.size() > 0) {
                    Log.i(TAG, "(onLoadFinished) Search results not null.");

                    // Get movies list and display it.
                    switch (loader.getId()) {
                        case NetworkUtils.TMDB_NOW_PLAYING_MOVIES_LOADER_ID: {
                            nowPlayingMovies = data;
                            setNowPlayingMovies();
                            break;
                        }
                        case NetworkUtils.TMDB_THIS_WEEK_RELEASES_MOVIES_LOADER_ID: {
                            thisWeekReleasesMovies = data;
                            setThisWeekReleasedMovies();
                            break;
                        }
                        default: {
                            upcomingMovies = data;
                            setUpcomingMovies();
                        }
                    }
                } else {
                    Log.i(TAG, "(onLoadFinished) No search results.");
                    connectionStatusText.setTextColor(getResources().getColor(R.color.colorWhite));
                    connectionStatusText.setText(getResources().getString(R.string.no_results));
                    connectionStatusText.setVisibility(View.VISIBLE);
                }
            } else

            {
                // There is no connection. Show error message.
                Log.i(TAG, "(onLoadFinished) No connection to internet.");
                connectionStatusText.setTextColor(getResources().getColor(R.color.colorWhite));
                connectionStatusText.setText(getResources().getString(R.string.no_connection));
                connectionStatusText.setVisibility(View.VISIBLE);
            }

        }

        /**
         * Called when a previously created loaderId is being reset, and thus
         * making its data unavailable.  The application should at this point
         * remove any references it has to the Loader's data.
         *
         * @param loader The Loader that is being reset.
         */
        @Override
        public void onLoaderReset(Loader<ArrayList<TmdbMovie>> loader) {
        }
    }
}
