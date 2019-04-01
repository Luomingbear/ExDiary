package com.bearever.articlememento.decorator;

import android.content.Context;
import android.view.View;

import com.bearever.articlememento.model.Section;

/**
 * 段落的基类
 * Created by luoming on 2018/3/29.
 */

public abstract class SectionDecorator {
    protected Context mContext;
    protected View mView; //构造成的view
    private Section section = new Section();
    protected OnSectionChangedListener mListener;

    public SectionDecorator(Context mContext) {
        this.mContext = mContext;
    }

    public SectionDecorator(Context mContext, Section section) {
        this.mContext = mContext;
        this.section = section;
    }

    public void setOnSectionChangedListener(OnSectionChangedListener mListener) {
        this.mListener = mListener;
    }

    /**
     * 显示view
     * 在return之间设置mView = 生成的view
     *
     * @return 返回一个view对象用于显示
     */
    public abstract View display();

    /**
     * 更新Ui显示
     */
    public abstract void updateDisplay();

    /**
     * 对该对象进行一些交互行为设计，例如点击响应
     */
    public abstract void action();

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
        updateDisplay();
    }

    public interface OnSectionChangedListener {
        void onChanged(Section section);
    }
}
