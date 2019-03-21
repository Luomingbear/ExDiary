package com.bearever.baselib.ui.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

public abstract class BaseDialog extends Dialog {

    protected Activity activity;

    public BaseDialog(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    public BaseDialog(Activity activity, int theme) {
        this((Context) activity, theme);
        this.activity = activity;
    }

    public BaseDialog(Context context, int theme) {
        super(context, theme);
        initData(context);
        initUI(context);
        initAnim(context);
        initListener(context);

    }

    public void finish() {
        activity.finish();
    }

    protected abstract void initData(Context context);

    protected abstract void initUI(Context context);

    protected abstract void initAnim(Context context);

    protected abstract void initListener(Context context);

    protected void setDialogWidth(double factor) {
        Window dialogWindow = this.getWindow();
//        WindowManager wm = (WindowManager) Application.getInstance().getSystemService(Context.WINDOW_SERVICE);
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.width = (int) (wm.getDefaultDisplay().getWidth() * factor); // 宽度设置为屏幕的factor倍
        dialogWindow.setAttributes(lp);
    }

    protected void setDialogHeight(double factor) {
        Window dialogWindow = this.getWindow();
        //        WindowManager wm = (WindowManager) Application.getInstance().getSystemService(Context.WINDOW_SERVICE);
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.height = (int) (wm.getDefaultDisplay().getHeight() * factor); // 高度设置为屏幕的factor倍
        dialogWindow.setAttributes(lp);
    }

    /**
     * @Description 只显示此dlg, 把其他dlg关闭
     * @author huangwr
     * @date 2016/10/8
     */
    public void showOnly() {
        DialogManager.dismiss(activity);
        if (!this.isShowing() && null != activity) {
            this.show();
        }
    }

    /**
     * @Description 和其它显示不冲突
     * @author ljb
     * @date 2018/8
     */
    public void showCommon() {
        if (!this.isShowing() && null != activity) {
            this.show();
        }
    }

}
