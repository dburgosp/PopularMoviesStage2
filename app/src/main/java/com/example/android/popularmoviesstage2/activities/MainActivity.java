package com.example.android.popularmoviesstage2.activities;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.asynctaskloaders.TmdbMoviesAsyncTaskLoader;
import com.example.android.popularmoviesstage2.asynctaskloaders.TmdbPeopleAsyncTaskLoader;
import com.example.android.popularmoviesstage2.classes.Tmdb;
import com.example.android.popularmoviesstage2.classes.TmdbMovie;
import com.example.android.popularmoviesstage2.classes.TmdbPerson;
import com.example.android.popularmoviesstage2.classes.ViewFlipperIndicator;
import com.example.android.popularmoviesstage2.utils.DateTimeUtils;
import com.example.android.popularmoviesstage2.utils.DisplayUtils;
import com.example.android.popularmoviesstage2.utils.NetworkUtils;
import com.example.android.popularmoviesstage2.utils.ScoreUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    // Annotate fields with @BindView and views ID for Butter Knife to find and automatically cast
    // the corresponding views.
    @BindView(R.id.home_all_movies_cardview)
    CardView allMoviesCardView;
    @BindView(R.id.home_all_series_cardview)
    CardView allSeriesCardView;
    @BindView(R.id.home_all_people_cardview)
    CardView allPeopleCardView;

    @BindView(R.id.home_upcoming_movies_cardview)
    CardView upcomingMoviesCardView;
    @BindView(R.id.home_upcoming_movies_viewflipper)
    ViewFlipperIndicator upcomingMoviesViewFlipper;
    @BindView(R.id.home_upcoming_movies_loading_indicator)
    ProgressBar upcomingMoviesLoadingIndicator;
    @BindView(R.id.home_upcoming_movies_message)
    TextView upcomingMoviesMessage;

    @BindView(R.id.home_popular_on_the_air_linearlayout)
    LinearLayout popularOnTheAirLinearLayout;
    @BindView(R.id.home_popular_people_cardview)
    CardView popularPeopleCardView;
    @BindView(R.id.home_popular_people_viewflipper)
    ViewFlipper popularPeopleViewFlipper;
    @BindView(R.id.home_popular_people_loading_indicator)
    ProgressBar popularPeopleLoadingIndicator;
    @BindView(R.id.home_popular_people_message)
    TextView popularPeopleMessage;
    @BindView(R.id.home_on_the_air_cardview)
    CardView onTheAirCardView;
    @BindView(R.id.home_on_the_air_viewflipper)
    ViewFlipper OnTheAirViewFlipper;
    @BindView(R.id.home_on_the_air_loading_indicator)
    ProgressBar onTheAirLoadingIndicator;
    @BindView(R.id.home_on_the_air_message)
    TextView onTheAirMessage;

    @BindView(R.id.home_now_playing_movies_cardview)
    CardView nowPlayingMoviesCardView;
    @BindView(R.id.home_now_playing_movies_viewflipper)
    ViewFlipperIndicator nowPlayingMoviesViewFlipper;
    @BindView(R.id.home_now_playing_movies_loading_indicator)
    ProgressBar nowPlayingMoviesLoadingIndicator;
    @BindView(R.id.home_now_playing_movies_message)
    TextView nowPlayingMoviesMessage;

    @BindView(R.id.home_buy_and_rent_series_linearlayout)
    LinearLayout buyAndRentLinearLayout;
    @BindView(R.id.home_buy_and_rent_series_cardview)
    CardView buyAndRentSeriesCardView;
    @BindView(R.id.home_buy_and_rent_series_viewflipper)
    ViewFlipper buyAndRentSeriesViewFlipper;
    @BindView(R.id.home_buy_and_rent_series_loading_indicator)
    ProgressBar buyAndRentSeriesLoadingIndicator;
    @BindView(R.id.home_buy_and_rent_series_message)
    TextView buyAndRentSeriesMessage;
    @BindView(R.id.home_buy_and_rent_movies_cardview)
    CardView buyAndRentMoviesCardView;
    @BindView(R.id.home_buy_and_rent_movies_viewflipper)
    ViewFlipper buyAndRentMoviesViewFlipper;
    @BindView(R.id.home_buy_and_rent_movies_loading_indicator)
    ProgressBar buyAndRentMoviesLoadingIndicator;
    @BindView(R.id.home_buy_and_rent_movies_message)
    TextView buyAndRentMoviesMessage;

    @BindView(R.id.home_airing_today_cardview)
    CardView airingTodayCardView;
    @BindView(R.id.home_airing_today_viewflipper)
    ViewFlipper airingTodayViewFlipper;
    @BindView(R.id.home_airing_today_loading_indicator)
    ProgressBar airingTodayLoadingIndicator;
    @BindView(R.id.home_airing_today_message)
    TextView airingTodayMessage;

    @BindView(R.id.connection_status_layout)
    LinearLayout connectionStatusLayout;
    @BindView(R.id.connection_status_image_view)
    ImageView connectionImage;
    @BindView(R.id.connection_status_text)
    TextView connectionStatusText;
    @BindView(R.id.connection_status_loading_indicator)
    ProgressBar connectionStatusLoadingIndicator;

    private Unbinder unbinder;
    private int animatedViewCurrentIndex = 0;
    private ArrayList<View> animatedViews = new ArrayList<>();
    private LinearLayout.LayoutParams backdropLinearLayoutParams;
    private RelativeLayout.LayoutParams backdropRelativeLayoutParams;
    private RelativeLayout.LayoutParams posterLayoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        // Initialise layout.
        clearLayout();
        if (NetworkUtils.isConnected(MainActivity.this)) {
            setSizes();
            setAnimations();
        } else {
            // If there is no internet connection, show message.
            connectionStatusText.setText(getString(R.string.no_connection));
            connectionStatusText.setVisibility(View.VISIBLE);
            connectionImage.setVisibility(View.VISIBLE);
            connectionStatusLoadingIndicator.setVisibility(View.GONE);
        }

        Log.i(TAG, "(onCreate) Activity created");
    }

    /**
     * Called when an item in the navigation menu is selected.
     *
     * @param item The selected item
     * @return true to display the item as the selected item
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    /* -------------- */
    /* HELPER METHODS */
    /* -------------- */

    /**
     * Helper method to initially clear all the sections in the layout.
     */
    private void clearLayout() {
        allMoviesCardView.setVisibility(View.GONE);
        allSeriesCardView.setVisibility(View.GONE);
        allPeopleCardView.setVisibility(View.GONE);

        upcomingMoviesCardView.setVisibility(View.GONE);
        popularOnTheAirLinearLayout.setVisibility(View.GONE);
        nowPlayingMoviesCardView.setVisibility(View.GONE);
        buyAndRentLinearLayout.setVisibility(View.GONE);
        airingTodayCardView.setVisibility(View.GONE);

        connectionStatusLayout.setVisibility(View.GONE);

        upcomingMoviesMessage.setVisibility(View.GONE);
        popularPeopleMessage.setVisibility(View.GONE);
        nowPlayingMoviesMessage.setVisibility(View.GONE);
        onTheAirMessage.setVisibility(View.GONE);
        buyAndRentMoviesMessage.setVisibility(View.GONE);
        buyAndRentSeriesMessage.setVisibility(View.GONE);
        airingTodayMessage.setVisibility(View.GONE);
    }

    /**
     * Helper method for setting width and height for the background images of every section in this
     * activity, according to the display dimensions.
     */
    private void setSizes() {
        final DisplayUtils displayUtils = new DisplayUtils(MainActivity.this);

        // Sizes and layouts for backdrops.
        int backdropWidthPixels = displayUtils.getFullDisplayBackdropWidthPixels() -
                (2 * getResources().getDimensionPixelSize(R.dimen.small_padding));
        int backdropHeightPixels = (backdropWidthPixels * 9 / 16);

        backdropLinearLayoutParams = new LinearLayout.LayoutParams(
                backdropWidthPixels, backdropHeightPixels);
        backdropRelativeLayoutParams = new RelativeLayout.LayoutParams(
                backdropWidthPixels, backdropHeightPixels +
                getResources().getDimensionPixelSize(R.dimen.big_padding));

        nowPlayingMoviesViewFlipper.setLayoutParams(backdropRelativeLayoutParams);
        upcomingMoviesViewFlipper.setLayoutParams(backdropRelativeLayoutParams);
        airingTodayViewFlipper.setLayoutParams(backdropRelativeLayoutParams);

        // Sizes and layouts for posters.
        int posterWidthPixels = (displayUtils.getFullDisplayBackdropWidthPixels() -
                (3 * getResources().getDimensionPixelSize(R.dimen.small_padding))) / 2;
        int posterHeightPixels = posterWidthPixels * 3 / 2;

        posterLayoutParams = new RelativeLayout.LayoutParams(posterWidthPixels, posterHeightPixels);

        popularPeopleViewFlipper.setLayoutParams(posterLayoutParams);
        OnTheAirViewFlipper.setLayoutParams(posterLayoutParams);
        buyAndRentMoviesViewFlipper.setLayoutParams(posterLayoutParams);
        buyAndRentSeriesViewFlipper.setLayoutParams(posterLayoutParams);
    }

    /**
     * Helper method to start animations on initial layouts and to set in and out animations for
     * ViewFlippers.
     */
    private void setAnimations() {
        // Animate initial layouts.
        animatedViews.add(allMoviesCardView);
        animatedViews.add(allSeriesCardView);
        animatedViews.add(allPeopleCardView);
        animatedViews.add(upcomingMoviesCardView);
        animatedViews.add(popularOnTheAirLinearLayout);
        animatedViews.add(nowPlayingMoviesCardView);
        animatedViews.add(buyAndRentLinearLayout);
        animatedViews.add(airingTodayCardView);
        processAnimationQueue(
                AnimationUtils.loadAnimation(this, R.anim.in_from_left), 300);

        // Set animations for ViewFlippers.
        upcomingMoviesViewFlipper.setInAnimation(AnimationUtils.loadAnimation(
                this, R.anim.in_from_right));
        upcomingMoviesViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(
                this, R.anim.out_from_right));
        nowPlayingMoviesViewFlipper.setInAnimation(AnimationUtils.loadAnimation(
                this, R.anim.fade_in));
        nowPlayingMoviesViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(
                this, R.anim.fade_out));
        popularPeopleViewFlipper.setInAnimation(AnimationUtils.loadAnimation(
                this, R.anim.fade_in));
        popularPeopleViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(
                this, R.anim.fade_out));

        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.colorMoviesPrimaryLight));
        upcomingMoviesViewFlipper.setPaintCurrent(paint);
        nowPlayingMoviesViewFlipper.setPaintCurrent(paint);
        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.colorMoviesPrimaryDark));
        upcomingMoviesViewFlipper.setPaintNormal(paint);
        upcomingMoviesViewFlipper.setRadius(5);
        upcomingMoviesViewFlipper.setMargin(5);
        nowPlayingMoviesViewFlipper.setPaintNormal(paint);
        nowPlayingMoviesViewFlipper.setRadius(5);
        nowPlayingMoviesViewFlipper.setMargin(5);
    }

    /**
     * Display animations for every view included in animatedViews array.
     *
     * @param animation   is the Animation to be performed over the current view.
     * @param delayMillis is the delay, in milliseconds, to wait before displaying
     *                    current animation.
     */
    public void processAnimationQueue(final Animation animation, final int delayMillis) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animation.setDuration(250);
                animation.setInterpolator(new LinearOutSlowInInterpolator());

                // Animate current view after delayMillis from start.
                View view = animatedViews.get(animatedViewCurrentIndex);
                view.setVisibility(View.VISIBLE);
                view.startAnimation(animation);

                // Order animation for next view, if there's still another one in the queue.
                animatedViewCurrentIndex++;
                if (animatedViewCurrentIndex < animatedViews.size())
                    processAnimationQueue(animation, delayMillis);
                else {
                    // Instantiate classes for opening new threads to retrieve data from TMDB after
                    // performing the last animation.
                    new MainActivity.MainActivityMoviesList(
                            NetworkUtils.TMDB_NOW_PLAYING_MOVIES_LOADER_ID);
                    new MainActivity.MainActivityMoviesList(
                            NetworkUtils.TMDB_UPCOMING_MOVIES_LOADER_ID);
                    new MainActivity.MainActivityPeopleList(
                            NetworkUtils.TMDB_POPULAR_PEOPLE_LOADER_ID);
                }
            }
        }, delayMillis);
    }

    /* ------------- */
    /* INNER CLASSES */
    /* ------------- */

    // Private inner class to retrieve a given list of movies.
    private class MainActivityMoviesList implements LoaderManager.LoaderCallbacks<ArrayList<TmdbMovie>> {
        private final String TAG = MainActivity.MainActivityMoviesList.class.getSimpleName();

        // Constructor for objects of this class.
        MainActivityMoviesList(int loaderId) {
            // Create an AsyncTaskLoader for retrieving the list of movies.
            if (loaderId >= 0)
                getSupportLoaderManager().initLoader(loaderId, null, this);
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
        public Loader<ArrayList<TmdbMovie>> onCreateLoader(int id, Bundle args) {
            switch (id) {
                case NetworkUtils.TMDB_UPCOMING_MOVIES_LOADER_ID: {
                    upcomingMoviesLoadingIndicator.setVisibility(View.VISIBLE);
                    if (NetworkUtils.isConnected(MainActivity.this)) {
                        // There is an available connection. Fetch results from TMDB.
                        upcomingMoviesMessage.setVisibility(View.GONE);
                        return new TmdbMoviesAsyncTaskLoader(MainActivity.this,
                                Tmdb.TMDB_CONTENT_TYPE_UPCOMING, 1,
                                Locale.getDefault().getLanguage(),
                                Locale.getDefault().getCountry());
                    } else {
                        // There is no connection. Show error message.
                        upcomingMoviesMessage.setText(
                                getResources().getString(R.string.no_connection));
                        upcomingMoviesMessage.setVisibility(View.VISIBLE);
                        upcomingMoviesLoadingIndicator.setVisibility(View.GONE);
                        Log.i(TAG, "(onCreateLoader) No internet connection.");
                        return null;
                    }
                }

                case NetworkUtils.TMDB_NOW_PLAYING_MOVIES_LOADER_ID: {
                    nowPlayingMoviesLoadingIndicator.setVisibility(View.VISIBLE);
                    if (NetworkUtils.isConnected(MainActivity.this)) {
                        // There is an available connection. Fetch results from TMDB.
                        nowPlayingMoviesMessage.setVisibility(View.GONE);
                        return new TmdbMoviesAsyncTaskLoader(MainActivity.this,
                                Tmdb.TMDB_CONTENT_TYPE_NOW_PLAYING, 1,
                                Locale.getDefault().getLanguage(),
                                Locale.getDefault().getCountry());
                    } else {
                        // There is no connection. Show error message.
                        nowPlayingMoviesMessage.setText(
                                getResources().getString(R.string.no_connection));
                        nowPlayingMoviesMessage.setVisibility(View.VISIBLE);
                        nowPlayingMoviesLoadingIndicator.setVisibility(View.GONE);
                        Log.i(TAG, "(onCreateLoader) No internet connection.");
                        return null;
                    }
                }

                default: {
                    Log.e(TAG, "(onCreateLoader) Unexpected loader id: " + id);
                    return null;
                }
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
        public void onLoadFinished(Loader<ArrayList<TmdbMovie>> loader, ArrayList<TmdbMovie> data) {
            // Get movies list and display it, depending on the loader identifier.
            switch (loader.getId()) {
                case NetworkUtils.TMDB_NOW_PLAYING_MOVIES_LOADER_ID: {
                    nowPlayingMoviesLoadingIndicator.setVisibility(View.GONE);

                    // Check if there is an available connection.
                    if (NetworkUtils.isConnected(MainActivity.this)) {
                        // If there is a valid result, display it on its corresponding layout.
                        if (data != null && !data.isEmpty() && data.size() > 0) {
                            Log.i(TAG, "(onLoadFinished) Search results for now playing movies not null.");
                            nowPlayingMoviesMessage.setVisibility(View.GONE);
                            inflateMoviesViewFlipperChildren(data, nowPlayingMoviesViewFlipper,
                                    nowPlayingMoviesCardView, backdropLinearLayoutParams,
                                    R.layout.layout_main_movies_backdrop, false,
                                    true);
                        } else {
                            Log.i(TAG, "(onLoadFinished) No search results for now playing movies.");
                            nowPlayingMoviesMessage.setText(
                                    getResources().getString(R.string.no_results));
                            nowPlayingMoviesMessage.setVisibility(View.VISIBLE);
                        }
                    } else {
                        // There is no connection. Show error message.
                        Log.i(TAG, "(onLoadFinished) No connection to internet.");
                        nowPlayingMoviesMessage.setText(
                                getResources().getString(R.string.no_connection));
                        nowPlayingMoviesMessage.setVisibility(View.VISIBLE);
                    }
                    break;
                }

                case NetworkUtils.TMDB_UPCOMING_MOVIES_LOADER_ID: {
                    upcomingMoviesLoadingIndicator.setVisibility(View.GONE);

                    // Check if there is an available connection.
                    if (NetworkUtils.isConnected(MainActivity.this)) {
                        // If there is a valid result, display it on its corresponding layout.
                        if (data != null && !data.isEmpty() && data.size() > 0) {
                            Log.i(TAG, "(onLoadFinished) Search results for upcoming movies not null.");
                            upcomingMoviesMessage.setVisibility(View.GONE);
                            inflateMoviesViewFlipperChildren(data, upcomingMoviesViewFlipper,
                                    upcomingMoviesCardView, backdropLinearLayoutParams,
                                    R.layout.layout_main_movies_backdrop, true,
                                    false);
                        } else {
                            Log.i(TAG, "(onLoadFinished) No search results for upcoming movies.");
                            upcomingMoviesMessage.setText(
                                    getResources().getString(R.string.no_results));
                            upcomingMoviesMessage.setVisibility(View.VISIBLE);
                        }
                    } else {
                        // There is no connection. Show error message.
                        Log.i(TAG, "(onLoadFinished) No connection to internet.");
                        upcomingMoviesMessage.setText(
                                getResources().getString(R.string.no_connection));
                        upcomingMoviesMessage.setVisibility(View.VISIBLE);
                    }
                    break;
                }

                default: {
                    Log.e(TAG, "(onCreateLoader) Unexpected loader id: " + loader.getId());
                }
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
        public void onLoaderReset(Loader<ArrayList<TmdbMovie>> loader) {
        }

        private void inflateMoviesViewFlipperChildren(ArrayList<TmdbMovie> data,
                                                      ViewFlipper viewFlipper,
                                                      CardView cardView,
                                                      LinearLayout.LayoutParams layoutParams,
                                                      @LayoutRes int layoutRes,
                                                      boolean showReleaseDate,
                                                      boolean showUsersRating) {
            // Add children to ViewFlipper, only for those elements with a backdrop and a title to
            // display.
            for (int i = 0; i < data.size(); i++) {
                String backdropPath = data.get(i).getBackdrop_path();
                String title = data.get(i).getTitle();
                if (backdropPath != null && !backdropPath.equals("") && !backdropPath.isEmpty() &&
                        title != null && !title.equals("") && !title.isEmpty()) {
                    // Inflate view to display info.
                    LayoutInflater inflater =
                            (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view = inflater.inflate(layoutRes, null);

                    // Set image size.
                    RelativeLayout imageLayout =
                            (RelativeLayout) view.findViewById(R.id.movie_list_image_layout);
                    imageLayout.setLayoutParams(layoutParams);

                    // Load image file.
                    ImageView imageView = (ImageView) view.findViewById(R.id.movie_list_image);
                    Picasso.with(MainActivity.this)
                            .load(Tmdb.TMDB_POSTER_SIZE_W500_URL + backdropPath)
                            .into(imageView);

                    // Set movie title.
                    TextView titleTextView = (TextView) view.findViewById(R.id.movie_list_title);
                    titleTextView.setText(title);

                    // Set release date, if needed.
                    TextView releaseDateTextView =
                            (TextView) view.findViewById(R.id.movie_list_year);
                    if (showReleaseDate) {
                        String releaseDate = DateTimeUtils.getStringDate(
                                data.get(i).getRelease_date(),
                                DateTimeUtils.DATE_FORMAT_LONG);
                        if (releaseDate != null && !releaseDate.equals("") && !releaseDate.isEmpty())
                            releaseDateTextView.setText(releaseDate);
                        else
                            releaseDateTextView.setText(getResources().getString(R.string.no_date));
                        releaseDateTextView.setVisibility(View.VISIBLE);
                    } else
                        releaseDateTextView.setVisibility(View.GONE);

                    // Set users rating, if needed.
                    TextView usersRatingTextView =
                            (TextView) view.findViewById(R.id.movie_list_rating);
                    if (showUsersRating) {
                        String score = String.valueOf(data.get(i).getVote_average());
                        ScoreUtils.setTextViewRating(
                                MainActivity.this, score, usersRatingTextView);
                        usersRatingTextView.setVisibility(View.VISIBLE);
                    } else
                        usersRatingTextView.setVisibility(View.GONE);

                    // Add current child to ViewFlipper.
                    viewFlipper.addView(view, viewFlipper.getLayoutParams());
                }
            }

            // If there's nothing to show, exit before making this section visible.
            if (viewFlipper.getChildCount() > 0) {
                //viewFlipper.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorMoviesPrimary));

                // Start ViewFlipper animation.
                if (!viewFlipper.isFlipping()) {
                    viewFlipper.startFlipping();
                }
            }
        }
    }

    // Private inner class to retrieve a given list of persons.
    private class MainActivityPeopleList implements LoaderManager.LoaderCallbacks<ArrayList<TmdbPerson>> {
        private final String TAG = MainActivity.MainActivityPeopleList.class.getSimpleName();

        // Constructor for objects of this class.
        MainActivityPeopleList(int loaderId) {
            // Create an AsyncTaskLoader for retrieving the list of people.
            if (loaderId >= 0)
                getSupportLoaderManager().initLoader(loaderId, null, this);
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
        public Loader<ArrayList<TmdbPerson>> onCreateLoader(int id, Bundle args) {
            connectionStatusLayout.setVisibility(View.VISIBLE);
            connectionStatusText.setTextColor(getResources().getColor(R.color.colorWhite));
            connectionStatusText.setText(getString(R.string.fetching_info));
            connectionStatusText.setVisibility(View.VISIBLE);
            connectionStatusLoadingIndicator.setVisibility(View.VISIBLE);
            connectionImage.setVisibility(View.GONE);

            if (NetworkUtils.isConnected(MainActivity.this)) {
                // There is an available connection. Fetch results from TMDB.
                switch (id) {
                    case NetworkUtils.TMDB_POPULAR_PEOPLE_LOADER_ID:
                        return new TmdbPeopleAsyncTaskLoader(MainActivity.this,
                                Tmdb.TMDB_CONTENT_TYPE_POPULAR, 1,
                                Locale.getDefault().getLanguage());

                    default: {
                        Log.e(TAG, "(onCreateLoader) Unexpected loader id: " + id);
                        return null;
                    }
                }
            } else {
                // There is no connection. Show error message.
                connectionStatusText.setText(getResources().getString(R.string.no_connection));
                connectionStatusLoadingIndicator.setVisibility(View.GONE);
                connectionImage.setVisibility(View.VISIBLE);
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
        public void onLoadFinished(Loader<ArrayList<TmdbPerson>> loader, ArrayList<TmdbPerson> data) {

            // Check if there is an available connection.
            if (NetworkUtils.isConnected(MainActivity.this)) {
                // If there is a valid result, display it on its corresponding layout.
                if (data != null && !data.isEmpty() && data.size() > 0) {
                    Log.i(TAG, "(onLoadFinished) Search results not null.");

                    // Get people list and display it.
                    boolean isSet = false;
                    switch (loader.getId()) {
                        case NetworkUtils.TMDB_POPULAR_PEOPLE_LOADER_ID: {
                            isSet = inflatePeopleViewFlipperChildren(data, popularPeopleViewFlipper,
                                    popularPeopleCardView, posterLayoutParams,
                                    R.layout.layout_main_people_backdrop);

                            // Hide Loading indicator.
                            popularPeopleLoadingIndicator.setVisibility(View.GONE);
                            break;
                        }

                        default: {
                            Log.e(TAG, "(onCreateLoader) Unexpected loader id: " +
                                    loader.getId());
                        }
                    }
                    if (isSet) {
                        // Show section if
                        popularOnTheAirLinearLayout.setVisibility(View.VISIBLE);
                        popularOnTheAirLinearLayout.startAnimation(AnimationUtils.loadAnimation(
                                MainActivity.this, R.anim.in_from_left));
                    }
                } else {
                    Log.i(TAG, "(onLoadFinished) No search results.");
                    connectionStatusText.setTextColor(getResources().getColor(R.color.colorWhite));
                    connectionStatusText.setText(getResources().getString(R.string.no_results));
                    connectionStatusText.setVisibility(View.VISIBLE);
                }
            } else

            {
                // There is no connection. Show error message.
                Log.i(TAG, "(onLoadFinished) No connection to internet.");
                connectionStatusText.setTextColor(getResources().getColor(R.color.colorWhite));
                connectionStatusText.setText(getResources().getString(R.string.no_connection));
                connectionStatusText.setVisibility(View.VISIBLE);
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
        public void onLoaderReset(Loader<ArrayList<TmdbPerson>> loader) {
        }

        private boolean inflatePeopleViewFlipperChildren(ArrayList<TmdbPerson> data,
                                                         ViewFlipper viewFlipper,
                                                         CardView cardView,
                                                         RelativeLayout.LayoutParams layoutParams,
                                                         @LayoutRes int layoutRes) {
            // Add children to ViewFlipper, only for those elements with a backdrop to display.
            for (int i = 0; i < data.size(); i++) {
                String profilePath = data.get(i).getProfile_path();
                if (profilePath != null && !profilePath.equals("") && !profilePath.isEmpty()) {
                    // Inflate view to display info.
                    LayoutInflater inflater =
                            (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    try {
                        View view = inflater.inflate(layoutRes, null);

                        // Load image file and set size.
                        ImageView imageView = (ImageView) view.findViewById(R.id.person_list_image);
                        Picasso.with(MainActivity.this)
                                .load(Tmdb.TMDB_POSTER_SIZE_W185_URL + profilePath)
                                .into(imageView);

                        // Set person name.
                        TextView personName = (TextView) view.findViewById(R.id.person_name);
                        personName.setText(data.get(i).getName());

                        // Add current child to ViewFlipper. The image inside takes the full size of
                        // the ViewFlipper, so we use layoutParams to create the current view.
                        viewFlipper.addView(view, layoutParams);
                    } catch (NullPointerException e) {
                        Log.e(TAG, "(inflatePeopleViewFlipperChildren) Error inflating view: "
                                + e);
                    }
                }
            }

            // If there's nothing to show, exit before making this section visible.
            if (viewFlipper.getChildCount() == 0)
                return false;
            else {
                // Start ViewFlipper animation.
                if (!viewFlipper.isFlipping()) {
                    viewFlipper.startFlipping();
                }

                viewFlipper.setBackgroundColor(ContextCompat.getColor(MainActivity.this,
                        R.color.colorPrimaryDark));

                // Make CardView visible with animation.
/*                Animation animation = AnimationUtils.loadAnimation(
                        MainActivity.this, R.anim.in_from_left);
                animation.setDuration(250);
                cardView.setVisibility(View.VISIBLE);
                cardView.startAnimation(animation);*/

                return true;
            }
        }
    }
}
