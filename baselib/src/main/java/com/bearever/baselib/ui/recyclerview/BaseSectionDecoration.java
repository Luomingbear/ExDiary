package com.bearever.baselib.ui.recyclerview;


import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 带有分组粘着顶部效果：类似微信的通讯录的首字母置顶
 * 需要继承SectionUtil，实现里面的方法，设置自定义的section
 * Created by luoming on 2018/1/10.
 */

public class BaseSectionDecoration extends BaseLineDecoration {
    private static final String TAG = "BaseSectionDecoration";
    private SectionUtil mSectionUtil;

    public BaseSectionDecoration(SectionUtil sectionUtil, int mDecorationWidth, int mColor) {
        super(mDecorationWidth, mColor, LinearLayout.VERTICAL);
        this.mSectionUtil = sectionUtil;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int pos = parent.getChildAdapterPosition(view);
        if (pos == 0) {
            //第一个元素需要设置分组标题
            outRect.top = mSectionUtil.getSectionHeight(pos);
        } else {
            //分组id变化了说明是另外的一个分组了，需要设置分组的section高度
            if (!mSectionUtil.getSectionId(pos).equals(mSectionUtil.getSectionId(pos - 1))) {
                outRect.top = mSectionUtil.getSectionHeight(pos);
            }
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = parent.getChildAt(i);
            int pos = parent.getChildAdapterPosition(view);

            /**
             * 第一个元素默认显示第一个的分组信息
             */
            if (pos == 0) {
                mSectionUtil.drawSection(c, view, pos, view.getTop() - mSectionUtil.getSectionHeight(0));
            }
            /**
             * 如果当前的分组信息和上一个的分组信息不一样，需要显示分组标题
             */
            else if (pos > 0 && !mSectionUtil.getSectionId(pos).equals(mSectionUtil.getSectionId(pos - 1))) {
                mSectionUtil.drawSection(c, view, pos, view.getTop() - mSectionUtil.getSectionHeight(pos));
            }
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        if (parent.getChildCount() == 0)
            return;

        int count = parent.getChildCount();
        forCout:
        if (count >= 2) {
            for (int i = 0; i < count - 1; i++) {
                View view = parent.getChildAt(i);
                int pos = parent.getChildAdapterPosition(view);

                /**
                 * 如果是不同的SectionId,即不是同一个分组,需要绘制分组标题
                 */
                View afterView = parent.getChildAt(i + 1);
                if (!mSectionUtil.getSectionId(pos + 1).equals(mSectionUtil.getSectionId(pos))) {
                    //如果它的top小于section的高度，这说明它进入了固定section范围，这时需要移动上一个分组标题也就是移动顶部的标题
                    if (afterView.getTop() > mSectionUtil.getSectionHeight(pos) &&
                            afterView.getTop() < mSectionUtil.getSectionHeight(pos) + mSectionUtil.getSectionHeight(pos + 1)) {
                        mSectionUtil.drawSection(c, view, pos,
                                afterView.getTop() - mSectionUtil.getSectionHeight(pos + 1) - mSectionUtil.getSectionHeight(pos));
                    } else {
                        mSectionUtil.drawSection(c, view, pos, 0);
                    }
                    break forCout;
                }
                /**
                 * 当前的分组和上一个分组相同的话就也需要显示当前的分组标题在顶部
                 */
                else {
                    //绘制分组标题在顶部
                    mSectionUtil.drawSection(c, view, pos, 0);
                    break forCout;
                }
            }
        } else {
            //分组标题固定显示在顶部
            mSectionUtil.drawSection(c, parent.getChildAt(0), 0, 0);
        }
    }

    /**
     * 用来自定义显示分组的工具
     */
    public interface SectionUtil {
        /**
         * 获取分组头部的id
         *
         * @param position
         * @return
         */
        String getSectionId(int position);

        /**
         * 获取分组的高度
         */
        int getSectionHeight(int position);

        /**
         * 绘制分组的标题
         *
         * @param canvas
         * @param view
         * @param pos    列表的第几个元素
         * @param y      绘制的左上角y坐标
         */
        void drawSection(Canvas canvas, View view, int pos, int y);
    }
}
