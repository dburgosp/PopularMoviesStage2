package com.example.android.popularmoviesstage2.utils;

import android.content.Context;
import android.os.Handler;
import android.widget.TextView;

import com.example.android.popularmoviesstage2.R;
import com.github.lzyzsd.circleprogress.DonutProgress;

import java.text.DecimalFormat;

public class ScoreUtils {
    /**
     * Empty constructor.
     */
    public ScoreUtils() {
    }

    /**
     * Helper method to display users rating in a DonutProgress.
     *
     * @param context       is the context of the calling activity.
     * @param rating        is the users score, from "0.0" to "10.0".
     * @param donutProgress is the DonutProgress view that displays the score.
     * @return true if the rating is greater than 0, false otherwise.
     */
    public static boolean setDonutProgressRating(final Context context, String rating,
                                                 DonutProgress donutProgress) {
        boolean ratingSet = false;

        if (!rating.equals("0.0")) {
            ratingSet = true;
            final int scorePercent = Math.round(10 * Float.parseFloat(rating));

            // Set score in the donutProgress, running an animation from 0 to the final score value.
            animateDonutProgress(context, donutProgress, 0, scorePercent);
        }

        return ratingSet;
    }

    /**
     * Private helper method to animate recursively the score into a DonutProgress.
     *
     * @param context         is the context for getting the required resources to display colors.
     * @param donutProgress   is the DonutProgress view that displays the score.
     * @param currentProgress is the current progress to be displayed.
     * @param finalProgress   is the final score to be displayed in the DonutProgress.
     */
    private static void animateDonutProgress(final Context context, final DonutProgress donutProgress,
                                             final int currentProgress, final int finalProgress) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Set text.
                DecimalFormat decimalFormat = new DecimalFormat("0.0");
                Double currentProgressDouble = currentProgress / 10.0;
                String currentProgressString = decimalFormat.format(currentProgressDouble);
                if (currentProgress == 100)
                    donutProgress.setText("10");
                else
                    donutProgress.setText(currentProgressString);

                // Set colors.
                if (currentProgress < 40) {
                    // Low rating.
                    donutProgress.setUnfinishedStrokeColor(
                            context.getResources().getColor(R.color.colorLowScoreBackground));
                    donutProgress.setFinishedStrokeColor(
                            context.getResources().getColor(R.color.colorLowScoreForeground));
                } else if (currentProgress < 70) {
                    // Middle rating.
                    donutProgress.setUnfinishedStrokeColor(
                            context.getResources().getColor(R.color.colorMediumScoreBackground));
                    donutProgress.setFinishedStrokeColor(
                            context.getResources().getColor(R.color.colorMediumScoreForeground));
                } else {
                    // Top rating.
                    donutProgress.setUnfinishedStrokeColor(
                            context.getResources().getColor(R.color.colorHighScoreBackground));
                    donutProgress.setFinishedStrokeColor(
                            context.getResources().getColor(R.color.colorHighScoreForeground));
                }

                // Set progress.
                donutProgress.setDonut_progress(Integer.toString(currentProgress));

                // Prepare next iteration, if required.
                if (currentProgress < finalProgress) {
                    int newCurrentProgress = currentProgress;
                    newCurrentProgress++;
                    animateDonutProgress(context, donutProgress, newCurrentProgress, finalProgress);
                }
            }
        }, 5); // Next iteration 5 milliseconds later.
    }

    /**
     * Helper method to format TMDB users rating into a TextView.
     *
     * @param context  is the context for getting the required resources to display colors.
     * @param rating   is the users score, from "0.0" to "10.0".
     * @param textView is the TextView where we write the score.
     */
    public static void setTextViewRating(Context context, String rating, TextView textView) {
        String htmlText;
        int colorRes = R.color.colorGrey;

        if (!rating.equals("0.0")) {
            int scorePercent = Math.round(10 * Float.parseFloat(rating));
            if (scorePercent < 40) {
                // Low rating.
                colorRes = R.color.colorLowScoreForeground;
            } else if (scorePercent < 70) {
                // Middle rating.
                colorRes = R.color.colorMediumScoreForeground;
            } else {
                // Top rating.
                colorRes = R.color.colorHighScoreForeground;

                // If the score is the highest one, delete the decimal.
                if (rating.equals("10.0"))
                    rating = "10";
            }

            // Formatted score text.
            String stringColor =
                    String.format("%X", context.getResources().getColor(colorRes)).substring(2);
            htmlText =
                    "<strong><font color=\"#" + stringColor + "\">" + rating + "</font></strong>";
        } else
            htmlText = "n/a";

        // Color for left drawable.
        TextViewUtils.setTintedCompoundDrawable(context, textView,
                TextViewUtils.DRAWABLE_LEFT_INDEX, R.drawable.ic_star_black_18dp, colorRes, R.dimen.separator);

        // Set the score text.
        TextViewUtils.setHtmlText(textView, htmlText);
    }
}
