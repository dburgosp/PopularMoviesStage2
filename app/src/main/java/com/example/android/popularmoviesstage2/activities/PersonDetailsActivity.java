package com.example.android.popularmoviesstage2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.classes.Tmdb;
import com.example.android.popularmoviesstage2.classes.TmdbPerson;
import com.example.android.popularmoviesstage2.utils.TextViewUtils;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PersonDetailsActivity extends AppCompatActivity {
    private static final String TAG = PersonDetailsActivity.class.getSimpleName();
    public static final String EXTRA_PARAM_PERSON = "person";

    // Annotate fields with @BindView and views ID for Butter Knife to find and automatically cast
    // the corresponding views.
    @BindView(R.id.person_image)
    ImageView imageView;
    @BindView(R.id.person_name)
    TextView nameTextView;
    @BindView(R.id.person_viewpager)
    ViewPager viewPager;
    @BindView(R.id.person_tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.person_toolbar)
    Toolbar toolbar;
    @BindView(R.id.person_collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.person_appbar_layout)
    AppBarLayout appBarLayout;

    private static TmdbPerson person;
    private Bundle options;
    private static Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Define transitions to exit and enter to this activity.
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setBackgroundDrawableResource(R.color.colorPrimaryDark);
        getWindow().setEnterTransition(new Explode());
        getWindow().setExitTransition(new Explode());
        supportPostponeEnterTransition();

        setContentView(R.layout.activity_person_details);
        unbinder = ButterKnife.bind(this);

        // Get parameters from intent.
        Intent intent = getIntent();
        if (intent != null) {
            // Get current person simple information.
            person = intent.getParcelableExtra(EXTRA_PARAM_PERSON);
        }

        // Set the custom tool bar and show the back button.
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set title for this activity, depending on the collapsing state of the toolbar.
        setTitle("");
        AppBarLayout.OnOffsetChangedListener listener = new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (collapsingToolbarLayout.getHeight() + verticalOffset < 2 * ViewCompat.getMinimumHeight(collapsingToolbarLayout)) {
                    // CollapsingToolbar is collapsed. Show person name.
                    String name = person.getName();
                    if (name != null && !name.equals("") && !name.isEmpty())
                        collapsingToolbarLayout.setTitle(name);
                    else
                        collapsingToolbarLayout.setTitle(getResources().getString(R.string.no_name));
                } else {
                    // CollapsingToolbar is expanded. Hide title.
                    collapsingToolbarLayout.setTitle("");
                }
            }
        };
        appBarLayout.addOnOffsetChangedListener(listener);

        // Write the basic person info on activity_person_details.xml header.
        setPersonInfo();

        // Set TabLayout and ViewPager to manage the fragments with info for the current person.
/*        tabLayout.setupWithViewPager(viewPager);
        PersonDetailsFragmentPagerAdapter adapter = new PersonDetailsFragmentPagerAdapter(
                getSupportFragmentManager(), PersonDetailsActivity.this, person);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);*/

        Log.i(TAG, "(onCreate) Activity created");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        imageView = null;
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
    }

    /**
     * This method is called whenever the user chooses to navigate Up within your application's
     * activity hierarchy from the action bar.
     * <p>
     * <p>If a parent was specified in the manifest for this activity or an activity-alias to it,
     * default Up navigation will be handled automatically. See
     * {@link #getSupportParentActivityIntent()} for how to specify the parent. If any activity
     * along the parent chain requires extra Intent arguments, the Activity subclass
     * should override the method onPrepareSupportNavigateUpTaskStack(TaskStackBuilder)
     * to supply those arguments.</p>
     * <p>
     * <p>See <a href="{@docRoot}guide/topics/fundamentals/tasks-and-back-stack.html">Tasks and
     * Back Stack</a> from the developer guide and
     * <a href="{@docRoot}design/patterns/navigation.html">Navigation</a> from the design guide
     * for more information about navigating within your app.</p>
     * <p>
     * <p>See the TaskStackBuilder class and the Activity methods
     * {@link #getSupportParentActivityIntent()}, {@link #supportShouldUpRecreateTask(Intent)}, and
     * {@link #supportNavigateUpTo(Intent)} for help implementing custom Up navigation.</p>
     *
     * @return true if Up navigation completed successfully and this Activity was finished,
     * false otherwise.
     */
    @Override
    public boolean onSupportNavigateUp() {
        // Transition back to the person poster on OLD_MainActivity.
        supportFinishAfterTransition();
        return true;
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
        getMenuInflater().inflate(R.menu.movie_details_menu, menu);
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
     * Helper method to display current person information in the header of this activity.
     */
    void setPersonInfo() {
        Log.i(TAG, "(setPersonInfo) Display person information on the header");

        // Set image, if it exists.
        String profilePath = person.getProfile_path();
        imageView.setTransitionName(getString(R.string.transition_list_to_details));
        if (profilePath != null && !profilePath.equals("") && !profilePath.isEmpty()) {
            Picasso.with(this)
                    .load(Tmdb.TMDB_POSTER_SIZE_W500_URL + profilePath)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(imageView);
        }

        // Set person name.
        String name = person.getName();
        if (name != null && !name.equals("") && !name.isEmpty())
            TextViewUtils.setHtmlText(nameTextView, "<strong><big>" + name + " </big></strong>");
        else
            nameTextView.setText(getResources().getString(R.string.no_name));
    }
}
