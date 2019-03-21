package com.bearever.baselib.permission;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.bearever.baselib.permission.fragment.PermissionFragment;
import com.bearever.baselib.permission.info.PermissionGrantInfo;

import java.util.List;


/**
 * 权限管理
 * 判断权限<code>isApplyPermission</>方法在部分手机例如：锤子手机上面会一直返回true，他真实的权限请求在使用到权限的时候；
 * 请求权限只需要调用<code>applyPermission</>方法，不需要监听<code>onRequestPermissionsResult</>，工具内部进行了处理
 * 作者：luoming on 2018/9/17.
 * 邮箱：luomingbear@163.com
 * 版本：v1.0
 */
public class PermissionManager {
    private static final String TAG_FRAGMENT_PERMISSION = "TAG_FRAGMENT_PERMISSION";

    /**
     * 检查是否拥有权限
     *
     * @param context
     * @param permission 需要检查的权限
     * @return
     */
    public static boolean isApplyPermission(Context context, String permission) {
        if (context == null) {
            return false;
        }
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 检查是否拥有权限，强类型检查，针对有些机型请求权限后，返回的权限状态都为true
     * 检查系统安全中心的权限状态
     *
     * @param mContext
     * @param normalPermission 第三方系统权限名
     * @param appOpsPermission Android系统原生权限名
     * @return
     */
    public static boolean isApplyPermission(Context mContext, String normalPermission, String appOpsPermission) {
        if (mContext == null) {
            return false;
        }
        boolean checkNormalResult = false;
        boolean checkAppOpsResult = false;

        checkNormalResult = ContextCompat.checkSelfPermission(mContext, normalPermission) == PackageManager.PERMISSION_GRANTED;

        try {
            if (android.os.Build.VERSION.SDK_INT >= 19) {
                //安卓原生权限管理 适配小米安全中心等第三方
                AppOpsManager appOpsManager = (AppOpsManager) mContext.getSystemService(Context
                        .APP_OPS_SERVICE);
                int checkOp = appOpsManager.checkOp(appOpsPermission, android.os.Process.myUid(), mContext
                        .getPackageName());
                switch (checkOp) {
                    case AppOpsManager.MODE_ALLOWED:
                        Log.d("PermissionUtils", "权限:允许");
                        checkAppOpsResult = true;
                        break;
                    case AppOpsManager.MODE_IGNORED:
                        Log.d("PermissionUtils", "权限:拒绝");
                        checkAppOpsResult = false;
                        break;
                    case AppOpsManager.MODE_ERRORED:
                        Log.d("PermissionUtils", "权限:错误");
                        checkAppOpsResult = false;
                        break;
                    case 4:
                        Log.d("PermissionUtils", "权限:询问");
                        checkAppOpsResult = false;
                        break;
                    default:
                        checkAppOpsResult = false;
                        break;
                }
            }
        } catch (IllegalArgumentException e) {
            //没有检查到权限，默认为true
            checkAppOpsResult = true;
        }


        return checkNormalResult && checkAppOpsResult;
    }

    /**
     * 检查OPSmanager是否授权，用于MIUI安全中心
     *
     * @param mContext
     * @param appOpsPermission Android系统原生权限名
     * @return
     */
    public static boolean isApplyOPSPermission(Context mContext, String appOpsPermission) {
        if (mContext == null) {
            return false;
        }
        boolean checkAppOpsResult = false;
        try {
            if (android.os.Build.VERSION.SDK_INT >= 19) {
                //安卓原生权限管理 适配小米安全中心等第三方
                AppOpsManager appOpsManager = (AppOpsManager) mContext.getSystemService(Context
                        .APP_OPS_SERVICE);
                int checkOp = appOpsManager.checkOp(appOpsPermission, android.os.Process.myUid(), mContext
                        .getPackageName());
                switch (checkOp) {
                    case AppOpsManager.MODE_ALLOWED:
                        Log.d("PermissionUtils", "权限:允许");
                        checkAppOpsResult = true;
                        break;
                    case AppOpsManager.MODE_IGNORED:
                        Log.d("PermissionUtils", "权限:拒绝");
                        checkAppOpsResult = false;
                        break;
                    case AppOpsManager.MODE_ERRORED:
                        Log.d("PermissionUtils", "权限:错误");
                        checkAppOpsResult = false;
                        break;
                    case 4:
                        Log.d("PermissionUtils", "权限:询问");
                        checkAppOpsResult = false;
                        break;
                    default:
                        checkAppOpsResult = false;
                        break;
                }
            }
        } catch (IllegalArgumentException e) {
            checkAppOpsResult = true;
        }
        return checkAppOpsResult;
    }


    /**
     * 检查是否拥有权限
     *
     * @param context
     * @param permissions 需要检查的权限
     * @return
     */
    public static boolean isApplyPermissions(Context context, String[] permissions) {
        if (context == null) {
            return false;
        }
        for (String p : permissions) {
            if (!isApplyPermission(context, p)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 请求权限
     *
     * @param fragmentManager 当前页的FragmentManager
     * @param permissions     需要的权限
     * @param permissionCode  请求标识码
     * @param listener        权限获取情况的回调
     * @param needDialog      权限获取失败是否需要弹窗提示打开设置页面
     */
    public static void applyPermission(FragmentManager fragmentManager, int permissionCode,
                                       OnPermissionListener listener, boolean needDialog,
                                       String... permissions) {
        Fragment fragment = fragmentManager.findFragmentByTag(TAG_FRAGMENT_PERMISSION);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (fragment == null) {
            fragment = new PermissionFragment();
            fragmentTransaction.add(fragment, TAG_FRAGMENT_PERMISSION);
        }
        if (fragment instanceof PermissionFragment) {
            fragmentTransaction.show(fragment).commit();
            ((PermissionFragment) fragment).requestPermission(listener, needDialog, permissionCode,
                    permissions);
        }
    }

    /**
     * 请求权限
     *
     * @param activity       当前页的fragmentActivity
     * @param permissions    需要的权限
     * @param permissionCode 请求标识码
     * @param listener       权限获取情况的回调
     * @param needDialog     权限获取失败是否需要弹窗提示打开设置页面
     */
    public static void applyPermission(AppCompatActivity activity, int permissionCode,
                                       OnPermissionListener listener, boolean needDialog,
                                       String... permissions) {
        applyPermission(activity.getSupportFragmentManager(), permissionCode, listener, needDialog, permissions);
    }

    /**
     * 请求权限
     *
     * @param fragment       当前页的fragment
     * @param permissions    需要的权限
     * @param permissionCode 请求标识码
     * @param listener       权限获取情况的回调
     * @param needDialog     权限获取失败是否需要弹窗提示打开设置页面
     */
    public static void applyPermission(Fragment fragment, int permissionCode,
                                       OnPermissionListener listener, boolean needDialog,
                                       String... permissions) {
        applyPermission(fragment.getFragmentManager(), permissionCode, listener, needDialog, permissions);
    }

    public interface OnPermissionListener {
        void onFinish(List<PermissionGrantInfo> grantList, boolean isAllAllow);
    }
}
