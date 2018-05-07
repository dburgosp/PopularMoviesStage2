package com.example.android.popularmoviesstage2.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.Window;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.fragmentpageradapters.MoviesFragmentPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MoviesActivity extends AppCompatActivity {

    // Annotate fields with @BindView and views ID for Butter Knife to find and automatically cast
    // the corresponding views.
    @BindView(R.id.movies_viewpager)
    ViewPager viewPager;
    @BindView(R.id.movies_tab_layout)
    TabLayout tabLayout;

    private Unbinder unbinder;
    private int sort = MoviesFragmentPagerAdapter.PAGE_ALL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTransitions();
        setContentView(R.layout.activity_movies);
        unbinder = ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.movies_toolbar);
        setSupportActionBar(toolbar);

        // If we have passed a "sort" param into the intent, take this param as the default sort
        // order to show (selected page in the ViewPager) when this activity is created.
        if (getIntent().hasExtra("sort"))
            sort = getIntent().getIntExtra("sort", MoviesFragmentPagerAdapter.PAGE_ALL);

        // Set TabLayout and ViewPager to manage the fragments with the different ways of displaying
        // movies_menu lists.
        tabLayout.setupWithViewPager(viewPager);
        MoviesFragmentPagerAdapter adapter =
                new MoviesFragmentPagerAdapter(getSupportFragmentManager(),
                        MoviesActivity.this);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(sort);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.movies_menu, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    /* -------------- */
    /* HELPER METHODS */
    /* -------------- */

    /**
     * Define transitions to exit/enter from/to this activity.
     */
    private void setTransitions() {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setBackgroundDrawableResource(R.color.colorPrimaryDark);
        Slide slideIn = new Slide(Gravity.END);
        getWindow().setEnterTransition(slideIn.setDuration(250));
        Slide slideOut = new Slide(Gravity.START);
        getWindow().setExitTransition(slideOut.setDuration(250));
    }
}
