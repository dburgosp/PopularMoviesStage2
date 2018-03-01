package com.example.android.popularmoviesstage2.fragmentpageradapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.popularmoviesstage2.classes.Tmdb;
import com.example.android.popularmoviesstage2.fragments.MoviesFragment;
import com.example.android.popularmoviesstage2.fragments.MoviesListFragment;

public class MoviesListFragmentPagerAdapter extends FragmentPagerAdapter {
    private final int NUM_PAGES = 2;
    private int sortOrder;
    private String tabTitles[] = new String[NUM_PAGES];
    private Context context;

    public MoviesListFragmentPagerAdapter(FragmentManager fm, Context context, int sortOrder) {
        super(fm);
        this.sortOrder = sortOrder;
        this.context = context;
        tabTitles[0] = "LIST";
        tabTitles[1] = "LINK";
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MoviesListFragment.newInstance(sortOrder);
            default:
                return MoviesFragment.newInstance(Tmdb.TMDB_SORT_BY_FOR_BUY_AND_RENT);
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
