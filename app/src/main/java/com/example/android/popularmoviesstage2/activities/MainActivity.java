package com.example.android.popularmoviesstage2.activities;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.asynctaskloaders.GenericAsyncTaskLoader;
import com.example.android.popularmoviesstage2.classes.MyNavigationDrawer;
import com.example.android.popularmoviesstage2.classes.MyViewFlipperIndicator;
import com.example.android.popularmoviesstage2.classes.Tmdb;
import com.example.android.popularmoviesstage2.classes.TmdbMovie;
import com.example.android.popularmoviesstage2.classes.TmdbMoviesParameters;
import com.example.android.popularmoviesstage2.classes.TmdbPerson;
import com.example.android.popularmoviesstage2.classes.TmdbTVSeries;
import com.example.android.popularmoviesstage2.classes.TmdbTVSeriesParameters;
import com.example.android.popularmoviesstage2.data.MyPreferences;
import com.example.android.popularmoviesstage2.fragmentpageradapters.MoviesFragmentPagerAdapter;
import com.example.android.popularmoviesstage2.utils.AnimatedViewsUtils;
import com.example.android.popularmoviesstage2.utils.DateTimeUtils;
import com.example.android.popularmoviesstage2.utils.DisplayUtils;
import com.example.android.popularmoviesstage2.utils.NetworkUtils;
import com.example.android.popularmoviesstage2.utils.ScoreUtils;
import com.example.android.popularmoviesstage2.utils.TextViewUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

