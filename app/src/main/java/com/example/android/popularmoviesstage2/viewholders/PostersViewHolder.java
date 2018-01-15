package com.example.android.popularmoviesstage2.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.adapters.PostersAdapter;
import com.example.android.popularmoviesstage2.classes.Image;
import com.example.android.popularmoviesstage2.utils.NetworkUtils;
import com.example.android.popularmoviesstage2.utils.TextUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v4.content.ContextCompat.getDrawable;

public class PostersViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = PostersViewHolder.class.getSimpleName();

    // Annotate fields with @BindView and views ID for Butter Knife to find and automatically
    // cast the corresponding views.
    @BindView(R.id.poster_image)
    ImageView imageView;
    @BindView(R.id.poster_language)
    TextView languageTextView;

    private Context context;
    private View viewHolder;

    /**
     * Constructor for our ViewHolder.
     *
     * @param itemView The View that we inflated in {@link PostersAdapter#onCreateViewHolder}.
     */
    public PostersViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        viewHolder = itemView;
        context = itemView.getContext();
        Log.i(TAG, "(PostersViewHolder) New ViewHolder created");
    }

    /**
     * Helper method for setting Image information for the current PostersViewHolder from the
     * {@link PostersAdapter#onBindViewHolder(PostersViewHolder, int)} method.
     *
     * @param currentImage is the Image object attached to the current PostersViewHolder element.
     * @param listener     is the listener for click events.
     */
    public void bind(final Image currentImage, final PostersAdapter.OnItemClickListener listener) {
        Log.i(TAG, "(bind) Binding data for the current PostersViewHolder.");

        // Draw image.
        String imagePath = currentImage.getFile_path();
        if (imagePath != null && !imagePath.equals("") && !imagePath.isEmpty()) {
            String posterPath = NetworkUtils.THUMBNAIL_IMAGE_URL + imagePath;
            Picasso.with(context).load(posterPath).into(imageView);
        } else
            imageView.setImageDrawable(getDrawable(context, R.drawable.no_poster));

        // Set language.
        String language = currentImage.getIso_639_1();
        if (language != null && !language.equals(""))
            language = context.getResources().getString(R.string.language) + ": <strong>" + language + "</strong>";
        else
            language = context.getResources().getString(R.string.no_language);
        TextUtils.setHtmlText(languageTextView, language);

        // Set the listener for click events.
        viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(currentImage);
            }
        });
    }
}