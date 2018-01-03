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
    TextView directingDepartmentTextView;
    @BindView(R.id.cast_crew_recycler_view2)
    RecyclerView directingDepartmentRecyclerView;
    @BindView(R.id.cast_crew_title3)
    TextView productionDepartmentTextView;
    @BindView(R.id.cast_crew_recycler_view3)
    RecyclerView productionDepartmentRecyclerView;
    @BindView(R.id.cast_crew_title4)
    TextView writingDepartmentTextView;
    @BindView(R.id.cast_crew_recycler_view4)
    RecyclerView writingDepartmentRecyclerView;
    @BindView(R.id.cast_crew_title5)
    TextView actorsDepartmentTextView;
    @BindView(R.id.cast_crew_recycler_view5)
    RecyclerView actorsDepartmentRecyclerView;
    @BindView(R.id.cast_crew_title6)
    TextView cameraDepartmentTextView;
    @BindView(R.id.cast_crew_recycler_view6)
    RecyclerView cameraDepartmentRecyclerView;
    @BindView(R.id.cast_crew_title7)
    TextView editingDepartmentTextView;
    @BindView(R.id.cast_crew_recycler_view7)
    RecyclerView editingDepartmentRecyclerView;
    @BindView(R.id.cast_crew_title8)
    TextView artDepartmentTextView;
    @BindView(R.id.cast_crew_recycler_view8)
    RecyclerView artDepartmentRecyclerView;
    @BindView(R.id.cast_crew_title9)
    TextView costumeMakeupDepartmentTextView;
    @BindView(R.id.cast_crew_recycler_view9)
    RecyclerView costumeMakeupDepartmentRecyclerView;
    @BindView(R.id.cast_crew_title10)
    TextView soundDepartmentTextView;
    @BindView(R.id.cast_crew_recycler_view10)
    RecyclerView soundDepartmentRecyclerView;
    @BindView(R.id.cast_crew_title11)
    TextView visualEffectsDepartmentTextView;
    @BindView(R.id.cast_crew_recycler_view11)
    RecyclerView visualEffectsDepartmentRecyclerView;
    @BindView(R.id.cast_crew_title12)
    TextView crewDepartmentTextView;
    @BindView(R.id.cast_crew_recycler_view12)
    RecyclerView crewDepartmentRecyclerView;
    @BindView(R.id.cast_crew_title13)
    TextView lightingDepartmentTextView;
    @BindView(R.id.cast_crew_recycler_view13)
    RecyclerView lightingDepartmentRecyclerView;
    @BindView(R.id.cast_crew_title14)
    TextView otherDepartmentTextView;
    @BindView(R.id.cast_crew_recycler_view14)
    RecyclerView otherDepartmentRecyclerView;

    private int movieId;
    private CastAdapter castAdapter;
    private CrewAdapter directingDepartmentAdapter;
    private CrewAdapter productionDepartmentAdapter;
    private CrewAdapter writingDepartmentAdapter;
    private CrewAdapter actorsDepartmentAdapter;
    private CrewAdapter cameraDepartmentAdapter;
    private CrewAdapter editingDepartmentAdapter;
    private CrewAdapter artDepartmentAdapter;
    private CrewAdapter costumeMakeupDepartmentAdapter;
    private CrewAdapter soundDepartmentAdapter;
    private CrewAdapter visualEffectsDepartmentAdapter;
    private CrewAdapter crewDepartmentAdapter;
    private CrewAdapter lightingDepartmentAdapter;
    private CrewAdapter otherDepartmentAdapter;

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
            directingDepartmentTextView.setPadding(getResources().getDimensionPixelSize(R.dimen.regular_padding), 0, 0, 0);
            productionDepartmentTextView.setPadding(getResources().getDimensionPixelSize(R.dimen.regular_padding), 0, 0, 0);
            writingDepartmentTextView.setPadding(getResources().getDimensionPixelSize(R.dimen.regular_padding), 0, 0, 0);
            actorsDepartmentTextView.setPadding(getResources().getDimensionPixelSize(R.dimen.regular_padding), 0, 0, 0);
            cameraDepartmentTextView.setPadding(getResources().getDimensionPixelSize(R.dimen.regular_padding), 0, 0, 0);
            editingDepartmentTextView.setPadding(getResources().getDimensionPixelSize(R.dimen.regular_padding), 0, 0, 0);
            artDepartmentTextView.setPadding(getResources().getDimensionPixelSize(R.dimen.regular_padding), 0, 0, 0);
            costumeMakeupDepartmentTextView.setPadding(getResources().getDimensionPixelSize(R.dimen.regular_padding), 0, 0, 0);
            soundDepartmentTextView.setPadding(getResources().getDimensionPixelSize(R.dimen.regular_padding), 0, 0, 0);
            visualEffectsDepartmentTextView.setPadding(getResources().getDimensionPixelSize(R.dimen.regular_padding), 0, 0, 0);
            crewDepartmentTextView.setPadding(getResources().getDimensionPixelSize(R.dimen.regular_padding), 0, 0, 0);
            lightingDepartmentTextView.setPadding(getResources().getDimensionPixelSize(R.dimen.regular_padding), 0, 0, 0);
            otherDepartmentTextView.setPadding(getResources().getDimensionPixelSize(R.dimen.regular_padding), 0, 0, 0);
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

        // Set the LayoutManager for the RecyclerViews.
        castRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        directingDepartmentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        productionDepartmentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        writingDepartmentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        actorsDepartmentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        cameraDepartmentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        editingDepartmentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        artDepartmentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        costumeMakeupDepartmentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        soundDepartmentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        visualEffectsDepartmentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        crewDepartmentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        lightingDepartmentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        otherDepartmentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        castRecyclerView.setHasFixedSize(true);
        directingDepartmentRecyclerView.setHasFixedSize(true);
        productionDepartmentRecyclerView.setHasFixedSize(true);
        writingDepartmentRecyclerView.setHasFixedSize(true);
        actorsDepartmentRecyclerView.setHasFixedSize(true);
        cameraDepartmentRecyclerView.setHasFixedSize(true);
        editingDepartmentRecyclerView.setHasFixedSize(true);
        artDepartmentRecyclerView.setHasFixedSize(true);
        costumeMakeupDepartmentRecyclerView.setHasFixedSize(true);
        soundDepartmentRecyclerView.setHasFixedSize(true);
        visualEffectsDepartmentRecyclerView.setHasFixedSize(true);
        crewDepartmentRecyclerView.setHasFixedSize(true);
        lightingDepartmentRecyclerView.setHasFixedSize(true);
        otherDepartmentRecyclerView.setHasFixedSize(true);

        // Set the listeners for click events in the CastAdapters.
        CastAdapter.OnItemClickListener castListener = new CastAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Cast item) {
                Toast.makeText(getContext(), "Cast item clicked", Toast.LENGTH_SHORT).show();
            }
        };
        CrewAdapter.OnItemClickListener crewListener = new CrewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Crew item) {
                Toast.makeText(getContext(), "Crew item clicked", Toast.LENGTH_SHORT).show();
            }
        };

        // Set the Adapters for the RecyclerViews.
        castAdapter = new CastAdapter(new ArrayList<Cast>(), castListener);
        directingDepartmentAdapter = new CrewAdapter(new ArrayList<Crew>(), crewListener);
        productionDepartmentAdapter = new CrewAdapter(new ArrayList<Crew>(), crewListener);
        writingDepartmentAdapter = new CrewAdapter(new ArrayList<Crew>(), crewListener);
        actorsDepartmentAdapter = new CrewAdapter(new ArrayList<Crew>(), crewListener);
        cameraDepartmentAdapter = new CrewAdapter(new ArrayList<Crew>(), crewListener);
        editingDepartmentAdapter = new CrewAdapter(new ArrayList<Crew>(), crewListener);
        artDepartmentAdapter = new CrewAdapter(new ArrayList<Crew>(), crewListener);
        costumeMakeupDepartmentAdapter = new CrewAdapter(new ArrayList<Crew>(), crewListener);
        soundDepartmentAdapter = new CrewAdapter(new ArrayList<Crew>(), crewListener);
        visualEffectsDepartmentAdapter = new CrewAdapter(new ArrayList<Crew>(), crewListener);
        crewDepartmentAdapter = new CrewAdapter(new ArrayList<Crew>(), crewListener);
        lightingDepartmentAdapter = new CrewAdapter(new ArrayList<Crew>(), crewListener);
        otherDepartmentAdapter = new CrewAdapter(new ArrayList<Crew>(), crewListener);

        castRecyclerView.setAdapter(castAdapter);
        directingDepartmentRecyclerView.setAdapter(directingDepartmentAdapter);
        productionDepartmentRecyclerView.setAdapter(productionDepartmentAdapter);
        writingDepartmentRecyclerView.setAdapter(writingDepartmentAdapter);
        actorsDepartmentRecyclerView.setAdapter(actorsDepartmentAdapter);
        cameraDepartmentRecyclerView.setAdapter(cameraDepartmentAdapter);
        editingDepartmentRecyclerView.setAdapter(editingDepartmentAdapter);
        artDepartmentRecyclerView.setAdapter(artDepartmentAdapter);
        costumeMakeupDepartmentRecyclerView.setAdapter(costumeMakeupDepartmentAdapter);
        soundDepartmentRecyclerView.setAdapter(soundDepartmentAdapter);
        visualEffectsDepartmentRecyclerView.setAdapter(visualEffectsDepartmentAdapter);
        crewDepartmentRecyclerView.setAdapter(crewDepartmentAdapter);
        lightingDepartmentRecyclerView.setAdapter(lightingDepartmentAdapter);
        otherDepartmentRecyclerView.setAdapter(otherDepartmentAdapter);
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
            if (data != null) {
                // If there is a valid list of {@link CastCrew} objects, then add them to the
                // adapters' data sets.
                Log.i(TAG, "(onLoadFinished) Search results not null.");

                // Cast of characters.
                ArrayList<Cast> castArrayList = data.getCast();
                if (castArrayList != null && castArrayList.size() > 0) {
                    castAdapter.setCastArray(castArrayList);
                    castAdapter.notifyDataSetChanged();
                } else {
                    // Hide section if there is no cast information for this movie.
                    castTitleTextView.setVisibility(View.GONE);
                    castRecyclerView.setVisibility(View.GONE);
                }

                // Film crew.
                setCrewByDepartment(data.getCrew(), "Directing", directingDepartmentAdapter, directingDepartmentTextView, directingDepartmentRecyclerView);
                setCrewByDepartment(data.getCrew(), "Production", productionDepartmentAdapter, productionDepartmentTextView, productionDepartmentRecyclerView);
                setCrewByDepartment(data.getCrew(), "Writing", writingDepartmentAdapter, writingDepartmentTextView, writingDepartmentRecyclerView);
                setCrewByDepartment(data.getCrew(), "Actors", actorsDepartmentAdapter, actorsDepartmentTextView, actorsDepartmentRecyclerView);
                setCrewByDepartment(data.getCrew(), "Camera", cameraDepartmentAdapter, cameraDepartmentTextView, cameraDepartmentRecyclerView);
                setCrewByDepartment(data.getCrew(), "Editing", editingDepartmentAdapter, editingDepartmentTextView, editingDepartmentRecyclerView);
                setCrewByDepartment(data.getCrew(), "Art", artDepartmentAdapter, artDepartmentTextView, artDepartmentRecyclerView);
                setCrewByDepartment(data.getCrew(), "Costume & Make-Up", costumeMakeupDepartmentAdapter, costumeMakeupDepartmentTextView, costumeMakeupDepartmentRecyclerView);
                setCrewByDepartment(data.getCrew(), "Sound", soundDepartmentAdapter, soundDepartmentTextView, soundDepartmentRecyclerView);
                setCrewByDepartment(data.getCrew(), "Visual Effects", visualEffectsDepartmentAdapter, visualEffectsDepartmentTextView, visualEffectsDepartmentRecyclerView);
                setCrewByDepartment(data.getCrew(), "Crew", crewDepartmentAdapter, crewDepartmentTextView, crewDepartmentRecyclerView);
                setCrewByDepartment(data.getCrew(), "Lighting", lightingDepartmentAdapter, lightingDepartmentTextView, lightingDepartmentRecyclerView);
                setCrewByDepartment(data.getCrew(), null, otherDepartmentAdapter, otherDepartmentTextView, otherDepartmentRecyclerView);
            } else {
                // Loader has not returned a valid list of {@link CastCrew} objects.
                Log.i(TAG, "(onLoadFinished) No search results.");
                noResultsTextView.setVisibility(View.VISIBLE);
                noResultsTextView.setText(getResources().getString(R.string.no_results));
            }
        } else

        {
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
    public void onLoaderReset(Loader<CastCrew> loader) {
    }

    /**
     * Helper method to manage Crew elements filtered by department.
     *
     * @param crewArrayList is the complete array of Crew elements.
     * @param department    is the department to filter the array. If this value is null or empty,
     *                      we filter by hypothetical not listed departments.
     * @param crewAdapter   is the adapter for displaying results.
     * @param textView      is the title of the current crew section.
     * @param recyclerView  the view for containing results.
     */
    void setCrewByDepartment(ArrayList<Crew> crewArrayList, String department, CrewAdapter crewAdapter, TextView textView, RecyclerView recyclerView) {
        // Get an array of Crew elements filtered by department.
        ArrayList<Crew> crewByDepartment = new ArrayList<>();
        for (Crew element : crewArrayList) {
            if (department != null && !department.isEmpty()) {
                // Filter by given department.
                if (element.getDepartment().equals(department))
                    crewByDepartment.add(element);
            } else {
                // Filter by hypothetical not listed departments.
                if (!element.getDepartment().equals("Directing") &&
                        !element.getDepartment().equals("Production") &&
                        !element.getDepartment().equals("Writing") &&
                        !element.getDepartment().equals("Actors") &&
                        !element.getDepartment().equals("Camera") &&
                        !element.getDepartment().equals("Editing") &&
                        !element.getDepartment().equals("Art") &&
                        !element.getDepartment().equals("Costume & Make-Up") &&
                        !element.getDepartment().equals("Sound") &&
                        !element.getDepartment().equals("Visual Effects") &&
                        !element.getDepartment().equals("Crew") &&
                        !element.getDepartment().equals("Lighting"))
                    crewByDepartment.add(element);
            }
        }

        // Set the corresponding crew section if there is data.
        if (crewByDepartment.size() > 0) {
            crewAdapter.setCrewArray(crewByDepartment);
            crewAdapter.notifyDataSetChanged();
        } else {
            // Hide crew section if there is no information for the current department.
            textView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        }
    }
}
