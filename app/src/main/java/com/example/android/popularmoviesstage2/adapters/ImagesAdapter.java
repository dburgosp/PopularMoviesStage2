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
import com.example.android.popularmoviesstage2.activities.FullSizeImageActivity;
import com.example.android.popularmoviesstage2.classes.Tmdb;
import com.example.android.popularmoviesstage2.classes.TmdbImage;
import com.example.android.popularmoviesstage2.utils.TextViewUtils;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.support.v4.content.ContextCompat.getDrawable;
import static com.example.android.popularmoviesstage2.activities.FullSizeImageActivity.IMAGE_TYPE_BACKDROP;
import static com.example.android.popularmoviesstage2.activities.FullSizeImageActivity.IMAGE_TYPE_POSTER;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImagesViewHolder> {
    private static final String TAG = ImagesAdapter.class.getSimpleName();
    private ArrayList<TmdbImage> imagesArrayList;
    private int imageType;
    private final ImagesAdapter.OnItemClickListener listener;

    /**
     * Constructor for this class.
     *
     * @param imagesArrayList is the list of items that will be represented into the adapter.
     * @param imageType       is the type of the images to be stored in the imagesArrayList.
     *                        Available values: {@link FullSizeImageActivity#IMAGE_TYPE_POSTER} for
     *                        posters, {@link FullSizeImageActivity#IMAGE_TYPE_BACKDROP} for
     *                        backdrops.
     * @param listener        is the listener for receiving the clicks.
     */
    public ImagesAdapter(ArrayList<TmdbImage> imagesArrayList, int imageType, 
                         OnItemClickListener listener) {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        this.imagesArrayList = imagesArrayList;
        this.imageType = imageType;
        this.listener = listener;
        Log.i(TAG + "." + methodName, "Object created");
    }

    /**
     * Setter method for updating the list of items in the adapter.
     *
     * @param imagesArrayList is the new list of items.
     */
    public void setImageArray(ArrayList<TmdbImage> imagesArrayList) {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        this.imagesArrayList = imagesArrayList;
        Log.i(TAG + "." + methodName, "TmdbImage list updated");
    }

    /**
     * Getter method to retrieve the complete images array.
     *
     * @return the images array, imagesArrayList.
     */
    public ArrayList<TmdbImage> getImagesArrayList() {
        return imagesArrayList;
    }

    /**
     * Called when RecyclerView needs a new {@link ImagesViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(ImagesViewHolder, int)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(ImagesViewHolder, int)
     */
    @Override
    public ImagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        Log.i(TAG + "." + methodName, "ViewHolder created");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_image_thumbnail, parent, false);
        return new ImagesViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ImagesViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link ImagesViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     *
     * @param viewHolder The ViewHolder which should be updated to represent the contents of the
     *                   item at the given position in the data set.
     * @param position   The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ImagesViewHolder viewHolder, int position) {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        Log.i(TAG + "." + methodName, "Displaying data at position " + position);
        if (!imagesArrayList.isEmpty()) {
            // Update ImagesViewHolder with the item movie_details_menu at current position in the adapter.
            TmdbImage currentTmdbImage = imagesArrayList.get(position);
            currentTmdbImage.setPosition(position);
            viewHolder.bind(currentTmdbImage, imageType, listener);
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
        int itemCount = imagesArrayList.size();
        Log.i(TAG + "." + methodName, "Number of items in this adapter: " + itemCount);
        return itemCount;
    }

    /**
     * Set a click listener to the RecyclerView, so we can manage OnClick events from the activity
     * from which the RecyclerView is created.
     * <p>
     * For more information: https://antonioleiva.com/recyclerview-listener/
     */
    public interface OnItemClickListener {
        void onItemClick(TmdbImage item);
    }

    /* ----------- */
    /* VIEW HOLDER */
    /* ----------- */

    class ImagesViewHolder extends RecyclerView.ViewHolder {
        private final String TAG = ImagesViewHolder.class.getSimpleName();

        // Annotate fields with @BindView and views ID for Butter Knife to find and automatically
        // cast the corresponding views.
        @BindView(R.id.thumbnail_image)
        ImageView imageView;
        @BindView(R.id.thumbnail_language)
        TextView languageTextView;
        @BindView(R.id.thumbnail_size)
        TextView sizeTextView;

        private Context context;
        private View viewHolder;
        private Unbinder unbinder;

        /**
         * Constructor for our ViewHolder.
         *
         * @param itemView The View that we inflated in {@link ImagesAdapter#onCreateViewHolder}.
         */
        ImagesViewHolder(View itemView) {
            super(itemView);
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            unbinder = ButterKnife.bind(this, itemView);
            viewHolder = itemView;
            context = itemView.getContext();
            Log.i(TAG + "." + methodName, "New ViewHolder created");
        }

        /**
         * Helper method for setting TmdbImage information for the current ImagesViewHolder from the
         * {@link ImagesAdapter#onBindViewHolder(ImagesViewHolder, int)} method.
         *
         * @param currentTmdbImage is the TmdbImage object attached to the current ImagesViewHolder element.
         * @param imageType        is the type of the images to be held in the viewHolder. Available values:
         *                         {@link FullSizeImageActivity#IMAGE_TYPE_POSTER} for posters,
         *                         {@link FullSizeImageActivity#IMAGE_TYPE_BACKDROP} for backdrops.
         * @param listener         is the listener for click events.
         */
        public void bind(final TmdbImage currentTmdbImage, int imageType, 
                         final ImagesAdapter.OnItemClickListener listener) {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            Log.i(TAG + "." + methodName, "Binding data for the current ImagesViewHolder.");

            // Set image dimensions and default background, according to its type, and draw image.
            int widthPixels, heightPixels;
            switch (imageType) {
                case IMAGE_TYPE_POSTER: {
                    widthPixels = context.getResources().getDimensionPixelSize(R.dimen.poster_width);
                    heightPixels = context.getResources().getDimensionPixelSize(R.dimen.poster_height);
                    imageView.setBackgroundResource(R.drawable.no_poster);
                    drawImage(currentTmdbImage, R.drawable.no_poster);
                    break;
                }
                default: {
                    widthPixels = context.getResources().getDimensionPixelSize(R.dimen.backdrop_width);
                    heightPixels = context.getResources().getDimensionPixelSize(R.dimen.backdrop_height);
                    imageView.setBackgroundResource(R.drawable.no_backdrop);
                    drawImage(currentTmdbImage, R.drawable.no_backdrop);
                }
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(widthPixels, heightPixels);
            imageView.setLayoutParams(layoutParams);

            // Write language.
            String language = currentTmdbImage.getIso_639_1();
            if (language != null && !language.equals("")) {
                Locale locale = new Locale(language);
                language = locale.getDisplayLanguage().substring(0, 1).toUpperCase() + locale.getDisplayLanguage().substring(1);
            } else
                language = context.getResources().getString(R.string.no_language);
            TextViewUtils.setHtmlText(languageTextView, language);

            // Write image size for backdrops.
            switch (imageType)
            {
                case IMAGE_TYPE_BACKDROP: {
                    String size = currentTmdbImage.getHeight() + "x" + currentTmdbImage.getWidth();
                    sizeTextView.setText(size);
                    break;
                }
                default: {
                    sizeTextView.setVisibility(View.GONE);
                }
            }

            // Set the listener for click events.
            viewHolder.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(currentTmdbImage);
                }
            });
        }

        /**
         * Called by the garbage collector on an object when garbage collection
         * determines that there are no more references to the object.
         * A subclass overrides the {@code finalize} method to dispose of
         * system resources or to perform other cleanup.
         * <p>
         * The general contract of {@code finalize} is that it is invoked
         * if and when the Java<font size="-2"><sup>TM</sup></font> virtual
         * machine has determined that there is no longer any
         * means by which this object can be accessed by any thread that has
         * not yet died, except as a result of an action taken by the
         * finalization of some other object or class which is ready to be
         * finalized. The {@code finalize} method may take any action, including
         * making this object available again to other threads; the usual purpose
         * of {@code finalize}, however, is to perform cleanup actions before
         * the object is irrevocably discarded. For example, the finalize method
         * for an object that represents an input/output connection might perform
         * explicit I/O transactions to break the connection before the object is
         * permanently discarded.
         * <p>
         * The {@code finalize} method of class {@code Object} performs no
         * special action; it simply returns normally. Subclasses of
         * {@code Object} may override this definition.
         * <p>
         * The Java programming language does not guarantee which thread will
         * invoke the {@code finalize} method for any given object. It is
         * guaranteed, however, that the thread that invokes finalize will not
         * be holding any user-visible synchronization locks when finalize is
         * invoked. If an uncaught exception is thrown by the finalize method,
         * the exception is ignored and finalization of that object terminates.
         * <p>
         * After the {@code finalize} method has been invoked for an object, no
         * further action is taken until the Java virtual machine has again
         * determined that there is no longer any means by which this object can
         * be accessed by any thread that has not yet died, including possible
         * actions by other objects or classes which are ready to be finalized,
         * at which point the object may be discarded.
         * <p>
         * The {@code finalize} method is never invoked more than once by a Java
         * virtual machine for any given object.
         * <p>
         * Any exception thrown by the {@code finalize} method causes
         * the finalization of this object to be halted, but is otherwise
         * ignored.
         *
         * @throws Throwable the {@code Exception} raised by this method
         */
        @Override
        protected void finalize() throws Throwable {
            super.finalize();
            unbinder.unbind();
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            Log.i(TAG + "." + methodName, "Release resources for freeing up memory");
        }

        /* -------------- */
        /* HELPER METHODS */
        /* -------------- */

        /**
         * Private helper method to draw a poster or backdrop image.
         *
         * @param currentTmdbImage   is the current {@link TmdbImage} object to be drawn.
         * @param defaultDrawableRes is the drawable resource to be drawn if the path of the current
         *                           image produces no image.
         */
        private void drawImage(TmdbImage currentTmdbImage, int defaultDrawableRes) {
            String imagePath = currentTmdbImage.getFile_path();
            if (imagePath != null && !imagePath.equals("") && !imagePath.isEmpty()) {
                String posterPath = Tmdb.TMDB_POSTER_SIZE_W185_URL + imagePath;
                Picasso.with(context)
                        .load(posterPath)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(imageView);
            } else
                // No image. Show default image.
                imageView.setImageDrawable(getDrawable(context, defaultDrawableRes));
        }
    }
}