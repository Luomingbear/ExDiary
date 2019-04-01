package com.bearever.bean;

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
}
