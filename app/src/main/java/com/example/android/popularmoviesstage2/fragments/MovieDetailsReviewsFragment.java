package com.example.android.popularmoviesstage2.fragments;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.adapters.ReviewsAdapter;
import com.example.android.popularmoviesstage2.asynctaskloaders.GenericAsyncTaskLoader;
import com.example.android.popularmoviesstage2.classes.TmdbMovie;
import com.example.android.popularmoviesstage2.classes.TmdbReview;
import com.example.android.popularmoviesstage2.itemdecorations.SpaceItemDecoration;
import com.example.android.popularmoviesstage2.utils.NetworkUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

@SuppressWarnings("unchecked")
public class MovieDetailsReviewsFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Object> {
    private static final String TAG = MovieDetailsReviewsFragment.class.getSimpleName();

    @BindView(R.id.reviews_relative_layout)
    RelativeLayout mainLayout;
    @BindView(R.id.reviews_recycler_view)
    RecyclerView reviewsRecyclerView;
    @BindView(R.id.reviews_no_result_text_view)
    TextView noResultsTextView;
    @BindView(R.id.reviews_no_result_image_view)
    ImageView noResultsImageView;
    @BindView(R.id.reviews_loading_indicator)
    ProgressBar progressBar;
    @BindView(R.id.reviews_swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    private boolean isLoading = false, appendToEnd = true;
    private int movieId, currentPage;
    private ReviewsAdapter reviewsAdapter;
    private Unbinder unbinder;

    /**
     * Required empty public constructor.
     */
    public MovieDetailsReviewsFragment() {
    }

    /**
     * Factory method to create a new instance of this fragment using the provided parameters.
     *
     * @param tmdbMovie is the {@link TmdbMovie} object.
     * @return a new instance of fragment MovieDetailsReviewsFragment.
     */
    public static MovieDetailsReviewsFragment newInstance(TmdbMovie tmdbMovie) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", tmdbMovie.getId());
        MovieDetailsReviewsFragment fragment = new MovieDetailsReviewsFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_movie_details_reviews, container,
                false);
        unbinder = ButterKnife.bind(this, rootView);

        // Get arguments from calling activity.
        if (getArguments() != null) {
            movieId = getArguments().getInt("id");
        }

        // Hide "no results" views.
        noResultsTextView.setVisibility(View.GONE);
        noResultsImageView.setVisibility(View.GONE);

        // Set RecyclerViews for displaying cast & crew photos.
        setRecyclerViews();

        // Create an AsyncTaskLoader for retrieving reviews information from internet in a separate
        // thread. We start at page 1 and we will append reviews to the end of the RecyclerView.
        currentPage = 1;
        appendToEnd = true;
        getLoaderManager().initLoader(NetworkUtils.TMDB_REVIEWS_LOADER_ID, null, this);

        Log.i(methodTag, "Fragment created");
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
            noResultsImageView.setVisibility(View.GONE);
            noResultsTextView.setText(getString(R.string.fetching_movie_reviews));
            noResultsTextView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            Log.i(methodTag, "Movie ID: " + movieId + ". Current page: " + currentPage);
            return new GenericAsyncTaskLoader(getContext(), movieId, currentPage,
                    GenericAsyncTaskLoader.ASYNC_TASK_LOADER_TYPE_TMDB_REVIEWS);
        } else {
            // There is no connection. Show error message.
            progressBar.setVisibility(View.GONE);
            noResultsImageView.setVisibility(View.VISIBLE);
            noResultsTextView.setVisibility(View.VISIBLE);
            noResultsTextView.setText(getString(R.string.no_connection));
            Log.i(methodTag, "No internet connection.");
            return null;
        }
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
     * the {@link CursorAdapter( Context , Cursor, int)} constructor <em>without</em> passing
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
    public void onLoadFinished(Loader<Object> loader, Object data) {
        String methodTag = TAG + "." + Thread.currentThread().getStackTrace()[2].getMethodName();
        
        // Hide progress bar and stop refreshing animation.
        isLoading = false;
        progressBar.setVisibility(View.GONE);
        noResultsTextView.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);

        // Check if there is an available connection.
        if (NetworkUtils.isConnected(getContext())) {
            // If there is a valid list of {@link TmdbReview} objects, then add them to the adapter's
            // data set.
            ArrayList<TmdbReview> reviews = (ArrayList<TmdbReview>)data;
            if (data != null && reviews.size() > 0) {
                Log.i(methodTag, reviews.size() + " review(s) received.");
                reviewsAdapter.updateReviewsArray(reviews, appendToEnd);
                reviewsAdapter.notifyDataSetChanged();
            } else {
                Log.i(methodTag, "No search results.");
                noResultsImageView.setVisibility(View.VISIBLE);
                noResultsTextView.setVisibility(View.VISIBLE);
                noResultsTextView.setText(getString(R.string.no_movie_reviews));
            }
        } else {
            // There is no connection. Show error message.
            Log.i(methodTag, "No connection to internet.");
            noResultsImageView.setVisibility(View.VISIBLE);
            noResultsTextView.setVisibility(View.VISIBLE);
            noResultsTextView.setText(getResources().getString(R.string.no_connection));
        }
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
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
     * Helper method for setting the RecyclerViews in order to display a vertical list of reviews.
     */
    void setRecyclerViews() {
        // Set the LayoutManager for the RecyclerViews.
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        reviewsRecyclerView.setLayoutManager(linearLayoutManager);
        reviewsRecyclerView.setHasFixedSize(true);
        int verticalSeparation = getContext().getResources().getDimensionPixelOffset(R.dimen.regular_padding);
        reviewsRecyclerView.addItemDecoration(new SpaceItemDecoration(verticalSeparation,
                SpaceItemDecoration.VERTICAL_SEPARATION));

        // Set the Adapter for the RecyclerView.
        reviewsAdapter = new ReviewsAdapter(new ArrayList<TmdbReview>());
        reviewsRecyclerView.setAdapter(reviewsAdapter);

        // Listen for scroll changes on the recycler view, in order to know if it is necessary to
        // load another page of results.
        reviewsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                int totalPages = reviewsAdapter.getTotalPages();

                if (!isLoading) {
                    if (currentPage < totalPages &&
                            (visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
                        // Load next page of results. The fetched reviews will be appended at the
                        // end of the RecyclerView.
                        currentPage++;
                        appendToEnd = true;
                        getLoaderManager().restartLoader(NetworkUtils.TMDB_REVIEWS_LOADER_ID,
                                null, MovieDetailsReviewsFragment.this);
                    }
                }
            }
        });

        // Set a listener on the SwipeRefreshLayout that contains the RecyclerViews, just in case we
        // are at the top of the RecyclerViews and we need to reload previous reviews.
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        int currentShownPage = reviewsAdapter.getCurrentPage();

                        if (!isLoading && currentShownPage > 1) {
                            // If we are at the top of the list and we are showing a page number
                            // bigger than 1, we need to reload the previous page of results. The
                            // fetched reviews will be appended to the start of the RecyclerView.
                            currentPage = currentShownPage - 1;
                            appendToEnd = false;
                            getLoaderManager().restartLoader(NetworkUtils.TMDB_REVIEWS_LOADER_ID,
                                    null, MovieDetailsReviewsFragment.this);
                        } else
                            swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
    }
}
