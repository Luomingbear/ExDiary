package com.bearever.baselib.permission.info;

/**
 *
 * 作者：luoming on 2018/9/19.
 * 邮箱：luomingbear@163.com
 * 版本：v1.0
 */
public class WaitPermissionInfo {
    private String[] permission;
    private int requestCode;

    public WaitPermissionInfo(String[] permission, int requestCode) {
        this.permission = permission;
        this.requestCode = requestCode;
    }

    public String[] getPermission() {
        return permission;
    }

    public void setPermission(String[] permission) {
        this.permission = permission;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }
}
