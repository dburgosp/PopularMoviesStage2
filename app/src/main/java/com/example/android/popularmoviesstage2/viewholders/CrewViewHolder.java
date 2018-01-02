package com.example.android.popularmoviesstage2.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.adapters.CrewAdapter;
import com.example.android.popularmoviesstage2.classes.Crew;
import com.example.android.popularmoviesstage2.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v4.content.ContextCompat.getDrawable;

public class CrewViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = CrewViewHolder.class.getSimpleName();
    // Annotate fields with @BindView and views ID for Butter Knife to find and automatically
    // cast the corresponding views.
    @BindView(R.id.cast_crew_image)
    ImageView posterImageView;
    @BindView(R.id.cast_crew_name)
    TextView nameTextView;
    @BindView(R.id.cast_crew_character)
    TextView characterTextView;
    private View viewHolder;
    private Context context;

    /**
     * Constructor for our ViewHolder.
     *
     * @param itemView The View that we inflated in {@link CrewAdapter#onCreateViewHolder}.
     */
    public CrewViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        context = itemView.getContext();
        viewHolder = itemView;
        Log.i(TAG, "(CrewViewHolder) New ViewHolder created");
    }

    /**
     * Helper method for setting Person information for the current CrewViewHolder from the
     * {@link CrewAdapter#onBindViewHolder(CrewViewHolder, int)} method.
     *
     * @param currentPerson is the Crew object attached to the current CrewViewHolder element.
     * @param listener      is the listener for click events.
     */
    public void bind(final Crew currentPerson, final CrewAdapter.OnItemClickListener listener) {
        Log.i(TAG, "(bind) Binding data for the current CrewViewHolder.");

        // Draw profile image for current person and resize image to fit screen size and orientation.
        String profilePath = currentPerson.getProfile_path();
        if (profilePath != null && !profilePath.equals("") && !profilePath.isEmpty()) {
            String posterPath = NetworkUtils.THUMBNAIL_IMAGE_URL + profilePath;
            Picasso.with(context).load(posterPath).into(posterImageView);
        } else
            posterImageView.setImageDrawable(getDrawable(context, R.drawable.no_person));

        // Set person name.
        nameTextView.setText(currentPerson.getName());

        // Set person character/job.
        characterTextView.setText(currentPerson.getJob());

        // Set the listener for click events.
        viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(currentPerson);
            }
        });
    }
}