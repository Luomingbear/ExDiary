package com.bearever.event;

import com.bearever.bean.CreateDiaryDO;

public class DiaryDeleteEvent {
    public CreateDiaryDO data;

    public DiaryDeleteEvent(CreateDiaryDO data) {
        this.data = data;
    }
}
