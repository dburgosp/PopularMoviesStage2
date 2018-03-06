package com.example.android.popularmoviesstage2.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.classes.Tmdb;
import com.example.android.popularmoviesstage2.classes.TmdbMovie;
import com.example.android.popularmoviesstage2.classes.TmdbMovieGenre;
import com.example.android.popularmoviesstage2.utils.DateTimeUtils;
import com.example.android.popularmoviesstage2.utils.ScoreUtils;
import com.example.android.popularmoviesstage2.utils.TextViewUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesListAdapter
        extends RecyclerView.Adapter<MoviesListAdapter.MoviesListViewHolder> {
    private static final String TAG = MoviesListAdapter.class.getSimpleName();
    private final OnItemClickListener listener;
    private ArrayList<TmdbMovie> moviesArrayList;
    private int currentScrollPosition, currentPage, totalPages;
    private FrameLayout.LayoutParams layoutParams;
    private @LayoutRes
    int resource;

    /**
     * Constructor for this class.
     *
     * @param resource        is the identifier of the layout resource to be inflated.
     * @param moviesArrayList is the list of movies that will be represented into the adapter.
     * @param listener        is the listener for receiving the clicks.
     */
    public MoviesListAdapter(@LayoutRes int resource, ArrayList<TmdbMovie> moviesArrayList,
                             OnItemClickListener listener) {
        this.resource = resource;
        this.moviesArrayList = moviesArrayList;
        this.listener = listener;
        this.currentPage = 0;
        this.currentScrollPosition = 0;
        this.totalPages = 0;
        this.layoutParams = null;
        Log.i(TAG, "(MoviesListAdapter) Object created");
    }

    /**
     * Constructor for this class.
     *
     * @param resource        is the identifier of the layout resource to be inflated.
     * @param moviesArrayList is the list of movies that will be represented into the adapter.
     * @param widthPixels     is the width in pixels of a movie poster.
     * @param heightPixels    is the height in pixels of a movie poster.
     * @param listener        is the listener for receiving the clicks.
     */
    public MoviesListAdapter(@LayoutRes int resource, ArrayList<TmdbMovie> moviesArrayList,
                             int widthPixels, int heightPixels, OnItemClickListener listener) {
        this.resource = resource;
        this.moviesArrayList = moviesArrayList;
        this.listener = listener;
        this.currentPage = 0;
        this.currentScrollPosition = 0;
        this.totalPages = 0;
        this.layoutParams = new FrameLayout.LayoutParams(widthPixels, heightPixels);
        Log.i(TAG, "(MoviesListAdapter) Object created");
    }

    /**
     * Public helper method to clear the current movies arrayList.
     */
    void clearMoviesArrayList() {
        this.moviesArrayList.clear();
        this.currentScrollPosition = 0;
        this.currentPage = 1;
        this.totalPages = 0;
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
        Log.i(TAG, "(updateMoviesArrayList) Movie list updated. Current size is " +
                this.moviesArrayList.size());
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
     * Called when RecyclerView needs a new {@link MoviesListViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(MoviesListViewHolder, int)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(MoviesListViewHolder, int)
     */
    @Override
    public MoviesListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG, "(onCreateViewHolder) ViewHolder created");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(resource, parent, false);
        return new MoviesListViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link MoviesListViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link MoviesListViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     *
     * @param viewHolder The ViewHolder which should be updated to represent the contents of the
     *                   item at the given position in the data set.
     * @param position   The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(MoviesListViewHolder viewHolder, int position) {
        Log.i(TAG, "(onBindViewHolder) Displaying data at position " + position);
        if (!moviesArrayList.isEmpty()) {
            // Update MoviesListViewHolder with the movie details at current position in the adapter.
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
        clearMoviesArrayList();
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

    class MoviesListViewHolder extends RecyclerView.ViewHolder {

        private final String TAG = MoviesListViewHolder.class.getSimpleName();
        // Annotate fields with @BindView and views ID for Butter Knife to find and automatically
        // cast the corresponding views.
        @BindView(R.id.movie_list_poster)
        ImageView posterImageView;
        @BindView(R.id.movie_list_title)
        TextView titleTextView;
        @BindView(R.id.movie_list_year)
        TextView releaseDateTextView;
        @BindView(R.id.movie_list_rating)
        TextView scoreTextView;
        @BindView(R.id.movie_list_popularity)
        TextView popularityTextView;
        @BindView(R.id.movie_list_overview)
        TextView genresTextView;

        private Context context;
        private View viewHolder;

        /**
         * Constructor for our ViewHolder.
         *
         * @param itemView The View that we inflated in {@link MoviesListAdapter#onCreateViewHolder}.
         */
        MoviesListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
            viewHolder = itemView;
            Log.i(TAG, "(MoviesListViewHolder) New ViewHolder created");
        }

        /**
         * Helper method for setting movie information for the current MoviesListViewHolder
         * from the {@link MoviesListAdapter#onBindViewHolder(MoviesListViewHolder, int)}
         * method.
         *
         * @param currentTmdbMovie is the TmdbMovie object attached to the current
         *                         MoviesListViewHolder element.
         * @param listener         is the listener for click events.
         * @param layoutParams     contains the width and height for displaying the movie poster.
         */
        public void bind(final TmdbMovie currentTmdbMovie,
                         final MoviesListAdapter.OnItemClickListener listener,
                         FrameLayout.LayoutParams layoutParams) {
            Log.i(TAG, "(bind) Binding data for the current MoviesListViewHolder.");

            // Set movie info: poster, title, release date, users rating, popularity and genres.
            drawMoviePoster(currentTmdbMovie);
            writeMovieTitle(currentTmdbMovie);
            writeMovieReleaseDate(currentTmdbMovie);
            writeMovieUsersRating(currentTmdbMovie);
            writeMoviePopularity(currentTmdbMovie);
            writeMovieOverview(currentTmdbMovie);

            // Set the listener for click events.
            viewHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(currentTmdbMovie, posterImageView);
                }
            });
        }

        /**
         * Helper method to set the poster image, its dimensions (if needed) and the transition for
         * animation (if needed).
         *
         * @param currentTmdbMovie is the {@link TmdbMovie} object with the movie info.
         */
        void drawMoviePoster(final TmdbMovie currentTmdbMovie) {
            String posterPath = currentTmdbMovie.getPoster_path();
            if (posterPath != null && !posterPath.equals("") && !posterPath.isEmpty()) {
                Picasso.with(context)
                        .load(Tmdb.TMDB_POSTER_SIZE_W185_URL + posterPath)
                        /*.memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)*/
                        .into(posterImageView);
            } else
                posterImageView.setImageDrawable(context.getDrawable(R.drawable.default_poster));

            if (layoutParams != null) {
                // Resize image to fit screen size and orientation.
                posterImageView.setLayoutParams(layoutParams);
            }

            switch (resource) {
                case R.layout.list_item_poster_horizontal_layout_2: {
                    // Set transition name to the current ImageView, so it can be animated if
                    // clicked.
                    ViewCompat.setTransitionName(posterImageView,
                            context.getResources().getString(R.string.transition_list_to_details));
                    break;
                }
            }
        }

        /**
         * Helper method to set the movie title.
         *
         * @param currentTmdbMovie is the {@link TmdbMovie} object with the movie info.
         */
        void writeMovieTitle(final TmdbMovie currentTmdbMovie) {
            switch (resource) {
                case R.layout.list_item_poster_horizontal_layout_1:
                case R.layout.list_item_poster_horizontal_layout_2:
                case R.layout.list_item_poster_horizontal_layout_3:
                case R.layout.list_item_poster_vertical_layout_1:
                case R.layout.list_item_poster_vertical_layout_2:
                    // Movie title only for the specified layouts.
                    String title = currentTmdbMovie.getTitle();
                    if (title == null || title.equals("") || title.isEmpty())
                        titleTextView.setVisibility(View.GONE);
                    else
                        titleTextView.setText(title);
                default:
                    break;
            }
        }

        /**
         * Helper method to write the movie users score.
         *
         * @param currentTmdbMovie is the {@link TmdbMovie} object with the movie info.
         */
        void writeMovieUsersRating(final TmdbMovie currentTmdbMovie) {
            switch (resource) {
                case R.layout.list_item_poster_horizontal_layout_1:
                case R.layout.list_item_poster_horizontal_layout_3:
                case R.layout.list_item_poster_vertical_layout_1:
                case R.layout.list_item_poster_vertical_layout_2:
                    // Users score only for the specified layouts.
                    String score = String.valueOf(currentTmdbMovie.getVote_average());
                    ScoreUtils.setTextViewRating(context, score, scoreTextView);
                default:
                    break;
            }
        }

        /**
         * Helper method to write the release date using the required format, depending on the
         * resource layout used to display the movie info.
         *
         * @param currentTmdbMovie is the {@link TmdbMovie} object with the movie info.
         */
        void writeMovieReleaseDate(final TmdbMovie currentTmdbMovie) {
            switch (resource) {
                case R.layout.list_item_poster_horizontal_layout_1:
                case R.layout.list_item_poster_horizontal_layout_2:
                case R.layout.list_item_poster_horizontal_layout_3:
                case R.layout.list_item_poster_vertical_layout_1:
                case R.layout.list_item_poster_vertical_layout_2:
                    // Release date only for the specified layouts.
                    String releaseDate = currentTmdbMovie.getRelease_date();
                    if (resource == R.layout.list_item_poster_horizontal_layout_2) {
                        // Show date in medium format.
                        releaseDate = DateTimeUtils.getStringDate(releaseDate,
                                DateTimeUtils.DATE_FORMAT_MEDIUM);
                    } else {
                        // Show only the year.
                        releaseDate = DateTimeUtils.getYear(releaseDate);
                    }

                    if (releaseDate != null && !releaseDate.equals("") && !releaseDate.isEmpty()) {
                        // Set text.
                        releaseDateTextView.setText(releaseDate);

                        // Color grey for left drawable.
                        TextViewUtils.setTintedCompoundDrawable(context, releaseDateTextView,
                                TextViewUtils.DRAWABLE_LEFT_INDEX, R.drawable.ic_date_range_black_18dp,
                                R.color.colorGrey, R.dimen.tiny_padding);
                    } else
                        releaseDateTextView.setVisibility(View.GONE);
                default:
                    break;
            }
        }

        /**
         * Helper method to write the popularity of the movie.
         *
         * @param currentTmdbMovie is the {@link TmdbMovie} object with the movie info.
         */
        void writeMoviePopularity(final TmdbMovie currentTmdbMovie) {
            switch (resource) {
                case R.layout.list_item_poster_vertical_layout_1:
                case R.layout.list_item_poster_vertical_layout_2:
                    // Popularity only for the specified layouts (two decimals).
                    String popularity = String.format(Locale.getDefault(), "%.2f",
                            currentTmdbMovie.getPopularity());
                    if (popularity != null && !popularity.equals("0.0") && !popularity.isEmpty()) {
                        // Set text.
                        popularityTextView.setText(popularity);

                        // Color grey for left drawable.
                        TextViewUtils.setTintedCompoundDrawable(context, popularityTextView,
                                TextViewUtils.DRAWABLE_LEFT_INDEX, R.drawable.ic_thumb_up_black_18dp,
                                R.color.colorGrey, R.dimen.tiny_padding);
                    } else
                        popularityTextView.setVisibility(View.GONE);
                default:
                    break;
            }
        }

        /**
         * Helper method to write the list of genres of the movie.
         *
         * @param currentTmdbMovie is the {@link TmdbMovie} object with the movie info.
         */
        void writeMovieGenres(final TmdbMovie currentTmdbMovie) {
            switch (resource) {
                case R.layout.list_item_poster_vertical_layout_1:
                case R.layout.list_item_poster_vertical_layout_2:
                    // List of genres only for the specified layouts.
                    ArrayList<TmdbMovieGenre> movieGenres = currentTmdbMovie.getGenres();
                    if (movieGenres != null && movieGenres.size() > 0) {
                        // Build the string with the genres list.
                        StringBuilder genresStringBuilder = new StringBuilder();
                        for (int i = 0; i < movieGenres.size(); i++) {
                            genresStringBuilder.append(movieGenres.get(i).getName());
                            if ((i + 1) < movieGenres.size())
                                genresStringBuilder.append(",");
                        }

                        // Write the string.
                        genresTextView.setText(genresStringBuilder);
                    } else
                        genresTextView.setVisibility(View.GONE);
                default:
                    break;
            }
        }

        /**
         * Helper method to write the overview of the movie.
         *
         * @param currentTmdbMovie is the {@link TmdbMovie} object with the movie info.
         */
        void writeMovieOverview(final TmdbMovie currentTmdbMovie) {
            switch (resource) {
                case R.layout.list_item_poster_vertical_layout_1:
                case R.layout.list_item_poster_vertical_layout_2:
                    // Overview only for the specified layouts.
                    String overview = currentTmdbMovie.getOverview();
                    if (overview != null && !overview.isEmpty() && !overview.equals("")) {
                        genresTextView.setText(overview);
                    }
                    //else genresTextView.setVisibility(View.GONE);
                default:
                    break;
            }
        }
    }
}
