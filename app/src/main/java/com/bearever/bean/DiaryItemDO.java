package com.bearever.bean;

import java.io.Serializable;

/**
 * 一条日记的数据对象
 * luoming
 * 2019/3/23
 **/
public class DiaryItemDO implements Serializable {
    private int did;
    private String time;
    private String content;
    private String title;
    private boolean exchange;

    public DiaryItemDO() {
    }

    public int getDid() {
        return did;
    }

    public void setDid(int did) {
        this.did = did;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isExchange() {
        return exchange;
    }

    public void setExchange(boolean exchange) {
        this.exchange = exchange;
    }

    public static DiaryItemDO from(CreateDiaryDO diaryDO) {
        if (diaryDO == null) {
            return null;
        }
        DiaryItemDO result = new DiaryItemDO();
        result.setDid(diaryDO.getDid());
        result.setExchange(diaryDO.isExchange());
        result.setTitle(diaryDO.getTitle());
        result.setTime(diaryDO.getTime());
        result.setContent(diaryDO.getContent());
        return result;
    }
}
