package com.example.android.popularmoviesstage2.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;

import com.example.android.popularmoviesstage2.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ConfigNowPlayingMoviesActivity extends AppCompatActivity {
    private static final String TAG = ConfigNowPlayingMoviesActivity.class.getSimpleName();

    // Annotate fields with @BindView and views ID for Butter Knife to find and automatically cast
    // the corresponding views.

    @BindView(R.id.config_now_playing_movies_how_theaters)
    RadioButton howTheatersRadioButton;
    @BindView(R.id.config_now_playing_movies_how_digital)
    RadioButton howDigitalRadioButton;
    @BindView(R.id.config_now_playing_movies_how_physical)
    RadioButton howPhysicalRadioButton;

    private Unbinder unbinder;
    private String how, howOriginal;
    private Intent intent;

    private View.OnClickListener howOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == howDigitalRadioButton) {
                // Digital releases selected.
                how = getString(R.string.preferences_upcoming_movies_how_digital_value);
            } else if (v == howPhysicalRadioButton) {
                // DVD/Blu-ray option selected.
                how = getString(R.string.preferences_upcoming_movies_how_physical_value);
            } else {
                // Default value: in theaters.
                how = getString(R.string.preferences_upcoming_movies_how_theaters_value);
            }

            // Update preferences.
            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.preferences_upcoming_movies_how_key), how);
            editor.apply();

            // Update RadioButtons.
            setHow();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTransitions();

        setContentView(R.layout.activity_config_now_playing_movies);
        unbinder = ButterKnife.bind(this);

        initPreferences();

        // Get calling intent.
        intent = getIntent();

        // Set onClickListeners for options.
        howTheatersRadioButton.setOnClickListener(howOnClickListener);
        howDigitalRadioButton.setOnClickListener(howOnClickListener);
        howPhysicalRadioButton.setOnClickListener(howOnClickListener);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (howOriginal.equals(how)) {
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

    private void finishWithResult() {
        super.onBackPressed();
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Define transitions to exit/enter from/to this activity.
     */
    private void setTransitions() {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setBackgroundDrawableResource(R.color.colorPrimaryDark);

        Slide slideIn = new Slide();
        slideIn.setDuration(250);
        slideIn.setSlideEdge(Gravity.RIGHT);
        getWindow().setEnterTransition(slideIn);

        Slide slideOut = new Slide();
        slideOut.setDuration(250);
        slideOut.setSlideEdge(Gravity.LEFT);
        getWindow().setExitTransition(slideOut);
    }

    /**
     * Get preferences for filters on upcoming movies and set the corresponding selected options by
     * default.
     */
    private void initPreferences() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

        // How to see upcoming movies. Default value: on theaters.
        how = sharedPref.getString(getString(R.string.preferences_upcoming_movies_how_key),
                getString(R.string.preferences_upcoming_movies_how_theaters_value));
        howOriginal = how;
        setHow();
    }

    /**
     * Set "How" preferences section in the display, enabling the selected option and disabling the
     * other options.
     */
    private void setHow() {
        if (how.equals(getString(R.string.preferences_upcoming_movies_how_digital_value))) {
            setPreferenceValue(howTheatersRadioButton, false);
            setPreferenceValue(howDigitalRadioButton, true);
            setPreferenceValue(howPhysicalRadioButton, false);
        } else if (how.equals(getString(R.string.preferences_upcoming_movies_how_physical_value))) {
            setPreferenceValue(howTheatersRadioButton, false);
            setPreferenceValue(howDigitalRadioButton, false);
            setPreferenceValue(howPhysicalRadioButton, true);
        } else {
            // Default value.
            setPreferenceValue(howTheatersRadioButton, true);
            setPreferenceValue(howDigitalRadioButton, false);
            setPreferenceValue(howPhysicalRadioButton, false);
        }
    }

    /**
     * Draw a RadioButton with different colors, depending on the "check" parameter.
     *
     * @param radioButton is the RadioButton to be colored.
     * @param check       is a boolean value used to decide the RadioButton colors. If true,
     *                    background tint is white and text color takes the accent color; if false,
     *                    background tint takes the accent color and text color is white.
     */
    private void setPreferenceValue(RadioButton radioButton, boolean check) {
        if (check) {
            radioButton.setChecked(true);
            radioButton.setButtonTintList(getResources().getColorStateList(R.color.colorAccent));
            radioButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorWhite));
            radioButton.setTextColor(getResources().getColor(R.color.colorAccent));
        } else {
            radioButton.setChecked(false);
            radioButton.setButtonTintList(getResources().getColorStateList(R.color.colorBlueGreyPrimary));
            radioButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent));
            radioButton.setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }
}
