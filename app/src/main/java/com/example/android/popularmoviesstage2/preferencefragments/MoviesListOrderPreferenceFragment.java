package com.example.android.popularmoviesstage2.preferencefragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.android.popularmoviesstage2.R;

public class MoviesListOrderPreferenceFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.movies_list_preferences);
    }
}