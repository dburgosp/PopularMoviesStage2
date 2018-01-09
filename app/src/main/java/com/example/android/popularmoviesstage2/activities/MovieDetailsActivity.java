package com.example.android.popularmoviesstage2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.classes.Movie;
import com.example.android.popularmoviesstage2.fragmentpageradapters.MovieDetailsFragmentPagerAdapter;
import com.example.android.popularmoviesstage2.utils.DateTimeUtils;
import com.example.android.popularmoviesstage2.utils.NetworkUtils;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailsActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static Movie movie;

    // Annotate fields with @BindView and views ID for Butter Knife to find and automatically cast
    // the corresponding views.
    @BindView(R.id.movie_details_background)
    ImageView movieDetailsBackground;
    @BindView(R.id.movie_details_poster)
    ImageView movieDetailsPoster;
    @BindView(R.id.movie_details_title)
    TextView movieDetailsTitle;
    @BindView(R.id.movie_details_year)
    TextView movieDetailsReleaseYear;
    @BindView(R.id.movie_details_cardview)
    CardView posterCardView;
    @BindView(R.id.movie_details_viewpager)
    ViewPager viewPager;
    @BindView(R.id.movie_details_tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.movie_details_score_progress)
    DonutProgress userScore;
    @BindView(R.id.movie_details_toolbar)
    Toolbar toolbar;

    private String sortOrder, sortOrderText;
    private int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        // Get parameters from intent.
        getParameters();

        // Set title for this activity.
        //this.setTitle(sortOrderText);
        this.setTitle("");

        // Write the basic movie info on activity_movie_details.xml header.
        setMovieInfo();

        // Give the TabLayout the ViewPager.
        tabLayout.setupWithViewPager(viewPager);

        // Create an adapter that knows which fragment should be shown on each page, set the adapter
        // onto the view pager and go to the current page.
        MovieDetailsFragmentPagerAdapter adapter = new MovieDetailsFragmentPagerAdapter(getSupportFragmentManager(), MovieDetailsActivity.this, movie);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);

        Log.i(TAG, "(onCreate) Activity created");
    }

    /**
     * Helper method to extract parameters from the intent that started this activity.
     */
    void getParameters() {
        Intent intent = getIntent();

        // Get current sort order for the movie list, in order to pass it back to MainActivity when
        // pressing "back" button.
        sortOrder = intent.getStringExtra("sortOrder");

        // Get sort order string for setting the title of this activity.
        sortOrderText = intent.getStringExtra("sortOrderText");

        // Get current movie simple information.
        movie = intent.getParcelableExtra("movie");
    }

    /**
     * Helper method to display current movie information in the header of this activity.
     */
    void setMovieInfo() {
        Log.i(TAG, "(setMovieInfo) Display movie information on the header");

        // Set poster, if it exists.
        String posterPath = movie.getPoster_path();
        if (posterPath != null && !posterPath.equals("") && !posterPath.isEmpty())
            Picasso.with(this).load(NetworkUtils.THUMBNAIL_IMAGE_URL + posterPath).into(movieDetailsPoster);

        // Set background image, if it exists.
        String backdropPath = movie.getBackdrop_path();
        if (backdropPath != null && !backdropPath.equals("") && !backdropPath.isEmpty())
            Picasso.with(this).load(NetworkUtils.FULL_IMAGE_URL + backdropPath).into(movieDetailsBackground);

        // Set title. Otherwise, we show the default text for title.
        String title = movie.getTitle();
        if (title != null && !title.equals("") && !title.isEmpty())
            movieDetailsTitle.setText(title);
        else
            movieDetailsTitle.setText(getResources().getString(R.string.no_title));

        // Set release year. Otherwise, we hide the corresponding section.
        String year = DateTimeUtils.getYear(movie.getRelease_date());
        if (year != null && !year.equals("") && !year.isEmpty()) {
            movieDetailsReleaseYear.setText(year);
            movieDetailsReleaseYear.setVisibility(View.VISIBLE);
        } else
            movieDetailsReleaseYear.setVisibility(View.INVISIBLE);

        // Set users rating.
        String rating = String.valueOf(movie.getVote_average());
        if (!rating.equals("0.0")) {
            // Set custom progress bar.
            int scorePercent = Math.round(10 * Float.parseFloat(rating));
            userScore.setText(rating);
            userScore.setDonut_progress(Integer.toString(scorePercent));
            userScore.setVisibility(View.VISIBLE);
        } else {
            // Clear and hide score section if we get "0.0", which means that there is no score for
            // the current movie.
            userScore.setVisibility(View.INVISIBLE);
            userScore.setDonut_progress("0");
        }
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
        // Back to parent activity with extra data.
        /*
        Intent returnIntent = new Intent(this, MainActivity.class);
        returnIntent.putExtra("sortOrder", sortOrder);
        returnIntent.putExtra("currentPosition", currentPosition);
        this.startActivity(returnIntent);
        */
        finish();
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
}
