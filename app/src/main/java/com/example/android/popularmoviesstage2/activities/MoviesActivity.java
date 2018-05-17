package com.example.android.popularmoviesstage2.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.fragmentpageradapters.MoviesFragmentPagerAdapter;
import com.example.android.popularmoviesstage2.fragments.ConfigFilteredMoviesFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MoviesActivity extends AppCompatActivity {
    private static final int RESULT_CODE_CONFIG_UPCOMING_MOVIES = 1;
    private static final int RESULT_CODE_CONFIG_NOW_PLAYING_MOVIES = 2;

    // Annotate fields with @BindView and views ID for Butter Knife to find and automatically cast
    // the corresponding views.
    @BindView(R.id.movies_viewpager)
    ViewPager viewPager;
    @BindView(R.id.movies_tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.movies_fab)
    FloatingActionButton floatingActionButton;

    private Unbinder unbinder;
    private int sort = MoviesFragmentPagerAdapter.PAGE_ALL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //AnimatedViewsUtils.setTransitions(getWindow());
        getWindow().setBackgroundDrawableResource(R.color.colorPrimaryDark);
        setContentView(R.layout.activity_movies);
        unbinder = ButterKnife.bind(this);

        // Set custom Toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.movies_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // If we have passed a "sort" param into the intent, take this param as the default sort
        // order to show (selected page in the ViewPager) when this activity is created.
        if (getIntent().hasExtra("sort"))
            sort = getIntent().getIntExtra("sort", MoviesFragmentPagerAdapter.PAGE_ALL);

        // Set TabLayout and ViewPager to manage the fragments with the different ways of displaying
        // movies_menu lists.
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);
                        switch (tab.getPosition()) {
                            case 1:
                                // If we are showing now playing movies_menu info, show FAB and set
                                // its onClick behaviour for opening ConfigFilteredMoviesActivity.
                                setFloatingActionButton(
                                        ConfigFilteredMoviesActivity.TYPE_NOW_PLAYING,
                                        RESULT_CODE_CONFIG_NOW_PLAYING_MOVIES);
                                break;

                            case 2:
                                // If we are showing upcoming movies_menu info, show FAB and set its
                                // onClick behaviour for opening ConfigFilteredMoviesActivity.
                                setFloatingActionButton(
                                        ConfigFilteredMoviesActivity.TYPE_UPCOMING,
                                        RESULT_CODE_CONFIG_UPCOMING_MOVIES);
                                break;

                            default:
                                floatingActionButton.setVisibility(View.GONE);
                        }
                    }
                });
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

    /**
     * Receive the result from a previous call to
     * {@link #startActivityForResult(Intent, int)}.  This follows the
     * related Activity API as described there in
     * {@link Activity#onActivityResult(int, int, Intent)}.
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // We come from ConfigFilteredMoviesActivity and there are changes on movie preferences.
            // Refresh the adapter for the ViewPager in order to get the fresh new movies lists.
            MoviesFragmentPagerAdapter adapter =
                    new MoviesFragmentPagerAdapter(getSupportFragmentManager(),
                            MoviesActivity.this);
            viewPager.setAdapter(adapter);

            // Set corresponding tab and refresh the current movie list.
            switch (requestCode) {
                case RESULT_CODE_CONFIG_NOW_PLAYING_MOVIES:
                    // Preferences have changed for now playing movies_menu section.
                    viewPager.setCurrentItem(MoviesFragmentPagerAdapter.PAGE_NOW_PLAYING);
                    break;

                case RESULT_CODE_CONFIG_UPCOMING_MOVIES:
                    // Preferences have changed for upcoming movies_menu section. Set
                    // corresponding tab and refresh the current movie list.
                    viewPager.setCurrentItem(MoviesFragmentPagerAdapter.PAGE_UPCOMING);
                    break;
            }
        }
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
     * Helper method for showing FloatingActionButton and setting its behaviour.
     *
     * @param typeValue  is the value for the ConfigFilteredMoviesActivity.EXTRA_TYPE
     *                   parameter of the intent for calling ConfigFilteredMoviesActivity when the
     *                   FloatingActionButton is clicked.
     * @param resultCode is the request code for calling ConfigFilteredMoviesActivity for result.
     */
    private void setFloatingActionButton(final int typeValue, final int resultCode) {
        floatingActionButton.setVisibility(View.VISIBLE);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoviesActivity.this,
                        ConfigFilteredMoviesActivity.class);

                // Get the center of the clicked view, in order to perform a circular reveal
                // animation taking these coordinates as the center of the circle.
                int revealX = (int) (v.getX() + v.getWidth() / 2);
                int revealY = (int) (v.getY() + v.getHeight() / 2);

/*                intent.putExtra(ConfigFilteredMoviesActivity.EXTRA_CIRCULAR_REVEAL_X, revealX);
                intent.putExtra(ConfigFilteredMoviesActivity.EXTRA_CIRCULAR_REVEAL_Y, revealY);

                // Set the sort type, so that ConfigFilteredMoviesActivity knows which settings to
                // show.
                intent.putExtra(ConfigFilteredMoviesActivity.EXTRA_TYPE, typeValue);

                // Start config activity for result. We need to know, as a result, if the
                // configuration has changed on preferences for the current movies sort type.
                Bundle option = ActivityOptions.makeSceneTransitionAnimation(
                        MoviesActivity.this, v, "transition").toBundle();
                startActivityForResult(intent, resultCode, option);*/

                FragmentManager fm = getSupportFragmentManager();
                ConfigFilteredMoviesFragment editNameDialogFragment = ConfigFilteredMoviesFragment.newInstance("Some Title",typeValue,revealX,revealY);
                editNameDialogFragment.show(fm, "fragment_edit_name");
            }
        });
    }
}