package com.example.android.popularmoviesstage2.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.classes.Image;
import com.example.android.popularmoviesstage2.fragmentpageradapters.PostersFragmentPagerAdapter;
import com.example.android.popularmoviesstage2.pagetransformers.DepthPageTransformer;
import com.example.android.popularmoviesstage2.pagetransformers.ZoomOutPageTransformer;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FullSizeImageActivity extends AppCompatActivity {
    private static final String TAG = FullSizeImageActivity.class.getSimpleName();
    private ArrayList<Image> imagesArrayList = new ArrayList<>();
    private int currentImage = 0;
    private int imageType;

    @BindView(R.id.full_posters_viewpager)
    ViewPager viewPager;

    public static final int IMAGE_TYPE_POSTER = 1;
    public static final int IMAGE_TYPE_BACKDROP = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_posters);
        ButterKnife.bind(this);

        // Get arguments from calling intent.
        if (getIntent() != null) {
            imagesArrayList = getIntent().getParcelableArrayListExtra("imagesArrayList");
            currentImage = getIntent().getIntExtra("currentImage", 0);
            imageType = getIntent().getIntExtra("imageType", 1);
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
        PostersFragmentPagerAdapter adapter = new PostersFragmentPagerAdapter(getSupportFragmentManager(),
                FullSizeImageActivity.this, imagesArrayList);

        // Set the adapter onto the view pager and go to the current page.
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(currentImage);

        Log.i(TAG, "(onCreate) Activity created");
    }
}
