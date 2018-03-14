package com.example.android.popularmoviesstage2.fragmentpageradapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.classes.Tmdb;
import com.example.android.popularmoviesstage2.fragments.MoviesFragment;

public class MoviesFragmentPagerAdapter extends FragmentPagerAdapter {
    private final int NUM_PAGES = 7;
    private String tabTitles[] = new String[NUM_PAGES];
    private Context context;

    public MoviesFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        tabTitles[0] = context.getString(R.string.movies_sort_by_now_playing);
        tabTitles[1] = context.getString(R.string.movies_sort_by_released_this_week);
        tabTitles[2] = context.getString(R.string.movies_sort_by_upcoming);
        tabTitles[3] = context.getString(R.string.movies_sort_by_popular);
        tabTitles[4] = context.getString(R.string.movies_sort_by_top_rated);
        tabTitles[5] = context.getString(R.string.movies_sort_by_for_buy_and_rent);
        tabTitles[6] = context.getString(R.string.movies_sort_by_favorites);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MoviesFragment.newInstance(Tmdb.TMDB_CONTENT_TYPE_NOW_PLAYING);
            case 1:
                return MoviesFragment.newInstance(Tmdb.TMDB_CONTENT_TYPE_THIS_WEEK_RELEASES);
            case 2:
                return MoviesFragment.newInstance(Tmdb.TMDB_CONTENT_TYPE_UPCOMING);
            case 3:
                return MoviesFragment.newInstance(Tmdb.TMDB_CONTENT_TYPE_POPULAR);
            case 4:
                return MoviesFragment.newInstance(Tmdb.TMDB_CONTENT_TYPE_TOP_RATED);
            case 5:
                return MoviesFragment.newInstance(Tmdb.TMDB_CONTENT_TYPE_FAVORITES);
            default:
                return MoviesFragment.newInstance(Tmdb.TMDB_CONTENT_TYPE_FOR_BUY_AND_RENT);
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
