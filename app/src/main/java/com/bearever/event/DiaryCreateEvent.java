package com.bearever.event;

import com.bearever.bean.CreateDiaryDO;

public class DiaryCreateEvent {
    public CreateDiaryDO data;

    public DiaryCreateEvent(CreateDiaryDO data) {
        this.data = data;
    }
}
