package com.example.android.popularmoviesstage2.fragments;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.activities.MovieDetailsActivity;
import com.example.android.popularmoviesstage2.adapters.MoviesShortListAdapter;
import com.example.android.popularmoviesstage2.asynctaskloaders.OmdbMovieAsyncTaskLoader;
import com.example.android.popularmoviesstage2.asynctaskloaders.TmdbMovieDetailsAsyncTaskLoader;
import com.example.android.popularmoviesstage2.classes.Facebook;
import com.example.android.popularmoviesstage2.classes.FlowLayout;
import com.example.android.popularmoviesstage2.classes.Imdb;
import com.example.android.popularmoviesstage2.classes.Instagram;
import com.example.android.popularmoviesstage2.classes.OmdbMovie;
import com.example.android.popularmoviesstage2.classes.Tmdb;
import com.example.android.popularmoviesstage2.classes.TmdbKeyword;
import com.example.android.popularmoviesstage2.classes.TmdbMovie;
import com.example.android.popularmoviesstage2.classes.TmdbMovieCompany;
import com.example.android.popularmoviesstage2.classes.TmdbMovieCountry;
import com.example.android.popularmoviesstage2.classes.TmdbMovieDetails;
import com.example.android.popularmoviesstage2.classes.TmdbMovieGenre;
import com.example.android.popularmoviesstage2.classes.TmdbMovieLanguage;
import com.example.android.popularmoviesstage2.classes.TmdbRelease;
import com.example.android.popularmoviesstage2.classes.Twitter;
import com.example.android.popularmoviesstage2.utils.DateTimeUtils;
import com.example.android.popularmoviesstage2.utils.LocaleUtils;
import com.example.android.popularmoviesstage2.utils.NetworkUtils;
import com.example.android.popularmoviesstage2.utils.ScoreUtils;
import com.example.android.popularmoviesstage2.utils.TextViewUtils;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.support.v4.content.ContextCompat.getDrawable;

/**
 * {@link Fragment} that displays the main information about the current movieDetails.
 */
public class MovieDetailsInfoFragment extends Fragment implements LoaderManager.LoaderCallbacks<TmdbMovieDetails> {
    private static final String TAG = MovieDetailsInfoFragment.class.getSimpleName();

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
    @BindView(R.id.info_genres_title)
    TextView genresTextView;

    @BindView(R.id.info_main_layout)
    LinearLayout mainLinearLayout;
    @BindView(R.id.info_movie_runtime)
    TextView runtimeTextView;
    @BindView(R.id.info_movie_release_date)
    TextView releaseDateTextView;
    @BindView(R.id.info_movie_age_rating)
    TextView ageRatingTextView;
    @BindView(R.id.info_movie_original_language)
    TextView originalLanguageTextView;
    @BindView(R.id.info_movie_production_countries)
    TextView productionCountriesTextView;
    @BindView(R.id.info_movie_original_title)
    TextView originalTitleTextView;
    @BindView(R.id.info_movie_production_companies)
    TextView productionCompaniesTextView;
    @BindView(R.id.info_movie_budget)
    TextView budgetTextView;
    @BindView(R.id.info_movie_revenue)
    TextView revenueTextView;

    @BindView(R.id.info_collection_layout)
    RelativeLayout collectionLayout;
    @BindView(R.id.info_collection_background)
    ImageView collectionBackgroundImageView;
    @BindView(R.id.info_collection_poster)
    ImageView collectionPosterImageView;
    @BindView(R.id.info_collection_name)
    TextView collectionNameTextView;

    @BindView(R.id.info_keywords_layout)
    LinearLayout keywordsLayout;
    @BindView(R.id.info_keywords_title)
    TextView keywordsTitleTextView;
    @BindView(R.id.info_keywords_flowlayout)
    FlowLayout keywordsFlowLayout;

