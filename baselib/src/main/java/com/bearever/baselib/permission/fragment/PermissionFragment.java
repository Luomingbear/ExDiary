package com.bearever.baselib.permission.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;


import com.bearever.baselib.permission.PermissionManager;
import com.bearever.baselib.permission.info.OnPermissionListenerInfo;
import com.bearever.baselib.permission.info.PermissionGrantInfo;
import com.bearever.baselib.permission.info.WaitPermissionInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * 用来请求权限的fragment
 * 作者：luoming on 2018/9/18.
 * 邮箱：luomingbear@163.com
 * 版本：v1.0
 */
public class PermissionFragment extends Fragment implements PermissionFragmentInterface {
    private static final String TAG = "PermissionFragment";
    private Map<Integer, OnPermissionListenerInfo> mListenerMap = new HashMap<>();
    private Map<Integer, WaitPermissionInfo> mWaitMap = new HashMap<>();

    @Override
    public boolean isApplyPermission(String permission) {
        if (TextUtils.isEmpty(permission) || getContext() == null) {
            return false;
        }
        return ContextCompat.checkSelfPermission(getContext(), permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void requestPermission(PermissionManager.OnPermissionListener listener, boolean needDialog,
                                  int requestCode, String... permissions) {
        if (Build.VERSION.SDK_INT < 23) {
            return;
        }
        mListenerMap.put(requestCode,
                new OnPermissionListenerInfo(needDialog, permissions, listener));
        Activity activity = getActivity();
        if (permissions != null) {
            if (activity != null) {
                //直接请求权限
                requestPermissions(permissions, requestCode);
            } else {
                //等待fragment初始化成功之后请求
                mWaitMap.put(requestCode, new WaitPermissionInfo(permissions, requestCode));
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() == null) {
            return;
        }
        //取出缓存的待请求权限，开始请求
        Iterator iterator = mWaitMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            WaitPermissionInfo info = (WaitPermissionInfo) entry.getValue();
            requestPermissions(info.getPermission(), info.getRequestCode());
            mWaitMap.remove(entry.getKey());
        }
    }

    /**
     * 处理权限请求之后的结果
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        OnPermissionListenerInfo info = mListenerMap.get(requestCode);
        if (info == null) {
            return;
        }

        boolean isAllAllow = true;
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                isAllAllow = false;
                break;
            }
        }

        if (isAllAllow || !info.isNeedDialog()) { //所有的权限都获取到了，或者不需要弹窗则表示权限请求结束了
            onFinish(info);
            return;
        }
        showNeedSettingDialog(requestCode, permissions, grantResults);
    }

    /**
     * 显示需要打开设置页面的弹窗
     *
     * @param requestCode
     * @param permissions
     */
    private void showNeedSettingDialog(int requestCode, @NonNull String[] permissions,
                                       @NonNull int[] grantResults) {
        if (permissions.length == 0) {
            return;
        }

        //通过遍历权限名，得到需要的权限说明，用于弹窗显示
        List<String> hintList = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                String hint = getPermissionHint(permissions[i]);
                if (!hintList.contains(hint)) {
                    hintList.add(hint);
                }
            }
        }

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < hintList.size(); i++) {
            sb.append(hintList.get(i));
            if (i < hintList.size() - 1) {
                sb.append(",");
            }
        }

        if (sb.length() == 0) { //如果没有匹配的权限说明，则返回固定说明
            sb.append("一些必备的权限");
        }

        openSettingActivity("应用运行需要" + sb + "，您可以在应用设置->权限管理，打开该权限。",
                requestCode);
    }

    /**
     * 获取权限的提示语
     *
     * @param p
     * @return
     */
    private String getPermissionHint(String p) {
        if (p.equals(Manifest.permission.ACCESS_FINE_LOCATION) ||
                p.equals(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            return "定位权限";
        } else if (p.equals(Manifest.permission.READ_EXTERNAL_STORAGE) ||
                p.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            return "文件读写权限";
        } else if (p.equals(Manifest.permission.CALL_PHONE)) {
            return "拨号权限";
        } else if (p.equals(Manifest.permission.READ_CONTACTS) ||
                p.equals(Manifest.permission.WRITE_CONTACTS)) {
            return "通讯录权限";
        } else if (p.equals(Manifest.permission.CAMERA)) {
            return "相机权限";
        }
        return "";
    }

    /**
     * 打开应用的设置界面
     *
     * @param message
     */
    private void openSettingActivity(String message, final int requestCode) {
        if (getContext() == null) {
            return;
        }

        new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        OnPermissionListenerInfo info = mListenerMap.get(requestCode);
                        onFinish(info);
                    }
                })
                .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //跳转到设置页面
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, requestCode);
                    }
                })
                .create()
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        OnPermissionListenerInfo info = mListenerMap.get(requestCode);
        if (info == null || info.getListener() == null) {
            return;
        }
        onFinish(info);
    }

    /**
     * 通知回调权限获取结果
     */
    private void onFinish(OnPermissionListenerInfo info) {
        List<PermissionGrantInfo> list = new ArrayList<>();
        boolean isAllAllow = true;
        if (info != null && info.getListener() != null) {
            for (String p : info.getPermission()) {
                if (isApplyPermission(p)) {
                    list.add(new PermissionGrantInfo(p, true));
                } else {
                    list.add(new PermissionGrantInfo(p, false));
                    isAllAllow = false;
                }
            }
            info.getListener().onFinish(list, isAllAllow);
        }
    }

    @Override
    public void onDestroy() {
        mListenerMap.clear();
        super.onDestroy();
    }
}
