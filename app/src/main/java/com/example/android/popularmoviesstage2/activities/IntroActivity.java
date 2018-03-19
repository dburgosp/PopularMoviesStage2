package com.example.android.popularmoviesstage2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.popularmoviesstage2.R;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;
import com.transitionseverywhere.extra.Scale;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IntroActivity extends AppCompatActivity {

    // Annotate fields with @BindView and views ID for Butter Knife to find and automatically cast
    // the corresponding views.
    @BindView(R.id.intro_layout)
    RelativeLayout introRelativeLayout;
    @BindView(R.id.intro_title)
    TextView introTitle;
    @BindView(R.id.intro_subtitle)
    TextView introSubtitle;
    @BindView(R.id.intro_image1)
    ImageView introImage1;
    @BindView(R.id.intro_image1_underline)
    View introImage1Line;
    @BindView(R.id.intro_image2)
    ImageView introImage2;
    @BindView(R.id.intro_image2_underline)
    View introImage2Line;
    @BindView(R.id.intro_image3)
    ImageView introImage3;
    @BindView(R.id.intro_image3_underline)
    View introImage3Line;

    private ArrayList<View> animatedViews = new ArrayList<>();
    private int animatedViewCurrentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);

        // Animations.
        animatedViews.add(introTitle);
        animatedViews.add(introImage1);
        animatedViews.add(introImage1Line);
        animatedViews.add(introImage2);
        animatedViews.add(introImage2Line);
        animatedViews.add(introImage3);
        animatedViews.add(introImage3Line);
        animatedViews.add(introSubtitle);
        startAnimation();

        introRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * Displays animations for every view in this activity, each one after the previous one.
     */
    private void startAnimation() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Display animation for current view after delay.
                TransitionSet set = new TransitionSet()
                        .addTransition(new Scale(0.7f))
                        .addTransition(new Fade())
                        .setDuration(500)
                        .setInterpolator(new LinearOutSlowInInterpolator());
                TransitionManager.beginDelayedTransition(introRelativeLayout, set);
                animatedViews.get(animatedViewCurrentIndex).setVisibility(View.VISIBLE);

                // Order animation for next view, if there's still another one.
                animatedViewCurrentIndex++;
                if (animatedViewCurrentIndex < animatedViews.size())
                    startAnimation();
            }
        }, 250);
    }
}