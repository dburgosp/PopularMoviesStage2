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
import com.example.android.popularmoviesstage2.adapters.CastAdapter;
import com.example.android.popularmoviesstage2.adapters.CrewAdapter;
import com.example.android.popularmoviesstage2.asynctaskloaders.CastCrewAsyncTaskLoader;
import com.example.android.popularmoviesstage2.classes.Cast;
import com.example.android.popularmoviesstage2.classes.CastCrew;
import com.example.android.popularmoviesstage2.classes.Crew;
import com.example.android.popularmoviesstage2.classes.Movie;
import com.example.android.popularmoviesstage2.utils.NetworkUtils;
import com.example.android.popularmoviesstage2.utils.TextUtils;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CastCrewFragment extends Fragment implements LoaderManager.LoaderCallbacks<CastCrew> {
    private static final String TAG = CastCrewFragment.class.getSimpleName();
    private static final int CAST_CREW_LOADER_ID = 2;
    private static final int CAST_CREW_MAX_ELEMENTS = 10;

    @BindView(R.id.cast_crew_no_result_text_view)
    TextView noResultsTextView;
    @BindView(R.id.cast_crew_loading_indicator)
    ProgressBar progressBar;
    @BindView(R.id.cast_linear_layout)
    LinearLayout castLinearLayout;
    @BindView(R.id.cast_title)
    TextView castTitleTextView;
    @BindView(R.id.cast_view_all)
    TextView viewAllCastTextView;
    @BindView(R.id.cast_recycler_view)
    RecyclerView castRecyclerView;
    @BindView(R.id.cast_relative_layout)
    RelativeLayout castRelativeLayout;
    @BindView(R.id.cast_crew_separator)
    View separatorView;
    @BindView(R.id.crew_linear_layout)
    LinearLayout crewLinearLayout;
    @BindView(R.id.crew_title)
    TextView crewTextView;
    @BindView(R.id.crew_view_all)
    TextView viewAllCrewTextView;
    @BindView(R.id.crew_recycler_view)
    RecyclerView directingDepartmentRecyclerView;
    @BindView(R.id.crew_relative_layout)
    RelativeLayout crewRelativeLayout;
    @BindView(R.id.writing_title)
    TextView writingDepartmentTitleTextView;
    @BindView(R.id.writing_content)
    TextView writingDepartmentTextView;
    @BindView(R.id.production_title)
    TextView productionDepartmentTitleTextView;
    @BindView(R.id.production_content)
    TextView productionDepartmentTextView;
    @BindView(R.id.writing_linear_layout)
    LinearLayout writingLinearLayout;
    @BindView(R.id.production_linear_layout)
    LinearLayout productionLinearLayout;

    private int movieId;
    private CastAdapter castAdapter;
    private CrewAdapter directingDepartmentAdapter;

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
        // Set the LayoutManager for the RecyclerViews.
        castRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        directingDepartmentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        castRecyclerView.setHasFixedSize(true);
        directingDepartmentRecyclerView.setHasFixedSize(true);

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
        castRecyclerView.setAdapter(castAdapter);
        directingDepartmentRecyclerView.setAdapter(directingDepartmentAdapter);
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

                /* ------------------ */
                /* Cast of characters */
                /* ------------------ */
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
                    String viewAllText = "<strong>" + getResources().getString(R.string.view_all) +
                            "</strong> (" + castArrayList.size() + ")";
                    TextUtils.setHtmlText(viewAllCastTextView, viewAllText);
                } else {
                    // Hide section if there is no cast information for this movie.
                    castLinearLayout.setVisibility(View.GONE);
                    separatorView.setVisibility(View.GONE);
                }

                /* --------- */
                /* Film crew */
                /* --------- */
                ArrayList<Crew> crewArrayList = data.getCrew();
                if (crewArrayList != null && crewArrayList.size() > 0) {
                    // Set "view all" button.
                    String viewAllText = "<strong>" + getResources().getString(R.string.view_all) +
                            "</strong> (" + crewArrayList.size() + ")";
                    TextUtils.setHtmlText(viewAllCrewTextView, viewAllText);

                    /* -------------------- */
                    /* Directing department */
                    /* -------------------- */

                    // Get an array list with only the Crew members of the directing department.
                    ArrayList<Crew> crewDirectingArrayList = getFilteredCrewArrayList(crewArrayList, "Directing");

                    // Set the corresponding crew section if there is data.
                    if (crewDirectingArrayList.size() > 0) {
                        directingDepartmentAdapter.setCrewArray(crewDirectingArrayList);
                        directingDepartmentAdapter.notifyDataSetChanged();
                    } else {
                        // Hide recycler view if there is no information for the current department.
                        directingDepartmentRecyclerView.setVisibility(View.GONE);
                    }

                    /* ------------------ */
                    /* Writing department */
                    /* ------------------ */

                    // Get an array list with only the Crew members of the writing department.
                    ArrayList<Crew> crewWritingArrayList = getFilteredCrewArrayList(crewArrayList, "Writing");

                    // Set the corresponding crew section if there is data.
                    setCrewTextView(crewWritingArrayList, writingDepartmentTextView, writingLinearLayout);

                    /* --------------------- */
                    /* Production department */
                    /* --------------------- */

                    // Get an array list with only the Crew members of the production department.
                    ArrayList<Crew> crewProductionArrayList = getFilteredCrewArrayList(crewArrayList, "Production");

                    // Set the corresponding crew section if there is data.
                    setCrewTextView(crewProductionArrayList, productionDepartmentTextView, productionLinearLayout);
                } else {
                    // Hide section if there is no crew information for this movie.
                    separatorView.setVisibility(View.GONE);
                    crewLinearLayout.setVisibility(View.GONE);
                }
            } else {
                // Loader has not returned a valid list of {@link CastCrew} objects.
                Log.i(TAG, "(onLoadFinished) No search results.");
                noResultsTextView.setVisibility(View.VISIBLE);
                noResultsTextView.setText(getResources().getString(R.string.no_results));
            }
        } else {
            // There is no connection. Show error message.
            Log.i(TAG, "(onLoadFinished) No connection to internet.");
            noResultsTextView.setVisibility(View.VISIBLE);
            noResultsTextView.setText(getResources().getString(R.string.no_connection));

            // Hide cast and crew sections.
            castLinearLayout.setVisibility(View.GONE);
            separatorView.setVisibility(View.GONE);
            crewLinearLayout.setVisibility(View.GONE);
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
     * @param crewArrayList is the original array of Crew elements.
     * @param department    is the department to filter the array.
     * @return an ArrayList containing at most CAST_CREW_MAX_ELEMENTS Crew elements filtered by the
     * given department.
     */
    ArrayList<Crew> getFilteredCrewArrayList(ArrayList<Crew> crewArrayList, String department) {
        ArrayList<Crew> crewArrayListByDepartment = new ArrayList<>();

        // The output array will contain at most CAST_CREW_MAX_ELEMENTS Crew elements.
        int i = 0;
        while ((i < CAST_CREW_MAX_ELEMENTS) && (i < crewArrayList.size())) {
            // Filter by given department.
            if (crewArrayList.get(i).getDepartment().equals(department)) {
                // Add the current element to the temporary array of Crew elements filtered by
                // department and remove it from the copy of the original array passed to this
                // method, which will be returned on exit.
                crewArrayListByDepartment.add(crewArrayList.get(i));
            }
            i++;
        }

        // Return a new array of Crew elements containing only members of the given department.
        return crewArrayListByDepartment;
    }

    /**
     * Helper method to write crew members on a comma-separated TextView.
     *
     * @param crewArrayList is the ArrayList containing the Crew elements to be shown.
     * @param textView      is the TextView where the Crew elements are going to be written.
     * @param linearLayout  is the LinearLayout that contains the current Crew section.
     */
    private void setCrewTextView(ArrayList<Crew> crewArrayList, TextView textView, LinearLayout linearLayout) {
        if (crewArrayList.size() > 0) {
            String text = "";
            int labelColor = getResources().getColor(R.color.colorBlack);
            String сolorString = String.format("%X", labelColor).substring(2);
            for (int i = 0; i < crewArrayList.size(); i++) {
                // Add members of the current department to the corresponding TextView.
                if (i > 0)
                    text = text + ", ";
                text = text + "<font color=\"#" + сolorString + "\"><strong>" +
                        crewArrayList.get(i).getName() + "</strong></font> (" +
                        crewArrayList.get(i).getJob() + ")";
            }
            TextUtils.setHtmlText(textView, text);
        } else {
            // Hide crew section if there is no information for the current department.
            linearLayout.setVisibility(View.GONE);
        }
    }
}
