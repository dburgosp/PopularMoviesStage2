package com.example.android.popularmoviesstage2.fragments;

import android.app.Activity;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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
import android.widget.TextView;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.activities.MovieDetailsActivity;
import com.example.android.popularmoviesstage2.adapters.MoviesFullListAdapter;
import com.example.android.popularmoviesstage2.asynctaskloaders.TmdbMoviesAsyncTaskLoader;
import com.example.android.popularmoviesstage2.classes.Tmdb;
import com.example.android.popularmoviesstage2.classes.TmdbMovie;
import com.example.android.popularmoviesstage2.utils.DisplayUtils;
import com.example.android.popularmoviesstage2.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MoviesFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<ArrayList<TmdbMovie>> {
    private static final String TAG = MoviesFragment.class.getSimpleName();

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

    private boolean allowClicks = true;
    private String sortOrder = Tmdb.TMDB_SORT_BY_NOW_PLAYING;
    private MoviesFullListAdapter moviesFullListAdapter;
    private int currentPage = 1, currentScrollPosition = 0, loaderId = 0;
    private boolean isLoading, appendToEnd;
    private ArrayList<TmdbMovie> moviesArrayList;
    private Unbinder unbinder;
    private Loader<ArrayList<TmdbMovie>> loader = null;

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
        bundle.putString("sortOrder", sortOrder);
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
        sortOrder = getArguments().getString("sortOrder");
        loaderId = getLoaderId();

        // Initialize variables, set the RecyclerView for displaying movie posters and create the
        // AsyncTaskLoader for getting movies lists from TMDB in a separate thread.
        init();
        if (getLoaderManager().getLoader(loaderId) == null)
            getLoaderManager().initLoader(loaderId, null, this);
        else
            getLoaderManager().restartLoader(loaderId, null, this);

        Log.i(TAG, "(onCreate) Fragment created");
        return rootView;
    }

    /**
     * Called when the view previously created by {@link #onCreateView} has
     * been detached from the fragment.  The next time the fragment needs
     * to be displayed, a new view will be created.  This is called
     * after {@link #onStop()} and before {@link #onDestroy()}.  It is called
     * <em>regardless</em> of whether {@link #onCreateView} returned a
     * non-null view.  Internally it is called after the view's state has
     * been saved but before it has been removed from its parent.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
            loader = new TmdbMoviesAsyncTaskLoader(getContext(), sortOrder, currentPage,
                    Locale.getDefault().getLanguage(), Locale.getDefault().getCountry());
        } else {
            // There is no connection. Restart everything and show error message.
            Log.i(TAG, "(onCreateLoader) No internet connection.");
            init();
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
    public void onLoadFinished(Loader<ArrayList<TmdbMovie>> loader, ArrayList<TmdbMovie> data) {
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
            if (data != null && !data.isEmpty()) {
                Log.i(TAG, "(onLoadFinished) Search results not null.");
                moviesFullListAdapter.updateMoviesArrayList(data, appendToEnd);
                moviesFullListAdapter.notifyDataSetChanged();
            } else {
                Log.i(TAG, "(onLoadFinished) No search results.");
                noResultsTextView.setVisibility(View.VISIBLE);
                noResultsTextView.setText(getResources().getString(R.string.no_results));
            }
        } else {
            // There is no connection. Show error message.
            Log.i(TAG, "(onLoadFinished) No connection to internet.");
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

    void init() {
        moviesArrayList = new ArrayList<>();
        currentScrollPosition = 0;
        currentPage = 1;
        loader = null;
        isLoading = false;
        appendToEnd = true;
        setRecyclerView();
    }

    /**
     * Helper method for setting the RecyclerView in order to display a list of movies with a grid
     * arrangement.
     */
    void setRecyclerView() {
        // Get current display metrics, depending on device rotation.
        final DisplayUtils displayUtils = new DisplayUtils(getContext());

        // Vertical GridLayoutManager for displaying movie posters with a number of columns
        // determined by the current orientation of the device.
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),
                displayUtils.getSpanCount());

        // Set the LayoutManager for the RecyclerView.
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        // Set the listener for click events in the Adapter.
        MoviesFullListAdapter.OnItemClickListener listener = new MoviesFullListAdapter.OnItemClickListener() {
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

                    // Start MovieDetailsActivity to show movie details when the current element is
                    // clicked. We need to know when the other activity finishes, so we use
                    // startActivityForResult. No need a requestCode, we don't care for any result.
                    Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
                    intent.putExtra(MovieDetailsActivity.EXTRA_PARAM_MOVIE, movie);
                    //startActivityForResult(intent, 0, options.toBundle());
                    startActivityForResult(intent, 0);
                }
            }
        };

        // Set the Adapter for the RecyclerView, according to the current display size and
        // orientation.
        moviesFullListAdapter = new MoviesFullListAdapter(moviesArrayList,
                displayUtils.getFullListPosterWidthPixels(),
                displayUtils.getFullListPosterHeightPixels(),
                listener);
        recyclerView.setAdapter(moviesFullListAdapter);

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
                int totalPages = moviesFullListAdapter.getTotalPages();

                if (!isLoading) {
                    // Load next page of results, if we are at the bottom of the current list and
                    // there are more pages to load.
                    if ((currentPage < totalPages) && ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount)) {
                        currentPage++;
                        appendToEnd = true;
                        if (loader != null)
                            getLoaderManager().restartLoader(loaderId, null, MoviesFragment.this);
                        else
                            getLoaderManager().initLoader(loaderId, null, MoviesFragment.this);
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
                        init();
                        getLoaderManager().restartLoader(loaderId, null, MoviesFragment.this);
/*                        int currentShownPage = moviesFullListAdapter.getCurrentPage();
                        if (!isLoading && currentShownPage > 1) {
                            // If we are at the top of the list and we are showing a page number
                            // bigger than 1, we need to reload the previous page of results. The
                            // fetched movies will be appended to the start of the RecyclerView.
                            currentPage = currentShownPage - 1;
                            appendToEnd = false;
                            if (loader != null)
                                getLoaderManager().restartLoader(loaderId, null, MoviesFragment.this);
                            else
                                getLoaderManager().initLoader(loaderId, null, MoviesFragment.this);
                        } else {
                            getLoaderManager().restartLoader(loaderId, null, MoviesFragment.this);
                            swipeRefreshLayout.setRefreshing(false);
                        }*/
                    }
                }
        );
    }

    /**
     * Helper method that returns the loader id depending on the current sort order.
     *
     * @return a number with the loader id .
     */
    private int getLoaderId() {
        switch (sortOrder) {
            case Tmdb.TMDB_SORT_BY_NOW_PLAYING:
                return NetworkUtils.TMDB_NOW_PLAYING_MOVIES_LOADER_ID;

            case Tmdb.TMDB_SORT_BY_THIS_WEEK_RELEASES:
                return NetworkUtils.TMDB_THIS_WEEK_RELEASES_MOVIES_LOADER_ID;

            case Tmdb.TMDB_SORT_BY_UPCOMING:
                return NetworkUtils.TMDB_UPCOMING_MOVIES_LOADER_ID;

            case Tmdb.TMDB_SORT_BY_POPULAR:
                return NetworkUtils.TMDB_POPULAR_MOVIES_LOADER_ID;

            case Tmdb.TMDB_SORT_BY_TOP_RATED:
                return NetworkUtils.TMDB_TOP_RATED_MOVIES_LOADER_ID;

            case Tmdb.TMDB_SORT_BY_FOR_BUY_AND_RENT:
                return NetworkUtils.TMDB_BUY_AND_RENT_MOVIES_LOADER_ID;

            default:
                return NetworkUtils.TMDB_FAVORITE_MOVIES_LOADER_ID;
        }
    }
}
