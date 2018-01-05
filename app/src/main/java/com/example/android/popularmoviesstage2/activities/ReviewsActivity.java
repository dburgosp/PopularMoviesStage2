package com.example.android.popularmoviesstage2.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.classes.Review;
import com.example.android.popularmoviesstage2.utils.NetworkUtils;
import com.example.android.popularmoviesstage2.utils.TextUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewsActivity extends AppCompatActivity {
    @BindView(R.id.reviews_cardview)
    CardView posterCardView;
    @BindView(R.id.reviews_poster)
    ImageView posterImageView;
    @BindView(R.id.reviews_background)
    ImageView backgroundImageView;
    @BindView(R.id.reviews_author_text_view)
    TextView authorTextView;
    @BindView(R.id.reviews_content_text_view)
    TextView contentTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Review review;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        ButterKnife.bind(this);

        // Get parameters from intent.
        Intent intent = getIntent();
        review = intent.getParcelableExtra("review");

        // If device orientation is portrait, set backdrop height to match_parent.
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            int width = backgroundImageView.getLayoutParams().width;
            int height = LayoutParams.MATCH_PARENT;
            LayoutParams params = new LayoutParams(width, height);
            backgroundImageView.setLayoutParams(params);
        }

        // Set poster. If there is no poster, set the default poster.
        String posterPath = review.getPosterPath();
        if (posterPath != null && !posterPath.equals("") && !posterPath.isEmpty())
            Picasso.with(this).load(NetworkUtils.THUMBNAIL_IMAGE_URL + posterPath).into(posterImageView);
        else
            posterImageView.setImageDrawable(getDrawable(R.drawable.no_poster));

        // Set background image. If there is no background image, set the default image.
        String backdropPath = review.getBackdropPath();
        if (backdropPath != null && !backdropPath.equals("") && !backdropPath.isEmpty())
            Picasso.with(this).load(NetworkUtils.FULL_IMAGE_URL + backdropPath).into(backgroundImageView);
        else
            backgroundImageView.setImageDrawable(getDrawable(R.drawable.tmdb_backdrop));

        // Set title for this activity. Otherwise, we show the default text for title.
        String title = review.getMovieTitle();
        if (title != null && !title.equals("") && !title.isEmpty())
            setTitle(title);
        else
            setTitle(getResources().getString(R.string.no_title));

        // Set user name. Otherwise, we show a default text.
        String username = review.getAuthor();
        if (username != null && !username.equals("") && !username.isEmpty())
            TextUtils.setHtmlText(authorTextView, "<small>" + getResources().getString(R.string.a_review_by) + "</small> <strong>" + username + "</strong>");
        else
            authorTextView.setText(getResources().getString(R.string.no_title));

        // Set review content. Otherwise, we show a default text.
        String content = review.getContent();
        if (content != null && !content.equals("") && !content.isEmpty()) {
            // Text reviews are stored in Markdown format.
            TextUtils.setMarkdownText(contentTextView, content);
        } else
            contentTextView.setText(getResources().getString(R.string.no_title));
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
        // Just finish this activity.
        finish();
        return true;
    }
}
