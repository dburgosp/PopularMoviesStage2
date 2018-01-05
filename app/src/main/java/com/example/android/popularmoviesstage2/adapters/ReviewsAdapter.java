package com.example.android.popularmoviesstage2.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.classes.Review;
import com.example.android.popularmoviesstage2.utils.NetworkUtils;
import com.example.android.popularmoviesstage2.viewholders.ReviewsViewHolder;

import java.util.ArrayList;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsViewHolder> {
    private static final String TAG = ReviewsAdapter.class.getSimpleName();
    private final ReviewsAdapter.OnItemClickListener listener;
    private ArrayList<Review> reviewArrayList;
    private LinearLayout.LayoutParams layoutParams;
    private int page;

    public static final int REVIEWS_NEXT_PAGE = 1;
    public static final int REVIEWS_CURRENT_PAGE = 0;
    public static final int REVIEWS_PREVIOUS_PAGE = -1;

    /**
     * Constructor for this class.
     *
     * @param reviewArrayList is the list of reviews that will be represented into the adapter.
     * @param listener        is the listener for receiving the clicks.
     */
    public ReviewsAdapter(ArrayList<Review> reviewArrayList, ReviewsAdapter.OnItemClickListener listener) {
        this.reviewArrayList = reviewArrayList;
        this.listener = listener;
        this.page = 0;
        Log.i(TAG, "(ReviewsAdapter) Object created");
    }

    /**
     * Setter method for updating the list of reviews in the adapter.
     *
     * @param reviewsArrayList is the new list of reviews.
     */
    public void setReviewsArray(ArrayList<Review> reviewsArrayList) {
        this.reviewArrayList.addAll(reviewsArrayList);
        Log.i(TAG, "(setReviewsArray) Review list updated");
    }

    /**
     * Getter method to obtain the page private variable of this adapter.
     *
     * @return one of the following values: 0 for the current page, 1 for the next page and -1 for
     * the previous page.
     */
    public int getPage() {
        return page;
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
        Log.i(TAG, "(onCreateViewHolder) ViewHolder created");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_review, parent, false);
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
        Log.i(TAG, "(onBindViewHolder) Displaying data at position " + position);
        if (!reviewArrayList.isEmpty()) {
            // Get current review object and update the view holder with the review details at
            // current position in the adapter.
            Review currentReview = reviewArrayList.get(position);
            viewHolder.bind(currentReview, listener);

            // Prepare results from next or previous page, if necessary.
            if (currentReview.getPosition() == (NetworkUtils.REVIEWS_PER_PAGE - 1) &&
                    currentReview.getPage() < currentReview.getTotal_pages()) {
                page = REVIEWS_NEXT_PAGE;
            } else {
                if (currentReview.getPosition() == 0 && currentReview.getPage() > 1) {
                    page = REVIEWS_PREVIOUS_PAGE;
                } else
                    page = REVIEWS_CURRENT_PAGE;
            }
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        int itemCount = reviewArrayList.size();
        Log.i(TAG, "(getItemCount) Number of items in this adapter: " + itemCount);
        return itemCount;
    }

    /**
     * Set a click listener to the RecyclerView, so we can manage OnClick events from the activity
     * from which the RecyclerView is created.
     * <p>
     * For more information: https://antonioleiva.com/recyclerview-listener/
     */
    public interface OnItemClickListener {
        void onItemClick(Review item);
    }
}