package com.bearever.business.receivediary;

import android.content.Context;

import com.bearever.baselib.mvp.BasePresenter;

/**
 * 收到的日记页面 的逻辑
 * luoming
 * 2019/3/30
 **/
public class ReceiveDiaryPresenter extends BasePresenter<ReceiveDiaryContact.View, ReceiveDiaryModel>
        implements ReceiveDiaryContact.Presenter {
    public ReceiveDiaryPresenter(ReceiveDiaryContact.View view, Context context) {
        super(view, context);
    }
}
