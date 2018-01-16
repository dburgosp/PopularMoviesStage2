package com.example.android.popularmoviesstage2.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.classes.Image;
import com.example.android.popularmoviesstage2.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.support.v4.content.ContextCompat.getDrawable;

public class FullSizeImageFragment extends Fragment {
    private static final String TAG = FullSizeImageFragment.class.getSimpleName();

    @BindView(R.id.full_posters_image)
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
     * @param image is the {@link Image} object.
     * @return a new instance of fragment FullSizeImageFragment.
     */
    public static FullSizeImageFragment newInstance(Image image) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("image", image);
        FullSizeImageFragment fragment = new FullSizeImageFragment();
        fragment.setArguments(bundle);
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
            // Get image info and show it.
            Image image = getArguments().getParcelable("image");
            String imagePath = image.getFile_path();
            if (imagePath != null && !imagePath.equals("") && !imagePath.isEmpty()) {
                String posterPath = NetworkUtils.FULL_IMAGE_URL + imagePath;
                Picasso.with(getContext()).load(posterPath).into(imageView);
            } else
                imageView.setImageDrawable(getDrawable(getContext(), R.drawable.default_poster));
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

        // Release resources for freeing up memory.
        unbinder.unbind();
    }
}
