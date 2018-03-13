package com.example.android.popularmoviesstage2.adapters;

import android.content.Context;
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
import com.example.android.popularmoviesstage2.classes.TmdbCrew;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v4.content.ContextCompat.getDrawable;

public class CrewAdapter extends RecyclerView.Adapter<CrewAdapter.CrewViewHolder> {
    private static final String TAG = CrewAdapter.class.getSimpleName();
    private final CrewAdapter.OnItemClickListener listener;
    private ArrayList<TmdbCrew> tmdbCrewArrayList;
    //private int position = 0;

    /**
     * Constructor for this class.
     *
     * @param tmdbCrewArrayList is the list of persons that will be represented into the adapter.
     * @param listener          is the listener for receiving the clicks.
     */
    public CrewAdapter(ArrayList<TmdbCrew> tmdbCrewArrayList, CrewAdapter.OnItemClickListener listener) {
        this.tmdbCrewArrayList = tmdbCrewArrayList;
        this.listener = listener;
        Log.i(TAG, "(CrewAdapter) Object created");
    }

    /**
     * Setter method for updating the list of persons in the adapter.
     *
     * @param personsArray is the new list of persons.
     */
    public void setCrewArray(ArrayList<TmdbCrew> personsArray) {
        this.tmdbCrewArrayList = personsArray;
        Log.i(TAG, "(setCrewArray) TmdbCrew list updated");
    }

    /**
     * Called when RecyclerView needs a new {@link CrewViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(CrewViewHolder, int)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(CrewViewHolder, int)
     */
    @Override
    public CrewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG, "(onCreateViewHolder) ViewHolder created");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_person_thumbnail_vertical, parent, false);
        return new CrewViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link CrewViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link CrewViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     *
     * @param viewHolder The ViewHolder which should be updated to represent the contents of the
     *                   item at the given position in the data set.
     * @param position   The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(CrewViewHolder viewHolder, int position) {
        Log.i(TAG, "(onBindViewHolder) Displaying data at position " + position);
        if (!tmdbCrewArrayList.isEmpty()) {
            TmdbCrew currentTmdbCrew = tmdbCrewArrayList.get(position);

            // Set position in the adapter for current person.
            //currentTmdbCrew.setPosition(position);

            // Update CrewViewHolder with the person movie_details_menu at current position in the adapter.
            viewHolder.bind(currentTmdbCrew, listener);

            // Save current position.
            //this.position = viewHolder.getAdapterPosition();
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        int itemCount = tmdbCrewArrayList.size();
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
        void onItemClick(TmdbCrew item);
    }

    /* ----------- */
    /* VIEW HOLDER */
    /* ----------- */

    class CrewViewHolder extends RecyclerView.ViewHolder {
        private final String TAG = CrewViewHolder.class.getSimpleName();
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
        CrewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
            viewHolder = itemView;
            Log.i(TAG, "(CrewViewHolder) New ViewHolder created");
        }

        /**
         * Helper method for setting TmdbPerson information for the current CrewViewHolder from the
         * {@link CrewAdapter#onBindViewHolder(CrewViewHolder, int)} method.
         *
         * @param currentPerson is the TmdbCrew object attached to the current CrewViewHolder element.
         * @param listener      is the listener for click events.
         */
        void bind(final TmdbCrew currentPerson, final CrewAdapter.OnItemClickListener listener) {
            Log.i(TAG, "(bind) Binding data for the current CrewViewHolder.");

            // Draw profile image for current person and resize image to fit screen size and orientation.
            String profilePath = currentPerson.getProfile_path();
            if (profilePath != null && !profilePath.equals("") && !profilePath.isEmpty()) {
                String posterPath = Tmdb.TMDB_POSTER_SIZE_W185_URL + profilePath;
                Picasso.with(context)
                        .load(posterPath)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(posterImageView);
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
}