package com.example.android.popularmoviesstage2.fragmentpageradapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.classes.TmdbPerson;
import com.example.android.popularmoviesstage2.fragments.PersonDetailsBioFragment;
import com.example.android.popularmoviesstage2.fragments.PersonDetailsImagesFragment;
import com.example.android.popularmoviesstage2.fragments.PersonDetailsMoviesFragment;
import com.example.android.popularmoviesstage2.fragments.PersonDetailsTvFragment;

/**
 * Provides the appropriate {@link Fragment} for a view pager.
 */
public class PersonDetailsFragmentPagerAdapter extends FragmentPagerAdapter {
    private final int NUM_PAGES = 4;
    private String tabTitles[] = new String[NUM_PAGES];
    private TmdbPerson person;

    public PersonDetailsFragmentPagerAdapter(FragmentManager fm, Context context, TmdbPerson person) {
        super(fm);
        this.person = person;
        tabTitles[0] = context.getString(R.string.person_details_bio);
        tabTitles[1] = context.getString(R.string.person_details_movies);
        tabTitles[2] = context.getString(R.string.person_details_tv);
        tabTitles[3] = context.getString(R.string.person_details_images);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return PersonDetailsBioFragment.newInstance(person);
            case 1:
                return PersonDetailsMoviesFragment.newInstance(person);
            case 2:
                return PersonDetailsTvFragment.newInstance(person);
            default:
                return PersonDetailsImagesFragment.newInstance(person);
        }
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position.
        return tabTitles[position];
    }
}