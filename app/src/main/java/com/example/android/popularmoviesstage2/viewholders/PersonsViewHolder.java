package com.example.android.popularmoviesstage2.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.adapters.PersonsAdapter;
import com.example.android.popularmoviesstage2.classes.Person;
import com.example.android.popularmoviesstage2.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Cache of the children views for a list item.
 */
public class PersonsViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = PersonsViewHolder.class.getSimpleName();
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
     * @param itemView The View that we inflated in {@link PersonsAdapter#onCreateViewHolder}.
     */
    public PersonsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        context = itemView.getContext();
        Log.i(TAG, "(PersonsViewHolder) New ViewHolder created");
    }

    /**
     * Helper method for setting Person information for the current PersonsViewHolder from the
     * {@link PersonsAdapter#onBindViewHolder(PersonsViewHolder, int)} method.
     *
     * @param currentPerson is the Person object attached to the current PersonsViewHolder element.
     * @param listener      is the listener for click events.
     * @param layoutParams  contains the width and height for displaying the profile image.
     */
    public void bind(final Person currentPerson, final PersonsAdapter.OnItemClickListener listener, LinearLayout.LayoutParams layoutParams) {
        Log.i(TAG, "(bind) Binding data for the current PersonsViewHolder.");

        // Draw profile image for current person and resize image to fit screen size and orientation.
        String posterPath = NetworkUtils.THUMBNAIL_IMAGE_URL + currentPerson.getProfile_path();
        Picasso.with(context).load(posterPath).into(posterImageView);
        posterImageView.setLayoutParams(layoutParams);

        // Set person name.
        nameTextView.setText(currentPerson.getName());

        // Set the listener for click events.
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(currentPerson);
            }
        });
    }
}