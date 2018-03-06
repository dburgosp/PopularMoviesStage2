package com.example.android.popularmoviesstage2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.adapters.MoviesListAdapter;
import com.example.android.popularmoviesstage2.asynctaskloaders.TmdbMoviesAsyncTaskLoader;
import com.example.android.popularmoviesstage2.classes.Tmdb;
import com.example.android.popularmoviesstage2.classes.TmdbMovie;
import com.example.android.popularmoviesstage2.itemdecorations.SpaceItemDecoration;
import com.example.android.popularmoviesstage2.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MoviesListActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<TmdbMovie>> {
    private static final String TAG = MoviesListActivity.class.getSimpleName();

    // Annotate fields with @BindView and views ID for Butter Knife to find and automatically cast
    // the corresponding views.
    @BindView(R.id.movies_list_recyclerview)
    RecyclerView moviesListRecyclerView;
    @BindView(R.id.movies_list_loading_indicator)
    ProgressBar connectionStatusLoadingIndicator;
    @BindView(R.id.movies_list_no_result_text_view)
    TextView connectionStatusText;
    @BindView(R.id.movies_list_swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    private ArrayList<Integer> genres = new ArrayList<>(), keywords = new ArrayList<>();
    private MoviesListAdapter moviesListAdapter;
    private boolean allowClicks = true, isLoading = false, appendToEnd;
    private int loaderId = -1, currentPage = 1;
    private Unbinder unbinder;
    private Loader<ArrayList<TmdbMovie>> loader = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Define transitions to exit and enter to this activity.
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        //getWindow().setBackgroundDrawableResource(R.color.colorPrimaryDark);
        getWindow().setEnterTransition(new Explode().setDuration(500));
        getWindow().setExitTransition(new Explode().setDuration(500));

        setContentView(R.layout.activity_movies_list);
        unbinder = ButterKnife.bind(this);

        // Read the sort parameter passed to this activity into the intent.
        boolean sort = getParameters();

        if (sort && loaderId >= 0) {
            // Set the recycler view to display the list and create an AsyncTaskLoader for 
            // retrieving the list of movies.
            setRecyclerView();
            if (getSupportLoaderManager().getLoader(loaderId) == null)
                getSupportLoaderManager().initLoader(loaderId, null, this);
            else
                getSupportLoaderManager().restartLoader(loaderId, null, this);
        } else {
            // Nothing to search.
            connectionStatusText.setText(getResources().getString(R.string.no_results));
            connectionStatusText.setVisibility(View.VISIBLE);
            connectionStatusLoadingIndicator.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // When MovieDetailsActivity has finished, we enabled clicks again. We don't need to know
        // any result from MovieDetailsActivity, only when it has finished.
        allowClicks = true;
        super.onActivityResult(requestCode, resultCode, data);
    }

    /* -------------- */
    /* HELPER METHODS */
    /* -------------- */

    /**
     * Helper method to init global variables.
     */
    void initVariables() {
        currentPage = 1;
        loader = null;
        isLoading = false;
        appendToEnd = true;
        setRecyclerView();
    }

    /**
     * Helper method to get the sort parameters passed to the activity into the intent. Initialize
     * some global variables too.
     *
     * @return true if there is any sort parameter, false otherwise.
     */
    boolean getParameters() {
        if (getIntent().hasExtra("genreId") && getIntent().hasExtra("genreName")) {
            // Sorting movies by genre. Create the genres array with only one element, set the
            // title for the activity using the genre name and select the appropriate loader id.
            genres.add(getIntent().getIntExtra("genreId", 0));
            String title = getResources().getQuantityString(R.plurals.genres, genres.size()) +
                    ": " + getIntent().getStringExtra("genreName");
            setTitle(title);
            loaderId = NetworkUtils.TMDB_GENRES_LOADER_ID;
            return true;
        } else if (getIntent().hasExtra("keywordId") && getIntent().hasExtra("keywordName")) {
            // Sorting movies by keyword. Create the keywords array with only one element, set the
            // title for the activity using the keyword name and select the appropriate loader id.
            keywords.add(getIntent().getIntExtra("keywordId", 0));
            String title = getResources().getQuantityString(R.plurals.keywords, keywords.size()) +
                    ": " + getIntent().getStringExtra("keywordName");
            setTitle(title);
            loaderId = NetworkUtils.TMDB_KEYWORDS_LOADER_ID;
            return true;
        } else
            return false;
    }

    /**
     * Helper method for setting the RecyclerView in order to display a list of movies with a given
     * arrangement.
     */
    void setRecyclerView() {
        // Set the LayoutManager for the RecyclerView.
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        moviesListRecyclerView.setLayoutManager(layoutManager);
        moviesListRecyclerView.setHasFixedSize(true);
        moviesListRecyclerView.addItemDecoration(new SpaceItemDecoration(
                1,
                SpaceItemDecoration.VERTICAL_SEPARATION));

        // Set the onClickMoviesListener for click events in the adapters.
        MoviesListAdapter.OnItemClickListener movieListener =
                new MoviesListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(TmdbMovie movie, View clickedView) {
                        if (allowClicks) {
                            // Disable clicks for now, in order to prevent more than one click while
                            // the transition is running. Clicks will be enabled again when we
                            // return from MovieDetailsActivity.
                            allowClicks = false;

                            // Create an ActivityOptions to transition between Activities using
                            // cross-Activity scene animations.
                            ActivityOptionsCompat options =
                                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                                            MoviesListActivity.this, clickedView,
                                            getString(R.string.transition_list_to_details));

                            // Start MovieDetailsActivity to show movie details when the current
                            // element is clicked. We need to know when the other activity finishes,
                            // so we use startActivityForResult. No need a requestCode, we don't
                            // care for any result.
                            Intent intent = new Intent(MoviesListActivity.this,
                                    MovieDetailsActivity.class);
                            intent.putExtra(MovieDetailsActivity.EXTRA_PARAM_MOVIE, movie);
                            startActivityForResult(intent, 0, options.toBundle());
                        }
                    }
                };

        // Set the Adapters for the RecyclerViews.
        moviesListAdapter = new MoviesListAdapter(
                R.layout.list_item_poster_vertical_layout_1, new ArrayList<TmdbMovie>(),
                movieListener);
        moviesListRecyclerView.setAdapter(moviesListAdapter);

        // Listen for scroll changes on the recycler view, in order to know if it is necessary to
        // load another page of results.
        moviesListRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            /**
             * Callback method to be invoked when the RecyclerView has been scrolled. This will be
             * called after the scroll has completed.
             * <p>
             * This callback will also be called if visible item range changes after a layout
             * calculation. In that case, dx and dy will be 0.
             *
             * @param recyclerView The RecyclerView which scrolled.
             * @param dx           The amount of horizontal scroll.
             * @param dy           The amount of vertical scroll.
             */
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                int totalPages = moviesListAdapter.getTotalPages();

                if (!isLoading) {
                    // Load next page of results, if we are at the bottom of the current list and
                    // there are more pages to load.
                    if ((currentPage < totalPages) &&
                            ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount)) {
                        currentPage++;
                        appendToEnd = true;
                        if (loader != null)
                            getSupportLoaderManager().restartLoader(loaderId, null,
                                    MoviesListActivity.this);
                        else
                            getSupportLoaderManager().initLoader(loaderId, null,
                                    MoviesListActivity.this);
                    }
                }
            }
        });

        // Set a listener on the SwipeRefreshLayout that contains the RecyclerViews, just in case we
        // are at the top of the RecyclerViews and we need to reload previous movies.
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        initVariables();
                        getSupportLoaderManager().restartLoader(loaderId, null,
                                MoviesListActivity.this);
                    }
                }
        );
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
        connectionStatusText.setVisibility(View.VISIBLE);

        if (NetworkUtils.isConnected(MoviesListActivity.this)) {
            // There is an available connection. Fetch results from TMDB.
            isLoading = true;
            connectionStatusText.setText(getString(R.string.fetching_info));
            connectionStatusLoadingIndicator.setVisibility(View.VISIBLE);
            switch (id) {
                case NetworkUtils.TMDB_GENRES_LOADER_ID: {
                    loader = new TmdbMoviesAsyncTaskLoader(MoviesListActivity.this,
                            Tmdb.TMDB_SORT_BY_GENRES, currentPage,
                            Locale.getDefault().getLanguage(),
                            Locale.getDefault().getCountry(), genres);
                    break;
                }
                case NetworkUtils.TMDB_KEYWORDS_LOADER_ID: {
                    loader = new TmdbMoviesAsyncTaskLoader(MoviesListActivity.this,
                            Tmdb.TMDB_SORT_BY_KEYWORDS, currentPage,
                            Locale.getDefault().getLanguage(),
                            Locale.getDefault().getCountry(), keywords);
                    break;
                }
                default:
                    loader = null;
            }
            return loader;
        } else {
            // There is no connection. Show error message.
            isLoading = false;
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
     * management of its data so you don't have to.
     *
     * @param loader The Loader that has finished.
     * @param data   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<ArrayList<TmdbMovie>> loader, ArrayList<TmdbMovie> data) {
        // Hide connection status.
        isLoading = false;
        swipeRefreshLayout.setRefreshing(false);
        connectionStatusText.setVisibility(View.GONE);
        connectionStatusLoadingIndicator.setVisibility(View.GONE);

        // Check if there is an available connection.
        if (NetworkUtils.isConnected(MoviesListActivity.this)) {
            // If there is a valid result, then update its data into the current
            // {@link TmdbMovieDetails} object.
            if (data != null && !data.isEmpty() && data.size() > 0) {
                Log.i(TAG, "(onLoadFinished) Search results not null.");

                // Get movies list and display it.
                moviesListAdapter.updateMoviesArrayList(data, appendToEnd);
                moviesListAdapter.notifyDataSetChanged();
            } else {
                Log.i(TAG, "(onLoadFinished) No search results.");
                connectionStatusText.setText(getResources().getString(R.string.no_results));
                connectionStatusText.setVisibility(View.VISIBLE);
            }
        } else {
            // There is no connection. Show error message.
            Log.i(TAG, "(onLoadFinished) No connection to internet.");
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
        isLoading = false;
    }
}
