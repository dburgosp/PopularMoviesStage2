package com.example.android.popularmoviesstage2.fragments;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.adapters.BackdropsAdapter;
import com.example.android.popularmoviesstage2.adapters.PostersAdapter;
import com.example.android.popularmoviesstage2.adapters.VideosAdapter;
import com.example.android.popularmoviesstage2.asynctaskloaders.MediaAsyncTaskLoader;
import com.example.android.popularmoviesstage2.classes.Image;
import com.example.android.popularmoviesstage2.classes.Media;
import com.example.android.popularmoviesstage2.classes.Movie;
import com.example.android.popularmoviesstage2.classes.Video;
import com.example.android.popularmoviesstage2.utils.NetworkUtils;
import com.example.android.popularmoviesstage2.utils.TextUtils;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MediaFragment extends Fragment implements LoaderManager.LoaderCallbacks<Media> {
    private static final String TAG = MediaFragment.class.getSimpleName();
    private static final int VIDEO_LOADER_ID = 4;
    private static final int VIDEO_MAX_ELEMENTS = 10;
    private static final int IMAGE_MAX_ELEMENTS = 10;

    @BindView(R.id.media_no_result_text_view)
    TextView noResultsTextView;
    @BindView(R.id.media_loading_indicator)
    ProgressBar progressBar;

    @BindView(R.id.videos_linear_layout)
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

    @BindView(R.id.posters_linear_layout)
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
    private VideosAdapter videosAdapter;
    private PostersAdapter postersAdapter;
    private BackdropsAdapter backdropsAdapter;

    /**
     * Required empty public constructor.
     */
    public MediaFragment() {
    }

    /**
     * Factory method to create a new instance of this fragment using the provided parameters.
     *
     * @param movie is the {@link Movie} object.
     * @return a new instance of fragment MediaFragment.
     */
    public static MediaFragment newInstance(Movie movie) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", movie.getId());
        MediaFragment fragment = new MediaFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_media, container, false);
        ButterKnife.bind(this, rootView);

        // Get arguments from calling activity.
        if (getArguments() != null) {
            movieId = getArguments().getInt("id");
        }

        // Set RecyclerViews for displaying Video & crew photos.
        setRecyclerViews();

        // Create AsyncTaskLoaders for retrieving Video & Image information from internet in
        // separate threads.
        getLoaderManager().initLoader(VIDEO_LOADER_ID, null, this);

        Log.i(TAG, "(onCreate) Fragment created");
        return rootView;
    }

    /**
     * Helper method for setting the RecyclerViews in order to display lists of videos and images 
     * with a horizontal arrangement.
     */
    void setRecyclerViews() {
        // Set the LayoutManagers for the RecyclerViews.
        videosRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        postersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        backdropsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        videosRecyclerView.setHasFixedSize(true);
        postersRecyclerView.setHasFixedSize(true);
        backdropsRecyclerView.setHasFixedSize(true);

        // Set the listeners for click events in the adapters.
        VideosAdapter.OnItemClickListener videoListener = new VideosAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Video item) {
                Toast.makeText(getContext(), "Video item clicked", Toast.LENGTH_SHORT).show();
            }
        };
        PostersAdapter.OnItemClickListener posterListener = new PostersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Image item) {
                Toast.makeText(getContext(), "Poster item clicked", Toast.LENGTH_SHORT).show();
            }
        };
        BackdropsAdapter.OnItemClickListener backdropListener = new BackdropsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Image item) {
                Toast.makeText(getContext(), "Backdrop item clicked", Toast.LENGTH_SHORT).show();
            }
        };

        // Set the Adapters for the RecyclerViews.
        videosAdapter = new VideosAdapter(new ArrayList<Video>(), videoListener);
        postersAdapter = new PostersAdapter(new ArrayList<Image>(), posterListener);
        backdropsAdapter = new BackdropsAdapter(new ArrayList<Image>(), backdropListener);
        videosRecyclerView.setAdapter(videosAdapter);
        postersRecyclerView.setAdapter(postersAdapter);
        backdropsRecyclerView.setAdapter(backdropsAdapter);
    }

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<Media> onCreateLoader(int id, Bundle args) {
        if (NetworkUtils.isConnected(getContext())) {
            // There is an available connection. Fetch results from TMDB.
            progressBar.setVisibility(View.VISIBLE);
            noResultsTextView.setVisibility(View.INVISIBLE);
            URL searchURL = NetworkUtils.buildFetchMediaListURL(movieId);
            Log.i(TAG, "(onCreateLoader) Search URL: " + searchURL.toString());
            return new MediaAsyncTaskLoader(getContext(), searchURL);
        } else {
            // There is no connection. Show error message.
            progressBar.setVisibility(View.INVISIBLE);
            noResultsTextView.setVisibility(View.VISIBLE);
            noResultsTextView.setText(getResources().getString(R.string.no_connection));
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
     * the {@link CursorAdapter(Context, * Cursor, int)} constructor <em>without</em> passing
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
    public void onLoadFinished(Loader<Media> loader, Media data) {
        // Hide progress bar.
        progressBar.setVisibility(View.INVISIBLE);

        // Check if there is an available connection.
        if (NetworkUtils.isConnected(getContext())) {
            if (data != null) {
                // If there is a valid {@link Media} object, then add this information to the
                // adapters' data sets.
                Log.i(TAG, "(onLoadFinished) Search results not null.");

                /* -------------- */
                /* List of videos */
                /* -------------- */
                ArrayList<Video> videosArrayList = data.getVideos();
                if (videosArrayList != null && videosArrayList.size() > 0) {
                    // Add the array of elements to the adapter.
                    videosAdapter.setVideoArray(videosArrayList);
                    videosAdapter.notifyDataSetChanged();

                    // Set "view all" button.
                    String viewAllText = "<strong>" + getResources().getString(R.string.view_all) +
                            "</strong> (" + videosArrayList.size() + ")";
                    TextUtils.setHtmlText(viewAllVideosTextView, viewAllText);
                } else {
                    // Hide section if there is no Video information for this movie.
                    videosLinearLayout.setVisibility(View.GONE);
                    separatorView1.setVisibility(View.GONE);
                }

                /* --------------- */
                /* List of posters */
                /* --------------- */
                ArrayList<Image> postersArrayList = data.getPosters();
                if (postersArrayList != null && postersArrayList.size() > 0) {
                    // Add the array of elements to the adapter.
                    postersAdapter.setImageArray(postersArrayList);
                    postersAdapter.notifyDataSetChanged();
                    
                    // Set "view all" button.
                    String viewAllText = "<strong>" + getResources().getString(R.string.view_all) +
                            "</strong> (" + postersArrayList.size() + ")";
                    TextUtils.setHtmlText(viewAllPostersTextView, viewAllText);
                } else {
                    // Hide section if there is no crew information for this movie.
                    separatorView1.setVisibility(View.GONE);
                    postersLinearLayout.setVisibility(View.GONE);
                }

                /* ----------------- */
                /* List of backdrops */
                /* ----------------- */
                ArrayList<Image> backdropsArrayList = data.getBackdrops();
                if (backdropsArrayList != null && backdropsArrayList.size() > 0) {
                    // Add the array of elements to the adapter.
                    backdropsAdapter.setImageArray(backdropsArrayList);
                    backdropsAdapter.notifyDataSetChanged();

                    // Set "view all" button.
                    String viewAllText = "<strong>" + getResources().getString(R.string.view_all) +
                            "</strong> (" + backdropsArrayList.size() + ")";
                    TextUtils.setHtmlText(viewAllBackdropsTextView, viewAllText);
                } else {
                    // Hide section if there is no backdrop information for this movie.
                    separatorView2.setVisibility(View.GONE);
                    backdropsLinearLayout.setVisibility(View.GONE);
                }
            } else {
                // Loader has not returned a valid list of {@link Media} objects.
                Log.i(TAG, "(onLoadFinished) No search results.");
                noResultsTextView.setVisibility(View.VISIBLE);
                noResultsTextView.setText(getResources().getString(R.string.no_results));
            }
        } else {
            // There is no connection. Show error message.
            Log.i(TAG, "(onLoadFinished) No connection to internet.");
            noResultsTextView.setVisibility(View.VISIBLE);
            noResultsTextView.setText(getResources().getString(R.string.no_connection));

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
    public void onLoaderReset(Loader<Media> loader) {
    }
}
