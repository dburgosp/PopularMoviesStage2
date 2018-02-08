package com.example.android.popularmoviesstage2.utils;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.Html;
import android.widget.TextView;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class TextViewUtils {
    public static final int DRAWABLE_LEFT_INDEX = 0;
    public static final int DRAWABLE_TOP_INDEX = 1;
    public static final int DRAWABLE_RIGHT_INDEX = 2;
    public static final int DRAWABLE_BOTTOM_INDEX = 3;

    /**
     * Empty constructor.
     */
    public TextViewUtils() {
    }

    /**
     * Helper method to set text in a TextView using HTML sintax and to avoid deprecated use of
     * Html.fromHtml depending on the current Android version.
     *
     * @param textView is the TextView to be set.
     * @param html     is the text containing HTML sintax.
     */
    public static void setHtmlText(TextView textView, String html) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY));
        } else {
            textView.setText(Html.fromHtml(html));
        }
    }

    /**
     * Helper method to set text in a TextView using Markdown sintax.
     *
     * @param textView is the TextView to be set.
     * @param markdown is the text containing Markdown sintax.
     */
    public static void setMarkdownText(TextView textView, String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        setHtmlText(textView, renderer.render(document));
    }

    /**
     * Helper method to dynamically set drawables and drawable tints to a TextView. It works
     * regardless of the API level.
     *
     * @param context     is the context of the calling activity.
     * @param textView    is the TextView to set the drawable.
     * @param bound       is the position of the drawable into the TextView. Allowed values are
     *                    {@link TextViewUtils#DRAWABLE_LEFT_INDEX},
     *                    {@link TextViewUtils#DRAWABLE_TOP_INDEX},
     *                    {@link TextViewUtils#DRAWABLE_RIGHT_INDEX} and
     *                    {@link TextViewUtils#DRAWABLE_BOTTOM_INDEX}.
     * @param drawableRes is the resource identifier of the drawable.
     * @param tintRes     is the resource identifier of the tint color.
     * @param paddingRes  is the resource identifier of the drawable padding.
     */
    public static void setTintedCompoundDrawable(Context context, TextView textView, int bound,
                                                 int drawableRes, int tintRes, int paddingRes) {
        // Get drawable from drawable resource.
        Drawable drawable = DrawableCompat.wrap(ContextCompat.getDrawable(context, drawableRes));

        // Get tint from color resource.
        DrawableCompat.setTint(drawable, ContextCompat.getColor(context, tintRes));
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_ATOP);

        // Set drawable.
        switch (bound) {
            case DRAWABLE_LEFT_INDEX:
                textView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                break;
            case DRAWABLE_TOP_INDEX:
                textView.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
                break;
            case DRAWABLE_RIGHT_INDEX:
                textView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
                break;
            default: // DRAWABLE_BOTTOM_INDEX
                textView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
        }

        // Set padding.
        if (paddingRes > 0) {
            int padding = context.getResources().getDimensionPixelSize(paddingRes);
            textView.setCompoundDrawablePadding(padding);
        }
    }
}
