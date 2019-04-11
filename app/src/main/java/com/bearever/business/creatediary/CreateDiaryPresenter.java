package com.bearever.business.creatediary;

import android.content.Context;
import android.text.TextUtils;

import com.bearever.baselib.listener.EventCallBack;
import com.bearever.baselib.mvp.BasePresenter;
import com.bearever.baselib.ui.ToastHelper;
import com.bearever.bean.CreateDiaryDO;
import com.bearever.diarybase.database.sql.DiaryDB;
import com.bearever.diarybase.util.TimeUtils;
import com.bearever.event.DiaryCreateEvent;
import com.bearever.event.DiaryDeleteEvent;
import com.bearever.event.DiaryUpdateEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * luoming
 * 2019/3/30
 **/
public class CreateDiaryPresenter extends BasePresenter<CreateDiaryContact.View, CreateDiaryModel> implements
        CreateDiaryContact.Presenter {

    private boolean hasSaveLocal = false;

    public CreateDiaryPresenter(CreateDiaryContact.View view, Context context) {
        super(view, context);
    }

    @Override
    public void post(CreateDiaryDO postData) {
        if (getView() == null) {
            return;
        }

        if (!checkPostData(postData)) {
            return;
        }

        boolean publicDiary = false; //是否设置了公开
        if (publicDiary) {
            mModel.postNet(postData, new EventCallBack() {
                @Override
                public void onSuccess(Object o) {
                    //更新本地数据
                    saveLocal(postData);
                }

                @Override
                public void onFail(String msg) {
                    mView.postFailed(msg);
                }
            });
        } else {
            saveLocal(postData);
        }

    }

    private boolean checkPostData(CreateDiaryDO data) {
        if (data == null || data.isEmpty()) {
            ToastHelper.showToast(mContext, "请先编辑内容");
            return false;
        }
        return true;
    }

    @Override
    public void saveLocal(CreateDiaryDO postData) {
        if (getView() == null) {
            return;
        }

        if (postData.isEmpty()) {
            return;
        }

        mModel.saveLocal(postData, new EventCallBack() {
            @Override
            public void onSuccess(Object o) {
                hasSaveLocal = true;
                List<DiaryDB.DiaryDatabaseDO> results = (List<DiaryDB.DiaryDatabaseDO>) o;
                EventBus.getDefault().post(new DiaryCreateEvent(CreateDiaryDO.from(results.get(0))));
                mView.postSucceed();
            }

            @Override
            public void onFail(String msg) {
                hasSaveLocal = false;
                mView.postFailed(msg);
            }
        });
    }

    @Override
    public void updateLocal(CreateDiaryDO postData) {
        if (getView() == null) {
            return;
        }
        if (!checkPostData(postData)) {
            return;
        }

        hasSaveLocal = false;
        mModel.updateLocal(postData, new EventCallBack() {
            @Override
            public void onSuccess(Object o) {
                hasSaveLocal = true;
                List<DiaryDB.DiaryDatabaseDO> results = (List<DiaryDB.DiaryDatabaseDO>) o;
                EventBus.getDefault().post(new DiaryUpdateEvent(CreateDiaryDO.from(results.get(0))));
                mView.postSucceed();
            }

            @Override
            public void onFail(String msg) {
                hasSaveLocal = false;
                mView.postFailed(msg);
            }
        });
    }

    @Override
    public void onStopAndFinish(CreateDiaryDO saveData, boolean editModel) {
        if (saveData == null || getView() == null) {
            return;
        }

        if (hasSaveLocal) {
            return;
        }

        if (editModel) {
            if (saveData.isEmpty()) {
                //修改模式将内容置空，需要将这条数据删除
                mModel.deleteLocal(saveData, new EventCallBack() {
                    @Override
                    public void onSuccess(Object o) {
                        EventBus.getDefault().post(new DiaryDeleteEvent(saveData));
                    }

                    @Override
                    public void onFail(String msg) {

                    }
                });
            } else {
                updateLocal(saveData);
            }
        } else {
            saveLocal(saveData);
        }
    }

}
