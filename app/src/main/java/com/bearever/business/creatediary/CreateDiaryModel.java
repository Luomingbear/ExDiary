package com.bearever.business.creatediary;

import android.text.TextUtils;

import com.bearever.articlememento.model.Section;
import com.bearever.articlememento.richtext.RichTextEditor;
import com.bearever.baselib.http.Convert;
import com.bearever.baselib.listener.EventCallBack;
import com.bearever.bean.CreateDiaryDO;
import com.bearever.diarybase.database.DiaryDBManager;
import com.bearever.diarybase.database.sql.DiaryDB;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * luoming
 * 2019/3/30
 **/
public class CreateDiaryModel implements CreateDiaryContact.Model {
    public CreateDiaryModel() {
    }

    @Override
    public void postNet(CreateDiaryDO postData, EventCallBack callBack) {

    }

    @Override
    public void saveLocal(CreateDiaryDO saveData, EventCallBack callBack) {
        DiaryDB.DiaryDatabaseDO data = convert(saveData);
        DiaryDBManager.getInstance().insert(data, new DiaryDBManager.OnDiaryDBListener() {
            @Override
            public void finish(List<DiaryDB.DiaryDatabaseDO> results, boolean next) {
                if (results != null) {
                    callBack.onSuccess(results);
                } else {
                    callBack.onFail("保存失败");
                }
            }
        });
    }

    @Override
    public void updateLocal(CreateDiaryDO saveData, EventCallBack callBack) {
        DiaryDB.DiaryDatabaseDO data = convert(saveData);
        DiaryDBManager.getInstance().update(data, new DiaryDBManager.OnDiaryDBListener() {
            @Override
            public void finish(List<DiaryDB.DiaryDatabaseDO> results, boolean next) {
                if (results != null) {
                    callBack.onSuccess(results);
                } else {
                    callBack.onFail("保存失败");
                }
            }
        });
    }

    @Override
    public void deleteLocal(CreateDiaryDO saveData, EventCallBack callBack) {
        DiaryDBManager.getInstance().delete(saveData.getDid(), new DiaryDBManager.OnDiaryDBListener() {
            @Override
            public void finish(List<DiaryDB.DiaryDatabaseDO> results, boolean next) {
                if (results != null) {
                    callBack.onSuccess(results);
                } else {
                    callBack.onFail("保存失败");
                }
            }
        });
    }

    private DiaryDB.DiaryDatabaseDO convert(CreateDiaryDO saveData) {
        DiaryDB.DiaryDatabaseDO data = new DiaryDB.DiaryDatabaseDO();
        // TODO: 2019/4/11 适配数据
        data.setDid(saveData.getDid());
        data.setContent(saveData.getContent());
        data.setTime(saveData.getTime());
        data.setTittle(saveData.getTitle());
        data.setExchange(saveData.isExchange());
        return data;
    }
}
