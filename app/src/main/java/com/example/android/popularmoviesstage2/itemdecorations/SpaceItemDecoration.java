package com.example.android.popularmoviesstage2.itemdecorations;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * ItemDecoration for adding vertical or horizontal separation between RecyclerView elements.
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    public static final int HORIZONTAL_SEPARATION = 0;
    public static final int VERTICAL_SEPARATION = 1;
    public static final int HORIZONTAL_VERTICAL_SEPARATION = 2;
    private final int spaceSeparation, orientation, spanCount;

    /**
     * Constructor for elements of this class.
     *
     * @param spaceSeparation is the vertical or horizontal separation to apply.
     * @param orientation     indicates the orientation to apply the vertical (VERTICAL_SEPARATION)
     *                        or horizontal (HORIZONTAL_SEPARATION) separation.
     */
    public SpaceItemDecoration(int spaceSeparation, int orientation) {
        this.spaceSeparation = spaceSeparation;
        this.orientation = orientation;
        this.spanCount = 0;
    }

    /**
     * Another constructor for elements of this class.
     *
     * @param spaceSeparation is the vertical or horizontal separation to apply.
     * @param orientation     indicates the orientation to apply the vertical (VERTICAL_SEPARATION)
     *                        or horizontal (HORIZONTAL_SEPARATION) separation.
     * @param spanCount       is the number of columns if we are using a GridLayout.
     */
    public SpaceItemDecoration(int spaceSeparation, int orientation, int spanCount) {
        this.spaceSeparation = spaceSeparation;
        this.orientation = orientation;
        this.spanCount = spanCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        switch (orientation) {
            case VERTICAL_SEPARATION: {
                addBottomMargin(outRect, view, parent);
                break;
            }
            case HORIZONTAL_VERTICAL_SEPARATION: {
                addBottomMargin(outRect, view, parent);
                addEndMargin(outRect, view, parent);
                break;
            }
            default: {
                addEndMargin(outRect, view, parent);
            }
        }
    }

    private void addBottomMargin(Rect outRect, View view, RecyclerView parent) {
        // Add bottom margin to every element and top margin only to the first element, or to every
        // element if we are using a GridLayout (spanCount is greater than 0).
        outRect.bottom = spaceSeparation;
        if ((parent.getChildAdapterPosition(view) == 0) || (spanCount > 0)) {
            outRect.top = spaceSeparation;
        }
    }

    private void addEndMargin(Rect outRect, View view, RecyclerView parent) {
        // Add right margin to every element and left margin only to the first element, or to every
        // first element of every row if we are using a GridLayout (spanCount is greater than 0).
        outRect.right = spaceSeparation;
        if (parent.getChildAdapterPosition(view) == 0 || (spanCount > 0)) {
                //((spanCount > 0) && (parent.getChildAdapterPosition(view) % spanCount == 0))) {
            outRect.left = spaceSeparation;
        }
    }
}