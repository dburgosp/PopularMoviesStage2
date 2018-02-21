package com.example.android.popularmoviesstage2.activities;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.adapters.MoviesShortListAdapter;
import com.example.android.popularmoviesstage2.asynctaskloaders.TmdbMoviesAsyncTaskLoader;
import com.example.android.popularmoviesstage2.classes.Tmdb;
import com.example.android.popularmoviesstage2.classes.TmdbMovie;
import com.example.android.popularmoviesstage2.itemdecorations.SpaceItemDecoration;
import com.example.android.popularmoviesstage2.utils.DateTimeUtils;
import com.example.android.popularmoviesstage2.utils.DisplayUtils;
import com.example.android.popularmoviesstage2.utils.NetworkUtils;
import com.example.android.popularmoviesstage2.utils.TextViewUtils;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    // Annotate fields with @BindView and views ID for Butter Knife to find and automatically cast
    // the corresponding views.
    @BindView(R.id.now_playing_movies_layout)
    LinearLayout nowPlayingMoviesLayout;
    @BindView(R.id.now_playing_movies_view_all)
    TextView nowPlayingMoviesViewAll;
    @BindView(R.id.now_playing_movies_recyclerview)
    RecyclerView nowPlayingMoviesRecyclerview;

    @BindView(R.id.released_this_week_movies_layout)
    LinearLayout thisWeekReleasesMoviesLayout;
    @BindView(R.id.released_this_week_movies_view_all)
    TextView thisWeekReleasesMoviesViewAll;
    @BindView(R.id.released_this_week_movies_recyclerview)
    RecyclerView thisWeekReleasesMoviesRecyclerview;

    @BindView(R.id.upcoming_movies_layout)
    LinearLayout upcomingMoviesLayout;
    @BindView(R.id.upcoming_movies_view_all)
    TextView upcomingMoviesViewAll;
    @BindView(R.id.upcoming_movies_cardview)
    CardView upcomingMoviesCardview;
    @BindView(R.id.upcoming_movies_cardview_image)
    ImageView upcomingMoviesCardviewImage;
    @BindView(R.id.upcoming_movies_cardview_release_date)
    TextView upcomingMoviesCardviewReleaseDate;
    @BindView(R.id.upcoming_movies_cardview_title)
    TextView upcomingMoviesCardviewTitle;
    @BindView(R.id.upcoming_movies_recyclerview)
    RecyclerView upcomingMoviesRecyclerview;

    @BindView(R.id.connection_status_layout)
    LinearLayout connectionStatusLayout;
    @BindView(R.id.connection_status_text)
    TextView connectionStatusText;
    @BindView(R.id.connection_status_loading_indicator)
    ProgressBar connectionStatusLoadingIndicator;

    private boolean allowClicks = true;
    private Unbinder unbinder;
    private MoviesShortListAdapter nowPlayingMoviesAdapter, thisWeekReleasesMoviesAdapter,
            upcomingMoviesAdapter;
    private ArrayList<TmdbMovie> nowPlayingMovies = null, thisWeekReleasesMovies = null,
            upcomingMovies = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Define transitions to exit and enter to this activity.
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setBackgroundDrawableResource(R.color.colorPrimaryDark);
        getWindow().setEnterTransition(new Explode());
        getWindow().setExitTransition(new Explode());

        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Initially clear the layout, set the RecyclerViews and instantiate classes for retrieving
        // the data from TMDB.
        clearLayout();
        setRecyclerViews();
        new MainActivityMoviesList(NetworkUtils.TMDB_NOW_PLAYING_MOVIES_LOADER_ID);
        new MainActivityMoviesList(NetworkUtils.TMDB_THIS_WEEK_RELEASES_MOVIES_LOADER_ID);
        new MainActivityMoviesList(NetworkUtils.TMDB_UPCOMING_MOVIES_LOADER_ID);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // When MovieDetailsActivity has finished, we enabled clicks again. We don't need to know
        // any result from MovieDetailsActivity, only when it has finished.
        allowClicks = true;
        super.onActivityResult(requestCode, resultCode, data);
    }

    /* -------------- */
    /* HELPER METHODS */
    /* -------------- */

    /**
     * Helper method for setting the RecyclerView in order to display a list of movies with a grid
     * arrangement.
     */
    void setRecyclerViews() {
        // Set the LayoutManager for the RecyclerViews.
        int horizontalSeparation = getResources().getDimensionPixelOffset(R.dimen.regular_padding);

        nowPlayingMoviesRecyclerview.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        nowPlayingMoviesRecyclerview.setHasFixedSize(true);
        nowPlayingMoviesRecyclerview.addItemDecoration(new SpaceItemDecoration(horizontalSeparation,
                SpaceItemDecoration.HORIZONTAL_SEPARATION));

        thisWeekReleasesMoviesRecyclerview.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        thisWeekReleasesMoviesRecyclerview.setHasFixedSize(true);
        thisWeekReleasesMoviesRecyclerview.addItemDecoration(new SpaceItemDecoration(horizontalSeparation,
                SpaceItemDecoration.HORIZONTAL_SEPARATION));

        upcomingMoviesRecyclerview.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        upcomingMoviesRecyclerview.setHasFixedSize(true);
        upcomingMoviesRecyclerview.addItemDecoration(new SpaceItemDecoration(horizontalSeparation,
                SpaceItemDecoration.HORIZONTAL_SEPARATION));

        // Set the listener for click events in the adapters.
        MoviesShortListAdapter.OnItemClickListener movieListener =
                new MoviesShortListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(TmdbMovie movie, View clickedView) {
                        if (allowClicks) {
                            // Disable clicks for now, in order to prevent more than one click while
                            // the transition is running. Clicks will be enabled again when we
                            // return from MovieDetailsActivity.
                            allowClicks = false;

                            // Create an ActivityOptions to transition between Activities using
                            // cross-Activity scene animations.
                            ActivityOptionsCompat options =
                                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                                            MainActivity.this, clickedView,
                                            getString(R.string.transition_list_to_details));

                            // Start MovieDetailsActivity to show movie details when the current
                            // element is clicked. We need to know when the other activity finishes,
                            // so we use startActivityForResult. No need a requestCode, we don't
                            // care for any result.
                            Intent intent = new Intent(MainActivity.this,
                                    MovieDetailsActivity.class);
                            intent.putExtra(MovieDetailsActivity.EXTRA_PARAM_MOVIE, movie);
                            startActivityForResult(intent, 0, options.toBundle());
                        }
                    }
                };

        // Set the Adapters for the RecyclerViews.
        nowPlayingMoviesAdapter = new MoviesShortListAdapter(
                R.layout.list_item_poster_horizontal_layout_3, new ArrayList<TmdbMovie>(),
                movieListener);
        nowPlayingMoviesRecyclerview.setAdapter(nowPlayingMoviesAdapter);

        thisWeekReleasesMoviesAdapter = new MoviesShortListAdapter(
                R.layout.list_item_poster_horizontal_layout_2, new ArrayList<TmdbMovie>(),
                movieListener);
        thisWeekReleasesMoviesRecyclerview.setAdapter(thisWeekReleasesMoviesAdapter);

        upcomingMoviesAdapter = new MoviesShortListAdapter(
                R.layout.list_item_poster_horizontal_layout_2, new ArrayList<TmdbMovie>(),
                movieListener);
        upcomingMoviesRecyclerview.setAdapter(upcomingMoviesAdapter);
    }

    /**
     * Helper method to initially clear all the info sections in the layout.
     */
    void clearLayout() {
        nowPlayingMoviesLayout.setVisibility(View.GONE);
        thisWeekReleasesMoviesLayout.setVisibility(View.GONE);
        upcomingMoviesLayout.setVisibility(View.GONE);
    }

    /**
     * Helper method to display the list of now playing movies.
     */
    void setNowPlayingMovies() {
        if (nowPlayingMovies != null && nowPlayingMovies.size() > 0) {
            // Set view all text.
            String viewAllText = getString(R.string.view_all) + " (" +
                    nowPlayingMovies.get(0).getTotal_results() + ")";
            nowPlayingMoviesViewAll.setText(viewAllText);

            // Set the now playing movies list.
            if (nowPlayingMovies.size() > 0) {
                nowPlayingMoviesAdapter.setMoviesArrayList(nowPlayingMovies);
                nowPlayingMoviesAdapter.notifyDataSetChanged();
            } else
                nowPlayingMoviesRecyclerview.setVisibility(View.GONE);

            // Show the now playing movies section.
            nowPlayingMoviesLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Helper method to display the list of movies that are being released this week.
     */
    void setThisWeekReleasedMovies() {
        if (thisWeekReleasesMovies != null && thisWeekReleasesMovies.size() > 0) {
            // Set view all text.
            String viewAllText = getString(R.string.view_all) + " (" +
                    thisWeekReleasesMovies.get(0).getTotal_results() + ")";
            thisWeekReleasesMoviesViewAll.setText(viewAllText);

            // Set the movies list.
            if (thisWeekReleasesMovies.size() > 0) {
                thisWeekReleasesMoviesAdapter.setMoviesArrayList(thisWeekReleasesMovies);
                thisWeekReleasesMoviesAdapter.notifyDataSetChanged();
            } else
                thisWeekReleasesMoviesRecyclerview.setVisibility(View.GONE);

            // Show the this week releases movies section.
            thisWeekReleasesMoviesLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Helper method to display all the movieDetails information in this fragment.
     */
    void setUpcomingMovies() {
        if (upcomingMovies != null && upcomingMovies.size() > 0) {
            // Set view all text.
            String viewAllText = getString(R.string.view_all) + " (" +
                    upcomingMovies.get(0).getTotal_results() + ")";
            upcomingMoviesViewAll.setText(viewAllText);

        /* ------------ */
        /* MAIN ELEMENT */
        /* ------------ */

            // Set background for the main element, if it exists.
            String backdropPath = upcomingMovies.get(0).getBackdrop_path();
            if (backdropPath != null && !backdropPath.equals("") && !backdropPath.isEmpty()) {
                // Draw image.
                backdropPath = Tmdb.TMDB_POSTER_SIZE_W500_URL + backdropPath;
                Picasso.with(MainActivity.this)
                        .load(backdropPath)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(upcomingMoviesCardviewImage);
            }

            // Set width and height for the background image, according to the display dimensions.
            final DisplayUtils displayUtils = new DisplayUtils(MainActivity.this);
            int widthPixels = displayUtils.getFullDisplayBackdropWidthPixels();
            int heightPixels = displayUtils.getFullDisplayBackdropHeightPixels();
            LinearLayout.LayoutParams layoutParams =
                    new LinearLayout.LayoutParams(widthPixels, heightPixels);
            upcomingMoviesCardviewImage.setLayoutParams(layoutParams);

            // Set movie title for the main element.
            String movieTitle = upcomingMovies.get(0).getTitle();
            if (movieTitle != null && !movieTitle.equals("") && !movieTitle.isEmpty())
                upcomingMoviesCardviewTitle.setText(movieTitle);
            else
                upcomingMoviesCardviewTitle.setText(getResources().getString(R.string.no_title));

            // Set release date for the main element. Add a left drawable with grey tint color.
            TextViewUtils.setTintedCompoundDrawable(MainActivity.this,
                    upcomingMoviesCardviewReleaseDate, TextViewUtils.DRAWABLE_LEFT_INDEX,
                    R.drawable.ic_date_range_black_18dp, R.color.colorGrey, R.dimen.tiny_padding);
            String releaseDate = DateTimeUtils.getStringDate(upcomingMovies.get(0).getRelease_date(),
                    DateTimeUtils.DATE_FORMAT_LONG);
            if (releaseDate != null && !releaseDate.equals("") && !releaseDate.isEmpty())
                upcomingMoviesCardviewReleaseDate.setText(releaseDate);
            else
                upcomingMoviesCardviewReleaseDate.setText(getResources().getString(R.string.no_date));

            // Set the listener for click events in the main element.
            upcomingMoviesCardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Open a new MovieDetailsActivity to show detailed info about the current movie.
                    Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
                    intent.putExtra(MovieDetailsActivity.EXTRA_PARAM_MOVIE, upcomingMovies.get(0));
                    startActivity(intent);
                }
            });

        /* ----------- */
        /* MOVIES LIST */
        /* ----------- */

            // Set the upcoming movies list from the second element.
            @SuppressWarnings("unchecked")
            ArrayList<TmdbMovie> upcomingMoviesCopy = (ArrayList<TmdbMovie>) upcomingMovies.clone();
            upcomingMoviesCopy.remove(0);
            if (upcomingMoviesCopy.size() > 0) {
                upcomingMoviesAdapter.setMoviesArrayList(upcomingMoviesCopy);
                upcomingMoviesAdapter.notifyDataSetChanged();
            } else
                upcomingMoviesRecyclerview.setVisibility(View.GONE);

            // Show the upcoming movies section.
            upcomingMoviesLayout.setVisibility(View.VISIBLE);
        }
    }

    /* ----------- */
    /* INNER CLASS */
    /* ----------- */

    // Private inner class to retrieve a given list of movies.
    private class MainActivityMoviesList implements LoaderManager.LoaderCallbacks<ArrayList<TmdbMovie>> {
        private final String TAG = MainActivityMoviesList.class.getSimpleName();

        // Constructor for objects of this class.
        MainActivityMoviesList(int loaderId) {
            // Create an AsyncTaskLoader for retrieving the list of movies.
            getSupportLoaderManager().initLoader(loaderId, null, this);
        }

        /* ------ */
        /* LOADER */
        /* ------ */

        /**
         * Instantiate and return a new Loader for the given ID.
         *
         * @param id   The ID whose loaderId is to be created.
         * @param args Any arguments supplied by the caller.
         * @return Return a new Loader instance that is ready to start loading.
         */
        @Override
        public Loader<ArrayList<TmdbMovie>> onCreateLoader(int id, Bundle args) {
            connectionStatusLayout.setVisibility(View.VISIBLE);
            connectionStatusText.setTextColor(getResources().getColor(R.color.colorWhite));
            connectionStatusText.setVisibility(View.VISIBLE);

            if (NetworkUtils.isConnected(MainActivity.this)) {
                // There is an available connection. Fetch results from TMDB.
                connectionStatusText.setText(getString(R.string.fetching_info));
                connectionStatusLoadingIndicator.setVisibility(View.VISIBLE);
                switch (id) {
                    case NetworkUtils.TMDB_NOW_PLAYING_MOVIES_LOADER_ID:
                        return new TmdbMoviesAsyncTaskLoader(MainActivity.this,
                                Tmdb.TMDB_SORT_BY_NOW_PLAYING, 1,
                                Locale.getDefault().getLanguage(),
                                Locale.getDefault().getCountry());
                    case NetworkUtils.TMDB_THIS_WEEK_RELEASES_MOVIES_LOADER_ID:
                        return new TmdbMoviesAsyncTaskLoader(MainActivity.this,
                                Tmdb.TMDB_SORT_BY_THIS_WEEK_RELEASES, 1,
                                Locale.getDefault().getLanguage(),
                                Locale.getDefault().getCountry());
                    default:
                        return new TmdbMoviesAsyncTaskLoader(MainActivity.this,
                                Tmdb.TMDB_SORT_BY_UPCOMING, 1,
                                Locale.getDefault().getLanguage(),
                                Locale.getDefault().getCountry());
                }
            } else {
                // There is no connection. Show error message.
                connectionStatusText.setText(getResources().getString(R.string.no_connection));
                connectionStatusLoadingIndicator.setVisibility(View.INVISIBLE);
                Log.i(TAG, "(onCreateLoader) No internet connection.");
                return null;
            }
        }

        /**
         * Called when a previously created loaderId has finished its load.  Note
         * that normally an application is <em>not</em> allowed to commit fragment
         * transactions while in this call, since it can happen after an
         * activity's state is saved.  See {@link FragmentManager#beginTransaction()
         * FragmentManager.openTransaction()} for further discussion on this.
         * <p>
         * <p>This function is guaranteed to be called prior to the release of
         * the last data that was supplied for this Loader.  At this point
         * you should remove all use of the old data (since it will be released
         * soon), but should not do your own release of the data since its Loader
         * owns it and will take care of that.  The Loader will take care of
         * management of its data so you don't have to.  In particular:
         * <p>
         * <ul>
         * <li> <p>The Loader will monitor for changes to the data, and report
         * them to you through new calls here.  You should not monitor the
         * data yourself.  For example, if the data is a {@link Cursor}
         * and you place it in a {@link CursorAdapter}, use
         * the {@link CursorAdapter(Context, Cursor, int)} constructor <em>without</em> passing
         * in either {@link CursorAdapter#FLAG_AUTO_REQUERY}
         * or {@link CursorAdapter#FLAG_REGISTER_CONTENT_OBSERVER}
         * (that is, use 0 for the flags argument).  This prevents the CursorAdapter
         * from doing its own observing of the Cursor, which is not needed since
         * when a change happens you will get a new Cursor throw another call
         * here.
         * <li> The Loader will release the data once it knows the application
         * is no longer using it.  For example, if the data is
         * a {@link Cursor} from a {@link CursorLoader},
         * you should not call close() on it yourself.  If the Cursor is being placed in a
         * {@link CursorAdapter}, you should use the
         * {@link CursorAdapter#swapCursor(Cursor)}
         * method so that the old Cursor is not closed.
         * </ul>
         *
         * @param loader The Loader that has finished.
         * @param data   The data generated by the Loader.
         */
        @Override
        public void onLoadFinished(Loader<ArrayList<TmdbMovie>> loader, ArrayList<TmdbMovie> data) {
            // Hide connection layout.
            connectionStatusLayout.setVisibility(View.GONE);

            // Check if there is an available connection.
            if (NetworkUtils.isConnected(MainActivity.this)) {
                // If there is a valid result, then update its data into the current {@link TmdbMovieDetails}
                // object.
                if (data != null) {
                    Log.i(TAG, "(onLoadFinished) Search results not null.");

                    // Get movies list and display it.
                    switch (loader.getId()) {
                        case NetworkUtils.TMDB_NOW_PLAYING_MOVIES_LOADER_ID: {
                            nowPlayingMovies = data;
                            setNowPlayingMovies();
                            break;
                        }
                        case NetworkUtils.TMDB_THIS_WEEK_RELEASES_MOVIES_LOADER_ID: {
                            thisWeekReleasesMovies = data;
                            setThisWeekReleasedMovies();
                            break;
                        }
                        default: {
                            upcomingMovies = data;
                            setUpcomingMovies();
                        }
                    }
                } else {
                    Log.i(TAG, "(onLoadFinished) No search results.");
                    connectionStatusText.setTextColor(getResources().getColor(R.color.colorWhite));
                    connectionStatusText.setText(getResources().getString(R.string.no_results));
                    connectionStatusText.setVisibility(View.VISIBLE);
                }
            } else

            {
                // There is no connection. Show error message.
                Log.i(TAG, "(onLoadFinished) No connection to internet.");
                connectionStatusText.setTextColor(getResources().getColor(R.color.colorWhite));
                connectionStatusText.setText(getResources().getString(R.string.no_connection));
                connectionStatusText.setVisibility(View.VISIBLE);
            }

        }

        /**
         * Called when a previously created loaderId is being reset, and thus
         * making its data unavailable.  The application should at this point
         * remove any references it has to the Loader's data.
         *
         * @param loader The Loader that is being reset.
         */
        @Override
        public void onLoaderReset(Loader<ArrayList<TmdbMovie>> loader) {
        }
    }
}