    @BindView(R.id.info_links_layout)
    LinearLayout linksLayout;
    @BindView(R.id.info_links_homepage)
    TextView homepageTextView;
    @BindView(R.id.info_links_imdb)
    TextView imdbTextView;
    @BindView(R.id.info_links_twitter)
    TextView twitterTextView;
    @BindView(R.id.info_links_facebook)
    TextView facebookTextView;
    @BindView(R.id.info_links_instagram)
    TextView instagramTextView;

    @BindView(R.id.recommended_movies_layout)
    LinearLayout recommendedMoviesLayout;
    @BindView(R.id.recommended_movies_view_all)
    TextView recommendedMoviesViewAllTextView;
    @BindView(R.id.recommended_movies_recyclerview)
    RecyclerView recommendedMoviesRecyclerView;

    private static TmdbMovieDetails movieDetails;
    private int movieId;
    private MoviesShortListAdapter recommendedMoviesAdapter;
    String ageRating = "";
    private Unbinder unbinder;

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
        unbinder = ButterKnife.bind(this, rootView);

        // Clean all elements in the layout when creating this fragment.
        clearLayout();

        // Set recycler views for recommended movies.
        setRecyclerView();

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
    public void onLoadFinished(Loader<TmdbMovieDetails> loader, TmdbMovieDetails data) {
        // Hide progress bar.
        progressBar.setVisibility(View.INVISIBLE);
        noResultsTextView.setVisibility(View.INVISIBLE);

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
        mainLinearLayout.setVisibility(View.GONE);
        collectionLayout.setVisibility(View.GONE);
        linksLayout.setVisibility(View.GONE);
        recommendedMoviesLayout.setVisibility(View.GONE);
        keywordsLayout.setVisibility(View.GONE);
    }

