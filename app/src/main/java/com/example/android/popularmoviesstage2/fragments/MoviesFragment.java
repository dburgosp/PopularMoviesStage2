package com.example.android.popularmoviesstage2.fragments;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.activities.ConfigFilteredMoviesActivity;
import com.example.android.popularmoviesstage2.activities.MovieDetailsActivity;
import com.example.android.popularmoviesstage2.adapters.MoviesListAdapter;
import com.example.android.popularmoviesstage2.asynctaskloaders.TmdbMoviesAsyncTaskLoader;
import com.example.android.popularmoviesstage2.classes.CustomToast;
import com.example.android.popularmoviesstage2.classes.Tmdb;
import com.example.android.popularmoviesstage2.classes.TmdbMovie;
import com.example.android.popularmoviesstage2.data.MyPreferences;
import com.example.android.popularmoviesstage2.itemdecorations.SpaceItemDecoration;
import com.example.android.popularmoviesstage2.utils.DisplayUtils;
import com.example.android.popularmoviesstage2.utils.NetworkUtils;

import java.text.NumberFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

public class MoviesFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<ArrayList<TmdbMovie>> {
    private static final String TAG = MoviesFragment.class.getSimpleName();
    private static final int RESULT_CODE_MOVIE_DETAILS = 0;
    private static final int RESULT_CODE_CONFIG_UPCOMING_MOVIES = 1;
    private static final int RESULT_CODE_CONFIG_NOW_PLAYING_MOVIES = 2;

