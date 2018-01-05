package com.example.android.popularmoviesstage2.viewholders;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.adapters.ReviewsAdapter;
import com.example.android.popularmoviesstage2.classes.Review;
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
     * Helper method for setting Person information for the current ReviewsViewHolder from the
     * {@link ReviewsAdapter#onBindViewHolder(ReviewsViewHolder, int)} method.
     *
     * @param currentReview is the Review object attached to the current ReviewsViewHolder element.
     */
    public void bind(final Review currentReview, final ReviewsAdapter.OnItemClickListener listener) {
        Log.i(TAG, "(bind) Binding data for the current ReviewsViewHolder.");

        // If this is the first element, add a top padding to the CardView.
        int page = currentReview.getPage();
        int position = currentReview.getPosition();
        if (page == 1 && position == 0) {
            Log.i(TAG, "(bind) Page " + page + ", position " + position + ". Adding top padding to review by " + currentReview.getAuthor() + ".");
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) reviewsCardView.getLayoutParams();
            layoutParams.setMargins(0,
                    context.getResources().getDimensionPixelSize(R.dimen.regular_padding),
                    context.getResources().getDimensionPixelSize(R.dimen.regular_padding),
                    context.getResources().getDimensionPixelSize(R.dimen.regular_padding));
            reviewsCardView.setLayoutParams(layoutParams);
        }

        // Set content for the current review. This text is stored in Markdown format.
        TextUtils.setMarkdownText(contentTextView, currentReview.getContent());

        // Set current review author.
        authorTextView.setText(currentReview.getAuthor());

        // Set the listener for click events.
        viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(currentReview);
            }
        });
    }
}