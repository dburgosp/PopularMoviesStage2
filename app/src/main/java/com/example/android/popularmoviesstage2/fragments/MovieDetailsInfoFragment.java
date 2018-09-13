package com.example.android.popularmoviesstage2.fragments;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.activities.MovieDetailsActivity;
import com.example.android.popularmoviesstage2.activities.MoviesListActivity;
import com.example.android.popularmoviesstage2.adapters.MoviesListAdapter;
import com.example.android.popularmoviesstage2.asynctaskloaders.OmdbMovieAsyncTaskLoader;
import com.example.android.popularmoviesstage2.asynctaskloaders.TmdbMovieDetailsAsyncTaskLoader;
import com.example.android.popularmoviesstage2.asynctaskloaders.TmdbMoviesCollectionAsyncTaskLoader;
import com.example.android.popularmoviesstage2.classes.Facebook;
import com.example.android.popularmoviesstage2.classes.Imdb;
import com.example.android.popularmoviesstage2.classes.Instagram;
import com.example.android.popularmoviesstage2.classes.OmdbMovie;
import com.example.android.popularmoviesstage2.classes.Tmdb;
import com.example.android.popularmoviesstage2.classes.TmdbKeyword;
import com.example.android.popularmoviesstage2.classes.TmdbMovie;
import com.example.android.popularmoviesstage2.classes.TmdbMovieCollection;
import com.example.android.popularmoviesstage2.classes.TmdbMovieCompany;
import com.example.android.popularmoviesstage2.classes.TmdbMovieCountry;
import com.example.android.popularmoviesstage2.classes.TmdbMovieDetails;
import com.example.android.popularmoviesstage2.classes.TmdbGenre;
import com.example.android.popularmoviesstage2.classes.TmdbMovieLanguage;
import com.example.android.popularmoviesstage2.classes.TmdbRelease;
import com.example.android.popularmoviesstage2.classes.Twitter;
import com.example.android.popularmoviesstage2.itemdecorations.SpaceItemDecoration;
import com.example.android.popularmoviesstage2.utils.AnimatedViewsUtils;
import com.example.android.popularmoviesstage2.utils.DateTimeUtils;
import com.example.android.popularmoviesstage2.utils.LocaleUtils;
import com.example.android.popularmoviesstage2.utils.NetworkUtils;
import com.example.android.popularmoviesstage2.utils.ScoreUtils;
import com.example.android.popularmoviesstage2.utils.SoundsUtils;
import com.example.android.popularmoviesstage2.utils.TextViewUtils;
import com.example.android.popularmoviesstage2.viewgroups.FlowLayout;
import com.github.lzyzsd.circleprogress.DonutProgress;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link Fragment} that displays the main_menu information about the current movieDetails.
 */
public class MovieDetailsInfoFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<TmdbMovieDetails> {
    private static final String TAG = MovieDetailsInfoFragment.class.getSimpleName();

    // Annotate fields with @BindView and views ID for Butter Knife to find and automatically cast
    // the corresponding views.
    @BindView(R.id.info_loading_indicator)
    ProgressBar progressBar;
    @BindView(R.id.info_no_result_text_view)
    TextView noResultsTextView;
    @BindView(R.id.info_no_result_image_view)
    ImageView noResultsImageView;

    @BindView(R.id.movie_details_info_scores_layout)
    LinearLayout scoresLinearLayout;
    @BindView(R.id.movie_details_info_imdb_layout)
    LinearLayout imdbLinearLayout;
    @BindView(R.id.movie_details_info_imdb_score)
    DonutProgress imdbDonutProgress;
    @BindView(R.id.movie_details_info_rottentomatoes_layout)
    LinearLayout rottenTomatoesLinearLayout;
    @BindView(R.id.movie_details_info_rottentomatoes_score)
    DonutProgress rottenTomatoesDonutProgress;
    @BindView(R.id.movie_details_info_metacritic_layout)
    LinearLayout metacriticLinearLayout;
    @BindView(R.id.info_metacritic_score)
    DonutProgress metacriticDonutProgress;

    @BindView(R.id.movie_details_info_overview_layout)
    LinearLayout overviewLinearLayout;
    @BindView(R.id.movie_details_info_tagline)
    TextView taglineTextView;
    @BindView(R.id.movie_details_info_overview)
    TextView overviewTextView;

    @BindView(R.id.movie_details_info_genres_flowlayout)
    FlowLayout genresFlowLayout;
    @BindView(R.id.movie_details_info_genres_title)
    TextView genresTextView;

    @BindView(R.id.info_data_layout)
    LinearLayout dataLayout;

    @BindView(R.id.movie_details_info_main_layout)
    LinearLayout mainLinearLayout;
    @BindView(R.id.movie_details_info_main_runtime_header)
    TextView runtimeHeaderTextView;
    @BindView(R.id.movie_details_info_main_runtime_content)
    TextView runtimeContentTextView;
    @BindView(R.id.movie_details_info_main_release_date_header)
    TextView releaseDateHeaderTextView;
    @BindView(R.id.movie_details_info_main_release_date_content)
    TextView releaseDateContentTextView;
    @BindView(R.id.movie_details_info_main_age_rating_header)
    TextView ageRatingHeaderTextView;
    @BindView(R.id.movie_details_info_main_age_rating_content)
    TextView ageRatingContentTextView;
    @BindView(R.id.movie_details_info_main_original_language_header)
    TextView originalLanguageHeaderTextView;
    @BindView(R.id.movie_details_info_main_original_language_content)
    TextView originalLanguageContentTextView;
    @BindView(R.id.movie_details_info_main_production_countries_header)
    TextView productionCountriesHeaderTextView;
    @BindView(R.id.movie_details_info_main_production_countries_content)
    TextView productionCountriesContentTextView;
    @BindView(R.id.movie_details_info_main_original_title_header)
    TextView originalTitleHeaderTextView;
    @BindView(R.id.movie_details_info_main_original_title_content)
    TextView originalTitleContentTextView;
    @BindView(R.id.movie_details_info_main_production_companies_header)
    TextView productionCompaniesHeaderTextView;
    @BindView(R.id.movie_details_info_main_production_companies_content)
    TextView productionCompaniesContentTextView;
    @BindView(R.id.movie_details_info_main_budget_header)
    TextView budgetHeaderTextView;
    @BindView(R.id.movie_details_info_main_budget_content)
    TextView budgetContentTextView;
    @BindView(R.id.movie_details_info_main_revenue_header)
    TextView revenueHeaderTextView;
    @BindView(R.id.movie_details_info_main_revenue_content)
    TextView revenueContentTextView;

