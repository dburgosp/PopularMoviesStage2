package com.example.android.popularmoviesstage2.fragments;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.asynctaskloaders.MoviesAsyncTaskLoader;
import com.example.android.popularmoviesstage2.classes.Movie;
import com.example.android.popularmoviesstage2.classes.MovieGenre;
import com.example.android.popularmoviesstage2.utils.DateTimeUtils;
import com.example.android.popularmoviesstage2.utils.NetworkUtils;
import com.example.android.popularmoviesstage2.utils.TextUtils;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * {@link Fragment} that displays the main information about the current movie.
 */
public class InfoFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Movie>> {
    private static final String TAG = InfoFragment.class.getSimpleName();
    private static Movie movie;

    @BindView(R.id.info_overview_textview)
    TextView infoOverviewTextView;
    @BindView(R.id.info_tagline_textview)
    TextView infoTaglineTextView;
    @BindView(R.id.info_runtime_textview)
    TextView infoRuntimeTextView;
    @BindView(R.id.info_genres_textview)
    TextView infoGenresTextView;
    @BindView(R.id.info_release_date_textview)
    TextView infoReleaseDateTextView;
    @BindView(R.id.info_status_textview)
    TextView infoStatusTextView;
    @BindView(R.id.info_original_title_textview)
    TextView infoOriginalTitleTextView;
    @BindView(R.id.info_original_language_textview)
    TextView infoOriginalLanguageTextView;
    @BindView(R.id.info_budget_textview)
    TextView infoBudgetTextView;
    @BindView(R.id.info_revenue_textview)
    TextView infoRevenueTextView;
    @BindView(R.id.info_no_result_text_view)
    TextView noResultsTextView;
    @BindView(R.id.info_loading_indicator)
    ProgressBar progressBar;
    @BindView(R.id.info_main_layout)
    RelativeLayout mainLayout;

    private int movieId;
    private Unbinder unbinder;

    /**
     * Required empty public constructor.
     */
    public InfoFragment() {
    }

    /**
     * Factory method to create a new instance of this fragment using the provided parameters.
     *
     * @param movie is the {@link Movie} object.
     * @return a new instance of fragment InfoFragment.
     */
    public static InfoFragment newInstance(Movie movie) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", movie.getId());
        InfoFragment fragment = new InfoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_info, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        // Get arguments from calling activity.
        if (getArguments() != null) {
            movieId = getArguments().getInt("id");
        }

