package com.example.android.popularmoviesstage2.fragmentpageradapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.classes.TmdbMovie;
import com.example.android.popularmoviesstage2.fragments.MovieDetailsCastCrewFragment;
import com.example.android.popularmoviesstage2.fragments.MovieDetailsInfoFragment;
import com.example.android.popularmoviesstage2.fragments.MovieDetailsMediaFragment;
import com.example.android.popularmoviesstage2.fragments.MovieDetailsReviewsFragment;

/**
 * Provides the appropriate {@link Fragment} for a view pager.
 */
public class MovieDetailsFragmentPagerAdapter extends FragmentPagerAdapter {
    private final int NUM_PAGES = 4;
    private String tabTitles[] = new String[NUM_PAGES];
    private Context context;
    private TmdbMovie tmdbMovie;

    public MovieDetailsFragmentPagerAdapter(FragmentManager fm, Context context, TmdbMovie tmdbMovie) {
        super(fm);
        this.context = context;
        this.tmdbMovie = tmdbMovie;
        tabTitles[0] = context.getString(R.string.movie_details_info);
        tabTitles[1] = context.getString(R.string.movie_details_cast_crew);
        tabTitles[2] = context.getString(R.string.movie_details_media);
        tabTitles[3] = context.getString(R.string.movie_details_reviews);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MovieDetailsInfoFragment.newInstance(tmdbMovie);
            case 1:
                return MovieDetailsCastCrewFragment.newInstance(tmdbMovie);
            case 2:
                return MovieDetailsMediaFragment.newInstance(tmdbMovie);
            default:
                return MovieDetailsReviewsFragment.newInstance(tmdbMovie);
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