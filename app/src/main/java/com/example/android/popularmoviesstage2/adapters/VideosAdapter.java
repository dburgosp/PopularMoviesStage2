package com.example.android.popularmoviesstage2.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.classes.Video;
import com.example.android.popularmoviesstage2.viewholders.VideosViewHolder;

import java.util.ArrayList;

public class VideosAdapter extends RecyclerView.Adapter<VideosViewHolder> {
    private static final String TAG = VideosAdapter.class.getSimpleName();
    private final VideosAdapter.OnItemClickListener listener;
    private ArrayList<Video> videosArrayList;

    /**
     * Constructor for this class.
     *
     * @param videosArrayList is the list of videos that will be represented into the adapter.
     * @param listener        is the listener for receiving the clicks.
     */
    public VideosAdapter(ArrayList<Video> videosArrayList, OnItemClickListener listener) {
        this.videosArrayList = videosArrayList;
        this.listener = listener;
        Log.i(TAG, "(VideosAdapter) Object created");
    }

    /**
     * Setter method for updating the list of videos in the adapter.
     *
     * @param videosArrayList is the new list of videos.
     */
    public void setVideoArray(ArrayList<Video> videosArrayList) {
        this.videosArrayList = videosArrayList;
        Log.i(TAG, "(setVideoArray) Video list updated");
    }

    /**
     * Called when RecyclerView needs a new {@link VideosViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(VideosViewHolder, int)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(VideosViewHolder, int)
     */
    @Override
    public VideosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG, "(onCreateViewHolder) ViewHolder created");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_video_thumbnail, parent, false);
        return new VideosViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link VideosViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link VideosViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     *
     * @param viewHolder The ViewHolder which should be updated to represent the contents of the
     *                   item at the given position in the data set.
     * @param position   The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(VideosViewHolder viewHolder, int position) {
        Log.i(TAG, "(onBindViewHolder) Displaying data at position " + position);
        if (!videosArrayList.isEmpty()) {
            Video currentVideo = videosArrayList.get(position);

            // Update VideosViewHolder with the video details at current position in the adapter.
            viewHolder.bind(currentVideo, listener);
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        int itemCount = videosArrayList.size();
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
        void onItemClick(Video item);
    }
}