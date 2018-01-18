package com.example.android.popularmoviesstage2.utils;

import android.content.Context;

import com.example.android.popularmoviesstage2.R;
import com.github.lzyzsd.circleprogress.DonutProgress;

public class ScoreUtils {
    /**
     * Empty constructor.
     */
    public ScoreUtils() {
    }

    /**
     * Helper method to display users rating.
     *
     * @param context       is the context for getting the required resources to display colors.
     * @param rating        is the users score, from "0.0" to "10.0".
     * @param donutProgress is the DonutProgress view that displays the score.
     */
    public static void setRating(Context context, String rating, DonutProgress donutProgress) {
        if (!rating.equals("0.0")) {
            int scorePercent = Math.round(10 * Float.parseFloat(rating));
            if (scorePercent < 40) {
                // Low rating.
                donutProgress.setUnfinishedStrokeColor(context.getResources().getColor(R.color.colorLowScoreBackground));
                donutProgress.setFinishedStrokeColor(context.getResources().getColor(R.color.colorLowScoreForeground));
            } else if (scorePercent < 70) {
                // Middle rating.
                donutProgress.setUnfinishedStrokeColor(context.getResources().getColor(R.color.colorMediumScoreBackground));
                donutProgress.setFinishedStrokeColor(context.getResources().getColor(R.color.colorMediumScoreForeground));
            } else {
                // Top rating.
                donutProgress.setUnfinishedStrokeColor(context.getResources().getColor(R.color.colorHighScoreBackground));
                donutProgress.setFinishedStrokeColor(context.getResources().getColor(R.color.colorHighScoreForeground));
            }

            // If the score is the highest, delete the decimal.
            if (rating.equals("10.0"))
                donutProgress.setText("10");
            else
                donutProgress.setText(rating);

            donutProgress.setDonut_progress(Integer.toString(scorePercent));
        }
    }
}
