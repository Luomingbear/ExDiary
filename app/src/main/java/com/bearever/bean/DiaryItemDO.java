package com.bearever.bean;

import java.io.Serializable;

/**
 * 一条日记的数据对象
 * luoming
 * 2019/3/23
 **/
public class DiaryItemDO implements Serializable {
    private String time;
    private String content;

    public DiaryItemDO() {
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
}
