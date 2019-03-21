package com.bearever.baselib.ui.dialog;


import android.app.Activity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class DialogFactory {

    public static final int TYPE_LOADING = 1002;
    public static final int TYPE_ONE_BTN = 1003;
    public static final int TYPE_TWO_BTN = 1004;


    public static <T extends BaseDialog> BaseDialog getDialog(Activity activity, Class<T> clazz) {
        T ret = null;

        try {
            Constructor<T> constructor = clazz.getConstructor(Activity.class);
            ret = constructor.newInstance(activity);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return ret;
    }

//
//    public static void show(Activity activity){
//        DialogFactory.getDialog(activity,BtnTwoDialog.class).show();
//    }

//    public static void showDialog(Activity mContext){
//        DialogManager.getInstance().get(mContext, BtnTwoDialog.class).show("测试",null,null,null,null);
//    }

}
