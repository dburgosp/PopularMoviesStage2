package com.example.android.popularmoviesstage2.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.example.android.popularmoviesstage2.utils.TextViewUtils;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences;

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
    @BindView(R.id.movies_list_title)
    TextView titleTextView;
    @BindView(R.id.movies_list_swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.movies_list_fab)
    FloatingActionButton floatingActionButton;

    private ArrayList<Integer> genres = new ArrayList<>(), keywords = new ArrayList<>();
    private MoviesListAdapter moviesListAdapter;
    private boolean allowClicks = true, isLoading = false, appendToEnd;
    private String moviesBy, sortOrderBy;
    private int loaderId = -1, currentPage = 1;
    private Unbinder unbinder;
    private Loader<ArrayList<TmdbMovie>> loader = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Define transitions to exit and enter to this activity.
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setBackgroundDrawableResource(R.color.colorPrimary);
        getWindow().setEnterTransition(new Explode().setDuration(250));
        getWindow().setExitTransition(new Explode().setDuration(250));

        setContentView(R.layout.activity_movies_list);
        unbinder = ButterKnife.bind(this);

        // Read the sort parameter passed to this activity into the intent.
        boolean sort = getParameters();

        if (sort && loaderId >= 0) {
            // Set the recycler view to display the list and create an AsyncTaskLoader for 
            // retrieving the list of movies.
            initVariables();
            getDefaultSettings();
            setRecyclerView();
            if (getSupportLoaderManager().getLoader(loaderId) == null)
                getSupportLoaderManager().initLoader(loaderId, null, this);
            else
                getSupportLoaderManager().restartLoader(loaderId, null, this);

            // Set the floating action button.
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: define FAB behaviour on click events.
                }
            });
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

    @Override
    public boolean onSupportNavigateUp() {
        supportFinishAfterTransition();
        return true;
    }

    /**
     * Initialize the contents of the Activity's standard options menu.  You
     * should place your menu items in to <var>menu</var>.
     * <p>
     * <p>This is only called once, the first time the options menu is
     * displayed.  To update the menu every time it is displayed, see
     * {@link #onPrepareOptionsMenu}.
     * <p>
     * <p>The default implementation populates the menu with standard system
     * menu items.  These are placed in the {@link Menu#CATEGORY_SYSTEM} group so that
     * they will be correctly ordered with application-defined menu items.
     * Deriving classes should always call through to the base implementation.
     * <p>
     * <p>You can safely hold on to <var>menu</var> (and any items created
     * from it), making modifications to it as desired, until the next
     * time onCreateOptionsMenu() is called.
     * <p>
     * <p>When you add items to the menu, you can implement the Activity's
     * {@link #onOptionsItemSelected} method to handle them there.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.movies_list_menu, menu);
        return true;
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     * <p>
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.</p>
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        Context packageContext = MoviesListActivity.this;
        Class<?> cls;
        int id = item.getItemId();
        switch (id) {
            case (R.id.movies_list_menu_home): {
                // Set the intent for navigating from here to the main activity.
                cls = MainActivity.class;
                break;
            }
            default: {
                // case (R.id.movies_list_menu_settings):
                // Set the intent for navigating from here to the settings activity.
                cls = MoviesListSettingsActivity.class;
            }
        }

        // Navigate to the given activity.
        intent = new Intent(packageContext, cls);
        startActivity(intent);
        return true;
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
    }

    /**
     * Helper method to get the default settings for this activity.
     */
    void getDefaultSettings() {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(this);
        sortOrderBy = sharedPreferences.getString(getString(R.string.preferences_sort_by_key),
                getString(R.string.preferences_sort_by_popularity_desc_value));
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
            moviesBy = getIntent().getStringExtra("genreName");

            // Set title for this activity.
            setTitle(getResources().getString(R.string.sort_movies_by_genre));

            // Set the loader identifier.
            loaderId = NetworkUtils.TMDB_GENRES_LOADER_ID;
            return true;
        } else if (getIntent().hasExtra("keywordId") && getIntent().hasExtra("keywordName")) {
            // Sorting movies by keyword. Create the keywords array with only one element, set the
            // title for the activity using the keyword name and select the appropriate loader id.
            keywords.add(getIntent().getIntExtra("keywordId", 0));
            moviesBy = getIntent().getStringExtra("keywordName");

            // Set title for this activity.
            setTitle(getResources().getString(R.string.sort_movies_by_keyword));

            // Set the loader identifier.
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

                            // Start MovieDetailsActivity to show movie movie_details_menu when the current
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
                        moviesListAdapter.clearMoviesArrayList();
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
        if (NetworkUtils.isConnected(MoviesListActivity.this)) {
            // There is an available connection. Fetch results from TMDB.
            isLoading = true;
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

                // Set subtitle with the sort string and the total number of results.
                int labelColor = getResources().getColor(R.color.colorGrey);
                String сolorString = String.format("%X", labelColor).substring(2);
                TextViewUtils.setHtmlText(titleTextView, "<strong><big>" +
                        moviesBy.toUpperCase() + "  </big></strong><small><font color=\"#" +
                        сolorString + "\">(" + data.get(0).getTotal_results() + " " +
                        getResources().getQuantityString(R.plurals.results, data.size()) + ")</font></small>");

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
