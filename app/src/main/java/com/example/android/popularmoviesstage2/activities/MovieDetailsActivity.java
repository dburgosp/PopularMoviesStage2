package com.example.android.popularmoviesstage2.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.classes.MyBounceInterpolator;
import com.example.android.popularmoviesstage2.classes.Tmdb;
import com.example.android.popularmoviesstage2.classes.TmdbMovie;
import com.example.android.popularmoviesstage2.fragmentpageradapters.MovieDetailsFragmentPagerAdapter;
import com.example.android.popularmoviesstage2.utils.AnimatedViewsUtils;
import com.example.android.popularmoviesstage2.utils.DateTimeUtils;
import com.example.android.popularmoviesstage2.utils.DisplayUtils;
import com.example.android.popularmoviesstage2.utils.ScoreUtils;
import com.example.android.popularmoviesstage2.utils.TextViewUtils;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MovieDetailsActivity extends AppCompatActivity {
    private static final String TAG = MovieDetailsActivity.class.getSimpleName();
    public static final String EXTRA_PARAM_MOVIE = "movie";
    public static final String EXTRA_PARAM_CALLING_MOVIE_TITLE = "title";

    // Annotate fields with @BindView and views ID for Butter Knife to find and automatically cast
    // the corresponding views.
    @BindView(R.id.movie_details_background)
    ImageView backgroundImageView;
    @BindView(R.id.movie_details_gradient)
    ImageView gradientImageView;
    @BindView(R.id.movie_details_poster)
    ImageView posterImageView;
    @BindView(R.id.movie_details_title)
    TextView titleTextView;
    @BindView(R.id.movie_details_score_votes)
    TextView votesTextView;
    @BindView(R.id.movie_details_score_title)
    TextView scoreTextView;
    @BindView(R.id.movie_details_cardview)
    CardView posterCardView;
    @BindView(R.id.movie_details_viewpager)
    ViewPager viewPager;
    @BindView(R.id.movie_details_tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.movie_details_score)
    DonutProgress scoreDonutProgress;
    @BindView(R.id.movie_details_toolbar)
    Toolbar toolbar;
    @BindView(R.id.movie_details_collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.movie_details_appbar_layout)
    AppBarLayout appBarLayout;

    private static TmdbMovie movie;
    private static Unbinder unbinder;
    private AppBarLayout.OnOffsetChangedListener onOffsetChangedListener;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        super.onCreate(savedInstanceState);

        AnimatedViewsUtils.setTransitions(getWindow());

        setContentView(R.layout.activity_movie_details);
        unbinder = ButterKnife.bind(this);

        // Get parameters from intent.
        Intent intent = getIntent();
        if (intent != null) {
            // Get current movie simple information.
            movie = intent.getParcelableExtra(EXTRA_PARAM_MOVIE);
        }

        // Write the basic movie info on activity_movie_details.xml header.
        setMovieInfo();

        // Set the custom tool bar and show the back button.
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set title for this activity, depending on the collapsing state of the toolbar.
        setTitle("");
        onOffsetChangedListener = new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (collapsingToolbarLayout != null) {
                    if (collapsingToolbarLayout.getHeight() + verticalOffset <
                            2 * ViewCompat.getMinimumHeight(collapsingToolbarLayout)) {
                        // CollapsingToolbar is collapsed. Show title.
                        CharSequence oldTitle = collapsingToolbarLayout.getTitle();
                        String title = movie.getTitle();
                        String year = DateTimeUtils.getYear(movie.getRelease_date());
                        if (title != null && !title.equals("") && !title.isEmpty()) {
                            // There is a title defined for current movie.
                            if (year != null && !year.equals("") && !year.isEmpty())
                                title = title + " (" + year + ")";
                            if (oldTitle != null && !oldTitle.equals("")) {
                                // Avoid setting wrong title from another MovieDetailsActivity. If
                                // there was already a title defined for this activity, reuse it.
                                collapsingToolbarLayout.setTitle(oldTitle);
                            } else
                                collapsingToolbarLayout.setTitle(title);
                        } else {
                            // No title for current movie. Write default "no title" string.
                            collapsingToolbarLayout.setTitle(getResources().getString(
                                    R.string.no_title));
                        }
                    } else {
                        // CollapsingToolbar is expanded. Hide title.
                        collapsingToolbarLayout.setTitle("");
                    }
                }
            }
        };
        appBarLayout.addOnOffsetChangedListener(onOffsetChangedListener);

        // Set TabLayout and ViewPager to manage the fragments with info for the current movie.
        tabLayout.setupWithViewPager(viewPager);
        MovieDetailsFragmentPagerAdapter adapter =
                new MovieDetailsFragmentPagerAdapter(getSupportFragmentManager(),
                        MovieDetailsActivity.this, movie);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);

        Log.i(TAG + "." + methodName, "Activity created");
    }

    /**
     * This method is called whenever the user chooses to navigate Up within your application's
     * activity hierarchy from the action bar.
     * <p>
     * <p>If a parent was specified in the manifest for this activity or an activity-alias to it,
     * default Up navigation will be handled automatically. See
     * {@link #getSupportParentActivityIntent()} for how to specify the parent. If any activity
     * along the parent chain requires extra Intent arguments, the Activity subclass
     * should override the method onPrepareSupportNavigateUpTaskStack(TaskStackBuilder)
     * to supply those arguments.</p>
     * <p>
     * <p>See <a href="{@docRoot}guide/topics/fundamentals/tasks-and-back-stack.html">Tasks and
     * Back Stack</a> from the developer guide and
     * <a href="{@docRoot}design/patterns/navigation.html">Navigation</a> from the design guide
     * for more information about navigating within your app.</p>
     * <p>
     * <p>See the TaskStackBuilder class and the Activity methods
     * {@link #getSupportParentActivityIntent()}, {@link #supportShouldUpRecreateTask(Intent)}, and
     * {@link #supportNavigateUpTo(Intent)} for help implementing custom Up navigation.</p>
     *
     * @return true if Up navigation completed successfully and this Activity was finished,
     * false otherwise.
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        appBarLayout.removeOnOffsetChangedListener(onOffsetChangedListener);
        Intent returnIntent = getIntent();
        setResult(Activity.RESULT_CANCELED, returnIntent);

        // Hide background with animation.
        AnimatedViewsUtils.circularUnrevealLayout(backgroundImageView,
                backgroundImageView.getMeasuredWidth() / 2,
                backgroundImageView.getMeasuredHeight() / 2, null,
                AnimatedViewsUtils.ANIM_DURATION_LONG);

        // Transition back to the movie poster into the calling activity.
        supportFinishAfterTransition();

        super.onBackPressed();
    }

    /**
     * Dispatch incoming result to the correct fragment.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String title = data.getStringExtra(EXTRA_PARAM_CALLING_MOVIE_TITLE);
        movie.setTitle(title);
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Initialize the contents of the Activity's standard options menu.  You
     * should place your menu items in to <var>menu</var>.
     * <p>
     * <p>This is only called once, the first time the options menu is
     * displayed.  To update the menu every time it is displayed, see
     * {@link #onPrepareOptionsMenu}.
     * <p>
     * <p>The default implementation populates the menu with standard system
     * menu items.  These are placed in the {@link Menu#CATEGORY_SYSTEM} group so that
     * they will be correctly ordered with application-defined menu items.
     * Deriving classes should always call through to the base implementation.
     * <p>
     * <p>You can safely hold on to <var>menu</var> (and any items created
     * from it), making modifications to it as desired, until the next
     * time onCreateOptionsMenu() is called.
     * <p>
     * <p>When you add items to the menu, you can implement the Activity's
     * {@link #onOptionsItemSelected} method to handle them there.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                // Back button.
                onBackPressed();
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* -------------- */
    /* HELPER METHODS */
    /* -------------- */

    /**
     * Helper method to display current movie information in the header of this activity.
     */
    private void setMovieInfo() {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        Log.i(TAG + "." + methodName, "Display movie information on the header");

        // Set background image. Also set its size, according to display measures, and its
        // transition between the backdrop in calling activity and this. The background is invisible
        // by default and it will be shown later.
        backgroundImageView.setVisibility(View.INVISIBLE);
        final DisplayUtils displayUtils = new DisplayUtils(MovieDetailsActivity.this);
        int backdropWidthPixels = displayUtils.getFullDisplayBackdropWidthPixels();
        int backdropHeightPixels = (backdropWidthPixels * 9 / 16);
        CollapsingToolbarLayout.LayoutParams layoutParams =
                new CollapsingToolbarLayout.LayoutParams(backdropWidthPixels, backdropHeightPixels);
        backgroundImageView.setLayoutParams(layoutParams);
        String backdropPath = movie.getBackdrop_path();
        Picasso.with(this)
                .load(Tmdb.TMDB_POSTER_SIZE_W300_URL + backdropPath)
                .noFade()
                .resize(backdropWidthPixels, backdropHeightPixels)
                .centerCrop()
                .into(backgroundImageView);

        // Set poster. Also set transition between the poster in calling activity and this.
        posterImageView.setVisibility(View.INVISIBLE);
        String posterPath = Tmdb.TMDB_POSTER_SIZE_W185_URL + movie.getPoster_path();
        posterImageView.setTransitionName(getString(R.string.transition_poster));
        postponeEnterTransition();
        Picasso.with(this)
                .load(posterPath)
                .noFade()
                .fit()
                .centerCrop()
                .into(posterImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        posterImageView.getViewTreeObserver().addOnPreDrawListener(
                                new ViewTreeObserver.OnPreDrawListener() {
                                    @Override
                                    public boolean onPreDraw() {
                                        posterImageView.setVisibility(View.VISIBLE);
                                        posterImageView.getViewTreeObserver()
                                                .removeOnPreDrawListener(this);
                                        startPostponedEnterTransition();

                                        // Show background with a circular reveal animation.
                                        //AnimatedViewsUtils.enterReveal(backgroundImageView);
                                        AnimatedViewsUtils.circularRevealLayout(backgroundImageView,
                                                backgroundImageView.getMeasuredWidth() / 2,
                                                backgroundImageView.getMeasuredHeight() / 2,
                                                AnimatedViewsUtils.ANIM_DURATION_LONG);
                                        return true;
                                    }
                                });
                    }

                    @Override
                    public void onError() {
                        posterImageView.setVisibility(View.VISIBLE);
                        startPostponedEnterTransition();
                    }
                });

        // Set movie title and year. Hide text by default and show it using an animation.
        titleTextView.setVisibility(View.INVISIBLE);
        String title = movie.getTitle();
        String year = DateTimeUtils.getYear(movie.getRelease_date());
        if (title != null && !title.equals("") && !title.isEmpty())
            if (year != null && !year.equals("") && !year.isEmpty()) {
                int labelColor = getResources().getColor(R.color.colorGrey);
                String color = String.format("%X", labelColor).substring(2);
                TextViewUtils.setHtmlText(titleTextView, "<strong><big>" + title +
                        " </big></strong><small><font color=\"#" + color + "\">(" + year +
                        ")</font></small>");
            } else
                TextViewUtils.setHtmlText(titleTextView, "<strong><big>" + title +
                        "</big></strong>");
        else
            titleTextView.setText(getResources().getString(R.string.no_title));
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.in_from_right);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        animation.setInterpolator(interpolator);
        titleTextView.setVisibility(View.VISIBLE);
        titleTextView.startAnimation(animation);

        // Set users rating.
        String rating = String.valueOf(movie.getVote_average());
        if (!rating.equals("0.0")) {
            // Set custom progress bar.
            ScoreUtils.setDonutProgressRating(this, rating, scoreDonutProgress);
            scoreDonutProgress.setVisibility(View.VISIBLE);

            // Set number of votes with decimal separator according to the current regional
            // configuration.
            int votes = movie.getVote_count();
            if (votes > 0) {
                NumberFormat numberFormat = NumberFormat.getNumberInstance();
                votesTextView.setText(getResources().getQuantityString(R.plurals.score_votes, votes,
                        numberFormat.format(votes)));
            } else {
                votesTextView.setVisibility(View.GONE);
            }
        } else {
            // Clear and hide score section if we get "0.0", which means that there is no score for
            // the current movie.
            scoreTextView.setVisibility(View.GONE);
            votesTextView.setVisibility(View.GONE);
            scoreDonutProgress.setVisibility(View.GONE);
            scoreDonutProgress.setDonut_progress("0");
        }
    }
}
