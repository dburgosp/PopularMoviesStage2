package com.example.android.popularmoviesstage2.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.classes.Tmdb;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class GenericAdapter extends RecyclerView.Adapter<GenericAdapter.GenericViewHolder> {
    private static final String TAG = GenericAdapter.class.getSimpleName();

    /**
     * Set a click listener to the RecyclerView, so we can manage OnClick events from the activity
     * from which the RecyclerView is created.
     * <p>
     * For more information: https://antonioleiva.com/recyclerview-listener/
     */
    public interface OnItemClickListener {
        void onItemClick(Object item);
    }

    // Member variables.
    private ArrayList<Object> mArrayList;
    private int mResource;
    private final GenericAdapter.OnItemClickListener mListener;

    /**
     * Constructor for this class.
     *
     * @param arrayList is the list of elements that will be shown in the adapter.
     * @param resource  is the layout resource for displaying a single element.
     * @param listener  is the listener for receiving the clicks.
     */
    public GenericAdapter(ArrayList<Object> arrayList, int resource,
                          GenericAdapter.OnItemClickListener listener) {
        mArrayList = arrayList;
        mResource = resource;
        mListener = listener;
    }

    @Override
    public GenericAdapter.GenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        String methodTag = TAG + "." + Thread.currentThread().getStackTrace()[2].getMethodName();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(mResource, parent, false);
        return new GenericAdapter.GenericViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GenericAdapter.GenericViewHolder holder, int position) {
        if (!mArrayList.isEmpty()) {
            // Update ViewHolder with the element at current position in the adapter.
            String methodTag = TAG + "." + Thread.currentThread().getStackTrace()[2].getMethodName();
            Log.i(methodTag, "Displaying data at position " + position);
            Object currentElement = mArrayList.get(position);
            holder.bind(currentElement, mListener);
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    /* ----------- */
    /* VIEW HOLDER */
    /* ----------- */

    public class GenericViewHolder extends RecyclerView.ViewHolder {
        private final String TAG = GenericViewHolder.class.getSimpleName();

        // Member variables.
        private Context mContext;
        private View mViewHolder;

        /**
         * Constructor for our ViewHolder.
         *
         * @param itemView The View that we inflated in {@link GenericAdapter#onCreateViewHolder}.
         */
        GenericViewHolder(View itemView) {
            super(itemView);
            mViewHolder = itemView;
            mContext = itemView.getContext();
        }

        /**
         * Helper method for setting the information for current ViewHolder from the
         * {@link GenericAdapter#onBindViewHolder(GenericAdapter.GenericViewHolder, int)} method.
         *
         * @param currentElement is the object attached to the current element.
         * @param listener       is the listener for click events.
         */
        public void bind(final Object currentElement, final GenericAdapter.OnItemClickListener listener) {
            // TODO: set info on layout.

            // Set the listener for click events.
            mViewHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(currentElement);
                }
            });
        }
    }
}
