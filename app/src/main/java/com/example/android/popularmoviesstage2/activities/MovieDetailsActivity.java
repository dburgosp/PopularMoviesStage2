package com.example.android.popularmoviesstage2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.classes.Tmdb;
import com.example.android.popularmoviesstage2.classes.TmdbMovie;
import com.example.android.popularmoviesstage2.fragmentpageradapters.MovieDetailsFragmentPagerAdapter;
import com.example.android.popularmoviesstage2.utils.DateTimeUtils;
import com.example.android.popularmoviesstage2.utils.ScoreUtils;
import com.example.android.popularmoviesstage2.utils.TextViewUtils;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MovieDetailsActivity extends AppCompatActivity {
    private static final String TAG = MovieDetailsActivity.class.getSimpleName();
    public static final String EXTRA_PARAM_MOVIE = "movie";

    // Annotate fields with @BindView and views ID for Butter Knife to find and automatically cast
    // the corresponding views.
    @BindView(R.id.movie_details_background)
    ImageView backgroundImageView;
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
    private Bundle options;
    private static Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Define transitions to exit and enter to this activity.
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setBackgroundDrawableResource(R.color.colorPrimaryDark);
        getWindow().setEnterTransition(new Explode());
        getWindow().setExitTransition(new Explode());
        supportPostponeEnterTransition();

        setContentView(R.layout.activity_movie_details);
        unbinder = ButterKnife.bind(this);

        // Get parameters from intent.
        Intent intent = getIntent();
        if (intent != null) {
            // Get current movie simple information.
            movie = intent.getParcelableExtra(EXTRA_PARAM_MOVIE);
        }

        // Set the custom tool bar and show the back button.
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set title for this activity, depending on the collapsing state of the toolbar.
        setTitle("");
        AppBarLayout.OnOffsetChangedListener listener = new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (collapsingToolbarLayout.getHeight() + verticalOffset <
                        2 * ViewCompat.getMinimumHeight(collapsingToolbarLayout)) {
                    // CollapsingToolbar is collapsed. Show title.
                    String title = movie.getTitle();
                    String year = DateTimeUtils.getYear(movie.getRelease_date());
                    if (title != null && !title.equals("") && !title.isEmpty())
                        if (year != null && !year.equals("") && !year.isEmpty())
                            collapsingToolbarLayout.setTitle(movie.getTitle() + " (" +
                                    DateTimeUtils.getYear(movie.getRelease_date()) + ")");
                        else
                            collapsingToolbarLayout.setTitle(movie.getTitle());
                    else
                        collapsingToolbarLayout.setTitle(getResources().getString(R.string.no_title));
                } else {
                    // CollapsingToolbar is expanded. Hide title.
                    collapsingToolbarLayout.setTitle("");
                }
            }
        };
        appBarLayout.addOnOffsetChangedListener(listener);

        // Write the basic movie info on activity_movie_details.xml header.
        setMovieInfo();

        // Set TabLayout and ViewPager to manage the fragments with info for the current movie.
        tabLayout.setupWithViewPager(viewPager);
        MovieDetailsFragmentPagerAdapter adapter =
                new MovieDetailsFragmentPagerAdapter(getSupportFragmentManager(),
                MovieDetailsActivity.this, movie);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);

        Log.i(TAG, "(onCreate) Activity created");
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        posterImageView = null;
        backgroundImageView = null;
    }

    /**
     * This method is called after {@link #onStart} when the activity is
     * being re-initialized from a previously saved state, given here in
     * <var>savedInstanceState</var>.  Most implementations will simply use {@link #onCreate}
     * to restore their state, but it is sometimes convenient to do it here
     * after all of the initialization has been done or to allow subclasses to
     * decide whether to use your default implementation.  The default
     * implementation of this method performs a restore of any view state that
     * had previously been frozen by {@link #onSaveInstanceState}.
     * <p>
     * <p>This method is called between {@link #onStart} and
     * {@link #onPostCreate}.
     *
     * @param savedInstanceState the data most recently supplied in {@link #onSaveInstanceState}.
     * @see #onCreate
     * @see #onPostCreate
     * @see #onResume
     * @see #onSaveInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
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
        // Transition back to the movie poster on MainActivity.
        supportFinishAfterTransition();
        return true;
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
        getMenuInflater().inflate(R.menu.details, menu);
        return true;
    }

    /* -------------- */
    /* HELPER METHODS */
    /* -------------- */

    /**
     * Helper method to display current movie information in the header of this activity.
     */
    void setMovieInfo() {
        Log.i(TAG, "(setPersonInfo) Display movie information on the header");

        // Set poster, if it exists.
        String posterPath = movie.getPoster_path();
        posterImageView.setTransitionName(getString(R.string.transition_list_to_details));
        if (posterPath != null && !posterPath.equals("") && !posterPath.isEmpty()) {
            // Set transition between the poster in MainActivity and this.
            Picasso.with(this)
                    .load(Tmdb.TMDB_POSTER_SIZE_W185_URL + posterPath)
                    .noFade()
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(posterImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            supportStartPostponedEnterTransition();
                        }

                        @Override
                        public void onError() {
                            supportStartPostponedEnterTransition();
                        }
                    });
        }

        // Set background image, if it exists.
        String backdropPath = movie.getBackdrop_path();
        if (backdropPath != null && !backdropPath.equals("") && !backdropPath.isEmpty())
            Picasso.with(this)
                    .load(Tmdb.TMDB_POSTER_SIZE_W500_URL + backdropPath)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(backgroundImageView);

        // Set movie title and year.
        String title = movie.getTitle();
        String year = DateTimeUtils.getYear(movie.getRelease_date());
        if (title != null && !title.equals("") && !title.isEmpty())
            if (year != null && !year.equals("") && !year.isEmpty()) {
                int labelColor = getResources().getColor(R.color.colorGrey);
                String сolorString = String.format("%X", labelColor).substring(2);
                TextViewUtils.setHtmlText(titleTextView, "<strong><big>" + title + " </big></strong>" +
                        "<small><font color=\"#" + сolorString + "\">(" + year + ")</font></small>");
            } else
                TextViewUtils.setHtmlText(titleTextView, "<strong><big>" + title + "</big></strong>");
        else
            titleTextView.setText(getResources().getString(R.string.no_title));

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
                votesTextView.setText(getResources().getQuantityString(R.plurals.score_votes, votes, numberFormat.format(votes)));
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
