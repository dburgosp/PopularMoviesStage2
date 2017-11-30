package com.example.android.popularmoviesstage2;

/**
 * Created by David on 22/09/2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.android.popularmoviesstage2.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Cache of the children views for a list item.
 */
class MoviesViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = MoviesViewHolder.class.getSimpleName();
    private Context context;

    // Annotate fields with @BindView and views ID for Butter Knife to find and automatically
    // cast the corresponding views.
    @BindView(R.id.list_item_image)
    ImageView posterImageView;

    /**
     * Constructor for our ViewHolder.
     *
     * @param itemView The View that we inflated in {@link MoviesAdapter#onCreateViewHolder}.
     */
    MoviesViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        context = itemView.getContext();
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
    void bind(final Movie currentMovie, final MoviesAdapter.OnItemClickListener listener, FrameLayout.LayoutParams layoutParams) {
        Log.i(TAG, "(bind) Binding data for the current MoviesViewHolder.");

        // Draw poster for current movie and resize image to fit screen size and orientation.
        String posterPath = NetworkUtils.THUMBNAIL_IMAGE_URL + currentMovie.getPosterPath();
        Picasso.with(context).load(posterPath).into(posterImageView);
        posterImageView.setLayoutParams(layoutParams);

        // Set the listener for click events.
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(currentMovie);
            }
        });
    }
}