@SuppressLint("ClickableViewAccessibility")
@SuppressWarnings({"unchecked", "StatementWithEmptyBody"})
public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int MAX_ELEMENTS_FULL_SCREEN = 20;
    private static final int MAX_HALF_SCREEN_ELEMENTS = 8;
    private static final int CONTENT_TYPE_MOVIES = 0;
    private static final int CONTENT_TYPE_SERIES = 1;
    private static final int CONTENT_TYPE_PEOPLE = 2;
    /*    private static final int RESULT_CODE_UPCOMING_MOVIES = 100;
        private static final int RESULT_CODE_POPULAR_PEOPLE = 101;
        private static final int RESULT_CODE_ON_THE_AIR_SERIES = 102;
        private static final int RESULT_CODE_NOW_PLAYING_MOVIES = 103;
        private static final int RESULT_CODE_BUY_AND_RENT_MOVIES = 104;
        private static final int RESULT_CODE_BUY_AND_RENT_SERIES = 105;
        private static final int RESULT_CODE_AIRING_TODAY_SERIES = 106;*/
    private static final int FLIP_INTERVAL_SHORT = 3000;
    private static final int FLIP_INTERVAL_MEDIUM = 4000;
    private static final int FLIP_INTERVAL_LONG = 5000;

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
    @BindView(R.id.home_popular_people_cardview)
    CardView popularPeopleCardView;
    @BindView(R.id.home_on_the_air_cardview)
    CardView onTheAirCardView;
    @BindView(R.id.home_now_playing_movies_cardview)
    CardView nowPlayingMoviesCardView;
    @BindView(R.id.home_buy_and_rent_series_cardview)
    CardView buyAndRentSeriesCardView;
    @BindView(R.id.home_buy_and_rent_movies_cardview)
    CardView buyAndRentMoviesCardView;
    @BindView(R.id.home_airing_today_cardview)
    CardView airingTodayCardView;

    @BindView(R.id.connection_status_layout)
    LinearLayout connectionStatusLayout;
    @BindView(R.id.connection_status_image_view)
    ImageView connectionStatusImage;
    @BindView(R.id.connection_status_text)
    TextView connectionStatusText;
    @BindView(R.id.connection_status_loading_indicator)
    ProgressBar connectionStatusLoadingIndicator;

    private View upcomingMoviesLayout = null;
    private View onTheAirLayout = null;
    private View popularPeopleLayout = null;
    private View nowPlayingMoviesLayout = null;
    private View buyAndRentMoviesLayout = null;
    private View buyAndRentSeriesLayout = null;
    private View airingTodayLayout = null;

    private MyViewFlipperIndicator upcomingMoviesViewFlipper = null;
    private MyViewFlipperIndicator popularPeopleViewFlipper = null;
    private MyViewFlipperIndicator onTheAirViewFlipper = null;
    private MyViewFlipperIndicator nowPlayingMoviesViewFlipper = null;
    private MyViewFlipperIndicator buyAndRentMoviesViewFlipper = null;
    private MyViewFlipperIndicator buyAndRentSeriesViewFlipper = null;
    private MyViewFlipperIndicator airingTodayViewFlipper = null;

    private ProgressBar upcomingMoviesLoadingIndicator = null;
    private ProgressBar popularPeopleLoadingIndicator = null;
    private ProgressBar onTheAirLoadingIndicator = null;
    private ProgressBar nowPlayingMoviesLoadingIndicator = null;
    private ProgressBar buyAndRentMoviesLoadingIndicator = null;
    private ProgressBar buyAndRentSeriesLoadingIndicator = null;
    private ProgressBar airingTodayLoadingIndicator = null;

    private TextView upcomingMoviesTitle = null;
    private TextView popularPeopleTitle = null;
    private TextView onTheAirTitle = null;
    private TextView nowPlayingMoviesTitle = null;
    private TextView buyAndRentMoviesTitle = null;
    private TextView buyAndRentSeriesTitle = null;
    private TextView airingTodayTitle = null;

    private TextView upcomingMoviesMessage = null;
    private TextView popularPeopleMessage = null;
    private TextView onTheAirMessage = null;
    private TextView nowPlayingMoviesMessage = null;
    private TextView buyAndRentMoviesMessage = null;
    private TextView buyAndRentSeriesMessage = null;
    private TextView airingTodayMessage = null;

    private Unbinder unbinder;
    private ArrayList<View> animatedViews = new ArrayList<>();
    private LinearLayout.LayoutParams backdropLinearLayoutParams, posterLinearLayoutParams;
    private RelativeLayout.LayoutParams backdropRelativeLayoutParams, posterRelativeLayoutParams;
    private int animatedViewCurrentIndex = 0;
    private TmdbMoviesParameters tmdbUpcomingMoviesParameters, tmdbNowPlayingMoviesParameters,
            tmdbBuyAndRentMoviesParameters;
    private TmdbTVSeriesParameters tmdbOnTheAirTVSeriesParameters,
            tmdbAiringTodayTVSeriesParameters, tmdbBuyAndRentTVSeriesParameters;
    private float initialX = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String methodTag = TAG + "." + Thread.currentThread().getStackTrace()[2].getMethodName();
        super.onCreate(savedInstanceState);

        AnimatedViewsUtils.setSlideTransitions(getWindow());

        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Initialise layout.
        clearLayout();
        if (NetworkUtils.isConnected(MainActivity.this)) {
            setSearchParameters();
            setSizes();
            setLayoutElements();
            setAnimations();
        } else {
            // If there is no internet connection, show message.
            connectionStatusLayout.setVisibility(View.VISIBLE);
            connectionStatusText.setText(getString(R.string.no_connection));
            connectionStatusText.setTextColor(getResources().getColor(R.color.colorGrey));
            connectionStatusText.setVisibility(View.VISIBLE);
            connectionStatusImage.setVisibility(View.VISIBLE);
            connectionStatusLoadingIndicator.setVisibility(View.GONE);
        }

        Log.i(methodTag, "Activity created");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when an item in the navigation menu is selected.
     *
     * @param item The selected item
     * @return true to display the item as the selected item
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return MyNavigationDrawer.onNavigationItemSelected(item.getItemId(),
                (DrawerLayout) findViewById(R.id.main_drawer_layout));
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
    }

    /**
     * Helper method for setting fixed parameters for fetching results from the main activity.
     */
    private void setSearchParameters() {
        Date currentDate = DateTimeUtils.getCurrentDate();
        String currentLanguage = MyPreferences.getIsoLanguage(this);
        String today = DateTimeUtils.getStringCurrentDate();
        String tomorrow = DateTimeUtils.getStringAddedDaysToDate(currentDate, 1);
        String fortyFiveDaysAgo = DateTimeUtils.getStringAddedDaysToDate(currentDate, -45);
        String nextWeek = DateTimeUtils.getStringAddedDaysToDate(currentDate, 7);
        String currentSortOrder = MyPreferences.getMoviesSortOrder(this);
        ArrayList<String> VODNetworks = Tmdb.getVODNetworks();

        // Fixed parameters for now playing movies in theaters.
        tmdbNowPlayingMoviesParameters =
                MyPreferences.getAll(this, Tmdb.TMDB_CONTENT_TYPE_NOW_PLAYING);
        String moviesNowPlayingReleaseType = getResources().getStringArray(
                R.array.preferences_now_playing_movies_how_values_array)
                [MyPreferences.MOVIES_NOW_PLAYING_HOW_THEATERS_INDEX];
        tmdbNowPlayingMoviesParameters.setReleaseType(moviesNowPlayingReleaseType);
        tmdbNowPlayingMoviesParameters.setInitDate(fortyFiveDaysAgo);
        tmdbNowPlayingMoviesParameters.setEndDate(today);
        tmdbNowPlayingMoviesParameters.setVoteCount(1);

        // Fixed parameters for upcoming movies in theaters.
        tmdbUpcomingMoviesParameters =
                MyPreferences.getAll(this, Tmdb.TMDB_CONTENT_TYPE_UPCOMING);
        String moviesUpcomingReleaseType = getResources().getStringArray(
                R.array.preferences_upcoming_movies_how_values_array)
                [MyPreferences.MOVIES_UPCOMING_HOW_THEATERS_INDEX];
        tmdbUpcomingMoviesParameters.setReleaseType(moviesUpcomingReleaseType);
        tmdbUpcomingMoviesParameters.setInitDate(tomorrow);
        tmdbUpcomingMoviesParameters.setEndDate("");

        // Fixed parameters for movies for buy and rent.
        tmdbBuyAndRentMoviesParameters =
                MyPreferences.getAll(this, Tmdb.TMDB_CONTENT_TYPE_FOR_BUY_AND_RENT);
        String buyAndRentMoviesReleaseType = getResources().getStringArray(
                R.array.preferences_now_playing_movies_how_values_array)
                [MyPreferences.MOVIES_NOW_PLAYING_HOW_DIGITAL_INDEX];
        tmdbBuyAndRentMoviesParameters.setReleaseType(buyAndRentMoviesReleaseType);
        tmdbBuyAndRentMoviesParameters.setInitDate("");
        tmdbBuyAndRentMoviesParameters.setEndDate(today);

        // Fixed parameters for currently on the air TV series (any TV show that has an episode
        // with an air date in the next 7 days).
        tmdbOnTheAirTVSeriesParameters = new TmdbTVSeriesParameters(currentLanguage,
                currentSortOrder, 0.0, 0, today, nextWeek, null);

        // Fixed parameters for airing today TV series.
        tmdbAiringTodayTVSeriesParameters = new TmdbTVSeriesParameters(currentLanguage,
                currentSortOrder, 0.0, 0, today, today, null);

        // Fixed parameters for TV series to watch online on VOD networks.
        tmdbBuyAndRentTVSeriesParameters = new TmdbTVSeriesParameters(currentLanguage,
                currentSortOrder, 0.0, 0, "", "",
                VODNetworks);
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

        backdropLinearLayoutParams = new LinearLayout.LayoutParams(backdropWidthPixels,
                backdropHeightPixels);
        backdropRelativeLayoutParams = new RelativeLayout.LayoutParams(backdropWidthPixels,
                backdropHeightPixels + getResources().getDimensionPixelSize(R.dimen.big_padding));

        // Sizes and layouts for posters.
        int posterWidthPixels = (displayUtils.getFullDisplayBackdropWidthPixels() -
                (3 * getResources().getDimensionPixelSize(R.dimen.small_padding))) / 2;
        int posterHeightPixels = posterWidthPixels * 3 / 2;

        posterLinearLayoutParams = new LinearLayout.LayoutParams(posterWidthPixels,
                posterHeightPixels);
        posterRelativeLayoutParams = new RelativeLayout.LayoutParams(posterWidthPixels,
                posterHeightPixels + getResources().getDimensionPixelSize(R.dimen.big_padding));
    }

    /**
     * Helper method to initially set every element in the main_menu layout.
     */
    private void setLayoutElements() {
        final Context context = MainActivity.this;

        // All movies section. Click to start MoviesActivity with animation.
        allMoviesCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MoviesActivity.class);
                intent.putExtra("sort", MoviesFragmentPagerAdapter.PAGE_ALL);
                Bundle option = ActivityOptions
                        .makeSceneTransitionAnimation(MainActivity.this).toBundle();

                // Animate view when clicked and navigate to next activity.
                AnimatedViewsUtils.animateOnClick(context, allMoviesCardView);
                startActivity(intent, option);
            }
        });

        // Now playing movies section.
        nowPlayingMoviesLayout = setCardView(nowPlayingMoviesCardView, CONTENT_TYPE_MOVIES,
                true, getString(R.string.movies_sort_by_now_playing_in_theatres),
                FLIP_INTERVAL_LONG);
        if (nowPlayingMoviesLayout != null) {
            // Extract all layout elements and assign them to their corresponding private variables.
            nowPlayingMoviesViewFlipper = (MyViewFlipperIndicator) nowPlayingMoviesLayout.findViewById(
                    R.id.layout_main_cardview_content_viewflipper);
            nowPlayingMoviesMessage = (TextView) nowPlayingMoviesLayout.findViewById(
                    R.id.layout_main_cardview_content_message);
            nowPlayingMoviesLoadingIndicator = (ProgressBar) nowPlayingMoviesLayout.findViewById(
                    R.id.layout_main_cardview_content_loading_indicator);

            // Set ViewFlipper size.
            nowPlayingMoviesViewFlipper.setLayoutParams(backdropRelativeLayoutParams);

            // Click to start MoviesActivity with animation, showing now playing movies page.
            nowPlayingMoviesTitle = (TextView) nowPlayingMoviesLayout.findViewById(
                    R.id.layout_main_cardview_content_title);
            nowPlayingMoviesTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Set preferences for "Now Playing Movies" to show "In Theaters" and "In my
                    // country".
                    MyPreferences.setNowPlayingMovies(context,
                            MyPreferences.MOVIES_NOW_PLAYING_HOW_THEATERS_INDEX,
                            MyPreferences.TYPE_MOVIES_HOW);
                    MyPreferences.setNowPlayingMovies(context,
                            MyPreferences.MOVIES_UPCOMING_WHERE_THIS_REGION_INDEX,
                            MyPreferences.TYPE_MOVIES_WHERE);

                    // Set intent for calling MoviesActivity.
                    Intent intent = new Intent(context, MoviesActivity.class);
                    intent.putExtra("sort", MoviesFragmentPagerAdapter.PAGE_NOW_PLAYING);
                    Bundle option = ActivityOptions
                            .makeSceneTransitionAnimation(MainActivity.this).toBundle();

                    // Animate view when clicked and navigate to next activity.
                    AnimatedViewsUtils.animateOnClick(context, nowPlayingMoviesCardView);
                    startActivity(intent, option);
                }
            });
        }

        popularPeopleLayout = setCardView(popularPeopleCardView, CONTENT_TYPE_PEOPLE,
                true, getString(R.string.popular), FLIP_INTERVAL_SHORT);
        if (popularPeopleLayout != null) {
            // Extract all layout elements and assign them to their corresponding private variables.
            popularPeopleViewFlipper = (MyViewFlipperIndicator) popularPeopleLayout.findViewById(
                    R.id.layout_main_cardview_content_viewflipper);
            popularPeopleMessage = (TextView) popularPeopleLayout.findViewById(
                    R.id.layout_main_cardview_content_message);
            popularPeopleLoadingIndicator = (ProgressBar) popularPeopleLayout.findViewById(
                    R.id.layout_main_cardview_content_loading_indicator);

            // Set ViewFlipper size.
            popularPeopleViewFlipper.setLayoutParams(posterRelativeLayoutParams);
        }

        onTheAirLayout = setCardView(onTheAirCardView, CONTENT_TYPE_SERIES, true,
                getString(R.string.tv_on_the_air), FLIP_INTERVAL_MEDIUM);
        if (onTheAirLayout != null) {
            // Extract all layout elements and assign them to their corresponding private variables.
            onTheAirViewFlipper = (MyViewFlipperIndicator) onTheAirLayout.findViewById(
                    R.id.layout_main_cardview_content_viewflipper);
            onTheAirMessage = (TextView) onTheAirLayout.findViewById(
                    R.id.layout_main_cardview_content_message);
            onTheAirLoadingIndicator = (ProgressBar) onTheAirLayout.findViewById(
                    R.id.layout_main_cardview_content_loading_indicator);

            // Set ViewFlipper size.
            onTheAirViewFlipper.setLayoutParams(posterRelativeLayoutParams);

            // Click to start TVSeriesActivity with animation, showing on the air series list page.
            onTheAirTitle = (TextView) onTheAirLayout.findViewById(
                    R.id.layout_main_cardview_content_title);
            onTheAirTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Set preferences for "Now Playing Movies" to show "In Theaters" and "In my
                    // country".
/*                    MyPreferences.setonTheAir(context,
                            MyPreferences.MOVIES_NOW_PLAYING_HOW_THEATERS_INDEX,
                            MyPreferences.TYPE_MOVIES_HOW);
                    MyPreferences.setonTheAir(context,
                            MyPreferences.MOVIES_UPCOMING_WHERE_THIS_REGION_INDEX,
                            MyPreferences.TYPE_MOVIES_WHERE);*/

                    // Set intent for calling TVSeriesActivity.
                    Intent intent = new Intent(context, TVSeriesActivity.class);
                    intent.putExtra("sort", MoviesFragmentPagerAdapter.PAGE_NOW_PLAYING);
                    Bundle option = ActivityOptions
                            .makeSceneTransitionAnimation(MainActivity.this).toBundle();

                    // Animate view when clicked and navigate to next activity.
                    AnimatedViewsUtils.animateOnClick(context, onTheAirCardView);
                    startActivity(intent, option);
                }
            });
        }

        // Upcoming movies section.
        upcomingMoviesLayout = setCardView(upcomingMoviesCardView, CONTENT_TYPE_MOVIES,
                true, getString(R.string.movies_sort_by_upcoming_in_theatres),
                FLIP_INTERVAL_LONG);
        if (upcomingMoviesLayout != null) {
            // Extract all layout elements and assign them to their corresponding private variables.
            upcomingMoviesViewFlipper = (MyViewFlipperIndicator) upcomingMoviesLayout.findViewById(
                    R.id.layout_main_cardview_content_viewflipper);
            upcomingMoviesMessage = (TextView) upcomingMoviesLayout.findViewById(
                    R.id.layout_main_cardview_content_message);
            upcomingMoviesLoadingIndicator = (ProgressBar) upcomingMoviesLayout.findViewById(
                    R.id.layout_main_cardview_content_loading_indicator);

            // Set ViewFlipper size.
            upcomingMoviesViewFlipper.setLayoutParams(backdropRelativeLayoutParams);

            // Click to start MoviesActivity with animation, showing upcoming movies page.
            upcomingMoviesTitle = (TextView) upcomingMoviesLayout.findViewById(
                    R.id.layout_main_cardview_content_title);
            upcomingMoviesTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Set preferences for "Upcoming Movies" to show "In Theaters", "Any date" and
                    // "In my country".
                    MyPreferences.setUpcomingMovies(context,
                            MyPreferences.MOVIES_UPCOMING_HOW_THEATERS_INDEX,
                            MyPreferences.TYPE_MOVIES_HOW);
                    MyPreferences.setUpcomingMovies(context,
                            MyPreferences.MOVIES_UPCOMING_WHEN_ANY_DATE_INDEX,
                            MyPreferences.TYPE_MOVIES_WHEN);
                    MyPreferences.setUpcomingMovies(context,
                            MyPreferences.MOVIES_UPCOMING_WHERE_THIS_REGION_INDEX,
                            MyPreferences.TYPE_MOVIES_WHERE);

                    // Set intent for calling MoviesActivity.
                    Intent intent = new Intent(context,
                            MoviesActivity.class);
                    intent.putExtra("sort", MoviesFragmentPagerAdapter.PAGE_UPCOMING);
                    Bundle option = ActivityOptions
                            .makeSceneTransitionAnimation(MainActivity.this).toBundle();

                    // Animate view when clicked and navigate to next activity.
                    AnimatedViewsUtils.animateOnClick(context, upcomingMoviesCardView);
                    startActivity(intent, option);
                }
            });
        }

        buyAndRentSeriesLayout = setCardView(buyAndRentSeriesCardView, CONTENT_TYPE_SERIES,
                true, getString(R.string.movies_sort_by_online), FLIP_INTERVAL_MEDIUM);
        if (buyAndRentSeriesLayout != null) {
            // Extract all layout elements and assign them to their corresponding private variables.
            buyAndRentSeriesViewFlipper = (MyViewFlipperIndicator) buyAndRentSeriesLayout.findViewById(
                    R.id.layout_main_cardview_content_viewflipper);
            buyAndRentSeriesMessage = (TextView) buyAndRentSeriesLayout.findViewById(
                    R.id.layout_main_cardview_content_message);
            buyAndRentSeriesLoadingIndicator = (ProgressBar) buyAndRentSeriesLayout.findViewById(
                    R.id.layout_main_cardview_content_loading_indicator);

            // Set ViewFlipper size.
            buyAndRentSeriesViewFlipper.setLayoutParams(posterRelativeLayoutParams);
        }

        buyAndRentMoviesLayout = setCardView(buyAndRentMoviesCardView, CONTENT_TYPE_MOVIES,
                true, getString(R.string.movies_sort_by_online), FLIP_INTERVAL_MEDIUM);
        if (buyAndRentMoviesLayout != null) {
            // Extract all layout elements and assign them to their corresponding private variables.
            buyAndRentMoviesViewFlipper = (MyViewFlipperIndicator) buyAndRentMoviesLayout.findViewById(
                    R.id.layout_main_cardview_content_viewflipper);
            buyAndRentMoviesMessage = (TextView) buyAndRentMoviesLayout.findViewById(
                    R.id.layout_main_cardview_content_message);
            buyAndRentMoviesLoadingIndicator = (ProgressBar) buyAndRentMoviesLayout.findViewById(
                    R.id.layout_main_cardview_content_loading_indicator);

            // Set ViewFlipper size.
            buyAndRentMoviesViewFlipper.setLayoutParams(posterRelativeLayoutParams);

            // Click to start MoviesActivity with animation, showing buy and rent movies page.
            buyAndRentMoviesTitle = (TextView) buyAndRentMoviesLayout.findViewById(
                    R.id.layout_main_cardview_content_title);
            buyAndRentMoviesTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Set preferences for "Now Playing Movies" to show "Digital movie releases" and
                    // "any date" and "In my country".
                    MyPreferences.setNowPlayingMovies(context,
                            MyPreferences.MOVIES_NOW_PLAYING_HOW_DIGITAL_INDEX,
                            MyPreferences.TYPE_MOVIES_HOW);
                    MyPreferences.setNowPlayingMovies(context,
                            MyPreferences.MOVIES_UPCOMING_WHERE_THIS_REGION_INDEX,
                            MyPreferences.TYPE_MOVIES_WHERE);

                    // Set intent for calling MoviesActivity.
                    Intent intent = new Intent(context, MoviesActivity.class);
                    intent.putExtra("sort", MoviesFragmentPagerAdapter.PAGE_NOW_PLAYING);
                    Bundle option = ActivityOptions
                            .makeSceneTransitionAnimation(MainActivity.this).toBundle();

                    // Animate view when clicked and navigate to next activity.
                    AnimatedViewsUtils.animateOnClick(context,
                            buyAndRentMoviesCardView);
                    startActivity(intent, option);
                }
            });
        }

        airingTodayLayout = setCardView(airingTodayCardView, CONTENT_TYPE_SERIES, true,
                getString(R.string.tv_airing_today), FLIP_INTERVAL_LONG);
        if (airingTodayLayout != null) {
            // Extract all layout elements and assign them to their corresponding private variables.
            airingTodayViewFlipper = (MyViewFlipperIndicator) airingTodayLayout.findViewById(
                    R.id.layout_main_cardview_content_viewflipper);
            airingTodayMessage = (TextView) airingTodayLayout.findViewById(
                    R.id.layout_main_cardview_content_message);
            airingTodayLoadingIndicator = (ProgressBar) airingTodayLayout.findViewById(
                    R.id.layout_main_cardview_content_loading_indicator);

            // Set ViewFlipper size.
            airingTodayViewFlipper.setLayoutParams(backdropRelativeLayoutParams);
        }
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
        animatedViews.add(nowPlayingMoviesCardView);
        animatedViews.add(onTheAirCardView);
        animatedViews.add(popularPeopleCardView);
        animatedViews.add(upcomingMoviesCardView);
        animatedViews.add(buyAndRentMoviesCardView);
        animatedViews.add(buyAndRentSeriesCardView);
        animatedViews.add(airingTodayCardView);
        processAnimationQueue(100);
    }

    /**
     * Helper method for creating a new view, inflated from the layout_main_cardview_content.xml
     * file, and making it the child of the given cardView.
     *
     * @param cardView     is the CardView in which the new child layout is going to be
     *                     created.
     * @param contentType  is the content type to display into the CardView. Available values
     *                     are CONTENT_TYPE_MOVIES, CONTENT_TYPE_SERIES, CONTENT_TYPE_PEOPLE.
     * @param showControls is true if control elements (next and previous) for ViewFlipper
     *                     must be shown; false otherwise.
     * @param title        is the title for this section.
     * @param flipInterval is the flip interval duration in milliseconds.
     * @return the inflated view or null if there has happened something wrong.
     */
    private View setCardView(CardView cardView, int contentType, boolean showControls, String title,
                             final int flipInterval) {
        String methodTag = TAG + "." + Thread.currentThread().getStackTrace()[2].getMethodName();
        LayoutInflater inflater =
                (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        try {
            // Inflate view to display info.
            View cardViewContent =
                    inflater.inflate(R.layout.layout_main_cardview_content, null);

            // Extract ViewFlipper from the current layout.
            final MyViewFlipperIndicator viewFlipper = (MyViewFlipperIndicator)
                    cardViewContent.findViewById(R.id.layout_main_cardview_content_viewflipper);

            // Animations.
            final Animation animationInFromRight =
                    AnimationUtils.loadAnimation(this, R.anim.in_from_right);
            final Animation animationOutFromRight =
                    AnimationUtils.loadAnimation(this, R.anim.out_from_right);
            final Animation animationInFromLeft =
                    AnimationUtils.loadAnimation(this, R.anim.in_from_left);
            final Animation animationOutFromLeft =
                    AnimationUtils.loadAnimation(this, R.anim.out_from_left);

            // Get colors and texts depending on the content type.
            int colorPrimary, colorPrimaryDark, colorPrimaryLight, tintedCompoundDrawable;
            String sectionTitle;
            ColorStateList colorStateListPrimary;
            switch (contentType) {
                case CONTENT_TYPE_MOVIES:
                    colorPrimaryLight = getResources().getColor(R.color.colorMoviesPrimaryLight);
                    colorPrimary = getResources().getColor(R.color.colorMoviesPrimary);
                    colorPrimaryDark = getResources().getColor(R.color.colorMoviesPrimaryDark);
                    colorStateListPrimary =
                            getResources().getColorStateList(R.color.colorMoviesPrimary);
                    sectionTitle = getString(R.string.movies);
                    tintedCompoundDrawable = R.drawable.ic_local_movies_white_18dp;
                    break;

                case CONTENT_TYPE_SERIES:
                    colorPrimaryLight = getResources().getColor(R.color.colorSeriesPrimaryLight);
                    colorPrimary = getResources().getColor(R.color.colorSeriesPrimary);
                    colorPrimaryDark = getResources().getColor(R.color.colorSeriesPrimaryDark);
                    colorStateListPrimary =
                            getResources().getColorStateList(R.color.colorSeriesPrimary);
                    sectionTitle = getString(R.string.series);
                    tintedCompoundDrawable = R.drawable.ic_tv_white_18dp;
                    break;

                default: // case CONTENT_TYPE_PEOPLE:
                    colorPrimaryLight = getResources().getColor(R.color.colorPeoplePrimaryLight);
                    colorPrimary = getResources().getColor(R.color.colorPeoplePrimary);
                    colorPrimaryDark = getResources().getColor(R.color.colorPeoplePrimaryDark);
                    colorStateListPrimary =
                            getResources().getColorStateList(R.color.colorPeoplePrimary);
                    sectionTitle = getString(R.string.people);
                    tintedCompoundDrawable = R.drawable.ic_person_white_18dp;
            }

            // Set flip interval duration and animations for auto start behaviour.
            viewFlipper.setFlipInterval(flipInterval);
            viewFlipper.setInAnimation(animationInFromRight);
            viewFlipper.setOutAnimation(animationOutFromRight);

            // Set colors and sizes for ViewFlipper indicators.
            Paint paintCurrent = new Paint();
            paintCurrent.setColor(colorPrimaryLight);
            viewFlipper.setPaintCurrent(paintCurrent);
            Paint paintNormal = new Paint();
            paintNormal.setColor(colorPrimaryDark);
            viewFlipper.setPaintNormal(paintNormal);
            viewFlipper.setRadius(5);
            viewFlipper.setMargin(5);

            // Set texts and colors.
            TextView sectionTitleTextView = (TextView)
                    cardViewContent.findViewById(R.id.layout_main_cardview_content_section);
            sectionTitleTextView.setText(sectionTitle);
            sectionTitleTextView.setBackgroundTintList(colorStateListPrimary);
            TextViewUtils.setTintedCompoundDrawable(this, sectionTitleTextView,
                    TextViewUtils.DRAWABLE_LEFT_INDEX, tintedCompoundDrawable, R.color.colorWhite,
                    R.dimen.tiny_padding);
            TextView contentTitleTextView = (TextView)
                    cardViewContent.findViewById(R.id.layout_main_cardview_content_title);
            contentTitleTextView.setText(title);
            contentTitleTextView.setBackgroundColor(colorPrimary);

            // Background colors.
            RelativeLayout relativeLayout = (RelativeLayout)
                    cardViewContent.findViewById(R.id.layout_main_cardview_content_layout);
            relativeLayout.setBackgroundColor(colorPrimaryDark);
            View separator =
                    cardViewContent.findViewById(R.id.layout_main_cardview_content_separator);
            separator.setBackgroundTintList(colorStateListPrimary);

            // Make the just created layout the child of the CardView and return it.
            cardView.addView(cardViewContent);
            return cardViewContent;
        } catch (NullPointerException e) {
            Log.e(methodTag, "Error inflating view: " + e);
            return null;
        }
    }

    /**
     * Display animations for every view included in animatedViews array.
     *
     * @param delayMillis is the delay (in milliseconds) to wait before displaying current animation.
     */
    public void processAnimationQueue(final int delayMillis) {
        final Handler handler = new Handler();
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.in_from_left);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animation.setDuration(250);
                animation.setInterpolator(new LinearOutSlowInInterpolator());
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation arg0) {
                        // Instantiate classes for opening new threads to retrieve data from TMDB
                        // after performing the animation associated to the corresponding view.
                        View currentView = animatedViews.get(animatedViewCurrentIndex);
                        if (currentView == upcomingMoviesCardView) {
                            // Displaying animation for upcoming movies section: fetch results from
                            // TMDB.
                            new MainActivity.MainActivityResultsList(
                                    NetworkUtils.TMDB_UPCOMING_MOVIES_LOADER_ID);
                        } else if (currentView == nowPlayingMoviesCardView) {
                            // Displaying animation for now playing movies section: fetch results
                            // from TMDB.
                            new MainActivity.MainActivityResultsList(
                                    NetworkUtils.TMDB_NOW_PLAYING_MOVIES_LOADER_ID);
                        } else if (currentView == popularPeopleCardView) {
                            // Displaying animation for popular people section: fetch results from
                            // TMDB.
                            new MainActivity.MainActivityResultsList(
                                    NetworkUtils.TMDB_POPULAR_PEOPLE_LOADER_ID);
                        } else if (currentView == buyAndRentMoviesCardView) {
                            // Displaying animation for buy and rent movies section: fetch results
                            // from TMDB.
                            new MainActivity.MainActivityResultsList(
                                    NetworkUtils.TMDB_BUY_AND_RENT_MOVIES_LOADER_ID);
                        } else if (currentView == onTheAirCardView) {
                            // Displaying animation for on the air TV series section: fetch results
                            // from TMDB.
                            new MainActivity.MainActivityResultsList(
                                    NetworkUtils.TMDB_ON_THE_AIR_SERIES_LOADER_ID);
                        } else if (currentView == airingTodayCardView) {
                            // Displaying animation for airing today TV series section: fetch
                            // results from TMDB.
                            new MainActivity.MainActivityResultsList(
                                    NetworkUtils.TMDB_AIRING_TODAY_SERIES_LOADER_ID);
                        } else if (currentView == buyAndRentSeriesCardView) {
                            // Displaying animation for buy and rent series section: fetch results
                            // from TMDB.
                            new MainActivity.MainActivityResultsList(
                                    NetworkUtils.TMDB_BUY_AND_RENT_SERIES_LOADER_ID);
                        }

                        // Order animation for next view, if there's still another one in the queue.
                        animatedViewCurrentIndex++;
                        if (animatedViewCurrentIndex < animatedViews.size())
                            processAnimationQueue(delayMillis);
                    }

                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                    }

                    @Override
                    public void onAnimationEnd(Animation arg0) {
                    }
                });

                // Animate current view after delayMillis from start.
                View view = animatedViews.get(animatedViewCurrentIndex);
                view.setVisibility(View.VISIBLE);
                view.startAnimation(animation);
            }
        }, delayMillis);
    }

    /**
     * Helper method to set movie, series or people info in a ViewFlipper into a CardView in the
     * main_menu activity.
     *
     * @param data         is the array of elements to be shown.
     * @param viewFlipper  is the ViewFlipper that displays the info.
     * @param layoutParams is the size for the views into the ViewFlipper.
     * @param loaderId     is the number that identifies the origin of the search, and
     *                     therefore the type of information that must be shown.
     * @param maxElements  is the maximum number of elements to be displayed into the
     *                     ViewFlipper.
     * @param flipInterval is the delay for the handler to restart ViewFlipper animation, in
     *                     milliseconds.
     * @return true if viewFlipper is displaying at least one element, false otherwise.
     */
    private boolean inflateViewFlipperChildren(final ArrayList<?> data,
                                               final ViewFlipper viewFlipper,
                                               LinearLayout.LayoutParams layoutParams,
                                               final int loaderId, int maxElements, int flipInterval) {
        final String methodTag = TAG + "." + Thread.currentThread().getStackTrace()[2].getMethodName();
        final Context context = MainActivity.this;
        final Long delayMillis = Long.valueOf(flipInterval);

        // Initially clear ViewFlipper.
        viewFlipper.removeAllViews();

        // Animations.
        final Animation animationInFromRight =
                AnimationUtils.loadAnimation(context, R.anim.in_from_right);
        final Animation animationOutFromRight =
                AnimationUtils.loadAnimation(context, R.anim.out_from_right);
        final Animation animationInFromLeft =
                AnimationUtils.loadAnimation(context, R.anim.in_from_left);
        final Animation animationOutFromLeft =
                AnimationUtils.loadAnimation(context, R.anim.out_from_left);

        // Define Handler and Runnable for restarting auto flipping again when the Runnable is
        // dispatched.
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // Set animation from right to left and show set next element to be
                // displayed.
                viewFlipper.setInAnimation(animationInFromRight);
                viewFlipper.setOutAnimation(animationOutFromRight);
                if (viewFlipper.getDisplayedChild() < viewFlipper.getChildCount())
                    viewFlipper.setDisplayedChild(viewFlipper.getDisplayedChild() + 1);
                else
                    viewFlipper.setDisplayedChild(0);

                // Start flipping again.
                viewFlipper.startFlipping();
            }
        };

        // Add children to ViewFlipper, only the first maxElements elements and only for those
        // elements with an image to display.
        int i = 0;
        while (i < data.size() && i < maxElements) {
            // Backdrop or poster.
            String imagePath;
            switch (loaderId) {
                case NetworkUtils.TMDB_NOW_PLAYING_MOVIES_LOADER_ID:
                case NetworkUtils.TMDB_UPCOMING_MOVIES_LOADER_ID:
                    imagePath = ((ArrayList<TmdbMovie>) data).get(i).getBackdrop_path();
                    break;

                case NetworkUtils.TMDB_BUY_AND_RENT_MOVIES_LOADER_ID:
                    imagePath = ((ArrayList<TmdbMovie>) data).get(i).getPoster_path();
                    break;

                case NetworkUtils.TMDB_ON_THE_AIR_SERIES_LOADER_ID:
                case NetworkUtils.TMDB_BUY_AND_RENT_SERIES_LOADER_ID:
                    imagePath = ((ArrayList<TmdbTVSeries>) data).get(i).getPoster_path();
                    break;

                case NetworkUtils.TMDB_AIRING_TODAY_SERIES_LOADER_ID:
                    imagePath = ((ArrayList<TmdbTVSeries>) data).get(i).getBackdrop_path();
                    break;

                default: // case NetworkUtils.TMDB_POPULAR_PEOPLE_LOADER_ID:
                    imagePath = ((ArrayList<TmdbPerson>) data).get(i).getProfile_path();
                    break;
            }

            // Movie or series title, or person name.
            String name;
            switch (loaderId) {
                case NetworkUtils.TMDB_NOW_PLAYING_MOVIES_LOADER_ID:
                case NetworkUtils.TMDB_UPCOMING_MOVIES_LOADER_ID:
                case NetworkUtils.TMDB_BUY_AND_RENT_MOVIES_LOADER_ID:
                    name = ((ArrayList<TmdbMovie>) data).get(i).getTitle();
                    break;

                case NetworkUtils.TMDB_ON_THE_AIR_SERIES_LOADER_ID:
                case NetworkUtils.TMDB_AIRING_TODAY_SERIES_LOADER_ID:
                case NetworkUtils.TMDB_BUY_AND_RENT_SERIES_LOADER_ID:
                    name = ((ArrayList<TmdbTVSeries>) data).get(i).getName();
                    break;

                default: // case NetworkUtils.TMDB_POPULAR_PEOPLE_LOADER_ID:
                    name = ((ArrayList<TmdbPerson>) data).get(i).getName();
                    break;
            }

            if (!imagePath.equals("") && !imagePath.isEmpty() && !name.equals("") &&
                    !name.isEmpty()) {
                // Inflate view to display info.
                LayoutInflater inflater =
                        (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                try {
                    View view = inflater.inflate(R.layout.list_item_main, null);

                    // Load image file and set size.
                    final ImageView imageView =
                            (ImageView) view.findViewById(R.id.list_item_main_image);
                    switch (loaderId) {
                        case NetworkUtils.TMDB_NOW_PLAYING_MOVIES_LOADER_ID:
                        case NetworkUtils.TMDB_UPCOMING_MOVIES_LOADER_ID:
                        case NetworkUtils.TMDB_AIRING_TODAY_SERIES_LOADER_ID:
                        case NetworkUtils.TMDB_BUY_AND_RENT_SERIES_LOADER_ID:
                            imagePath = Tmdb.TMDB_POSTER_SIZE_W300_URL + imagePath;
                            break;

                        case NetworkUtils.TMDB_BUY_AND_RENT_MOVIES_LOADER_ID:
                        case NetworkUtils.TMDB_POPULAR_PEOPLE_LOADER_ID:
                        case NetworkUtils.TMDB_ON_THE_AIR_SERIES_LOADER_ID:
                            imagePath = Tmdb.TMDB_POSTER_SIZE_W185_URL + imagePath;
                            break;
                    }
                    Picasso.with(context)
                            .load(imagePath)
                            .into(imageView);
                    RelativeLayout imageLayout =
                            (RelativeLayout) view.findViewById(R.id.list_item_main_layout);
                    imageLayout.setLayoutParams(layoutParams);

                    // Set movie, series or person info.
                    TextView titleTextView =
                            (TextView) view.findViewById(R.id.list_item_main_name);
                    TextView releaseDateTextView =
                            (TextView) view.findViewById(R.id.list_item_main_date);
                    TextView usersRatingTextView =
                            (TextView) view.findViewById(R.id.list_item_main_score);
                    switch (loaderId) {
                        case NetworkUtils.TMDB_NOW_PLAYING_MOVIES_LOADER_ID:
                        case NetworkUtils.TMDB_AIRING_TODAY_SERIES_LOADER_ID: {
                            // Set movie or TV series title.
                            titleTextView.setText(name);

                            // Show users rating.
                            String score;
                            if (loaderId == NetworkUtils.TMDB_NOW_PLAYING_MOVIES_LOADER_ID)
                                score = String.valueOf(((ArrayList<TmdbMovie>) data)
                                        .get(i).getVote_average());
                            else
                                score = String.valueOf(((ArrayList<TmdbTVSeries>) data)
                                        .get(i).getVote_average());
                            ScoreUtils.setTextViewRating(
                                    context, score, usersRatingTextView);
                            usersRatingTextView.setVisibility(View.VISIBLE);

                            // Hide release date.
                            releaseDateTextView.setVisibility(View.GONE);
                            break;
                        }

                        case NetworkUtils.TMDB_UPCOMING_MOVIES_LOADER_ID: {
                            // Set movie title.
                            titleTextView.setText(name);

                            // Show release date.
                            String releaseDate = DateTimeUtils.getStringDate(
                                    ((ArrayList<TmdbMovie>) data).get(i).getRelease_date(),
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

                        case NetworkUtils.TMDB_BUY_AND_RENT_MOVIES_LOADER_ID:
                        case NetworkUtils.TMDB_ON_THE_AIR_SERIES_LOADER_ID:
                        case NetworkUtils.TMDB_BUY_AND_RENT_SERIES_LOADER_ID:
                            // Hide unused layout elements.
                            titleTextView.setVisibility(View.GONE);
                            releaseDateTextView.setVisibility(View.GONE);
                            usersRatingTextView.setVisibility(View.GONE);
                            break;

                        case NetworkUtils.TMDB_POPULAR_PEOPLE_LOADER_ID:
                            // Set person name.
                            titleTextView.setText(name);

                            // Hide unused layout elements.
                            releaseDateTextView.setVisibility(View.GONE);
                            usersRatingTextView.setVisibility(View.GONE);
                            break;
                    }

                    // Set the onTouchListener for detecting clicks on current image and for
                    // managing swipe gestures.
                    final int currentElement = i;
                    imageView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent motionEvent) {
                            int action = motionEvent.getAction();
                            switch (action) {
                                case MotionEvent.ACTION_DOWN:
                                case MotionEvent.ACTION_POINTER_DOWN:
                                    // Stop flipping and remove previous callbacks on the Handler
                                    // for avoiding to be dispatched again if it was defined
                                    // previously.
                                    viewFlipper.stopFlipping();
                                    handler.removeCallbacks(runnable);

                                    // Register current X coordinate,
                                    initialX = motionEvent.getX();
                                    break;

                                case MotionEvent.ACTION_UP:
                                case MotionEvent.ACTION_POINTER_UP:
                                case MotionEvent.ACTION_CANCEL:
                                    // Set the runnable again to be dispatched after 5 seconds, so
                                    // the automatic ViewFlipper animation restarts again.
                                    handler.postDelayed(runnable, delayMillis);

                                    float finalX = motionEvent.getX();
                                    if (initialX > (finalX + 50)) {
                                        // Show next element with an animation from right to left.
                                        // Give a minimum margin of 50 points to consider a swipe to
                                        // left gesture.
                                        viewFlipper.setInAnimation(animationInFromRight);
                                        viewFlipper.setOutAnimation(animationOutFromRight);
                                        viewFlipper.showNext();
                                    } else if ((initialX + 50) < finalX) {
                                        // Show previous element with an animation from left to
                                        // right. Give a minimum margin of 50 points to consider a
                                        // swipe to right gesture.
                                        viewFlipper.setInAnimation(animationInFromLeft);
                                        viewFlipper.setOutAnimation(animationOutFromLeft);
                                        viewFlipper.showPrevious();
                                    } else if ((finalX <= (initialX + 10)) &&
                                            (initialX <= (finalX + 10))) {
                                        // This is a single click. Open a new activity to show
                                        // detailed info about the current movie/series/person. Give
                                        // a maximum margin of 10 points to consider that this is a
                                        // click gesture and not a swipe gesture.
                                        Intent intent = null;
                                        Bundle option = null;
                                        switch (loaderId) {
                                            case NetworkUtils.TMDB_NOW_PLAYING_MOVIES_LOADER_ID:
                                            case NetworkUtils.TMDB_UPCOMING_MOVIES_LOADER_ID:
                                            case NetworkUtils.TMDB_BUY_AND_RENT_MOVIES_LOADER_ID:
                                                // Prepare default enter transition for the new
                                                // activity.
                                                intent = new Intent(context,
                                                        MovieDetailsActivity.class);
                                                TmdbMovie movie = ((ArrayList<TmdbMovie>) data)
                                                        .get(currentElement);
                                                intent.putExtra(
                                                        MovieDetailsActivity.EXTRA_PARAM_MOVIE,
                                                        movie);
                                                option = ActivityOptions.makeSceneTransitionAnimation(
                                                        MainActivity.this).toBundle();
                                                break;

                                            // TODO: write code for the rest of the loader ids.
                                            // At present, you get an error if you click on a TV
                                            // series element.

                                            case NetworkUtils.TMDB_POPULAR_PEOPLE_LOADER_ID:
                                                intent = new Intent(context,
                                                        PersonDetailsActivity.class);
                                                TmdbPerson currentPerson =
                                                        ((ArrayList<TmdbPerson>) data)
                                                                .get(currentElement);
                                                TmdbPerson person = new TmdbPerson(
                                                        currentPerson.getId(),
                                                        currentPerson.getName(),
                                                        currentPerson.getProfile_path());
                                                intent.putExtra(
                                                        PersonDetailsActivity.EXTRA_PARAM_PERSON,
                                                        person);
                                                option = ActivityOptions.makeSceneTransitionAnimation(
                                                        MainActivity.this).toBundle();
                                                break;
                                        }

                                        // Animate view when clicked and navigate to next activity.
                                        AnimatedViewsUtils.animateOnClick(context, imageView);
                                        if (intent != null) {
                                            if (option != null)
                                                startActivity(intent, option);
                                            else
                                                startActivity(intent);
                                        }
                                    }
                            }
                            return true;
                        }
                    });

                    // Add current child to ViewFlipper.
                    viewFlipper.addView(view, viewFlipper.getLayoutParams());
                } catch (java.lang.NullPointerException e) {
                    Log.e(methodTag, "Error inflatingview: " + e);
                }
            }
            i++;
        }

        // Show ViewFlipper only if it has one valid child at least.
        if (viewFlipper.getChildCount() > 0) {
            if (!viewFlipper.isFlipping()) {
                // Start ViewFlipper animation.
                viewFlipper.startFlipping();
            }
            return true;
        } else {
            // ViewFlipper has no elements.
            return false;
        }
    }

    /* ------------- */
    /* INNER CLASSES */
    /* ------------- */

    // Private inner class to retrieve a given list of movies.
    private class MainActivityResultsList implements LoaderManager.LoaderCallbacks<Object> {
        private final String TAG = MainActivity.MainActivityResultsList.class.getSimpleName();

        // Constructor for objects of this class.
        MainActivityResultsList(int loaderId) {
            // Create an AsyncTaskLoader for retrieving the list of movies.
            if (loaderId >= 0)
                if (getSupportLoaderManager().getLoader(loaderId) == null) {
                    // If this is the first time, init loader.
                    getSupportLoaderManager().initLoader(loaderId, null, this);
                } else {
                    // If it is not the first time, restart loader.
                    getSupportLoaderManager().restartLoader(loaderId, null, this);
                }
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
        public Loader<Object> onCreateLoader(int id, Bundle args) {
            String methodTag = TAG + "." + Thread.currentThread().getStackTrace()[2].getMethodName();
            Context context = MainActivity.this;

            switch (id) {
                case NetworkUtils.TMDB_UPCOMING_MOVIES_LOADER_ID:
                    upcomingMoviesLoadingIndicator.setVisibility(View.VISIBLE);
                    if (NetworkUtils.isConnected(context)) {
                        // There is an available connection. Fetch upcoming movies (in theaters and
                        // released any date later than tomorrow) from TMDB.
                        upcomingMoviesMessage.setVisibility(View.GONE);
                        return new GenericAsyncTaskLoader(context,
                                Tmdb.TMDB_CONTENT_TYPE_UPCOMING, null, 1,
                                tmdbUpcomingMoviesParameters,
                                GenericAsyncTaskLoader.ASYNC_TASK_LOADER_TYPE_TMDB_MOVIES_LIST);
                    } else {
                        // There is no connection. Show error message.
                        upcomingMoviesMessage.setText(
                                getResources().getString(R.string.no_connection));
                        upcomingMoviesMessage.setVisibility(View.VISIBLE);
                        upcomingMoviesLoadingIndicator.setVisibility(View.GONE);
                        Log.i(methodTag, "No internet connection.");
                        return null;
                    }


                case NetworkUtils.TMDB_NOW_PLAYING_MOVIES_LOADER_ID:
                    nowPlayingMoviesLoadingIndicator.setVisibility(View.VISIBLE);
                    if (NetworkUtils.isConnected(context)) {
                        // There is an available connection. Fetch Fetch now playing movies (in
                        // theaters, released 45 days ago or later) from TMDB.
                        nowPlayingMoviesMessage.setVisibility(View.GONE);
                        return new GenericAsyncTaskLoader(context,
                                Tmdb.TMDB_CONTENT_TYPE_NOW_PLAYING, null,
                                1, tmdbNowPlayingMoviesParameters,
                                GenericAsyncTaskLoader.ASYNC_TASK_LOADER_TYPE_TMDB_MOVIES_LIST);
                    } else {
                        // There is no connection. Show error message.
                        nowPlayingMoviesMessage.setText(
                                getResources().getString(R.string.no_connection));
                        nowPlayingMoviesMessage.setVisibility(View.VISIBLE);
                        nowPlayingMoviesLoadingIndicator.setVisibility(View.GONE);
                        Log.i(methodTag, "No internet connection.");
                        return null;
                    }

                case NetworkUtils.TMDB_BUY_AND_RENT_MOVIES_LOADER_ID:
                    buyAndRentMoviesLoadingIndicator.setVisibility(View.VISIBLE);
                    if (NetworkUtils.isConnected(context)) {
                        // There is an available connection. Fetch buy and rent movies (released in
                        // digital platforms, any date) from TMDB.
                        buyAndRentMoviesMessage.setVisibility(View.GONE);
                        return new GenericAsyncTaskLoader(context,
                                Tmdb.TMDB_CONTENT_TYPE_FOR_BUY_AND_RENT, null,
                                1, tmdbBuyAndRentMoviesParameters,
                                GenericAsyncTaskLoader.ASYNC_TASK_LOADER_TYPE_TMDB_MOVIES_LIST);
                    } else {
                        // There is no connection. Show error message.
                        buyAndRentMoviesMessage.setText(
                                getResources().getString(R.string.no_connection));
                        buyAndRentMoviesMessage.setVisibility(View.VISIBLE);
                        buyAndRentMoviesLoadingIndicator.setVisibility(View.GONE);
                        Log.i(methodTag, "No internet connection.");
                        return null;
                    }

                case NetworkUtils.TMDB_POPULAR_PEOPLE_LOADER_ID:
                    popularPeopleLoadingIndicator.setVisibility(View.VISIBLE);
                    if (NetworkUtils.isConnected(context)) {
                        // There is an available connection. Fetch results from TMDB.
                        popularPeopleMessage.setVisibility(View.GONE);
                        return new GenericAsyncTaskLoader(
                                context, 1, Locale.getDefault().getLanguage(),
                                GenericAsyncTaskLoader.ASYNC_TASK_LOADER_TYPE_TMDB_PEOPLE);
                    } else {
                        // There is no connection. Show error message.
                        popularPeopleMessage.setText(
                                getResources().getString(R.string.no_connection));
                        popularPeopleMessage.setVisibility(View.VISIBLE);
                        popularPeopleLoadingIndicator.setVisibility(View.GONE);
                        Log.i(methodTag, "No internet connection.");
                        return null;
                    }

                case NetworkUtils.TMDB_ON_THE_AIR_SERIES_LOADER_ID:
                    onTheAirLoadingIndicator.setVisibility(View.VISIBLE);
                    if (NetworkUtils.isConnected(context)) {
                        // There is an available connection. Fetch results from TMDB.
                        onTheAirMessage.setVisibility(View.GONE);
                        return new GenericAsyncTaskLoader(context,
                                Tmdb.TMDB_CONTENT_TYPE_ON_THE_AIR, null,
                                1, tmdbOnTheAirTVSeriesParameters,
                                GenericAsyncTaskLoader.ASYNC_TASK_LOADER_TYPE_TMDB_SERIES_LIST);
                    } else {
                        // There is no connection. Show error message.
                        onTheAirMessage.setText(getResources().getString(R.string.no_connection));
                        onTheAirMessage.setVisibility(View.VISIBLE);
                        onTheAirLoadingIndicator.setVisibility(View.GONE);
                        Log.i(methodTag, "No internet connection.");
                        return null;
                    }

                case NetworkUtils.TMDB_AIRING_TODAY_SERIES_LOADER_ID:
                    airingTodayLoadingIndicator.setVisibility(View.VISIBLE);
                    if (NetworkUtils.isConnected(context)) {
                        // There is an available connection. Fetch results from TMDB.
                        airingTodayMessage.setVisibility(View.GONE);
                        return new GenericAsyncTaskLoader(context,
                                Tmdb.TMDB_CONTENT_TYPE_AIRING_TODAY, null,
                                1, tmdbAiringTodayTVSeriesParameters,
                                GenericAsyncTaskLoader.ASYNC_TASK_LOADER_TYPE_TMDB_SERIES_LIST);
                    } else {
                        // There is no connection. Show error message.
                        airingTodayMessage.setText(getResources().getString(R.string.no_connection));
                        airingTodayMessage.setVisibility(View.VISIBLE);
                        airingTodayLoadingIndicator.setVisibility(View.GONE);
                        Log.i(methodTag, "No internet connection.");
                        return null;
                    }

                case NetworkUtils.TMDB_BUY_AND_RENT_SERIES_LOADER_ID:
                    buyAndRentSeriesLoadingIndicator.setVisibility(View.VISIBLE);
                    if (NetworkUtils.isConnected(context)) {
                        // There is an available connection. Fetch results from TMDB.
                        buyAndRentSeriesMessage.setVisibility(View.GONE);
                        return new GenericAsyncTaskLoader(context,
                                Tmdb.TMDB_CONTENT_TYPE_FOR_BUY_AND_RENT, null,
                                1, tmdbBuyAndRentTVSeriesParameters,
                                GenericAsyncTaskLoader.ASYNC_TASK_LOADER_TYPE_TMDB_SERIES_LIST);
                    } else {
                        // There is no connection. Show error message.
                        buyAndRentSeriesMessage.setText(getResources().getString(R.string.no_connection));
                        buyAndRentSeriesMessage.setVisibility(View.VISIBLE);
                        buyAndRentSeriesLoadingIndicator.setVisibility(View.GONE);
                        Log.i(methodTag, "No internet connection.");
                        return null;
                    }

                default:
                    Log.e(methodTag, "Unexpected loader id: " + id);
                    return null;
            }
        }

        /**
         * Called when a previously created loaderId has finished its load.
         *
         * @param loader The Loader that has finished.
         * @param data   The data generated by the Loader.
         */
        @Override
        public void onLoadFinished(Loader<Object> loader, Object data) {
            String methodTag = TAG + "." + Thread.currentThread().getStackTrace()[2].getMethodName();
            Context context = MainActivity.this;

            // Get movies list and display it, depending on the loader identifier.
            switch (loader.getId()) {
                case NetworkUtils.TMDB_UPCOMING_MOVIES_LOADER_ID: {
                    upcomingMoviesLoadingIndicator.setVisibility(View.GONE);

                    // Check if there is an available connection.
                    if (NetworkUtils.isConnected(context)) {
                        // If there is a valid result, display it on its corresponding layout.
                        boolean hasData = true, hasValidData = true;
                        ArrayList<TmdbMovie> movies = (ArrayList<TmdbMovie>) data;
                        if (movies != null && movies.size() > 0) {
                            Log.i(methodTag,
                                    "Search results for upcoming movies not null.");
                            upcomingMoviesMessage.setVisibility(View.GONE);
                            hasValidData = inflateViewFlipperChildren(movies,
                                    upcomingMoviesViewFlipper, backdropLinearLayoutParams,
                                    loader.getId(), MAX_ELEMENTS_FULL_SCREEN, FLIP_INTERVAL_LONG);
                        } else {
                            Log.i(methodTag,
                                    "No search results for upcoming movies.");
                            hasData = false;
                        }
                        if (!hasData || !hasValidData) {
                            // There's no search results in the movies array or there's no elements
                            // into the ViewFlipper. Show alert message.
                            upcomingMoviesMessage.setText(getResources().getString(
                                    R.string.no_movies_results));
                            upcomingMoviesMessage.setVisibility(View.VISIBLE);
                        }
                    } else {
                        // There is no connection. Show error message.
                        Log.i(methodTag, "No connection to internet.");
                        upcomingMoviesMessage.setText(
                                getResources().getString(R.string.no_connection));
                        upcomingMoviesMessage.setVisibility(View.VISIBLE);
                    }
                    break;
                }

                case NetworkUtils.TMDB_NOW_PLAYING_MOVIES_LOADER_ID: {
                    nowPlayingMoviesLoadingIndicator.setVisibility(View.GONE);

                    // Check if there is an available connection.
                    if (NetworkUtils.isConnected(context)) {
                        // If there is a valid result, display it on its corresponding layout.
                        boolean hasData = true, hasValidData = true;
                        ArrayList<TmdbMovie> movies = (ArrayList<TmdbMovie>) data;
                        if (movies != null && movies.size() > 0) {
                            Log.i(methodTag,
                                    "Search results for now playing movies not null.");
                            nowPlayingMoviesMessage.setVisibility(View.GONE);
                            hasValidData = inflateViewFlipperChildren(movies,
                                    nowPlayingMoviesViewFlipper, backdropLinearLayoutParams,
                                    loader.getId(), MAX_ELEMENTS_FULL_SCREEN, FLIP_INTERVAL_LONG);
                        } else {
                            Log.i(methodTag,
                                    "No search results for now playing movies.");
                            hasData = false;
                        }
                        if (!hasData || !hasValidData) {
                            // There's no search results in the movies array or there's no elements
                            // into the ViewFlipper. Show alert message.
                            nowPlayingMoviesMessage.setText(getResources().getString(
                                    R.string.no_movies_results));
                            nowPlayingMoviesMessage.setVisibility(View.VISIBLE);
                        }
                    } else {
                        // There is no connection. Show error message.
                        Log.i(methodTag, "No connection to internet.");
                        nowPlayingMoviesMessage.setText(
                                getResources().getString(R.string.no_connection));
                        nowPlayingMoviesMessage.setVisibility(View.VISIBLE);
                    }
                    break;
                }

                case NetworkUtils.TMDB_BUY_AND_RENT_MOVIES_LOADER_ID: {
                    buyAndRentMoviesLoadingIndicator.setVisibility(View.GONE);

                    // Check if there is an available connection.
                    if (NetworkUtils.isConnected(context)) {
                        boolean hasData = true, hasValidData = true;
                        ArrayList<TmdbMovie> movies = (ArrayList<TmdbMovie>) data;
                        if (movies != null && movies.size() > 0) {
                            // If there is a valid result, display it on its corresponding layout.
                            Log.i(methodTag, "Search results for buy and rent movies not null.");
                            buyAndRentMoviesMessage.setVisibility(View.GONE);
                            hasValidData = inflateViewFlipperChildren(movies,
                                    buyAndRentMoviesViewFlipper, posterLinearLayoutParams,
                                    loader.getId(), MAX_HALF_SCREEN_ELEMENTS, FLIP_INTERVAL_MEDIUM);
                        } else {
                            Log.i(methodTag, "No search results for buy and rent movies.");
                            hasData = false;
                        }
                        if (!hasData || !hasValidData) {
                            // There's no search results in the movies array or there's no elements
                            // into the ViewFlipper. Show alert messages.
                            buyAndRentMoviesMessage.setText(getResources().getString(
                                    R.string.no_results));
                            buyAndRentMoviesMessage.setVisibility(View.VISIBLE);
                        }
                    } else {
                        // There is no connection. Show error message.
                        Log.i(methodTag, "No connection to internet.");
                        buyAndRentMoviesMessage.setText(
                                getResources().getString(R.string.no_connection));
                        buyAndRentMoviesMessage.setVisibility(View.VISIBLE);
                    }
                    break;
                }

                case NetworkUtils.TMDB_POPULAR_PEOPLE_LOADER_ID: {
                    popularPeopleLoadingIndicator.setVisibility(View.GONE);
                    if (NetworkUtils.isConnected(context)) {
                        ArrayList<TmdbPerson> people = (ArrayList<TmdbPerson>) data;

                        // If there is a valid result, display it on its corresponding layout.
                        if (people != null && people.size() > 0) {
                            Log.i(methodTag, "Search results not null.");
                            inflateViewFlipperChildren(people, popularPeopleViewFlipper,
                                    posterLinearLayoutParams, loader.getId(),
                                    MAX_HALF_SCREEN_ELEMENTS, FLIP_INTERVAL_SHORT);
                            break;
                        } else {
                            Log.i(methodTag, "No search results.");
                            popularPeopleMessage.setText(
                                    getResources().getString(R.string.no_results));
                            popularPeopleMessage.setVisibility(View.VISIBLE);
                        }
                    } else {
                        // There is no connection. Show error message.
                        Log.i(methodTag, "No connection to internet.");
                        popularPeopleMessage.setText(
                                getResources().getString(R.string.no_connection));
                        popularPeopleMessage.setVisibility(View.VISIBLE);
                    }
                }

                case NetworkUtils.TMDB_ON_THE_AIR_SERIES_LOADER_ID: {
                    onTheAirLoadingIndicator.setVisibility(View.GONE);

                    // Check if there is an available connection.
                    if (NetworkUtils.isConnected(context)) {
                        boolean hasData = true, hasValidData = true;
                        ArrayList<TmdbTVSeries> series = (ArrayList<TmdbTVSeries>) data;
                        if (series != null && series.size() > 0) {
                            // If there is a valid result, display it on its corresponding layout.
                            Log.i(methodTag, "Search results for on the air series not null.");
                            onTheAirMessage.setVisibility(View.GONE);
                            hasValidData = inflateViewFlipperChildren(series,
                                    onTheAirViewFlipper, posterLinearLayoutParams,
                                    loader.getId(), MAX_HALF_SCREEN_ELEMENTS, FLIP_INTERVAL_MEDIUM);
                        } else {
                            Log.i(methodTag, "No search results for on the air series.");
                            hasData = false;
                        }
                        if (!hasData || !hasValidData) {
                            // There's no search results in the tv series array or there's no
                            // elements into the ViewFlipper. Show alert messages.
                            onTheAirMessage.setText(getResources().getString(
                                    R.string.no_results));
                            onTheAirMessage.setVisibility(View.VISIBLE);
                        }
                    } else {
                        // There is no connection. Show error message.
                        Log.i(methodTag, "No connection to internet.");
                        onTheAirMessage.setText(getResources().getString(R.string.no_connection));
                        onTheAirMessage.setVisibility(View.VISIBLE);
                    }
                    break;
                }

                case NetworkUtils.TMDB_AIRING_TODAY_SERIES_LOADER_ID: {
                    airingTodayLoadingIndicator.setVisibility(View.GONE);

                    // Check if there is an available connection.
                    if (NetworkUtils.isConnected(context)) {
                        boolean hasData = true, hasValidData = true;
                        ArrayList<TmdbTVSeries> series = (ArrayList<TmdbTVSeries>) data;
                        if (series != null && series.size() > 0) {
                            // If there is a valid result, display it on its corresponding layout.
                            Log.i(methodTag, "Search results for airing today series not null.");
                            airingTodayMessage.setVisibility(View.GONE);
                            hasValidData = inflateViewFlipperChildren(series,
                                    airingTodayViewFlipper, backdropLinearLayoutParams,
                                    loader.getId(), MAX_ELEMENTS_FULL_SCREEN, FLIP_INTERVAL_LONG);
                        } else {
                            Log.i(methodTag, "No search results for airing today series.");
                            hasData = false;
                        }
                        if (!hasData || !hasValidData) {
                            // There's no search results in the tv series array or there's no
                            // elements into the ViewFlipper. Show alert messages.
                            airingTodayMessage.setText(getResources().getString(
                                    R.string.no_results));
                            airingTodayMessage.setVisibility(View.VISIBLE);
                        }
                    } else {
                        // There is no connection. Show error message.
                        Log.i(methodTag, "No connection to internet.");
                        airingTodayMessage.setText(getResources().getString(R.string.no_connection));
                        airingTodayMessage.setVisibility(View.VISIBLE);
                    }
                    break;
                }

                case NetworkUtils.TMDB_BUY_AND_RENT_SERIES_LOADER_ID: {
                    buyAndRentSeriesLoadingIndicator.setVisibility(View.GONE);

                    // Check if there is an available connection.
                    if (NetworkUtils.isConnected(context)) {
                        boolean hasData = true, hasValidData = true;
                        ArrayList<TmdbTVSeries> series = (ArrayList<TmdbTVSeries>) data;
                        if (series != null && series.size() > 0) {
                            // If there is a valid result, display it on its corresponding layout.
                            Log.i(methodTag, "Search results for buy and rent series not null.");
                            buyAndRentSeriesMessage.setVisibility(View.GONE);
                            hasValidData = inflateViewFlipperChildren(series,
                                    buyAndRentSeriesViewFlipper, posterLinearLayoutParams,
                                    loader.getId(), MAX_HALF_SCREEN_ELEMENTS, FLIP_INTERVAL_MEDIUM);
                        } else {
                            Log.i(methodTag, "No search results for buy and rent series.");
                            hasData = false;
                        }
                        if (!hasData || !hasValidData) {
                            // There's no search results in the tv series array or there's no
                            // elements into the ViewFlipper. Show alert messages.
                            buyAndRentSeriesMessage.setText(getResources().getString(
                                    R.string.no_results));
                            buyAndRentSeriesMessage.setVisibility(View.VISIBLE);
                        }
                    } else {
                        // There is no connection. Show error message.
                        Log.i(methodTag, "No connection to internet.");
                        buyAndRentSeriesMessage.setText(getResources().getString(R.string.no_connection));
                        buyAndRentSeriesMessage.setVisibility(View.VISIBLE);
                    }
                    break;
                }

                default: {
                    Log.e(methodTag, "Unexpected loader id: " + loader.getId());
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
        public void onLoaderReset(Loader<Object> loader) {
        }
    }
}