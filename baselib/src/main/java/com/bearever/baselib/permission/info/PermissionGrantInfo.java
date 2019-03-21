package com.bearever.baselib.permission.info;

/**
 * 单个权限获取的结果
 * 作者：luoming on 2018/9/19.
 * 邮箱：luomingbear@163.com
 * 版本：v1.0
 */
public class PermissionGrantInfo {
    private String permission; //权限
    private boolean isAllow; //是否获取成功

    public PermissionGrantInfo(String permission, boolean isAllow) {
        this.permission = permission;
        this.isAllow = isAllow;
    }

    public String getPermission() {
        return permission;
    }

    public boolean isAllow() {
        return isAllow;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public void setAllow(boolean allow) {
        isAllow = allow;
    }
}
