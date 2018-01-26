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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.asynctaskloaders.OmdbMovieAsyncTaskLoader;
import com.example.android.popularmoviesstage2.asynctaskloaders.TmdbMovieDetailsAsyncTaskLoader;
import com.example.android.popularmoviesstage2.classes.FlowLayout;
import com.example.android.popularmoviesstage2.classes.OmdbMovie;
import com.example.android.popularmoviesstage2.classes.Tmdb;
import com.example.android.popularmoviesstage2.classes.TmdbMovie;
import com.example.android.popularmoviesstage2.classes.TmdbMovieCompany;
import com.example.android.popularmoviesstage2.classes.TmdbMovieCountry;
import com.example.android.popularmoviesstage2.classes.TmdbMovieGenre;
import com.example.android.popularmoviesstage2.classes.TmdbMovieLanguage;
import com.example.android.popularmoviesstage2.utils.DateTimeUtils;
import com.example.android.popularmoviesstage2.utils.NetworkUtils;
import com.example.android.popularmoviesstage2.utils.ScoreUtils;
import com.example.android.popularmoviesstage2.utils.TextUtils;
import com.github.lzyzsd.circleprogress.DonutProgress;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * {@link Fragment} that displays the main information about the current movie.
 */
public class InfoFragment extends Fragment implements LoaderManager.LoaderCallbacks<TmdbMovie> {
    private static final String TAG = InfoFragment.class.getSimpleName();

    // Annotate fields with @BindView and views ID for Butter Knife to find and automatically cast
    // the corresponding views.
    @BindView(R.id.info_loading_indicator)
    ProgressBar progressBar;
    @BindView(R.id.info_no_result_text_view)
    TextView noResultsTextView;

    @BindView(R.id.info_scores_layout)
    LinearLayout scoresLinearLayout;
    @BindView(R.id.info_imdb_layout)
    LinearLayout imdbLinearLayout;
    @BindView(R.id.info_imdb_score)
    DonutProgress imdbDonutProgress;
    @BindView(R.id.info_rottentomatoes_layout)
    LinearLayout rottenTomatoesLinearLayout;
    @BindView(R.id.info_rottentomatoes_score)
    DonutProgress rottenTomatoesDonutProgress;
    @BindView(R.id.info_metacritic_layout)
    LinearLayout metacriticLinearLayout;
    @BindView(R.id.info_metacritic_score)
    DonutProgress metacriticDonutProgress;

    @BindView(R.id.info_overview_layout)
    LinearLayout overviewLinearLayout;
    @BindView(R.id.info_tagline)
    TextView taglineTextView;
    @BindView(R.id.info_overview)
    TextView overviewTextView;

    @BindView(R.id.info_genres_flowlayout)
    FlowLayout genresFlowLayout;

    @BindView(R.id.info_main_layout)
    LinearLayout mainLinearLayout;
    @BindView(R.id.info_runtime)
    TextView runtimeTextView;
    @BindView(R.id.info_release_date)
    TextView releaseDateTextView;
    @BindView(R.id.info_age_rating)
    TextView ageRatingTextView;
    @BindView(R.id.info_original_language)
    TextView originalLanguageTextView;
    @BindView(R.id.info_production_countries)
    TextView productionCountriesTextView;
    @BindView(R.id.info_original_title)
    TextView originalTitleTextView;
    @BindView(R.id.info_production_companies)
    TextView productionCompaniesTextView;
    @BindView(R.id.info_spoken_languages)
    TextView spokenLanguagesTextView;
    @BindView(R.id.info_budget)
    TextView budgetTextView;
    @BindView(R.id.info_revenue)
    TextView revenueTextView;

    @BindView(R.id.info_collection_layout)
    RelativeLayout collectionLayout;
    @BindView(R.id.info_collection_background)
    ImageView collectionBackgroundImageView;
    @BindView(R.id.info_collection_poster)
    ImageView collectionPosterImageView;
    @BindView(R.id.info_collection_name)
    TextView collectionNameTextView;

