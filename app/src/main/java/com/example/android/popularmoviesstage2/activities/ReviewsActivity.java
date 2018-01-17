package com.example.android.popularmoviesstage2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.classes.Review;
import com.example.android.popularmoviesstage2.utils.NetworkUtils;
import com.example.android.popularmoviesstage2.utils.TextUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ReviewsActivity extends AppCompatActivity {
    private static final String TAG = ReviewsActivity.class.getSimpleName();

    // Annotate fields with @BindView and views ID for Butter Knife to find and automatically cast
    // the corresponding views.
    @BindView(R.id.reviews_cardview)
    CardView posterCardView;
    @BindView(R.id.reviews_poster)
    ImageView posterImageView;
    @BindView(R.id.reviews_author_text_view)
    TextView authorTextView;
    @BindView(R.id.reviews_content_text_view)
    TextView contentTextView;

    private Review review;
    private String movieYear;
    private Unbinder unbinder;

    // Public values for extra parameters for the intent to call this activity.
    public static final String EXTRA_PARAM_REVIEW = "review";
    public static final String EXTRA_PARAM_YEAR = "year";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        unbinder = ButterKnife.bind(this);

        // Get parameters from intent.
        Intent intent = getIntent();
        review = intent.getParcelableExtra(EXTRA_PARAM_REVIEW);
        movieYear = intent.getStringExtra(EXTRA_PARAM_YEAR);

        // Set content for the current movie review.
        setReview();

        Log.i(TAG, "(onCreate) Activity created");
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
        finish();
        Log.i(TAG, "(onSupportNavigateUp) Finish activity and go back");
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
        getMenuInflater().inflate(R.menu.review, menu);
        Log.i(TAG, "(onCreateOptionsMenu) Content of the options menu initialized");
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        Log.i(TAG, "(onDestroy) Release resources for freeing up memory");
    }

    /* -------------- */
    /* HELPER METHODS */
    /* -------------- */

    private void setReview() {
        // Set poster. If there is no poster, set the default poster.
        String posterPath = review.getPosterPath();
        if (posterPath != null && !posterPath.equals("") && !posterPath.isEmpty())
            Picasso.with(this).load(NetworkUtils.THUMBNAIL_IMAGE_URL + posterPath).into(posterImageView);
        else
            posterImageView.setImageDrawable(getDrawable(R.drawable.no_poster));

        // Set title for this activity, if exists. Otherwise, we show the default text for title.
        String title = review.getMovieTitle();
        if (title != null && !title.equals("") && !title.isEmpty()) {
            if (movieYear != null && !movieYear.equals("") && !movieYear.isEmpty())
                title = title + " (" + movieYear + ")";
            setTitle(title);
        } else
            setTitle(getResources().getString(R.string.no_title));

        // Set user name if exists. Otherwise, we show a default text.
        String username = review.getAuthor();
        if (username != null && !username.equals("") && !username.isEmpty())
            TextUtils.setHtmlText(authorTextView,
                    "<small>" + getResources().getString(R.string.a_review_by) +
                            "</small> <strong>" + username + "</strong>");
        else
            authorTextView.setText(getResources().getString(R.string.no_author));

        // Set review content if exists. Otherwise, we show a default text.
        String content = review.getContent();
        if (content != null && !content.equals("") && !content.isEmpty()) {
            // Text reviews are stored in Markdown format.
            TextUtils.setMarkdownText(contentTextView, content);
        } else
            contentTextView.setText(getResources().getString(R.string.no_content));

        Log.i(TAG, "(setReview) User review written");
    }
}