        // Create an AsyncTaskLoader for retrieving complete movie information from internet in a
        // separate thread.
        getLoaderManager().initLoader(NetworkUtils.MOVIE_DETAILS_LOADER_ID, null, this);

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
    public Loader<ArrayList<Movie>> onCreateLoader(int id, Bundle args) {
        if (NetworkUtils.isConnected(getContext())) {
            // There is an available connection. Fetch results from TMDB.
            progressBar.setVisibility(View.VISIBLE);
            noResultsTextView.setVisibility(View.INVISIBLE);
            URL searchURL = NetworkUtils.buildFetchMovieDetailsURL(movieId);
            Log.i(TAG, "(onCreateLoader) Search URL: " + searchURL.toString());
            return new MoviesAsyncTaskLoader(getContext(), searchURL, NetworkUtils.OPERATION_GET_MOVIE_DETAILS);
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
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
        // Hide progress bar.
        progressBar.setVisibility(View.INVISIBLE);

        // Check if there is an available connection.
        if (NetworkUtils.isConnected(getContext())) {
            // If there is a valid result, then update its data into the current {@link Movie}
            // object.
            if (data != null && !data.isEmpty()) {
                Log.i(TAG, "(onLoadFinished) Search results not null.");
                movie = data.get(0);
                setMovieInfo();
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
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {
    }

    /* -------------- */
    /* HELPER METHODS */
    /* -------------- */

    /**
     * Helper method to display the movie information in this fragment.
     */
    void setMovieInfo() {
        // Set tagline. If there is no overview, we hide this section.
        String tagline = movie.getTagline();
        if (tagline != null && !tagline.equals("") && !tagline.isEmpty()) {
            infoTaglineTextView.setText(tagline);
            infoTaglineTextView.setVisibility(View.VISIBLE);
        } else
            infoTaglineTextView.setVisibility(View.GONE);

        // Set overview. If there is no overview, we show the default text for overview.
        String overview = movie.getOverview();
        if (overview != null && !overview.equals("") && !overview.isEmpty())
            infoOverviewTextView.setText(overview);
        else
            infoOverviewTextView.setText(R.string.no_overview);

        // Set runtime. If there is no available runtime info, we hide this section.
        String runtime = DateTimeUtils.getHoursAndMinutes(getContext(), movie.getRuntime());
        if (runtime != null && !runtime.equals("") && !runtime.isEmpty()) {
            String html = "<strong>Runtime: </strong>" + runtime;
            TextUtils.setHtmlText(infoRuntimeTextView, html);
            infoRuntimeTextView.setVisibility(View.VISIBLE);
        } else {
            infoRuntimeTextView.setVisibility(View.GONE);
        }

        // Set genres. If there is no available genres info, we hide this section.
        ArrayList<MovieGenre> movieGenres = movie.getGenres();
        if (movieGenres != null && movieGenres.size() > 0) {
            String html;
            if (movieGenres.size() == 1) {
                html = "<strong>Genre: </strong>" + movieGenres.get(0).getName();
            } else {
                html = "<strong>Genres: </strong>";
                StringBuilder stringBuilder = new StringBuilder(html);
                for (int n = 0; n < movieGenres.size(); n++) {
                    stringBuilder.append(movieGenres.get(n).getName());
                    if ((n + 1) < movieGenres.size())
                        stringBuilder.append(", ");
                }
                html = "" + stringBuilder;
            }
            TextUtils.setHtmlText(infoGenresTextView, html);
            infoGenresTextView.setVisibility(View.VISIBLE);
        } else
            infoGenresTextView.setVisibility(View.GONE);

        // Set release date. If there is no available release date, we hide this section.
        String releaseDate = movie.getRelease_date();
        if (releaseDate != null && !releaseDate.equals("") && !releaseDate.isEmpty()) {
            String html = "<strong>Release date: </strong>" + releaseDate;
            TextUtils.setHtmlText(infoReleaseDateTextView, html);
            infoReleaseDateTextView.setVisibility(View.VISIBLE);
        } else
            infoReleaseDateTextView.setVisibility(View.GONE);

        // Set status. If there is no available status info, we hide this section.
        String status = movie.getStatus();
        if (status != null && !status.equals("") && !status.isEmpty()) {
            String html = "<strong>Status: </strong>" + status;
            TextUtils.setHtmlText(infoStatusTextView, html);
            infoStatusTextView.setVisibility(View.VISIBLE);
        } else
            infoStatusTextView.setVisibility(View.GONE);

        // Set original title. If there is no available original title info, we hide this section.
        String originalTitle = movie.getOriginal_title();
        if (originalTitle != null && !originalTitle.equals("") && !originalTitle.isEmpty()) {
            String html = "<strong>Original title: </strong>" + originalTitle;
            TextUtils.setHtmlText(infoOriginalTitleTextView, html);
            infoOriginalTitleTextView.setVisibility(View.VISIBLE);
        } else
            infoOriginalTitleTextView.setVisibility(View.GONE);

        // Set original language. If there is no available original language info, we hide this section.
        String originalLanguage = movie.getOriginal_language();
        if (originalLanguage != null && !originalLanguage.equals("") && !originalLanguage.isEmpty()) {
            String html = "<strong>Original language: </strong>" + originalLanguage;
            TextUtils.setHtmlText(infoOriginalLanguageTextView, html);
            infoOriginalLanguageTextView.setVisibility(View.VISIBLE);
        } else
            infoOriginalLanguageTextView.setVisibility(View.GONE);

        // Set movie budget. If there is no available budget info, we hide this section.
        int budget = movie.getBudget();
        if (budget > 0) {
            DecimalFormat decimalFormat = new DecimalFormat("###,###");
            String html = "<strong>Budget: </strong>" + decimalFormat.format(budget) + " $";
            TextUtils.setHtmlText(infoBudgetTextView, html);
            infoBudgetTextView.setVisibility(View.VISIBLE);
        } else
            infoBudgetTextView.setVisibility(View.GONE);

        // Set movie revenue. If there is no available revenue info, we hide this section.
        int revenue = movie.getRevenue();
        if (revenue > 0) {
            DecimalFormat decimalFormat = new DecimalFormat("###,###");
            String html = "<strong>Revenue: </strong>" + decimalFormat.format(revenue) + " $";
            TextUtils.setHtmlText(infoRevenueTextView, html);
            infoRevenueTextView.setVisibility(View.VISIBLE);
        } else
            infoRevenueTextView.setVisibility(View.GONE);
    }
}