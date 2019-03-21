package com.bearever.baselib.ui.dialog;


import android.app.Activity;
import android.support.v4.util.ArrayMap;

import java.util.Map;
import java.util.Set;

public class DialogManager {

    private ArrayMap<Activity, BaseDialog>                  map;
    private ArrayMap<Activity, ArrayMap<Class, BaseDialog>> map2;

    /* 单例 beign */
    private DialogManager() {
//        map = new ArrayMap<Activity, BaseDialog>();
        map2 = new ArrayMap<Activity, ArrayMap<Class, BaseDialog>>();
        System.out.println("DialogManager Constructor");
    }

    private static class DialogManagerHolder {
        static DialogManager instance = new DialogManager();
    }

    public static DialogManager getInstance() {
        return DialogManagerHolder.instance;
    }
    /* 单例 end */

    public static <T extends BaseDialog> T get(Activity activity, Class<T> clazz){
        return getInstance().queryDialog(activity, clazz);
    }

    public static void dismissOnly(Activity activity, Class<? extends BaseDialog> clazz){
        getInstance().dismissDlg(activity,clazz);
    }

    public static void dismiss(Activity activity){
        getInstance().dismissAllDlgs(activity);
    }

    public static void destory(Activity activity) {
        getInstance().destoryDlgs(activity);
    }



    private void useDlg(Activity activity, BaseDialog dlg) {
        map.put(activity, dlg);
        dlg.show();
    }

    private <T extends BaseDialog> T queryDialog(Activity activity, Class<T> clazz) {

//        BaseDialog dlg = map.get(activity);
        BaseDialog dlg = null;
        ArrayMap<Class, BaseDialog> dialogMap = map2.get(activity);
        if (null == dialogMap) {
            dialogMap=new ArrayMap<Class, BaseDialog>(1);
            map2.put(activity, dialogMap);
        } else {
            dlg = dialogMap.get(clazz);
        }



        if (null == dlg) {
            //不存在，要创建
            dlg = DialogFactory.getDialog(activity, clazz);
            dialogMap.put(clazz,dlg);
        } else {
//            Class curClazz = dlg.getClass();
//            if (curClazz == clazz) {
//                //存在，复用
//                dlg.dismissOnly();//先关闭之前的
//            } else {
//                //存在，类型不同，新建一个以添加
//                dlg = DialogFactory.get(activity, clazz);
//            }
        }
        return (T)dlg;
    }

    public void dismissDlg(Activity activity,Class<? extends BaseDialog> clazz){
        ArrayMap<Class, BaseDialog> dialogMap = map2.get(activity);
        if (null!=dialogMap){
            BaseDialog dlg = dialogMap.get(clazz);
            if(null!=dlg){
                if (dlg.isShowing()){
                    dlg.dismiss();
                }
            }
        }
    }

    public void dismissAllDlgs(Activity activity) {
//        BaseDialog dlg = map.get(activity);
//        if (null != dlg && dlg.isShowing()) {
//            dlg.dismissOnly();
//        }
        ArrayMap<Class, BaseDialog> dialogMap = map2.get(activity);
        if (null!=dialogMap){
            BaseDialog dlg = null;
            Set<Map.Entry<Class, BaseDialog>> entries = dialogMap.entrySet();
            for (Map.Entry<Class, BaseDialog> entry:entries) {
                dlg = dialogMap.get(entry.getKey());
                if(null!=dlg){
                    if (dlg.isShowing()){
                        dlg.dismiss();
                    }
                }
            }
        }
    }

    public void destoryDlgs(Activity activity) {
        dismissAllDlgs(activity);
//        map.remove(activity);
        map2.remove(activity);
    }
}
