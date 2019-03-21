package com.bearever.baselib.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.widget.FrameLayout;

import com.bearever.baselib.R;

/**
 * loading 弹窗
 * luoming
 * 2019/2/27
 **/
public class LoadingDialog extends BaseDialog {
    public static final int TYPE_DEFAULT = 0;
    public static final int TYPE_FULLSCREEN = 1;
    public static final int TYPE_TRANSPARENT = 2;
    private FrameLayout rootView;

    public LoadingDialog(Activity activity) {
        super(activity, R.style.Theme_Base_Dialog);
    }

    @Override
    protected void initData(Context context) {

    }

    @Override
    protected void initUI(Context context) {
        setContentView(R.layout.dialog_base_loading);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        rootView = (FrameLayout) findViewById(R.id.content_root);
    }

    @Override
    protected void initAnim(Context context) {
    }

    @Override
    protected void initListener(Context context) {

    }

    /*******************
     * default false
     * *****************/
    public void setFullScreen(int type) {
        switch (type) {
            case TYPE_FULLSCREEN:
                rootView.setBackgroundColor(0xffffffff);
                break;
            case TYPE_TRANSPARENT:
            case TYPE_DEFAULT:
                rootView.setBackgroundColor(0x00ffffff);
                break;

            default:
                break;
        }
    }
}
