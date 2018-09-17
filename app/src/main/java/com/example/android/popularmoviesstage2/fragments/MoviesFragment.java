package com.example.android.popularmoviesstage2.fragments;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.activities.MovieDetailsActivity;
import com.example.android.popularmoviesstage2.adapters.MoviesListAdapter;
import com.example.android.popularmoviesstage2.asynctaskloaders.GenericAsyncTaskLoader;
import com.example.android.popularmoviesstage2.classes.MyCustomToast;
import com.example.android.popularmoviesstage2.classes.Tmdb;
import com.example.android.popularmoviesstage2.classes.TmdbMovie;
import com.example.android.popularmoviesstage2.classes.TmdbMoviesParameters;
import com.example.android.popularmoviesstage2.data.MyPreferences;
import com.example.android.popularmoviesstage2.itemdecorations.SpaceItemDecoration;
import com.example.android.popularmoviesstage2.utils.AnimatedViewsUtils;
import com.example.android.popularmoviesstage2.utils.DisplayUtils;
import com.example.android.popularmoviesstage2.utils.NetworkUtils;

import java.text.NumberFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

@SuppressWarnings("unchecked")
public class MoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Object> {
    private static final String TAG = MoviesFragment.class.getSimpleName();
    private static final int RESULT_CODE_MOVIE_DETAILS = 0;

    // Annotate fields with @BindView and views ID for Butter Knife to find and automatically cast
    // the corresponding views.
    @BindView(R.id.fragment_movies_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.fragment_movies_no_result_text_view)
    TextView noResultsTextView;
    @BindView(R.id.fragment_movies_loading_indicator)
    ProgressBar progressBar;

    private boolean allowClicks = true;
    private String contentType = Tmdb.TMDB_CONTENT_TYPE_NOW_PLAYING;
    private MoviesListAdapter moviesListAdapter;
    private int currentPage = 1, loaderId = 0;
    private boolean isLoading, appendToEnd;
    private ArrayList<TmdbMovie> moviesArrayList;
    private Unbinder unbinder;
    private Loader<Object> loader = null;
    private Toast customToast = null;
    private ViewPager viewPager;
    private FloatingActionButton floatingActionButton;
    private TmdbMoviesParameters tmdbMoviesParameters;

    /**
     * Required empty public constructor.
     */
    public MoviesFragment() {
    }

