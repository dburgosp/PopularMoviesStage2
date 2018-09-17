package com.example.android.popularmoviesstage2.fragments;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.activities.FullSizeImageActivity;
import com.example.android.popularmoviesstage2.adapters.ImagesAdapter;
import com.example.android.popularmoviesstage2.adapters.VideosAdapter;
import com.example.android.popularmoviesstage2.asynctaskloaders.GenericAsyncTaskLoader;
import com.example.android.popularmoviesstage2.classes.Google;
import com.example.android.popularmoviesstage2.classes.TmdbImage;
import com.example.android.popularmoviesstage2.classes.TmdbMedia;
import com.example.android.popularmoviesstage2.classes.TmdbMovie;
import com.example.android.popularmoviesstage2.classes.TmdbVideo;
import com.example.android.popularmoviesstage2.classes.YouTube;
import com.example.android.popularmoviesstage2.utils.DateTimeUtils;
import com.example.android.popularmoviesstage2.utils.NetworkUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MovieDetailsMediaFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Object> {
    private static final String TAG = MovieDetailsMediaFragment.class.getSimpleName();

    @BindView(R.id.media_no_result_text_view)
    TextView noResultsTextView;
    @BindView(R.id.media_loading_indicator)
    ProgressBar progressBar;
    @BindView(R.id.media_no_result_image_view)
    ImageView noResultsImageView;
    @BindView(R.id.media_no_result_youtube_image_view)
    ImageView noResultsYoutubeImageView;
    @BindView(R.id.media_no_result_google_image_view)
    ImageView noResultsGoogleImageView;

    @BindView(R.id.videos_layout)
    LinearLayout videosLinearLayout;
    @BindView(R.id.videos_title)
    TextView videosTitleTextView;
    @BindView(R.id.videos_view_all)
    TextView viewAllVideosTextView;
    @BindView(R.id.videos_recycler_view)
    RecyclerView videosRecyclerView;
    @BindView(R.id.videos_relative_layout)
    RelativeLayout videosRelativeLayout;

    @BindView(R.id.media_separator_1)
    View separatorView1;

    @BindView(R.id.posters_layout)
    LinearLayout postersLinearLayout;
    @BindView(R.id.posters_title)
    TextView postersTitleTextView;
    @BindView(R.id.posters_view_all)
    TextView viewAllPostersTextView;
    @BindView(R.id.posters_recycler_view)
    RecyclerView postersRecyclerView;
    @BindView(R.id.posters_relative_layout)
    RelativeLayout postersRelativeLayout;

    @BindView(R.id.media_separator_2)
    View separatorView2;

    @BindView(R.id.backdrops_linear_layout)
    LinearLayout backdropsLinearLayout;
    @BindView(R.id.backdrops_title)
    TextView backdropsTitleTextView;
    @BindView(R.id.backdrops_view_all)
    TextView viewAllBackdropsTextView;
    @BindView(R.id.backdrops_recycler_view)
    RecyclerView backdropsRecyclerView;
    @BindView(R.id.backdrops_relative_layout)
    RelativeLayout backdropsRelativeLayout;

    private int movieId;
    private String movieTitle, movieYear;
    private VideosAdapter videosAdapter;
    private ImagesAdapter postersAdapter;
    private ImagesAdapter backdropsAdapter;
    private Unbinder unbinder;

    /**
     * Required empty public constructor.
     */
    public MovieDetailsMediaFragment() {
    }

    /**
     * Factory method to create a new instance of this fragment using the provided parameters.
     *
     * @param tmdbMovie is the {@link TmdbMovie} object.
     * @return a new instance of fragment MovieDetailsMediaFragment.
     */
    public static MovieDetailsMediaFragment newInstance(TmdbMovie tmdbMovie) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", tmdbMovie.getId());
        bundle.putString("title", tmdbMovie.getTitle());
        bundle.putString("year", DateTimeUtils.getYear(tmdbMovie.getRelease_date()));
        MovieDetailsMediaFragment fragment = new MovieDetailsMediaFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details_media, container,
                false);
        unbinder = ButterKnife.bind(this, rootView);

        // Get arguments from calling activity.
        if (getArguments() != null) {
            movieId = getArguments().getInt("id");
            movieTitle = getArguments().getString("title");
            movieYear = getArguments().getString("year");
        }

        // Clean all elements in the layout when creating this fragment.
        clearLayout();

        // Set RecyclerViews for displaying TmdbVideo & crew photos.
        setRecyclerViews();

        // Create AsyncTaskLoaders for retrieving TmdbVideo & TmdbImage information from internet in
        // separate threads.
        getLoaderManager().initLoader(NetworkUtils.TMDB_MEDIA_LOADER_ID, null, this);

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
        if (NetworkUtils.isConnected(getContext())) {
            // There is an available connection. Fetch results from TMDB.
            progressBar.setVisibility(View.VISIBLE);
            noResultsTextView.setText(getString(R.string.fetching_movie_media));
            noResultsTextView.setVisibility(View.VISIBLE);
            Log.i(TAG, "(onCreateLoader) Movie ID: " + movieId);
            return new GenericAsyncTaskLoader(getContext(), movieId,
                    GenericAsyncTaskLoader.ASYNC_TASK_LOADER_TYPE_TMDB_MEDIA);
        } else {
            // There is no connection. Show error message.
            progressBar.setVisibility(View.INVISIBLE);
            noResultsTextView.setVisibility(View.VISIBLE);
            noResultsImageView.setVisibility(View.VISIBLE);
            noResultsTextView.setText(getString(R.string.no_connection));
            Log.i(TAG, "(onCreateLoader) No internet connection.");
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
    public void onLoadFinished(Loader<Object> loader, final Object data) {
        // Hide progress bar.
        progressBar.setVisibility(View.GONE);
        noResultsTextView.setVisibility(View.GONE);

        // Check if there is an available connection.
        if (NetworkUtils.isConnected(getContext())) {
            ArrayList<TmdbVideo> videosArrayList = ((TmdbMedia)data).getTmdbVideos();
            ArrayList<TmdbImage> postersArrayList = ((TmdbMedia)data).getPosters();
            ArrayList<TmdbImage> backdropsArrayList = ((TmdbMedia)data).getBackdrops();
            if ((videosArrayList != null && videosArrayList.size() > 0) ||
                    (postersArrayList != null && postersArrayList.size() > 0) ||
                    (backdropsArrayList != null && backdropsArrayList.size() > 0)) {
                // If there is a valid {@link TmdbMedia} object, then add this information to the
                // adapters' data sets.
                Log.i(TAG, "(onLoadFinished) Search results not null.");

                // Show videos, posters and backdrops.
                setVideos(videosArrayList);
                setPosters(postersArrayList);
                setBackdrops(backdropsArrayList);
            } else {
                // Loader has not returned a valid list of {@link TmdbMedia} objects.
                Log.i(TAG, "(onLoadFinished) No search results.");
                noResultsTextView.setText(getString(R.string.no_movie_media));
                noResultsTextView.setVisibility(View.VISIBLE);
                noResultsImageView.setVisibility(View.VISIBLE);
                noResultsYoutubeImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Implicit intent for searching videos from the title of the movie on
                        // YouTube.
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(YouTube.YOUTUBE_SEARCH_VIDEOS_URL + movieTitle + " " +
                                        movieYear));
                        startActivity(intent);
                    }
                });
                noResultsYoutubeImageView.setVisibility(View.VISIBLE);
                noResultsGoogleImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Implicit intent for searching images from the title of the movie on
                        // Google.
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(Google.GOOGLE_LOOK_FOR_IMAGES_URL + movieTitle + " " +
                                        movieYear));
                        startActivity(intent);
                    }
                });
                noResultsGoogleImageView.setVisibility(View.VISIBLE);
            }
        } else {
            // There is no connection. Show error message.
            Log.i(TAG, "(onLoadFinished) No connection to internet.");
            noResultsTextView.setVisibility(View.VISIBLE);
            noResultsImageView.setVisibility(View.VISIBLE);
            noResultsTextView.setText(getString(R.string.no_connection));

            // Hide Videos and crew sections.
            videosLinearLayout.setVisibility(View.GONE);
            separatorView1.setVisibility(View.GONE);
            postersLinearLayout.setVisibility(View.GONE);
            separatorView2.setVisibility(View.GONE);
            backdropsLinearLayout.setVisibility(View.GONE);
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
    }

    /* -------------- */
    /* HELPER METHODS */
    /* -------------- */

    /**
     * Helper method for setting the videos section into the media page.
     *
     * @param videosArrayList is the list of videos.
     */
    private void setVideos(ArrayList<TmdbVideo> videosArrayList) {
        if (videosArrayList != null && videosArrayList.size() > 0) {
            // Add the array of elements to the adapter.
            videosAdapter.setVideoArray(videosArrayList);
            videosAdapter.notifyDataSetChanged();

            // Set "view all" button.
            String viewAllText = getString(R.string.all_languages) + " (" + videosArrayList.size() + ")";
            viewAllVideosTextView.setText(viewAllText);

            // Show this section.
            videosLinearLayout.setVisibility(View.VISIBLE);
            separatorView1.setVisibility(View.VISIBLE);
        } else {
            // Hide section if there is no TmdbVideo information for this movie.
            videosLinearLayout.setVisibility(View.GONE);
            separatorView1.setVisibility(View.GONE);
        }
    }

    /**
     * Helper method for setting the posters section into the media page.
     *
     * @param postersArrayList is the list of posters.
     */
    private void setPosters(ArrayList<TmdbImage> postersArrayList) {
        if (postersArrayList != null && postersArrayList.size() > 0) {
            // Add the array of elements to the adapter.
            postersAdapter.setImageArray(postersArrayList);
            postersAdapter.notifyDataSetChanged();

            // Set "view all" button.
            String viewAllText = getString(R.string.all_languages) + " (" + postersArrayList.size() + ")";
            viewAllPostersTextView.setText(viewAllText);

            // Show this section.
            separatorView1.setVisibility(View.VISIBLE);
            postersLinearLayout.setVisibility(View.VISIBLE);
        } else {
            // Hide section if there is no crew information for this movie.
            separatorView1.setVisibility(View.GONE);
            postersLinearLayout.setVisibility(View.GONE);
        }
    }

    /**
     * Helper method for setting the backdrops section into the media page.
     *
     * @param backdropsArrayList is the list of backdrops.
     */
    private void setBackdrops(ArrayList<TmdbImage> backdropsArrayList) {
        if (backdropsArrayList != null && backdropsArrayList.size() > 0) {
            // Add the array of elements to the adapter.
            backdropsAdapter.setImageArray(backdropsArrayList);
            backdropsAdapter.notifyDataSetChanged();

            // Set "view all" button.
            String viewAllText = getString(R.string.all_languages) + " (" + backdropsArrayList.size() + ")";
            viewAllBackdropsTextView.setText(viewAllText);

            // Show this section.
            separatorView2.setVisibility(View.VISIBLE);
            backdropsLinearLayout.setVisibility(View.VISIBLE);
        } else {
            // Hide section if there is no backdrop information for this movie.
            separatorView2.setVisibility(View.GONE);
            backdropsLinearLayout.setVisibility(View.GONE);
        }
    }

    /**
     * Hide every info element in the layout.
     */
    private void clearLayout() {
        videosLinearLayout.setVisibility(View.GONE);
        postersLinearLayout.setVisibility(View.GONE);
        backdropsLinearLayout.setVisibility(View.GONE);
        noResultsTextView.setVisibility(View.GONE);
        noResultsImageView.setVisibility(View.GONE);
        noResultsYoutubeImageView.setVisibility(View.GONE);
        noResultsGoogleImageView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    /**
     * Helper method for setting the RecyclerViews in order to display lists of videos and images
     * with a horizontal arrangement.
     */
    private void setRecyclerViews() {
        // Set the LayoutManagers for the RecyclerViews.
        videosRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        postersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        backdropsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        videosRecyclerView.setHasFixedSize(true);
        postersRecyclerView.setHasFixedSize(true);
        backdropsRecyclerView.setHasFixedSize(true);

        // Set the listener for click events in the videos adapter.
        VideosAdapter.OnItemClickListener videoListener = new VideosAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TmdbVideo item) {
                // Implicit intent for playing the YouTube video.
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(YouTube.YOUTUBE_WATCH_VIDEO_URL + item.getKey()));
                startActivity(intent);
            }
        };

        // Set the listener for click events in the posters adapter.
        final ImagesAdapter.OnItemClickListener posterListener = new ImagesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TmdbImage item) {
                // Explicit intent to open FullSizeImageActivity and show current poster at full
                // screen.
                Intent intent = new Intent(getContext(), FullSizeImageActivity.class);
                intent.putExtra(FullSizeImageActivity.EXTRA_PARAM_IMAGES_ARRAYLIST,
                        postersAdapter.getImagesArrayList());
                intent.putExtra(FullSizeImageActivity.EXTRA_PARAM_CURRENT_IMAGE,
                        item.getPosition());
                intent.putExtra(FullSizeImageActivity.EXTRA_PARAM_IMAGE_TYPE,
                        FullSizeImageActivity.IMAGE_TYPE_POSTER);
                startActivity(intent);
            }
        };

        // Set the listener for click events in the backdrops adapter.
        final ImagesAdapter.OnItemClickListener backdropListener = new ImagesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TmdbImage item) {
                // Explicit intent to open FullSizeImageActivity and show current poster at full
                // screen.
                Intent intent = new Intent(getContext(), FullSizeImageActivity.class);
                intent.putExtra(FullSizeImageActivity.EXTRA_PARAM_IMAGES_ARRAYLIST,
                        backdropsAdapter.getImagesArrayList());
                intent.putExtra(FullSizeImageActivity.EXTRA_PARAM_CURRENT_IMAGE,
                        item.getPosition());
                intent.putExtra(FullSizeImageActivity.EXTRA_PARAM_IMAGE_TYPE,
                        FullSizeImageActivity.IMAGE_TYPE_BACKDROP);
                startActivity(intent);
            }
        };

        // Set the Adapters for the RecyclerViews.
        videosAdapter = new VideosAdapter(new ArrayList<TmdbVideo>(), videoListener);
        videosRecyclerView.setAdapter(videosAdapter);
        postersAdapter = new ImagesAdapter(new ArrayList<TmdbImage>(),
                FullSizeImageActivity.IMAGE_TYPE_POSTER, posterListener);
        postersRecyclerView.setAdapter(postersAdapter);
        backdropsAdapter = new ImagesAdapter(new ArrayList<TmdbImage>(),
                FullSizeImageActivity.IMAGE_TYPE_BACKDROP, backdropListener);
        backdropsRecyclerView.setAdapter(backdropsAdapter);
    }
}
