package com.example.android.popularmoviesstage2.activities;

import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.adapters.MoviesAdapter;
import com.example.android.popularmoviesstage2.asynctaskloaders.MoviesAsyncTaskLoader;
import com.example.android.popularmoviesstage2.classes.Movie;
import com.example.android.popularmoviesstage2.utils.DisplayUtils;
import com.example.android.popularmoviesstage2.utils.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Movie>> {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int MOVIES_LOADER_ID = 0;

    private String sortOrder = NetworkUtils.SORT_ORDER_POPULAR;
    private MoviesAdapter moviesAdapter;
    private int currentPosition = 0;

    // Annotate fields with @BindView and views ID for Butter Knife to find and automatically cast
    // the corresponding views.
    @BindView(R.id.activity_main_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.activity_main_no_result_text_view)
    TextView noResultsTextView;
    @BindView(R.id.activity_main_loading_indicator)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Get sort order and last saved scroll currentPosition if there is extra data (if we come
        // from MovieDetailsActivity activity).
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("sortOrder"))
                sortOrder = intent.getStringExtra("sortOrder");
//            if (intent.hasExtra("currentPosition"))
//                currentPosition = intent.getIntExtra("currentPosition", 0);
        }

        // Set RecyclerView for displaying movie posters on screen.
        setRecyclerView();

        // Create an AsyncTaskLoader for getting movie information from internet in a separate
        // thread.
        getSupportLoaderManager().initLoader(MOVIES_LOADER_ID, null, this);

        // Title and icon for this activity.
        this.setTitle(getSortOrderText());
        // ActionBar menu = getSupportActionBar();
        // menu.setDisplayShowHomeEnabled(true);
        // menu.setIcon(R.mipmap.ic_launcher);

        Log.i(TAG, "(onCreate) Activity created");
    }

    /**
     * Helper method for setting the RecyclerView in order to display a list of movies with a grid
     * arrangement.
     */
    void setRecyclerView() {
        // Get current display metrics, depending on device rotation.
        final DisplayUtils displayUtils = new DisplayUtils(this);

        // Vertical GridLayoutManager for displaying movie posters with a number of columns
        // determined by the current orientation of the device.
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, displayUtils.getSpanCount());

        // Set the LayoutManager for the RecyclerView.
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        // Set the listener for click events in the Adapter.
        MoviesAdapter.OnItemClickListener listener = new MoviesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Movie movie) {
                // Start "MovieDetailsActivity" activity to show movie details when the current
                // element is clicked.
                Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
                intent.putExtra("sortOrder", sortOrder);
                intent.putExtra("sortOrderText", getSortOrderText());
                intent.putExtra("movie", movie);
                currentPosition = movie.getPosition();
                startActivity(intent);
            }
        };

        // Set the Adapter for the RecyclerView, according to the current display size and
        // orientation.
        moviesAdapter = new MoviesAdapter(new ArrayList<Movie>(),
                displayUtils.getListPosterWidthPixels(),
                displayUtils.getListPosterHeightPixels(),
                listener);
        recyclerView.setAdapter(moviesAdapter);
    }

    /**
     * Helper method that returns a text string describing the current sort order.
     *
     * @return a String with the current sort order.
     */
    private String getSortOrderText() {
        switch (sortOrder) {
            case NetworkUtils.SORT_ORDER_POPULAR:
                return getResources().getString(R.string.order_popular);

            case NetworkUtils.SORT_ORDER_TOP_RATED:
                return getResources().getString(R.string.order_top_rated);

            default:
                return null;
        }
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

        // New sort order for the list of movies.
        String newSortOrder;
        switch (itemId) {
            case R.id.order_popular:
                newSortOrder = NetworkUtils.SORT_ORDER_POPULAR;
                break;

            case R.id.order_top_rated:
                newSortOrder = NetworkUtils.SORT_ORDER_TOP_RATED;
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        // Only load new movies array if we are changing the sort order.
        if (!newSortOrder.equals(sortOrder)) {
            // Set new global sort order and show it on screen.
            sortOrder = newSortOrder;
            String sortOrderText = getSortOrderText();
            Toast.makeText(this, getResources().getString(R.string.sort_order_changed, sortOrderText), Toast.LENGTH_SHORT).show();

            // Update activity title.
            this.setTitle(getResources().getString(R.string.app_name) + " - " + sortOrderText);

            // Fetch new list of movies from TMDB and restore scroll currentPosition to 0, so the
            // new movies list will be displayed from the top.
            getSupportLoaderManager().restartLoader(MOVIES_LOADER_ID, null, this);
            currentPosition = 0;
        }

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
        if (currentPosition == 0)
            currentPosition = moviesAdapter.getPosition();
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
    }

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, Bundle args) {
        if (NetworkUtils.isConnected(this)) {
            // There is an available connection. Fetch results from TMDB.
            progressBar.setVisibility(View.VISIBLE);
            noResultsTextView.setVisibility(View.INVISIBLE);
            URL searchURL = NetworkUtils.buildFetchMoviesListURL(sortOrder);
            Log.i(TAG, "(onCreateLoader) Search URL: " + searchURL.toString());
            return new MoviesAsyncTaskLoader(this, searchURL, NetworkUtils.OPERATION_GET_MOVIES_LIST);
        } else {
            // There is no connection. Show error message.
            progressBar.setVisibility(View.INVISIBLE);
            noResultsTextView.setVisibility(View.VISIBLE);
            noResultsTextView.setText(getResources().getString(R.string.no_connection));
            Log.i(TAG, "(onCreateLoader) No internet connection.");
            return null;
        }
    }

    /**
     * Called when a previously created loader has finished its load.  Note
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
     * the  constructor <em>without</em> passing
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
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
        // Hide progress bar.
        progressBar.setVisibility(View.INVISIBLE);

        // Check if there is an available connection.
        if (NetworkUtils.isConnected(this)) {
            // If there is a valid list of {@link Movie}s, then add them to the adapter's data set.
            if (data != null && !data.isEmpty()) {
                Log.i(TAG, "(onLoadFinished) Search results not null.");
                moviesAdapter.setMoviesArrayList(data);
                moviesAdapter.notifyDataSetChanged();

                // Restore last currentPosition in the grid, if previously saved. This won't work if we
                // try to restore currentPosition before having displayed the results into the adapter.
                recyclerView.getLayoutManager().scrollToPosition(currentPosition);
            } else {
                Log.i(TAG, "(onLoadFinished) No search results.");
                noResultsTextView.setVisibility(View.VISIBLE);
                noResultsTextView.setText(getResources().getString(R.string.no_results));
            }
        } else {
            // There is no connection. Show error message.
            Log.i(TAG, "(onLoadFinished) No connection to internet.");
            noResultsTextView.setVisibility(View.VISIBLE);
            noResultsTextView.setText(getResources().getString(R.string.no_connection));
        }
    }

    /**
     * Called when a previously created loader is being reset, and thus making its data unavailable.
     * The application should at this point remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {
    }
}
