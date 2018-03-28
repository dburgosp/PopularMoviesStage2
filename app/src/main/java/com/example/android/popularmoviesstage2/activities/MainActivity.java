package com.example.android.popularmoviesstage2.activities;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.AnimRes;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
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
    private static final int MAX_MOVIES = 20;
    private static final int MAX_PEOPLE = 8;

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
    ViewFlipperIndicator popularPeopleViewFlipper;
    @BindView(R.id.home_popular_people_loading_indicator)
    ProgressBar popularPeopleLoadingIndicator;
    @BindView(R.id.home_popular_people_message)
    TextView popularPeopleMessage;
    @BindView(R.id.home_on_the_air_cardview)
    CardView onTheAirCardView;
    @BindView(R.id.home_on_the_air_viewflipper)
    ViewFlipperIndicator onTheAirViewFlipper;
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
    ImageView connectionStatusImage;
    @BindView(R.id.connection_status_text)
    TextView connectionStatusText;
    @BindView(R.id.connection_status_loading_indicator)
    ProgressBar connectionStatusLoadingIndicator;

    private Unbinder unbinder;
    private int animatedViewCurrentIndex = 0;
    private ArrayList<View> animatedViews = new ArrayList<>();
    private LinearLayout.LayoutParams backdropLinearLayoutParams, posterLinearLayoutParams;
    private RelativeLayout.LayoutParams backdropRelativeLayoutParams, posterRelativeLayoutParams;
    private int nowPlayingMoviesCurrentPage = 1, popularPeopleCurrentPage = 1,
            upcomingMoviesCurrentPage = 1;

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
            setViewFlippers();
        } else {
            // If there is no internet connection, show message.
            connectionStatusLayout.setVisibility(View.VISIBLE);
            connectionStatusText.setText(getString(R.string.no_connection));
            connectionStatusText.setVisibility(View.VISIBLE);
            connectionStatusImage.setVisibility(View.VISIBLE);
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
        onTheAirCardView.setVisibility(View.GONE);
        popularPeopleCardView.setVisibility(View.GONE);
        nowPlayingMoviesCardView.setVisibility(View.GONE);
        buyAndRentMoviesCardView.setVisibility(View.GONE);
        buyAndRentSeriesCardView.setVisibility(View.GONE);
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

        posterLinearLayoutParams = new LinearLayout.LayoutParams(posterWidthPixels, posterHeightPixels);
        posterRelativeLayoutParams = new RelativeLayout.LayoutParams(posterWidthPixels, posterHeightPixels +
                getResources().getDimensionPixelSize(R.dimen.big_padding));

        popularPeopleViewFlipper.setLayoutParams(posterRelativeLayoutParams);
        onTheAirViewFlipper.setLayoutParams(posterRelativeLayoutParams);
        buyAndRentMoviesViewFlipper.setLayoutParams(posterRelativeLayoutParams);
        buyAndRentSeriesViewFlipper.setLayoutParams(posterRelativeLayoutParams);
    }

    /**
     * Helper method to start animations on initial layouts and to set in and out animations for
     * ViewFlippers.
     */
    private void setAnimations() {
        // Animate initial layouts in this order.
        animatedViews.add(allMoviesCardView);
        animatedViews.add(allSeriesCardView);
        animatedViews.add(allPeopleCardView);
        animatedViews.add(upcomingMoviesCardView);
        animatedViews.add(onTheAirCardView);
        animatedViews.add(popularPeopleCardView);
        animatedViews.add(nowPlayingMoviesCardView);
        animatedViews.add(buyAndRentMoviesCardView);
        animatedViews.add(buyAndRentSeriesCardView);
        animatedViews.add(airingTodayCardView);
        processAnimationQueue(
                AnimationUtils.loadAnimation(this, R.anim.in_from_left), 300);
    }

    private void setViewFlippers() {
        setViewFlipper(upcomingMoviesViewFlipper, R.anim.in_from_right, R.anim.out_from_right,
                R.color.colorMoviesPrimaryLight, R.color.colorMoviesPrimaryDark);
        setViewFlipper(nowPlayingMoviesViewFlipper, R.anim.in_from_right, R.anim.out_from_right,
                R.color.colorMoviesPrimaryLight, R.color.colorMoviesPrimaryDark);
        setViewFlipper(popularPeopleViewFlipper, R.anim.in_from_right, R.anim.out_from_right,
                R.color.colorPeoplePrimaryLight, R.color.colorPeoplePrimaryDark);
        setViewFlipper(onTheAirViewFlipper, R.anim.in_from_right, R.anim.out_from_right,
                R.color.colorSeriesPrimaryLight, R.color.colorSeriesPrimaryDark);
    }

    private void setViewFlipper(final ViewFlipperIndicator viewFlipper, @AnimRes int animationIn,
                                @AnimRes int animationOut, @ColorRes int colorCurrent,
                                @ColorRes int colorNormal) {

        // Set animations for autoStart behaviour.
        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, animationIn));
        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, animationOut));

        // Set colors and sizes for indicators.
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(colorCurrent));
        viewFlipper.setPaintCurrent(paint);

        paint = new Paint();
        paint.setColor(getResources().getColor(colorNormal));
        viewFlipper.setPaintNormal(paint);

        viewFlipper.setRadius(5);
        viewFlipper.setMargin(5);

        // Define behaviour when the ViewFlipper is touched.
        viewFlipper.setOnTouchListener(new View.OnTouchListener() {
            private float init_x;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        // User touches screen. Get the X coordinate for measuring the horizontal
                        // displacement later in the ACTION_UP event.
                        init_x = event.getX();

                        // Stop flipping and schedule a new auto flipping for later.
                        viewFlipper.setFlipInterval(30000);
                        viewFlipper.stopFlipping();
                        viewFlipper.setAutoStart(false);
                    }

                    case MotionEvent.ACTION_UP: {
                        // User releases screen. Get new X coordinate.
                        float distance = init_x - event.getX();

                        if (distance > 0) {
                            // Right displacement.
                            viewFlipper.setInAnimation(AnimationUtils.loadAnimation(
                                    MainActivity.this, R.anim.in_from_right));
                            viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(
                                    MainActivity.this, R.anim.out_from_right));
                            viewFlipper.showNext();
                        } else if (distance < 0) {
                            // Left displacement.
                            viewFlipper.setInAnimation(AnimationUtils.loadAnimation(
                                    MainActivity.this, R.anim.in_from_left));
                            viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(
                                    MainActivity.this, R.anim.out_from_left));
                            viewFlipper.showPrevious();
                        } else {
                            // No displacement. This is a click gesture, so manage the click event
                            // on the current view.
                            return v.performClick();
                        }

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                viewFlipper.setFlipInterval(5000);
                                viewFlipper.setAutoStart(true);
                                viewFlipper.startFlipping();
                            }
                        }, 5000);
                        return false;
                    }

                    default: {
                        break;
                    }
                }
                return false;
            }
        });
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
                                Tmdb.TMDB_CONTENT_TYPE_UPCOMING, upcomingMoviesCurrentPage,
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
                                Tmdb.TMDB_CONTENT_TYPE_NOW_PLAYING, nowPlayingMoviesCurrentPage,
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
                                    backdropLinearLayoutParams, loader.getId());
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
                                    backdropLinearLayoutParams, loader.getId());
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
                    Log.e(TAG, "(onLoadFinished) Unexpected loader id: " + loader.getId());
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

        /**
         * Helper method to set movie, series or people info in a wide element with a backdrop image
         * in the main activity.
         *
         * @param data         is the array of elements to be shown.
         * @param viewFlipper  is the ViewFlipper that displays the info.
         * @param layoutParams is the size for the views into the ViewFlipper.
         * @param loaderId     is the number that identifies the origin of the search, and therefore
         *                     the type of information that must be shown.
         */
        private void inflateMoviesViewFlipperChildren(final ArrayList<TmdbMovie> data,
                                                      ViewFlipper viewFlipper,
                                                      LinearLayout.LayoutParams layoutParams,
                                                      int loaderId) {
            // Add children to ViewFlipper, only the first MAX_MOVIES elements and only for those
            // elements with a image to display.
            int i = 0;
            while (i < data.size() && i < MAX_MOVIES) {
                String backdropPath = data.get(i).getBackdrop_path();
                String title = data.get(i).getTitle();
                if (backdropPath != null && !backdropPath.equals("") && !backdropPath.isEmpty() &&
                        title != null && !title.equals("") && !title.isEmpty()) {
                    // Inflate view to display info.
                    LayoutInflater inflater =
                            (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    try {
                        View view = inflater.inflate(R.layout.list_item_main, null);

                        // Set image size.
                        RelativeLayout imageLayout =
                                (RelativeLayout) view.findViewById(R.id.list_item_main_layout);
                        imageLayout.setLayoutParams(layoutParams);

                        // Load image file.
                        ImageView imageView =
                                (ImageView) view.findViewById(R.id.list_item_main_image);
                        Picasso.with(MainActivity.this)
                                .load(Tmdb.TMDB_POSTER_SIZE_W500_URL + backdropPath)
                                .into(imageView);

                        // Set movie title.
                        TextView titleTextView =
                                (TextView) view.findViewById(R.id.list_item_main_name);
                        titleTextView.setText(title);

                        // Set release date or users score.
                        TextView releaseDateTextView =
                                (TextView) view.findViewById(R.id.list_item_main_date);
                        TextView usersRatingTextView =
                                (TextView) view.findViewById(R.id.list_item_main_score);
                        switch (loaderId) {
                            case NetworkUtils.TMDB_NOW_PLAYING_MOVIES_LOADER_ID: {
                                // Show users rating.
                                String score = String.valueOf(data.get(i).getVote_average());
                                ScoreUtils.setTextViewRating(
                                        MainActivity.this, score, usersRatingTextView);
                                usersRatingTextView.setVisibility(View.VISIBLE);

                                // Hide release date.
                                releaseDateTextView.setVisibility(View.GONE);
                                break;
                            }

                            case NetworkUtils.TMDB_UPCOMING_MOVIES_LOADER_ID: {
                                // Show release date.
                                String releaseDate = DateTimeUtils.getStringDate(
                                        data.get(i).getRelease_date(),
                                        DateTimeUtils.DATE_FORMAT_LONG);
                                if (releaseDate != null && !releaseDate.equals("") &&
                                        !releaseDate.isEmpty())
                                    releaseDateTextView.setText(releaseDate);
                                else
                                    releaseDateTextView.setText(
                                            getResources().getString(R.string.no_date));
                                releaseDateTextView.setVisibility(View.VISIBLE);

                                // Hide users rating.
                                usersRatingTextView.setVisibility(View.GONE);
                                break;
                            }
                        }

                        // Set the onClickMoviesListener for navigating to the movie details
                        // activity when clicking on the view.
                        final int currentMovie = i;
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Open a new MovieDetailsActivity to show detailed info about the
                                // current movie.
                                Intent intent = new Intent(MainActivity.this,
                                        MovieDetailsActivity.class);
                                intent.putExtra(MovieDetailsActivity.EXTRA_PARAM_MOVIE,
                                        data.get(currentMovie));
                                startActivity(intent);
                            }
                        });

                        // Add current child to ViewFlipper.
                        viewFlipper.addView(view, viewFlipper.getLayoutParams());
                    } catch (java.lang.NullPointerException e) {
                        Log.e(TAG, "(inflateMoviesViewFlipperChildren) Error inflatingview: "
                                + e);
                    }
                }
                i++;
            }

            // Show ViewFlipper only if it has one valid child at least.
            if (viewFlipper.getChildCount() > 0) {
                // Start ViewFlipper animation.
                if (!viewFlipper.isFlipping()) {
                    viewFlipper.startFlipping();
                }
            } else {
                // If there's no valid elements in the first page of results, try next page.
                switch (loaderId) {
                    case NetworkUtils.TMDB_NOW_PLAYING_MOVIES_LOADER_ID: {
                        if (nowPlayingMoviesCurrentPage < data.get(0).getTotal_pages()) {
                            nowPlayingMoviesCurrentPage++;
                            new MainActivity.MainActivityMoviesList(
                                    NetworkUtils.TMDB_NOW_PLAYING_MOVIES_LOADER_ID);
                            new MainActivity.MainActivityMoviesList(
                                    NetworkUtils.TMDB_UPCOMING_MOVIES_LOADER_ID);
                        } else {
                            // There's no more pages to try. Show alert message.
                            nowPlayingMoviesMessage.setText(getString(R.string.no_results));
                            nowPlayingMoviesMessage.setVisibility(View.VISIBLE);
                        }
                        break;
                    }

                    case NetworkUtils.TMDB_UPCOMING_MOVIES_LOADER_ID: {
                        if (upcomingMoviesCurrentPage < data.get(0).getTotal_pages()) {
                            upcomingMoviesCurrentPage++;
                            new MainActivity.MainActivityMoviesList(
                                    NetworkUtils.TMDB_UPCOMING_MOVIES_LOADER_ID);
                        } else {
                            // There's no more pages to try. Show alert message.
                            nowPlayingMoviesMessage.setText(getString(R.string.no_results));
                            nowPlayingMoviesMessage.setVisibility(View.VISIBLE);
                        }
                        break;
                    }
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
            switch (id) {
                case NetworkUtils.TMDB_POPULAR_PEOPLE_LOADER_ID: {
                    popularPeopleLoadingIndicator.setVisibility(View.VISIBLE);
                    if (NetworkUtils.isConnected(MainActivity.this)) {
                        // There is an available connection. Fetch results from TMDB.
                        popularPeopleMessage.setVisibility(View.GONE);
                        return new TmdbPeopleAsyncTaskLoader(MainActivity.this,
                                Tmdb.TMDB_CONTENT_TYPE_POPULAR, 1,
                                Locale.getDefault().getLanguage());
                    } else {
                        // There is no connection. Show error message.
                        popularPeopleMessage.setText(getResources().getString(R.string.no_connection));
                        popularPeopleMessage.setVisibility(View.VISIBLE);
                        popularPeopleLoadingIndicator.setVisibility(View.GONE);
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
        public void onLoadFinished(Loader<ArrayList<TmdbPerson>> loader, ArrayList<TmdbPerson> data) {
            // Get people list and display it.
            switch (loader.getId()) {
                case NetworkUtils.TMDB_POPULAR_PEOPLE_LOADER_ID: {
                    popularPeopleLoadingIndicator.setVisibility(View.GONE);
                    if (NetworkUtils.isConnected(MainActivity.this)) {
                        // If there is a valid result, display it on its corresponding layout.
                        if (data != null && !data.isEmpty() && data.size() > 0) {
                            Log.i(TAG, "(onLoadFinished) Search results not null.");
                            inflatePeopleViewFlipperChildren(data, popularPeopleViewFlipper,
                                    popularPeopleCardView, posterLinearLayoutParams,
                                    R.layout.list_item_main);
                            break;
                        } else {
                            Log.i(TAG, "(onLoadFinished) No search results.");
                            popularPeopleMessage.setText(getResources().getString(R.string.no_results));
                            popularPeopleMessage.setVisibility(View.VISIBLE);
                        }
                    } else {
                        // There is no connection. Show error message.
                        Log.i(TAG, "(onLoadFinished) No connection to internet.");
                        popularPeopleMessage.setText(getResources().getString(R.string.no_connection));
                        popularPeopleMessage.setVisibility(View.VISIBLE);
                    }
                }

                default: {
                    Log.e(TAG, "(onLoadFinished) Unexpected loader id: " +
                            loader.getId());
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
        public void onLoaderReset(Loader<ArrayList<TmdbPerson>> loader) {
        }

        private void inflatePeopleViewFlipperChildren(ArrayList<TmdbPerson> data,
                                                      ViewFlipper viewFlipper,
                                                      CardView cardView,
                                                      LinearLayout.LayoutParams layoutParams,
                                                      @LayoutRes int layoutRes) {
            // Add children to ViewFlipper, only the first MAX_PEOPLE elements and only for those
            // elements with a image to display.
            int i = 0;
            while (i < data.size() && i < MAX_PEOPLE) {
                String profilePath = data.get(i).getProfile_path();
                if (profilePath != null && !profilePath.equals("") && !profilePath.isEmpty()) {
                    // Inflate view to display info.
                    LayoutInflater inflater =
                            (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    try {
                        View view = inflater.inflate(layoutRes, null);

                        // Load image file and set size.
                        ImageView imageView =
                                (ImageView) view.findViewById(R.id.list_item_main_image);
                        Picasso.with(MainActivity.this)
                                .load(Tmdb.TMDB_POSTER_SIZE_W185_URL + profilePath)
                                .into(imageView);

                        // Set image size.
                        RelativeLayout imageLayout =
                                (RelativeLayout) view.findViewById(R.id.list_item_main_layout);
                        imageLayout.setLayoutParams(layoutParams);

                        // Set person name.
                        TextView personName =
                                (TextView) view.findViewById(R.id.list_item_main_name);
                        personName.setText(data.get(i).getName());

                        // Hide unused layout elements.
                        TextView date = (TextView) view.findViewById(R.id.list_item_main_date);
                        TextView score = (TextView) view.findViewById(R.id.list_item_main_score);
                        date.setVisibility(View.GONE);
                        score.setVisibility(View.GONE);

                        // Add current child to ViewFlipper.
                        viewFlipper.addView(view, viewFlipper.getLayoutParams());
                    } catch (NullPointerException e) {
                        Log.e(TAG, "(inflatePeopleViewFlipperChildren) Error inflating view: "
                                + e);
                    }
                }
                i++;
            }

            // Show ViewFlipper only if there's one element at least.
            if (viewFlipper.getChildCount() > 0) {
                // Start ViewFlipper animation.
                if (!viewFlipper.isFlipping()) {
                    viewFlipper.startFlipping();
                }
            }
        }
    }
}
