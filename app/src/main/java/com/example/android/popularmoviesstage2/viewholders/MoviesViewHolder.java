package com.example.android.popularmoviesstage2.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.adapters.MoviesAdapter;
import com.example.android.popularmoviesstage2.classes.Movie;
import com.example.android.popularmoviesstage2.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Cache of the children views for a list item.
 */
public class MoviesViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = MoviesViewHolder.class.getSimpleName();
    // Annotate fields with @BindView and views ID for Butter Knife to find and automatically
    // cast the corresponding views.
    @BindView(R.id.movie_list_item_image)
    ImageView posterImageView;
    private Context context;
    private View viewHolder;

    /**
     * Constructor for our ViewHolder.
     *
     * @param itemView The View that we inflated in {@link MoviesAdapter#onCreateViewHolder}.
     */
    public MoviesViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        context = itemView.getContext();
        viewHolder = itemView;
        Log.i(TAG, "(MoviesViewHolder) New ViewHolder created");
    }

    /**
     * Helper method for setting Movie information for the current MoviesViewHolder from the
     * {@link MoviesAdapter#onBindViewHolder(MoviesViewHolder, int)} method.
     *
     * @param currentMovie is the Movie object attached to the current MoviesViewHolder element.
     * @param listener     is the listener for click events.
     * @param layoutParams contains the width and height for displaying the movie poster.
     */
    public void bind(final Movie currentMovie, final MoviesAdapter.OnItemClickListener listener, FrameLayout.LayoutParams layoutParams) {
        Log.i(TAG, "(bind) Binding data for the current MoviesViewHolder.");

        // Draw poster for current movie and resize image to fit screen size and orientation.
        String posterPath = NetworkUtils.TMDB_THUMBNAIL_IMAGE_URL + currentMovie.getPoster_path();
        Picasso.with(context).load(posterPath).into(posterImageView);
        posterImageView.setLayoutParams(layoutParams);

        // Set the listener for click events.
        viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(currentMovie);
            }
        });
    }
}
