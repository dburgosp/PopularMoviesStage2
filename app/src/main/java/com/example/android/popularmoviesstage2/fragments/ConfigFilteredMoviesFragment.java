package com.example.android.popularmoviesstage2.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.utils.AnimatedViewsUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ConfigFilteredMoviesFragment extends DialogFragment {
    // Annotate fields with @BindView and views ID for Butter Knife to find and automatically cast
    // the corresponding views.
    @BindView(R.id.config_filtered_movies_how_title)
    TextView howTextView;
    @BindView(R.id.config_filtered_movies_how_radiogroup)
    RadioGroup howRadioGroup;
    @BindView(R.id.config_filtered_movies_when_title)
    TextView whenTextView;
    @BindView(R.id.config_filtered_movies_when_radiogroup)
    RadioGroup whenRadioGroup;
    @BindView(R.id.config_filtered_movies_where_title)
    TextView whereTextView;
    @BindView(R.id.config_filtered_movies_where_radiogroup)
    RadioGroup whereRadioGroup;
    @BindView(R.id.config_filtered_movies_layout)
    LinearLayout rootLayout;

    private Unbinder unbinder;
    private int revealX;
    private int revealY;
    private int endHowCheckedIndex = 0, startHowCheckedIndex = 0;
    private int endWhenCheckedIndex = 0, startWhenCheckedIndex = 0;
    private int endWhereCheckedIndex = 0, startWhereCheckedIndex = 0;

    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String EXTRA_TYPE = "EXTRA_TYPE";
    public static final String EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X";
    public static final String EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y";
    public static final int TYPE_UPCOMING = 0;
    public static final int TYPE_NOW_PLAYING = 1;

    public ConfigFilteredMoviesFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static ConfigFilteredMoviesFragment newInstance(String title, int type, int x, int y) {
        ConfigFilteredMoviesFragment frag = new ConfigFilteredMoviesFragment();
        Bundle args = new Bundle();
        args.putString("EXTRA_TITLE", title);
        args.putInt("EXTRA_TYPE", type);
        args.putInt("EXTRA_CIRCULAR_REVEAL_X", x);
        args.putInt("EXTRA_CIRCULAR_REVEAL_Y", y);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_config_filtered_movies, container);
        unbinder = ButterKnife.bind(this, rootView);

        // Fetch arguments from bundle and set title
        String title = getArguments().getString("EXTRA_TITLE");
        getDialog().setTitle(title);
        int type = getArguments().getInt(EXTRA_TYPE);
        revealX = getArguments().getInt("EXTRA_CIRCULAR_REVEAL_X");
        revealY = getArguments().getInt("EXTRA_CIRCULAR_REVEAL_YX");

        AnimatedViewsUtils.revealActivity(rootLayout, revealX, revealY);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}