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

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ConfigUpcomingMoviesActivity extends AppCompatActivity {
    private static final String TAG = ConfigUpcomingMoviesActivity.class.getSimpleName();

    // Annotate fields with @BindView and views ID for Butter Knife to find and automatically cast
    // the corresponding views.
    @BindView(R.id.config_upcoming_movies_how_theaters)
    RadioButton howTheatersRadioButton;
    @BindView(R.id.config_upcoming_movies_how_digital)
    RadioButton howDigitalRadioButton;
    @BindView(R.id.config_upcoming_movies_how_physical)
    RadioButton howPhysicalRadioButton;
    @BindView(R.id.config_upcoming_movies_when_this_week)
    RadioButton whenThisWeekRadioButton;
    @BindView(R.id.config_upcoming_movies_when_next_week)
    RadioButton whenNextWeekRadioButton;
    @BindView(R.id.config_upcoming_movies_when_any_date)
    RadioButton whenAnyDateRadioButton;
    @BindView(R.id.config_upcoming_movies_where_here)
    RadioButton whereThisCountryRadioButton;
    @BindView(R.id.config_upcoming_movies_where_anywhere)
    RadioButton whereAnyCountryRadioButton;

    private Unbinder unbinder;
    private String how, when, where, howOriginal, whenOriginal, whereOriginal;
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

            // Update display.
            setHow();
        }
    };

    private View.OnClickListener whenOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == whenNextWeekRadioButton) {
                // Next week selected.
                when = getString(R.string.preferences_upcoming_movies_when_next_week_value);
            } else if (v == whenAnyDateRadioButton) {
                // Any date selected.
                when = getString(R.string.preferences_upcoming_movies_when_any_date_value);
            } else {
                // Default value: this week.
                when = getString(R.string.preferences_upcoming_movies_when_this_week_value);
            }

            // Update preferences.
            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.preferences_upcoming_movies_when_key), when);
            editor.apply();

            // Update display.
            setWhen();
        }
    };

    private View.OnClickListener whereOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == whereAnyCountryRadioButton) {
                // Any country selected.
                where = getString(R.string.preferences_upcoming_movies_where_any_country_value);
            } else {
                // Default value: this country.
                where = getString(R.string.preferences_upcoming_movies_where_your_country_value);
            }

            // Update preferences.
            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.preferences_upcoming_movies_where_key), where);
            editor.apply();

            // Update display.
            setWhere();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTransitions();

        setContentView(R.layout.activity_config_upcoming_movies);
        unbinder = ButterKnife.bind(this);

        initPreferences();

        // Get calling intent.
        intent = getIntent();

        // Set onClickListeners for options.
        howTheatersRadioButton.setOnClickListener(howOnClickListener);
        howDigitalRadioButton.setOnClickListener(howOnClickListener);
        howPhysicalRadioButton.setOnClickListener(howOnClickListener);

        whenThisWeekRadioButton.setOnClickListener(whenOnClickListener);
        whenNextWeekRadioButton.setOnClickListener(whenOnClickListener);
        whenAnyDateRadioButton.setOnClickListener(whenOnClickListener);

        whereThisCountryRadioButton.setOnClickListener(whereOnClickListener);
        whereAnyCountryRadioButton.setOnClickListener(whereOnClickListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (whereOriginal.equals(where) && howOriginal.equals(how) && whenOriginal.equals(when)) {
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

        // How to see upcoming movies (default value: on theaters). Store original value for this
        // preference.
        how = sharedPref.getString(getString(R.string.preferences_upcoming_movies_how_key),
                getString(R.string.preferences_upcoming_movies_how_theaters_value));
        howOriginal = how;
        setHow();

        // When to see upcoming movies (default value: this week). Store original value for this
        // preference.
        when = sharedPref.getString(getString(R.string.preferences_upcoming_movies_when_key),
                getString(R.string.preferences_upcoming_movies_when_this_week_value));
        whenOriginal = when;
        setWhen();

        // Where to see upcoming movies (default value: in this country). Store original value for
        // this preference and compose title for "Where to see upcoming movies: your country".
        where = sharedPref.getString(getString(R.string.preferences_upcoming_movies_where_key),
                getString(R.string.preferences_upcoming_movies_where_your_country_value));
        setWhere();
        whereOriginal = where;
        whereThisCountryRadioButton.setText(getString(
                R.string.preferences_movies_where_your_country_title,
                Locale.getDefault().getDisplayCountry()));
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
     * Set "When" preferences section in the display, enabling the selected option and disabling the
     * other options.
     */
    private void setWhen() {
        if (when.equals(getString(R.string.preferences_upcoming_movies_when_next_week_value))) {
            setPreferenceValue(whenThisWeekRadioButton, false);
            setPreferenceValue(whenNextWeekRadioButton, true);
            setPreferenceValue(whenAnyDateRadioButton, false);
        } else if (when.equals(getString(R.string.preferences_upcoming_movies_when_any_date_value))) {
            setPreferenceValue(whenThisWeekRadioButton, false);
            setPreferenceValue(whenNextWeekRadioButton, false);
            setPreferenceValue(whenAnyDateRadioButton, true);
        } else {
            // Default value.
            setPreferenceValue(whenThisWeekRadioButton, true);
            setPreferenceValue(whenNextWeekRadioButton, false);
            setPreferenceValue(whenAnyDateRadioButton, false);
        }
    }

    /**
     * Set "Where" preferences section in the display, enabling the selected option and disabling
     * the other options.
     */
    private void setWhere() {
        if (where.equals(getString(R.string.preferences_upcoming_movies_where_any_country_value))) {
            setPreferenceValue(whereThisCountryRadioButton, false);
            setPreferenceValue(whereAnyCountryRadioButton, true);
        } else {
            // Default value.
            setPreferenceValue(whereThisCountryRadioButton, true);
            setPreferenceValue(whereAnyCountryRadioButton, false);
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
