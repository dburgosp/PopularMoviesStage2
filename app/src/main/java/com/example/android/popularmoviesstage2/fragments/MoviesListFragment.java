package com.example.android.popularmoviesstage2.fragments;

import android.content.CursorLoader;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
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
import android.widget.LinearLayout;
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

public class MoviesListFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<ArrayList<TmdbMovie>> {
    private static final String TAG = MoviesListFragment.class.getSimpleName();

    private boolean allowClicks = true;
    private String sortOrder = Tmdb.TMDB_SORT_BY_NOW_PLAYING;
    private MoviesFullListAdapter moviesFullListAdapter;
    private int currentPage = 1, loaderId = 0;
    private boolean isLoading, appendToEnd;
    private ArrayList<TmdbMovie> moviesArrayList;
    private Unbinder unbinder;
    private Loader<ArrayList<TmdbMovie>> loader = null;

    private RecyclerView recyclerView;
    private @LayoutRes
    int resource;

    /**
     * Required empty public constructor.
     */
    public MoviesListFragment() {
    }

    /**
     * Factory method to create a new instance of this fragment using the provided parameters.
     *
     * @param recyclerView
     * @param resource
     * @return a new instance of fragment MoviesListFragment.
     */
    public static MoviesListFragment newInstance(RecyclerView recyclerView, @LayoutRes int resource) {
        Bundle bundle = new Bundle();
        bundle.putInt("layoutRes", resource);
        MoviesListFragment fragment = new MoviesListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(resource, container, false);
        //unbinder = ButterKnife.bind(this, rootView);

        // Initialize variables, set the RecyclerView for displaying movie posters and create the
        // AsyncTaskLoader for getting movies lists from TMDB in a separate thread.
        initFragmentData();
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
            loader = new TmdbMoviesAsyncTaskLoader(getContext(),
                    Tmdb.TMDB_SORT_BY_NOW_PLAYING, 1,
                    Locale.getDefault().getLanguage(),
                    Locale.getDefault().getCountry());
        } else {
            // There is no connection. Restart everything and show error message.
            Log.i(TAG, "(onCreateLoader) No internet connection.");
            initFragmentData();
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
        isLoading = false;

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
            }
        } else {
            // There is no connection. Show error message.
            Log.i(TAG, "(onLoadFinished) No connection to internet.");
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

    void initFragmentData() {
        moviesArrayList = new ArrayList<>();
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
                            getLoaderManager().restartLoader(loaderId, null, MoviesListFragment.this);
                        else
                            getLoaderManager().initLoader(loaderId, null, MoviesListFragment.this);
                    }
                }
            }
        });
    }
}