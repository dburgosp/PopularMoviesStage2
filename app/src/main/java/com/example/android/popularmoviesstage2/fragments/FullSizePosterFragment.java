package com.example.android.popularmoviesstage2.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.classes.Image;
import com.example.android.popularmoviesstage2.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v4.content.ContextCompat.getDrawable;

public class FullSizePosterFragment extends Fragment {
    private static final String TAG = FullSizePosterFragment.class.getSimpleName();

    @BindView(R.id.full_posters_image)
    ImageView imageView;

    /**
     * Required empty public constructor.
     */
    public FullSizePosterFragment() {
    }

    /**
     * Factory method to create a new instance of this fragment using the provided parameters.
     *
     * @param image is the {@link Image} object.
     * @return a new instance of fragment FullSizePosterFragment.
     */
    public static FullSizePosterFragment newInstance(Image image) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("image", image);
        FullSizePosterFragment fragment = new FullSizePosterFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_poster, container, false);
        ButterKnife.bind(this, rootView);

        // Get arguments from calling activity.
        if (getArguments() != null) {
            // Get image info and show it.
            Image image = getArguments().getParcelable("image");
            String imagePath = image.getFile_path();
            if (imagePath != null && !imagePath.equals("") && !imagePath.isEmpty()) {
                String posterPath = NetworkUtils.FULL_IMAGE_URL + imagePath;
                Picasso.with(getContext()).load(posterPath).into(imageView);
            } else
                imageView.setImageDrawable(getDrawable(getContext(), R.drawable.no_poster));

            // Show message with some info about the image.
            String text;
            String language = image.getIso_639_1();
            if (language != null && !language.equals(""))
                text = getContext().getResources().getString(R.string.language) + ": " + language;
            else
                text = getContext().getResources().getString(R.string.no_language);
            int height = image.getHeight();
            int width = image.getWidth();
            if (height > 0 && width > 0)
                text = text + "\n" + getContext().getResources().getString(R.string.size) + Integer.toString(width) + "x" + Integer.toString(height);
            Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
        }

        Log.i(TAG, "(onCreate) Fragment created");
        return rootView;
    }
}
