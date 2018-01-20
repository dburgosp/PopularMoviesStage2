package com.example.android.popularmoviesstage2.viewholders;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.adapters.ReviewsAdapter;
import com.example.android.popularmoviesstage2.classes.TmdbReview;
import com.example.android.popularmoviesstage2.utils.TextUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewsViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = ReviewsViewHolder.class.getSimpleName();

    // Annotate fields with @BindView and views ID for Butter Knife to find and automatically
    // cast the corresponding views.
    @BindView(R.id.reviews_content_text_view)
    TextView contentTextView;
    @BindView(R.id.reviews_author_text_view)
    TextView authorTextView;
    @BindView(R.id.reviews_cardview)
    CardView reviewsCardView;

    private Context context;
    private View viewHolder;

    /**
     * Constructor for our ViewHolder.
     *
     * @param itemView The View that we inflated in {@link ReviewsAdapter#onCreateViewHolder}.
     */
    public ReviewsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        context = itemView.getContext();
        viewHolder = itemView;
        Log.i(TAG, "(ReviewsViewHolder) New ViewHolder created");
    }

    /**
     * Helper method for setting TmdbPerson information for the current ReviewsViewHolder from the
     * {@link ReviewsAdapter#onBindViewHolder(ReviewsViewHolder, int)} method.
     *
     * @param currentTmdbReview is the TmdbReview object attached to the current ReviewsViewHolder element.
     */
    public void bind(final TmdbReview currentTmdbReview, final ReviewsAdapter.OnItemClickListener listener) {
        Log.i(TAG, "(bind) Binding data for the current ReviewsViewHolder.");

        // Set content for the current review. This text is stored in Markdown format.
        TextUtils.setMarkdownText(contentTextView, currentTmdbReview.getContent());

        // Set current review author.
        authorTextView.setText(currentTmdbReview.getAuthor());

        // Set the listener for click events.
        viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(currentTmdbReview, contentTextView);
            }
        });
    }
}