package com.bearever.business.creatediary;

import android.content.Context;

import com.bearever.baselib.mvp.BasePresenter;
import com.bearever.bean.CreateDiaryDO;

/**
 * luoming
 * 2019/3/30
 **/
public class CreateDiaryPresenter extends BasePresenter<CreateDiaryContact.View, CreateDiaryModel> implements
        CreateDiaryContact.Presenter {
    public CreateDiaryPresenter(CreateDiaryContact.View view, Context context) {
        super(view, context);
    }

    @Override
    public void post(CreateDiaryDO postData) {

    }
}
