package com.example.android.popularmoviesstage2.fragments;

import android.content.Context;
import android.content.CursorLoader;
import android.content.res.Configuration;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.adapters.CastAdapter;
import com.example.android.popularmoviesstage2.adapters.CrewAdapter;
import com.example.android.popularmoviesstage2.asynctaskloaders.CastCrewAsyncTaskLoader;
import com.example.android.popularmoviesstage2.classes.Cast;
import com.example.android.popularmoviesstage2.classes.CastCrew;
import com.example.android.popularmoviesstage2.classes.Crew;
import com.example.android.popularmoviesstage2.classes.Movie;
import com.example.android.popularmoviesstage2.utils.DisplayUtils;
import com.example.android.popularmoviesstage2.utils.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CastCrewFragment extends Fragment implements LoaderManager.LoaderCallbacks<CastCrew> {
    private static final String TAG = CastCrewFragment.class.getSimpleName();
    private static final int CAST_CREW_LOADER_ID = 2;
    private static CastCrew castCrew;
    @BindView(R.id.cast_crew_title1)
    TextView castCrewTitle1TextView;
    @BindView(R.id.cast_crew_recycler_view1)
    RecyclerView castRecyclerView;
    @BindView(R.id.cast_crew_title2)
    TextView castCrewTitle2TextView;
    @BindView(R.id.cast_crew_recycler_view2)
    RecyclerView crewRecyclerView;
    @BindView(R.id.cast_crew_no_result_text_view)
    TextView noResultsTextView;
    @BindView(R.id.cast_crew_loading_indicator)
    ProgressBar progressBar;

    private int movieId;
    private CastAdapter castAdapter;
    private CrewAdapter crewAdapter;

    /**
     * Required empty public constructor.
     */
    public CastCrewFragment() {
    }

    /**
     * Factory method to create a new instance of this fragment using the provided parameters.
     *
     * @param movie is the {@link Movie} object.
     * @return a new instance of fragment CastCrewFragment.
     */
    public static CastCrewFragment newInstance(Movie movie) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", movie.getId());
        CastCrewFragment fragment = new CastCrewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cast_crew, container, false);
        ButterKnife.bind(this, rootView);

        // Get arguments from calling activity.
        if (getArguments() != null) {
            movieId = getArguments().getInt("id");
        }

        // Set left paddings if the device is in portrait orientation.
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            castCrewTitle1TextView.setPadding(getResources().getDimensionPixelSize(R.dimen.regular_padding), 0, 0, 0);
            castCrewTitle2TextView.setPadding(getResources().getDimensionPixelSize(R.dimen.regular_padding), 0, 0, 0);
        }

        // Set RecyclerViews for displaying cast & crew photos.
        setRecyclerViews();

        // Create an AsyncTaskLoader for retrieving cast & crew information from internet in a
        // separate thread.
        getLoaderManager().initLoader(CAST_CREW_LOADER_ID, null, this);

        Log.i(TAG, "(onCreate) Fragment created");
        return rootView;
    }

    /**
     * Helper method for setting the RecyclerViews in order to display a list of persons with a grid
     * arrangement.
     */
    void setRecyclerViews() {
        // Get current display metrics, depending on device rotation.
        DisplayUtils displayUtils = new DisplayUtils(getContext());

        // Set the listeners for click events in the CastAdapters.
        CastAdapter.OnItemClickListener castListener = new CastAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Cast item) {
                Toast.makeText(getContext(), "Cast item clicked", Toast.LENGTH_SHORT).show();

                /*// Start "MovieDetailsActivity" activity to show movie details when the current element is
                // clicked.
                Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
                intent.putExtra("sortOrder", sortOrder);
                intent.putExtra("sortOrderText", getSortOrderText());
                intent.putExtra("movie", movie);
                currentPosition = movie.getPosition();

                // Set sizes for the poster in the movie details activity.
                int detailsPosterHeightPixels = 750 * detailsPosterlWidthPixels / 500;
                Log.i(TAG, "(setRecyclerViews) Poster width for movie details activity: " + detailsPosterlWidthPixels);
                Log.i(TAG, "(setRecyclerViews) Poster height for movie details activity: " + detailsPosterHeightPixels);
                intent.putExtra("widthPixels", detailsPosterlWidthPixels);
                intent.putExtra("heightPixels", detailsPosterHeightPixels);

                startActivity(intent);*/
            }
        };
        CrewAdapter.OnItemClickListener crewListener = new CrewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Crew item) {
                Toast.makeText(getContext(), "Crew item clicked", Toast.LENGTH_SHORT).show();
            }
        };

        // Set the LayoutManager for the RecyclerViews.
        castRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        crewRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        castRecyclerView.setHasFixedSize(true);
        crewRecyclerView.setHasFixedSize(true);

        // Set the Adapters for the RecyclerViews, according to the current display size and
        // orientation.
        castAdapter = new CastAdapter(new ArrayList<Cast>(), castListener);
        crewAdapter = new CrewAdapter(new ArrayList<Crew>(), crewListener);
        castRecyclerView.setAdapter(castAdapter);
        crewRecyclerView.setAdapter(crewAdapter);
    }

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<CastCrew> onCreateLoader(int id, Bundle args) {
        if (NetworkUtils.isConnected(getContext())) {
            // There is an available connection. Fetch results from TMDB.
            progressBar.setVisibility(View.VISIBLE);
            noResultsTextView.setVisibility(View.INVISIBLE);
            URL searchURL = NetworkUtils.buildFetchCastCrewListURL(movieId);
            Log.i(TAG, "(onCreateLoader) Search URL: " + searchURL.toString());
            return new CastCrewAsyncTaskLoader(getContext(), searchURL);
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
    public void onLoadFinished(Loader<CastCrew> loader, CastCrew data) {
        // Hide progress bar.
        progressBar.setVisibility(View.INVISIBLE);

        // Check if there is an available connection.
        if (NetworkUtils.isConnected(getContext())) {
            // If there is a valid list of {@link Movie}s, then add them to the adapter's data set.
            if (data != null) {
                Log.i(TAG, "(onLoadFinished) Search results not null.");
                castAdapter.setCastArray(data.getCast());
                crewAdapter.setCrewArray(data.getCrew());
                castAdapter.notifyDataSetChanged();
                crewAdapter.notifyDataSetChanged();

                // Restore last currentPosition in the grid, if previously saved. This won't work if we
                // try to restore currentPosition before having displayed the results into the adapter.
                //recyclerView.getLayoutManager().scrollToPosition(currentPosition);
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
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<CastCrew> loader) {
    }
}
