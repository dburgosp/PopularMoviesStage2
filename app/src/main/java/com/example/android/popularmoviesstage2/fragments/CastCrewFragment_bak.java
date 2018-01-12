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
import com.example.android.popularmoviesstage2.utils.TextUtils;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CastCrewFragment_bak extends Fragment implements LoaderManager.LoaderCallbacks<CastCrew> {
    private static final String TAG = CastCrewFragment_bak.class.getSimpleName();
    private static final int CAST_CREW_LOADER_ID = 2;
    private static final int CAST_CREW_MAX_ELEMENTS = 8;

    @BindView(R.id.cast_crew_no_result_text_view)
    TextView noResultsTextView;
    @BindView(R.id.cast_crew_loading_indicator)
    ProgressBar progressBar;
    @BindView(R.id.cast_title)
    TextView castTitleTextView;
    @BindView(R.id.cast_view_all)
    TextView viewAllCastTextView;
    @BindView(R.id.cast_recycler_view)
    RecyclerView castRecyclerView;
    @BindView(R.id.crew_title)
    TextView directingDepartmentTextView;
    @BindView(R.id.crew_view_all)
    TextView viewAllDirectingDepartmentTextView;
    @BindView(R.id.crew_recycler_view)
    RecyclerView directingDepartmentRecyclerView;
    @BindView(R.id.writing_title)
    TextView productionDepartmentTextView;
    @BindView(R.id.cast_crew_view_all_3)
    TextView viewAllProductionDepartmentTextView;
    @BindView(R.id.cast_crew_recycler_view_3)
    RecyclerView productionDepartmentRecyclerView;
    @BindView(R.id.production_title)
    TextView writingDepartmentTextView;
    @BindView(R.id.cast_crew_view_all_4)
    TextView viewAllWritingDepartmentTextView;
    @BindView(R.id.cast_crew_recycler_view_4)
    RecyclerView writingDepartmentRecyclerView;
    @BindView(R.id.cast_crew_title_5)
    TextView actorsDepartmentTextView;
    @BindView(R.id.cast_crew_view_all_5)
    TextView viewAllActorsDepartmentTextView;
    @BindView(R.id.cast_crew_recycler_view_5)
    RecyclerView actorsDepartmentRecyclerView;
    @BindView(R.id.cast_crew_title_6)
    TextView cameraDepartmentTextView;
    @BindView(R.id.cast_crew_view_all_6)
    TextView viewAllCameraDepartmentTextView;
    @BindView(R.id.cast_crew_recycler_view_6)
    RecyclerView cameraDepartmentRecyclerView;
    @BindView(R.id.cast_crew_title_7)
    TextView editingDepartmentTextView;
    @BindView(R.id.cast_crew_view_all_7)
    TextView viewAllEditingDepartmentTextView;
    @BindView(R.id.cast_crew_recycler_view_7)
    RecyclerView editingDepartmentRecyclerView;
    @BindView(R.id.cast_crew_title_8)
    TextView artDepartmentTextView;
    @BindView(R.id.cast_crew_view_all_8)
    TextView viewAllArtDepartmentTextView;
    @BindView(R.id.cast_crew_recycler_view_8)
    RecyclerView artDepartmentRecyclerView;
    @BindView(R.id.cast_crew_title_9)
    TextView costumeMakeupDepartmentTextView;
    @BindView(R.id.cast_crew_view_all_9)
    TextView viewAllCostumeMakeupDepartmentTextView;
    @BindView(R.id.cast_crew_recycler_view_9)
    RecyclerView costumeMakeupDepartmentRecyclerView;
    @BindView(R.id.cast_crew_title_10)
    TextView soundDepartmentTextView;
    @BindView(R.id.cast_crew_view_all_10)
    TextView viewAllSoundDepartmentTextView;
    @BindView(R.id.cast_crew_recycler_view_10)
    RecyclerView soundDepartmentRecyclerView;
    @BindView(R.id.cast_crew_title_11)
    TextView visualEffectsDepartmentTextView;
    @BindView(R.id.cast_crew_view_all_11)
    TextView viewAllVisualEffectsDepartmentTextView;
    @BindView(R.id.cast_crew_recycler_view_11)
    RecyclerView visualEffectsDepartmentRecyclerView;
    @BindView(R.id.cast_crew_title_12)
    TextView crewDepartmentTextView;
    @BindView(R.id.cast_crew_view_all_12)
    TextView viewAllCrewDepartmentTextView;
    @BindView(R.id.cast_crew_recycler_view_12)
    RecyclerView crewDepartmentRecyclerView;
    @BindView(R.id.cast_crew_title_13)
    TextView lightingDepartmentTextView;
    @BindView(R.id.cast_crew_view_all_13)
    TextView viewAllLightingDepartmentTextView;
    @BindView(R.id.cast_crew_recycler_view_13)
    RecyclerView lightingDepartmentRecyclerView;
    @BindView(R.id.cast_crew_title_14)
    TextView otherDepartmentTextView;
    @BindView(R.id.cast_crew_view_all_14)
    TextView viewAllOtherDepartmentTextView;
    @BindView(R.id.cast_crew_recycler_view_14)
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
    public CastCrewFragment_bak() {
    }

    /**
     * Factory method to create a new instance of this fragment using the provided parameters.
     *
     * @param movie is the {@link Movie} object.
     * @return a new instance of fragment CastCrewFragment.
     */
    public static CastCrewFragment_bak newInstance(Movie movie) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", movie.getId());
        CastCrewFragment_bak fragment = new CastCrewFragment_bak();
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
                    // Show only the first CAST_CREW_MAX_ELEMENTS elements of this array list.
                    ArrayList<Cast> castArrayListAux = new ArrayList<>();
                    int i = 0;
                    while ((i < CAST_CREW_MAX_ELEMENTS) && (i < castArrayList.size())) {
                        castArrayListAux.add(castArrayList.get(i));
                        i++;
                    }
                    castAdapter.setCastArray(castArrayListAux);
                    castAdapter.notifyDataSetChanged();

                    // Set "view all" button.
                    int labelColor = getResources().getColor(R.color.colorBlack);
                    String сolorString = String.format("%X", labelColor).substring(2);
                    TextUtils.setHtmlText(viewAllCastTextView, "" +
                            "<font color=\"#" + сolorString + "\"><strong>" +
                            getResources().getString(R.string.view_all) + "</strong></font> " +
                            "(" + castArrayList.size() + ")");
                } else {
                    // Hide section if there is no cast information for this movie.
                    castTitleTextView.setVisibility(View.GONE);
                    castRecyclerView.setVisibility(View.GONE);
                    viewAllCastTextView.setVisibility(View.GONE);
                }

                // Film crew.
                ArrayList<Crew> crewArrayList = data.getCrew();
                crewArrayList = setCrewByDepartment(crewArrayList, "Directing", directingDepartmentAdapter, directingDepartmentTextView, directingDepartmentRecyclerView, viewAllDirectingDepartmentTextView);
                crewArrayList = setCrewByDepartment(crewArrayList, "Production", productionDepartmentAdapter, productionDepartmentTextView, productionDepartmentRecyclerView, viewAllProductionDepartmentTextView);
                crewArrayList = setCrewByDepartment(crewArrayList, "Writing", writingDepartmentAdapter, writingDepartmentTextView, writingDepartmentRecyclerView, viewAllWritingDepartmentTextView);
                crewArrayList = setCrewByDepartment(crewArrayList, "Actors", actorsDepartmentAdapter, actorsDepartmentTextView, actorsDepartmentRecyclerView, viewAllActorsDepartmentTextView);
                crewArrayList = setCrewByDepartment(crewArrayList, "Camera", cameraDepartmentAdapter, cameraDepartmentTextView, cameraDepartmentRecyclerView, viewAllCameraDepartmentTextView);
                crewArrayList = setCrewByDepartment(crewArrayList, "Editing", editingDepartmentAdapter, editingDepartmentTextView, editingDepartmentRecyclerView, viewAllEditingDepartmentTextView);
                crewArrayList = setCrewByDepartment(crewArrayList, "Art", artDepartmentAdapter, artDepartmentTextView, artDepartmentRecyclerView, viewAllArtDepartmentTextView);
                crewArrayList = setCrewByDepartment(crewArrayList, "Costume & Make-Up", costumeMakeupDepartmentAdapter, costumeMakeupDepartmentTextView, costumeMakeupDepartmentRecyclerView, viewAllCostumeMakeupDepartmentTextView);
                crewArrayList = setCrewByDepartment(crewArrayList, "Sound", soundDepartmentAdapter, soundDepartmentTextView, soundDepartmentRecyclerView, viewAllSoundDepartmentTextView);
                crewArrayList = setCrewByDepartment(crewArrayList, "Visual Effects", visualEffectsDepartmentAdapter, visualEffectsDepartmentTextView, visualEffectsDepartmentRecyclerView, viewAllVisualEffectsDepartmentTextView);
                crewArrayList = setCrewByDepartment(crewArrayList, "Crew", crewDepartmentAdapter, crewDepartmentTextView, crewDepartmentRecyclerView, viewAllCrewDepartmentTextView);
                crewArrayList = setCrewByDepartment(crewArrayList, "Lighting", lightingDepartmentAdapter, lightingDepartmentTextView, lightingDepartmentRecyclerView, viewAllLightingDepartmentTextView);
                setCrewByDepartment(crewArrayList, null, otherDepartmentAdapter, otherDepartmentTextView, otherDepartmentRecyclerView, viewAllOtherDepartmentTextView);
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
     * @param crewArrayListIn is the original array of Crew elements.
     * @param department      is the department to filter the array. If this value is null or empty,
     *                        we filter by hypothetical not listed departments.
     * @param crewAdapter     is the adapter for displaying results.
     * @param titleTextView   is the title of the current crew section.
     * @param recyclerView    the view for containing results.
     * @param viewAllTextView is the "view all" button for displaying the vertical list of elements.
     * @return the modified crewArrayListIn, without the elements deleted within this method.
     */
    ArrayList<Crew> setCrewByDepartment(ArrayList<Crew> crewArrayListIn, String department,
                                        CrewAdapter crewAdapter, TextView titleTextView,
                                        RecyclerView recyclerView, TextView viewAllTextView) {
        // Make a copy of the original array of Crew elements, in order to remove the elements
        // found into the next loop.
        ArrayList<Crew> crewArrayListOut = (ArrayList<Crew>) crewArrayListIn.clone();

        // Get an array of Crew elements filtered by department.
        ArrayList<Crew> crewArrayListByDepartment = new ArrayList<>();
        boolean found = false;
        for (Crew element : crewArrayListIn) {
            if (department != null && !department.isEmpty()) {
                // Filter by given department.
                if (element.getDepartment().equals(department))
                    found = true;
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
                    found = true;
            }

            if (found) {
                // Add the current element to the temporary array of Crew elements filtered by
                // department and remove it from the copy of the original array passed to this
                // method, which will be returned on exit.
                crewArrayListByDepartment.add(element);
                crewArrayListOut.remove(element);
                found = false;
            }
        }

        // Set the corresponding crew section if there is data.
        if (crewArrayListByDepartment.size() > 0) {
            // Show only the first CAST_CREW_MAX_ELEMENTS elements of this array list.
            ArrayList<Crew> crewArrayListAux = new ArrayList<>();
            int i = 0;
            while ((i < CAST_CREW_MAX_ELEMENTS) && (i < crewArrayListByDepartment.size())) {
                crewArrayListAux.add(crewArrayListByDepartment.get(i));
                i++;
            }
            crewAdapter.setCrewArray(crewArrayListAux);
            crewAdapter.notifyDataSetChanged();

            // Set the "view all" button.
            int labelColor = getResources().getColor(R.color.colorBlack);
            String сolorString = String.format("%X", labelColor).substring(2);
            TextUtils.setHtmlText(viewAllTextView, "" +
                    "<font color=\"#" + сolorString + "\"><strong>" +
                    getResources().getString(R.string.view_all) + "</strong></font> " +
                    "(" + crewArrayListByDepartment.size() + ")");
        } else {
            // Hide crew section if there is no information for the current department.
            titleTextView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            viewAllTextView.setVisibility(View.GONE);
        }

        // Return the copy of the original ArrayList<Crew> without the elements removed previously
        // into the loop.
        return crewArrayListOut;
    }
}
