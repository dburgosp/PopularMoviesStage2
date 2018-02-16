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
    private final int spaceSeparation, orientation;

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
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        if (orientation == VERTICAL_SEPARATION) {
            // Add top margin only to the first element.
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = spaceSeparation;
            }

            // Add bottom margin to every element.
            outRect.bottom = spaceSeparation;
        } else {
            // Add start margin only to the first element.
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.left = spaceSeparation;
            }

            // Add end margin to every element.
            outRect.right = spaceSeparation;
        }
    }
}