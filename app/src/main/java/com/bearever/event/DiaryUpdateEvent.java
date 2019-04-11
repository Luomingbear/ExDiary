package com.bearever.event;

import com.bearever.bean.CreateDiaryDO;

public class DiaryUpdateEvent {
    public CreateDiaryDO data;

    public DiaryUpdateEvent(CreateDiaryDO data) {
        this.data = data;
    }
}
