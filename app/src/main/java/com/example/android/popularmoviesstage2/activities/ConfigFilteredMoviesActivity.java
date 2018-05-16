package com.example.android.popularmoviesstage2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.data.MyPreferences;

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

    private Unbinder unbinder;
    private int endHowCheckedIndex = 0, startHowCheckedIndex = 0;
    private int endWhenCheckedIndex = 0, startWhenCheckedIndex = 0;
    private int endWhereCheckedIndex = 0, startWhereCheckedIndex = 0;
    private Intent intent;

    public static final String PARAM_TYPE = "type";
    public static final int TYPE_UPCOMING = 0;
    public static final int TYPE_NOW_PLAYING = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTransitions();

        setContentView(R.layout.activity_config_filtered_movies);
        unbinder = ButterKnife.bind(this);

        // Set the custom tool bar and show the back button.
        Toolbar toolbar = (Toolbar) findViewById(R.id.config_filtered_movies_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get intent and parameters from calling activity.
        intent = getIntent();
        int type = intent.getIntExtra(PARAM_TYPE, TYPE_UPCOMING);

        // Add radio buttons to RadioGroups and set checked elements according to saved preferences.
        switch (type) {
            case TYPE_NOW_PLAYING: {
                setTitle(getString(R.string.movies_sort_by_now_playing));

                // Show only "How" and "Where" elements.
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
        super.onBackPressed();
        if ((endHowCheckedIndex == startHowCheckedIndex) &&
                (endWhenCheckedIndex == startWhenCheckedIndex) &&
                (endWhereCheckedIndex == startWhereCheckedIndex)) {
            // No changes.
            setResult(RESULT_CANCELED, intent);
        } else {
            // Preferences have changed.
            setResult(RESULT_OK, intent);
        }
        finish();
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
     * @param moviesType      is a value that indicates the type of movies_menu in order to update the
     *                        corresponding global index values. Available values are
     *                        {@link #TYPE_UPCOMING}, {@link #TYPE_NOW_PLAYING}.
     */
    private void setRadioButtons(final RadioGroup radioGroup, String array[],
                                 final int preferencesType, final int moviesType) {
        // Add elements to RadioGroup.
        int index = 0;
        for (String text : array) {
            // Create the new RadioButton.
            RadioButton radioButton = MyPreferences.createRadioButton(this, text, index);
            index++;

            // Set onClickListener.
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get current RadioGroup state.
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
                                getResources().getColor(R.color.colorAccent));
                        RadioButton uncheckedRadioButton =
                                (RadioButton) radioGroup.getChildAt(endCheckedIndex);
                        uncheckedRadioButton.setTextColor(
                                getResources().getColor(R.color.colorWhite));

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
                                MyPreferences.setUpcomingMovies(ConfigFilteredMoviesActivity.this,
                                        checkedIndex, preferencesType);
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
            currentRadioButton.setTextColor(getResources().getColor(R.color.colorAccent));
        }
    }

    /**
     * Define transitions to exit/enter from/to this activity.
     */
    private void setTransitions() {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setBackgroundDrawableResource(R.color.colorPrimaryDark);
        Slide slideIn = new Slide(Gravity.END);
        getWindow().setEnterTransition(slideIn.setDuration(250));
        Slide slideOut = new Slide(Gravity.START);
        getWindow().setExitTransition(slideOut.setDuration(250));
    }
}
