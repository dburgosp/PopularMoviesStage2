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

    @BindView(R.id.cast_crew_no_result_text_view)
    TextView noResultsTextView;
    @BindView(R.id.cast_crew_loading_indicator)
    ProgressBar progressBar;
    @BindView(R.id.cast_crew_title1)
    TextView castTitleTextView;
    @BindView(R.id.cast_crew_recycler_view1)
    RecyclerView castRecyclerView;
    @BindView(R.id.cast_crew_title2)
    TextView directingDepartamentTextView;
    @BindView(R.id.cast_crew_recycler_view2)
    RecyclerView directingDepartamentRecyclerView;
    @BindView(R.id.cast_crew_title3)
    TextView productionDepartamentTextView;
    @BindView(R.id.cast_crew_recycler_view3)
    RecyclerView productionDepartamentRecyclerView;
    @BindView(R.id.cast_crew_title4)
    TextView writingDepartamentTextView;
    @BindView(R.id.cast_crew_recycler_view4)
    RecyclerView writingDepartamentRecyclerView;
    @BindView(R.id.cast_crew_title5)
    TextView actorsDepartamentTextView;
    @BindView(R.id.cast_crew_recycler_view5)
    RecyclerView actorsDepartamentRecyclerView;
    @BindView(R.id.cast_crew_title6)
    TextView cameraDepartamentTextView;
    @BindView(R.id.cast_crew_recycler_view6)
    RecyclerView cameraDepartamentRecyclerView;
    @BindView(R.id.cast_crew_title7)
    TextView editingDepartamentTextView;
    @BindView(R.id.cast_crew_recycler_view7)
    RecyclerView editingDepartamentRecyclerView;
    @BindView(R.id.cast_crew_title8)
    TextView artDepartamentTextView;
    @BindView(R.id.cast_crew_recycler_view8)
    RecyclerView artDepartamentRecyclerView;
    @BindView(R.id.cast_crew_title9)
    TextView costumeMakeupDepartamentTextView;
    @BindView(R.id.cast_crew_recycler_view9)
    RecyclerView costumeMakeupDepartamentRecyclerView;
    @BindView(R.id.cast_crew_title10)
    TextView soundDepartamentTextView;
    @BindView(R.id.cast_crew_recycler_view10)
    RecyclerView soundDepartamentRecyclerView;
    @BindView(R.id.cast_crew_title11)
    TextView visualEffectsDepartamentTextView;
    @BindView(R.id.cast_crew_recycler_view11)
    RecyclerView visualEffectsDepartamentRecyclerView;
    @BindView(R.id.cast_crew_title12)
    TextView crewDepartamentTextView;
    @BindView(R.id.cast_crew_recycler_view12)
    RecyclerView crewDepartamentRecyclerView;
    @BindView(R.id.cast_crew_title13)
    TextView otherDepartamentTextView;
    @BindView(R.id.cast_crew_recycler_view13)
    RecyclerView otherDepartamentRecyclerView;

    private int movieId;
    private CastAdapter castAdapter;
    private CrewAdapter directingDepartamentAdapter;
    private CrewAdapter productionDepartamentAdapter;
    private CrewAdapter writingDepartamentAdapter;
    private CrewAdapter actorsDepartamentAdapter;
    private CrewAdapter cameraDepartamentAdapter;
    private CrewAdapter editingDepartamentAdapter;
    private CrewAdapter artDepartamentAdapter;
    private CrewAdapter costumeMakeupDepartamentAdapter;
    private CrewAdapter soundDepartamentAdapter;
    private CrewAdapter visualEffectsDepartamentAdapter;
    private CrewAdapter crewDepartamentAdapter;
    private CrewAdapter otherDepartamentAdapter;

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
            castTitleTextView.setPadding(getResources().getDimensionPixelSize(R.dimen.regular_padding), 0, 0, 0);
            directingDepartamentTextView.setPadding(getResources().getDimensionPixelSize(R.dimen.regular_padding), 0, 0, 0);
            productionDepartamentTextView.setPadding(getResources().getDimensionPixelSize(R.dimen.regular_padding), 0, 0, 0);
            writingDepartamentTextView.setPadding(getResources().getDimensionPixelSize(R.dimen.regular_padding), 0, 0, 0);
            actorsDepartamentTextView.setPadding(getResources().getDimensionPixelSize(R.dimen.regular_padding), 0, 0, 0);
            cameraDepartamentTextView.setPadding(getResources().getDimensionPixelSize(R.dimen.regular_padding), 0, 0, 0);
            editingDepartamentTextView.setPadding(getResources().getDimensionPixelSize(R.dimen.regular_padding), 0, 0, 0);
            artDepartamentTextView.setPadding(getResources().getDimensionPixelSize(R.dimen.regular_padding), 0, 0, 0);
            costumeMakeupDepartamentTextView.setPadding(getResources().getDimensionPixelSize(R.dimen.regular_padding), 0, 0, 0);
            soundDepartamentTextView.setPadding(getResources().getDimensionPixelSize(R.dimen.regular_padding), 0, 0, 0);
            visualEffectsDepartamentTextView.setPadding(getResources().getDimensionPixelSize(R.dimen.regular_padding), 0, 0, 0);
            crewDepartamentTextView.setPadding(getResources().getDimensionPixelSize(R.dimen.regular_padding), 0, 0, 0);
            otherDepartamentTextView.setPadding(getResources().getDimensionPixelSize(R.dimen.regular_padding), 0, 0, 0);
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
        directingDepartamentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        productionDepartamentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        writingDepartamentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        actorsDepartamentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        cameraDepartamentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        editingDepartamentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        artDepartamentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        costumeMakeupDepartamentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        soundDepartamentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        visualEffectsDepartamentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        crewDepartamentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        otherDepartamentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        castRecyclerView.setHasFixedSize(true);
        directingDepartamentRecyclerView.setHasFixedSize(true);
        productionDepartamentRecyclerView.setHasFixedSize(true);
        writingDepartamentRecyclerView.setHasFixedSize(true);
        actorsDepartamentRecyclerView.setHasFixedSize(true);
        cameraDepartamentRecyclerView.setHasFixedSize(true);
        editingDepartamentRecyclerView.setHasFixedSize(true);
        artDepartamentRecyclerView.setHasFixedSize(true);
        costumeMakeupDepartamentRecyclerView.setHasFixedSize(true);
        soundDepartamentRecyclerView.setHasFixedSize(true);
        visualEffectsDepartamentRecyclerView.setHasFixedSize(true);
        crewDepartamentRecyclerView.setHasFixedSize(true);
        otherDepartamentRecyclerView.setHasFixedSize(true);

        // Set the Adapters for the RecyclerViews.
        castAdapter = new CastAdapter(new ArrayList<Cast>(), castListener);
        directingDepartamentAdapter = new CrewAdapter(new ArrayList<Crew>(), crewListener);
        productionDepartamentAdapter = new CrewAdapter(new ArrayList<Crew>(), crewListener);
        writingDepartamentAdapter = new CrewAdapter(new ArrayList<Crew>(), crewListener);
        actorsDepartamentAdapter = new CrewAdapter(new ArrayList<Crew>(), crewListener);
        cameraDepartamentAdapter = new CrewAdapter(new ArrayList<Crew>(), crewListener);
        editingDepartamentAdapter = new CrewAdapter(new ArrayList<Crew>(), crewListener);
        artDepartamentAdapter = new CrewAdapter(new ArrayList<Crew>(), crewListener);
        costumeMakeupDepartamentAdapter = new CrewAdapter(new ArrayList<Crew>(), crewListener);
        soundDepartamentAdapter = new CrewAdapter(new ArrayList<Crew>(), crewListener);
        visualEffectsDepartamentAdapter = new CrewAdapter(new ArrayList<Crew>(), crewListener);
        crewDepartamentAdapter = new CrewAdapter(new ArrayList<Crew>(), crewListener);
        otherDepartamentAdapter = new CrewAdapter(new ArrayList<Crew>(), crewListener);

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
            // If there is a valid list of {@link CastCrew} objectss, then add them to the adapters'
            // data sets.
            if (data != null) {
                Log.i(TAG, "(onLoadFinished) Search results not null.");
                castAdapter.setCastArray(data.getCast());
                castAdapter.notifyDataSetChanged();

                //
                crewAdapter.setCrewArray(data.getCrew());
                crewAdapter.notifyDataSetChanged();
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
