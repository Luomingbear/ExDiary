package com.bearever.bean;

import com.bearever.diarybase.database.sql.DiaryDB;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取网络的我的日记的数据
 * luoming
 * 2019/3/30
 **/
public class HttpMyDiaryDO {
    private List<DiaryItemDO> content;
    private boolean hasNext;

    public List<DiaryItemDO> getContent() {
        return content;
    }

    public void setContent(List<DiaryItemDO> content) {
        this.content = content;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public static HttpMyDiaryDO from(List<DiaryDB.DiaryDatabaseDO> results) {
        if (results == null) {
            return null;
        }
        List<DiaryItemDO> list = new ArrayList<>();
        for (DiaryDB.DiaryDatabaseDO item : results) {
            DiaryItemDO diaryItemDO = new DiaryItemDO();
            diaryItemDO.setDid(item.getDid());
            diaryItemDO.setContent(item.getContent());
            diaryItemDO.setTime(item.getTime());
            diaryItemDO.setTitle(item.getTittle());
            diaryItemDO.setExchange(item.isExchange());
            list.add(diaryItemDO);
        }
        HttpMyDiaryDO httpMyDiaryDO = new HttpMyDiaryDO();
        httpMyDiaryDO.setContent(list);
        httpMyDiaryDO.setHasNext(!results.isEmpty());
        return httpMyDiaryDO;
    }
}
