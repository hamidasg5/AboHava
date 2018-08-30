package com.abohava.views;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class HorizontalDividerItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;

    public HorizontalDividerItemDecoration(Drawable drawable) {
        mDivider = drawable.mutate();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getLeft() + parent.getPaddingLeft();
        int right = parent.getRight() - parent.getPaddingRight();

        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();

            final int ty = (int)(child.getTranslationY() + 0.5f);
            int top = child.getBottom() + layoutParams.bottomMargin + ty;
            int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left, top ,right, bottom);
            mDivider.draw(c);
        }
    }
}
