package com.bearever.articlememento.decorator;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bearever.articlememento.model.Section;

/**
 * 文本段落
 * Created by luoming on 2018/3/29.
 */

public class TextSectionDecorator extends SectionDecorator {
    private int mTextColor; //文本颜色
    private int mTextSize; //文本大小
    private int mLineSpace; //文本行间距
    private int mMargin; //编辑框与外界间距

    public TextSectionDecorator(Context mContext) {
        super(mContext);
        init();
    }

    public TextSectionDecorator(Context mContext, Section section) {
        super(mContext, section);
        init();
    }

    private void init() {
        mTextColor = Color.BLACK;
        mTextSize = 16;
        mLineSpace = 14;
        mMargin = 16;
    }

    @Override
    public View display() {
        EditText textView = new EditText(mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(mMargin, 0, mMargin, 0);
        textView.setLayoutParams(layoutParams);
        textView.setTextColor(mTextColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mTextSize);
        textView.setText(getSection().getContent());
        textView.setSelection(getSection().getSelection());
        textView.setLineSpacing(mLineSpace, 1);
        textView.setBackgroundResource(0);
        textView.requestFocus();
        mView = textView;
        //
        action();
        return textView;
    }

    @Override
    public void updateDisplay() {
        if (mView == null)
            return;
        EditText textView = (EditText) mView;
        textView.setText(getSection().getContent());
        textView.requestFocus();
        textView.setSelection(getSection().getSelection());
        //风格
        switch (getSection().getStyle()) {
            case Section.STYLE_NORMAL:
                textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                break;
            case Section.STYLE_BOLD:
                textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                break;
        }

        //重心
        switch (getSection().getGravity()) {
            case Section.GRIVITY_LEFT:
                textView.setGravity(Gravity.START);
                break;

            case Section.GRIVITY_CENTER:
                textView.setGravity(Gravity.CENTER);
                break;
        }
    }


    @Override
    public void action() {
        //首次创建的时候返回值
        if (mListener != null)
            mListener.onChanged(getSection());

        //检测到回车键时不输入回车
        EditText editText = (EditText) mView;
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    return true;
                }
                return false;
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getSection().setContent(s.toString());
                getSection().setSelection(s.length());
                if (mListener != null)
                    mListener.onChanged(getSection());
            }
        });
    }
}
