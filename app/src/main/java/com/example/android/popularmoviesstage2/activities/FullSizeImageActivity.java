package com.example.android.popularmoviesstage2.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.classes.TmdbImage;
import com.example.android.popularmoviesstage2.fragmentpageradapters.FullSizeImagesFragmentPagerAdapter;
import com.example.android.popularmoviesstage2.pagetransformers.DepthPageTransformer;
import com.example.android.popularmoviesstage2.pagetransformers.ZoomOutPageTransformer;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FullSizeImageActivity extends AppCompatActivity {
    private static final String TAG = FullSizeImageActivity.class.getSimpleName();
    private ArrayList<TmdbImage> imagesArrayList = new ArrayList<>();
    private int currentImage = 0;
    private int imageType;

    @BindView(R.id.full_posters_viewpager)
    ViewPager viewPager;

    // Public TmdbImage types.
    public static final int IMAGE_TYPE_POSTER = 1;
    public static final int IMAGE_TYPE_BACKDROP = 2;

    // Public extra strings.
    public static final String EXTRA_PARAM_IMAGES_ARRAYLIST = "imagesArrayList";
    public static final String EXTRA_PARAM_CURRENT_IMAGE = "currentImage";
    public static final String EXTRA_PARAM_IMAGE_TYPE = "imageType";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_full_size_images);
        ButterKnife.bind(this);

        // Get arguments from calling intent.
        if (getIntent() != null) {
            imagesArrayList = getIntent().getParcelableArrayListExtra(EXTRA_PARAM_IMAGES_ARRAYLIST);
            currentImage = getIntent().getIntExtra(EXTRA_PARAM_CURRENT_IMAGE, 0);
            imageType = getIntent().getIntExtra(EXTRA_PARAM_IMAGE_TYPE, 1);
        }

        // Set layout orientation and animation between ViewPager elements according to image type.
        switch (imageType) {
            case IMAGE_TYPE_POSTER: {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                viewPager.setPageTransformer(true, new DepthPageTransformer());
                break;
            }
            default: {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
            }
        }

        // Create an adapter that knows which fragment should be shown on each page.
        FullSizeImagesFragmentPagerAdapter adapter = new FullSizeImagesFragmentPagerAdapter(getSupportFragmentManager(),
                FullSizeImageActivity.this, imagesArrayList, imageType);

        // Set the adapter onto the view pager and go to the current page.
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(currentImage);

        Log.i(TAG, "(onCreate) Activity created");
    }
}
