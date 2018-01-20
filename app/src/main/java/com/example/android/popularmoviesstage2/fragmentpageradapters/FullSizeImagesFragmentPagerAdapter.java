package com.example.android.popularmoviesstage2.fragmentpageradapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.popularmoviesstage2.classes.TmdbImage;
import com.example.android.popularmoviesstage2.fragments.FullSizeImageFragment;

import java.util.ArrayList;

/**
 * Provides the appropriate {@link Fragment} for a view pager.
 */
public class FullSizeImagesFragmentPagerAdapter extends FragmentPagerAdapter {
    private Context context;
    private ArrayList<TmdbImage> tmdbImageArrayList;
    private int imageType;

    public FullSizeImagesFragmentPagerAdapter(FragmentManager fm, Context context,
                                              ArrayList<TmdbImage> tmdbImageArrayList, int imageType) {
        super(fm);
        this.context = context;
        this.tmdbImageArrayList = tmdbImageArrayList;
        this.imageType = imageType;
    }

    @Override
    public Fragment getItem(int position) {
        return FullSizeImageFragment.newInstance(tmdbImageArrayList.get(position), imageType);
    }

    @Override
    public int getCount() {
        return tmdbImageArrayList.size();
    }
}