package com.bearever.baselib.permission.info;

import com.bearever.baselib.permission.PermissionManager;

/**
 * 作者：luoming on 2018/9/19.
 * 邮箱：luomingbear@163.com
 * 版本：v1.0
 */
public class OnPermissionListenerInfo {
    private boolean needDialog;
    private String[] permission;
    private PermissionManager.OnPermissionListener listener;

    public OnPermissionListenerInfo(boolean needDialog, String[] permission,
                                    PermissionManager.OnPermissionListener listener) {
        this.needDialog = needDialog;
        this.permission = permission;
        this.listener = listener;
    }

    public boolean isNeedDialog() {
        return needDialog;
    }

    public void setNeedDialog(boolean needDialog) {
        this.needDialog = needDialog;
    }

    public String[] getPermission() {
        return permission;
    }

    public void setPermission(String[] permission) {
        this.permission = permission;
    }

    public PermissionManager.OnPermissionListener getListener() {
        return listener;
    }

    public void setListener(PermissionManager.OnPermissionListener listener) {
        this.listener = listener;
    }
}
