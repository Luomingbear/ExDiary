package com.bearever.business.mydiary;

import com.bearever.baselib.listener.EventCallBack;
import com.bearever.bean.DiaryItemDO;
import com.bearever.bean.HttpMyDiaryDO;
import com.bearever.diarybase.database.DiaryDBManager;
import com.bearever.diarybase.database.sql.DiaryDB;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 我的日记的数据获取
 * luoming
 * 2019/3/23
 **/
public class MyDiaryModel implements MyDiaryContact.Model {
    public MyDiaryModel() {

    }

    @Override
    public void getDiaryFromLocal(int page, EventCallBack callBack) {
        DiaryDBManager.getInstance().getByPage(page, new DiaryDBManager.OnDiaryDBListener() {
            @Override
            public void finish(List<DiaryDB.DiaryDatabaseDO> results, boolean next) {
                if (results != null) {
                    HttpMyDiaryDO data = HttpMyDiaryDO.from(results);
                    data.setHasNext(next);
                    callBack.onSuccess(data);
                } else {
                    HttpMyDiaryDO result = new HttpMyDiaryDO();
                    result.setHasNext(false);
                    result.setContent(Collections.emptyList());
                    callBack.onSuccess(result);
                }
            }
        });
    }

    @Override
    public void getDiaryFromNet(int page, EventCallBack callBack) {
        List<DiaryItemDO> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DiaryItemDO item = new DiaryItemDO();
            item.setContent("你好嗷嗷嗷嗷哦嗷嗷嗷嗷嗷嗷" + i);
            item.setTime("2019-02-12 13:22:22");
            list.add(item);
        }
        HttpMyDiaryDO result = new HttpMyDiaryDO();
        result.setContent(list);
        callBack.onSuccess(result);
    }
}
