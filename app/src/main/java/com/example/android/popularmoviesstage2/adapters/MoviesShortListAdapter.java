package com.example.android.popularmoviesstage2.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.classes.Tmdb;
import com.example.android.popularmoviesstage2.classes.TmdbMovie;
import com.example.android.popularmoviesstage2.utils.DateTimeUtils;
import com.example.android.popularmoviesstage2.utils.ScoreUtils;
import com.example.android.popularmoviesstage2.utils.TextViewUtils;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesShortListAdapter extends RecyclerView.Adapter<MoviesShortListAdapter.MoviesShortListViewHolder> {
    private static final String TAG = MoviesShortListAdapter.class.getSimpleName();
    public static final int DATE_FORMAT_SHORT = 0;
    public static final int DATE_FORMAT_LONG = 1;
    private final OnItemClickListener listener;
    private ArrayList<TmdbMovie> moviesArrayList;
    private @LayoutRes
    int resource;
    private int dateFormat;

    /**
     * Constructor for this class.
     *
     * @param resource        is the identifier of the layout resource to be inflated.
     * @param moviesArrayList is the list of movies that will be represented into the adapter.
     * @param listener        is the listener for receiving the clicks.
     * @param dateFormat      is the date format for the release date. Available values are:
     *                        {@link MoviesShortListAdapter#DATE_FORMAT_SHORT},
     *                        {@link MoviesShortListAdapter#DATE_FORMAT_LONG}.
     */
    public MoviesShortListAdapter(@LayoutRes int resource, ArrayList<TmdbMovie> moviesArrayList,
                                  OnItemClickListener listener, int dateFormat) {
        Log.i(TAG, "(MoviesShortListAdapter) Object created");
        this.resource = resource;
        this.moviesArrayList = moviesArrayList;
        this.listener = listener;
        this.dateFormat = dateFormat;
    }

    /**
     * Public helper method to clear the current movies arrayList.
     */
    public void clearMoviesArrayList() {
        this.moviesArrayList.clear();
    }

    public ArrayList<TmdbMovie> getMoviesArrayList() {
        return moviesArrayList;
    }

    /**
     * Setter method for updating the list of movies in the adapter.
     *
     * @param moviesArrayList is the new list of movies.
     */
    public void setMoviesArrayList(ArrayList<TmdbMovie> moviesArrayList) {
        this.moviesArrayList.addAll(moviesArrayList);
        Log.i(TAG, "(setMoviesArrayList) TmdbMovie list updated. Current size is " + this.moviesArrayList.size());
    }

    /**
     * Called when RecyclerView needs a new {@link MoviesShortListViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(MoviesShortListViewHolder, int)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(MoviesShortListViewHolder, int)
     */
    @Override
    public MoviesShortListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG, "(onCreateViewHolder) ViewHolder created");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(resource, parent, false);
        return new MoviesShortListViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link MoviesShortListViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link MoviesShortListViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     *
     * @param viewHolder The ViewHolder which should be updated to represent the contents of the
     *                   item at the given position in the data set.
     * @param position   The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(MoviesShortListViewHolder viewHolder, int position) {
        Log.i(TAG, "(onBindViewHolder) Displaying data at position " + position);
        if (!moviesArrayList.isEmpty()) {
            // Update MoviesShortListViewHolder with the movie details at current position in the adapter.
            TmdbMovie currentTmdbMovie = moviesArrayList.get(position);
            viewHolder.bind(currentTmdbMovie, listener, dateFormat);
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

    /* ----------- */
    /* VIEW HOLDER */
    /* ----------- */

    class MoviesShortListViewHolder extends RecyclerView.ViewHolder {

        private final String TAG = MoviesShortListViewHolder.class.getSimpleName();
        // Annotate fields with @BindView and views ID for Butter Knife to find and automatically
        // cast the corresponding views.
        @BindView(R.id.movie_short_list_poster)
        ImageView posterImageView;
        @BindView(R.id.movie_short_list_title)
        TextView titleTextView;
        @BindView(R.id.movie_short_list_year)
        TextView yearTextView;
        @BindView(R.id.movie_short_list_rating)
        TextView scoreTextView;

        private Context context;
        private View viewHolder;

        /**
         * Constructor for our ViewHolder.
         *
         * @param itemView The View that we inflated in {@link MoviesShortListAdapter#onCreateViewHolder}.
         */
        MoviesShortListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
            viewHolder = itemView;
            Log.i(TAG, "(MoviesShortListViewHolder) New ViewHolder created");
        }

        /**
         * Helper method for setting movie information for the current MoviesShortListViewHolder
         * from the {@link MoviesShortListAdapter#onBindViewHolder(MoviesShortListViewHolder, int)}
         * method.
         *
         * @param currentTmdbMovie is the TmdbMovie object attached to the current
         *                         MoviesShortListViewHolder element.
         * @param listener         is the listener for click events.
         * @param dateFormat       is the date format for the release date. Available values are:
         *                         {@link MoviesShortListAdapter#DATE_FORMAT_SHORT},
         *                         {@link MoviesShortListAdapter#DATE_FORMAT_LONG}.
         */
        public void bind(final TmdbMovie currentTmdbMovie,
                         final MoviesShortListAdapter.OnItemClickListener listener, int dateFormat) {
            Log.i(TAG, "(bind) Binding data for the current MoviesShortListViewHolder.");

            // Draw poster for current movie.
            String posterPath = Tmdb.TMDB_POSTER_SIZE_W185_URL + currentTmdbMovie.getPoster_path();
            Picasso.with(context)
                    .load(posterPath)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(posterImageView);

            // Write movie title.
            String title = currentTmdbMovie.getTitle();
            if (title == null || title.equals("") || title.isEmpty())
                titleTextView.setVisibility(View.GONE);
            else
                titleTextView.setText(title);

            // Write release date.
            String releaseDate;
            switch (dateFormat) {
                case MoviesShortListAdapter.DATE_FORMAT_LONG:
                    releaseDate = DateTimeUtils.getYear(currentTmdbMovie.getRelease_date());
                    break;
                default:
                    releaseDate = currentTmdbMovie.getRelease_date();
            }
            if (releaseDate != null && !releaseDate.equals("") && !releaseDate.isEmpty()) {
                // Set text.
                yearTextView.setText(releaseDate);

                // Color grey for left drawable.
                TextViewUtils.setTintedCompoundDrawable(context, yearTextView,
                        TextViewUtils.DRAWABLE_LEFT_INDEX, R.drawable.ic_date_range_black_18dp,
                        R.color.colorGrey, R.dimen.separator);
            } else
                yearTextView.setVisibility(View.GONE);

            // Write users rating.
            String score = String.valueOf(currentTmdbMovie.getVote_average());
            ScoreUtils.setTextViewRating(context, score, scoreTextView);

            // Set transition name to the current view, so it can be animated if clicked.
            ViewCompat.setTransitionName(posterImageView,
                    context.getResources().getString(R.string.transition_details_to_details));

            // Set the listener for click events.
            viewHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(currentTmdbMovie, posterImageView);
                }
            });
        }
    }
}
