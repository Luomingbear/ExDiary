package com.bearever.adapter;

import android.support.annotation.Nullable;

import com.bearever.bean.DiaryItemDO;
import com.bearever.diary.R;
import com.bearever.diarybase.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 我的日记的适配器
 * luoming
 * 2019/3/23
 **/
public class MyDiaryAdapter extends BaseQuickAdapter<DiaryItemDO, BaseViewHolder> {
    public MyDiaryAdapter(@Nullable List<DiaryItemDO> data) {
        super(R.layout.view_my_diary_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DiaryItemDO item) {
        helper.setText(R.id.tv_time, TimeUtils.getDiaryTime(item.getTime()));
        helper.setText(R.id.tv_content, item.getContent());
    }
}
