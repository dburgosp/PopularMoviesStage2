package com.example.android.popularmoviesstage2;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmoviesstage2.utils.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int VERTICAL_SPAN_COUNT = 3;
    private static final int HORIZONTAL_SPAN_COUNT = 4;
    // Annotate fields with @BindView and views ID for Butter Knife to find and automatically cast
    // the corresponding views.
    @BindView(R.id.activity_main_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.activity_main_no_result_text_view)
    TextView noResultsTextView;
    @BindView(R.id.activity_main_loading_indicator)
    ProgressBar progressBar;
    private String sortOrder = NetworkUtils.SORT_ORDER_POPULAR;
    private MoviesAdapter moviesAdapter;
    private int currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Get sort order and last saved scroll currentPosition if there is extra data (if we come from
        // MovieDetails activity).
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("sortOrder")) sortOrder = intent.getStringExtra("sortOrder");
            if (intent.hasExtra("currentPosition"))
                currentPosition = intent.getIntExtra("currentPosition", 0);
        }

        // Set RecyclerView for displaying movie posters on screen.
        setRecyclerView();

        // Create an AsyncTask for getting movie information from internet in a separate thread.
        if (savedInstanceState == null) {
            // Here we only fetch results the first time that this activity is created. Otherwise,
            // setAsyncTask will be called from onRestoreInstanceState. Thus we avoid to fetch data
            // from TMBD two times if we rotate the device on this activity.
            setAsyncTask();
        }

        Log.i(TAG, "(onCreate) Activity created");
    }

    /**
     * Helper method for setting the RecyclerView in order to display a list of movies with a grid
     * arrangement.
     */
    void setRecyclerView() {
        // Get current display metrics.
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final int displaWidthPixels = metrics.widthPixels;
        final int displayHeightPixels = metrics.heightPixels;

        // Vertical GridLayoutManager for displaying movie posters with a number of columns
        // determined by the current orientation of the device.
        int spanCount;
        final int detailsPosterlWidthPixels;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.i(TAG, "(setRecyclerView) Portrait orientation");
            spanCount = VERTICAL_SPAN_COUNT;
            detailsPosterlWidthPixels = (displaWidthPixels / 2) - ((getResources().getDimensionPixelSize(R.dimen.regular_padding)) * 2);
        } else {
            Log.i(TAG, "(setRecyclerView) Landscape orientation");
            spanCount = HORIZONTAL_SPAN_COUNT;

            // When display orientation is landscape, we use screen height for setting the poster
            // width for the movie details activity.
            detailsPosterlWidthPixels = (displayHeightPixels / 2) - ((getResources().getDimensionPixelSize(R.dimen.regular_padding)) * 2);
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanCount);

        // Set the LayoutManager for the RecyclerView.
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        // Set the listener for click events in the Adapter.
        MoviesAdapter.OnItemClickListener listener = new MoviesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Movie movie) {
                // Start "MovieDetails" activity to show movie details when the current element is
                // clicked.
                Intent intent = new Intent(MainActivity.this, MovieDetails.class);
                intent.putExtra("sortOrder", sortOrder);
                intent.putExtra("movie", movie);
                currentPosition = movie.getPosition();

                // Set sizes for the poster in the movie details activity.
                int detailsPosterHeightPixels = 750 * detailsPosterlWidthPixels / 500;
                Log.i(TAG, "(setRecyclerView) Poster width for movie details activity: " + detailsPosterlWidthPixels);
                Log.i(TAG, "(setRecyclerView) Poster height for movie details activity: " + detailsPosterHeightPixels);
                intent.putExtra("widthPixels", detailsPosterlWidthPixels);
                intent.putExtra("heightPixels", detailsPosterHeightPixels);

                startActivity(intent);
            }
        };

        // Number of columns for the movies list in the RecyclerView (spanCount) is different
        // depending on device rotation, so poster width for this list also depends on device
        // rotation.
        int listPosterWidthPixels = displaWidthPixels / spanCount;
        int listPosterHeightPixels = 750 * listPosterWidthPixels / 500;
        Log.i(TAG, "(setRecyclerView) Poster width for movies list: " + listPosterWidthPixels);
        Log.i(TAG, "(setRecyclerView) Poster height for movies list: " + listPosterHeightPixels);

        // Set the Adapter for the RecyclerView, according to the current display size and
        // orientation.
        moviesAdapter = new MoviesAdapter(new ArrayList<Movie>(), listPosterWidthPixels, listPosterHeightPixels, listener);
        recyclerView.setAdapter(moviesAdapter);
    }

    /**
     * Helper method to create a new async task for fetching results from TheMovieDB using a
     * certain sortOrder.
     */
    void setAsyncTask() {
        if (isConnected()) {
            // There is an available connection. Fetch results from TMDB.
            progressBar.setVisibility(View.VISIBLE);
            noResultsTextView.setVisibility(View.INVISIBLE);
            URL searchURL = NetworkUtils.buildURL(sortOrder);
            Log.i(TAG, "(setAsyncTask) Search URL: " + searchURL.toString());
            new MoviesAsyncTask(new MoviesAsyncTaskCompleteListener()).execute(searchURL);
        } else {
            // There is no connection. Show error message.
            progressBar.setVisibility(View.INVISIBLE);
            noResultsTextView.setVisibility(View.VISIBLE);
            noResultsTextView.setText(getResources().getString(R.string.no_connection));
        }
    }

    /**
     * Helper method to determine if there is an active network connection.
     *
     * @return true if we are connected to the internet, false otherwise.
     */
    boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    /**
     * Initialize the contents of the Activity's standard options menu.  You
     * should place your menu items in to <var>menu</var>.
     * <p>
     * <p>This is only called once, the first time the options menu is
     * displayed.  To update the menu every time it is displayed, see
     * {@link #onPrepareOptionsMenu}.
     * <p>
     * <p>The default implementation populates the menu with standard system
     * menu items.  These are placed in the {@link Menu#CATEGORY_SYSTEM} group so that
     * they will be correctly ordered with application-defined menu items.
     * Deriving classes should always call through to the base implementation.
     * <p>
     * <p>You can safely hold on to <var>menu</var> (and any items created
     * from it), making modifications to it as desired, until the next
     * time onCreateOptionsMenu() is called.
     * <p>
     * <p>When you add items to the menu, you can implement the Activity's
     * {@link #onOptionsItemSelected} method to handle them there.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     * <p>
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.</p>
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        Log.i(TAG, "(onOptionsItemSelected) Item ID: " + itemId);

        // Set sort order for the list of movies.
        switch (itemId) {
            case R.id.order_popular:
                sortOrder = NetworkUtils.SORT_ORDER_POPULAR;
                break;

            case R.id.order_top_rated:
                sortOrder = NetworkUtils.SORT_ORDER_TOP_RATED;
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        // Restore scroll currentPosition to 0, so the new movies list will be displayed from the top.
        currentPosition = 0;

        // Fetch list of movies from TMDB.
        setAsyncTask();

        return true;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save current sort order.
        outState.putString("sortOrder", sortOrder);

        // Save current position in the grid. If there is no positive stored value for the current
        // position (we have not clicked on a movie poster, so currentPosition contains the default
        // value 0) we must read the current position in the adapter from the helper method
        // {@link MoviesAdapter#getPosition()}.
        if (currentPosition == 0) currentPosition = moviesAdapter.getPosition();
        outState.putInt("currentPosition", currentPosition);
    }

    /**
     * This method is called after {@link #onStart} when the activity is
     * being re-initialized from a previously saved state, given here in
     * <var>savedInstanceState</var>.  Most implementations will simply use {@link #onCreate}
     * to restore their state, but it is sometimes convenient to do it here
     * after all of the initialization has been done or to allow subclasses to
     * decide whether to use your default implementation.  The default
     * implementation of this method performs a restore of any view state that
     * had previously been frozen by {@link #onSaveInstanceState}.
     * <p>
     * <p>This method is called between {@link #onStart} and
     * {@link #onPostCreate}.
     *
     * @param savedInstanceState the data most recently supplied in {@link #onSaveInstanceState}.
     * @see #onCreate
     * @see #onPostCreate
     * @see #onResume
     * @see #onSaveInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Restore sort order and last saved currentPosition in the grid.
        sortOrder = savedInstanceState.getString("sortOrder");
        currentPosition = savedInstanceState.getInt("currentPosition");

        // Retrieve data from TMDB.
        setAsyncTask();
    }

    /**
     * Inner class for allowing to create a separate file for the MoviesAsyncTask.
     */
    private class MoviesAsyncTaskCompleteListener implements MoviesAsyncTask.AsyncTaskCompleteListener<ArrayList<Movie>> {
        @Override
        public void onTaskComplete(ArrayList<Movie> searchResults) {
            // Hide progress bar.
            progressBar.setVisibility(View.INVISIBLE);

            // Check if there is an available connection.
            if (isConnected()) {
                // If there is a valid list of {@link Movie}s, then add them to the adapter's data set.
                if (searchResults != null && !searchResults.isEmpty()) {
                    Log.i(TAG, "(onPostExecute) Search results not null.");
                    moviesAdapter.setMoviesArray(searchResults);
                    moviesAdapter.notifyDataSetChanged();

                    // Restore last currentPosition in the grid, if previously saved. This won't work if we
                    // try to restore currentPosition before having displayed the results into the adapter.
                    recyclerView.getLayoutManager().scrollToPosition(currentPosition);
                } else {
                    Log.i(TAG, "(onPostExecute) No search results.");
                    noResultsTextView.setVisibility(View.VISIBLE);
                    noResultsTextView.setText(getResources().getString(R.string.no_results));
                }
            } else {
                // There is no connection. Show error message.
                Log.i(TAG, "(onPostExecute) No connection to internet.");
                noResultsTextView.setVisibility(View.VISIBLE);
                noResultsTextView.setText(getResources().getString(R.string.no_connection));
            }
        }
    }
}
