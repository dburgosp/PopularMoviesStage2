package com.example.android.popularmoviesstage2.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.classes.Tmdb;
import com.example.android.popularmoviesstage2.classes.TmdbPerson;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonsAdapter extends RecyclerView.Adapter<PersonsAdapter.PersonsViewHolder> {
    private static final String TAG = PersonsAdapter.class.getSimpleName();
    private final PersonsAdapter.OnItemClickListener listener;
    private ArrayList<TmdbPerson> tmdbPersonArrayList;
    private LinearLayout.LayoutParams layoutParams;
    //private int position = 0;

    /**
     * Constructor for this class.
     *
     * @param tmdbPersonArrayList is the list of persons that will be represented into the adapter.
     * @param widthPixels         is the width in pixels of a movie poster.
     * @param heightPixels        is the height in pixels of a movie poster.
     * @param listener            is the listener for receiving the clicks.
     */
    PersonsAdapter(ArrayList<TmdbPerson> tmdbPersonArrayList, int widthPixels, int heightPixels, 
                   PersonsAdapter.OnItemClickListener listener) {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        this.tmdbPersonArrayList = tmdbPersonArrayList;
        this.listener = listener;
        layoutParams = new LinearLayout.LayoutParams(widthPixels, heightPixels);
        Log.i(TAG + "." + methodName, "Object created");
    }

    /**
     * Setter method for updating the list of persons in the adapter.
     *
     * @param personsArray is the new list of persons.
     */
    void setPersonsArray(ArrayList<TmdbPerson> personsArray) {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        this.tmdbPersonArrayList = personsArray;
        Log.i(TAG + "." + methodName, "TmdbPerson list updated");
    }

    /**
     * Called when RecyclerView needs a new {@link PersonsViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(PersonsViewHolder, int)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(PersonsViewHolder, int)
     */
    @Override
    public PersonsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_poster_grid_layout_1, parent, 
                false);
        Log.i(TAG + "." + methodName, "ViewHolder created");
        return new PersonsViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link PersonsViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link PersonsViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     *
     * @param viewHolder The ViewHolder which should be updated to represent the contents of the
     *                   item at the given position in the data set.
     * @param position   The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(PersonsViewHolder viewHolder, int position) {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        Log.i(TAG + "." + methodName, "Displaying data at position " + position);
        if (!tmdbPersonArrayList.isEmpty()) {
            TmdbPerson currentTmdbPerson = tmdbPersonArrayList.get(position);

            // Set position in the adapter for current movie.
            //currentTmdbPerson.setPosition(position);

            // Update PersonsViewHolder with the movie movie_details_menu at current position in the adapter.
            viewHolder.bind(currentTmdbPerson, listener, layoutParams);

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
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        int itemCount = tmdbPersonArrayList.size();
        Log.i(TAG + "." + methodName, "Number of items in this adapter: " + itemCount);
        return itemCount;
    }

    /**
     * Set a click listener to the RecyclerView, so we can manage OnClick events from the Main
     * Activity from which the RecyclerView is created.
     * <p>
     * For more information: https://antonioleiva.com/recyclerview-listener/
     */
    public interface OnItemClickListener {
        void onItemClick(TmdbPerson item);
    }

    /* ----------- */
    /* VIEW HOLDER */
    /* ----------- */

    class PersonsViewHolder extends RecyclerView.ViewHolder {
        private final String TAG = PersonsViewHolder.class.getSimpleName();
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
        PersonsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            context = itemView.getContext();
            Log.i(TAG + "." + methodName, "New ViewHolder created");
        }

        /**
         * Helper method for setting TmdbPerson information for the current PersonsViewHolder from the
         * {@link PersonsAdapter#onBindViewHolder(PersonsViewHolder, int)} method.
         *
         * @param currentTmdbPerson is the TmdbPerson object attached to the current PersonsViewHolder element.
         * @param listener          is the listener for click events.
         * @param layoutParams      contains the width and height for displaying the profile image.
         */
        public void bind(final TmdbPerson currentTmdbPerson, 
                         final PersonsAdapter.OnItemClickListener listener,
                         LinearLayout.LayoutParams layoutParams) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            Log.i(TAG + "." + methodName, "Binding data for current PersonsViewHolder.");

            // Draw profile image for current person and resize image to fit screen size and orientation.
            String posterPath = Tmdb.TMDB_POSTER_SIZE_W185_URL + currentTmdbPerson.getProfile_path();
            Picasso.with(context)
                    .load(posterPath)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(posterImageView);
            posterImageView.setLayoutParams(layoutParams);

            // Set person name.
            nameTextView.setText(currentTmdbPerson.getName());

            // Set the listener for click events.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(currentTmdbPerson);
                }
            });
        }
    }
}