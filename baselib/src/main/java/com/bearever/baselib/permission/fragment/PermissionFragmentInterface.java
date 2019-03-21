package com.bearever.baselib.permission.fragment;


import com.bearever.baselib.permission.PermissionManager;

/**
 * 权限请求fragment开放的接口
 * 作者：luoming on 2018/9/18.
 * 邮箱：luomingbear@163.com
 * 版本：v1.0
 */
public interface PermissionFragmentInterface {
    boolean isApplyPermission(String permission);

    void requestPermission(PermissionManager.OnPermissionListener listener, boolean needDialog, int requestCode, String... permissions);

}
