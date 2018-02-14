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
import com.example.android.popularmoviesstage2.classes.TmdbCast;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v4.content.ContextCompat.getDrawable;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {
    private static final String TAG = CastAdapter.class.getSimpleName();
    private final CastAdapter.OnItemClickListener listener;
    private ArrayList<TmdbCast> tmdbCastArrayList;
    //private int position = 0;

    /**
     * Constructor for this class.
     *
     * @param tmdbCastArrayList is the list of persons that will be represented into the adapter.
     * @param listener      is the listener for receiving the clicks.
     */
    public CastAdapter(ArrayList<TmdbCast> tmdbCastArrayList, OnItemClickListener listener) {
        this.tmdbCastArrayList = tmdbCastArrayList;
        this.listener = listener;
        Log.i(TAG, "(CastAdapter) Object created");
    }

    /**
     * Setter method for updating the list of persons in the adapter.
     *
     * @param personsArrayList is the new list of persons.
     */
    public void setCastArray(ArrayList<TmdbCast> personsArrayList) {
        this.tmdbCastArrayList = personsArrayList;
        Log.i(TAG, "(setCastArray) TmdbCast list updated");
    }

    /**
     * Getter method to obtain the last position saved at {@link #onBindViewHolder}.
     *
     * @return current position.
     */
/*    int getPosition() {
        return position;
    }*/

    /**
     * Called when RecyclerView needs a new {@link CastViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(CastViewHolder, int)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(CastViewHolder, int)
     */
    @Override
    public CastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG, "(onCreateViewHolder) ViewHolder created");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_person_thumbnail_horizontal, parent, false);
        return new CastViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link CastViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link CastViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     *
     * @param viewHolder The ViewHolder which should be updated to represent the contents of the
     *                   item at the given position in the data set.
     * @param position   The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(CastViewHolder viewHolder, int position) {
        Log.i(TAG, "(onBindViewHolder) Displaying data at position " + position);
        if (!tmdbCastArrayList.isEmpty()) {
            TmdbCast currentTmdbCast = tmdbCastArrayList.get(position);

            // Set position in the adapter for current person.
            //currentTmdbCast.setPosition(position);

            // Update CastViewHolder with the person details at current position in the adapter.
            viewHolder.bind(currentTmdbCast, listener);

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
        int itemCount = tmdbCastArrayList.size();
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
        void onItemClick(TmdbCast item);
    }

    /* ----------- */
    /* VIEW HOLDER */
    /* ----------- */

    public class CastViewHolder extends RecyclerView.ViewHolder {
        private final String TAG = CastViewHolder.class.getSimpleName();
        // Annotate fields with @BindView and views ID for Butter Knife to find and automatically
        // cast the corresponding views.
        @BindView(R.id.cast_crew_image)
        ImageView posterImageView;
        @BindView(R.id.cast_crew_name)
        TextView nameTextView;
        @BindView(R.id.cast_crew_character)
        TextView characterTextView;
        private Context context;
        private View viewHolder;

        /**
         * Constructor for our ViewHolder.
         *
         * @param itemView The View that we inflated in {@link CastAdapter#onCreateViewHolder}.
         */
        CastViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            viewHolder = itemView;
            context = itemView.getContext();
            Log.i(TAG, "(CastViewHolder) New ViewHolder created");
        }

        /**
         * Helper method for setting TmdbPerson information for the current CastViewHolder from the
         * {@link CastAdapter#onBindViewHolder(CastViewHolder, int)} method.
         *
         * @param currentPerson is the TmdbCast object attached to the current CastViewHolder element.
         * @param listener      is the listener for click events.
         */
        public void bind(final TmdbCast currentPerson, final CastAdapter.OnItemClickListener listener) {
            Log.i(TAG, "(bind) Binding data for the current CastViewHolder.");

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
            characterTextView.setText(currentPerson.getCharacter());

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