    @BindView(R.id.info_tags_layout)
    LinearLayout tagsLayout;
    @BindView(R.id.info_tags_title)
    TextView tagsTitleTextView;
    @BindView(R.id.info_tags_flowlayout)
    FlowLayout tagsFlowLayout;

    private static TmdbMovie movie;
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
     * @param tmdbMovie is the {@link TmdbMovie} object.
     * @return a new instance of fragment InfoFragment.
     */
    public static InfoFragment newInstance(TmdbMovie tmdbMovie) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", tmdbMovie.getId());
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

        // Clean all elements in the layout when creating this fragment.
        clearLayout();

        // Get arguments from calling activity.
        if (getArguments() != null) {
            movieId = getArguments().getInt("id");
        }

        // Create an AsyncTaskLoader for retrieving complete movie information from internet in a
        // separate thread.
        getLoaderManager().initLoader(NetworkUtils.TMDB_MOVIE_DETAILS_LOADER_ID, null, this);

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
    public Loader<TmdbMovie> onCreateLoader(int id, Bundle args) {
        if (NetworkUtils.isConnected(getContext())) {
            // There is an available connection. Fetch results from TMDB.
            noResultsTextView.setText(getString(R.string.fetching_movie_info));
            noResultsTextView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            Log.i(TAG, "(onCreateLoader) Movie ID: " + movieId);
            return new TmdbMovieDetailsAsyncTaskLoader(getContext(), movieId);
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
    public void onLoadFinished(Loader<TmdbMovie> loader, TmdbMovie data) {
        // Hide progress bar.
        progressBar.setVisibility(View.INVISIBLE);
        noResultsTextView.setVisibility(View.INVISIBLE);

        // Check if there is an available connection.
        if (NetworkUtils.isConnected(getContext())) {
            // If there is a valid result, then update its data into the current {@link TmdbMovie}
            // object.
            if (data != null) {
                Log.i(TAG, "(onLoadFinished) Search results not null.");

                // Get movie info and display it.
                movie = data;
                setMovieInfo();

                String imdbId = movie.getImdb_id();
                if (imdbId != null && !imdbId.equals("")) {
                    // Create an instance of an OMDBinfo class in order to fetch additional info for
                    // this movie from OMDB API, using the just retrieved IMDB identifier of the
                    // movie.
                    new OMDBinfo(imdbId);
                }
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
    public void onLoaderReset(Loader<TmdbMovie> loader) {
    }

    /* -------------- */
    /* HELPER METHODS */
    /* -------------- */

    /**
     * Hide every info element in the layout.
     */
    void clearLayout() {
        scoresLinearLayout.setVisibility(View.GONE);
        overviewLinearLayout.setVisibility(View.GONE);
        mainLinearLayout.setVisibility(View.GONE);
        genresFlowLayout.setVisibility(View.GONE);
        collectionLayout.setVisibility(View.GONE);
        tagsLayout.setVisibility(View.GONE);
    }

    /**
     * Helper method to display all the movie information in this fragment.
     */
    void setMovieInfo() {
        // Set overview section.
        if (setOverviewSection())
            overviewLinearLayout.setVisibility(View.VISIBLE);

        // Set main info section.
        if (setMainInfoSection())
            mainLinearLayout.setVisibility(View.VISIBLE);

        // Set genres list.
        if (setGenresSection())
            genresFlowLayout.setVisibility(View.VISIBLE);

        // TODO. Set collection section.

        // TODO. Set tags section.
    }

    /**
     * Set overview section.
     *
     * @return true if any of the elements of this section has been set, false otherwise.
     */
    private boolean setOverviewSection() {
        boolean infoSectionSet = false;

        // Set tagline. If there is no overview, we hide this section.
        String tagline = movie.getTagline();
        if (tagline != null && !tagline.equals("") && !tagline.isEmpty()) {
            infoSectionSet = true;
            taglineTextView.setText(tagline);
            taglineTextView.setVisibility(View.VISIBLE);
        } else
            taglineTextView.setVisibility(View.GONE);

        // Set overview. If there is no overview, we show the default text for overview.
        String overview = movie.getOverview();
        if (overview != null && !overview.equals("") && !overview.isEmpty()) {
            infoSectionSet = true;
            overviewTextView.setText(overview);
            overviewLinearLayout.setVisibility(View.VISIBLE);
        } else
            overviewLinearLayout.setVisibility(View.GONE);

        return infoSectionSet;
    }

    /**
     * Set elements of primary info section.
     *
     * @return true if any of the elements of this section has been set, false otherwise.
     */
    private boolean setMainInfoSection() {
        boolean infoSectionSet = false;

        // Set runtime. If there is no available runtime info, hide this section.
        String runtime = DateTimeUtils.getHoursAndMinutes(getContext(), movie.getRuntime());
        if (runtime != null && !runtime.equals("") && !runtime.isEmpty()) {
            infoSectionSet = true;
            String htmlText = "<strong>" + getString(R.string.runtime).toUpperCase() +
                    "</strong><br>" + runtime;
            TextUtils.setHtmlText(runtimeTextView, htmlText);
            runtimeTextView.setVisibility(View.VISIBLE);
        } else {
            runtimeTextView.setVisibility(View.GONE);
        }

        // Set release date. If there is no available release date, hide this section.
        String releaseDate = movie.getRelease_date();
        if (releaseDate != null && !releaseDate.equals("") && !releaseDate.isEmpty()) {
            infoSectionSet = true;

            // TODO: release date info from append_to_response=release_dates

            String htmlText = "<strong>" + getString(R.string.release_date).toUpperCase() +
                    "</strong><br>" + releaseDate;

            // Add status to date if it is other than "released".
            String status = movie.getStatus();
            if (status != null && !status.equals("") && !status.isEmpty() &&
                    !status.equals(Tmdb.TMDB_STATUS_RELEASED)) {
                htmlText = htmlText + " (" + status + ")";
            }
            TextUtils.setHtmlText(releaseDateTextView, htmlText);
            releaseDateTextView.setVisibility(View.VISIBLE);
        } else
            releaseDateTextView.setVisibility(View.GONE);

        // TODO: age rating from append_to_response=release_dates
        ageRatingTextView.setVisibility(View.GONE);

        // Set original language. If there is no available original language info, hide this
        // section.
        String originalLanguage = movie.getOriginal_language();
        if (originalLanguage != null && !originalLanguage.equals("") && !originalLanguage.isEmpty()) {
            infoSectionSet = true;
            Locale locale = new Locale(originalLanguage);
            String language = locale.getDisplayLanguage().substring(0, 1).toUpperCase() +
                    locale.getDisplayLanguage().substring(1);
            String htmlText = "<strong>" + getString(R.string.original_language).toUpperCase() +
                    "</strong><br>" + language;
            TextUtils.setHtmlText(originalLanguageTextView, htmlText);
            originalLanguageTextView.setVisibility(View.VISIBLE);
        } else
            originalLanguageTextView.setVisibility(View.GONE);

        // Set original title. If there is no available original title info, hide this section.
        String originalTitle = movie.getOriginal_title();
        if (originalTitle != null && !originalTitle.equals("") && !originalTitle.isEmpty()) {
            infoSectionSet = true;
            String htmlText = "<strong>" + getString(R.string.original_title).toUpperCase() +
                    "</strong><br>" + originalTitle;
            TextUtils.setHtmlText(originalTitleTextView, htmlText);
            originalTitleTextView.setVisibility(View.VISIBLE);
        } else
            originalTitleTextView.setVisibility(View.GONE);

        // Set production companies. If there is no available countries info, we hide this section.
        ArrayList<TmdbMovieCompany> movieCompanies = movie.getProduction_companies();
        if (movieCompanies != null && movieCompanies.size() > 0) {
            infoSectionSet = true;
            StringBuilder stringBuilder = new StringBuilder();
            for (int n = 0; n < movieCompanies.size(); n++) {
                stringBuilder.append(movieCompanies.get(n).getName());
                if ((n + 1) < movieCompanies.size())
                    stringBuilder.append("<br>");
            }
            String htmlText = "<strong>" + getResources().getQuantityString(R.plurals.production_companies, movieCompanies.size()).toUpperCase()
                    + "</strong><br>" + stringBuilder;
            TextUtils.setHtmlText(productionCompaniesTextView, htmlText);
            productionCompaniesTextView.setVisibility(View.VISIBLE);
        } else
            productionCompaniesTextView.setVisibility(View.GONE);

        // Set production countries. If there is no available countries info, we hide this section.
        ArrayList<TmdbMovieCountry> movieCountries = movie.getProduction_countries();
        if (movieCountries != null && movieCountries.size() > 0) {
            infoSectionSet = true;
            StringBuilder stringBuilder = new StringBuilder();
            for (int n = 0; n < movieCountries.size(); n++) {
                stringBuilder.append(movieCountries.get(n).getName());
                if ((n + 1) < movieCountries.size())
                    stringBuilder.append("<br>");
            }
            String htmlText = "<strong>" + getResources().getQuantityString(R.plurals.production_countries, movieCountries.size()).toUpperCase()
                    + "</strong><br>" + stringBuilder;
            TextUtils.setHtmlText(productionCountriesTextView, htmlText);
            productionCountriesTextView.setVisibility(View.VISIBLE);
        } else
            productionCountriesTextView.setVisibility(View.GONE);

        // Set spoken languages. If there are less than two spoken languages, we hide this section.
        ArrayList<TmdbMovieLanguage> movieLanguages = movie.getSpoken_languages();
        if (movieLanguages != null && movieLanguages.size() > 1) {
            infoSectionSet = true;
            StringBuilder stringBuilder = new StringBuilder();
            for (int n = 0; n < movieLanguages.size(); n++) {
                Locale locale = new Locale(movieLanguages.get(n).getIso_639_1());
                String language = locale.getDisplayLanguage().substring(0, 1).toUpperCase() +
                        locale.getDisplayLanguage().substring(1);
                stringBuilder.append(language);
                if ((n + 1) < movieLanguages.size())
                    stringBuilder.append("<br>");
            }
            String htmlText = "<strong>" + getResources().getQuantityString(R.plurals.spoken_languages, movieLanguages.size()).toUpperCase()
                    + "</strong><br>" + stringBuilder;
            TextUtils.setHtmlText(spokenLanguagesTextView, htmlText);
            spokenLanguagesTextView.setVisibility(View.VISIBLE);
        } else
            spokenLanguagesTextView.setVisibility(View.GONE);

        // Set movie budget. If there is no available budget info, hide this section.
        int budget = movie.getBudget();
        if (budget > 0) {
            infoSectionSet = true;
            DecimalFormat decimalFormat = new DecimalFormat("###,###");
            String htmlText = "<strong>" + getString(R.string.budget_title).toUpperCase() +
                    "</strong><br>" + decimalFormat.format(budget);
            TextUtils.setHtmlText(budgetTextView, htmlText);
            budgetTextView.setVisibility(View.VISIBLE);
        } else
            budgetTextView.setVisibility(View.GONE);

        // Set movie revenue. If there is no available revenue info, hide this section.
        int revenue = movie.getRevenue();
        if (revenue > 0) {
            infoSectionSet = true;
            DecimalFormat decimalFormat = new DecimalFormat("###,###");
            String htmlText = "<strong>" + getString(R.string.revenue_title).toUpperCase() +
                    "</strong><br>" + decimalFormat.format(revenue);
            TextUtils.setHtmlText(revenueTextView, htmlText);
            revenueTextView.setVisibility(View.VISIBLE);
        } else
            revenueTextView.setVisibility(View.GONE);

        return infoSectionSet;
    }

    /**
     * Set genres section.
     *
     * @return true if there is one genre at least, false otherwise.
     */
    private boolean setGenresSection() {
        ArrayList<TmdbMovieGenre> movieGenres = movie.getGenres();

        genresFlowLayout.removeAllViews();
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (movieGenres != null && movieGenres.size() > 0) {
            for (int n = 0; n < movieGenres.size(); n++) {
                // Create the genre element into the FlowLayout.
                try {
                    View view = inflater.inflate(R.layout.list_item_genre, null);
                    final TextView genreTextView = (TextView) view.findViewById(R.id.genre_textview);
                    genreTextView.setText(movieGenres.get(n).getName());
                    genresFlowLayout.addView(view);

                    // Set a listener for managing click events on genres.
                    genreTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getContext(), "Genre clicked", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (NullPointerException e) {
                    Log.e(TAG, "(setGenresSection) Error inflating view for " + movieGenres.get(n).getName());
                }
            }
            return true;
        } else
            return false;
    }

    /* ------------- */
    /* INNER CLASSES */
    /* ------------- */

    /**
     * Inner class for fetching info from OMDB API.
     */
    class OMDBinfo implements LoaderManager.LoaderCallbacks<OmdbMovie> {
        private final String TAG = OMDBinfo.class.getSimpleName();
        private OmdbMovie omdbMovie;
        private String imdbId;

        OMDBinfo(String imdbId) {
            this.imdbId = imdbId;
            // Create an AsyncTaskLoader for retrieving additional movie information from OMDB in a
            // separate thread.
            getLoaderManager().initLoader(NetworkUtils.OMDB_MOVIES_LOADER_ID, null, this);
        }

        /**
         * Instantiate and return a new Loader for the given ID.
         *
         * @param id   The ID whose loader is to be created.
         * @param args Any arguments supplied by the caller.
         * @return Return a new Loader instance that is ready to start loading.
         */
        @Override
        public Loader<OmdbMovie> onCreateLoader(int id, Bundle args) {
            if (NetworkUtils.isConnected(getContext())) {
                // There is an available connection. Fetch results from OMDB.
                Log.i(TAG, "(onCreateLoader) Search OMDB for IMDB id: " + imdbId);
                return new OmdbMovieAsyncTaskLoader(getContext(), imdbId);
            } else {
                // There is no connection. Show error message.
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
        public void onLoadFinished(Loader<OmdbMovie> loader, OmdbMovie data) {
            // Check if there is an available connection.
            if (NetworkUtils.isConnected(getContext())) {
                // If there is a valid result, then update its data into the current {@link OmdbMovie}
                // object.
                if (data != null) {
                    Log.i(TAG, "(onLoadFinished) Search results not null.");
                    omdbMovie = data;

                    // Vote average from OMDB API.
                    if (setUserScores())
                        scoresLinearLayout.setVisibility(View.VISIBLE);
                    else
                        scoresLinearLayout.setVisibility(View.GONE);
                } else {
                    Log.i(TAG, "(onLoadFinished) No search results.");
                    scoresLinearLayout.setVisibility(View.GONE);
                }
            } else {
                // There is no connection.
                Log.i(TAG, "(onLoadFinished) No connection to internet.");
                scoresLinearLayout.setVisibility(View.GONE);
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
        public void onLoaderReset(Loader<OmdbMovie> loader) {

        }

        /* --------------------------------- */
        /* HELPER METHODS FOR OMDBinfo CLASS */
        /* --------------------------------- */

        /**
         * Helper method for setting the user ratings from OMDB API.
         *
         * @return true if at least one of the three external scores (IMDB, Rotten Tomatoes or
         * Metacritic) is set, false otherwise.
         */
        private boolean setUserScores() {
            boolean imdbRating = ScoreUtils.setRating(getContext(),
                    String.valueOf(omdbMovie.getImdb_vote_average()),
                    imdbDonutProgress);
            boolean rottenTomatoesRating = ScoreUtils.setRating(getContext(),
                    String.valueOf(omdbMovie.getRt_vote_average()),
                    rottenTomatoesDonutProgress);
            boolean metacriticRating = ScoreUtils.setRating(getContext(),
                    String.valueOf(omdbMovie.getMc_vote_average()),
                    metacriticDonutProgress);

            // Set transparency for score sections if there is no score.
            if (!imdbRating)
                imdbLinearLayout.setAlpha(0.2f);
            if (!rottenTomatoesRating)
                rottenTomatoesLinearLayout.setAlpha(0.2f);
            if (!metacriticRating)
                metacriticLinearLayout.setAlpha(0.2f);

            return (imdbRating || rottenTomatoesRating || metacriticRating);
        }
    }
}