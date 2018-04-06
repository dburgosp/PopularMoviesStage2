package com.example.android.popularmoviesstage2.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

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
    TextView howTheatersTextView;
    @BindView(R.id.config_upcoming_movies_how_digital)
    TextView howDigitalTextView;
    @BindView(R.id.config_upcoming_movies_how_physical)
    TextView howPhysicalTextView;
    @BindView(R.id.config_upcoming_movies_when_this_week)
    TextView whenThisWeekTextView;
    @BindView(R.id.config_upcoming_movies_when_next_week)
    TextView whenNextWeekTextView;
    @BindView(R.id.config_upcoming_movies_when_any_date)
    TextView whenAnyDateTextView;
    @BindView(R.id.config_upcoming_movies_where_here)
    TextView whereThisCountryTextView;
    @BindView(R.id.config_upcoming_movies_where_anywhere)
    TextView whereAnyCountryTextView;

    private Unbinder unbinder;
    private String how, when, where;

    private View.OnClickListener howOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == howDigitalTextView) {
                // Digital releases selected.
                how = getString(R.string.preferences_upcoming_movies_how_digital_value);
            } else if (v == howPhysicalTextView) {
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
            if (v == whenNextWeekTextView) {
                // Next week selected.
                when = getString(R.string.preferences_upcoming_movies_when_next_week_value);
            } else if (v == whenAnyDateTextView) {
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
            if (v == whereAnyCountryTextView) {
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

        // Set onClickListeners for options.
        howTheatersTextView.setOnClickListener(howOnClickListener);
        howDigitalTextView.setOnClickListener(howOnClickListener);
        howPhysicalTextView.setOnClickListener(howOnClickListener);

        whenThisWeekTextView.setOnClickListener(whenOnClickListener);
        whenNextWeekTextView.setOnClickListener(whenOnClickListener);
        whenAnyDateTextView.setOnClickListener(whenOnClickListener);

        whereThisCountryTextView.setOnClickListener(whereOnClickListener);
        whereAnyCountryTextView.setOnClickListener(whereOnClickListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

        // How to see upcoming movies. Default value: on theaters.
        how = sharedPref.getString(getString(R.string.preferences_upcoming_movies_how_key),
                getString(R.string.preferences_upcoming_movies_how_theaters_value));
        setHow();

        // When to see upcoming movies. Default value: this week.
        when = sharedPref.getString(getString(R.string.preferences_upcoming_movies_when_key),
                getString(R.string.preferences_upcoming_movies_when_this_week_value));
        setWhen();

        // Where to see upcoming movies. Default value: in this country.
        where = sharedPref.getString(getString(R.string.preferences_upcoming_movies_where_key),
                getString(R.string.preferences_upcoming_movies_where_your_country_value));
        setWhere();

        // Compose title for "Where to see upcoming movies: your country".
        whereThisCountryTextView.setText(getString(
                R.string.preferences_upcoming_movies_where_your_country_title,
                Locale.getDefault().getDisplayCountry()));
    }

    private void setHow() {
        if (how.equals(getString(R.string.preferences_upcoming_movies_how_digital_value))) {
            setPreferenceValue(howTheatersTextView, false);
            setPreferenceValue(howDigitalTextView, true);
            setPreferenceValue(howPhysicalTextView, false);
        } else if (how.equals(getString(R.string.preferences_upcoming_movies_how_physical_value))) {
            setPreferenceValue(howTheatersTextView, false);
            setPreferenceValue(howDigitalTextView, false);
            setPreferenceValue(howPhysicalTextView, true);
        } else {
            // Default value.
            setPreferenceValue(howTheatersTextView, true);
            setPreferenceValue(howDigitalTextView, false);
            setPreferenceValue(howPhysicalTextView, false);
        }
    }

    private void setWhen() {
        if (how.equals(getString(R.string.preferences_upcoming_movies_when_next_week_value))) {
            setPreferenceValue(whenThisWeekTextView, false);
            setPreferenceValue(whenNextWeekTextView, true);
            setPreferenceValue(whenAnyDateTextView, false);
        } else if (how.equals(getString(R.string.preferences_upcoming_movies_when_any_date_value))) {
            setPreferenceValue(whenThisWeekTextView, false);
            setPreferenceValue(whenNextWeekTextView, false);
            setPreferenceValue(whenAnyDateTextView, true);
        } else {
            // Default value.
            setPreferenceValue(whenThisWeekTextView, true);
            setPreferenceValue(whenNextWeekTextView, false);
            setPreferenceValue(whenAnyDateTextView, false);
        }
    }

    private void setWhere() {
        if (how.equals(getString(R.string.preferences_upcoming_movies_where_any_country_value))) {
            setPreferenceValue(whereThisCountryTextView, false);
            setPreferenceValue(whereAnyCountryTextView, true);
        } else {
            // Default value.
            setPreferenceValue(whereThisCountryTextView, true);
            setPreferenceValue(whereAnyCountryTextView, false);
        }
    }

    private void setPreferenceValue(TextView textView, boolean check) {
        if (check) {
            textView.setBackgroundTintList(getResources().getColorStateList(R.color.colorWhite));
            textView.setTextColor(getResources().getColor(R.color.colorAccent));
        } else {
            textView.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent));
            textView.setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }
}
