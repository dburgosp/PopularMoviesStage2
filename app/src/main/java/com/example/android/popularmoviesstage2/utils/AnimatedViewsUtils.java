package com.example.android.popularmoviesstage2.utils;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.classes.MyBounceInterpolator;

import java.util.ArrayList;

public final class AnimatedViewsUtils {
    private static final String TAG = AnimatedViewsUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link AnimatedViewsUtils}
     * object. This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name AnimatedViewsUtils (and an object instance of AnimatedViewsUtils
     * is not needed).
     */
    private AnimatedViewsUtils(Animation animation) {
    }

    /**
     * Display animations for every view included in animatedViews array.
     *
     * @param animatedViews            is the array of views to be animated.
     * @param animatedViewCurrentIndex is the index to indicate the current element into
     *                                 animatedViews array.
     * @param animation                is the Animation to be performed over the current view.
     * @param delayMillis              is the delay, in milliseconds, to wait before displaying
     *                                 current animation.
     */
    public static void startAnimationQueue(final ArrayList<View> animatedViews,
                                           final int animatedViewCurrentIndex,
                                           final Animation animation,
                                           final int delayMillis) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animation.setDuration(400);

                // Animate current view after delayMillis from start.
                View view = animatedViews.get(animatedViewCurrentIndex);
                view.setVisibility(View.VISIBLE);
                view.startAnimation(animation);

                // Order animation for next view, if there's still another one in the queue.
                int indexCopy = animatedViewCurrentIndex;
                indexCopy++;
                if (indexCopy < animatedViews.size())
                    startAnimationQueue(animatedViews, indexCopy, animation, delayMillis);
            }
        }, delayMillis);
    }

    /**
     * Define transitions to exit/enter from/to an activity.
     */
    public static void setTransitions(Window window) {
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        window.setBackgroundDrawableResource(R.color.colorPrimaryDark);
        window.setEnterTransition(new Slide(Gravity.END));
        window.setExitTransition(new Slide(Gravity.START));
    }

    /**
     * Helper method to play an animation and a sound when a View is clicked.
     *
     * @param context is the context of the calling activity.
     * @param view    is the clicked View.
     */
    public static void animateOnClick(final Context context, View view) {
        // Play sound.
        SoundsUtils.buttonClick(context);

        // Play animation.
        final Animation animation = AnimationUtils.loadAnimation(context, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        animation.setInterpolator(interpolator);
        view.startAnimation(animation);
    }

    /**
     * https://android.jlelse.eu/a-little-thing-that-matter-how-to-reveal-an-activity-with-circular-revelation-d94f9bfcae28
     *
     * @param rootLayout is the View to be animated.
     * @param x          is the horizontal coordinate of the center of the circle.
     * @param y          is the vertical coordinate of the center of the circle.
     */
    public static void revealLayout(final View rootLayout, final int x, final int y) {
        rootLayout.setVisibility(View.INVISIBLE);
        ViewTreeObserver viewTreeObserver = rootLayout.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    float finalRadius = (float) (Math.max(rootLayout.getWidth(),
                            rootLayout.getHeight()) * 1.1);

                    // Create the animator for this view (the start radius is zero).
                    Animator circularReveal = ViewAnimationUtils.createCircularReveal(rootLayout, x,
                            y, 0, finalRadius);
                    circularReveal.setDuration(1000);
                    circularReveal.setInterpolator(new AccelerateInterpolator());

                    // Make the view visible and start the animation.
                    rootLayout.setVisibility(View.VISIBLE);
                    circularReveal.start();

                    rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }
    }

    /**
     * https://android.jlelse.eu/a-little-thing-that-matter-how-to-reveal-an-activity-with-circular-revelation-d94f9bfcae28
     *
     * @param rootLayout is the View to be animated.
     * @param x          is the horizontal coordinate of the center of the circle.
     * @param y          is the vertical coordinate of the center of the circle.
     * @param activity   is the activity to be finished (if it is not null) after performing the
     *                   animation.
     */
    public static void unrevealLayout(final View rootLayout, int x, int y, final Activity activity) {
        float finalRadius = (float) (Math.max(rootLayout.getWidth(), rootLayout.getHeight()) * 1.1);
        Animator circularReveal = ViewAnimationUtils.createCircularReveal(rootLayout, x, y,
                finalRadius, 0);
        circularReveal.setDuration(1000);
        circularReveal.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                rootLayout.setVisibility(View.INVISIBLE);
                if (activity != null)
                    activity.finish();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        circularReveal.start();
    }
}