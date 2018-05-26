package com.example.android.popularmoviesstage2.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.data.MyPreferences;
import com.example.android.popularmoviesstage2.utils.AnimatedViewsUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ConfigFilteredMoviesActivity extends AppCompatActivity {
    private static final String TAG = ConfigFilteredMoviesActivity.class.getSimpleName();

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
    CoordinatorLayout rootLayout;

    private Unbinder unbinder;
    private int endHowCheckedIndex = 0, startHowCheckedIndex = 0;
    private int endWhenCheckedIndex = 0, startWhenCheckedIndex = 0;
    private int endWhereCheckedIndex = 0, startWhereCheckedIndex = 0;
    private Intent intent;
    private int revealX;
    private int revealY;

    public static final String EXTRA_TYPE = "EXTRA_TYPE";
    public static final String EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X";
    public static final String EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y";
    public static final int TYPE_UPCOMING = 0;
    public static final int TYPE_NOW_PLAYING = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_config_filtered_movies);
        unbinder = ButterKnife.bind(this);

        // Set the custom tool bar and show the back button.
        Toolbar toolbar = (Toolbar) findViewById(R.id.config_filtered_movies_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get intent and parameters from calling activity.
        intent = getIntent();
        int type = intent.getIntExtra(EXTRA_TYPE, TYPE_UPCOMING);
        if (savedInstanceState == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_X) &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_Y)) {
            // Get coordinates for the center of the CircularReveal animation of the root View.
            revealX = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_X, 0);
            revealY = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_Y, 0);
            AnimatedViewsUtils.circularRevealLayout(rootLayout, revealX, revealY,
                    AnimatedViewsUtils.ANIM_DURATION_SHORT);
        } else {
            rootLayout.setVisibility(View.VISIBLE);
        }

        // Add radio buttons to RadioGroups and set checked elements according to saved preferences.
        switch (type) {
            case TYPE_NOW_PLAYING: {
                setTitle(getString(R.string.movies_sort_by_now_playing));

                // Show only "How" and "Where" elements.
                startHowCheckedIndex = MyPreferences.getNowPlayingMoviesIndex(this,
                        MyPreferences.TYPE_MOVIES_HOW);
                startWhereCheckedIndex = MyPreferences.getNowPlayingMoviesIndex(this,
                        MyPreferences.TYPE_MOVIES_WHERE);

                setRadioButtons(howRadioGroup, getResources().getStringArray(
                        R.array.preferences_now_playing_movies_how_list_array),
                        MyPreferences.TYPE_MOVIES_HOW, type);
                setRadioButtons(whereRadioGroup, getResources().getStringArray(
                        R.array.preferences_movies_where_list_array),
                        MyPreferences.TYPE_MOVIES_WHERE, type);

                // Hide "When" elements.
                whenRadioGroup.setVisibility(View.GONE);
                whenTextView.setVisibility(View.GONE);
                break;
            }

            case TYPE_UPCOMING: {
                setTitle(getString(R.string.movies_sort_by_upcoming));

                // Show all.
                startHowCheckedIndex = MyPreferences.getUpcomingMoviesIndex(this,
                        MyPreferences.TYPE_MOVIES_HOW);
                startWhenCheckedIndex = MyPreferences.getUpcomingMoviesIndex(this,
                        MyPreferences.TYPE_MOVIES_WHEN);
                startWhereCheckedIndex = MyPreferences.getUpcomingMoviesIndex(this,
                        MyPreferences.TYPE_MOVIES_WHERE);

                setRadioButtons(howRadioGroup, getResources().getStringArray(
                        R.array.preferences_upcoming_movies_how_list_array),
                        MyPreferences.TYPE_MOVIES_HOW, type);
                setRadioButtons(whenRadioGroup, getResources().getStringArray(
                        R.array.preferences_upcoming_movies_when_list_array),
                        MyPreferences.TYPE_MOVIES_WHEN, type);
                setRadioButtons(whereRadioGroup, getResources().getStringArray(
                        R.array.preferences_movies_where_list_array),
                        MyPreferences.TYPE_MOVIES_WHERE, type);
                break;
            }

            default: {
                // Show nothing. Hide all elements.
                howRadioGroup.setVisibility(View.GONE);
                howTextView.setVisibility(View.GONE);
                whenRadioGroup.setVisibility(View.GONE);
                whenTextView.setVisibility(View.GONE);
                whereRadioGroup.setVisibility(View.GONE);
                whereTextView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        if ((endHowCheckedIndex == startHowCheckedIndex) &&
                (endWhenCheckedIndex == startWhenCheckedIndex) &&
                (endWhereCheckedIndex == startWhereCheckedIndex)) {
            // No changes.
            setResult(RESULT_CANCELED, intent);
        } else {
            // Preferences have changed.
            setResult(RESULT_OK, intent);
        }

        // Reverse circular reveal animation and finish this activity.
        AnimatedViewsUtils.circularUnrevealLayout(rootLayout, revealX, revealY,
                ConfigFilteredMoviesActivity.this, AnimatedViewsUtils.ANIM_DURATION_SHORT);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                // Back button.
                onBackPressed();
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* ************** */
    /* HELPER METHODS */
    /* ************** */

    /**
     * Helper method for creating and adding the required RadioButtons to a given RadioGroup.
     *
     * @param radioGroup      is the RadioGroup to add elements.
     * @param array           is the String array with labels for every RadioButton.
     * @param preferencesType is a value that indicates the type of preferences in order to update
     *                        the corresponding global index values. Available values are
     *                        {@link MyPreferences#TYPE_MOVIES_HOW},
     *                        {@link MyPreferences#TYPE_MOVIES_WHEN},
     *                        {@link MyPreferences#TYPE_MOVIES_WHERE}.
     * @param moviesType      is a value that indicates the type of movies_menu in order to update
     *                        the corresponding global index values. Available values are
     *                        {@link #TYPE_UPCOMING}, {@link #TYPE_NOW_PLAYING}.
     */
    private void setRadioButtons(final RadioGroup radioGroup, String array[],
                                 final int preferencesType, final int moviesType) {
        // Add elements to RadioGroup.
        int index = 0;
        for (String text : array) {
            // Create the new RadioButton.
            RadioButton radioButton;
            switch (preferencesType) {
                case MyPreferences.TYPE_MOVIES_HOW:
                    radioButton = MyPreferences.createRadioButton(this, text, index,
                            startHowCheckedIndex);
                    break;

                case MyPreferences.TYPE_MOVIES_WHEN:
                    radioButton = MyPreferences.createRadioButton(this, text, index,
                            startWhenCheckedIndex);
                    break;

                default: // case MyPreferences.TYPE_MOVIES_WHERE:
                    radioButton = MyPreferences.createRadioButton(this, text, index,
                            startWhereCheckedIndex);
            }
            index++;

            // Set onClickListener.
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int endCheckedIndex, checkedIndex = radioGroup.getCheckedRadioButtonId();
                    switch (preferencesType) {
                        case MyPreferences.TYPE_MOVIES_HOW:
                            endCheckedIndex = endHowCheckedIndex;
                            break;

                        case MyPreferences.TYPE_MOVIES_WHEN:
                            endCheckedIndex = endWhenCheckedIndex;
                            break;

                        default: // case MyPreferences.TYPE_MOVIES_WHERE:
                            endCheckedIndex = endWhereCheckedIndex;
                    }

                    // Make changes only if state has changed.
                    if (endCheckedIndex != checkedIndex) {
                        // Update text colors.
                        RadioButton checkedRadioButton =
                                (RadioButton) radioGroup.getChildAt(checkedIndex);
                        checkedRadioButton.setTextColor(
                                getResources().getColor(R.color.colorWhite));
                        checkedRadioButton.setTypeface(null, Typeface.BOLD);
                        RadioButton uncheckedRadioButton =
                                (RadioButton) radioGroup.getChildAt(endCheckedIndex);
                        uncheckedRadioButton.setTextColor(
                                getResources().getColor(R.color.colorDarkerWhite));
                        uncheckedRadioButton.setTypeface(null, Typeface.NORMAL);

                        // Update preferences and global variables.
                        switch (preferencesType) {
                            case MyPreferences.TYPE_MOVIES_HOW:
                                endHowCheckedIndex = checkedIndex;
                                break;

                            case MyPreferences.TYPE_MOVIES_WHEN:
                                endWhenCheckedIndex = checkedIndex;
                                break;

                            default: // case MyPreferences.TYPE_MOVIES_WHERE:
                                endWhereCheckedIndex = checkedIndex;
                        }
                        switch (moviesType) {
                            case TYPE_UPCOMING:
                                MyPreferences.setUpcomingMovies(
                                        ConfigFilteredMoviesActivity.this, checkedIndex,
                                        preferencesType);
                                break;

                            case TYPE_NOW_PLAYING:
                                MyPreferences.setNowPlayingMovies(
                                        ConfigFilteredMoviesActivity.this, checkedIndex,
                                        preferencesType);
                                break;
                        }
                    }
                }
            });

            // Add newly created element to RadioGroup.
            radioGroup.addView(radioButton);
        }

        // Set currently checked element depending on shared preferences, and set global variables
        // by default.
        RadioButton currentRadioButton = new RadioButton(this);
        switch (preferencesType) {
            case MyPreferences.TYPE_MOVIES_HOW:
                switch (moviesType) {
                    case TYPE_UPCOMING:
                        startHowCheckedIndex = MyPreferences.getUpcomingMoviesIndex(this,
                                MyPreferences.TYPE_MOVIES_HOW);
                        break;

                    case TYPE_NOW_PLAYING:
                        startHowCheckedIndex = MyPreferences.getNowPlayingMoviesIndex(this,
                                MyPreferences.TYPE_MOVIES_HOW);
                        break;
                }
                endHowCheckedIndex = startHowCheckedIndex;
                currentRadioButton = (RadioButton) radioGroup.getChildAt(startHowCheckedIndex);
                break;

            case MyPreferences.TYPE_MOVIES_WHEN:
                startWhenCheckedIndex = MyPreferences.getUpcomingMoviesIndex(this,
                        MyPreferences.TYPE_MOVIES_WHEN);
                endWhenCheckedIndex = startWhenCheckedIndex;
                currentRadioButton = (RadioButton) radioGroup.getChildAt(startWhenCheckedIndex);
                break;

            default: // case MyPreferences.TYPE_MOVIES_WHERE:
                switch (moviesType) {
                    case TYPE_UPCOMING:
                        startWhereCheckedIndex = MyPreferences.getUpcomingMoviesIndex(this,
                                MyPreferences.TYPE_MOVIES_WHERE);
                        break;

                    case TYPE_NOW_PLAYING:
                        startWhereCheckedIndex = MyPreferences.getNowPlayingMoviesIndex(this,
                                MyPreferences.TYPE_MOVIES_WHERE);
                        break;
                }
                endWhereCheckedIndex = startWhereCheckedIndex;
                currentRadioButton = (RadioButton) radioGroup.getChildAt(startWhereCheckedIndex);
        }
        if (currentRadioButton != null) {
            currentRadioButton.setChecked(true);
            //currentRadioButton.setTextColor(getResources().getColor(R.color.colorAccent));
        }
    }
}