    @BindView(R.id.movie_details_info_collection_layout)
    LinearLayout collectionLayout;
    @BindView(R.id.movie_details_info_collection_name)
    TextView collectionNameTextView;
    @BindView(R.id.movie_details_info_collection_overview)
    TextView collectionOverviewTextView;
    @BindView(R.id.movie_details_info_collection_recyclerview)
    RecyclerView collectionRecyclerView;

    @BindView(R.id.movie_details_info_links_layout)
    LinearLayout linksLayout;
    @BindView(R.id.movie_details_info_links_homepage)
    LinearLayout homepageLinkLinearLayout;
    @BindView(R.id.movie_details_info_links_imdb)
    LinearLayout imdbLinkLinearLayout;
    @BindView(R.id.movie_details_info_links_twitter)
    LinearLayout twitterLinkLinearLayout;
    @BindView(R.id.movie_details_info_links_facebook)
    LinearLayout facebookLinkLinearLayout;
    @BindView(R.id.movie_details_info_links_instagram)
    LinearLayout instagramLinkLinearLayout;

    @BindView(R.id.movie_details_recommended_movies_layout)
    LinearLayout recommendedMoviesLayout;
    @BindView(R.id.movie_details_recommended_movies_view_all)
    TextView recommendedMoviesViewAllTextView;
    @BindView(R.id.movie_details_recommended_movies_recyclerview)
    RecyclerView recommendedMoviesRecyclerView;
    @BindView(R.id.movie_details_recommended_movies_view_all_action)
    TextView recommendedMoviesViewAllActionTextView;
    @BindView(R.id.movie_details_recommended_movies_view_all_cardview)
    CardView recommendedMoviesViewAllCardView;

    @BindView(R.id.movie_details_info_keywords_layout)
    LinearLayout keywordsLayout;
    @BindView(R.id.movie_details_info_keywords_title)
    TextView keywordsTitleTextView;
    @BindView(R.id.movie_details_info_keywords_flowlayout)
    FlowLayout keywordsFlowLayout;

    @BindView(R.id.movie_details_recommended_and_keywords_layout)
    LinearLayout recommendedMoviesKeywordsLayout;

    private static TmdbMovieDetails movieDetails;
    private int movieId;
    private MoviesListAdapter recommendedMoviesAdapter, collectionAdapter;
    String ageRating = "";

    /**
     * Required empty public constructor.
     */
    public MovieDetailsInfoFragment() {
    }

