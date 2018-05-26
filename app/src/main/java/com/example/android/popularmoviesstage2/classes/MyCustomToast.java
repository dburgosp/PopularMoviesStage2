package com.example.android.popularmoviesstage2.classes;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.utils.TextViewUtils;

/**
 * Public class that provides a customised Toast layout.
 */
public class MyCustomToast {
    private static final String TAG = MyCustomToast.class.getSimpleName();

    /**
     * Empty constructor for objects of this class.
     */
    private MyCustomToast() {
    }

    /**
     * Sets a Toast with a custom layout, an image and a styled text.
     *
     * @param context     is the context of the calling activity.
     * @param title       is the title for the custom toast.
     * @param htmlText    is the text to be displayed into the layout.
     * @param drawableRes is the identifier of the drawable to be displayed into the layout.
     */
    @SuppressWarnings("ConstantConditions")
    public static Toast setCustomToast(Context context, String title, String htmlText,
                                       @DrawableRes int drawableRes) {
        Toast toast = new Toast(context);

        // Get custom layout.
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        try {
            View layout = inflater.inflate(R.layout.layout_custom_toast,
                    (ViewGroup) ((Activity) context).findViewById(R.id.custom_toast_container));

            // Set header.
            TextView titleTextView = (TextView) layout.findViewById(R.id.toast_header);
            titleTextView.setText(title);

            // Set icon for header.
            int colorRes = R.color.colorWhite;
            TextViewUtils.setTintedCompoundDrawable(context, titleTextView,
                    TextViewUtils.DRAWABLE_LEFT_INDEX, drawableRes, colorRes, R.dimen.small_padding);

            // Set text.
            TextView textView = (TextView) layout.findViewById(R.id.toast_text);
            TextViewUtils.setHtmlText(textView, htmlText);

            // Customize toast and return it.
            toast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
        } catch (java.lang.NullPointerException e) {
            Log.e(TAG, "(setCustomToast) Error inflating layout.");
        }
        return toast;
    }
}