package com.bearever.business.mydiary;

import android.content.Context;

import com.bearever.baselib.mvp.BasePresenter;

/**
 * 我的日记的逻辑
 * luoming
 * 2019/3/23
 **/
public class MyDiaryPresenter extends BasePresenter<MyDiaryContact.View, MyDiaryModel>
        implements MyDiaryContact.Presenter {
    public MyDiaryPresenter(MyDiaryContact.View view, Context context) {
        super(view, context);
    }

    @Override
    public void refresh() {

    }

    @Override
    public void loadMore() {

    }
}