    /**
     * Helper method for setting the RecyclerView in order to display lists of recommended movies
     * with a horizontal arrangement.
     */
    void setRecyclerView() {
        // Set the LayoutManager for the RecyclerView.
        recommendedMoviesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        recommendedMoviesRecyclerView.setHasFixedSize(true);

        // Set the listeners for click events in the adapter.
        MoviesShortListAdapter.OnItemClickListener recommendedMovieListener = new MoviesShortListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TmdbMovie movie, View clickedView) {
                // Start MovieDetailsActivity to show movie details when the current element is
                // clicked. We need to know when the other activity finishes, so we use
                // startActivityForResult. No need a requestCode, we don't care for any result.
                Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
                intent.putExtra(MovieDetailsActivity.EXTRA_PARAM_MOVIE, movie);
                startActivity(intent);
            }
        };

        // Set the Adapter for the RecyclerView.
        recommendedMoviesAdapter = new MoviesShortListAdapter(R.layout.list_item_poster_horizontal_layout_1,
                new ArrayList<TmdbMovie>(), recommendedMovieListener);
        recommendedMoviesRecyclerView.setAdapter(recommendedMoviesAdapter);
    }

    /**
     * Helper method to display all the movieDetails information in this fragment.
     */
    void setMovieInfo() {
        // Set overview section.
        if (setOverviewSection())
            overviewLinearLayout.setVisibility(View.VISIBLE);

        // Set main info section.
        if (setMainInfoSection())
            mainLinearLayout.setVisibility(View.VISIBLE);

        // Set collection section.
        if (setCollectionSection())
            collectionLayout.setVisibility(View.VISIBLE);

        // Set links section.
        if (setLinksSection())
            linksLayout.setVisibility(View.VISIBLE);

        // Set keywords section.
        if (setKeywordsSection())
            keywordsLayout.setVisibility(View.VISIBLE);

        // Recommended movies section.
        if (setRecommendedMoviesSection())
            recommendedMoviesLayout.setVisibility(View.VISIBLE);
    }

    /**
     * Set overview section.
     *
     * @return true if any of the elements of this section has been set, false otherwise.
     */
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

        ArrayList<TmdbMovieGenre> movieGenres = movieDetails.getMovie().getGenres();
        if (movieGenres != null && movieGenres.size() > 0) {
            infoSectionSet = true;

            // Set section title.
            String genresTitle = getResources().getQuantityString(R.plurals.genres, movieGenres.size());
            genresTextView.setText(genresTitle);

            // Manage genres array.
            genresFlowLayout.removeAllViews();
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            for (int n = 0; n < movieGenres.size(); n++) {
                // Create the genre element into the FlowLayout.
                try {
                    View view = inflater.inflate(R.layout.list_item_flowlayout, null);
                    final TextView genreTextView = (TextView) view.findViewById(R.id.flowlayout_textview);
                    genreTextView.setText(movieGenres.get(n).getName());
                    genresFlowLayout.addView(view);

                    // Set a listener for managing click events on genres.
                    genreTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            view.setElevation(0);
                            Toast.makeText(getContext(), "Genre clicked", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (java.lang.NullPointerException e) {
                    Log.e(TAG, "(setMainInfoSection) Error inflating view for " + movieGenres.get(n).getName());
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
        String color = String.format("%X",
                getResources().getColor(R.color.colorDarkWhite)).substring(2);
        String currentCountry = LocaleUtils.getCurrentCountryName();
        StringBuilder stringBuilder = new StringBuilder();

        /* ------- */
        /* RUNTIME */
        /* ------- */

        String runtime = DateTimeUtils.getHoursAndMinutes(getContext(), movieDetails.getRuntime());
        if (runtime != null && !runtime.equals("") && !runtime.isEmpty()) {
            infoSectionSet = true;
            String htmlText = "<strong>" + getString(R.string.runtime).toUpperCase() + "</strong><br>" +
                    "<font color=\"#" + color + "\">" + runtime + "</font>";
            TextViewUtils.setHtmlText(runtimeTextView, htmlText);
            runtimeTextView.setVisibility(View.VISIBLE);
        } else {
            runtimeTextView.setVisibility(View.GONE);
        }

        /* ------------- */
        /* RELEASE DATES */
        /* ------------- */

        String releaseDatesTitle = "";
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
            stringBuilder = new StringBuilder();
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
                // Set section title.
                releaseDatesTitle = getResources().getQuantityString(R.plurals.release_dates, 1);

                // Official release date.
                releaseDatesContent = DateTimeUtils.getStringDate(releaseDate,
                        DateTimeUtils.DATE_FORMAT_LONG);
            }
        }

        // Show this section only if there is an information about release dates.
        if (!releaseDatesContent.equals("")) {
            infoSectionSet = true;
            releaseDatesContent = "<strong>" + releaseDatesTitle.toUpperCase() + "</strong><br>" +
                    "<font color=\"#" + color + "\">" + releaseDatesContent + "</font>";
            TextViewUtils.setHtmlText(releaseDateTextView, releaseDatesContent);
            releaseDateTextView.setVisibility(View.VISIBLE);
        } else
            releaseDateTextView.setVisibility(View.GONE);

        /* ---------- */
        /* AGE RATING */
        /* ---------- */

        // We obtained the age rating previously, if it exists, from the release dates information.
        if (!ageRating.equals("")) {
            String ageRatingContent = getString(R.string.age_rating_title);
            ageRatingContent = "<strong>" + ageRatingContent.toUpperCase() + "</strong><br>" +
                    "<font color=\"#" + color + "\">" + ageRating + " (" + currentCountry +
                    ")</font>";
            TextViewUtils.setHtmlText(ageRatingTextView, ageRatingContent);
            ageRatingTextView.setVisibility(View.VISIBLE);
        } else
            ageRatingTextView.setVisibility(View.GONE);

        /* --------- */
        /* LANGUAGES */
        /* --------- */

        String originalLanguage = movieDetails.getMovie().getOriginal_language();
        if (originalLanguage != null && !originalLanguage.equals("") && !originalLanguage.isEmpty()) {
            infoSectionSet = true;
            String languageName = LocaleUtils.getLanguageName(originalLanguage);
            String htmlText = "<strong>" + getString(R.string.original_language).toUpperCase() +
                    "</strong><br><font color=\"#" + color + "\">" +
                    languageName.substring(0, 1).toUpperCase() + languageName.substring(1) +
                    "</font>";

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
                htmlText = htmlText + "<br><font color=\"#" + color + "\">" +
                        getString(R.string.spoken_languages) + ": " + stringBuilder + "</font>";
            }

            TextViewUtils.setHtmlText(originalLanguageTextView, htmlText);
            originalLanguageTextView.setVisibility(View.VISIBLE);
        } else
            originalLanguageTextView.setVisibility(View.GONE);

        /* -------------- */
        /* ORIGINAL TITLE */
        /* -------------- */

        String originalTitle = movieDetails.getMovie().getOriginal_title();
        if (originalTitle != null && !originalTitle.equals("") && !originalTitle.isEmpty()) {
            infoSectionSet = true;
            String htmlText = "<strong>" + getString(R.string.original_title).toUpperCase() +
                    "</strong><br><font color=\"#" + color + "\">" + originalTitle + "</font>";
            TextViewUtils.setHtmlText(originalTitleTextView, htmlText);
            originalTitleTextView.setVisibility(View.VISIBLE);
        } else
            originalTitleTextView.setVisibility(View.GONE);

        /* -------------------- */
        /* PRODUCTION COMPANIES */
        /* -------------------- */

        ArrayList<TmdbMovieCompany> movieCompanies = movieDetails.getProduction_companies();
        if (movieCompanies != null && movieCompanies.size() > 0) {
            infoSectionSet = true;
            stringBuilder = new StringBuilder();
            for (int n = 0; n < movieCompanies.size(); n++) {
                stringBuilder.append(movieCompanies.get(n).getName());
                if ((n + 1) < movieCompanies.size())
                    stringBuilder.append("<br>");
            }
            String htmlText = "<strong>" +
                    getResources().getQuantityString(R.plurals.production_companies,
                            movieCompanies.size()).toUpperCase() +
                    "</strong><br><font color=\"#" + color + "\">" + stringBuilder + "</font>";
            TextViewUtils.setHtmlText(productionCompaniesTextView, htmlText);
            productionCompaniesTextView.setVisibility(View.VISIBLE);
        } else
            productionCompaniesTextView.setVisibility(View.GONE);

        /* -------------------- */
        /* PRODUCTION COUNTRIES */
        /* -------------------- */

        ArrayList<TmdbMovieCountry> movieCountries = movieDetails.getProduction_countries();
        if (movieCountries != null && movieCountries.size() > 0) {
            infoSectionSet = true;
            stringBuilder = new StringBuilder();
            for (int n = 0; n < movieCountries.size(); n++) {
                String countryName =
                        LocaleUtils.getCountryName(movieCountries.get(n).getIso_3166_1());
                stringBuilder.append(countryName);
                if ((n + 1) < movieCountries.size())
                    stringBuilder.append("<br>");
            }
            String htmlText = "<strong>" +
                    getResources().getQuantityString(R.plurals.production_countries,
                            movieCountries.size()).toUpperCase() +
                    "</strong><br><font color=\"#" + color + "\">" + stringBuilder + "</font>";
            TextViewUtils.setHtmlText(productionCountriesTextView, htmlText);
            productionCountriesTextView.setVisibility(View.VISIBLE);
        } else
            productionCountriesTextView.setVisibility(View.GONE);

        /* ------ */
        /* BUDGET */
        /* ------ */

        int budget = movieDetails.getBudget();
        if (budget > 0) {
            infoSectionSet = true;

            // Budget amounts in USD.
            String usdBudget = LocaleUtils.getCurrencyFormat(budget, "USD");
            String htmlText = "<strong>" + getString(R.string.budget_title).toUpperCase() +
                    "</strong><br><font color=\"#" + color + "\">" + usdBudget + "</font>";
            TextViewUtils.setHtmlText(budgetTextView, htmlText);
            budgetTextView.setVisibility(View.VISIBLE);
        } else
            budgetTextView.setVisibility(View.GONE);

        /* ------- */
        /* REVENUE */
        /* ------- */

        int revenue = movieDetails.getRevenue();
        if (revenue > 0) {
            infoSectionSet = true;

            // Revenue amounts in USD.
            String usdRevenue = LocaleUtils.getCurrencyFormat(revenue, "USD");
            String htmlText = "<strong>" + getString(R.string.revenue_title).toUpperCase() + "</strong><br>" +
                    "<font color=\"#" + color + "\">" + usdRevenue + "</font>";
            TextViewUtils.setHtmlText(revenueTextView, htmlText);
            revenueTextView.setVisibility(View.VISIBLE);
        } else
            revenueTextView.setVisibility(View.GONE);

        return infoSectionSet;
    }

    /**
     * Set collection section.
     *
     * @return true if the movieDetails belongs to a collection, false otherwise.
     */
    private boolean setCollectionSection() {
        if (movieDetails.getBelongs_to_collection() != null) {
            // Set poster, if it exists.
            String posterPath = movieDetails.getBelongs_to_collection().getPoster_path();
            if (posterPath != null && !posterPath.equals("") && !posterPath.isEmpty()) {
                posterPath = Tmdb.TMDB_POSTER_SIZE_W185_URL + posterPath;
                Picasso.with(getContext())
                        .load(posterPath)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(collectionPosterImageView);
            } else
                // No image. Show default image.
                collectionPosterImageView.setImageDrawable(getDrawable(getContext(),
                        R.drawable.default_poster));

            // Set background image, if it exists.
            String backdropPath = movieDetails.getBelongs_to_collection().getBackdrop_path();
            if (backdropPath != null && !backdropPath.equals("") && !backdropPath.isEmpty()) {
                backdropPath = Tmdb.TMDB_POSTER_SIZE_W500_URL + backdropPath;
                Picasso.with(getContext())
                        .load(backdropPath)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(collectionBackgroundImageView);
            }

            // Set collection title.
            String name = movieDetails.getBelongs_to_collection().getName();
            if (name != null && !name.equals("") && !name.isEmpty())
                collectionNameTextView.setText(name);
            else
                collectionNameTextView.setText(getResources().getString(R.string.no_title));

            // TODO: setOnClickListener for opening the collection into another activity.

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
        final Float LINK_ALPHA = 0.4f;
        boolean infoSectionSet = false;

        // Set background color for this section.
        linksLayout.setBackgroundColor(getContext().getResources().getColor(R.color.colorAccent));

        // Set homepage. If there is no homepage, make this section transparent.
        final String homepage = movieDetails.getHomepage();
        if (homepage != null && !homepage.equals("") && !homepage.isEmpty()) {
            // Implicit intent to open the homepage.
            homepageTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(homepage);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
            infoSectionSet = true;
        } else
            homepageTextView.setAlpha(LINK_ALPHA);

        // Set IMDB link. If there is no IMDB link, make this section transparent.
        final String imdb = movieDetails.getImdb_id();
        if (setExternalLink(imdb, imdbTextView, Imdb.IMDB_BASE_URL))
            infoSectionSet = true;
        else
            imdbTextView.setAlpha(LINK_ALPHA);

        // Set Facebook link. If there is no Facebook link, make this section transparent.
        final String facebook = movieDetails.getExternalIds().getFacebook_id();
        if (setExternalLink(facebook, facebookTextView, Facebook.FACEBOOK_BASE_URL))
            infoSectionSet = true;
        else
            facebookTextView.setAlpha(LINK_ALPHA);

        // Set Instagram link. If there is no Instagram link, make this section transparent.
        final String instagram = movieDetails.getExternalIds().getInstagram_id();
        if (setExternalLink(instagram, instagramTextView, Instagram.INSTAGRAM_BASE_URL))
            infoSectionSet = true;
        else
            instagramTextView.setAlpha(LINK_ALPHA);

        // Set Twitter link. If there is no Twitter link, make this section transparent.
        final String twitter = movieDetails.getExternalIds().getTwitter_id();
        if (setExternalLink(twitter, twitterTextView, Twitter.TWITTER_BASE_URL))
            infoSectionSet = true;
        else
            twitterTextView.setAlpha(LINK_ALPHA);

        return infoSectionSet;
    }

    /**
     * Set recommended movies section.
     *
     * @return true if any of the elements of this section has been set, false otherwise.
     */
    private boolean setRecommendedMoviesSection() {
        boolean infoSectionSet = false;

        ArrayList<TmdbMovie> recommendedMovies = movieDetails.getRecommendedMovies();
        if (recommendedMovies != null && recommendedMovies.size() > 0) {
            infoSectionSet = true;
            recommendedMoviesAdapter.setMoviesArrayList(recommendedMovies);
            recommendedMoviesAdapter.notifyDataSetChanged();
        }

        return infoSectionSet;
    }

    /**
     * Set movie keywords section.
     *
     * @return true if any of the elements of this section has been set, false otherwise.
     */
    private boolean setKeywordsSection() {
        ArrayList<TmdbKeyword> keywordsArrayList = movieDetails.getKeywords();

        if (keywordsArrayList != null && keywordsArrayList.size() > 0) {
            keywordsFlowLayout.removeAllViews();
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            for (int n = 0; n < keywordsArrayList.size(); n++) {
                // Create the current keyword element into the FlowLayout.
                try {
                    View view = inflater.inflate(R.layout.list_item_flowlayout, null);
                    final TextView keywordTextView = (TextView) view.findViewById(R.id.flowlayout_textview);
                    keywordTextView.setText(keywordsArrayList.get(n).getName());
                    keywordsFlowLayout.addView(view);

                    // Set a listener for managing click events on keywords.
                    keywordTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getContext(), "Keyword clicked", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (NullPointerException e) {
                    Log.e(TAG, "(setKeywordsSection) Error inflating view for " + keywordsArrayList.get(n).getName());
                }
            }
            return true;
        } else
            return false;
    }

    /**
     * Helper method to set an OnClickListener on a TextView with a Drawable for opening an external
     * link.
     *
     * @param externalId is the unique identifier to append to the url.
     * @param textView   is the TextView that will be set with the OnClickListener.
     * @param url        is the base url of the external link.
     * @return true if the externalId is not empty, false otherwise.
     */
    private boolean setExternalLink(final String externalId, TextView textView, final String url) {
        if (externalId != null && !externalId.equals("") && !externalId.isEmpty()) {
            // Implicit intent to open the link.
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri builtUri = Uri.parse(url).buildUpon()
                            .appendPath(externalId)
                            .build();
                    Intent intent = new Intent(Intent.ACTION_VIEW, builtUri);
                    startActivity(intent);
                }
            });
            return true;
        }
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

                    // Set age rating to US rating from OMDB, if it has not been previously set.
                    if (setAgeRating())
                        ageRatingTextView.setVisibility(View.VISIBLE);
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
                    String color = String.format("%X",
                            getResources().getColor(R.color.colorDarkWhite)).substring(2);
                    String country = LocaleUtils.getUSCountryName();
                    String ageRatingTitle = getString(R.string.age_rating_title);
                    String ageRatingContent = "<strong>" + ageRatingTitle.toUpperCase() +
                            "</strong><br><font color=\"#" + color + "\">" + omdbAgeRating + " (" +
                            country + ")</font>";
                    TextViewUtils.setHtmlText(ageRatingTextView, ageRatingContent);
                    return true;
                } else
                    Log.i(TAG, "(setAgeRating) No US age rating from OMDB");
            } else
                Log.i(TAG, "(setAgeRating) The age rating was previously set");
            return false;
        }
    }
}