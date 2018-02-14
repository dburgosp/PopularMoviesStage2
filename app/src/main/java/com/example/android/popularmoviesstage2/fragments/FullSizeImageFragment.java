package com.example.android.popularmoviesstage2.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.activities.FullSizeImageActivity;
import com.example.android.popularmoviesstage2.classes.Tmdb;
import com.example.android.popularmoviesstage2.classes.TmdbImage;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.support.v4.content.ContextCompat.getDrawable;
import static com.example.android.popularmoviesstage2.activities.FullSizeImageActivity.IMAGE_TYPE_POSTER;

public class FullSizeImageFragment extends Fragment {
    private static final String TAG = FullSizeImageFragment.class.getSimpleName();
    private static final String PARAM_IMAGE = "image";
    private static final String PARAM_IMAGE_TYPE = "imageType";

    // Annotate fields with @BindView and views ID for Butter Knife to find and automatically cast
    // the corresponding views.
    @BindView(R.id.full_size_image)
    ImageView imageView;

    private Unbinder unbinder;

    /**
     * Required empty public constructor.
     */
    public FullSizeImageFragment() {
    }

    /**
     * Factory method to create a new instance of this fragment using the provided parameters.
     *
     * @param image     is the {@link TmdbImage} object.
     * @param imageType is the type of the images to be held in the viewHolder. Available values:
     *                  {@link FullSizeImageActivity#IMAGE_TYPE_POSTER} for posters,
     *                  {@link FullSizeImageActivity#IMAGE_TYPE_BACKDROP} for backdrops.
     * @return a new instance of fragment FullSizeImageFragment.
     */
    public static FullSizeImageFragment newInstance(TmdbImage image, int imageType) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARAM_IMAGE, image);
        bundle.putInt(PARAM_IMAGE_TYPE, imageType);
        FullSizeImageFragment fragment = new FullSizeImageFragment();
        fragment.setArguments(bundle);
        Log.i(TAG, "(newInstance) New instance created using the provided parameters");
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_full_size_image, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        // Get arguments from calling activity, in order to extract image information.
        if (getArguments() != null) {
            // Set image properties depending on its type.
            int imageType = getArguments().getInt(PARAM_IMAGE_TYPE);
            switch (imageType) {
                case IMAGE_TYPE_POSTER: {
                    imageView.setBackgroundResource(R.drawable.default_poster);
                    break;
                }
                default: {
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageView.setBackgroundResource(R.drawable.default_backdrop);
                }
            }

            // Get image info and show it.
            TmdbImage image = getArguments().getParcelable(PARAM_IMAGE);
            String imagePath = image.getFile_path();
            if (imagePath != null && !imagePath.equals("") && !imagePath.isEmpty()) {
                String posterPath = Tmdb.TMDB_POSTER_SIZE_W500_URL + imagePath;
                Picasso.with(getContext())
                        .load(posterPath)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(imageView);
            } else {
                // No image. Show default image.
                switch (imageType) {
                    case IMAGE_TYPE_POSTER: {
                        imageView.setImageDrawable(getDrawable(getContext(), R.drawable.default_poster));
                        break;
                    }
                    default:
                        imageView.setImageDrawable(getDrawable(getContext(), R.drawable.default_backdrop));
                }
            }
        }
        Log.i(TAG, "(onCreate) Fragment created");
        return rootView;
    }

    /**
     * Called when the view previously created by {@link #onCreateView} has
     * been detached from the fragment.  The next time the fragment needs
     * to be displayed, a new view will be created.  This is called
     * after {@link #onStop()} and before {@link #onDestroy()}.  It is called
     * <em>regardless</em> of whether {@link #onCreateView} returned a
     * non-null view.  Internally it is called after the view's state has
     * been saved but before it has been removed from its parent.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        Log.i(TAG, "(onDestroyView) Release resources for freeing up memory");
    }
}
