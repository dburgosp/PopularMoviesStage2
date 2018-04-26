package com.example.android.popularmoviesstage2.activities;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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
import com.example.android.popularmoviesstage2.data.MyPreferences;
import com.example.android.popularmoviesstage2.fragmentpageradapters.MoviesFragmentPagerAdapter;
import com.example.android.popularmoviesstage2.utils.DateTimeUtils;
import com.example.android.popularmoviesstage2.utils.DisplayUtils;
import com.example.android.popularmoviesstage2.utils.NetworkUtils;
import com.example.android.popularmoviesstage2.utils.ScoreUtils;
import com.example.android.popularmoviesstage2.utils.TextViewUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

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

    ImageView upcomingMoviesPreviousImageView;
    ImageView nowPlayingMoviesPreviousImageView;
    ImageView airingTodayPreviousImageView;
    ImageView upcomingMoviesNextImageView;
    ImageView nowPlayingMoviesNextImageView;
    ImageView airingTodayNextImageView;

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

    private ViewFlipperIndicator upcomingMoviesViewFlipper = null;
    private ViewFlipperIndicator popularPeopleViewFlipper = null;
    private ViewFlipperIndicator onTheAirViewFlipper = null;
    private ViewFlipperIndicator nowPlayingMoviesViewFlipper = null;
    private ViewFlipperIndicator buyAndRentMoviesViewFlipper = null;
    private ViewFlipperIndicator buyAndRentSeriesViewFlipper = null;
    private ViewFlipperIndicator airingTodayViewFlipper = null;

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

    private TextView upcomingMoviesMessageHeader = null;
    private TextView upcomingMoviesMessage = null;
    private TextView popularPeopleMessage = null;
    private TextView onTheAirMessage = null;
    private TextView nowPlayingMoviesMessageHeader = null;
    private TextView nowPlayingMoviesMessage = null;
    private TextView buyAndRentMoviesMessage = null;
    private TextView buyAndRentSeriesMessage = null;
    private TextView airingTodayMessageHeader = null;
    private TextView airingTodayMessage = null;

    private Unbinder unbinder;
    private ArrayList<View> animatedViews = new ArrayList<>();
    private LinearLayout.LayoutParams backdropLinearLayoutParams, posterLinearLayoutParams;
    private RelativeLayout.LayoutParams backdropRelativeLayoutParams, posterRelativeLayoutParams;
    private int moviesVoteCount = 0, animatedViewCurrentIndex = 0;
    private String language, region, moviesCertification, moviesSortBy;
    private Double moviesVoteAverage = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTransitions();

        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        getMyPreferences();

        // Initialise layout.
        clearLayout();
        if (NetworkUtils.isConnected(MainActivity.this)) {
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
     * Define transitions to exit/enter from/to this activity.
     */
    private void setTransitions() {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setBackgroundDrawableResource(R.color.colorPrimaryDark);
        Slide slideIn = new Slide(Gravity.END);
        getWindow().setEnterTransition(slideIn.setDuration(250));
        Slide slideOut = new Slide(Gravity.START);
        getWindow().setExitTransition(slideOut.setDuration(250));
    }

    /**
     * Helper method to get current values from preferences for query parameters.
     */
    private void getMyPreferences() {
        moviesSortBy = MyPreferences.getMoviesSortOrder(this);
        language = MyPreferences.getIsoLanguage(this);
        region = MyPreferences.getIsoRegion(this);
        moviesCertification = MyPreferences.getMoviesCertification(this);
        moviesVoteAverage = MyPreferences.getMoviesVoteAverage(this);
        moviesVoteCount = MyPreferences.getMoviesVoteCount(this);
    }

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

        // Sizes and layouts for posters.
        int posterWidthPixels = (displayUtils.getFullDisplayBackdropWidthPixels() -
                (3 * getResources().getDimensionPixelSize(R.dimen.small_padding))) / 2;
        int posterHeightPixels = posterWidthPixels * 3 / 2;

        posterLinearLayoutParams =
                new LinearLayout.LayoutParams(posterWidthPixels, posterHeightPixels);
        posterRelativeLayoutParams =
                new RelativeLayout.LayoutParams(posterWidthPixels, posterHeightPixels +
                        getResources().getDimensionPixelSize(R.dimen.big_padding));
    }

    /**
     * Helper method to initially set every element in the main layout.
     */
    private void setLayoutElements() {
        // All movies section. Click to start MoviesActivity with animation.
        allMoviesCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MoviesActivity.class);
                intent.putExtra("sort", MoviesFragmentPagerAdapter.PAGE_ALL);
                Bundle option = ActivityOptions
                        .makeSceneTransitionAnimation(MainActivity.this).toBundle();
                startActivity(intent, option);
            }
        });

        // Upcoming movies section.
        upcomingMoviesLayout = setCardView(upcomingMoviesCardView, CONTENT_TYPE_MOVIES,
                true, getString(R.string.movies_sort_by_upcoming), 5000);
        if (upcomingMoviesLayout != null) {
            // Extract all layout elements and assign them to their corresponding private variables.
            upcomingMoviesViewFlipper = (ViewFlipperIndicator) upcomingMoviesLayout.findViewById(
                    R.id.layout_main_cardview_content_viewflipper);
            upcomingMoviesMessageHeader = (TextView) upcomingMoviesLayout.findViewById(
                    R.id.layout_main_cardview_content_message_header);
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
                    // "Locale region".
                    MyPreferences.setUpcomingMoviesHow(MainActivity.this,
                            MyPreferences.MOVIES_UPCOMING_HOW_THEATERS_INDEX);
                    MyPreferences.setUpcomingMoviesWhen(MainActivity.this,
                            MyPreferences.MOVIES_UPCOMING_WHEN_ANY_DATE_INDEX);
                    MyPreferences.setUpcomingMoviesWhere(MainActivity.this,
                            MyPreferences.MOVIES_UPCOMING_WHERE_THIS_REGION_INDEX);

                    // Set intent for calling MoviesActivity.
                    Intent intent = new Intent(MainActivity.this,
                            MoviesActivity.class);
                    intent.putExtra("sort", MoviesFragmentPagerAdapter.PAGE_UPCOMING);
                    Bundle option = ActivityOptions
                            .makeSceneTransitionAnimation(MainActivity.this).toBundle();
                    startActivity(intent, option);
                }
            });
        }

        popularPeopleLayout = setCardView(popularPeopleCardView, CONTENT_TYPE_PEOPLE,
                false, getString(R.string.popular), 3000);
        if (popularPeopleLayout != null) {
            // Extract all layout elements and assign them to their corresponding private variables.
            popularPeopleViewFlipper = (ViewFlipperIndicator) popularPeopleLayout.findViewById(
                    R.id.layout_main_cardview_content_viewflipper);
            popularPeopleMessage = (TextView) popularPeopleLayout.findViewById(
                    R.id.layout_main_cardview_content_message);
            popularPeopleLoadingIndicator = (ProgressBar) popularPeopleLayout.findViewById(
                    R.id.layout_main_cardview_content_loading_indicator);

            // Set ViewFlipper size.
            popularPeopleViewFlipper.setLayoutParams(posterRelativeLayoutParams);
        }

        onTheAirLayout = setCardView(onTheAirCardView, CONTENT_TYPE_SERIES, false,
                getString(R.string.tv_on_the_air), 4000);
        if (onTheAirLayout != null) {
            // Extract all layout elements and assign them to their corresponding private variables.
            onTheAirViewFlipper = (ViewFlipperIndicator) onTheAirLayout.findViewById(
                    R.id.layout_main_cardview_content_viewflipper);
            onTheAirMessage = (TextView) onTheAirLayout.findViewById(
                    R.id.layout_main_cardview_content_message);
            onTheAirLoadingIndicator = (ProgressBar) onTheAirLayout.findViewById(
                    R.id.layout_main_cardview_content_loading_indicator);

            // Set ViewFlipper size.
            onTheAirViewFlipper.setLayoutParams(posterRelativeLayoutParams);
        }

        // Now playing movies section.
        nowPlayingMoviesLayout = setCardView(nowPlayingMoviesCardView, CONTENT_TYPE_MOVIES,
                true, getString(R.string.movies_sort_by_now_playing), 5000);
        if (nowPlayingMoviesLayout != null) {
            // Extract all layout elements and assign them to their corresponding private variables.
            nowPlayingMoviesViewFlipper = (ViewFlipperIndicator) nowPlayingMoviesLayout.findViewById(
                    R.id.layout_main_cardview_content_viewflipper);
            nowPlayingMoviesMessageHeader = (TextView) nowPlayingMoviesLayout.findViewById(
                    R.id.layout_main_cardview_content_message_header);
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
                    // Set preferences for "Now Playing Movies" to show "In Theaters".
                    MyPreferences.setNowPlayingMoviesHow(MainActivity.this,
                            MyPreferences.MOVIES_NOW_PLAYING_HOW_THEATERS_INDEX);

                    // Set intent for calling MoviesActivity.
                    Intent intent = new Intent(MainActivity.this,
                            MoviesActivity.class);
                    intent.putExtra("sort", MoviesFragmentPagerAdapter.PAGE_NOW_PLAYING);
                    Bundle option = ActivityOptions
                            .makeSceneTransitionAnimation(MainActivity.this).toBundle();
                    startActivity(intent, option);
                }
            });
        }

        buyAndRentSeriesLayout = setCardView(buyAndRentSeriesCardView, CONTENT_TYPE_SERIES,
                false, getString(R.string.movies_sort_by_for_buy_and_rent),
                4000);
        if (buyAndRentSeriesLayout != null) {
            // Extract all layout elements and assign them to their corresponding private variables.
            buyAndRentSeriesViewFlipper = (ViewFlipperIndicator) buyAndRentSeriesLayout.findViewById(
                    R.id.layout_main_cardview_content_viewflipper);
            buyAndRentSeriesMessage = (TextView) buyAndRentSeriesLayout.findViewById(
                    R.id.layout_main_cardview_content_message);
            buyAndRentSeriesLoadingIndicator = (ProgressBar) buyAndRentSeriesLayout.findViewById(
                    R.id.layout_main_cardview_content_loading_indicator);

            // Set ViewFlipper size.
            buyAndRentSeriesViewFlipper.setLayoutParams(posterRelativeLayoutParams);
        }

        buyAndRentMoviesLayout = setCardView(buyAndRentMoviesCardView, CONTENT_TYPE_MOVIES,
                false, getString(R.string.movies_sort_by_for_buy_and_rent),
                4000);
        if (buyAndRentMoviesLayout != null) {
            // Extract all layout elements and assign them to their corresponding private variables.
            buyAndRentMoviesViewFlipper = (ViewFlipperIndicator) buyAndRentMoviesLayout.findViewById(
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
                    // "any date".
                    MyPreferences.setNowPlayingMoviesHow(MainActivity.this,
                            MyPreferences.MOVIES_NOW_PLAYING_HOW_DIGITAL_INDEX);

                    // Set intent for calling MoviesActivity.
                    Intent intent = new Intent(MainActivity.this,
                            MoviesActivity.class);
                    intent.putExtra("sort", MoviesFragmentPagerAdapter.PAGE_NOW_PLAYING);
                    Bundle option = ActivityOptions
                            .makeSceneTransitionAnimation(MainActivity.this).toBundle();
                    startActivity(intent, option);
                }
            });
        }

        airingTodayLayout = setCardView(airingTodayCardView, CONTENT_TYPE_SERIES, true,
                getString(R.string.tv_airing_today), 5000);
        if (airingTodayLayout != null) {
            // Extract all layout elements and assign them to their corresponding private variables.
            airingTodayViewFlipper = (ViewFlipperIndicator) airingTodayLayout.findViewById(
                    R.id.layout_main_cardview_content_viewflipper);
            airingTodayMessageHeader = (TextView) airingTodayLayout.findViewById(
                    R.id.layout_main_cardview_content_message_header);
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
        animatedViews.add(upcomingMoviesCardView);
        animatedViews.add(onTheAirCardView);
        animatedViews.add(popularPeopleCardView);
        animatedViews.add(nowPlayingMoviesCardView);
        animatedViews.add(buyAndRentMoviesCardView);
        animatedViews.add(buyAndRentSeriesCardView);
        animatedViews.add(airingTodayCardView);
        processAnimationQueue(
                AnimationUtils.loadAnimation(this, R.anim.in_from_left), 50);
    }

    /**
     * Helper method for creating a new view, inflated from the layout_main_cardview_content.xml
     * file, and making it the child of the given cardView.
     *
     * @param cardView     is the CardView in which the new child layout is going to be created.
     * @param contentType  is the content type to display into the CardView. Available values are
     *                     CONTENT_TYPE_MOVIES, CONTENT_TYPE_SERIES and CONTENT_TYPE_PEOPLE.
     * @param showControls is true if control elements (next, previous and play) for ViewFlipper
     *                     must be shown; false otherwise.
     * @param title        is the title for this section.
     * @param flipInterval is the flip interval duration in milliseconds.
     * @return the inflated view or null if there has happened something wrong.
     */
    private View setCardView(CardView cardView, int contentType, boolean showControls, String title,
                             int flipInterval) {
        LayoutInflater inflater =
                (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        try {
            // Inflate view to display info.
            View cardViewContent =
                    inflater.inflate(R.layout.layout_main_cardview_content, null);

            // Extract ViewFlipper from the current layout.
            final ViewFlipperIndicator viewFlipper = (ViewFlipperIndicator)
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

            // Set onClickListeners on control elements (initially hidden) if required.
            ImageView previousImage = (ImageView) cardViewContent.findViewById(
                    R.id.layout_main_cardview_content_previous);
            ImageView nextImage = (ImageView) cardViewContent.findViewById(
                    R.id.layout_main_cardview_content_next);
            final ImageView play = (ImageView) cardViewContent.findViewById(
                    R.id.layout_main_cardview_content_play);
            previousImage.setVisibility(View.GONE);
            nextImage.setVisibility(View.GONE);

            if (showControls) {
                // Show previous element of the ViewFlipper and stop auto flipping when clicking
                // on "previous" arrow.
                previousImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewFlipper.stopFlipping();
                        viewFlipper.setInAnimation(animationInFromLeft);
                        viewFlipper.setOutAnimation(animationOutFromLeft);
                        viewFlipper.showPrevious();
                        play.setVisibility(View.VISIBLE);
                    }
                });

                // Show next element of the ViewFlipper and stop auto flipping when clicking on
                // "next" arrow.
                nextImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewFlipper.stopFlipping();
                        viewFlipper.setInAnimation(animationInFromRight);
                        viewFlipper.setOutAnimation(animationOutFromRight);
                        viewFlipper.showNext();
                        play.setVisibility(View.VISIBLE);
                    }
                });

                // Restart auto flipping when clicking on "play" icon.
                play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewFlipper.setInAnimation(animationInFromRight);
                        viewFlipper.setOutAnimation(animationOutFromRight);
                        if (viewFlipper.getDisplayedChild() < viewFlipper.getChildCount())
                            viewFlipper.setDisplayedChild(viewFlipper.getDisplayedChild() + 1);
                        else
                            viewFlipper.setDisplayedChild(0);
                        viewFlipper.startFlipping();
                        play.setVisibility(View.INVISIBLE);
                    }
                });

                // Set global control images for previous and next navigation into ViewFlippers.
                if (title.equals(getString(R.string.tv_airing_today))) {
                    airingTodayPreviousImageView = previousImage;
                    airingTodayNextImageView = nextImage;
                } else if (title.equals(getString(R.string.movies_sort_by_now_playing))) {
                    nowPlayingMoviesPreviousImageView = previousImage;
                    nowPlayingMoviesNextImageView = nextImage;
                } else if (title.equals(getString(R.string.movies_sort_by_upcoming))) {
                    upcomingMoviesPreviousImageView = previousImage;
                    upcomingMoviesNextImageView = nextImage;
                }
            } else {
                // Hide control elements.
                previousImage.setVisibility(View.GONE);
                nextImage.setVisibility(View.GONE);
            }
            play.setVisibility(View.INVISIBLE);

            // Set texts and colors.
            TextView sectionTitleTextView = (TextView)
                    cardViewContent.findViewById(R.id.layout_main_cardview_content_section);
            sectionTitleTextView.setText(sectionTitle);
            sectionTitleTextView.setBackgroundTintList(colorStateListPrimary);
            TextViewUtils.setTintedCompoundDrawable(this, sectionTitleTextView,
                    TextViewUtils.DRAWABLE_RIGHT_INDEX, tintedCompoundDrawable, R.color.colorWhite,
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
            Log.e(TAG, "(setCardView) Error inflating view: " + e);
            return null;
        }
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
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation arg0) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                    }

                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        // Instantiate classes for opening new threads to retrieve data from TMDB
                        // after performing the animation associated to the corresponding view.
                        View currentView = animatedViews.get(animatedViewCurrentIndex);
                        if (currentView == upcomingMoviesCardView) {
                            // Displaying animation for upcoming movies section: fetch upcoming
                            // movies list from TMDB.
                            new MainActivity.MainActivityMoviesList(
                                    NetworkUtils.TMDB_UPCOMING_MOVIES_LOADER_ID);
                        } else if (currentView == nowPlayingMoviesCardView) {
                            // Displaying animation for now playing movies section: fetch now
                            // playing movies list from TMDB.
                            new MainActivity.MainActivityMoviesList(
                                    NetworkUtils.TMDB_NOW_PLAYING_MOVIES_LOADER_ID);
                        } else if (currentView == popularPeopleCardView) {
                            // Displaying animation for popular people section: fetch popular people
                            // list from TMDB.
                            new MainActivity.MainActivityPeopleList(
                                    NetworkUtils.TMDB_POPULAR_PEOPLE_LOADER_ID);
                        } else if (currentView == buyAndRentMoviesCardView) {
                            // Displaying animation for buy and rent movies section: fetch buy and
                            // rent movies list from TMDB.
                            new MainActivity.MainActivityMoviesList(
                                    NetworkUtils.TMDB_BUY_AND_RENT_MOVIES_LOADER_ID);
                        }

                        // Order animation for next view, if there's still another one in the queue.
                        animatedViewCurrentIndex++;
                        if (animatedViewCurrentIndex < animatedViews.size())
                            processAnimationQueue(animation, delayMillis);
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
     * main activity.
     *
     * @param data          is the array of elements to be shown.
     * @param viewFlipper   is the ViewFlipper that displays the info.
     * @param layoutParams  is the size for the views into the ViewFlipper.
     * @param loaderId      is the number that identifies the origin of the search, and
     *                      therefore the type of information that must be shown.
     * @param previousImage is the ImageView used for going back to the previous image in the
     *                      CardView.
     * @param nextImage     is the ImageView used for going forward to the next image in the
     *                      CardView.
     * @param fullScreen    if true, ViewFlipper takes full screen; if false, ViewFlipper takes
     *                      half screen.
     * @param maxElements   is the maximum number of elements to be displayed into the
     *                      ViewFlipper.
     * @return true if viewFlipper is displaying at least one element, false otherwise.
     */
    @SuppressWarnings("unchecked")
    private boolean inflateMoviesViewFlipperChildren(final ArrayList<?> data,
                                                     final ViewFlipper viewFlipper,
                                                     LinearLayout.LayoutParams layoutParams,
                                                     final int loaderId, ImageView previousImage,
                                                     ImageView nextImage, boolean fullScreen,
                                                     int maxElements) {
        viewFlipper.removeAllViews();

        // Add children to ViewFlipper, only the first maxElements elements and only for those
        // elements with a image to display.
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

                default: // case NetworkUtils.TMDB_POPULAR_PEOPLE_LOADER_ID:
                    name = ((ArrayList<TmdbPerson>) data).get(i).getName();
                    break;
            }

            if (!imagePath.equals("") && !imagePath.isEmpty() &&
                    !name.equals("") && !name.isEmpty()) {
                // Inflate view to display info.
                LayoutInflater inflater =
                        (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                try {
                    View view = inflater.inflate(R.layout.list_item_main, null);

                    // Load image file and set size.
                    ImageView imageView =
                            (ImageView) view.findViewById(R.id.list_item_main_image);
                    switch (loaderId) {
                        case NetworkUtils.TMDB_NOW_PLAYING_MOVIES_LOADER_ID:
                        case NetworkUtils.TMDB_UPCOMING_MOVIES_LOADER_ID:
                            imagePath = Tmdb.TMDB_POSTER_SIZE_W500_URL + imagePath;
                            break;

                        case NetworkUtils.TMDB_BUY_AND_RENT_MOVIES_LOADER_ID:
                        case NetworkUtils.TMDB_POPULAR_PEOPLE_LOADER_ID:
                            imagePath = Tmdb.TMDB_POSTER_SIZE_W185_URL + imagePath;
                            break;
                    }
                    Picasso.with(MainActivity.this)
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
                        case NetworkUtils.TMDB_NOW_PLAYING_MOVIES_LOADER_ID: {
                            // Set movie title.
                            titleTextView.setText(name);

                            // Show users rating.
                            String score = String.valueOf(((ArrayList<TmdbMovie>) data)
                                    .get(i).getVote_average());
                            ScoreUtils.setTextViewRating(
                                    MainActivity.this, score, usersRatingTextView);
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

                    // Set the onClickListener for navigating to the movie details activity when
                    // clicking on the entire view.
                    final int currentElement = i;
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Open a new activity to show detailed info about the
                            // current movie/series/person.
                            Intent intent;
                            Bundle option;
                            switch (loaderId) {
                                case NetworkUtils.TMDB_NOW_PLAYING_MOVIES_LOADER_ID:
                                case NetworkUtils.TMDB_UPCOMING_MOVIES_LOADER_ID:
                                case NetworkUtils.TMDB_BUY_AND_RENT_MOVIES_LOADER_ID:
                                    intent = new Intent(MainActivity.this,
                                            MovieDetailsActivity.class);
                                    intent.putExtra(MovieDetailsActivity.EXTRA_PARAM_MOVIE,
                                            ((ArrayList<TmdbMovie>) data).get(currentElement));
                                    option = ActivityOptions.makeSceneTransitionAnimation(
                                            MainActivity.this).toBundle();
                                    break;

                                default: // case NetworkUtils.TMDB_POPULAR_PEOPLE_LOADER_ID:
                                    intent = new Intent(MainActivity.this,
                                            PersonDetailsActivity.class);
                                    TmdbPerson person = new TmdbPerson(
                                            ((ArrayList<TmdbPerson>) data).get(currentElement).getId(),
                                            ((ArrayList<TmdbPerson>) data).get(currentElement).getName(),
                                            ((ArrayList<TmdbPerson>) data).get(currentElement).getProfile_path());
                                    intent.putExtra(PersonDetailsActivity.EXTRA_PARAM_PERSON, person);
                                    option = ActivityOptions.makeSceneTransitionAnimation(
                                            MainActivity.this).toBundle();
                                    break;
                            }
                            startActivity(intent, option);
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
            if (!viewFlipper.isFlipping()) {
                // Start ViewFlipper animation.
                viewFlipper.startFlipping();
            }

            switch (loaderId) {
                case NetworkUtils.TMDB_NOW_PLAYING_MOVIES_LOADER_ID:
                case NetworkUtils.TMDB_UPCOMING_MOVIES_LOADER_ID:
                    // Make controls visible.
                    previousImage.setVisibility(View.VISIBLE);
                    nextImage.setVisibility(View.VISIBLE);
                    break;
            }

            return true;
        } else
            return false;
    }

    /* ------------- */
    /* INNER CLASSES */
    /* ------------- */

    // Private inner class to retrieve a given list of movies.
    private class MainActivityMoviesList
            implements LoaderManager.LoaderCallbacks<ArrayList<TmdbMovie>> {
        private final String TAG = MainActivity.MainActivityMoviesList.class.getSimpleName();

        // Constructor for objects of this class.
        MainActivityMoviesList(int loaderId) {
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
        public Loader<ArrayList<TmdbMovie>> onCreateLoader(int id, Bundle args) {
            Context context = MainActivity.this;
            switch (id) {
                case NetworkUtils.TMDB_UPCOMING_MOVIES_LOADER_ID: {
                    upcomingMoviesLoadingIndicator.setVisibility(View.VISIBLE);
                    if (NetworkUtils.isConnected(MainActivity.this)) {
                        // There is an available connection. Fetch upcoming movies (in theaters and
                        // released any date later than tomorrow) from TMDB.
                        upcomingMoviesMessageHeader.setVisibility(View.GONE);
                        upcomingMoviesMessage.setVisibility(View.GONE);
                        String moviesUpcomingReleaseType = getResources().getStringArray(
                                R.array.preferences_upcoming_movies_how_values_array)
                                [MyPreferences.MOVIES_UPCOMING_HOW_THEATERS_INDEX];
                        String moviesUpcomingInitDate = DateTimeUtils.getStringAddedDaysToDate(
                                DateTimeUtils.getCurrentDate(), 1);
                        return new TmdbMoviesAsyncTaskLoader(context,
                                Tmdb.TMDB_CONTENT_TYPE_UPCOMING, null,
                                1, language, region, moviesSortBy,
                                moviesCertification, moviesVoteCount, moviesVoteAverage,
                                moviesUpcomingReleaseType, moviesUpcomingInitDate,
                                "");
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
                        // There is an available connection. Fetch Fetch now playing movies (in
                        // theaters, released 45 days ago or later) from TMDB.
                        nowPlayingMoviesMessage.setVisibility(View.GONE);
                        String moviesNowPlayingReleaseType = getResources().getStringArray(
                                R.array.preferences_now_playing_movies_how_values_array)
                                [MyPreferences.MOVIES_NOW_PLAYING_HOW_THEATERS_INDEX];
                        String moviesNowPlayingInitDate = DateTimeUtils.getStringAddedDaysToDate(
                                DateTimeUtils.getCurrentDate(), -45);
                        String moviesNowPlayingEndDate = DateTimeUtils.getStringCurrentDate();
                        return new TmdbMoviesAsyncTaskLoader(context,
                                Tmdb.TMDB_CONTENT_TYPE_NOW_PLAYING, null,
                                1, language, region, moviesSortBy,
                                moviesCertification, moviesVoteCount, moviesVoteAverage,
                                moviesNowPlayingReleaseType, moviesNowPlayingInitDate,
                                moviesNowPlayingEndDate);
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

                case NetworkUtils.TMDB_BUY_AND_RENT_MOVIES_LOADER_ID: {
                    buyAndRentMoviesLoadingIndicator.setVisibility(View.VISIBLE);
                    if (NetworkUtils.isConnected(MainActivity.this)) {
                        // There is an available connection. Fetch buy and rent movies (released in
                        // digital platforms, any date) from TMDB.
                        buyAndRentMoviesMessage.setVisibility(View.GONE);
                        String buyAndRentMoviesReleaseType = getResources().getStringArray(
                                R.array.preferences_now_playing_movies_how_values_array)
                                [MyPreferences.MOVIES_NOW_PLAYING_HOW_DIGITAL_INDEX];
                        String buyAndRentMoviesEndDate = DateTimeUtils.getStringCurrentDate();
                        return new TmdbMoviesAsyncTaskLoader(context,
                                Tmdb.TMDB_CONTENT_TYPE_FOR_BUY_AND_RENT, null,
                                1, language, region, moviesSortBy,
                                moviesCertification, moviesVoteCount, moviesVoteAverage,
                                buyAndRentMoviesReleaseType, "",
                                buyAndRentMoviesEndDate);
                    } else {
                        // There is no connection. Show error message.
                        buyAndRentMoviesMessage.setText(
                                getResources().getString(R.string.no_connection));
                        buyAndRentMoviesMessage.setVisibility(View.VISIBLE);
                        buyAndRentMoviesLoadingIndicator.setVisibility(View.GONE);
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
            Context context = MainActivity.this;

            // Get movies list and display it, depending on the loader identifier.
            switch (loader.getId()) {
                case NetworkUtils.TMDB_UPCOMING_MOVIES_LOADER_ID: {
                    upcomingMoviesLoadingIndicator.setVisibility(View.GONE);

                    // Check if there is an available connection.
                    if (NetworkUtils.isConnected(MainActivity.this)) {
                        // If there is a valid result, display it on its corresponding layout.
                        boolean hasData = true, hasValidData = true;
                        if (data != null && data.size() > 0) {
                            Log.i(TAG, "(onLoadFinished) Search results for upcoming movies not null.");
                            upcomingMoviesMessageHeader.setVisibility(View.GONE);
                            upcomingMoviesMessage.setVisibility(View.GONE);
                            hasValidData = inflateMoviesViewFlipperChildren(data,
                                    upcomingMoviesViewFlipper, backdropLinearLayoutParams,
                                    loader.getId(), upcomingMoviesPreviousImageView,
                                    upcomingMoviesNextImageView, true,
                                    MAX_ELEMENTS_FULL_SCREEN);
                        } else {
                            Log.i(TAG, "(onLoadFinished) No search results for upcoming movies.");
                            hasData = false;
                        }
                        if (!hasData || !hasValidData) {
                            // There's no search results in the movies array or there's no elements
                            // into the ViewFlipper. Show alert messages.
                            upcomingMoviesMessageHeader.setText(getResources().getString(
                                    R.string.no_results));
                            String filters = MyPreferences.getUpcomingMoviesHowTitle(context) + ", "
                                    + MyPreferences.getUpcomingMoviesWhenTitle(context) + ", "
                                    + MyPreferences.getUpcomingMoviesWhereTitle(context);
                            String htmlText = String.format(getResources().getString(
                                    R.string.no_movies_results), filters);
                            TextViewUtils.setHtmlText(upcomingMoviesMessage, htmlText);
                            upcomingMoviesMessageHeader.setVisibility(View.VISIBLE);
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

                case NetworkUtils.TMDB_NOW_PLAYING_MOVIES_LOADER_ID: {
                    nowPlayingMoviesLoadingIndicator.setVisibility(View.GONE);

                    // Check if there is an available connection.
                    if (NetworkUtils.isConnected(MainActivity.this)) {
                        // If there is a valid result, display it on its corresponding layout.
                        boolean hasData = true, hasValidData = true;
                        if (data != null && data.size() > 0) {
                            Log.i(TAG, "(onLoadFinished) Search results for now playing movies not null.");
                            nowPlayingMoviesMessageHeader.setVisibility(View.GONE);
                            nowPlayingMoviesMessage.setVisibility(View.GONE);
                            hasValidData = inflateMoviesViewFlipperChildren(data,
                                    nowPlayingMoviesViewFlipper, backdropLinearLayoutParams,
                                    loader.getId(), nowPlayingMoviesPreviousImageView,
                                    nowPlayingMoviesNextImageView, true,
                                    MAX_ELEMENTS_FULL_SCREEN);
                        } else {
                            Log.i(TAG, "(onLoadFinished) No search results for now playing movies.");
                            hasData = false;
                        }
                        if (!hasData || !hasValidData) {
                            // There's no search results in the movies array or there's no elements
                            // into the ViewFlipper. Show alert messages.
                            nowPlayingMoviesMessageHeader.setText(getResources().getString(
                                    R.string.no_results));
                            String filters = MyPreferences.getNowPlayingMoviesHowTitle(context);
                            String htmlText = String.format(getResources().getString(
                                    R.string.no_movies_results), filters);
                            TextViewUtils.setHtmlText(nowPlayingMoviesMessage, htmlText);
                            nowPlayingMoviesMessageHeader.setVisibility(View.VISIBLE);
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

                case NetworkUtils.TMDB_BUY_AND_RENT_MOVIES_LOADER_ID: {
                    buyAndRentMoviesLoadingIndicator.setVisibility(View.GONE);

                    // Check if there is an available connection.
                    if (NetworkUtils.isConnected(MainActivity.this)) {
                        boolean hasData = true, hasValidData = true;
                        if (data != null && data.size() > 0) {
                            // If there is a valid result, display it on its corresponding layout.
                            Log.i(TAG, "(onLoadFinished) Search results for buy and rent movies not null.");
                            buyAndRentMoviesMessage.setVisibility(View.GONE);
                            hasValidData = inflateMoviesViewFlipperChildren(data,
                                    buyAndRentMoviesViewFlipper, posterLinearLayoutParams,
                                    loader.getId(), null, null,
                                    false, MAX_HALF_SCREEN_ELEMENTS);
                        } else {
                            Log.i(TAG, "(onLoadFinished) No search results for buy and rent movies.");
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
                        Log.i(TAG, "(onLoadFinished) No connection to internet.");
                        buyAndRentMoviesMessage.setText(
                                getResources().getString(R.string.no_connection));
                        buyAndRentMoviesMessage.setVisibility(View.VISIBLE);
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
                        if (data != null && data.size() > 0) {
                            Log.i(TAG, "(onLoadFinished) Search results not null.");
                            inflateMoviesViewFlipperChildren(data, popularPeopleViewFlipper,
                                    posterLinearLayoutParams, loader.getId(), null,
                                    null, false, MAX_HALF_SCREEN_ELEMENTS);
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
    }
}
