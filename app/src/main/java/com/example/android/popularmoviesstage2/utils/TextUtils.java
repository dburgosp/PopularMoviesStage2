package com.example.android.popularmoviesstage2.utils;

import android.text.Html;
import android.widget.TextView;

public class TextUtils {
    /**
     * Empty constructor.
     */
    public TextUtils() {
    }

    /**
     * Helper method to set text in a TextView using HTML sintax and to avoid deprecated use of
     * Html.fromHtml depending on the current Android version.
     *
     * @param html     is the text containing HTML sintax.
     * @param textView is the TextView to be set.
     */
    public void setHtmlText(String html, TextView textView) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY));
        } else {
            textView.setText(Html.fromHtml(html));
        }
    }
}
