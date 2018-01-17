package com.example.android.popularmoviesstage2.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.classes.Cast;
import com.example.android.popularmoviesstage2.viewholders.CastViewHolder;

import java.util.ArrayList;

public class CastAdapter extends RecyclerView.Adapter<CastViewHolder> {
    private static final String TAG = CastAdapter.class.getSimpleName();
    private final CastAdapter.OnItemClickListener listener;
    private ArrayList<Cast> castArrayList;
    //private int position = 0;

    /**
     * Constructor for this class.
     *
     * @param castArrayList is the list of persons that will be represented into the adapter.
     * @param listener      is the listener for receiving the clicks.
     */
    public CastAdapter(ArrayList<Cast> castArrayList, OnItemClickListener listener) {
        this.castArrayList = castArrayList;
        this.listener = listener;
        Log.i(TAG, "(CastAdapter) Object created");
    }

    /**
     * Setter method for updating the list of persons in the adapter.
     *
     * @param personsArrayList is the new list of persons.
     */
    public void setCastArray(ArrayList<Cast> personsArrayList) {
        this.castArrayList = personsArrayList;
        Log.i(TAG, "(setCastArray) Cast list updated");
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
        if (!castArrayList.isEmpty()) {
            Cast currentCast = castArrayList.get(position);

            // Set position in the adapter for current person.
            //currentCast.setPosition(position);

            // Update CastViewHolder with the person details at current position in the adapter.
            viewHolder.bind(currentCast, listener);

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
        int itemCount = castArrayList.size();
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
        void onItemClick(Cast item);
    }
}