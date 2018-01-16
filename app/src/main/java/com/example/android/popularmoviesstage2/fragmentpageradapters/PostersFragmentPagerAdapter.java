package com.example.android.popularmoviesstage2.fragmentpageradapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.popularmoviesstage2.classes.Image;
import com.example.android.popularmoviesstage2.fragments.FullSizeImageFragment;

import java.util.ArrayList;

/**
 * Provides the appropriate {@link Fragment} for a view pager.
 */
public class PostersFragmentPagerAdapter extends FragmentPagerAdapter {
    private Context context;
    private ArrayList<Image> imageArrayList;

    public PostersFragmentPagerAdapter(FragmentManager fm, Context context, ArrayList<Image> imageArrayList) {
        super(fm);
        this.context = context;
        this.imageArrayList = imageArrayList;
    }

    @Override
    public Fragment getItem(int position) {
        return FullSizeImageFragment.newInstance(imageArrayList.get(position));
    }

    @Override
    public int getCount() {
        return imageArrayList.size();
    }
}