package com.example.android.popularmoviesstage2.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.classes.Image;
import com.example.android.popularmoviesstage2.fragmentpageradapters.PostersFragmentPagerAdapter;
import com.example.android.popularmoviesstage2.pagetransformers.ZoomOutPageTransformer;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostersActivity extends AppCompatActivity {
    private static final String TAG = PostersActivity.class.getSimpleName();

    @BindView(R.id.full_posters_viewpager)
    ViewPager viewPager;

    private ArrayList<Image> postersArrayList = new ArrayList<>();
    private int currentPoster = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posters);
        ButterKnife.bind(this);

        // Get arguments from calling intent.
        if (getIntent() != null) {
            postersArrayList = getIntent().getParcelableArrayListExtra("postersArray");
            currentPoster = getIntent().getIntExtra("currentPoster", 0);
        }

        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        // Create an adapter that knows which fragment should be shown on each page.
        PostersFragmentPagerAdapter adapter = new PostersFragmentPagerAdapter(getSupportFragmentManager(),
                PostersActivity.this, postersArrayList);

        // Set the adapter onto the view pager and go to the current page.
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(currentPoster);

        Log.i(TAG, "(onCreate) Activity created");
    }
}
