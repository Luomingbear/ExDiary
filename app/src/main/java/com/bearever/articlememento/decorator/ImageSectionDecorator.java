package com.bearever.articlememento.decorator;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bearever.articlememento.model.Section;
import com.bumptech.glide.Glide;

/**
 * 图片段落
 * Created by luoming on 2018/3/29.
 */

public class ImageSectionDecorator extends SectionDecorator {
    private ImageView imageView;
    private ImageView closeView;

    public ImageSectionDecorator(Context mContext) {
        super(mContext);
    }

    public ImageSectionDecorator(Context mContext, Section section) {
        super(mContext, section);
    }

    @Override
    public View display() {
        RelativeLayout root = createRootLayout();
        imageView = new ImageView(mContext);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(params);
        Glide.with(mContext).load(getSection().getContent()).into(imageView);
        root.addView(imageView);
        closeView = new ImageView(mContext);
        closeView.setBackgroundColor(Color.RED);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(80, 80);
        p.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        closeView.setLayoutParams(p);
        root.addView(closeView);
        //
        mView = root;
        action();
        return root;
    }

    private RelativeLayout createRootLayout() {
        RelativeLayout layout = new RelativeLayout(mContext);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(params);
        return layout;
    }

    @Override
    public void updateDisplay() {
        Glide.with(mContext).load(getSection().getContent()).into(imageView);
    }

    @Override
    public void action() {
        //首次创建的时候返回值
        if (mListener != null && !TextUtils.isEmpty(getSection().getContent()))
            mListener.onChanged(getSection());

        //删除就是置空content
        closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSection().setContent("");
                if (mListener != null)
                    mListener.onChanged(getSection());
            }
        });
    }
}
