package com.example.android.popularmoviesstage2.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.adapters.CastAdapter;
import com.example.android.popularmoviesstage2.classes.Cast;
import com.example.android.popularmoviesstage2.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v4.content.ContextCompat.getDrawable;

public class CastViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = CastViewHolder.class.getSimpleName();
    // Annotate fields with @BindView and views ID for Butter Knife to find and automatically
    // cast the corresponding views.
    @BindView(R.id.cast_crew_image)
    ImageView posterImageView;
    @BindView(R.id.cast_crew_name)
    TextView nameTextView;
    @BindView(R.id.cast_crew_character)
    TextView characterTextView;
    private Context context;

    /**
     * Constructor for our ViewHolder.
     *
     * @param itemView The View that we inflated in {@link CastAdapter#onCreateViewHolder}.
     */
    public CastViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        context = itemView.getContext();
        Log.i(TAG, "(CastViewHolder) New ViewHolder created");
    }

    /**
     * Helper method for setting Person information for the current CastViewHolder from the
     * {@link CastAdapter#onBindViewHolder(CastViewHolder, int)} method.
     *
     * @param currentPerson is the Cast object attached to the current CastViewHolder element.
     * @param listener      is the listener for click events.
     * @param layoutParams  contains the width and height for displaying the profile image.
     */
    public void bind(final Cast currentPerson, final CastAdapter.OnItemClickListener listener, FrameLayout.LayoutParams layoutParams) {
        Log.i(TAG, "(bind) Binding data for the current CastViewHolder.");

        // Draw profile image for current person and resize image to fit screen size and orientation.
        String profilePath = currentPerson.getProfile_path();
        if (profilePath != null && !profilePath.equals("") && !profilePath.isEmpty()) {
            String posterPath = NetworkUtils.THUMBNAIL_IMAGE_URL + profilePath;
            Picasso.with(context).load(posterPath).into(posterImageView);
        } else
            posterImageView.setImageDrawable(getDrawable(context, R.drawable.no_person));
        posterImageView.setLayoutParams(layoutParams);

        // Set person name.
        nameTextView.setText(currentPerson.getName());

        // Set person character/job.
        characterTextView.setText(currentPerson.getCharacter());

        // Set the listener for click events.
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(currentPerson);
            }
        });
    }
}