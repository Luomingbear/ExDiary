package com.bearever.business.mydiary;

import android.content.Context;

import com.bearever.baselib.listener.EventCallBack;
import com.bearever.baselib.mvp.BasePresenter;
import com.bearever.baselib.ui.ToastHelper;
import com.bearever.bean.HttpMyDiaryDO;

/**
 * 我的日记的逻辑
 * luoming
 * 2019/3/23
 **/
public class MyDiaryPresenter extends BasePresenter<MyDiaryContact.View, MyDiaryModel>
        implements MyDiaryContact.Presenter {
    private int page = 0;

    public MyDiaryPresenter(MyDiaryContact.View view, Context context) {
        super(view, context);
    }

    @Override
    public void refresh() {
        page = 0;
        //获取本地记录
        mModel.getDiaryFromLocal(page, new EventCallBack() {
            @Override
            public void onSuccess(Object o) {
                HttpMyDiaryDO result = (HttpMyDiaryDO) o;
                mView.showDiary(result.getContent(), result.isHasNext());
            }

            @Override
            public void onFail(String msg) {
                ToastHelper.showToast(mContext, msg);
            }
        });

//获取网络记录
//        mModel.getDiaryFromNet(page, new EventCallBack() {
//            @Override
//            public void onSuccess(Object o) {
//                HttpMyDiaryDO result = (HttpMyDiaryDO) o;
//                mView.showDiary(result.getContent(), result.isHasNext());
//            }
//
//            @Override
//            public void onFail(String msg) {
//                ToastHelper.showToast(mContext, msg);
//            }
//        });
    }

    @Override
    public void loadMore() {
        page++;
        mModel.getDiaryFromLocal(page, new EventCallBack() {
            @Override
            public void onSuccess(Object o) {
                HttpMyDiaryDO result = (HttpMyDiaryDO) o;
                mView.showMoreDiary(result.getContent(), result.isHasNext());
            }

            @Override
            public void onFail(String msg) {
                ToastHelper.showToast(mContext, msg);
            }
        });
    }
}
