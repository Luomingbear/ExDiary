package com.bearever.baselib.base;

import android.content.Context;

import com.bearever.baselib.util.MVPUtils;

import java.lang.ref.WeakReference;

/**
 * 控制层抽取
 * T代表View E代表Model
 * 在这里会给view 和model解耦
 * created by JiangHua on 2019/2/25
 */
public abstract  class BasePresenter<T,E> {
    public Context mContext;
    public T mView;
    public E mModel;
    public WeakReference<T> mViewRef;

    public BasePresenter(T view, Context context) {
        mViewRef = new WeakReference<T>(view);
        this.mContext = context;
        this.mView = view;
        this.mModel = MVPUtils.getT(this, 1);
    }


    public T getView() {
        if (isAttach()) {
            return mViewRef.get();
        } else {
            return null;
        }
    }

    public boolean isAttach() {
        return null != mViewRef && null != mViewRef.get();
    }


    public void onDetach() {
        if (null != mViewRef) {
            mViewRef.clear();
            mViewRef = null;
        }
    }
}
