package com.example.android.popularmoviesstage2;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link Fragment} that displays a list of culture activities at the tourist destination.
 */
public class InfoFragment extends Fragment {
    private final int NUM_LOCATIONS = 10;
    private final int LOCATION_TYPE = 1;

    @BindView(R.id.movie_details_overview)
    TextView movieDetailsOverview;

    public InfoFragment() {
        // Required empty public constructor.
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_info, container, false);
        ButterKnife.bind(this, rootView);
        String name, imageName;
        Drawable image;
        int resourceId, imageId;

        // Set overview. If there is no overview, we show the default text for overview.
        String overview = MovieDetails.getMovieInfo().getOverview();
        if (!overview.equals(""))
            movieDetailsOverview.setText(overview);

        return rootView;
    }
}