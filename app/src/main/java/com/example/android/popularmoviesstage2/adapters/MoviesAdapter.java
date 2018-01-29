package com.example.android.popularmoviesstage2.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.classes.TmdbMovie;
import com.example.android.popularmoviesstage2.viewholders.MoviesViewHolder;

import java.util.ArrayList;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesViewHolder> {
    private static final String TAG = MoviesAdapter.class.getSimpleName();
    private final OnItemClickListener listener;
    private ArrayList<TmdbMovie> moviesArrayList;
    private FrameLayout.LayoutParams layoutParams;
    private int currentScrollPosition, currentPage, totalPages;

    /**
     * Constructor for this class.
     *
     * @param moviesArrayList is the list of movies that will be represented into the adapter.
     * @param widthPixels     is the width in pixels of a movie poster.
     * @param heightPixels    is the height in pixels of a movie poster.
     * @param listener        is the listener for receiving the clicks.
     */
    public MoviesAdapter(ArrayList<TmdbMovie> moviesArrayList, int widthPixels, int heightPixels, OnItemClickListener listener) {
        Log.i(TAG, "(MoviesAdapter) Object created");
        this.moviesArrayList = moviesArrayList;
        this.listener = listener;
        this.currentPage = 0;
        this.currentScrollPosition = 0;
        this.totalPages = 0;
        layoutParams = new FrameLayout.LayoutParams(widthPixels, heightPixels);
    }

    /**
     * Public helper method to clear the current movies arrayList.
     */
    public void clearMoviesArrayList() {
        this.moviesArrayList.clear();
        this.currentScrollPosition = 0;
        this.currentPage = 1;
        this.totalPages = 0;
    }

    public ArrayList<TmdbMovie> getMoviesArrayList() {
        return moviesArrayList;
    }

    /**
     * Setter method for updating the list of movies in the adapter.
     *
     * @param moviesArrayList is the new list of movies.
     * @param appendToEnd     if true, the new list will be appended to the end of the current
     *                        moviesArrayList; if false, the new list will be appended to the
     *                        start.
     */
    public void updateMoviesArrayList(ArrayList<TmdbMovie> moviesArrayList, boolean appendToEnd) {
        if (appendToEnd)
            this.moviesArrayList.addAll(moviesArrayList);
        else
            this.moviesArrayList.addAll(0, moviesArrayList);
        Log.i(TAG, "(updateMoviesArrayList) TmdbMovie list updated. Current size is " + this.moviesArrayList.size());
    }

    // Getter methods.

    /**
     * @return the current position into the movie list. Notice that this value is not the relative
     * position of the element into the current page, but the current scroll position.
     */
    public int getCurrentScrollPosition() {
        return currentScrollPosition;
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
     * Called when RecyclerView needs a new {@link MoviesViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(MoviesViewHolder, int)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(MoviesViewHolder, int)
     */
    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG, "(onCreateViewHolder) ViewHolder created");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_movie, parent, false);
        return new MoviesViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link MoviesViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link MoviesViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     *
     * @param viewHolder The ViewHolder which should be updated to represent the contents of the
     *                   item at the given position in the data set.
     * @param position   The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(MoviesViewHolder viewHolder, int position) {
        Log.i(TAG, "(onBindViewHolder) Displaying data at position " + position);
        if (!moviesArrayList.isEmpty()) {
            // Update MoviesViewHolder with the movie details at current position in the adapter.
            TmdbMovie currentTmdbMovie = moviesArrayList.get(position);
            viewHolder.bind(currentTmdbMovie, listener, layoutParams);

            // Set private variables in order to manage paging information.
            currentScrollPosition = viewHolder.getAdapterPosition();
            currentPage = currentTmdbMovie.getPage();
            totalPages = currentTmdbMovie.getTotal_pages();
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        int itemCount = moviesArrayList.size();
        Log.i(TAG, "(getItemCount) Number of items in this adapter: " + itemCount);
        return itemCount;
    }

    /**
     * Called by RecyclerView when it stops observing this Adapter.
     *
     * @param recyclerView The RecyclerView instance which stopped observing this adapter.
     * @see #onAttachedToRecyclerView(RecyclerView)
     */
    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.moviesArrayList.clear();
        this.currentScrollPosition = 0;
        this.currentPage = 1;
        this.totalPages = 0;
    }

    /**
     * Set a click listener to the RecyclerView, so we can manage OnClick events from the Main
     * Activity from which the RecyclerView is created.
     * <p>
     * For more information: https://antonioleiva.com/recyclerview-listener/
     */
    public interface OnItemClickListener {
        void onItemClick(TmdbMovie item, View clickedView);
    }
}
