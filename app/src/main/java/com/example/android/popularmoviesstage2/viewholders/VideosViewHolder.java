package com.example.android.popularmoviesstage2.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.adapters.VideosAdapter;
import com.example.android.popularmoviesstage2.classes.Video;
import com.example.android.popularmoviesstage2.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v4.content.ContextCompat.getDrawable;

public class VideosViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = VideosViewHolder.class.getSimpleName();
    // Annotate fields with @BindView and views ID for Butter Knife to find and automatically
    // cast the corresponding views.
    @BindView(R.id.video_image)
    ImageView videoImageView;
    @BindView(R.id.video_name)
    TextView nameTextView;
    @BindView(R.id.video_language)
    TextView languageTextView;
    @BindView(R.id.video_resolution)
    TextView resolutionTextView;
    private Context context;
    private View viewHolder;

    /**
     * Constructor for our ViewHolder.
     *
     * @param itemView The View that we inflated in {@link VideosAdapter#onCreateViewHolder}.
     */
    public VideosViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        viewHolder = itemView;
        context = itemView.getContext();
        Log.i(TAG, "(VideosViewHolder) New ViewHolder created");
    }

    /**
     * Helper method for setting Video information for the current VideosViewHolder from the
     * {@link VideosAdapter#onBindViewHolder(VideosViewHolder, int)} method.
     *
     * @param currentVideo is the Video object attached to the current VideosViewHolder element.
     * @param listener     is the listener for click events.
     */
    public void bind(final Video currentVideo, final VideosAdapter.OnItemClickListener listener) {
        Log.i(TAG, "(bind) Binding data for the current VideosViewHolder.");

        // Draw backdrop image for current video.
        String videoImagePath = currentVideo.getKey();
        if (videoImagePath != null && !videoImagePath.equals("") && !videoImagePath.isEmpty()) {
            String posterPath = NetworkUtils.YOUTUBE_VIDEO_PREVIEW_URL + videoImagePath + NetworkUtils.YOUTUBE_VIDEO_PREVIEW_IMAGE;
            Picasso.with(context).load(posterPath).into(videoImageView);
        } else
            videoImageView.setImageDrawable(getDrawable(context, R.drawable.no_movie));

        // Set video name.
        nameTextView.setText(currentVideo.getName());

        // Set video language.
        String language = currentVideo.getIso_639_1();
        if (language != null && !language.equals(""))
            language = context.getResources().getString(R.string.language) + ": " + language;
        else
            language = context.getResources().getString(R.string.no_language);
        languageTextView.setText(language);

        // Set video resolution.
        String resolution = Integer.toString(currentVideo.getSize());
        resolutionTextView.setText(resolution);

        // Set the listener for click events.
        viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(currentVideo);
            }
        });
    }
}