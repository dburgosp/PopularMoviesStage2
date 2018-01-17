package com.example.android.popularmoviesstage2.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.activities.FullSizeImageActivity;
import com.example.android.popularmoviesstage2.adapters.ImagesAdapter;
import com.example.android.popularmoviesstage2.classes.Image;
import com.example.android.popularmoviesstage2.utils.NetworkUtils;
import com.example.android.popularmoviesstage2.utils.TextUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.support.v4.content.ContextCompat.getDrawable;
import static com.example.android.popularmoviesstage2.activities.FullSizeImageActivity.IMAGE_TYPE_BACKDROP;
import static com.example.android.popularmoviesstage2.activities.FullSizeImageActivity.IMAGE_TYPE_POSTER;

public class ImagesViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = ImagesViewHolder.class.getSimpleName();

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
    public ImagesViewHolder(View itemView) {
        super(itemView);
        unbinder = ButterKnife.bind(this, itemView);
        viewHolder = itemView;
        context = itemView.getContext();
        Log.i(TAG, "(ImagesViewHolder) New ViewHolder created");
    }

    /**
     * Helper method for setting Image information for the current ImagesViewHolder from the
     * {@link ImagesAdapter#onBindViewHolder(ImagesViewHolder, int)} method.
     *
     * @param currentImage is the Image object attached to the current ImagesViewHolder element.
     * @param imageType    is the type of the images to be held in the viewHolder. Available values:
     *                     {@link FullSizeImageActivity#IMAGE_TYPE_POSTER} for posters,
     *                     {@link FullSizeImageActivity#IMAGE_TYPE_BACKDROP} for backdrops.
     * @param listener     is the listener for click events.
     */
    public void bind(final Image currentImage, int imageType, final ImagesAdapter.OnItemClickListener listener) {
        Log.i(TAG, "(bind) Binding data for the current ImagesViewHolder.");

        // Set image dimensions and default background, according to its type, and draw image.
        int widthPixels, heightPixels;
        switch (imageType) {
            case IMAGE_TYPE_POSTER: {
                widthPixels = context.getResources().getDimensionPixelSize(R.dimen.poster_width);
                heightPixels = context.getResources().getDimensionPixelSize(R.dimen.poster_height);
                imageView.setBackgroundResource(R.drawable.no_poster);
                drawImage(currentImage, R.drawable.no_poster);
                break;
            }
            default: {
                widthPixels = context.getResources().getDimensionPixelSize(R.dimen.backdrop_width);
                heightPixels = context.getResources().getDimensionPixelSize(R.dimen.backdrop_height);
                imageView.setBackgroundResource(R.drawable.no_backdrop);
                drawImage(currentImage, R.drawable.no_backdrop);
            }
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(widthPixels, heightPixels);
        imageView.setLayoutParams(layoutParams);

        // Write language.
        String language = currentImage.getIso_639_1();
        if (language != null && !language.equals(""))
            language = context.getResources().

                    getString(R.string.language) + ": <strong>" + language + "</strong>";
        else
            language = context.getResources().

                    getString(R.string.no_language);
        TextUtils.setHtmlText(languageTextView, language);

        // Write image size for backdrops.
        switch (imageType)

        {
            case IMAGE_TYPE_BACKDROP: {
                String size = currentImage.getHeight() + "x" + currentImage.getWidth();
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
                listener.onItemClick(currentImage);
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
        Log.i(TAG, "(finalize) Release resources for freeing up memory");
    }

    /* -------------- */
    /* HELPER METHODS */
    /* -------------- */

    /**
     * Private helper method to draw a poster o backdrop image.
     *
     * @param currentImage       is the current {@link Image} object to be drawn.
     * @param defaultDrawableRes is the drawable resource to be drawn if the path of the current
     *                           image produces no image.
     */
    private void drawImage(Image currentImage, int defaultDrawableRes) {
        String imagePath = currentImage.getFile_path();
        if (imagePath != null && !imagePath.equals("") && !imagePath.isEmpty()) {
            String posterPath = NetworkUtils.THUMBNAIL_IMAGE_URL + imagePath;
            Picasso.with(context).load(posterPath).into(imageView);
        } else
            // No image. Show default image.
            imageView.setImageDrawable(getDrawable(context, defaultDrawableRes));
    }
}