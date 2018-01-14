package com.example.android.popularmoviesstage2.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.classes.Image;
import com.example.android.popularmoviesstage2.viewholders.BackdropsViewHolder;

import java.util.ArrayList;

public class BackdropsAdapter extends RecyclerView.Adapter<BackdropsViewHolder> {
    private static final String TAG = BackdropsAdapter.class.getSimpleName();
    private final BackdropsAdapter.OnItemClickListener listener;
    private ArrayList<Image> imagesArrayList;

    /**
     * Constructor for this class.
     *
     * @param imagesArrayList is the list of items that will be represented into the adapter.
     * @param listener        is the listener for receiving the clicks.
     */
    public BackdropsAdapter(ArrayList<Image> imagesArrayList, OnItemClickListener listener) {
        this.imagesArrayList = imagesArrayList;
        this.listener = listener;
        Log.i(TAG, "(BackdropsAdapter) Object created");
    }

    /**
     * Setter method for updating the list of items in the adapter.
     *
     * @param imagesArrayList is the new list of items.
     */
    public void setImageArray(ArrayList<Image> imagesArrayList) {
        this.imagesArrayList = imagesArrayList;
        Log.i(TAG, "(setImageArray) Image list updated");
    }

    /**
     * Called when RecyclerView needs a new {@link BackdropsViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(BackdropsViewHolder, int)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(BackdropsViewHolder, int)
     */
    @Override
    public BackdropsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG, "(onCreateViewHolder) ViewHolder created");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_backdrop_info, parent, false);
        return new BackdropsViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link BackdropsViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link BackdropsViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     *
     * @param viewHolder The ViewHolder which should be updated to represent the contents of the
     *                   item at the given position in the data set.
     * @param position   The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(BackdropsViewHolder viewHolder, int position) {
        Log.i(TAG, "(onBindViewHolder) Displaying data at position " + position);
        if (!imagesArrayList.isEmpty()) {
            Image currentImage = imagesArrayList.get(position);

            // Update BackdropsViewHolder with the item details at current position in the adapter.
            viewHolder.bind(currentImage, listener);
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        int itemCount = imagesArrayList.size();
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
        void onItemClick(Image item);
    }
}