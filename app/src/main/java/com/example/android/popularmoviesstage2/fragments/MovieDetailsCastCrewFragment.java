package com.example.android.popularmoviesstage2.fragments;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.activities.PersonDetailsActivity;
import com.example.android.popularmoviesstage2.adapters.CastAdapter;
import com.example.android.popularmoviesstage2.adapters.CrewAdapter;
import com.example.android.popularmoviesstage2.asynctaskloaders.TmdbCastCrewAsyncTaskLoader;
import com.example.android.popularmoviesstage2.classes.TmdbCast;
import com.example.android.popularmoviesstage2.classes.TmdbCastCrew;
import com.example.android.popularmoviesstage2.classes.TmdbCrew;
import com.example.android.popularmoviesstage2.classes.TmdbMovie;
import com.example.android.popularmoviesstage2.classes.TmdbPerson;
import com.example.android.popularmoviesstage2.itemdecorations.SpaceItemDecoration;
import com.example.android.popularmoviesstage2.utils.NetworkUtils;
import com.example.android.popularmoviesstage2.utils.TextViewUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MovieDetailsCastCrewFragment extends Fragment implements LoaderManager.LoaderCallbacks<TmdbCastCrew> {
    private static final String TAG = MovieDetailsCastCrewFragment.class.getSimpleName();
    private static final int CAST_CREW_MAX_ELEMENTS = 10;

    @BindView(R.id.cast_crew_no_result_text_view)
    TextView noResultsTextView;
    @BindView(R.id.cast_crew_loading_indicator)
    ProgressBar progressBar;
    @BindView(R.id.cast_layout)
    LinearLayout castLinearLayout;
    @BindView(R.id.cast_title)
    TextView castTitleTextView;
    @BindView(R.id.cast_view_all)
    TextView viewAllCastTextView;
    @BindView(R.id.cast_view_all_action)
    TextView viewAllActionCastTextView;
    @BindView(R.id.cast_view_all_cardview)
    CardView viewAllActionCastCardView;
    @BindView(R.id.cast_recyclerview)
    RecyclerView castRecyclerView;
    @BindView(R.id.cast_title_layout)
    LinearLayout castLayout;
    @BindView(R.id.cast_crew_separator)
    View separatorView;
    @BindView(R.id.crew_layout)
    LinearLayout crewLinearLayout;
    @BindView(R.id.crew_title)
    TextView crewTextView;
    @BindView(R.id.crew_view_all)
    TextView viewAllCrewTextView;
    @BindView(R.id.crew_recycler_view)
    RecyclerView directingDepartmentRecyclerView;
    @BindView(R.id.crew_title_layout)
    LinearLayout crewRelativeLayout;
    @BindView(R.id.writing_textview)
    TextView writingDepartmentTextView;
    @BindView(R.id.production_textview)
    TextView productionDepartmentTextView;
    @BindView(R.id.writing_production_layout)
    LinearLayout writingProductionLayout;

    private int movieId;
    private CastAdapter castAdapter;
    private CrewAdapter directingDepartmentAdapter;
    private Unbinder unbinder;

    /**
     * Required empty public constructor.
     */
    public MovieDetailsCastCrewFragment() {
    }

    /**
     * Factory method to create a new instance of this fragment using the provided parameters.
     *
     * @param movie is the {@link TmdbMovie} object.
     * @return a new instance of fragment MovieDetailsCastCrewFragment.
     */
    public static MovieDetailsCastCrewFragment newInstance(TmdbMovie movie) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", movie.getId());
        MovieDetailsCastCrewFragment fragment = new MovieDetailsCastCrewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details_cast_crew, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        // Clean all elements in the layout when creating this fragment.
        clearLayout();

        // Get arguments from calling activity.
        if (getArguments() != null) {
            movieId = getArguments().getInt("id");
        }

        // Set RecyclerViews for displaying cast & crew photos.
        setRecyclerViews();

        // Create an AsyncTaskLoader for retrieving cast & crew information from internet in a
        // separate thread.
        getLoaderManager().initLoader(NetworkUtils.TMDB_CAST_CREW_LOADER_ID, null, this);

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
    public Loader<TmdbCastCrew> onCreateLoader(int id, Bundle args) {
        if (NetworkUtils.isConnected(getContext())) {
            // There is an available connection. Fetch results from TMDB.
            noResultsTextView.setText(getString(R.string.fetching_cast_crew_info));
            noResultsTextView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            Log.i(TAG, "(onCreateLoader) Movie ID: " + movieId);
            return new TmdbCastCrewAsyncTaskLoader(getContext(), movieId);
        } else {
            // There is no connection. Show error message.
            noResultsTextView.setText(getResources().getString(R.string.no_connection));
            noResultsTextView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
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
    public void onLoadFinished(Loader<TmdbCastCrew> loader, TmdbCastCrew data) {
        // Hide progress bar.
        progressBar.setVisibility(View.INVISIBLE);
        noResultsTextView.setVisibility(View.INVISIBLE);

        // Check if there is an available connection.
        if (NetworkUtils.isConnected(getContext())) {
            // If there is a valid list of {@link TmdbCastCrew} objects, then add them to the
            // adapters' data sets.
            if (data != null) {
                Log.i(TAG, "(onLoadFinished) Search results not null.");

                /* ------------------ */
                /* Cast of characters */
                /* ------------------ */
                ArrayList<TmdbCast> castArrayList = data.getCast();
                if (castArrayList != null && castArrayList.size() > 0) {
                    // Make this section visible.
                    castLinearLayout.setVisibility(View.VISIBLE);

                    // Show only the first CAST_CREW_MAX_ELEMENTS elements of this array list.
                    ArrayList<TmdbCast> castArrayListAux = new ArrayList<>();
                    int i = 0;
                    while ((i < CAST_CREW_MAX_ELEMENTS) && (i < castArrayList.size())) {
                        castArrayListAux.add(castArrayList.get(i));
                        i++;
                    }
                    castAdapter.setCastArray(castArrayListAux);
                    castAdapter.notifyDataSetChanged();

                    // Set "view all" sections.
                    View.OnClickListener onClickMoviesListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getContext(), "View all clicked", Toast.LENGTH_SHORT).show();
                        }
                    };
                    viewAllCastTextView.setOnClickListener(onClickMoviesListener);
                    int totalResults = castArrayList.size();
                    if (totalResults > CAST_CREW_MAX_ELEMENTS) {
                        String viewAllText = getString(R.string.view_all_cast, totalResults);
                        viewAllActionCastTextView.setText(viewAllText);
                        viewAllActionCastCardView.setVisibility(View.VISIBLE);
                        viewAllActionCastCardView.setOnClickListener(onClickMoviesListener);
                    } else {
                        viewAllActionCastCardView.setVisibility(View.GONE);
                    }
                } else {
                    // Hide section if there is no cast information for this movie.
                    castLinearLayout.setVisibility(View.GONE);
                    separatorView.setVisibility(View.GONE);
                }

                /* --------- */
                /* Film crew */
                /* --------- */
                ArrayList<TmdbCrew> crewArrayList = data.getCrew();
                if (crewArrayList != null && crewArrayList.size() > 0) {
                    // Make this section visible.
                    crewLinearLayout.setVisibility(View.VISIBLE);

                    /* -------------------- */
                    /* Directing department */
                    /* -------------------- */

                    // Get an array list with only the crew members of the directing department.
                    ArrayList<TmdbCrew> crewDirectingArrayList =
                            getFilteredCrewArrayList(crewArrayList, "Directing");

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

                    // Get an array list with only the crew members of the writing department.
                    ArrayList<TmdbCrew> crewWritingArrayList =
                            getFilteredCrewArrayList(crewArrayList, "Writing");

                    // Set the corresponding crew section if there is data.
                    boolean writingCrewIsSet = setCrewTextView(crewWritingArrayList,
                            writingDepartmentTextView, getString(R.string.department_writing));

                    /* --------------------- */
                    /* Production department */
                    /* --------------------- */

                    // Get an array list with only the crew members of the production department.
                    ArrayList<TmdbCrew> crewProductionArrayList =
                            getFilteredCrewArrayList(crewArrayList, "Production");

                    // Set the corresponding crew section if there is data.
                    boolean productionCrewIsSet = setCrewTextView(crewProductionArrayList,
                            productionDepartmentTextView, getString(R.string.department_production));

                    // Hide writing and production CardView if there is no information to display.
                    if (!writingCrewIsSet && !productionCrewIsSet)
                        writingProductionLayout.setVisibility(View.GONE);
                } else {
                    // Hide section if there is no crew information for this movie.
                    separatorView.setVisibility(View.GONE);
                    crewLinearLayout.setVisibility(View.GONE);
                }
            } else {
                // Loader has not returned a valid list of {@link TmdbCastCrew} objects.
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
    public void onLoaderReset(Loader<TmdbCastCrew> loader) {
    }

    /* -------------- */
    /* HELPER METHODS */
    /* -------------- */

    /**
     * Hide every info element in the layout.
     */
    void clearLayout() {
        castLinearLayout.setVisibility(View.GONE);
        separatorView.setVisibility(View.GONE);
        crewLinearLayout.setVisibility(View.GONE);
    }

    /**
     * Helper method for setting the RecyclerViews in order to display lists of film crew and cast
     * with a horizontal arrangement.
     */
    void setRecyclerViews() {
        // Set the LayoutManager for the RecyclerViews.
        int separation = getResources().getDimensionPixelOffset(R.dimen.small_padding);

        castRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        castRecyclerView.setHasFixedSize(true);
        castRecyclerView.addItemDecoration(new SpaceItemDecoration(separation,
                SpaceItemDecoration.HORIZONTAL_SEPARATION));

        directingDepartmentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        directingDepartmentRecyclerView.setHasFixedSize(true);
        directingDepartmentRecyclerView.addItemDecoration(new SpaceItemDecoration(separation,
                SpaceItemDecoration.VERTICAL_SEPARATION));

        // Set the listeners for click events in the CastAdapters.
        CastAdapter.OnItemClickListener castListener = new CastAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TmdbCast cast) {
                // Open a new PersonDetailsActivity to show info about the current cast member.
                Intent intent = new Intent(getContext(), PersonDetailsActivity.class);
                TmdbPerson person = new TmdbPerson(cast.getId(), cast.getName(), cast.getProfile_path());
                intent.putExtra(PersonDetailsActivity.EXTRA_PARAM_PERSON, person);
                startActivity(intent);
            }
        };

        CrewAdapter.OnItemClickListener crewListener = new CrewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TmdbCrew crew) {
                // Open a new PersonDetailsActivity to show info about the current crew member.
                Intent intent = new Intent(getContext(), PersonDetailsActivity.class);
                TmdbPerson person = new TmdbPerson(crew.getId(), crew.getName(), crew.getProfile_path());
                intent.putExtra(PersonDetailsActivity.EXTRA_PARAM_PERSON, person);
                startActivity(intent);
            }
        };

        // Set the Adapters for the RecyclerViews.
        castAdapter = new CastAdapter(new ArrayList<TmdbCast>(), castListener);
        castRecyclerView.setAdapter(castAdapter);

        directingDepartmentAdapter = new CrewAdapter(new ArrayList<TmdbCrew>(), crewListener);
        directingDepartmentRecyclerView.setAdapter(directingDepartmentAdapter);
    }

    /**
     * Helper method to manage crew elements filtered by department.
     *
     * @param crewArrayList is the original array of crew elements.
     * @param department    is the department to filter the array.
     * @return an ArrayList containing at most CAST_CREW_MAX_ELEMENTS crew elements filtered by the
     * given department.
     */
    ArrayList<TmdbCrew> getFilteredCrewArrayList(ArrayList<TmdbCrew> crewArrayList, String department) {
        ArrayList<TmdbCrew> crewArrayListByDepartment = new ArrayList<>();

        // The output array will contain at most CAST_CREW_MAX_ELEMENTS crew elements.
        int i = 0;
        while ((i < CAST_CREW_MAX_ELEMENTS) && (i < crewArrayList.size())) {
            // Filter by given department.
            if (crewArrayList.get(i).getDepartment().equals(department)) {
                // Add the current element to the temporary array of crew elements filtered by
                // department and remove it from the copy of the original array passed to this
                // method, which will be returned on exit.
                crewArrayListByDepartment.add(crewArrayList.get(i));
            }
            i++;
        }

        // Return a new array of crew elements containing only members of the given department.
        return crewArrayListByDepartment;
    }

    /**
     * Helper method to write crew members on a comma-separated TextView.
     *
     * @param crewArrayList is the ArrayList containing the crew elements to be shown.
     * @param textView      is the TextView where the crew elements are going to be written.
     * @param title         is a String containing the title of the current crew section.
     * @return true if the size of crewArrayList is greater than zero, false otherwise.
     */
    private boolean setCrewTextView(ArrayList<TmdbCrew> crewArrayList, TextView textView, String title) {
        if (crewArrayList.size() > 0) {
            String text = "";
            String color = String.format("%X", getResources().getColor(R.color.colorDarkGrey)).substring(2);
            for (int i = 0; i < crewArrayList.size(); i++) {
                // Add members of the current department to the corresponding TextView.
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(text);
                stringBuilder.append("<font color=\"#");
                stringBuilder.append(color);
                stringBuilder.append("\">");
                stringBuilder.append(crewArrayList.get(i).getName());
                stringBuilder.append(" (");
                stringBuilder.append(crewArrayList.get(i).getJob());
                stringBuilder.append(")</font>");
                if (i < (crewArrayList.size() - 1)) {
                    // Don't append newline character to the last element.
                    stringBuilder.append("<br>");
                }
                text = stringBuilder.toString();
            }

            // Add title and set text.
            //text = "<font color=\"#" + color+"\"><strong>"+title.toUpperCase() + "</strong></font><br>" + text;
            text = "<strong>" + title.toUpperCase() + "</strong><br>" + text;
            TextViewUtils.setHtmlText(textView, text);
            return true;
        } else {
            // Hide crew section if there is no information for the current department.
            textView.setVisibility(View.GONE);
            return false;
        }
    }
}
