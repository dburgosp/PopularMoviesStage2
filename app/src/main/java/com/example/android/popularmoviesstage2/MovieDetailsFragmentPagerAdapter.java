package com.example.android.popularmoviesstage2;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Provides the appropriate {@link Fragment} for a view pager.
 */
public class MovieDetailsFragmentPagerAdapter extends FragmentPagerAdapter {
    private final int NUM_PAGES = 4;
    private String tabTitles[] = new String[NUM_PAGES];
    private Context context;

    public MovieDetailsFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        tabTitles[0] = context.getString(R.string.movie_details_1);
        tabTitles[1] = context.getString(R.string.movie_details_2);
        tabTitles[2] = context.getString(R.string.movie_details_3);
        tabTitles[3] = context.getString(R.string.movie_details_4);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new InfoFragment();
            case 1:
                return new CastCrewFragment();
            case 2:
                return new ReviewsFragment();
            default:
                return new MediaFragment();
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