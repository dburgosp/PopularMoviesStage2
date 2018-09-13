package com.example.android.popularmoviesstage2.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.adapters.MoviesListAdapter;
import com.example.android.popularmoviesstage2.asynctaskloaders.TmdbMoviesAsyncTaskLoader;
import com.example.android.popularmoviesstage2.classes.MyCustomToast;
import com.example.android.popularmoviesstage2.classes.Tmdb;
import com.example.android.popularmoviesstage2.classes.TmdbMovie;
import com.example.android.popularmoviesstage2.classes.TmdbMoviesParameters;
import com.example.android.popularmoviesstage2.data.MyPreferences;
import com.example.android.popularmoviesstage2.itemdecorations.SpaceItemDecoration;
import com.example.android.popularmoviesstage2.utils.AnimatedViewsUtils;
import com.example.android.popularmoviesstage2.utils.NetworkUtils;

import java.text.NumberFormat;
import java.util.ArrayList;

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

    private ArrayList<Integer> genres = new ArrayList<>(), keywords = new ArrayList<>();
    private MoviesListAdapter moviesListAdapter;
    private boolean allowClicks = true, isLoading = false, appendToEnd;
    private String moviesBy;
    private int loaderId = -1, currentPage = 1;
    private Unbinder unbinder;
    private Loader<ArrayList<TmdbMovie>> loader = null;
    private Toast customToast = null;
    private TmdbMoviesParameters tmdbMoviesParameters;

    // Public parameters for using to call this activity.
    public static final String PARAM_GENRE_ID = "genreId";
    public static final String PARAM_GENRE_NAME = "genreName";
    public static final String PARAM_KEYWORD_ID = "keywordId";
    public static final String PARAM_KEYWORD_NAME = "keywordName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AnimatedViewsUtils.setTransitions(getWindow());

        setContentView(R.layout.activity_movies_list);
        unbinder = ButterKnife.bind(this);

        // Set the custom tool bar and show the back button.
        Toolbar toolbar = (Toolbar) findViewById(R.id.movies_list_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Read the sort parameter passed to this activity into the intent and get the corresponding
        // loader id. If there's no valid parameters, then there's no valid loader id and there's
        // nothing to search.
        getParameters();
        if (loaderId >= 0) {
            initVariables();
            tmdbMoviesParameters = MyPreferences.getAll(this, Tmdb.TMDB_CONTENT_TYPE_ALL);

            // Set the recycler view to display the list and create an AsyncTaskLoader for 
            // retrieving the list of movies_menu.
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
                // Navigate to the main_menu activity.
                cls = MainActivity.class;
                intent = new Intent(packageContext, cls);
                startActivity(intent);
                break;
            }
            case (R.id.movies_list_menu_settings): {
                // Navigate to the settings activity.
                cls = MoviesListSettingsActivity.class;
                intent = new Intent(packageContext, cls);
                startActivity(intent);
                break;
            }
            default: {
                // case (android.R.id.home)
                onBackPressed();
            }
        }
        return true;
    }

    /**
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.  This means
     * that in some cases the previous state may still be saved, not allowing
     * fragment transactions that modify the state.  To correctly interact
     * with fragments in their proper state, you should instead override
     * {@link #onResumeFragments()}.
     */
    @Override
    protected void onResume() {
        super.onResume();

        // Get current sort order and current language and check if they have been changed.
        String currentSortBy = MyPreferences.getMoviesSortOrder(this);
        String currentLanguage = MyPreferences.getIsoLanguage(this);
        String oldSortBy = tmdbMoviesParameters.getSortBy();
        String oldLanguage = tmdbMoviesParameters.getLanguage();
        if (!currentSortBy.equals(oldSortBy) || !currentLanguage.equals(oldLanguage)) {

            tmdbMoviesParameters.setSortBy(currentSortBy);
            tmdbMoviesParameters.setLanguage(currentLanguage);

            // Restart the loader for displaying the current movies list with the new sort order.
            initVariables();
            moviesListAdapter.clearMoviesArrayList();
            getSupportLoaderManager().restartLoader(loaderId, null, this);
        }
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (customToast != null)
            customToast.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
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
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        if (NetworkUtils.isConnected(MoviesListActivity.this)) {
            // There is an available connection. Fetch results from TMDB.
            isLoading = true;
            connectionStatusLoadingIndicator.setVisibility(View.VISIBLE);
            Context context = MoviesListActivity.this;
            switch (id) {
                case NetworkUtils.TMDB_GENRES_LOADER_ID: {
                    loader = new TmdbMoviesAsyncTaskLoader(context, Tmdb.TMDB_CONTENT_TYPE_GENRES,
                            genres, currentPage, tmdbMoviesParameters);
                    break;
                }
                case NetworkUtils.TMDB_KEYWORDS_LOADER_ID: {
                    loader = new TmdbMoviesAsyncTaskLoader(context, Tmdb.TMDB_CONTENT_TYPE_KEYWORDS,
                            keywords, currentPage, tmdbMoviesParameters);
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
            Log.i(TAG + "." + methodName, "No internet connection.");
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
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();

        // Hide connection status.
        isLoading = false;
        connectionStatusText.setVisibility(View.GONE);
        connectionStatusLoadingIndicator.setVisibility(View.GONE);

        // Check if there is an available connection.
        if (NetworkUtils.isConnected(MoviesListActivity.this)) {
            // If there is a valid result, then update its data into the current
            // {@link TmdbMovieDetails} object.
            if (data != null && data.size() > 0) {
                Log.i(TAG + "." + methodName, "Search results not null.");

                // Show a message with search results, only when displaying the first page.
                if (data.get(0).getPage() == 1) {
                    // Set text.
                    NumberFormat numberFormat = NumberFormat.getNumberInstance();
                    int results = data.get(0).getTotal_results();
                    String title = "";
                    String concept = "";
                    switch (loaderId) {
                        case NetworkUtils.TMDB_GENRES_LOADER_ID:
                            title = getResources().getString(R.string.sort_movies_by_genre);
                            concept = getResources().getQuantityString(R.plurals.genres, 1);
                            break;

                        case NetworkUtils.TMDB_KEYWORDS_LOADER_ID:
                            title = getResources().getString(R.string.sort_movies_by_keyword);
                            concept = getResources().getQuantityString(R.plurals.keywords, 1);
                    }
                    String sortByTitle =
                            getResources().getString(R.string.preferences_movies_sort_by_title);
                    String sortByValue = MyPreferences.getMoviesSortOrderTitle(this);
                    String resultsTitle = getResources().getQuantityString(R.plurals.results,
                            results);
                    String resultsValue = getResources().getQuantityString(R.plurals.movies_number,
                            results, numberFormat.format(results));
                    String line1 = "<strong>" + concept + "</strong>: " + moviesBy;
                    String line2 = "<strong>" + sortByTitle + "</strong>: " + sortByValue;
                    String line3 = "<strong>" + resultsTitle + ": " + resultsValue + "</strong>";
                    String htmlText = line1 + "<br>" + line2 + "<br><br>" + line3.toUpperCase();

                    // Use customised Toast layout.
                    customToast = MyCustomToast.setCustomToast(this, title, htmlText,
                            R.drawable.ic_local_movies_white_24dp);
                    customToast.show();
                }

                // Get movies list and display it.
                moviesListAdapter.updateMoviesArrayList(data, appendToEnd);
                moviesListAdapter.notifyDataSetChanged();
            } else {
                Log.i(TAG + "." + methodName, "No search results.");
                connectionStatusText.setText(getResources().getString(R.string.no_results));
                connectionStatusText.setVisibility(View.VISIBLE);
            }
        } else {
            // There is no connection. Show error message.
            Log.i(TAG + "." + methodName, "No connection to internet.");
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
     * Helper method to get the sort parameters passed to the activity into the intent. Initialize
     * some global variables too.
     */
    private void getParameters() {
        if (getIntent().hasExtra(PARAM_GENRE_ID) && getIntent().hasExtra(PARAM_GENRE_NAME)) {
            // Sorting movies by genre. Create the genres array with only one element, set the
            // introTitle for the activity using the genre name and select the appropriate loader id.
            genres.add(getIntent().getIntExtra(PARAM_GENRE_ID, 0));
            moviesBy = getIntent().getStringExtra(PARAM_GENRE_NAME);

            // Set the loader identifier.
            loaderId = NetworkUtils.TMDB_GENRES_LOADER_ID;
        } else if (getIntent().hasExtra(PARAM_KEYWORD_ID) &&
                getIntent().hasExtra(PARAM_KEYWORD_NAME)) {
            // Sorting movies by keyword. Create the keywords array with only one element, set the
            // introTitle for the activity using the keyword name and select the appropriate loader id.
            keywords.add(getIntent().getIntExtra(PARAM_KEYWORD_ID, 0));
            moviesBy = getIntent().getStringExtra(PARAM_KEYWORD_NAME);

            // Set the loader identifier.
            loaderId = NetworkUtils.TMDB_KEYWORDS_LOADER_ID;
        } else
            loaderId = -1;

        // Set introTitle for this activity.
        setTitle(moviesBy);
    }

    /**
     * Helper method for setting the RecyclerView in order to display a list of movies with a given
     * arrangement.
     */
    private void setRecyclerView() {
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
                            Bundle option = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                    MoviesListActivity.this, clickedView,
                                    getString(R.string.transition_poster)).toBundle();

                            // Start MovieDetailsActivity to show movie movie_details_menu when the
                            // current element is clicked. We need to know when the other activity
                            // finishes, so we use startActivityForResult. No need a requestCode, we
                            // don't care for any result.
                            Intent intent = new Intent(MoviesListActivity.this,
                                    MovieDetailsActivity.class);
                            intent.putExtra(MovieDetailsActivity.EXTRA_PARAM_MOVIE, movie);

                            // Animate view when clicked and navigate to next activity for result.
                            AnimatedViewsUtils.animateOnClick(MoviesListActivity.this,
                                    clickedView);
                            startActivityForResult(intent, 0, option);
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
    }
}
