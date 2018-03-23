package com.example.android.popularmoviesstage2.utils;

import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;

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
}