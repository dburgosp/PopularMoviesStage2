package com.example.android.popularmoviesstage2.classes;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.utils.TextViewUtils;

/**
 * Public class that provides a customised Toast layout.
 */
public class CustomToast {
    private CustomToast(){

    }

    /**
     * Constructor for objects of this class.
     *
     * @param context     is the context of the calling activity.
     * @param htmlText    is the text to be displayed into the layout.
     * @param drawableRes is the identifier of the drawable to be displayed into the layout.
     */
    public static Toast setCustomToast(Context context, String htmlText, @DrawableRes int drawableRes) {
        // Get custom layout.
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.layout_toast,
                (ViewGroup) ((Activity) context).findViewById(R.id.custom_toast_container));

        // Set text.
        TextView text = (TextView) layout.findViewById(R.id.toast_text);
        TextViewUtils.setHtmlText(text, htmlText);

        // Set image.
        ImageView imageView = (ImageView) layout.findViewById(R.id.toast_image);
        imageView.setImageDrawable(
                context.getResources().getDrawable(drawableRes));

        // Customize toast.
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);

        return toast;
    }
}