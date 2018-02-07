package com.example.android.popularmoviesstage2.utils;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.example.android.popularmoviesstage2.R;
import com.github.lzyzsd.circleprogress.DonutProgress;

public class ScoreUtils {
    /**
     * Empty constructor.
     */
    public ScoreUtils() {
    }

    /**
     * Helper method to display users rating in a DonutProgress.
     *
     * @param context       is the context for getting the required resources to display colors.
     * @param rating        is the users score, from "0.0" to "10.0".
     * @param donutProgress is the DonutProgress view that displays the score.
     * @return true if the rating is greater than 0, false otherwise.
     */
    public static boolean setDonutProgressRating(Context context, String rating,
                                                 DonutProgress donutProgress) {
        boolean ratingSet = false;

        if (!rating.equals("0.0")) {
            ratingSet = true;
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

            // Set the score.
            donutProgress.setDonut_progress(Integer.toString(scorePercent));
        }

        return ratingSet;
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
        int color = context.getResources().getColor(R.color.colorGrey);

        if (!rating.equals("0.0")) {
            int scorePercent = Math.round(10 * Float.parseFloat(rating));
            if (scorePercent < 40) {
                // Low rating.
                color = context.getResources().getColor(R.color.colorLowScoreForeground);
            } else if (scorePercent < 70) {
                // Middle rating.
                color = context.getResources().getColor(R.color.colorMediumScoreForeground);
            } else {
                // Top rating.
                color = context.getResources().getColor(R.color.colorHighScoreForeground);

                // If the score is the highest one, delete the decimal.
                if (rating.equals("10.0"))
                    rating = "10";
            }

            // Formatted score text.
            String stringColor = String.format("%X", color).substring(2);
            htmlText = "<strong><font color=\"#" + stringColor + "\">" + rating + "</font></strong>";
        } else
            htmlText = "N/A";

        // Color for left drawable, if it exists.
        Drawable[] drawables = textView.getCompoundDrawables();
        if (drawables[0] != null) {
            drawables[0].setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }

        // Set the score text.
        TextUtils.setHtmlText(textView, htmlText);
    }
}
