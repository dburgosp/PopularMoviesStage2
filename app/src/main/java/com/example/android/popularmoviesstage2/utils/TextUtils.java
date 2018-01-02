package com.example.android.popularmoviesstage2.utils;

import android.text.Html;
import android.widget.TextView;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

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
}