    /**
     * Factory method to create a new instance of this fragment using the provided parameters.
     *
     * @param sortOrder is the sort order of the movies_menu list.
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
        String methodTag = TAG + "." + Thread.currentThread().getStackTrace()[2].getMethodName();
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        floatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.movies_fab);
        viewPager = (ViewPager) getActivity().findViewById(R.id.movies_viewpager);

        // Get arguments from calling activity.
        contentType = getArguments().getString("contentType");

        // Initialize variables, set the RecyclerView for displaying movie posters and get
        // preferences.
        initVariables();
        tmdbMoviesParameters = MyPreferences.getAll(getContext(), contentType);
        setRecyclerView();

        // Create the AsyncTaskLoader for getting movies_menu lists from TMDB in a separate thread.
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

        Log.i(methodTag, "Fragment created");
        return rootView;
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
        allowClicks = true;
    }

    /**
     * Called when the fragment is no longer in use.  This is called
     * after {@link #onStop()} and before {@link #onDetach()}.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (customToast != null)
            customToast.cancel();
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
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        String methodTag = TAG + "." + Thread.currentThread().getStackTrace()[2].getMethodName();
        if (NetworkUtils.isConnected(getContext())) {
            // There is an available connection. Fetch results from TMDB.
            isLoading = true;
            progressBar.setVisibility(View.VISIBLE);
            noResultsTextView.setVisibility(View.INVISIBLE);
            loader = new GenericAsyncTaskLoader(getContext(), contentType, null,
                    currentPage, tmdbMoviesParameters,
                    GenericAsyncTaskLoader.ASYNC_TASK_LOADER_TYPE_TMDB_MOVIES_LIST);
        } else {
            // There is no connection. Restart everything and show error message.
            Log.i(methodTag, "No internet connection.");
            initVariables();
            setRecyclerView();
            progressBar.setVisibility(View.INVISIBLE);
            noResultsTextView.setText(getResources().getString(R.string.no_connection));
            noResultsTextView.setVisibility(View.VISIBLE);
        }

        return loader;
    }

    /**
     * Called when a previously created loader has finished its load.
     *
     * @param loader The Loader that has finished.
     * @param data   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(final Loader<Object> loader, Object data) {
        String methodTag = TAG + "." + Thread.currentThread().getStackTrace()[2].getMethodName();

        // Hide progress bar.
        progressBar.setVisibility(View.INVISIBLE);
        isLoading = false;

        // Loaders issue? onLoadFinished triggers sometimes twice returning the same page. Avoid
        // adding the same page to the list of movies_menu.
        if (moviesArrayList != null && moviesArrayList.size() > 0 &&
                currentPage == moviesArrayList.get(moviesArrayList.size() - 1).getPage())
            return;

        // Check if there is an available connection.
        if (NetworkUtils.isConnected(getContext())) {
            // If there is a valid list of {@link TmdbMovie} objects, add them to the adapter's data
            // set.
            ArrayList<TmdbMovie> movies = (ArrayList<TmdbMovie>) data;
            if (data != null && movies.size() > 0) {
                Log.i(methodTag, "Search results not null.");

                // Get movies_menu list and display it.
                moviesListAdapter.updateMoviesArrayList(movies, appendToEnd);
                moviesListAdapter.notifyDataSetChanged();

                // Show a message with search results, only when displaying the first page in the
                // currently visible fragment.
                if (getUserVisibleHint() && movies.get(0).getPage() == 1) {
                    // Set text.
                    int results = movies.get(0).getTotal_results();
                    String title = "";
                    String htmlText = "";
                    NumberFormat numberFormat = NumberFormat.getNumberInstance();
                    switch (loader.getId()) {
                        case NetworkUtils.TMDB_ALL_MOVIES_LOADER_ID:
                            if (viewPager.getCurrentItem() == 0) {
                                title = getString(R.string.all_movies);
                                htmlText = ("<strong>" + getResources().getQuantityString(
                                        R.plurals.results, results) + ": " +
                                        getResources().getQuantityString(R.plurals.movies_number,
                                                results, numberFormat.format(results)) +
                                        "</strong>").toUpperCase();
                            }
                            break;

                        case NetworkUtils.TMDB_NOW_PLAYING_MOVIES_LOADER_ID:
                            if (viewPager.getCurrentItem() == 1) {
                                title = getString(R.string.movies_sort_by_now_playing);
                                htmlText = "<strong>" + getString(R.string.preferences_movies_how_title) + "</strong>: " +
                                        MyPreferences.getNowPlayingMoviesTitle(getContext(),
                                                MyPreferences.TYPE_MOVIES_HOW) + "<br><strong>" +
                                        getString(R.string.preferences_movies_where_title) + "</strong>: " +
                                        MyPreferences.getNowPlayingMoviesTitle(getContext(),
                                                MyPreferences.TYPE_MOVIES_WHERE) +
                                        "<br><br><strong>" + (getResources().getQuantityString(
                                        R.plurals.results, results) + ": " + getResources().getQuantityString(
                                        R.plurals.movies_number, results,
                                        numberFormat.format(results)) + "</strong>").toUpperCase();
                            }
                            break;

                        case NetworkUtils.TMDB_UPCOMING_MOVIES_LOADER_ID:
                            if (viewPager.getCurrentItem() == 2) {
                                title = getString(R.string.movies_sort_by_upcoming);
                                htmlText = "<strong>" + getString(R.string.preferences_movies_how_title) + "</strong>: "
                                        + MyPreferences.getUpcomingMoviesTitle(getContext(),
                                        MyPreferences.TYPE_MOVIES_HOW) + "<br><strong>" +
                                        getString(R.string.preferences_movies_when_title) + "</strong>: " +
                                        MyPreferences.getUpcomingMoviesTitle(getContext(),
                                                MyPreferences.TYPE_MOVIES_WHEN) + "<br><strong>" +
                                        getString(R.string.preferences_movies_where_title) + "</strong>: " +
                                        MyPreferences.getUpcomingMoviesTitle(getContext(),
                                                MyPreferences.TYPE_MOVIES_WHERE) +
                                        "<br><br><strong>" + (getResources().getQuantityString(
                                        R.plurals.results, results) + ": " + getResources().getQuantityString(
                                        R.plurals.movies_number, results,
                                        numberFormat.format(results)) + "</strong>").toUpperCase();
                            }
                            break;

                        default:
                            Log.i(methodTag, "No valid loader.");
                            floatingActionButton.setVisibility(View.GONE);
                            noResultsTextView.setVisibility(View.VISIBLE);
                            noResultsTextView.setText(getResources().getString(R.string.no_results));
                            return;
                    }
                    if (!htmlText.equals("")) {
                        // Show customised Toast layout.
                        customToast = MyCustomToast.setCustomToast(getContext(), title, htmlText,
                                R.drawable.ic_local_movies_white_24dp);
                        customToast.show();
                    }
                }
            } else {
                Log.i(methodTag, "No search results.");
                noResultsTextView.setVisibility(View.VISIBLE);
                noResultsTextView.setText(getResources().getString(R.string.no_results));
            }
        } else {
            // There is no connection. Show error message.
            Log.i(methodTag, "No connection to internet.");
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
    public void onLoaderReset(Loader<Object> loader) {
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
        currentPage = 1;
        loader = null;
        isLoading = false;
        appendToEnd = true;
    }

    /**
     * Helper method for setting the RecyclerView in order to display a list of movies_menu with a grid
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
                    final Bundle option = ActivityOptions.makeSceneTransitionAnimation(
                            (Activity) getContext(), clickedView,
                            getString(R.string.transition_poster)).toBundle();

                    // Start MovieDetailsActivity to show movie movie_details_menu when the current
                    // element is clicked. We need to know when the other activity finishes, so we
                    // use startActivityForResult.
                    final Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
                    intent.putExtra(MovieDetailsActivity.EXTRA_PARAM_MOVIE, movie);

                    // Animate view when clicked.
                    AnimatedViewsUtils.animateOnClick(getContext(), clickedView);
                    startActivityForResult(intent, RESULT_CODE_MOVIE_DETAILS, option);
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
     * Helper method that returns the loader id depending on the current content type for movies.
     *
     * @return a number with the loader id.
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