    // Annotate fields with @BindView and views ID for Butter Knife to find and automatically cast
    // the corresponding views.
    @BindView(R.id.fragment_movies_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.fragment_movies_no_result_text_view)
    TextView noResultsTextView;
    @BindView(R.id.fragment_movies_loading_indicator)
    ProgressBar progressBar;
    @BindView(R.id.fragment_movies_swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.fragment_movies_fab)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.fragment_movies_layout)
    RelativeLayout mainLayout;

    private boolean allowClicks = true;
    private String contentType = Tmdb.TMDB_CONTENT_TYPE_NOW_PLAYING, language, region, sortBy,
            certification, releaseType, initDate, endDate;
    private MoviesListAdapter moviesListAdapter;
    private int currentPage = 1, currentScrollPosition = 0, loaderId = 0, voteCount = 0;
    private Double voteAverage = 0.0;
    private boolean isLoading, appendToEnd;
    private ArrayList<TmdbMovie> moviesArrayList;
    private Unbinder unbinder;
    private Loader<ArrayList<TmdbMovie>> loader = null;
    private Toast customToast = null;

    /**
     * Required empty public constructor.
     */
    public MoviesFragment() {
    }

    /**
     * Factory method to create a new instance of this fragment using the provided parameters.
     *
     * @param sortOrder is the sort order of the movies list.
     * @return a new instance of fragment MoviesFragment.
     */
    public static MoviesFragment newInstance(String sortOrder) {
        Bundle bundle = new Bundle();
        bundle.putString("contentType", sortOrder);
        MoviesFragment fragment = new MoviesFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: Define transitions to exit and enter to this activity.
/*        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setBackgroundDrawableResource(R.color.colorPrimaryDark);
        getWindow().setEnterTransition(new Explode());
        getWindow().setExitTransition(new Explode());*/

        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        // Get arguments from calling activity.
        contentType = getArguments().getString("contentType");

        // Initialize variables, set the RecyclerView for displaying movie posters and set the
        // SwipeRefreshLayout.
        initVariables();
        getMyPreferences();
        setRecyclerView();
        setSwipeRefreshLayout();

        // By default, FAB is not visible.
        floatingActionButton.setVisibility(View.GONE);

        // Create the AsyncTaskLoader for getting movies lists from TMDB in a separate thread.
        loaderId = getLoaderId();
        if (loaderId >= 0) {
            // If there is a valid loaderId, check if we have to init or restart the loader.
            if (getLoaderManager().getLoader(loaderId) == null) {
                getLoaderManager().initLoader(loaderId, null, this);

            } else
                getLoaderManager().restartLoader(loaderId, null, this);
        } else {
            // If there is no valid loaderId, remove this fragment to exit.
            getFragmentManager().beginTransaction().remove(this).commit();
        }

        Log.i(TAG, "(onCreate) Fragment created");
        return rootView;
    }

    /**
     * Called when the fragment is no longer in use.  This is called
     * after {@link #onStop()} and before {@link #onDetach()}.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if (customToast != null)
            customToast.cancel();
    }

    /**
     * Receive the result from a previous call to
     * {@link #startActivityForResult(Intent, int)}.  This follows the
     * related Activity API as described there in
     * {@link Activity#onActivityResult(int, int, Intent)}.
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_CODE_CONFIG_NOW_PLAYING_MOVIES: {
                if (resultCode == RESULT_OK) {
                    // Preferences have changed for now playing movies section. Read new preferences
                    // values and refresh the current movie list.
                    refreshMovieList(Tmdb.TMDB_CONTENT_TYPE_NOW_PLAYING);
                }
                break;
            }

            case RESULT_CODE_CONFIG_UPCOMING_MOVIES: {
                if (resultCode == RESULT_OK) {
                    // Preferences have changed for upcoming movies section. Read new preferences
                    // values and refresh the current movie list.
                    refreshMovieList(Tmdb.TMDB_CONTENT_TYPE_UPCOMING);
                }
                break;
            }
        }
        allowClicks = true;

        // Set result for calling activity.
        getActivity().setResult(resultCode, getActivity().getIntent());
    }



    /* ------ */
    /* LOADER */
    /* ------ */

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<ArrayList<TmdbMovie>> onCreateLoader(int id, Bundle args) {
        if (NetworkUtils.isConnected(getContext())) {
            // There is an available connection. Fetch results from TMDB.
            isLoading = true;
            progressBar.setVisibility(View.VISIBLE);
            noResultsTextView.setVisibility(View.INVISIBLE);
            loader = new TmdbMoviesAsyncTaskLoader(getContext(), contentType, null,
                    currentPage, language, region, sortBy, certification, voteCount, voteAverage,
                    releaseType, initDate, endDate);
        } else {
            // There is no connection. Restart everything and show error message.
            Log.i(TAG, "(onCreateLoader) No internet connection.");
            initVariables();
            setRecyclerView();
            progressBar.setVisibility(View.INVISIBLE);
            noResultsTextView.setText(getResources().getString(R.string.no_connection));
            noResultsTextView.setVisibility(View.VISIBLE);
        }

        return loader;
    }

    /**
     * Called when a previously created loader has finished its load.  Note
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
     * the  constructor <em>without</em> passing
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
    public void onLoadFinished(final Loader<ArrayList<TmdbMovie>> loader, ArrayList<TmdbMovie> data) {
        // Hide progress bar.
        progressBar.setVisibility(View.INVISIBLE);
        isLoading = false;
        swipeRefreshLayout.setRefreshing(false);

        // Loaders issue? onLoadFinished triggers sometimes twice returning the same page. Avoid
        // adding the same page to the list of movies.
        if (moviesArrayList != null && moviesArrayList.size() > 0 &&
                currentPage == moviesArrayList.get(moviesArrayList.size() - 1).getPage())
            return;

        // Check if there is an available connection.
        if (NetworkUtils.isConnected(getContext())) {
            // If there is a valid list of {@link TmdbMovie} objects, add them to the adapter's data
            // set.
            if (data != null && data.size() > 0) {
                Log.i(TAG, "(onLoadFinished) Search results not null.");

                // Show a message with search results, only when displaying the first page in the
                // currently visible fragment.
                if (getUserVisibleHint() && data.get(0).getPage() == 1) {
                    // Set text.
                    String htmlText;
                    String color = String.format("%X",
                            getResources().getColor(R.color.colorDarkWhite)).substring(2);
                    NumberFormat numberFormat = NumberFormat.getNumberInstance();
                    switch (loader.getId()) {
                        case NetworkUtils.TMDB_ALL_MOVIES_LOADER_ID:
                            htmlText = "<strong>" + getString(R.string.all_movies).toUpperCase() +
                                    "</strong>";
                            break;

                        case NetworkUtils.TMDB_NOW_PLAYING_MOVIES_LOADER_ID:
                            htmlText = "<strong>" + getString(R.string.movies_sort_by_now_playing)
                                    .toUpperCase() + "</strong><br><font color=\"#" + color + "\">"
                                    + getString(R.string.preferences_movies_how_title) + ": " +
                                    MyPreferences.getNowPlayingMoviesTitle(getContext(),
                                            MyPreferences.TYPE_MOVIES_HOW) + "<br>" +
                                    getString(R.string.preferences_movies_where_title) + ": " +
                                    MyPreferences.getNowPlayingMoviesTitle(getContext(),
                                            MyPreferences.TYPE_MOVIES_WHERE) + "</font>";
                            break;

                        default: // case NetworkUtils.TMDB_UPCOMING_MOVIES_LOADER_ID:
                            htmlText = "<strong>" + getString(R.string.movies_sort_by_upcoming)
                                    .toUpperCase() + "</strong><br><font color=\"#" + color + "\">"
                                    + getString(R.string.preferences_movies_how_title) + ": "
                                    + MyPreferences.getUpcomingMoviesTitle(getContext(),
                                    MyPreferences.TYPE_MOVIES_HOW) + "<br>" +
                                    getString(R.string.preferences_movies_when_title) + ": " +
                                    MyPreferences.getUpcomingMoviesTitle(getContext(),
                                            MyPreferences.TYPE_MOVIES_WHEN) + "<br>" +
                                    getString(R.string.preferences_movies_where_title) + ": " +
                                    MyPreferences.getUpcomingMoviesTitle(getContext(),
                                            MyPreferences.TYPE_MOVIES_WHERE) + "</font>";
                            break;
                    }
                    int results = data.get(0).getTotal_results();
                    htmlText = htmlText + "<br><br><strong>" + getResources().getQuantityString(
                            R.plurals.results, results).toUpperCase() + "</strong>" +
                            "<br><font color=\"#" + color + "\">" + getResources().getQuantityString(
                            R.plurals.movies_number, results, numberFormat.format(results)) +
                            "</font>";

                    // Use customised Toast layout.
                    customToast = CustomToast.setCustomToast(getContext(), htmlText,
                            R.drawable.ic_local_movies_white_24dp);
                    customToast.show();
                }

                // Get movies list and display it.
                moviesListAdapter.updateMoviesArrayList(data, appendToEnd);
                moviesListAdapter.notifyDataSetChanged();
            } else {
                Log.i(TAG, "(onLoadFinished) No search results.");
                floatingActionButton.setVisibility(View.GONE);
                noResultsTextView.setVisibility(View.VISIBLE);
                noResultsTextView.setText(getResources().getString(R.string.no_results));
            }

            // Set FAB onClick behaviour anyway.
            switch (loader.getId()) {
                case NetworkUtils.TMDB_NOW_PLAYING_MOVIES_LOADER_ID: {
                    // If we are showing now playing movies info, show FAB and set its onClick
                    // behaviour for opening ConfigFilteredMoviesActivity.
                    setFloatingActionButton(ConfigFilteredMoviesActivity.TYPE_NOW_PLAYING,
                            RESULT_CODE_CONFIG_NOW_PLAYING_MOVIES);
                    break;
                }

                case NetworkUtils.TMDB_UPCOMING_MOVIES_LOADER_ID: {
                    // If we are showing upcoming movies info, show FAB and set its onClick
                    // behaviour for opening ConfigFilteredMoviesActivity.
                    setFloatingActionButton(ConfigFilteredMoviesActivity.TYPE_UPCOMING,
                            RESULT_CODE_CONFIG_UPCOMING_MOVIES);
                    break;
                }
            }
        } else {
            // There is no connection. Show error message.
            Log.i(TAG, "(onLoadFinished) No connection to internet.");
            floatingActionButton.setVisibility(View.GONE);
            noResultsTextView.setVisibility(View.VISIBLE);
            noResultsTextView.setText(getResources().getString(R.string.no_connection));
        }
    }

    /**
     * Called when a previously created loader is being reset, and thus making its data unavailable.
     * The application should at this point remove any references it has to the Loader's data.
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
        moviesArrayList = new ArrayList<>();
        currentScrollPosition = 0;
        currentPage = 1;
        loader = null;
        isLoading = false;
        appendToEnd = true;
    }

    /**
     * Helper method to get current values from preferences for query parameters.
     */
    private void getMyPreferences() {
        language = MyPreferences.getIsoLanguage(getContext());
        sortBy = MyPreferences.getMoviesSortOrder(getContext());
        certification = MyPreferences.getMoviesCertification(getContext());
        voteAverage = MyPreferences.getMoviesVoteAverage(getContext());
        voteCount = MyPreferences.getMoviesVoteCount(getContext());
        switch (contentType) {
            case Tmdb.TMDB_CONTENT_TYPE_NOW_PLAYING:
                region = MyPreferences.getNowPlayingMoviesRegion(getContext());
                releaseType = MyPreferences.getNowPlayingMoviesReleaseType(getContext());
                initDate = MyPreferences.getNowPlayingMoviesInitDate(getContext());
                endDate = MyPreferences.getNowPlayingMoviesEndDate(getContext());
                break;

            case Tmdb.TMDB_CONTENT_TYPE_UPCOMING:
                region = MyPreferences.getUpcomingMoviesRegion(getContext());
                releaseType = MyPreferences.getUpcomingMoviesReleaseType(getContext());
                initDate = MyPreferences.getUpcomingMoviesInitDate(getContext());
                endDate = MyPreferences.getUpcomingMoviesEndDate(getContext());
                break;

            default:
                region = "";
                releaseType = "";
                initDate = "";
                endDate = "";
        }
    }

    /**
     * Helper method for setting the RecyclerView in order to display a list of movies with a grid
     * arrangement.
     */
    private void setRecyclerView() {
        // Get current display metrics, depending on device rotation.
        final DisplayUtils displayUtils = new DisplayUtils(getContext());

        // Vertical GridLayoutManager for displaying movie posters with a number of columns
        // determined by the current orientation of the device.
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),
                displayUtils.getSpanCount());

        // Set the LayoutManager for the RecyclerView.
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        int separator = getContext().getResources().getDimensionPixelOffset(R.dimen.tiny_padding);
        recyclerView.addItemDecoration(new SpaceItemDecoration(separator,
                SpaceItemDecoration.HORIZONTAL_VERTICAL_SEPARATION, displayUtils.getSpanCount()));

        // Set the listener for click events in the Adapter.
        MoviesListAdapter.OnItemClickListener listener = new MoviesListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TmdbMovie movie, View clickedView) {
                if (allowClicks) {
                    // Disable clicks for now, in order to prevent more than one click while the
                    // transition is running. Clicks will be enabled again into onActivityResult,
                    // when we return from MovieDetailsActivity.
                    allowClicks = false;

                    // Create an ActivityOptions to transition between Activities using
                    // cross-Activity scene animations.
                    //ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getContext(), clickedView, getString(R.string.transition_list_to_details));

                    // Start MovieDetailsActivity to show movie movie_details_menu when the current element is
                    // clicked. We need to know when the other activity finishes, so we use
                    // startActivityForResult. No need a requestCode, we don't care for any result.
                    Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
                    intent.putExtra(MovieDetailsActivity.EXTRA_PARAM_MOVIE, movie);
                    //startActivityForResult(intent, 0, options.toBundle());
                    startActivityForResult(intent, RESULT_CODE_MOVIE_DETAILS);
                }
            }
        };

        // Set the Adapter for the RecyclerView, according to the current display size and
        // orientation.
        int posterWidth = (displayUtils.getFullDisplayBackdropWidthPixels() -
                (displayUtils.getSpanCount() * separator * 2)) / displayUtils.getSpanCount();
        int posterHeight = posterWidth * 3 / 2;
        moviesListAdapter = new MoviesListAdapter(R.layout.list_item_poster_grid_layout_1,
                moviesArrayList, posterWidth, posterHeight, listener);
        moviesListAdapter.clearMoviesArrayList();
        recyclerView.setAdapter(moviesListAdapter);

        // Listen for scroll changes on the recycler view, in order to know if it is necessary to
        // load another page of results.
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                int visibleItemCount = gridLayoutManager.getChildCount();
                int totalItemCount = gridLayoutManager.getItemCount();
                int firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition();
                int totalPages = moviesListAdapter.getTotalPages();

                if (!isLoading) {
                    // Load next page of results, if we are at the bottom of the current list and
                    // there are more pages to load.
                    if ((currentPage < totalPages) &&
                            ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount)) {
                        currentPage++;
                        appendToEnd = true;
                        if (loader != null)
                            getLoaderManager().restartLoader(loaderId, null,
                                    MoviesFragment.this);
                        else
                            getLoaderManager().initLoader(loaderId, null,
                                    MoviesFragment.this);
                    }
                }
            }
        });
    }

    /**
     * Helper method for showing FloatingActionButton and setting its behaviour.
     *
     * @param typeValue  is the value for the ConfigFilteredMoviesActivity.PARAM_TYPE parameter of the
     *                   intent for calling ConfigFilteredMoviesActivity when the FloatingActionButton is
     *                   clicked.
     * @param resultCode is the request code for calling ConfigFilteredMoviesActivity for result.
     */
    private void setFloatingActionButton(int typeValue, final int resultCode) {
        final Intent intent = new Intent(getContext(), ConfigFilteredMoviesActivity.class);
        intent.putExtra(ConfigFilteredMoviesActivity.PARAM_TYPE, typeValue);
        floatingActionButton.setVisibility(View.VISIBLE);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start config activity only when required.
                Bundle option = ActivityOptions.makeSceneTransitionAnimation(
                        getActivity()).toBundle();
                startActivityForResult(intent, resultCode, option);
            }
        });
    }

    /**
     * Helper method to set a listener on the SwipeRefreshLayout that contains the RecyclerViews,
     * just in case we are at the top of the RecyclerViews and we need to reload previous movies.
     */
    private void setSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        refreshMovieList(contentType);
                    }
                }
        );
    }

    /**
     * Get a fresh new movies list.
     */
    private void refreshMovieList(String contentType) {
        initVariables();
        getMyPreferences();
        this.contentType = contentType;
        loaderId = getLoaderId();
        moviesListAdapter.clearMoviesArrayList();
        getLoaderManager().restartLoader(loaderId, null, MoviesFragment.this);
    }

    /**
     * Helper method that returns the loader id depending on the current sort order.
     *
     * @return a number with the loader id .
     */
    private int getLoaderId() {
        switch (contentType) {
            case Tmdb.TMDB_CONTENT_TYPE_ALL:
                return NetworkUtils.TMDB_ALL_MOVIES_LOADER_ID;

            case Tmdb.TMDB_CONTENT_TYPE_UPCOMING:
                return NetworkUtils.TMDB_UPCOMING_MOVIES_LOADER_ID;

            case Tmdb.TMDB_CONTENT_TYPE_NOW_PLAYING:
                return NetworkUtils.TMDB_NOW_PLAYING_MOVIES_LOADER_ID;

            default:
                return NetworkUtils.TMDB_FAVORITE_MOVIES_LOADER_ID;
        }
    }
}
