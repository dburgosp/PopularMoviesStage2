package com.example.android.popularmoviesstage2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.popularmoviesstage2.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetails extends AppCompatActivity {
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
    //@BindView(R.id.movie_details_overview) TextView movieDetailsOverview;
    @BindView(R.id.movie_details_score)
    TextView movieDetailsScore;
    //@BindView(R.id.movie_details_score_title) TextView movieDetailsScoreTitle;
    @BindView(R.id.movie_details_cardview)
    CardView posterCardView;
    @BindView(R.id.movie_details_viewpager)
    ViewPager viewPager;
    @BindView(R.id.movie_details_tab_layout)
    TabLayout tabLayout;
    private String sortOrder;
    private int currentPosition;

    public static Movie getMovieInfo() {
        return movie;
    }

    /**
     * Helper method to display current movie information.
     */
    void setMovieInfo(Movie movie) {
        Log.i(TAG, "(setMovieInfo) Display movie information");

        // Set images.
        String posterPath = movie.getPosterPath();
        if (!posterPath.equals("")) {
            Picasso.with(this).load(NetworkUtils.FULL_IMAGE_URL + posterPath).into(movieDetailsBackground);
            Picasso.with(this).load(NetworkUtils.THUMBNAIL_IMAGE_URL + posterPath).into(movieDetailsPoster);
        }

        // Set title and release year. If there is no title, we show the default text for title.
        String title = movie.getTitle();
        String year = getYear(movie.getReleaseDate());
        if (!title.equals(""))
            if (!year.equals(""))
                movieDetailsTitle.setText(Html.fromHtml("<strong>" + title + "</strong> <small>(" + year + ")</small>"));
            else
                movieDetailsTitle.setText(Html.fromHtml("<strong>" + title + "</strong>"));

        // Set users rating.
        String rating = String.valueOf(movie.getVoteAverage());
        if (rating.equals("0.0")) {
            //movieDetailsScoreTitle.setVisibility(View.INVISIBLE);
            movieDetailsScore.setVisibility(View.INVISIBLE);
        } else {
            movieDetailsScore.setText(rating);
            //movieDetailsScoreTitle.setVisibility(View.VISIBLE);
            movieDetailsScore.setVisibility(View.VISIBLE);
        }

        // Set overview. If there is no overview, we show the default text for overview.
        String overview = movie.getOverview();
        //if (!overview.equals("")) movieDetailsOverview.setText(overview);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        Log.i(TAG, "(onCreate) Activity created");

        // Get current sort order for the movie list, in order to pass it back to MainActivity when
        // pressing "back" button.
        Intent intent = getIntent();
        sortOrder = intent.getStringExtra("sortOrder");

        // Set title for this activity.
        this.setTitle(getResources().getString(R.string.movie_details_title));

        // Set CardView size depending on the width and height in pixels of the current device. As
        // the parent of the CardView is a LinearLayout, we must use LinearLayout.LayoutParams.
        int widthPixels = intent.getIntExtra("widthPixels", 0);
        int heightPixels = intent.getIntExtra("heightPixels", 0);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(widthPixels, heightPixels);
        posterCardView.setLayoutParams(layoutParams);

        // Display movie information and save current movie currentPosition in the grid.
        movie = intent.getParcelableExtra("movie");
        setMovieInfo(movie);
        currentPosition = movie.getPosition();

        // Create an adapter that knows which fragment should be shown on each page.
        MovieDetailsFragmentPagerAdapter adapter = new MovieDetailsFragmentPagerAdapter(getSupportFragmentManager(), this);

        // Set the adapter onto the view pager and go to the current page.
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);

        // Give the TabLayout the ViewPager.
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    /**
     * Helper method to extract the year from a string representing a date in "yyyy-mm-dd" format.
     *
     * @param dateString is the "releaseDate" obtained from the TMDB API.
     * @return the year extracted from the date, or an empty string if there has been any problem.
     */
    String getYear(String dateString) {
        Log.i(TAG, "(getYear) Release date: " + dateString);
        Calendar calendar = Calendar.getInstance();
        String year = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault());

        try {
            calendar.setTime(format.parse(dateString));
            year = String.valueOf(calendar.get(Calendar.YEAR));
        } catch (java.text.ParseException e) {
            Log.e(TAG, "(getYear) Error parsing string: " + e);
        }

        Log.i(TAG, "(getYear) Year: " + year);
        return year;
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
}
