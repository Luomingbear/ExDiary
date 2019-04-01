package com.bearever.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 创建日记的数据
 * luoming
 * 2019/3/30
 **/
public class CreateDiaryDO implements Serializable {
    private Date time;
    private String content;
    private boolean exchange = false;

    public CreateDiaryDO() {
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isExchange() {
        return exchange;
    }

    public void setExchange(boolean exchange) {
        this.exchange = exchange;
    }
}
