package com.example.android.popularmoviesstage2.fragments;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.asynctaskloaders.OmdbMovieAsyncTaskLoader;
import com.example.android.popularmoviesstage2.asynctaskloaders.TmdbMovieDetailsAsyncTaskLoader;
import com.example.android.popularmoviesstage2.classes.OmdbMovie;
import com.example.android.popularmoviesstage2.classes.Tmdb;
import com.example.android.popularmoviesstage2.classes.TmdbMovie;
import com.example.android.popularmoviesstage2.classes.TmdbMovieCountry;
import com.example.android.popularmoviesstage2.classes.TmdbMovieGenre;
import com.example.android.popularmoviesstage2.utils.DateTimeUtils;
import com.example.android.popularmoviesstage2.utils.NetworkUtils;
import com.example.android.popularmoviesstage2.utils.ScoreUtils;
import com.example.android.popularmoviesstage2.utils.TextUtils;
import com.github.lzyzsd.circleprogress.DonutProgress;

import java.text.DecimalFormat;
import java.util.ArrayList;

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

    @BindView(R.id.info_primary_cardview)
    CardView mainCardView;

    @BindView(R.id.info_overview_linearlayout)
    LinearLayout overviewLinearLayout;
    @BindView(R.id.info_tagline_textview)
    TextView taglineTextView;
    @BindView(R.id.info_overview_textview)
    TextView overviewTextView;

    @BindView(R.id.info_main_linearlayout)
    LinearLayout mainLinearLayout;
    @BindView(R.id.info_runtime_linearlayout)
    LinearLayout runtimeLinearLayout;
    @BindView(R.id.info_runtime_textview)
    TextView runtimeTextView;
    @BindView(R.id.info_genres_linearlayout)
    LinearLayout genresLinearLayout;
    @BindView(R.id.info_genres_textview)
    TextView genresTextView;
    @BindView(R.id.info_production_countries_linearlayout)
    LinearLayout productionCountriesLinearLayout;
    @BindView(R.id.info_production_countries_textview)
    TextView productionCountriesTextView;
    @BindView(R.id.info_release_date_linearlayout)
    LinearLayout releaseDateLinearLayout;
    @BindView(R.id.info_release_date_textview)
    TextView releaseDateTextView;

    @BindView(R.id.info_secondary_linear_layout)
    LinearLayout secondaryLinearLayout;
    @BindView(R.id.info_original_title_textview)
    TextView originalTitleTextView;
    @BindView(R.id.info_original_language_textview)
    TextView originalLanguageTextView;
    @BindView(R.id.info_budget_textview)
    TextView budgetTextView;
    @BindView(R.id.info_revenue_textview)
    TextView revenueTextView;

    @BindView(R.id.info_age_rating_layout)
    LinearLayout ageRatingLinearLayout;
    @BindView(R.id.info_age_rating_image)
    ImageView ageRatingImageView;
    @BindView(R.id.info_age_rating)
    TextView ageRatingTextView;

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

    /* ------------- */
    /* INNER CLASSES */
    /* ------------- */

    /**
     * Inner class for fetching info from OMDB API.
     */
    class OMDBinfo implements LoaderManager.LoaderCallbacks<OmdbMovie> {
        private final String TAG = OMDBinfo.class.getSimpleName();
        private OmdbMovie OmdbMovie;
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
                    OmdbMovie = data;

                    // Vote average from OMDB API.
                    boolean imdbRating = ScoreUtils.setRating(getContext(),
                            String.valueOf(OmdbMovie.getImdb_vote_average()),
                            imdbDonutProgress);
                    boolean rottenTomatoesRating = ScoreUtils.setRating(getContext(),
                            String.valueOf(OmdbMovie.getRt_vote_average()),
                            rottenTomatoesDonutProgress);
                    boolean metacriticRating = ScoreUtils.setRating(getContext(),
                            String.valueOf(OmdbMovie.getMc_vote_average()),
                            metacriticDonutProgress);

                    if (!imdbRating)
                        imdbLinearLayout.setAlpha(0.2f);
                    if (!rottenTomatoesRating)
                        rottenTomatoesLinearLayout.setAlpha(0.2f);
                    if (!metacriticRating)
                        metacriticLinearLayout.setAlpha(0.2f);

                    if (!imdbRating && !rottenTomatoesRating && !metacriticRating)
                        scoresLinearLayout.setVisibility(View.GONE);
                    else
                        scoresLinearLayout.setVisibility(View.VISIBLE);

                    // Age rating from OMDB API.
                    if (OmdbMovie.getRated().equals(getString(R.string.rating_g))) {
                        setAgeRating(R.string.text_rating_g_1,
                                R.string.text_rating_g_2,
                                R.color.colorBlack,
                                R.color.colorDarkWhite,
                                R.drawable.rating_g_black);
                    } else if (OmdbMovie.getRated().equals(getString(R.string.rating_pg))) {
                        setAgeRating(R.string.text_rating_pg_1,
                                R.string.text_rating_pg_2,
                                R.color.colorBlack,
                                R.color.colorMediumScoreForeground,
                                R.drawable.rating_pg_black);
                    } else if (OmdbMovie.getRated().equals(getString(R.string.rating_pg_13))) {
                        setAgeRating(R.string.text_rating_pg_13_1,
                                R.string.text_rating_pg_13_2,
                                R.color.colorWhite,
                                R.color.colorLowScoreBackground,
                                R.drawable.rating_pg_13_white);
                    } else if (OmdbMovie.getRated().equals(getString(R.string.rating_r))) {
                        setAgeRating(R.string.text_rating_r_1,
                                R.string.text_rating_r_2,
                                R.color.colorWhite,
                                R.color.colorLowScoreForeground,
                                R.drawable.rating_r_white);
                    } else if (OmdbMovie.getRated().equals(getString(R.string.rating_nc_17)) || OmdbMovie.getRated().equals(getString(R.string.rating_nc_17))) {
                        setAgeRating(R.string.text_rating_nc_17_1,
                                R.string.text_rating_nc_17_2,
                                R.color.colorWhite,
                                R.color.colorPrimaryDark,
                                R.drawable.rating_nc_17_white);
                    } else
                        ageRatingLinearLayout.setVisibility(View.GONE);
                } else {
                    Log.i(TAG, "(onLoadFinished) No search results.");
                    scoresLinearLayout.setVisibility(View.GONE);
                    ageRatingLinearLayout.setVisibility(View.GONE);
                }
            } else {
                // There is no connection.
                Log.i(TAG, "(onLoadFinished) No connection to internet.");
                scoresLinearLayout.setVisibility(View.GONE);
                ageRatingLinearLayout.setVisibility(View.GONE);
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

    }

    /* -------------- */
    /* HELPER METHODS */
    /* -------------- */

    /**
     * Hide every info element in the layout.
     */
    void clearLayout() {
        scoresLinearLayout.setVisibility(View.GONE);
        mainCardView.setVisibility(View.GONE);
        ageRatingLinearLayout.setVisibility(View.GONE);
        secondaryLinearLayout.setVisibility(View.GONE);
    }

    /**
     * Helper method to display all the movie information in this fragment.
     */
    void setMovieInfo() {
        // Set main info section.
        boolean mainInfoIsSet = setMainInfoSection();
        if (mainInfoIsSet)
            mainCardView.setVisibility(View.VISIBLE);

        // Set secondary info section.
        boolean secondaryInfoIsSet = setSecondaryInfoSection();
        if (secondaryInfoIsSet)
            secondaryLinearLayout.setVisibility(View.VISIBLE);
    }

    /**
     * Set elements of primary info section.
     *
     * @return true if any of the elements of this section has been set, false otherwise.
     */
    private boolean setMainInfoSection() {
        boolean infoSectionSet = false;

        // Set tagline. If there is no overview, we hide this section.
        String tagline = movie.getTagline();
        if (tagline != null && !tagline.equals("") && !tagline.isEmpty()) {
            infoSectionSet = true;
            taglineTextView.setText(tagline);
            taglineTextView.setVisibility(View.VISIBLE);
        } else
            taglineTextView.setVisibility(View.GONE);

        // Set runtime. If there is no available runtime info, we hide this section.
        String runtime = DateTimeUtils.getHoursAndMinutes(getContext(), movie.getRuntime());
        if (runtime != null && !runtime.equals("") && !runtime.isEmpty()) {
            infoSectionSet = true;
            runtimeTextView.setText(runtime);
            runtimeLinearLayout.setVisibility(View.VISIBLE);
        } else {
            runtimeLinearLayout.setVisibility(View.GONE);
        }

        // Set genres. If there is no available genres info, we hide this section.
        ArrayList<TmdbMovieGenre> tmdbMovieGenres = movie.getGenres();
        if (tmdbMovieGenres != null && tmdbMovieGenres.size() > 0) {
            infoSectionSet = true;
            StringBuilder stringBuilder = new StringBuilder();
            for (int n = 0; n < tmdbMovieGenres.size(); n++) {
                stringBuilder.append(tmdbMovieGenres.get(n).getName());
                if ((n + 1) < tmdbMovieGenres.size())
                    stringBuilder.append(", ");
            }
            genresTextView.setText(stringBuilder);
            genresLinearLayout.setVisibility(View.VISIBLE);
        } else
            genresLinearLayout.setVisibility(View.GONE);

        // Set production countries. If there is no available countries info, we hide this section.
        ArrayList<TmdbMovieCountry> movieCountries = movie.getProduction_countries();
        if (movieCountries != null && movieCountries.size() > 0) {
            infoSectionSet = true;
            StringBuilder stringBuilder = new StringBuilder();
            for (int n = 0; n < movieCountries.size(); n++) {
                stringBuilder.append(movieCountries.get(n).getName());
                if ((n + 1) < movieCountries.size())
                    stringBuilder.append(", ");
            }
            productionCountriesTextView.setText(stringBuilder);
            productionCountriesLinearLayout.setVisibility(View.VISIBLE);
        } else
            productionCountriesLinearLayout.setVisibility(View.GONE);

        // Set release date. If there is no available release date, we hide this section.
        String releaseDate = movie.getRelease_date();
        if (releaseDate != null && !releaseDate.equals("") && !releaseDate.isEmpty()) {
            infoSectionSet = true;

            // Add status to date if it is other than "released".
            String status = movie.getStatus();
            if (status != null && !status.equals("") && !status.isEmpty() &&
                    !status.equals(Tmdb.TMDB_STATUS_RELEASED)) {
                releaseDate = releaseDate + " (" + status + ")";
                TextUtils.setHtmlText(releaseDateTextView, releaseDate);
            } else
                releaseDateTextView.setText(releaseDate);
            releaseDateLinearLayout.setVisibility(View.VISIBLE);
        } else
            releaseDateLinearLayout.setVisibility(View.GONE);

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
     * Set elements of secondary info section.
     *
     * @return true if any of the elements of this section has been set, false otherwise.
     */
    private boolean setSecondaryInfoSection() {
        boolean infoSectionSet = false;

        // Set original title. If there is no available original title info, hide this section.
        String originalTitle = movie.getOriginal_title();
        if (originalTitle != null && !originalTitle.equals("") && !originalTitle.isEmpty()) {
            infoSectionSet = true;
            String html = "<strong>Original title: </strong>" + originalTitle;
            TextUtils.setHtmlText(originalTitleTextView, html);
            originalTitleTextView.setVisibility(View.VISIBLE);
        } else
            originalTitleTextView.setVisibility(View.GONE);

        // Set original language. If there is no available original language info, hide this
        // section.
        String originalLanguage = movie.getOriginal_language();
        if (originalLanguage != null && !originalLanguage.equals("") && !originalLanguage.isEmpty()) {
            infoSectionSet = true;
            String html = "<strong>Original language: </strong>" + originalLanguage;
            TextUtils.setHtmlText(originalLanguageTextView, html);
            originalLanguageTextView.setVisibility(View.VISIBLE);
        } else
            originalLanguageTextView.setVisibility(View.GONE);

        // Set movie budget. If there is no available budget info, hide this section.
        int budget = movie.getBudget();
        if (budget > 0) {
            infoSectionSet = true;
            DecimalFormat decimalFormat = new DecimalFormat("###,###");
            String html = "<strong>Budget: </strong>" + decimalFormat.format(budget) + " $";
            TextUtils.setHtmlText(budgetTextView, html);
            budgetTextView.setVisibility(View.VISIBLE);
        } else
            budgetTextView.setVisibility(View.GONE);

        // Set movie revenue. If there is no available revenue info, hide this section.
        int revenue = movie.getRevenue();
        if (revenue > 0) {
            infoSectionSet = true;
            DecimalFormat decimalFormat = new DecimalFormat("###,###");
            String html = "<strong>Revenue: </strong>" + decimalFormat.format(revenue) + " $";
            TextUtils.setHtmlText(revenueTextView, html);
            revenueTextView.setVisibility(View.VISIBLE);
        } else
            revenueTextView.setVisibility(View.GONE);

        return infoSectionSet;
    }

    /**
     * Helper method for setting the age rating section.
     *
     * @param textResource1           is the string resource of the first sentence of the age rating
     *                                text.
     * @param textResource2           is the string resource of the second sentence of the age
     *                                rating text.
     * @param textColorResource       is the color resource of the text color of the age rating
     *                                section.
     * @param backgroundColorResource is the color resource of the background color of the age
     *                                rating section.
     * @param drawableImageResource   is the drawable resource of the age rating icon to be drawn.
     */
    private void setAgeRating(int textResource1, int textResource2, int textColorResource,
                              int backgroundColorResource, int drawableImageResource) {
        // Set text for age rating.
        String text1 = getString(textResource1).trim();
        String text2 = getString(textResource2).trim();
        String htmlText;
        if (!text1.equals(""))
            if (!text2.equals(""))
                htmlText = "<strong>" + text1 + "</strong><br>" + text2;
            else
                htmlText = "<strong>" + text1 + "</strong>";
        else
            htmlText = text2;
        TextUtils.setHtmlText(ageRatingTextView, htmlText);

        // Set colors.
        ageRatingTextView.setTextColor(getResources().getColor(textColorResource));
        ageRatingLinearLayout.setBackgroundColor(getResources().getColor(backgroundColorResource));

        // Set icons.
        ageRatingImageView.setImageDrawable(getResources().getDrawable(drawableImageResource));

        // Show this section.
        ageRatingLinearLayout.setVisibility(View.VISIBLE);
    }
}