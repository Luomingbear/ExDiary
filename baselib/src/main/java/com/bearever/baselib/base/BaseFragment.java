package com.bearever.baselib.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;

/**
 * 基类fragment
 * created by JiangHua on 2019/2/26
 */
public abstract class BaseFragment<T extends BasePresenter> extends Fragment implements BaseView {
    public Context mContext;

    protected View mRootView;
    //是否已经创建过
    protected boolean mIsOnCreateView;

    public T mPresenter;

    /**
     * 布局的style id
     *
     * @return
     */
    protected int initStyleId() {
        return 0;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            this.mIsOnCreateView = true;
            getIntentArguments();
            mRootView = getRootView(inflater, container);
            initView();
        } else {
            this.mIsOnCreateView = false;
        }

        //如果该fragment 已经被加入过容器，从容器中移除
        ViewGroup localViewGroup = (ViewGroup) mRootView.getParent();
        if (localViewGroup != null) {
            localViewGroup.removeView(mRootView);
        }

        initPresenter();
//        mPresenter = MVPUtils.getT(this, 0);

        if (null != mPresenter) {
            mPresenter.mContext = mContext;
        }

        return mRootView;
    }

    protected View getRootView(LayoutInflater inflater, ViewGroup container) {
        if (initStyleId() == 0) {
            return inflater.inflate(initLayout(), container, false);
        } else {
            try {
                final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), initStyleId());
                LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
                return localInflater.inflate(initLayout(), null, false);
            } catch (Throwable e) {
                return inflater.inflate(initLayout(), container, false);
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //如果已经创建直接填充或者刷新数据
        if (mIsOnCreateView) {
            setView();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            //解决fragment找不到activity的问题
            Field localField = Fragment.class.getDeclaredField("mChildFragmentManager");
            localField.setAccessible(true);
            localField.set(this, null);
        } catch (NoSuchFieldException localNoSuchFieldException) {
            throw new RuntimeException(localNoSuchFieldException);
        } catch (IllegalAccessException localIllegalAccessException) {
            throw new RuntimeException(localIllegalAccessException);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mPresenter) {
            mPresenter.onDetach();
        }
    }

    /**
     * 重写findView 简化每次获取view 的时候都需要getView
     *
     * @param id
     * @return
     */
    public final View findViewById(int id) {
        if (id < 0 || mRootView == null) {
            return null;
        }
        return mRootView.findViewById(id);
    }


    @Override
    public void showLoading() {
        //DialogManager.get(getActivity(), LoadingDialog.class).show();

    }

    @Override
    public void hideLoading() {
//        DialogManager.dismissOnly(getActivity(), LoadingDialog.class);

    }

    /**
     * 布局的layout id
     *
     * @return
     */
    protected abstract int initLayout();

    /**
     * 获取页面参数
     */
    protected abstract void getIntentArguments();

    /*****
     * 初始化控件
     */
    protected abstract void initView();

    protected abstract void initPresenter();

    /**
     * 设置view数据
     */
    protected abstract void setView();
}