    /**
     * Factory method to create a new instance of this fragment using the provided parameters.
     *
     * @param movie is the {@link TmdbMovie} object.
     * @return a new instance of fragment MovieDetailsInfoFragment.
     */
    public static MovieDetailsInfoFragment newInstance(TmdbMovie movie) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", movie.getId());
        MovieDetailsInfoFragment fragment = new MovieDetailsInfoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details_info, container, false);
        ButterKnife.bind(this, rootView);

        // Clean all elements in the layout when creating this fragment.
        clearLayout();

        // Set recycler views for recommended movies_menu and collection, if available.
        setRecyclerViews();

        // Get arguments from calling activity.
        if (getArguments() != null) {
            movieId = getArguments().getInt("id");
        }

        // Create an AsyncTaskLoader for retrieving complete movieDetails information from internet in a
        // separate thread.
        getLoaderManager().initLoader(NetworkUtils.TMDB_MOVIE_DETAILS_LOADER_ID, null, this);

        Log.i(TAG, "(onCreate) Fragment created");
        return rootView;
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
    public Loader<TmdbMovieDetails> onCreateLoader(int id, Bundle args) {
        if (NetworkUtils.isConnected(getContext())) {
            // There is an available connection. Fetch results from TMDB.
            noResultsTextView.setText(getString(R.string.fetching_movie_info));
            noResultsTextView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            Log.i(TAG, "(onCreateLoader) Movie ID: " + movieId);
            return new TmdbMovieDetailsAsyncTaskLoader(getContext(), movieId,
                    Locale.getDefault().getLanguage());
        } else {
            // There is no connection. Show error message.
            noResultsImageView.setVisibility(View.VISIBLE);
            noResultsTextView.setText(getString(R.string.no_connection));
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
     * management of its data so you don't have to.
     *
     * @param loader The Loader that has finished.
     * @param data   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<TmdbMovieDetails> loader, TmdbMovieDetails data) {
        // Hide progress status.
        progressBar.setVisibility(View.GONE);
        noResultsTextView.setVisibility(View.GONE);

        // Check if there is an available connection.
        if (NetworkUtils.isConnected(getContext())) {
            // If there is a valid result, then update its data into the current {@link TmdbMovieDetails}
            // object.
            if (data != null) {
                Log.i(TAG, "(onLoadFinished) Search results not null.");

                // Get movieDetails info and display it.
                movieDetails = data;
                setMovieInfo();

                String imdbId = movieDetails.getImdb_id();
                if (imdbId != null && !imdbId.equals("")) {
                    // Create an instance of an OMDBinfo class in order to fetch additional info for
                    // this movieDetails from OMDB API, using the just retrieved IMDB identifier of the
                    // movieDetails.
                    new OMDBinfo(imdbId);
                }
            } else {
                Log.i(TAG, "(onLoadFinished) No search results.");
                noResultsImageView.setVisibility(View.VISIBLE);
                noResultsTextView.setVisibility(View.VISIBLE);
                noResultsTextView.setText(getString(R.string.no_movie_info));
            }
        } else {
            // There is no connection. Show error message.
            Log.i(TAG, "(onLoadFinished) No connection to internet.");
            noResultsImageView.setVisibility(View.VISIBLE);
            noResultsTextView.setVisibility(View.VISIBLE);
            noResultsTextView.setText(getString(R.string.no_connection));
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
    public void onLoaderReset(Loader<TmdbMovieDetails> loader) {
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
        dataLayout.setVisibility(View.GONE);
        mainLinearLayout.setVisibility(View.GONE);
        collectionLayout.setVisibility(View.GONE);
        linksLayout.setVisibility(View.GONE);
        recommendedMoviesLayout.setVisibility(View.GONE);
        keywordsLayout.setVisibility(View.GONE);
        recommendedMoviesKeywordsLayout.setVisibility(View.GONE);
        noResultsImageView.setVisibility(View.GONE);
    }

    /**
     * Helper method for setting the RecyclerView in order to display lists of recommended movies_menu
     * with a horizontal arrangement.
     */
    void setRecyclerViews() {
        // Set the LayoutManager for the RecyclerViews.
        int horizontalSeparation = getResources().getDimensionPixelOffset(R.dimen.small_padding);

        recommendedMoviesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        recommendedMoviesRecyclerView.setHasFixedSize(true);
        recommendedMoviesRecyclerView.addItemDecoration(new SpaceItemDecoration(horizontalSeparation,
                SpaceItemDecoration.HORIZONTAL_SEPARATION));

        collectionRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        collectionRecyclerView.setHasFixedSize(true);
        collectionRecyclerView.addItemDecoration(new SpaceItemDecoration(horizontalSeparation,
                SpaceItemDecoration.HORIZONTAL_SEPARATION));

        // Set the listeners for click events in the adapters.
        MoviesListAdapter.OnItemClickListener recommendedMovieListener =
                new MoviesListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(TmdbMovie clickedMovie, View clickedView) {
                        if (movieId != clickedMovie.getId()) {
                            // Play a sound when clicked.
                            SoundsUtils.buttonClick(getContext());

                            // Start MovieDetailsActivity to show movie movie_details_menu when the
                            // current element is clicked. We need to know when the other activity
                            // finishes, so we use startActivityForResult. No need a requestCode,
                            // we don't care for any result.
                            Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
                            intent.putExtra(MovieDetailsActivity.EXTRA_PARAM_MOVIE, clickedMovie);
                            intent.putExtra(MovieDetailsActivity.EXTRA_PARAM_CALLING_MOVIE_TITLE,
                                    movieDetails.getMovie().getTitle());

                            // Create an ActivityOptions to transition between Activities using
                            // cross-Activity scene animations.
                            Bundle option = ActivityOptions.makeSceneTransitionAnimation(
                                    getActivity(), clickedView, getString(R.string.transition_poster)).toBundle();

                            // Animate view when clicked and navigate to next activity.
                            AnimatedViewsUtils.animateOnClick(getActivity(), clickedView);
                            startActivityForResult(intent, 0, option);
                        } else {
                            // If we are clicking on the current movie (it may be shown again as
                            // part of the collection or in the recommended movies_menu list), we
                            // don't need to move from here.
                            Toast.makeText(getContext(), getString(R.string.dont_open_this_again),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                };

        // Set the Adapter for the RecyclerViews.
        recommendedMoviesAdapter = new MoviesListAdapter(
                R.layout.list_item_poster_horizontal_layout_1, new ArrayList<TmdbMovie>(),
                recommendedMovieListener);
        recommendedMoviesRecyclerView.setAdapter(recommendedMoviesAdapter);

        collectionAdapter = new MoviesListAdapter(
                R.layout.list_item_poster_horizontal_layout_3, new ArrayList<TmdbMovie>(),
                recommendedMovieListener);
        collectionRecyclerView.setAdapter(collectionAdapter);
    }

    /**
     * Helper method to display all the movieDetails information in this fragment.
     */
    void setMovieInfo() {
        // Set collection section. This must be at the beginning of this method to save some time,
        // because it opens another thread to retrieve info.
        if (setCollectionSection())
            collectionLayout.setVisibility(View.VISIBLE);

        // Set overview section.
        if (setOverviewSection())
            overviewLinearLayout.setVisibility(View.VISIBLE);

        // Set main_menu info section.
        if (setMainInfoSection())
            mainLinearLayout.setVisibility(View.VISIBLE);

        // Set external links section.
        if (setLinksSection())
            linksLayout.setVisibility(View.VISIBLE);

        // Main info and external links section are into the same layout. Make it visible if any
        // of the inner sections has data.
        if (mainLinearLayout.getVisibility() == View.VISIBLE ||
                linksLayout.getVisibility() == View.VISIBLE)
            dataLayout.setVisibility(View.VISIBLE);

        // Recommended movies_menu section.
        if (setRecommendedMoviesSection())
            recommendedMoviesLayout.setVisibility(View.VISIBLE);

        // Set keywords section.
        if (setKeywordsSection())
            keywordsLayout.setVisibility(View.VISIBLE);

        // Recommended movies_menu and keywords section are into the same layout. Make it visible if any
        // of the inner sections has data.
        if (recommendedMoviesLayout.getVisibility() == View.VISIBLE ||
                keywordsLayout.getVisibility() == View.VISIBLE)
            recommendedMoviesKeywordsLayout.setVisibility(View.VISIBLE);
    }

    /**
     * Set overview section.
     *
     * @return true if any of the elements of this section has been set, false otherwise.
     */
    @SuppressWarnings("ConstantConditions")
    private boolean setOverviewSection() {
        boolean infoSectionSet = false;

        /* -------------------- */
        /* TAGLINE AND OVERVIEW */
        /* -------------------- */

        String overview = movieDetails.getMovie().getOverview();
        if (overview != null && !overview.equals("") && !overview.isEmpty()) {
            infoSectionSet = true;
            overviewTextView.setText(overview);

            // Show only the tagline if there is an overview.
            String tagline = movieDetails.getTagline();
            if (tagline != null && !tagline.equals("") && !tagline.isEmpty()) {
                infoSectionSet = true;
                taglineTextView.setText(tagline);
            } else {
                // There is no tagline but there is an overview. Write "Synopsis" text instead.
                taglineTextView.setText(getString(R.string.synopsis).toUpperCase());
            }
        } else {
            // Hide tagline and overview sections.
            taglineTextView.setVisibility(View.GONE);
            overviewTextView.setVisibility(View.GONE);
        }

        /* ------ */
        /* GENRES */
        /* ------ */

        ArrayList<TmdbGenre> movieGenres = movieDetails.getMovie().getGenres();
        if (movieGenres != null && movieGenres.size() > 0) {
            infoSectionSet = true;

            // Set section title.
            String genresTitle =
                    getResources().getQuantityString(R.plurals.genres, movieGenres.size());
            genresTextView.setText(genresTitle);

            // Manage genres array.
            genresFlowLayout.removeAllViews();
            LayoutInflater inflater =
                    (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            for (int n = 0; n < movieGenres.size(); n++) {
                // Create the genre element into the FlowLayout.
                try {
                    final ViewGroup nullParent = null;
                    View view = inflater.inflate(R.layout.list_item_flowlayout, nullParent);
                    final TextView genreTextView =
                            (TextView) view.findViewById(R.id.flowlayout_textview);
                    genreTextView.setText(movieGenres.get(n).getName());
                    genreTextView.setId(movieGenres.get(n).getId());
                    genresFlowLayout.addView(view);

                    // Set a listener for managing click events on genres.
                    genreTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getContext(), MoviesListActivity.class);
                            intent.putExtra(MoviesListActivity.PARAM_GENRE_ID,
                                    genreTextView.getId());
                            intent.putExtra(MoviesListActivity.PARAM_GENRE_NAME,
                                    genreTextView.getText());
                            Bundle option = ActivityOptions
                                    .makeSceneTransitionAnimation(getActivity()).toBundle();

                            // Animate view when clicked and navigate to next activity.
                            AnimatedViewsUtils.animateOnClick(getActivity(), view);
                            startActivity(intent, option);
                        }
                    });
                } catch (java.lang.NullPointerException e) {
                    Log.e(TAG, "(setMainInfoSection) Error inflating view for " +
                            movieGenres.get(n).getName());
                }
            }
        } else {
            // Hide genres section.
            genresTextView.setVisibility(View.GONE);
            genresFlowLayout.setVisibility(View.GONE);
        }

        return infoSectionSet;
    }

    /**
     * Set elements of primary info section.
     *
     * @return true if any of the elements of this section has been set, false otherwise.
     */
    private boolean setMainInfoSection() {
        boolean infoSectionSet = false;
        String currentCountry = LocaleUtils.getCurrentCountryName();
        StringBuilder stringBuilder = new StringBuilder();

        /* ------- */
        /* RUNTIME */
        /* ------- */

        String runtime = DateTimeUtils.getHoursAndMinutes(getContext(), movieDetails.getRuntime());
        if (runtime != null && !runtime.equals("") && !runtime.isEmpty()) {
            infoSectionSet = true;
            runtimeContentTextView.setText(runtime);
            runtimeHeaderTextView.setVisibility(View.VISIBLE);
            runtimeContentTextView.setVisibility(View.VISIBLE);
        } else {
            runtimeHeaderTextView.setVisibility(View.GONE);
            runtimeContentTextView.setVisibility(View.GONE);
        }

        /* ------------- */
        /* RELEASE DATES */
        /* ------------- */

        String releaseDatesTitle = getResources().getQuantityString(R.plurals.release_dates, 1);
        String releaseDatesContent = "";

        // Show release status if it is other than "released".
        String status = movieDetails.getStatus();
        if (status != null && !status.equals("") && !status.isEmpty() &&
                !status.equals(TmdbRelease.TMDB_RELEASE_STATUS_RELEASED)) {
            switch (status) {
                case TmdbRelease.TMDB_RELEASE_STATUS_CANCELED:
                    status = getString(R.string.release_status_canceled);
                    break;
                case TmdbRelease.TMDB_RELEASE_STATUS_IN_PRODUCTION:
                    status = getString(R.string.release_status_in_production);
                    break;
                case TmdbRelease.TMDB_RELEASE_STATUS_PLANNED:
                    status = getString(R.string.release_status_planned);
                    break;
                case TmdbRelease.TMDB_RELEASE_STATUS_POST_PRODUCTION:
                    status = getString(R.string.release_status_post_production);
                    break;
                default:
                    status = getString(R.string.release_status_rumored);
            }
            releaseDatesContent = status;
        }

        // If there is local release dates info, show it instead the 'official' release date.
        TmdbRelease releases = movieDetails.getReleases();
        if (releases != null) {
            // Extract release dates info related to current country.
            for (int i = 0; i < releases.getReleaseDateArrayList().size(); i++) {
                String date = releases.getReleaseDateArrayList().get(i).getRelease_date();
                stringBuilder.append(DateTimeUtils.getStringDate(date, DateTimeUtils.DATE_FORMAT_LONG));

                // Set release type, if exists and if it is not a theatrical release date.
                int type = releases.getReleaseDateArrayList().get(i).getType();
                if (type > 0) {
                    stringBuilder.append(" (");
                    switch (releases.getReleaseDateArrayList().get(i).getType()) {
                        case TmdbRelease.TMDB_RELEASE_TYPE_DIGITAL:
                            stringBuilder.append(getString(R.string.release_type_digital,
                                    currentCountry));
                            break;
                        case TmdbRelease.TMDB_RELEASE_TYPE_PHYSICAL:
                            stringBuilder.append(getString(R.string.release_type_physical,
                                    currentCountry));
                            break;
                        case TmdbRelease.TMDB_RELEASE_TYPE_PREMIERE:
                            stringBuilder.append(getString(R.string.release_type_premiere,
                                    currentCountry));
                            break;
                        case TmdbRelease.TMDB_RELEASE_TYPE_THEATRICAL:
                            stringBuilder.append(getString(R.string.release_type_theatrical,
                                    currentCountry));
                            break;
                        case TmdbRelease.TMDB_RELEASE_TYPE_THEATRICAL_LIMITED:
                            stringBuilder.append(getString(R.string.release_type_theatrical_limited,
                                    currentCountry));
                            break;
                        default:
                            stringBuilder.append(getString(R.string.release_type_tv,
                                    currentCountry));
                    }
                    String note = releases.getReleaseDateArrayList().get(i).getNote();
                    if (note != null && !note.equals("") && !note.isEmpty()) {
                        stringBuilder.append(", ");
                        stringBuilder.append(note);
                    }
                    stringBuilder.append(")");
                }

                if ((i + 1) < releases.getReleaseDateArrayList().size())
                    stringBuilder.append("<br>");

                // Save age rating for later.
                String certification = releases.getReleaseDateArrayList().get(i).getCertification();
                if (certification != null && !certification.equals("") && !certification.isEmpty())
                    ageRating = certification;
            }

            // Set section title.
            releaseDatesTitle = getResources().getQuantityString(R.plurals.release_dates,
                    releases.getReleaseDateArrayList().size());

            // Append previous status info, if exists.
            if (releaseDatesContent.isEmpty())
                releaseDatesContent = stringBuilder.toString();
            else
                releaseDatesContent = releaseDatesContent + "<br>" + stringBuilder;
        } else {
            // If there is no local release dates, show official release date.
            String releaseDate = movieDetails.getMovie().getRelease_date();
            if (releaseDate != null && !releaseDate.equals("") && !releaseDate.isEmpty()) {
                // Official release date.
                releaseDatesContent = DateTimeUtils.getStringDate(releaseDate,
                        DateTimeUtils.DATE_FORMAT_LONG);
            }
        }

        // Show this section only if there is information about release dates.
        if (!releaseDatesContent.equals("")) {
            infoSectionSet = true;
            releaseDateHeaderTextView.setText(releaseDatesTitle);
            releaseDateHeaderTextView.setVisibility(View.VISIBLE);
            TextViewUtils.setHtmlText(releaseDateContentTextView, releaseDatesContent);
            releaseDateContentTextView.setVisibility(View.VISIBLE);
        } else {
            releaseDateHeaderTextView.setVisibility(View.GONE);
            releaseDateContentTextView.setVisibility(View.GONE);
        }

        /* ---------- */
        /* AGE RATING */
        /* ---------- */

        // We obtained the age rating previously, if it exists, from the release dates information.
        if (!ageRating.equals("")) {
            String ageRatingContent = ageRating + " (" + currentCountry + ")";
            ageRatingContentTextView.setText(ageRatingContent);
            ageRatingContentTextView.setVisibility(View.VISIBLE);
            ageRatingHeaderTextView.setVisibility(View.VISIBLE);
        } else {
            ageRatingHeaderTextView.setVisibility(View.GONE);
            ageRatingContentTextView.setVisibility(View.GONE);
        }

        /* --------- */
        /* LANGUAGES */
        /* --------- */

        String originalLanguage = movieDetails.getMovie().getOriginal_language();
        if (originalLanguage != null && !originalLanguage.equals("") && !originalLanguage.isEmpty()) {
            infoSectionSet = true;
            String languageName = LocaleUtils.getLanguageName(originalLanguage);
            String htmlText =
                    languageName.substring(0, 1).toUpperCase() + languageName.substring(1);

            // Add other spoken languages, if available.
            ArrayList<TmdbMovieLanguage> movieLanguages = movieDetails.getSpoken_languages();
            if (movieLanguages != null && movieLanguages.size() > 1) {
                stringBuilder = new StringBuilder();
                String otherLanguageName;
                for (int n = 0; n < movieLanguages.size(); n++) {
                    otherLanguageName =
                            LocaleUtils.getLanguageName(movieLanguages.get(n).getIso_639_1());

                    // Add another language only if it is not the same as the original language.
                    if (!languageName.equals(otherLanguageName)) {
                        // Append ',' if this is not the first language appended.
                        if (n > 0 && stringBuilder.length() > 0)
                            stringBuilder.append(", ");

                        stringBuilder.append(otherLanguageName);
                    }
                }
                htmlText = htmlText + "<br>" + getString(R.string.spoken_languages) + ": " + stringBuilder;
            }

            TextViewUtils.setHtmlText(originalLanguageContentTextView, htmlText);
            originalLanguageHeaderTextView.setVisibility(View.VISIBLE);
            originalLanguageContentTextView.setVisibility(View.VISIBLE);
        } else {
            originalLanguageHeaderTextView.setVisibility(View.GONE);
            originalLanguageContentTextView.setVisibility(View.GONE);
        }

        /* -------------- */
        /* ORIGINAL TITLE */
        /* -------------- */

        String originalTitle = movieDetails.getMovie().getOriginal_title();
        if (originalTitle != null && !originalTitle.equals("") && !originalTitle.isEmpty()) {
            infoSectionSet = true;
            originalTitleContentTextView.setText(originalTitle);
            originalTitleHeaderTextView.setVisibility(View.VISIBLE);
            originalTitleContentTextView.setVisibility(View.VISIBLE);
        } else {
            originalTitleHeaderTextView.setVisibility(View.GONE);
            originalTitleContentTextView.setVisibility(View.GONE);
        }

        /* -------------------- */
        /* PRODUCTION COMPANIES */
        /* -------------------- */

        ArrayList<TmdbMovieCompany> movieCompanies = movieDetails.getProduction_companies();
        if (movieCompanies != null && movieCompanies.size() > 0) {
            infoSectionSet = true;

            // Section title.
            String productionCompaniesTitle =
                    getResources().getQuantityString(R.plurals.production_companies,
                            movieCompanies.size());
            productionCompaniesHeaderTextView.setText(productionCompaniesTitle);
            productionCompaniesHeaderTextView.setVisibility(View.VISIBLE);

            // Section content.
            stringBuilder = new StringBuilder();
            for (int n = 0; n < movieCompanies.size(); n++) {
                stringBuilder.append(movieCompanies.get(n).getName());
                if ((n + 1) < movieCompanies.size())
                    stringBuilder.append("<br>");
            }
            String htmlText = stringBuilder.toString();
            TextViewUtils.setHtmlText(productionCompaniesContentTextView, htmlText);
            productionCompaniesContentTextView.setVisibility(View.VISIBLE);
        } else {
            productionCompaniesHeaderTextView.setVisibility(View.GONE);
            productionCompaniesContentTextView.setVisibility(View.GONE);
        }

        /* -------------------- */
        /* PRODUCTION COUNTRIES */
        /* -------------------- */

        ArrayList<TmdbMovieCountry> movieCountries = movieDetails.getProduction_countries();
        if (movieCountries != null && movieCountries.size() > 0) {
            infoSectionSet = true;

            // Section title.
            String productionCountriesTitle =
                    getResources().getQuantityString(R.plurals.production_countries,
                            movieCountries.size());
            productionCountriesHeaderTextView.setText(productionCountriesTitle);
            productionCountriesHeaderTextView.setVisibility(View.VISIBLE);

            // Section content.
            stringBuilder = new StringBuilder();
            for (int n = 0; n < movieCountries.size(); n++) {
                String countryName =
                        LocaleUtils.getCountryName(movieCountries.get(n).getIso_3166_1());
                stringBuilder.append(countryName);
                if ((n + 1) < movieCountries.size())
                    stringBuilder.append("<br>");
            }
            String htmlText = stringBuilder.toString();
            TextViewUtils.setHtmlText(productionCountriesContentTextView, htmlText);
            productionCountriesContentTextView.setVisibility(View.VISIBLE);
        } else {
            productionCountriesHeaderTextView.setVisibility(View.GONE);
            productionCountriesContentTextView.setVisibility(View.GONE);
        }

        /* ------ */
        /* BUDGET */
        /* ------ */

        int budget = movieDetails.getBudget();
        if (budget > 0) {
            infoSectionSet = true;
            budgetHeaderTextView.setVisibility(View.VISIBLE);

            // Budget amounts in USD.
            String usdBudget = LocaleUtils.getCurrencyFormat(budget, "USD");
            budgetContentTextView.setText(usdBudget);
            budgetContentTextView.setVisibility(View.VISIBLE);
        } else {
            budgetHeaderTextView.setVisibility(View.GONE);
            budgetContentTextView.setVisibility(View.GONE);
        }

        /* ------- */
        /* REVENUE */
        /* ------- */

        int revenue = movieDetails.getRevenue();
        if (revenue > 0) {
            infoSectionSet = true;
            revenueHeaderTextView.setVisibility(View.VISIBLE);

            // Revenue amounts in USD.
            String usdRevenue = LocaleUtils.getCurrencyFormat(revenue, "USD");
            revenueContentTextView.setText(usdRevenue);
            revenueContentTextView.setVisibility(View.VISIBLE);
        } else {
            revenueHeaderTextView.setVisibility(View.GONE);
            revenueContentTextView.setVisibility(View.GONE);
        }

        return infoSectionSet;
    }

    /**
     * Set collection section.
     *
     * @return true if the movieDetails belongs to a collection, false otherwise.
     */
    private boolean setCollectionSection() {
        if (movieDetails.getBelongs_to_collection() != null) {
            // Retrieve the list of movies_menu that belong to the collection, and return.
            new CollectionMoviesList(movieDetails.getBelongs_to_collection().getId());
            return true;
        } else
            return false;
    }

    /**
     * Set external links section.
     *
     * @return true if any of the elements of this section has been set, false otherwise.
     */
    private boolean setLinksSection() {
        final Float NO_LINK_ALPHA = 0.38f;
        boolean infoSectionSet = false;

        // Set background color for this section.
        linksLayout.setBackgroundColor(
                getContext().getResources().getColor(R.color.colorPrimaryLighter));

        // Set homepage. If there is no homepage, make this section transparent.
        final String homepage = movieDetails.getHomepage();
        if (homepage != null && !homepage.equals("") && !homepage.isEmpty()) {
            // Implicit intent to open the homepage.
            homepageLinkLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(homepage);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);

                    // Animate view when clicked and navigate to next activity.
                    AnimatedViewsUtils.animateOnClick(getActivity(), v);
                    startActivity(intent, null);
                }
            });
            infoSectionSet = true;
        } else
            homepageLinkLinearLayout.setAlpha(NO_LINK_ALPHA);

        // Set IMDB link. If there is no IMDB link, make this section transparent.
        final String imdb = movieDetails.getImdb_id();
        if (setExternalLink(imdb, imdbLinkLinearLayout, Imdb.IMDB_BASE_URL))
            infoSectionSet = true;
        else
            imdbLinkLinearLayout.setAlpha(NO_LINK_ALPHA);

        // Set Facebook link. If there is no Facebook link, make this section transparent.
        final String facebook = movieDetails.getExternalIds().getFacebook_id();
        if (setExternalLink(facebook, facebookLinkLinearLayout, Facebook.FACEBOOK_BASE_URL))
            infoSectionSet = true;
        else
            facebookLinkLinearLayout.setAlpha(NO_LINK_ALPHA);

        // Set Instagram link. If there is no Instagram link, make this section transparent.
        final String instagram = movieDetails.getExternalIds().getInstagram_id();
        if (setExternalLink(instagram, instagramLinkLinearLayout, Instagram.INSTAGRAM_BASE_URL))
            infoSectionSet = true;
        else
            instagramLinkLinearLayout.setAlpha(NO_LINK_ALPHA);

        // Set Twitter link. If there is no Twitter link, make this section transparent.
        final String twitter = movieDetails.getExternalIds().getTwitter_id();
        if (setExternalLink(twitter, twitterLinkLinearLayout, Twitter.TWITTER_BASE_URL))
            infoSectionSet = true;
        else
            twitterLinkLinearLayout.setAlpha(NO_LINK_ALPHA);

        return infoSectionSet;
    }

    /**
     * Set recommended movies_menu section.
     *
     * @return true if any of the elements of this section has been set, false otherwise.
     */
    private boolean setRecommendedMoviesSection() {
        boolean infoSectionSet = false;

        ArrayList<TmdbMovie> recommendedMovies = movieDetails.getRecommendedMovies();
        if (recommendedMovies != null && recommendedMovies.size() > 0) {
            // Set adapter to show the first page of results.
            infoSectionSet = true;
            recommendedMoviesAdapter.updateMoviesArrayList(recommendedMovies, true);
            recommendedMoviesAdapter.notifyDataSetChanged();

            // Show/hide "view all" texts.
            int totalResults = recommendedMovies.get(0).getTotal_results();
            if (totalResults > Tmdb.TMDB_RESULTS_PER_PAGE) {
                String viewAllText = getString(R.string.view_all_recommended_movies, totalResults);
                recommendedMoviesViewAllActionTextView.setText(viewAllText);
                recommendedMoviesViewAllCardView.setVisibility(View.VISIBLE);
                recommendedMoviesViewAllTextView.setVisibility(View.VISIBLE);

                // Set the onClickMoviesListener for click events in the "view all" elements.
                View.OnClickListener onClickMoviesListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(), "View all clicked", Toast.LENGTH_SHORT).show();
                    }
                };
                recommendedMoviesViewAllCardView.setOnClickListener(onClickMoviesListener);
                recommendedMoviesViewAllTextView.setOnClickListener(onClickMoviesListener);
            } else {
                recommendedMoviesViewAllCardView.setVisibility(View.GONE);
                recommendedMoviesViewAllTextView.setVisibility(View.GONE);
            }
        }

        return infoSectionSet;
    }

    /**
     * Set movie keywords section.
     *
     * @return true if any of the elements of this section has been set, false otherwise.
     */
    @SuppressWarnings("ConstantConditions")
    private boolean setKeywordsSection() {
        ArrayList<TmdbKeyword> keywordsArrayList = movieDetails.getKeywords();

        if (keywordsArrayList != null && keywordsArrayList.size() > 0) {
            keywordsFlowLayout.removeAllViews();
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            for (int n = 0; n < keywordsArrayList.size(); n++) {
                // Create the current keyword element into the FlowLayout.
                try {
                    final ViewGroup nullParent = null;
                    View view = inflater.inflate(R.layout.list_item_flowlayout, nullParent);
                    final TextView keywordTextView = (TextView) view.findViewById(R.id.flowlayout_textview);
                    keywordTextView.setText(keywordsArrayList.get(n).getName());
                    keywordTextView.setId(keywordsArrayList.get(n).getId());
                    keywordsFlowLayout.addView(view);

                    // Set a listener for managing click events on keywords.
                    keywordTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getContext(), MoviesListActivity.class);
                            intent.putExtra(MoviesListActivity.PARAM_KEYWORD_ID,
                                    keywordTextView.getId());
                            intent.putExtra(MoviesListActivity.PARAM_KEYWORD_NAME,
                                    keywordTextView.getText());

                            Bundle option = ActivityOptions
                                    .makeSceneTransitionAnimation(getActivity()).toBundle();

                            // Animate view when clicked ang navigate to next activity.
                            AnimatedViewsUtils.animateOnClick(getActivity(), view);
                            startActivity(intent, option);
                        }
                    });
                } catch (NullPointerException e) {
                    Log.e(TAG, "(setKeywordsSection) Error inflating view for " + keywordsArrayList.get(n).getName());
                }

                // Set section title.
                String keywordsTitle =
                        getResources().getQuantityString(R.plurals.keywords, keywordsArrayList.size());
                keywordsTitleTextView.setText(keywordsTitle);
            }
            return true;
        } else
            return false;
    }

    /**
     * Helper method to set an OnClickListener on a TextView with a Drawable for opening an external
     * link.
     *
     * @param externalId   is the unique identifier to append to the url.
     * @param linearLayout is the LinearLayout that will be set with the OnClickListener.
     * @param url          is the base url of the external link.
     * @return true if the externalId is not empty, false otherwise.
     */
    private boolean setExternalLink(final String externalId, LinearLayout linearLayout,
                                    final String url) {
        if (externalId != null && !externalId.equals("") && !externalId.isEmpty()) {
            // Implicit intent to open the link.
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri builtUri = Uri.parse(url).buildUpon()
                            .appendPath(externalId)
                            .build();
                    Intent intent = new Intent(Intent.ACTION_VIEW, builtUri);

                    // Animate view when clicked and navigate to next activity.
                    AnimatedViewsUtils.animateOnClick(getActivity(), v);
                    startActivity(intent, null);
                }
            });
            return true;
        }
        return false;
    }

    /* ------------- */
    /* INNER CLASSES */
    /* ------------- */

    // Private inner class to retrieve the list of movies_menu of a given collection.
    private class CollectionMoviesList implements LoaderManager.LoaderCallbacks<TmdbMovieCollection> {
        private final String TAG = MovieDetailsInfoFragment.CollectionMoviesList.class.getSimpleName();
        private int collectionID;

        // Constructor for objects of this class.
        CollectionMoviesList(int collectionID) {
            this.collectionID = collectionID;

            // Create an AsyncTaskLoader for retrieving the list of movies_menu.
            getLoaderManager().initLoader(NetworkUtils.TMDB_COLLECTION_LOADER_ID, null, this);

        }

        /* ------ */
        /* LOADER */
        /* ------ */

        /**
         * Instantiate and return a new Loader for the given ID.
         *
         * @param id   The ID whose loaderId is to be created.
         * @param args Any arguments supplied by the caller.
         * @return Return a new Loader instance that is ready to start loading.
         */
        @Override
        public Loader<TmdbMovieCollection> onCreateLoader(int id, Bundle args) {
            if (NetworkUtils.isConnected(getContext())) {
                // There is an available connection. Fetch results from TMDB.
                return new TmdbMoviesCollectionAsyncTaskLoader(getContext(), collectionID,
                        Locale.getDefault().getLanguage(),
                        Locale.getDefault().getCountry());
            } else {
                // There is no connection. Show error message.
                Log.i(TAG, "(onCreateLoader) No internet connection.");
                return null;
            }
        }

        /**
         * Called when a previously created loaderId has finished its load.  Note
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
         * management of its data so you don't have to.
         *
         * @param loader The Loader that has finished.
         * @param data   The data generated by the Loader.
         */
        @Override
        public void onLoadFinished(Loader<TmdbMovieCollection> loader, TmdbMovieCollection data) {
            // Check if there is an available connection.
            if (NetworkUtils.isConnected(getContext())) {
                // If there is a valid result, then update its data into the current
                // {@link TmdbMovieDetails} object.
                if (data != null) {
                    Log.i(TAG, "(onLoadFinished) Search results not null.");

                    // Set collection name.
                    String name = data.getName();
                    if (name == null || name.equals("") || name.isEmpty())
                        name = getString(R.string.no_title);
                    collectionNameTextView.setText(name);

                    // Set collection overview.
                    String overview = data.getOverview();
                    if (overview != null && !overview.equals("") && !overview.isEmpty())
                        collectionOverviewTextView.setText(overview);
                    else
                        collectionOverviewTextView.setVisibility(View.GONE);

                    // Order the movies_menu list by release date and set adapter to show it.
                    ArrayList<TmdbMovie> movieCollection = data.getParts();
                    Collections.sort(movieCollection, new Comparator<TmdbMovie>() {
                        @Override
                        public int compare(TmdbMovie tmdbMovie2, TmdbMovie tmdbMovie1) {
                            return tmdbMovie2.getRelease_date().compareTo(tmdbMovie1.getRelease_date());
                        }
                    });
                    if (movieCollection.size() > 0) {
                        collectionAdapter.updateMoviesArrayList(movieCollection, true);
                        collectionAdapter.notifyDataSetChanged();
                    }

                    // Set overview, if it exists.
                } else {
                    Log.i(TAG, "(onLoadFinished) No search results.");
                }
            } else {
                // There is no connection. Show error message.
                Log.i(TAG, "(onLoadFinished) No connection to internet.");
            }
        }

        /**
         * Called when a previously created loaderId is being reset, and thus
         * making its data unavailable.  The application should at this point
         * remove any references it has to the Loader's data.
         *
         * @param loader The Loader that is being reset.
         */
        @Override
        public void onLoaderReset(Loader<TmdbMovieCollection> loader) {
        }
    }

    /**
     * Inner class for fetching info from OMDB API.
     */
    class OMDBinfo implements LoaderManager.LoaderCallbacks<OmdbMovie> {
        private final String TAG = OMDBinfo.class.getSimpleName();
        private OmdbMovie omdbMovie;
        private String imdbId;

        OMDBinfo(String imdbId) {
            this.imdbId = imdbId;
            // Create an AsyncTaskLoader for retrieving additional movieDetails information from OMDB in a
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
         * management of its data so you don't have to.
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

                    // Set age rating to US rating from OMDB, if it has not been previously set.
                    if (setAgeRating())
                        ageRatingContentTextView.setVisibility(View.VISIBLE);
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
            boolean imdbRating = ScoreUtils.setDonutProgressRating(getContext(),
                    String.valueOf(omdbMovie.getImdb_vote_average()),
                    imdbDonutProgress);
            boolean rottenTomatoesRating = ScoreUtils.setDonutProgressRating(getContext(),
                    String.valueOf(omdbMovie.getRt_vote_average()),
                    rottenTomatoesDonutProgress);
            boolean metacriticRating = ScoreUtils.setDonutProgressRating(getContext(),
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

        /**
         * Sets age rating to the US values if the age rating has not been previously set with the
         * locale values.
         *
         * @return true if the age rating has been set here, false otherwise.
         */
        private boolean setAgeRating() {
            if (ageRating.equals("")) {
                Log.i(TAG, "(setAgeRating) The age rating has not been previously set");
                String omdbAgeRating = omdbMovie.getRated();
                if (omdbAgeRating != null && !omdbAgeRating.equals("") && !omdbAgeRating.isEmpty()) {
                    Log.i(TAG, "(setAgeRating) US age rating from OMDB:" + omdbAgeRating);

                    // Age rating title.
                    ageRatingHeaderTextView.setVisibility(View.VISIBLE);

                    // Age rating content.
                    String ageRatingContent = omdbAgeRating +
                            " (" + LocaleUtils.getUSCountryName() + ")";
                    ageRatingContentTextView.setText(ageRatingContent);
                    ageRatingContentTextView.setVisibility(View.VISIBLE);
                    return true;
                } else
                    Log.i(TAG, "(setAgeRating) No US age rating from OMDB");
            } else
                Log.i(TAG, "(setAgeRating) The age rating was previously set");
            return false;
        }
    }
}