package com.example.android.popularmoviesstage2.fragmentpageradapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.classes.Tmdb;
import com.example.android.popularmoviesstage2.fragments.MoviesFragment;

public class MoviesFragmentPagerAdapter extends FragmentPagerAdapter {
    public static final int PAGE_ALL = 0;
    public static final int PAGE_NOW_PLAYING = 1;
    public static final int PAGE_UPCOMING = 2;
    public static final int PAGE_BUY_AND_RENT = 3;
    public static final int PAGE_FAVORITES = 4;

    private final int NUM_PAGES = 5;
    private String tabTitles[] = new String[NUM_PAGES];
    private Context context;

    public MoviesFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        tabTitles[PAGE_ALL] = context.getString(R.string.all_movies);
        tabTitles[PAGE_NOW_PLAYING] = context.getString(R.string.movies_sort_by_now_playing);
        tabTitles[PAGE_UPCOMING] = context.getString(R.string.movies_sort_by_upcoming);
        tabTitles[PAGE_BUY_AND_RENT] = context.getString(R.string.movies_sort_by_online);
        tabTitles[PAGE_FAVORITES] = context.getString(R.string.movies_sort_by_favorites);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case PAGE_ALL:
                return MoviesFragment.newInstance(Tmdb.TMDB_CONTENT_TYPE_ALL);
            case PAGE_NOW_PLAYING:
                return MoviesFragment.newInstance(Tmdb.TMDB_CONTENT_TYPE_NOW_PLAYING);
            case PAGE_UPCOMING:
                return MoviesFragment.newInstance(Tmdb.TMDB_CONTENT_TYPE_UPCOMING);
            case PAGE_BUY_AND_RENT:
                return MoviesFragment.newInstance(Tmdb.TMDB_CONTENT_TYPE_FAVORITES);
            default: // case PAGE_FAVORITES:
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
