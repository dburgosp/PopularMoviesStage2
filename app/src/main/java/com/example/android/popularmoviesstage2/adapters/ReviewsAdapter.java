package com.example.android.popularmoviesstage2.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.classes.TmdbReview;
import com.example.android.popularmoviesstage2.utils.TextViewUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {
    private static final String TAG = ReviewsAdapter.class.getSimpleName();
    private ArrayList<TmdbReview> reviewsArrayList;
    private LinearLayout.LayoutParams layoutParams;
    private int currentPosition, currentPage, totalPages;

    /**
     * Constructor for this class.
     *
     * @param reviewsArrayList is the list of reviews that will be represented into the adapter.
     */
    public ReviewsAdapter(ArrayList<TmdbReview> reviewsArrayList) {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        this.reviewsArrayList = reviewsArrayList;
        this.currentPosition = 0;
        this.currentPage = 0;
        this.totalPages = 0;
        Log.i(TAG + "." + methodName, "Object created");
    }

    // Getter methods.

    /**
     * @return the current position into the current page.
     */
    public int getCurrentPosition() {
        return currentPosition;
    }

    /**
     * @return the number of the last page of reviews.
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * @return the total number of pages of reviews.
     */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     * Setter method for updating the list of reviews in the adapter.
     *
     * @param reviewsArrayList is the new list of reviews.
     * @param appendToEnd      if true, the new list will be appended to the end of the current
     *                         reviewsArrayList; if false, the new list will be appended to the
     *                         start.
     */
    public void updateReviewsArray(ArrayList<TmdbReview> reviewsArrayList, boolean appendToEnd) {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        if (appendToEnd)
            this.reviewsArrayList.addAll(reviewsArrayList);
        else
            this.reviewsArrayList.addAll(0, reviewsArrayList);
        Log.i(TAG + "." + methodName, "Review list updated. Current size is " +
                this.reviewsArrayList.size());
    }

    /**
     * Called when RecyclerView needs a new {@link ReviewsViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(ReviewsViewHolder, int)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(ReviewsViewHolder, int)
     */
    @Override
    public ReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_review, parent, false);
        Log.i(TAG + "." + methodName, "ViewHolder created");
        return new ReviewsViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ReviewsViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link ReviewsViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     *
     * @param viewHolder The ViewHolder which should be updated to represent the contents of the
     *                   item at the given position in the data set.
     * @param position   The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ReviewsViewHolder viewHolder, int position) {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        Log.i(TAG + "." + methodName, "Displaying data at position " + position);
        if (!reviewsArrayList.isEmpty()) {
            // Get current review object and update the view holder with the review movie_details_menu at
            // current position in the adapter.
            TmdbReview currentTmdbReview = reviewsArrayList.get(position);
            viewHolder.bind(currentTmdbReview);

            // Set private variables in order to manage paging information.
            currentPosition = currentTmdbReview.getPosition();
            currentPage = currentTmdbReview.getPage();
            totalPages = currentTmdbReview.getTotal_pages();
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        int itemCount = reviewsArrayList.size();
        Log.i(TAG + "." + methodName, "Number of items in this adapter: " + itemCount);
        return itemCount;
    }

    /* ----------- */
    /* VIEW HOLDER */
    /* ----------- */

    class ReviewsViewHolder extends RecyclerView.ViewHolder {
        private final String TAG = ReviewsViewHolder.class.getSimpleName();
        private final int PREVIEW_MAX_LINES = 3;

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
        ReviewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            context = itemView.getContext();
            viewHolder = itemView;
            Log.i(TAG + "." + methodName, "New ViewHolder created");
        }

        /**
         * Helper method for setting TmdbPerson information for the current ReviewsViewHolder from
         * the {@link ReviewsAdapter#onBindViewHolder(ReviewsViewHolder, int)} method.
         *
         * @param currentTmdbReview is the TmdbReview object attached to the current
         *                          ReviewsViewHolder element.
         */
        public void bind(final TmdbReview currentTmdbReview) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            Log.i(TAG + "." + methodName, "Binding data for current ReviewsViewHolder.");

            // Set current review author.
            authorTextView.setText(currentTmdbReview.getAuthor());

            // Set content for the current review. This text is stored in Markdown format.
            TextViewUtils.setMarkdownText(contentTextView, currentTmdbReview.getContent());

            // Set the listener for click events.
            viewHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Show/hide the full preview.
                    if (contentTextView.getMaxLines() == PREVIEW_MAX_LINES)
                        contentTextView.setMaxLines(Integer.MAX_VALUE);
                    else
                        contentTextView.setMaxLines(PREVIEW_MAX_LINES);
                }
            });
        }
    }
}