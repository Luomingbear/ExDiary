package com.bearever.baselib.ui.recyclerview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 基本的直线分割线
 * Created by luoming on 2018/1/9.
 */

public class BaseLineDecoration extends RecyclerView.ItemDecoration {
    private int mDecorationWidth = 1; //分割线宽度
    private int mColor = Color.GRAY; //分割线颜色
    private int mGravity = LinearLayout.VERTICAL; //垂直排列或者水平排列
    private Paint mPaint;

    public BaseLineDecoration(int mDecorationWidth, int mColor, int gravity) {
        this.mDecorationWidth = mDecorationWidth;
        this.mColor = mColor;
        this.mGravity = gravity;

        mPaint = new Paint();
        mPaint.setColor(mColor);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (mGravity == LinearLayout.HORIZONTAL) {
            outRect.right = mDecorationWidth;
        } else if (mGravity == LinearLayout.VERTICAL) {
            outRect.bottom = mDecorationWidth;
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        for (int i = 0; i < childCount - 1; i++) {
            View view = parent.getChildAt(i);
            float top = view.getBottom();
            float bottom = view.getBottom() + mDecorationWidth;
            c.drawRect(left, top, right, bottom, mPaint);
        }
    }
}
