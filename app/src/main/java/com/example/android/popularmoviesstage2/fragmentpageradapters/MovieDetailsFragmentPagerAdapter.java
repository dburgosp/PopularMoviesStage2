package com.example.android.popularmoviesstage2.fragmentpageradapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.classes.Movie;
import com.example.android.popularmoviesstage2.fragments.CastCrewFragment;
import com.example.android.popularmoviesstage2.fragments.InfoFragment;
import com.example.android.popularmoviesstage2.fragments.MediaFragment;
import com.example.android.popularmoviesstage2.fragments.ReviewsFragment;

/**
 * Provides the appropriate {@link Fragment} for a view pager.
 */
public class MovieDetailsFragmentPagerAdapter extends FragmentPagerAdapter {
    private final int NUM_PAGES = 4;
    private String tabTitles[] = new String[NUM_PAGES];
    private Context context;
    private Movie movie;

    public MovieDetailsFragmentPagerAdapter(FragmentManager fm, Context context, Movie movie) {
        super(fm);
        this.context = context;
        this.movie = movie;
        tabTitles[0] = context.getString(R.string.movie_details_info);
        tabTitles[1] = context.getString(R.string.movie_details_cast_crew);
        tabTitles[2] = context.getString(R.string.movie_details_reviews);
        tabTitles[3] = context.getString(R.string.movie_details_media);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return InfoFragment.newInstance(movie);
            case 1:
                return CastCrewFragment.newInstance(movie);
            case 2:
                return ReviewsFragment.newInstance(movie);
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