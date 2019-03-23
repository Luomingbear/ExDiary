package com.bearever.baselib.listener;

/**
 * 逻辑处理事件回调
 * created by JiangHua on 2019/2/27
 */
public interface EventCallBack {
    void onSuccess(Object o);
    void onFail(String msg);
}
